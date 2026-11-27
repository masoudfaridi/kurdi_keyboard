package com.kurdikeyboard.predict;

import java.util.Locale;

/**
 * Folds spelling variants so that what the user types on the keyboard can be matched
 * against dictionary words. The folded form ("key") is only used for matching; the
 * dictionary spelling is what gets inserted.
 */
public final class TextNormalizer {

    public static final char ZWNJ = '‌';

    private TextNormalizer() {}

    /** Matching key for a word or a prefix. */
    public static String key(CharSequence in) {
        if (in == null) return "";
        StringBuilder sb = new StringBuilder(in.length());
        int n = in.length();
        for (int i = 0; i < n; i++) {
            char c = in.charAt(i);
            char next = i + 1 < n ? in.charAt(i + 1) : 0;
            // Kurdish letters typed as base letter + combining mark (the keyboard's
            // "fix for non-joining Kurdish letters" setting)
            if (c == 'ل' && next == '٘') { sb.append('ڵ'); i++; continue; } // ڵ
            if (c == 'ی' && next == 'ٚ') { sb.append('ێ'); i++; continue; } // ێ
            if (c == 'و' && next == 'ٚ') { sb.append('ۆ'); i++; continue; } // ۆ
            switch (c) {
                case 'ي': case 'ى': case 'ې': sb.append('ی'); break; // ي ى -> ی
                case 'ك': case 'ڪ': sb.append('ک'); break;               // ك -> ک
                case 'ە': case 'ھ': case 'ہ': sb.append('ه'); break; // ە ھ ہ -> ه
                case 'آ': case 'أ': case 'إ': case 'ٱ': sb.append('ا'); break; // آ أ إ -> ا
                case 'ؤ': sb.append('و'); break;                               // ؤ -> و
                case 'ة': sb.append('ه'); break;                               // ة -> ه
                case 'ۇ': case 'ۊ': sb.append('ۆ'); break;               // ۇ ۊ -> ۆ
                case ZWNJ: case '‍': case 'ـ': break;                          // zwnj, zwj, tatweel
                case '’': case 'ʼ': sb.append('\''); break;
                default:
                    if ((c >= 'ً' && c <= 'ٟ') || c == 'ٰ') break;     // harakat
                    sb.append(Character.toLowerCase(c));
            }
        }
        return sb.toString();
    }

    public static boolean isArabicScript(char c) {
        return (c >= '؀' && c <= 'ۿ') || (c >= 'ݐ' && c <= 'ݿ')
                || (c >= 'ﭐ' && c <= '﷿') || (c >= 'ﹰ' && c <= '﻿');
    }

    /** Characters that may appear inside a word (letters, apostrophe, ZWNJ, marks). */
    public static boolean isWordChar(char c) {
        if (Character.isLetter(c)) return true;
        if (c == ZWNJ || c == '‍' || c == '\'' || c == '’' || c == 'ـ') return true;
        int t = Character.getType(c);
        return t == Character.NON_SPACING_MARK || t == Character.COMBINING_SPACING_MARK;
    }

    /** Letters of Kurdish that do not exist in Persian: if a word has one, it is Kurdish. */
    public static boolean hasKurdishOnlyLetter(CharSequence s) {
        for (int i = 0; i < s.length(); i++) {
            switch (s.charAt(i)) {
                case 'ڵ': case 'ڕ': case 'ێ': case 'ۆ': case 'ە':
                case 'ڤ': case 'ۊ': case '٘': case 'ٚ':
                    return true;
            }
        }
        return false;
    }

    public static boolean hasLatinLetter(CharSequence s) {
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) return true;
        }
        return false;
    }

    public static String capitalize(String w) {
        if (w.isEmpty()) return w;
        return w.substring(0, 1).toUpperCase(Locale.ROOT) + w.substring(1);
    }
}
