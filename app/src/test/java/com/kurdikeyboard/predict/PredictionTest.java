package com.kurdikeyboard.predict;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/** Plain JVM tests for word prediction and swipe decoding with the real dictionaries. */
public class PredictionTest {

    private static PredictionEngine engine;
    private static WordDictionary en, fa, ckb;

    @BeforeClass
    public static void setUp() throws Exception {
        File dir = new File("src/main/assets/dict");
        en = WordDictionary.load("en", new FileInputStream(new File(dir, "en.dict")));
        fa = WordDictionary.load("fa", new FileInputStream(new File(dir, "fa.dict")));
        ckb = WordDictionary.load("ckb", new FileInputStream(new File(dir, "ckb.dict")));
        engine = new PredictionEngine(Files.createTempDirectory("hist").toFile());
        engine.putDictionary(en);
        engine.putDictionary(fa);
        engine.putDictionary(ckb);
    }

    @Test
    public void englishCompletion() {
        List<String> s = engine.suggest("en", null, "hel", 3);
        assertTrue(s.toString(), s.contains("help") || s.contains("hello"));
        s = engine.suggest("en", null, "Thx", 3);
        assertEquals("Thx", s.get(0)); // unknown word is offered as typed
    }

    @Test
    public void englishCorrection() {
        List<String> s = engine.suggest("en", null, "becuase", 3);
        assertTrue(s.toString(), s.contains("because"));
    }

    @Test
    public void persianCompletion() {
        List<String> s = engine.suggest("fa", null, "سلا", 3);
        assertTrue(s.toString(), s.contains("سلام"));
        // Arabic yeh/kaf typed by some keyboards still match
        s = engine.suggest("fa", null, "كتا", 3);
        assertTrue(s.toString(), s.contains("کتاب"));
    }

    @Test
    public void kurdishCompletionWithOldStyleHeh() {
        // the 2017 keyboard types ه for ە: "کە" must still find Kurdish words
        List<String> s = engine.suggest("ckb", null, "کوردست", 3);
        assertTrue(s.toString(), s.contains("کوردستان"));
        s = engine.suggest("ckb", null, "به", 3);
        assertTrue(s.toString(), s.contains("بە"));
    }

    @Test
    public void languageDetection() {
        assertEquals("ckb", PredictionEngine.languageFor("fa", "ڕۆژ"));
        assertEquals("en", PredictionEngine.languageFor("fa", "hi"));
        assertEquals("fa", PredictionEngine.languageFor("fa", "سلام"));
    }

    @Test
    public void learningAndNextWord() {
        for (int i = 0; i < 3; i++) engine.learn("en", "good", "morning");
        List<String> next = engine.suggest("en", "good", "", 3);
        assertEquals("morning", next.get(0));
        engine.learn("en", null, "kurdikeyboard");
        engine.learn("en", null, "kurdikeyboard");
        assertTrue(engine.suggest("en", null, "kurdik", 3).contains("kurdikeyboard"));
    }

    // ---------------------------------------------------------------- swipe

    private static final String[] QWERTY = {"qwertyuiop", "asdfghjkl", "zxcvbnm"};
    private static final int KW = 100, KH = 150;

    private static SwipeDecoder qwertyDecoder() {
        List<int[]> codes = new ArrayList<>(), rects = new ArrayList<>();
        for (int row = 0; row < QWERTY.length; row++) {
            int offset = row * KW / 2;
            for (int i = 0; i < QWERTY[row].length(); i++) {
                codes.add(new int[]{QWERTY[row].charAt(i)});
                rects.add(new int[]{offset + i * KW, row * KH, KW, KH});
            }
        }
        return new SwipeDecoder(codes, rects);
    }

    private static float[] center(char c) {
        for (int row = 0; row < QWERTY.length; row++) {
            int i = QWERTY[row].indexOf(c);
            if (i >= 0) return new float[]{row * KW / 2 + i * KW + KW / 2f, row * KH + KH / 2f};
        }
        throw new IllegalArgumentException("" + c);
    }

    /** a sloppy finger path: key centres + noise, with intermediate points */
    private static float[][] gesture(String word, Random rnd) {
        List<float[]> pts = new ArrayList<>();
        float[] prev = null;
        for (char c : word.toCharArray()) {
            float[] p = center(c);
            float[] q = {p[0] + (float) rnd.nextGaussian() * KW * 0.22f, p[1] + (float) rnd.nextGaussian() * KH * 0.22f};
            if (prev != null) {
                for (int s = 1; s <= 6; s++) {
                    float t = s / 7f;
                    pts.add(new float[]{prev[0] + t * (q[0] - prev[0]), prev[1] + t * (q[1] - prev[1])});
                }
            }
            pts.add(q);
            prev = q;
        }
        float[][] out = new float[2][pts.size()];
        for (int i = 0; i < pts.size(); i++) { out[0][i] = pts.get(i)[0]; out[1][i] = pts.get(i)[1]; }
        return out;
    }

    @Test
    public void swipeEnglishWords() {
        SwipeDecoder d = qwertyDecoder();
        String[] words = {"hello", "keyboard", "the", "world", "because", "people", "thanks",
                "good", "morning", "language", "write", "phone", "about", "really", "think"};
        Random rnd = new Random(7);
        int top1 = 0, top3 = 0;
        StringBuilder misses = new StringBuilder();
        for (String w : words) {
            float[][] g = gesture(w, rnd);
            List<String> r = d.decode(g[0], g[1], g[0].length, en, engine, "en", null, 3);
            if (!r.isEmpty() && r.get(0).equals(w)) top1++;
            if (r.contains(w)) top3++; else misses.append(w).append("->").append(r).append(' ');
        }
        System.out.println("swipe top1=" + top1 + " top3=" + top3 + "/" + words.length + " " + misses);
        assertTrue("top3 " + top3 + " misses: " + misses, top3 >= words.length - 2);
        assertTrue("top1 " + top1, top1 >= words.length * 2 / 3);
    }

    @Test
    public void distance() {
        assertEquals(1, PredictionEngine.distance("teh", "the", 2));
        assertEquals(1, PredictionEngine.distance("helo", "hello", 2));
        assertEquals(3, PredictionEngine.distance("abc", "xyz", 2));
    }
}
