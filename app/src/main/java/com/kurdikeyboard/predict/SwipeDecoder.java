package com.kurdikeyboard.predict;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Turns a swipe (glide) gesture over the letter keys into words.
 *
 * Shape matching in the spirit of SHARK2: for every dictionary word that starts near the
 * first touch point and ends near the last one, the "ideal" path through its key centres
 * is compared with the finger's path (both resampled to the same number of points).
 * Words whose letters the finger never came near are rejected early. The final score
 * mixes the shape distance with word frequency and what the user typed before.
 *
 * Coordinates are measured in key units (x / key width, y / key height) so the same
 * thresholds work on every screen size.
 */
public final class SwipeDecoder {

    private static final int N = 48;            // resampling points
    private static final float END_RADIUS = 1.25f;
    private static final float PASS_RADIUS = 1.1f;

    private final HashMap<Character, float[]> centers = new HashMap<>();
    private final float keyW, keyH;

    /**
     * @param codes    primary + alternative codes of each key
     * @param rects    {x, y, width, height} of each key
     */
    public SwipeDecoder(List<int[]> codes, List<int[]> rects) {
        List<Integer> widths = new ArrayList<>(), heights = new ArrayList<>();
        // primary codes first, so the key that shows a letter wins over a long-press alternative
        for (int pass = 0; pass < 2; pass++) {
            for (int k = 0; k < codes.size(); k++) {
                int[] c = codes.get(k);
                int[] r = rects.get(k);
                if (c == null || c.length == 0 || c[0] <= 0 || !Character.isLetter(c[0])) continue;
                if (pass == 0) { widths.add(r[2]); heights.add(r[3]); }
                for (int j = (pass == 0 ? 0 : 1); j < (pass == 0 ? 1 : c.length); j++) {
                    if (c[j] <= 0 || !Character.isLetter(c[j])) continue;
                    String nk = TextNormalizer.key(String.valueOf((char) c[j]));
                    if (nk.length() != 1) continue;
                    char ch = nk.charAt(0);
                    if (!centers.containsKey(ch)) {
                        centers.put(ch, new float[]{r[0] + r[2] / 2f, r[1] + r[3] / 2f});
                    }
                }
            }
        }
        keyW = median(widths, 1);
        keyH = median(heights, 1);
        for (float[] p : centers.values()) { p[0] /= keyW; p[1] /= keyH; }
    }

    public boolean isUsable() { return centers.size() >= 10; }

    public float keyWidth() { return keyW; }

    private static float median(List<Integer> l, float def) {
        if (l.isEmpty()) return def;
        Collections.sort(l);
        return Math.max(1, l.get(l.size() / 2));
    }

    private static final class Scored {
        final String word; final double score;
        Scored(String w, double s) { word = w; score = s; }
    }

    /** Best words for the gesture, best first (empty if nothing fits). */
    public List<String> decode(float[] xs, float[] ys, int n, WordDictionary dict,
                               PredictionEngine engine, String lang, String prevWord, int max) {
        List<String> out = new ArrayList<>();
        if (dict == null || n < 2) return out;

        // finger path in key units, without duplicate points
        float[] px = new float[n], py = new float[n];
        int m = 0;
        for (int i = 0; i < n; i++) {
            float x = xs[i] / keyW, y = ys[i] / keyH;
            if (m > 0 && Math.abs(x - px[m - 1]) < 0.02f && Math.abs(y - py[m - 1]) < 0.02f) continue;
            px[m] = x; py[m] = y; m++;
        }
        if (m < 2) return out;
        float pathLen = length(px, py, m);
        if (pathLen < 0.8f) return out;
        float[][] user = resample(px, py, m, N);

        Map<Character, Float> startSet = near(px[0], py[0]);
        Map<Character, Float> endSet = near(px[m - 1], py[m - 1]);
        if (startSet.isEmpty() || endSet.isEmpty()) return out;

        UserHistory hist = engine.history(lang);
        HashMap<String, Integer> nextCounts = new HashMap<>();
        if (prevWord != null && prevWord.length() > 0) {
            for (Map.Entry<String, Integer> e : hist.following(prevWord)) nextCounts.put(e.getKey(), e.getValue());
        }

        HashMap<String, Scored> best = new HashMap<>();
        float[] ix = new float[64], iy = new float[64];
        for (Map.Entry<Character, Float> st : startSet.entrySet()) {
            int[] range = dict.prefixRange(String.valueOf(st.getKey()));
            for (int pos = range[0]; pos < range[1]; pos++) {
                int wi = dict.wordAtSorted(pos);
                String key = dict.keyOf(wi);
                double s = scoreWord(key, px, py, m, pathLen, user, endSet, ix, iy);
                if (s >= Double.MAX_VALUE) continue;
                String w = dict.word(wi);
                s += 1.6 * (255 - dict.freq(wi)) / 255.0;
                s -= bonus(hist.count(w), nextCounts.get(w));
                s += 0.5 * (st.getValue() + endSet.get(key.charAt(key.length() - 1)));
                Scored old = best.get(key);
                if (old == null || s < old.score) best.put(key, new Scored(w, s));
            }
        }
        // words only the user knows (typed at least twice)
        for (Map.Entry<String, Integer> e : hist.allWords()) {
            if (e.getValue() < 2) continue;
            String key = TextNormalizer.key(e.getKey());
            if (key.length() < 2 || !startSet.containsKey(key.charAt(0))) continue;
            double s = scoreWord(key, px, py, m, pathLen, user, endSet, ix, iy);
            if (s >= Double.MAX_VALUE) continue;
            s += 0.8 - bonus(e.getValue(), nextCounts.get(e.getKey()));
            s += 0.5 * (startSet.get(key.charAt(0)) + endSet.get(key.charAt(key.length() - 1)));
            Scored old = best.get(key);
            if (old == null || s < old.score) best.put(key, new Scored(e.getKey(), s));
        }

        List<Scored> list = new ArrayList<>(best.values());
        Collections.sort(list, new Comparator<Scored>() {
            @Override public int compare(Scored a, Scored b) { return Double.compare(a.score, b.score); }
        });
        for (int i = 0; i < list.size() && out.size() < max; i++) out.add(list.get(i).word);
        return out;
    }

    private static double bonus(int count, Integer bigram) {
        double b = 0;
        if (count > 0) b += Math.min(0.8, 0.3 * Math.log(1 + count));
        if (bigram != null && bigram > 0) b += Math.min(1.0, 0.45 * Math.log(1 + bigram));
        return b;
    }

    /** shape distance (lower is better) or MAX_VALUE if the word cannot match */
    private double scoreWord(String key, float[] px, float[] py, int m, float pathLen,
                             float[][] user, Map<Character, Float> endSet, float[] ix, float[] iy) {
        int len = key.length();
        if (len < 2 || len > 60) return Double.MAX_VALUE;
        if (!endSet.containsKey(key.charAt(len - 1))) return Double.MAX_VALUE;
        // ideal path through the key centres, repeated letters collapsed
        int k = 0;
        char last = 0;
        for (int i = 0; i < len; i++) {
            char c = key.charAt(i);
            if (c == last) continue;
            float[] p = centers.get(c);
            if (p == null) return Double.MAX_VALUE;
            ix[k] = p[0]; iy[k] = p[1]; k++;
            last = c;
        }
        float idealLen = length(ix, iy, k);
        if (idealLen > pathLen * 1.7f + 1.2f || idealLen < pathLen * 0.45f - 1.2f) return Double.MAX_VALUE;
        // the finger must pass near every letter, in order
        int j = 0;
        for (int i = 0; i < k; i++) {
            while (j < m && dist2(px[j], py[j], ix[i], iy[i]) > PASS_RADIUS * PASS_RADIUS) j++;
            if (j >= m) return Double.MAX_VALUE;
        }
        float[][] ideal = resample(ix, iy, k, N);
        double d = 0;
        for (int i = 0; i < N; i++) {
            d += Math.sqrt(dist2(ideal[0][i], ideal[1][i], user[0][i], user[1][i]));
        }
        return 3.0 * d / N;
    }

    private Map<Character, Float> near(float x, float y) {
        HashMap<Character, Float> out = new HashMap<>();
        for (Map.Entry<Character, float[]> e : centers.entrySet()) {
            float[] p = e.getValue();
            float d = (float) Math.sqrt(dist2(x, y, p[0], p[1]));
            if (d <= END_RADIUS) out.put(e.getKey(), d);
        }
        return out;
    }

    private static float dist2(float ax, float ay, float bx, float by) {
        float dx = ax - bx, dy = ay - by;
        return dx * dx + dy * dy;
    }

    private static float length(float[] x, float[] y, int n) {
        float l = 0;
        for (int i = 1; i < n; i++) l += (float) Math.sqrt(dist2(x[i - 1], y[i - 1], x[i], y[i]));
        return l;
    }

    /** n points spread evenly along the polyline */
    static float[][] resample(float[] x, float[] y, int m, int n) {
        float[][] out = new float[2][n];
        float total = length(x, y, m);
        if (m == 1 || total == 0) {
            for (int i = 0; i < n; i++) { out[0][i] = x[0]; out[1][i] = y[0]; }
            return out;
        }
        float step = total / (n - 1);
        int seg = 1;
        float segStart = 0; // distance at point seg-1
        float segLen = (float) Math.sqrt(dist2(x[0], y[0], x[1], y[1]));
        for (int i = 0; i < n; i++) {
            float target = i * step;
            while (seg < m - 1 && segStart + segLen < target) {
                segStart += segLen;
                seg++;
                segLen = (float) Math.sqrt(dist2(x[seg - 1], y[seg - 1], x[seg], y[seg]));
            }
            float t = segLen == 0 ? 0 : Math.min(1, Math.max(0, (target - segStart) / segLen));
            out[0][i] = x[seg - 1] + t * (x[seg] - x[seg - 1]);
            out[1][i] = y[seg - 1] + t * (y[seg] - y[seg - 1]);
        }
        return out;
    }
}
