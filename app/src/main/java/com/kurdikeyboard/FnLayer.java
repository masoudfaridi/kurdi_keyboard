package com.kurdikeyboard;

import android.util.SparseIntArray;

/**
 * What the letter keys type with fn on (2.9.0, Persian and Kurdish keyboards): a two-letter key
 * its second letter (ص → ض), every other letter a mark or punctuation sign of the language.
 * The Persian marks sit where the standard Persian keyboard (ISIRI 9147) has them with shift
 * when that key exists here: ف → ُ, ق → ً, ث → ٍ, ه → ّ, ن → », م → «, ل → أ, ب → إ, ت → ة.
 */
final class FnLayer {
    private FnLayer() {}

    /** two-letter keys, for the layouts without double tap (their keys have one code) */
    private static final SparseIntArray PAIRS_FA = map(
            0x0635, 0x0636, // ص ض
            0x0639, 0x063A, // ع غ
            0x0627, 0x0622, // ا آ
            0x0637, 0x0638, // ط ظ
            0x0632, 0x0698, // ز ژ
            0x062F, 0x0630, // د ذ
            0x0648, 0x0624, // و ؤ
            0x06A9, 0x0626, // ک ئ
            0x06AF, 0x0621); // گ ء
    private static final SparseIntArray PAIRS_KR = map(
            0x0641, 0x06A4, // ف ڤ
            0x0639, 0x063A, // ع غ
            0x0644, 0x06B5, // ل ڵ
            0x0632, 0x0698, // ز ژ
            0x0631, 0x0695, // ر ڕ
            0x06C6, 0x06CA, // ۆ ۊ
            0x06A9, 0x0626); // ک ئ

    private static final SparseIntArray MARKS_FA = map(
            0x062B, 0x064D, // ث  ٍ  kasratan
            0x0642, 0x064B, // ق  ً  fathatan
            0x0641, 0x064F, // ف  ُ  damma
            0x0647, 0x0651, // ه  ّ  shadda
            0x062E, 0x064E, // خ  َ  fatha
            0x062D, 0x0650, // ح  ِ  kasra
            0x062C, 0x064C, // ج  ٌ  dammatan
            0x0686, 0x0652, // چ  ْ  sukun
            0x0634, 0x0654, // ش  ٔ  hamza above (خانهٔ)
            0x0633, 0x061B, // س  ؛
            0x06CC, 0x066A, // ی  ٪
            0x0628, 0x0625, // ب  إ
            0x0644, 0x0623, // ل  أ
            0x062A, 0x0629, // ت  ة
            0x0646, 0x00BB, // ن  »
            0x0645, 0x00AB, // م  «
            0x067E, 0xFDFC, // پ  ﷼
            0x0631, 0x0670); // ر  ٰ  superscript alef
    private static final SparseIntArray MARKS_KR = map(
            0x06CE, 0x064F, // ێ  ُ
            0x0626, 0x064D, // ئ  ٍ
            0x0642, 0x064B, // ق  ً
            0x0647, 0x0651, // ه  ّ
            0x062E, 0x064E, // خ  َ
            0x062D, 0x0650, // ح  ِ
            0x062C, 0x064C, // ج  ٌ
            0x0686, 0x0652, // چ  ْ
            0x0634, 0x061B, // ش  ؛
            0x0633, 0x060C, // س  ،
            0x06CC, 0x066A, // ی  ٪
            0x0628, 0x0021, // ب  !
            0x0627, 0x003A, // ا  :
            0x062A, 0x2026, // ت  …
            0x0646, 0x00BB, // ن  »
            0x0645, 0x00AB, // م  «
            0x067E, 0x061F, // پ  ؟
            0x062F, 0x0028, // د  (
            0x0648, 0x0029, // و  )
            0x06AF, 0x0640); // گ  ـ  kashida

    /**
     * What a key types with fn, or 0 (it types as without fn).
     * @param codes the key's codes; a second code is its second letter
     * @param onBoard whether a letter has its own key on this keyboard
     */
    static int code(String lang, int[] codes, java.util.Set<Integer> onBoard) {
        if (codes == null || codes.length == 0 || codes[0] <= 0) return 0;
        boolean fa = "fa".equals(lang), kr = "kr".equals(lang);
        if (!fa && !kr) return 0;
        if (codes.length > 1 && codes[1] > 0) return codes[1];
        int pair = (fa ? PAIRS_FA : PAIRS_KR).get(codes[0]);
        if (pair != 0 && !onBoard.contains(pair)) return pair;
        return (fa ? MARKS_FA : MARKS_KR).get(codes[0]);
    }

    /** how the key shows a character: a mark on a dotted circle, so it can be seen */
    static String display(int cp) {
        String s = new String(Character.toChars(cp));
        int type = Character.getType(cp);
        return type == Character.NON_SPACING_MARK || type == Character.ENCLOSING_MARK ? "◌" + s : s;
    }

    private static SparseIntArray map(int... kv) {
        SparseIntArray m = new SparseIntArray(kv.length / 2);
        for (int i = 0; i + 1 < kv.length; i += 2) m.put(kv[i], kv[i + 1]);
        return m;
    }
}
