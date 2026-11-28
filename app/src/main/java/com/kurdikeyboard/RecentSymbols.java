package com.kurdikeyboard;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * The symbols typed most recently. Long-pressing the dot key between space and enter shows
 * them in the middle row of its menu; the key itself always types a dot (before 2.9.0 it
 * changed to the last symbol picked).
 */
final class RecentSymbols {
    static final int SLOTS = 5;
    private static final String PREF = "pref_recent_symbols";

    private RecentSymbols() {}

    /** punctuation and symbols count; letters, digits, spaces, marks and the dot do not */
    static boolean counts(int cp) {
        if (cp == '.' || cp <= ' ' || cp > 0xFFFF) return false; // emoji have their own recents
        switch (Character.getType(cp)) {
            case Character.CONNECTOR_PUNCTUATION:
            case Character.DASH_PUNCTUATION:
            case Character.START_PUNCTUATION:
            case Character.END_PUNCTUATION:
            case Character.INITIAL_QUOTE_PUNCTUATION:
            case Character.FINAL_QUOTE_PUNCTUATION:
            case Character.OTHER_PUNCTUATION:
            case Character.MATH_SYMBOL:
            case Character.CURRENCY_SYMBOL:
            case Character.MODIFIER_SYMBOL:
            case Character.OTHER_SYMBOL:
                return true;
            default:
                return false;
        }
    }

    /** remembers a typed symbol as the newest one */
    static void add(Context c, int cp) {
        if (!counts(cp)) return;
        int[] old = load(c);
        if (old.length > 0 && old[0] == cp) return; // already the newest: nothing to save
        StringBuilder sb = new StringBuilder().appendCodePoint(cp);
        int n = 1;
        for (int o : old) {
            if (n >= SLOTS) break;
            if (o != cp) {
                sb.appendCodePoint(o);
                n++;
            }
        }
        prefs(c).edit().putString(PREF, sb.toString()).apply();
    }

    /** the recent symbols, newest first */
    static int[] load(Context c) {
        String s = prefs(c).getString(PREF, "");
        int[] out = new int[s.codePointCount(0, s.length())];
        for (int i = 0, k = 0; i < s.length() && k < out.length; k++) {
            out[k] = s.codePointAt(i);
            i += Character.charCount(out[k]);
        }
        return out;
    }

    private static SharedPreferences prefs(Context c) {
        return c.getSharedPreferences("MySharedPreference", Context.MODE_PRIVATE);
    }
}
