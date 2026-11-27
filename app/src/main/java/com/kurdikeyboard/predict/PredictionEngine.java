package com.kurdikeyboard.predict;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Word prediction for English ("en"), Persian ("fa") and Central Kurdish / Sorani ("ckb").
 *
 * Dictionaries load on a background thread; until a language is loaded its suggestions
 * are simply empty. Everything the user types is learned locally (UserHistory) and boosts
 * later suggestions and next-word predictions.
 */
public final class PredictionEngine {

    private static final String TAG = "PredictionEngine";
    public static final String EN = "en", FA = "fa", CKB = "ckb";

    private static PredictionEngine sInstance;

    private final Context app;      // null in unit tests
    private final java.io.File filesDir;
    private final ExecutorService bg = Executors.newSingleThreadExecutor();
    private final ConcurrentHashMap<String, WordDictionary> dicts = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Boolean> loading = new ConcurrentHashMap<>();
    private final HashMap<String, UserHistory> histories = new HashMap<>();

    public static synchronized PredictionEngine get(Context ctx) {
        if (sInstance == null) sInstance = new PredictionEngine(ctx.getApplicationContext());
        return sInstance;
    }

    private PredictionEngine(Context app) {
        this.app = app;
        this.filesDir = app.getFilesDir();
    }

    /** for unit tests: no Android context, dictionaries are supplied directly */
    PredictionEngine(java.io.File filesDir) {
        this.app = null;
        this.filesDir = filesDir;
    }

    void putDictionary(WordDictionary d) {
        dicts.put(d.lang, d);
    }

    /** Starts loading a language in the background (no-op if loaded or loading). */
    public void preload(final String lang) {
        if (app == null || dicts.containsKey(lang) || loading.putIfAbsent(lang, Boolean.TRUE) != null) return;
        bg.execute(new Runnable() {
            @Override public void run() {
                try {
                    long t = System.currentTimeMillis();
                    dicts.put(lang, WordDictionary.load(app, lang));
                    history(lang);
                    Log.i(TAG, "loaded " + lang + " in " + (System.currentTimeMillis() - t) + " ms");
                } catch (Throwable e) {
                    Log.e(TAG, "cannot load dictionary " + lang, e);
                } finally {
                    loading.remove(lang);
                }
            }
        });
    }

    /** The dictionary, or null while it is still loading. */
    public WordDictionary dictionary(String lang) {
        WordDictionary d = dicts.get(lang);
        if (d == null) preload(lang);
        return d;
    }

    synchronized UserHistory history(String lang) {
        UserHistory h = histories.get(lang);
        if (h == null) {
            h = new UserHistory(filesDir, lang);
            histories.put(lang, h);
        }
        return h;
    }

    /** Picks the dictionary language for a word typed while the keyboard is in {@code keyboardLang}. */
    public static String languageFor(String keyboardLang, CharSequence word) {
        if (word != null && word.length() > 0) {
            if (TextNormalizer.hasLatinLetter(word)) return EN;
            boolean arabic = false;
            for (int i = 0; i < word.length(); i++) {
                if (TextNormalizer.isArabicScript(word.charAt(i))) { arabic = true; break; }
            }
            if (arabic) {
                if (TextNormalizer.hasKurdishOnlyLetter(word)) return CKB;
                return CKB.equals(keyboardLang) ? CKB : FA;
            }
        }
        return keyboardLang;
    }

    // ------------------------------------------------------------------ learning

    public void learn(String lang, String prev, String word) {
        if (word == null || word.length() < 2 && !word.equals("و") && !word.equalsIgnoreCase("i")
                && !word.equalsIgnoreCase("a")) return;
        String w = EN.equals(lang) ? normalizeEnglishCase(word) : word;
        history(lang).learn(prev, w);
    }

    public void forget(String lang, String word) {
        history(lang).forget(EN.equals(lang) ? normalizeEnglishCase(word) : word);
    }

    public void clearHistory() {
        for (String l : new String[]{EN, FA, CKB}) history(l).clear();
    }

    public void saveAsync() {
        bg.execute(new Runnable() {
            @Override public void run() {
                synchronized (PredictionEngine.this) {
                    for (UserHistory h : histories.values()) h.save();
                }
            }
        });
    }

    /** true if the word is in the dictionary or typed by the user at least twice */
    public boolean isKnown(String lang, String word) {
        WordDictionary d = dicts.get(lang);
        if (d != null && d.indexOfKey(TextNormalizer.key(word)) >= 0) return true;
        return history(lang).count(EN.equals(lang) ? normalizeEnglishCase(word) : word) >= 2;
    }

    /** "Hello" stays "Hello" only if the dictionary has no lowercase form */
    private String normalizeEnglishCase(String w) {
        String lower = w.toLowerCase(Locale.ROOT);
        if (lower.equals(w)) return w;
        WordDictionary d = dicts.get(EN);
        if (d != null && d.indexOfKey(lower) >= 0) return lower;
        // sentence-initial capital: learn the lower-case word
        if (w.length() > 1 && w.substring(1).equals(w.substring(1).toLowerCase(Locale.ROOT))) return lower;
        return w;
    }

    // ------------------------------------------------------------------ suggestions

    private static final class Cand {
        final String word;
        double score;
        Cand(String w, double s) { word = w; score = s; }
    }

    /**
     * Suggestions for the word being typed.
     *
     * @param prev  the word before the current one (may be null)
     * @param typed the part of the current word left of the cursor ("" → next-word prediction)
     * @return at most {@code max} words, best first. The typed word itself is included
     * (as the first entry) when it is not a known word, so the user can keep it.
     */
    public List<String> suggest(String lang, String prev, String typed, int max) {
        List<String> out = new ArrayList<>();
        WordDictionary dict = dictionary(lang);
        UserHistory hist = history(lang);

        if (typed == null || typed.length() == 0) {
            if (prev == null || prev.length() == 0) return out;
            List<Map.Entry<String, Integer>> next = hist.following(prev);
            Collections.sort(next, new Comparator<Map.Entry<String, Integer>>() {
                @Override public int compare(Map.Entry<String, Integer> a, Map.Entry<String, Integer> b) {
                    return b.getValue() - a.getValue();
                }
            });
            for (Map.Entry<String, Integer> e : next) {
                if (out.size() >= max) break;
                out.add(e.getKey());
            }
            return out;
        }

        String key = TextNormalizer.key(typed);
        if (key.length() == 0) return out;
        HashMap<String, Cand> cands = new HashMap<>();
        HashMap<String, Integer> nextCounts = new HashMap<>();
        if (prev != null && prev.length() > 0) {
            for (Map.Entry<String, Integer> e : hist.following(prev)) nextCounts.put(e.getKey(), e.getValue());
        }

        if (dict != null) {
            // 1. completions
            int[] r = dict.prefixRange(key);
            for (int pos = r[0]; pos < r[1]; pos++) {
                int i = dict.wordAtSorted(pos);
                String w = dict.word(i);
                double s = dict.freq(i) + userBoost(hist.count(w), nextCounts.get(w));
                String k = dict.keyOf(i);
                if (k.equals(key)) s += 35;                               // exactly what was typed
                else s -= Math.min(30, 3 * (k.length() - key.length())); // prefer shorter completions
                add(cands, w, s);
            }
            // 2. corrections (one or two typos)
            if (key.length() >= 2) corrections(dict, hist, key, nextCounts, cands);
        }

        // 3. words the user typed before
        for (Map.Entry<String, Integer> e : hist.allWords()) {
            String w = e.getKey();
            String k = TextNormalizer.key(w);
            if (!k.startsWith(key)) continue;
            boolean inDict = dict != null && dict.indexOfKey(k) >= 0;
            if (!inDict && e.getValue() < 2) continue;   // unknown words count after the 2nd use
            double s = 120 + userBoost(e.getValue(), nextCounts.get(w));
            if (k.equals(key)) s += 35;
            add(cands, w, s);
        }

        List<Cand> list = new ArrayList<>(cands.values());
        Collections.sort(list, new Comparator<Cand>() {
            @Override public int compare(Cand a, Cand b) { return Double.compare(b.score, a.score); }
        });

        boolean known = isKnown(lang, typed);
        if (!known) out.add(typed);
        for (Cand c : list) {
            if (out.size() >= max) break;
            String w = applyCase(lang, typed, c.word);
            if (!out.contains(w)) out.add(w);
        }
        return out;
    }

    private static double userBoost(int count, Integer bigram) {
        double s = 0;
        if (count > 0) s += Math.min(70, 25 * Math.log(1 + count) / Math.log(2));
        if (bigram != null && bigram > 0) s += Math.min(90, 40 * Math.log(1 + bigram) / Math.log(2));
        return s;
    }

    private static void add(HashMap<String, Cand> m, String w, double s) {
        Cand c = m.get(w);
        if (c == null) m.put(w, new Cand(w, s));
        else if (s > c.score) c.score = s;
    }

    private void corrections(WordDictionary dict, UserHistory hist, String key,
                             HashMap<String, Integer> nextCounts, HashMap<String, Cand> cands) {
        int maxDist = key.length() >= 6 ? 2 : 1;
        // typos rarely hit the first letter: scan words starting with the same letter
        int[] r = dict.prefixRange(key.substring(0, 1));
        int scanned = 0;
        for (int pos = r[0]; pos < r[1] && scanned < 15000; pos++, scanned++) {
            int i = dict.wordAtSorted(pos);
            String k = dict.keyOf(i);
            if (Math.abs(k.length() - key.length()) > maxDist) continue;
            if (k.startsWith(key)) continue; // already a completion
            int d = distance(key, k, maxDist);
            if (d > maxDist) continue;
            String w = dict.word(i);
            double penalty = key.length() <= 3 ? 120 : key.length() <= 5 ? 85 : 70;
            double s = dict.freq(i) - penalty * d + userBoost(hist.count(w), nextCounts.get(w));
            add(cands, w, s);
        }
    }

    /** Damerau-Levenshtein distance with early exit once it exceeds max */
    static int distance(String a, String b, int max) {
        int n = a.length(), m = b.length();
        if (Math.abs(n - m) > max) return max + 1;
        int[] prev2 = new int[m + 1], prev = new int[m + 1], cur = new int[m + 1];
        for (int j = 0; j <= m; j++) prev[j] = j;
        for (int i = 1; i <= n; i++) {
            cur[0] = i;
            int rowMin = cur[0];
            char ca = a.charAt(i - 1);
            for (int j = 1; j <= m; j++) {
                char cb = b.charAt(j - 1);
                int cost = ca == cb ? 0 : 1;
                int v = Math.min(Math.min(prev[j] + 1, cur[j - 1] + 1), prev[j - 1] + cost);
                if (i > 1 && j > 1 && ca == b.charAt(j - 2) && a.charAt(i - 2) == cb) {
                    v = Math.min(v, prev2[j - 2] + 1);
                }
                cur[j] = v;
                if (v < rowMin) rowMin = v;
            }
            if (rowMin > max) return max + 1;
            int[] t = prev2; prev2 = prev; prev = cur; cur = t;
        }
        return prev[m];
    }

    /** Keeps the case the user started with: "Hel" → "Hello", "HEL" → "HELLO". */
    public static String applyCase(String lang, String typed, String word) {
        if (!EN.equals(lang) || typed.length() == 0) return word;
        boolean firstUpper = Character.isUpperCase(typed.charAt(0));
        boolean allUpper = typed.length() > 1 && typed.equals(typed.toUpperCase(Locale.ROOT))
                && !typed.equals(typed.toLowerCase(Locale.ROOT));
        if (allUpper) return word.toUpperCase(Locale.ROOT);
        if (firstUpper) return TextNormalizer.capitalize(word);
        return word;
    }
}
