package com.kurdikeyboard.predict;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * Read-only word list loaded from assets/dict/&lt;lang&gt;.dict ("word\tfreq", freq 1..255,
 * most frequent first). Words are indexed by their normalized key so prefix lookups
 * are a binary search.
 */
public final class WordDictionary {

    public final String lang;
    private final String[] words;
    private final String[] keys;
    private final int[] freqs;
    /** indices of words, sorted by key */
    private final int[] sorted;
    private final String[] sortedKeys;
    private final HashMap<String, Integer> exact = new HashMap<>();

    WordDictionary(String lang, List<String> w, List<Integer> f) {
        this.lang = lang;
        int n = w.size();
        words = w.toArray(new String[n]);
        freqs = new int[n];
        keys = new String[n];
        for (int i = 0; i < n; i++) {
            freqs[i] = f.get(i);
            keys[i] = TextNormalizer.key(words[i]);
        }
        Integer[] idx = new Integer[n];
        for (int i = 0; i < n; i++) idx[i] = i;
        Arrays.sort(idx, new Comparator<Integer>() {
            @Override public int compare(Integer a, Integer b) {
                int c = keys[a].compareTo(keys[b]);
                return c != 0 ? c : a - b;
            }
        });
        sorted = new int[n];
        sortedKeys = new String[n];
        for (int i = 0; i < n; i++) {
            sorted[i] = idx[i];
            sortedKeys[i] = keys[idx[i]];
        }
        // the file is ordered by frequency, so the first spelling of a key is the most common
        for (int i = n - 1; i >= 0; i--) exact.put(keys[i], i);
    }

    public static WordDictionary load(Context ctx, String lang) throws IOException {
        return load(lang, ctx.getAssets().open("dict/" + lang + ".dict"));
    }

    static WordDictionary load(String lang, java.io.InputStream in) throws IOException {
        List<String> w = new ArrayList<>(60000);
        List<Integer> f = new ArrayList<>(60000);
        BufferedReader r = new BufferedReader(new InputStreamReader(in, "UTF-8"), 1 << 16);
        try {
            String line;
            while ((line = r.readLine()) != null) {
                int tab = line.indexOf('\t');
                if (tab <= 0) continue;
                w.add(line.substring(0, tab));
                int v;
                try {
                    v = Integer.parseInt(line.substring(tab + 1).trim());
                } catch (NumberFormatException e) {
                    v = 1;
                }
                f.add(v);
            }
        } finally {
            r.close();
        }
        return new WordDictionary(lang, w, f);
    }

    public int size() { return words.length; }

    /** index of the most frequent word with this exact key, or -1 */
    public int indexOfKey(String key) {
        Integer i = exact.get(key);
        return i == null ? -1 : i;
    }

    public String word(int i) { return words[i]; }
    public String keyOf(int i) { return keys[i]; }
    public int freq(int i) { return freqs[i]; }

    /** [from, to) range, in sorted order, of keys starting with prefix */
    public int[] prefixRange(String prefix) {
        int lo = lowerBound(prefix);
        int hi = lowerBound(prefix + '￿');
        return new int[]{lo, hi};
    }

    /** word index at a position of the sorted order */
    public int wordAtSorted(int pos) { return sorted[pos]; }

    private int lowerBound(String k) {
        int lo = 0, hi = sortedKeys.length;
        while (lo < hi) {
            int mid = (lo + hi) >>> 1;
            if (sortedKeys[mid].compareTo(k) < 0) lo = mid + 1; else hi = mid;
        }
        return lo;
    }
}
