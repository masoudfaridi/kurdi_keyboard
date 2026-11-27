package com.kurdikeyboard.predict;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Words and word pairs the user has typed, per language. Stored in the app's private
 * files dir (it never leaves the device). Used to boost suggestions and for next-word
 * prediction.
 */
final class UserHistory {

    private static final int MAX_WORDS = 20000;
    private static final int MAX_BIGRAMS = 30000;
    private static final char SEP = '\u0001';

    private final File file;
    private final HashMap<String, Integer> words = new HashMap<>();   // spelling -> count
    private final HashMap<String, Integer> bigrams = new HashMap<>(); // prevKey SEP spelling -> count
    private boolean dirty;

    UserHistory(File dir, String lang) {
        file = new File(dir, "history_" + lang + ".txt");
        load();
    }

    private void load() {
        if (!file.exists()) return;
        BufferedReader r = null;
        try {
            r = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
            String line;
            while ((line = r.readLine()) != null) {
                String[] p = line.split("\t");
                try {
                    if (p.length == 3 && p[0].equals("w")) {
                        words.put(p[1], Integer.parseInt(p[2]));
                    } else if (p.length == 4 && p[0].equals("b")) {
                        bigrams.put(p[1] + SEP + p[2], Integer.parseInt(p[3]));
                    }
                } catch (NumberFormatException ignored) {
                }
            }
        } catch (IOException ignored) {
        } finally {
            if (r != null) try { r.close(); } catch (IOException ignored) {}
        }
    }

    synchronized void learn(String prev, String word) {
        if (word == null || word.length() == 0) return;
        Integer c = words.get(word);
        words.put(word, c == null ? 1 : Math.min(c + 1, 100000));
        if (prev != null && prev.length() > 0) {
            String k = TextNormalizer.key(prev) + SEP + word;
            Integer b = bigrams.get(k);
            bigrams.put(k, b == null ? 1 : Math.min(b + 1, 100000));
        }
        dirty = true;
        if (words.size() > MAX_WORDS) prune(words, MAX_WORDS * 3 / 4);
        if (bigrams.size() > MAX_BIGRAMS) prune(bigrams, MAX_BIGRAMS * 3 / 4);
    }

    synchronized void forget(String word) {
        words.remove(word);
        Iterator<String> it = bigrams.keySet().iterator();
        String suffix = SEP + word;
        while (it.hasNext()) {
            if (it.next().endsWith(suffix)) it.remove();
        }
        dirty = true;
    }

    synchronized int count(String word) {
        Integer c = words.get(word);
        return c == null ? 0 : c;
    }

    /** a snapshot of all learned words with their counts */
    synchronized List<Map.Entry<String, Integer>> allWords() {
        return new ArrayList<Map.Entry<String, Integer>>(
                new HashMap<String, Integer>(words).entrySet());
    }

    /** words that followed prev, unsorted */
    synchronized List<Map.Entry<String, Integer>> following(String prev) {
        String p = TextNormalizer.key(prev) + SEP;
        List<Map.Entry<String, Integer>> out = new ArrayList<>();
        for (Map.Entry<String, Integer> e : bigrams.entrySet()) {
            if (e.getKey().startsWith(p)) {
                out.add(new AbstractMap.SimpleEntry<>(e.getKey().substring(p.length()), e.getValue()));
            }
        }
        return out;
    }

    private static void prune(HashMap<String, Integer> m, int keep) {
        List<Map.Entry<String, Integer>> l = new ArrayList<>(m.entrySet());
        Collections.sort(l, new Comparator<Map.Entry<String, Integer>>() {
            @Override public int compare(Map.Entry<String, Integer> a, Map.Entry<String, Integer> b) {
                return b.getValue() - a.getValue();
            }
        });
        m.clear();
        for (int i = 0; i < Math.min(keep, l.size()); i++) m.put(l.get(i).getKey(), l.get(i).getValue());
    }

    synchronized void save() {
        if (!dirty) return;
        File tmp = new File(file.getPath() + ".tmp");
        Writer w = null;
        try {
            w = new OutputStreamWriter(new FileOutputStream(tmp), "UTF-8");
            for (Map.Entry<String, Integer> e : words.entrySet()) {
                w.write("w\t" + e.getKey() + "\t" + e.getValue() + "\n");
            }
            for (Map.Entry<String, Integer> e : bigrams.entrySet()) {
                int sep = e.getKey().indexOf(SEP);
                w.write("b\t" + e.getKey().substring(0, sep) + "\t" + e.getKey().substring(sep + 1)
                        + "\t" + e.getValue() + "\n");
            }
            w.close();
            w = null;
            if (!tmp.renameTo(file)) {
                file.delete();
                tmp.renameTo(file);
            }
            dirty = false;
        } catch (IOException ignored) {
        } finally {
            if (w != null) try { w.close(); } catch (IOException ignored) {}
        }
    }

    synchronized void clear() {
        words.clear();
        bigrams.clear();
        dirty = true;
        save();
    }
}
