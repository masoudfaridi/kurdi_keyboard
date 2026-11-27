package com.kurdikeyboard;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Keyboard themes, drawn entirely in code (no images): a palette plus a key style.
 *
 * The 2017 themes were Photoshop bitmaps per key and per screen density. They are replaced
 * by these; a user who had chosen an old theme is moved to the closest new one
 * ({@link #forId(String)} understands the old ids).
 */
public final class KeyboardTheme {

    public final String id;
    public final String name;        // shown in the theme list (Persian)
    public final int bg;             // keyboard + tools row background
    public final int key;            // letter keys
    public final int keyPressed;
    public final int fn;             // function keys (shift, delete, 123, emoji ...)
    public final int fnPressed;
    public final int text;           // labels and icons
    public final int hint;           // small hints (long-press letters, number-row icons)
    public final int accent;         // Enter key, active shift
    public final int onAccent;       // icon on the Enter key
    public final int shadow;         // 0 = no shadow
    public final int stroke;         // 0 = no outline
    public final float radiusDp;     // key corner radius
    public final boolean borderless; // keys without a shape, labels only
    /** extra effects of a "pro" theme (gradients, glow, 3D keys ...); null for a plain theme */
    public final Pro pro;

    private KeyboardTheme(String id, String name, int bg, int key, int keyPressed, int fn, int fnPressed,
                          int text, int hint, int accent, int onAccent, int shadow, int stroke,
                          float radiusDp, boolean borderless, Pro pro) {
        this.id = id;
        this.name = name;
        this.bg = bg;
        this.key = key;
        this.keyPressed = keyPressed;
        this.fn = fn;
        this.fnPressed = fnPressed;
        this.text = text;
        this.hint = hint;
        this.accent = accent;
        this.onAccent = onAccent;
        this.shadow = shadow;
        this.stroke = stroke;
        this.radiusDp = radiusDp;
        this.borderless = borderless;
        this.pro = pro;
    }

    public boolean isDark() {
        return pro != null ? pro.dark : luminance(bg) < 0.5;
    }

    public boolean isPro() {
        return pro != null;
    }

    static double luminance(int c) {
        return (0.299 * ((c >> 16) & 0xFF) + 0.587 * ((c >> 8) & 0xFF) + 0.114 * (c & 0xFF)) / 255.0;
    }

    // ------------------------------------------------------------------ catalogue

    public static final String DEFAULT_ID = "light";

    private static final Map<String, KeyboardTheme> THEMES = new LinkedHashMap<>();

    private static void add(String id, String name, int bg, int key, int keyPressed, int fn, int fnPressed,
                            int text, int hint, int accent, int onAccent, int shadow, int stroke,
                            float radius, boolean borderless) {
        THEMES.put(id, new KeyboardTheme(id, name, bg, key, keyPressed, fn, fnPressed, text, hint,
                accent, onAccent, shadow, stroke, radius, borderless, null));
    }

    static {
        final int W = 0xFFFFFFFF;
        // ---------------------------------------------------------------- light (24)
        //   id            name                 bg          key         pressed     fn          fnPressed   text        hint        accent      onAccent  shadow      stroke      r    borderless
        add("light",      "روشن",              0xFFE6EAF0, 0xFFFFFFFF, 0xFFD6DEE8, 0xFFCED6E0, 0xFFB9C3CF, 0xFF1A2433, 0xFF7B8594, 0xFF2F6FED, W, 0xFFB4BDC8, 0,          7,   false);
        add("classic",    "کلاسیک خاکستری",    0xFFC9CDD3, 0xFFF7F8F9, 0xFFDDE1E6, 0xFFAEB4BC, 0xFF9AA1AA, 0xFF111111, 0xFF5E6670, 0xFF3D7BF7, W, 0xFF8E959E, 0,          5,   false);
        add("paper",      "کاغذی",             0xFFF5F5F4, 0xFFFFFFFF, 0xFFE7E5E4, 0xFFE7E5E4, 0xFFD6D3D1, 0xFF1C1917, 0xFF78716C, 0xFF57534E, W, 0xFFD6D3D1, 0,          6,   false);
        add("minimal",    "مینیمال",           0xFFFAFAFA, 0xFFFAFAFA, 0xFFE5E7EB, 0xFFFAFAFA, 0xFFE5E7EB, 0xFF111827, 0xFF9CA3AF, 0xFF111827, W, 0,          0,          10,  true);
        add("cloud",      "ابری",              0xFFF1F5F9, 0xFFFFFFFF, 0xFFE2E8F0, 0xFFF1F5F9, 0xFFE2E8F0, 0xFF0F172A, 0xFF94A3B8, 0xFF0EA5E9, W, 0,          0xFFCBD5E1, 10,  false);
        add("silver",     "نقره‌ای",           0xFFD9DDE3, 0xFFF4F5F7, 0xFFDADDE2, 0xFFC3C8D0, 0xFFAEB4BE, 0xFF1F2937, 0xFF6B7280, 0xFF4B5563, W, 0xFFA1A8B2, 0,          4,   false);
        add("sky",        "آسمانی",            0xFFDBEAFE, 0xFFFFFFFF, 0xFFBFDBFE, 0xFFBFDBFE, 0xFF93C5FD, 0xFF1E3A8A, 0xFF5B7BB8, 0xFF2563EB, W, 0xFFA9C4EE, 0,          9,   false);
        add("ice",        "یخی",               0xFFE0F2FE, 0xFFFFFFFF, 0xFFE0F2FE, 0xFFBAE6FD, 0xFF93D2F5, 0xFF0C4A6E, 0xFF4B86A8, 0xFF0284C7, W, 0xFFA5D3EE, 0,          8,   false);
        add("diamond",    "الماس",             0xFFECFEFF, 0xFFFFFFFF, 0xFFCFFAFE, 0xFFE0F7FA, 0xFFB9EEF4, 0xFF155E75, 0xFF4F8C9C, 0xFF06B6D4, W, 0,          0xFF67E8F9, 8,   false);
        add("aqua",       "فیروزه‌ای",         0xFFD5F5F6, 0xFFFFFFFF, 0xFFB9EEF0, 0xFFA5E8EC, 0xFF7FDCE2, 0xFF0E4F55, 0xFF4A8E93, 0xFF0FA3B1, W, 0xFF9ED9DD, 0,          16,  false);
        add("teal",       "سبز-آبی",           0xFFCCFBF1, 0xFFFFFFFF, 0xFFDDFBF4, 0xFF99F6E4, 0xFF6EE7D2, 0xFF134E4A, 0xFF3F7F79, 0xFF0D9488, W, 0xFF8EE0CF, 0,          8,   false);
        add("mint",       "نعنایی",            0xFFECFDF5, 0xFFFFFFFF, 0xFFD1FAE5, 0xFFA7F3D0, 0xFF7EE6BA, 0xFF064E3B, 0xFF4A8A72, 0xFF10B981, W, 0xFFB5E8D2, 0,          18,  false);
        add("sage",       "مریم‌گلی",          0xFFE7EDE4, 0xFFF8FAF6, 0xFFD9E3D3, 0xFFCBD7C4, 0xFFB5C5AC, 0xFF2F3E2A, 0xFF6F8166, 0xFF5E8C4A, W, 0xFFBAC8B2, 0,          8,   false);
        add("lemon",      "لیمویی",            0xFFFEF9C3, 0xFFFFFFF5, 0xFFFEF08A, 0xFFFDE68A, 0xFFFCD34D, 0xFF713F12, 0xFFA3862E, 0xFFCA8A04, W, 0xFFEBDD8F, 0,          12,  false);
        add("sand",       "طلایی",             0xFFFEF3C7, 0xFFFFFBEB, 0xFFFDE68A, 0xFFFDE68A, 0xFFFCD34D, 0xFF78350F, 0xFFA16A2E, 0xFFD97706, W, 0xFFF1D58A, 0,          8,   false);
        add("cream",      "کرم",               0xFFF3EBDD, 0xFFFFFBF3, 0xFFEADFCB, 0xFFE3D5BE, 0xFFD4C2A5, 0xFF4A3B28, 0xFF8C7A62, 0xFFB07D3E, W, 0xFFD6C8B0, 0,          8,   false);
        add("peach",      "هلویی",             0xFFFFE4D6, 0xFFFFF7F2, 0xFFFFD5C2, 0xFFFFD0B8, 0xFFFDB797, 0xFF7A2E12, 0xFFB0705A, 0xFFF97316, W, 0xFFF2C0A8, 0,          10,  false);
        add("sunset",     "غروب",              0xFFFFEDD5, 0xFFFFFFFF, 0xFFFFE4C7, 0xFFFED7AA, 0xFFFDBA74, 0xFF7C2D12, 0xFFB0603A, 0xFFEA580C, W, 0xFFF5C9A0, 0,          8,   false);
        add("coral",      "مرجانی",            0xFFFFE8E3, 0xFFFFFFFF, 0xFFFFD3C9, 0xFFFFC9BC, 0xFFFFAE9C, 0xFF7F1D1D, 0xFFB9655A, 0xFFFF6B57, W, 0xFFF3BDB1, 0,          9,   false);
        add("blossom",    "شکوفه",             0xFFFFF1F2, 0xFFFFFFFF, 0xFFFFE4E6, 0xFFFECDD3, 0xFFFDA4AF, 0xFF881337, 0xFFB4637A, 0xFFF43F5E, W, 0xFFF6C3CB, 0,          12,  false);
        add("rose",       "صورتی",             0xFFFCE7F3, 0xFFFFFFFF, 0xFFFDF2F8, 0xFFFBCFE8, 0xFFF5A8D1, 0xFF831843, 0xFFB45A8A, 0xFFDB2777, W, 0xFFF3B5D5, 0,          8,   false);
        add("candy",      "صورتی فانتزی",      0xFFFDF2F8, 0xFFFBCFE8, 0xFFF9A8D4, 0xFFF9A8D4, 0xFFF472B6, 0xFF9D174D, 0xFFBE5A8C, 0xFFEC4899, W, 0xFFF0A3C8, 0,          20,  false);
        add("lavender",   "یاسی",              0xFFF5F3FF, 0xFFFFFFFF, 0xFFEDE9FE, 0xFFDDD6FE, 0xFFC4B5FD, 0xFF3B0764, 0xFF7C6A9C, 0xFF8B5CF6, W, 0xFFD5CCF5, 0,          8,   false);
        add("violet",     "بنفشه",             0xFFEDE7F6, 0xFFFFFFFF, 0xFFE1D5F5, 0xFFD1C4E9, 0xFFB39DDB, 0xFF311B92, 0xFF7E6BB0, 0xFF7C4DFF, W, 0xFFC9BCE3, 0,          14,  false);
        // ---------------------------------------------------------------- dark (24)
        add("dark",       "تیره",              0xFF15191E, 0xFF2C333C, 0xFF3D4651, 0xFF20262D, 0xFF323A44, 0xFFF2F4F7, 0xFF8E99A8, 0xFF2F6FED, W, 0xFF0B0E11, 0,          7,   false);
        add("amoled",     "مشکی مطلق",         0xFF000000, 0xFF161616, 0xFF2A2A2A, 0xFF0D0D0D, 0xFF222222, 0xFFFFFFFF, 0xFF8A8A8A, 0xFF3B82F6, W, 0,          0,          8,   false);
        add("graphite",   "خاکستری تیره",      0xFF374151, 0xFF4B5563, 0xFF5B6574, 0xFF3F4753, 0xFF4F5865, 0xFFF9FAFB, 0xFFB0B7C3, 0xFF60A5FA, W, 0xFF222831, 0,          8,   false);
        add("slate",      "سنگی",              0xFF1C2127, 0xFF2D343C, 0xFF3B444E, 0xFF252B32, 0xFF333B44, 0xFFE5E9EE, 0xFF8B96A3, 0xFF14B8A6, W, 0xFF0E1114, 0,          10,  false);
        add("midnight",   "شب آبی",            0xFF0F172A, 0xFF1E293B, 0xFF334155, 0xFF172033, 0xFF283548, 0xFFE2E8F0, 0xFF8291A8, 0xFF38BDF8, 0xFF0F172A, 0xFF060B16, 0, 8, false);
        add("navy",       "سرمه‌ای",           0xFF0A1931, 0xFF152A4A, 0xFF1E3A64, 0xFF0F213D, 0xFF1A3158, 0xFFE8EEF8, 0xFF8DA2C4, 0xFF4F8CFF, W, 0xFF050D1C, 0,          8,   false);
        add("glass",      "شیشه‌ای",           0xFF1E3A5F, 0x33FFFFFF, 0x55FFFFFF, 0x1FFFFFFF, 0x40FFFFFF, 0xFFFFFFFF, 0xB3FFFFFF, 0xFF60A5FA, W, 0,          0x4DFFFFFF, 9,   false);
        add("ocean",      "اقیانوس",           0xFF0B3954, 0xFF145374, 0xFF1D6B94, 0xFF0E4565, 0xFF185C82, 0xFFE6F4FA, 0xFF8FC1D9, 0xFF00B4D8, W, 0xFF062638, 0,          8,   false);
        add("aurora",     "شفق قطبی",          0xFF0B1D2A, 0xFF12313F, 0xFF1A4252, 0xFF0E2633, 0xFF163746, 0xFFE0FBF6, 0xFF7FC8C0, 0xFF5EEAD4, 0xFF0B1D2A, 0xFF06121A, 0, 12, false);
        add("neon",       "نئون",              0xFF0A0A12, 0xFF141420, 0xFF23233A, 0xFF0F0F18, 0xFF1C1C2C, 0xFFE0FFFF, 0xFF6FD6E0, 0xFF00E5F0, 0xFF0A0A12, 0, 0x6600F0FF, 8, false);
        add("forest",     "جنگل",              0xFF052E16, 0xFF14532D, 0xFF1C6B3B, 0xFF0F3F22, 0xFF185C33, 0xFFDCFCE7, 0xFF86C79C, 0xFF22C55E, 0xFF052E16, 0xFF021A0C, 0, 8, false);
        add("emerald",    "زمردی",             0xFF022C22, 0xFF064E3B, 0xFF065F46, 0xFF053B2E, 0xFF06513F, 0xFFD1FAE5, 0xFF7CC4A8, 0xFF34D399, 0xFF022C22, 0xFF01170F, 0, 14, false);
        add("olive",      "زیتونی",            0xFF1F2416, 0xFF343C25, 0xFF434D30, 0xFF293020, 0xFF384129, 0xFFEEF2DD, 0xFFA3AD86, 0xFFA3C14A, 0xFF1F2416, 0xFF0F1209, 0, 8, false);
        add("goldnight",  "طلایی شب",          0xFF141414, 0xFF242220, 0xFF34302B, 0xFF1B1A18, 0xFF2A2723, 0xFFF5E6C4, 0xFFB8A47A, 0xFFD4AF37, 0xFF141414, 0, 0x33D4AF37, 6, false);
        add("wood",       "چوبی",              0xFF5C3B23, 0xFF8B5E3C, 0xFF9E6E49, 0xFF6F4A2E, 0xFF7E5636, 0xFFFFF3E6, 0xFFE0C2A2, 0xFFE8A050, 0xFF3B2414, 0xFF3A2414, 0, 6, false);
        add("chocolate",  "شکلاتی",            0xFF2B1D16, 0xFF44302A, 0xFF56403A, 0xFF36251F, 0xFF473229, 0xFFF5E6DA, 0xFFB89C88, 0xFFC08457, W, 0xFF180F0B, 0,          8,   false);
        add("coffee",     "قهوه",              0xFF1E1611, 0xFF3A2C22, 0xFF4A3A2E, 0xFF2B2019, 0xFF3A2C22, 0xFFF2E6D8, 0xFFA8927C, 0xFFD4A373, 0xFF1E1611, 0xFF0E0A07, 0, 12, false);
        add("ember",      "اخگر",              0xFF1A0F0A, 0xFF2E1A10, 0xFF452717, 0xFF23140C, 0xFF371F12, 0xFFFFEDD5, 0xFFD19A6A, 0xFFFB923C, 0xFF1A0F0A, 0xFF0C0604, 0, 8, false);
        add("crimson",    "قرمز شب",           0xFF1F0A0D, 0xFF3B1219, 0xFF521A24, 0xFF2C0E13, 0xFF40141C, 0xFFFFE4E6, 0xFFC28A93, 0xFFF43F5E, W, 0xFF100406, 0,          8,   false);
        add("wine",       "شرابی",             0xFF2A0A17, 0xFF4A1428, 0xFF5E1B34, 0xFF3A0F20, 0xFF4D1629, 0xFFFCE7F0, 0xFFC58AA2, 0xFFE0457B, W, 0xFF160309, 0,          10,  false);
        add("cyber",      "سایبری",            0xFF12061A, 0xFF1F0D2B, 0xFF32143F, 0xFF18091F, 0xFF2A1035, 0xFFFFE3F6, 0xFFD07CB8, 0xFFFF2DAA, W, 0,          0x55FF2DAA, 8,   false);
        add("noir",       "مشکی-بنفش",         0xFF0B0B10, 0xFF1C1A24, 0xFF2A2735, 0xFF14121B, 0xFF221F2C, 0xFFEDE9FE, 0xFF8C84A6, 0xFF8B5CF6, W, 0,          0x338B5CF6, 8,   false);
        add("purple",     "ارغوانی",           0xFF2E1065, 0xFF4C1D95, 0xFF5B21B6, 0xFF3B0F7A, 0xFF4A1A92, 0xFFF5F3FF, 0xFFB5A4E0, 0xFFA78BFA, 0xFF2E1065, 0xFF1C0840, 0, 8, false);
        add("galaxy",     "کهکشان",            0xFF140F2E, 0xFF241B4D, 0xFF33276A, 0xFF1B143D, 0xFF2A2058, 0xFFEEE8FF, 0xFF9E92D0, 0xFFC084FC, 0xFF140F2E, 0xFF0A0718, 0, 12, false);
    }

    /** Themes of one section of the theme list: light or dark. */
    public static List<KeyboardTheme> section(boolean dark) {
        List<KeyboardTheme> out = new ArrayList<>();
        for (KeyboardTheme t : THEMES.values()) if (!t.isPro() && t.isDark() == dark) out.add(t);
        return out;
    }

    /** Themes of one section of the "pro" theme list: light or dark. */
    public static List<KeyboardTheme> proSection(boolean dark) {
        List<KeyboardTheme> out = new ArrayList<>();
        // the newest ("future", ids fx_*) first
        for (KeyboardTheme t : THEMES.values()) if (t.isPro() && t.isDark() == dark && t.id.startsWith("fx_")) out.add(t);
        for (KeyboardTheme t : THEMES.values()) if (t.isPro() && t.isDark() == dark && !t.id.startsWith("fx_")) out.add(t);
        return out;
    }

    /** All themes, in the order of the theme list. */
    public static List<KeyboardTheme> all() {
        return new ArrayList<>(THEMES.values());
    }

    /** The theme for a stored preference value; old (2017) theme ids map to the closest new theme. */
    public static KeyboardTheme forId(String id) {
        KeyboardTheme t = id == null ? null : THEMES.get(id);
        if (t != null) return t;
        return THEMES.get(legacy(id));
    }

    static String legacy(String id) {
        if (id == null) return DEFAULT_ID;
        switch (id) {
            case "35": return "light";
            case "36": return "dark";
            case "1": case "1x": case "1xx": case "4": case "23": return "classic";
            case "2": case "2x": case "2xx": return "glass";
            case "3": case "3x": case "3xx": case "12": case "13": return "rose";
            case "5": case "5x": case "5xx": case "9x": case "9xx": return "teal";
            case "6": case "6x": case "6xx": return "ice";
            case "7": case "7x": case "7xx": return "wood";
            case "8": case "8x": case "8xx": return "ocean";
            case "10": case "10x": case "10xx": return "chocolate";
            case "11": case "11x": case "11xx": return "paper";
            case "14": case "15": return "candy";
            case "16": return "purple";
            case "17": case "18": return "diamond";
            case "19": return "paper";
            case "20": return "noir";
            case "21": return "graphite";
            case "22": return "candy";
            case "24": case "25": case "26": case "27": case "28": return "light";
            case "29": case "30": case "31": case "32": case "33": case "34": return "dark";
            default: return DEFAULT_ID;
        }
    }

    // ------------------------------------------------------------------ pro themes

    /** Effects of a pro theme. All drawn in code by {@link ProPainter}. */
    public static final class Pro {
        boolean dark;
        int[] bg;                 // background gradient, top to bottom (or diagonal)
        boolean diagonal;
        int[] blobColor = new int[0];       // soft round glows on the background
        float[] blobX = new float[0], blobY = new float[0], blobR = new float[0]; // fractions of the size
        int stars;                // color of small sparkles on the background, 0 = none
        int keyTop, keyBottom;    // key gradient
        int fnTop, fnBottom;      // function keys gradient: always close to the letter keys
        int edge;                 // 3D: the side of the key under it, 0 = flat
        int line;                 // outline color, 0 = none
        float lineDp;
        int glow;                 // neon glow of outline and edge, 0 = none
        float glowDp;
        boolean rainbow;          // outlines change color across the keyboard (labels stay plain)
        boolean labelGlow;        // labels glow
        int sheen;                // light reflection on the upper half of the key, 0 = none
        int shape;                // ProPainter.SHAPE_*: hexagon, cut corners, leaf, ticket ...
        float cut;                // size of the corner cut of the shape (fraction), 0 = the shape's own
        boolean keycap;           // mechanical keycap: body and raised top face
        boolean glass;            // glass highlight and rim
        int plate = AUTO;         // backing plate under see-through keys, so the artwork stays behind
        int fx, fxColor;          // ProPainter.FX_*: flames, gloss, inner glow inside the keys
        int art, artColor, artColor2; // ProPainter.ART_*: artwork on the background

        static final int AUTO = 1; // plate: decided in done() (a real color always has alpha)
    }

    private static final class P {
        final String id, name;
        final Pro x = new Pro();
        int text = 0xFFFFFFFF, hint = 0xB3FFFFFF, accent = 0xFF3B82F6, pressed = 0x40FFFFFF;
        float radius = 8;

        P(String id, String name, boolean dark) {
            this.id = id;
            this.name = name;
            x.dark = dark;
            if (!dark) { text = 0xFF1F2937; hint = 0xFF6B7280; pressed = 0x22000000; }
        }

        P bg(int... c) { x.bg = c; return this; }
        P diagonal() { x.diagonal = true; return this; }
        P blob(int color, float fx, float fy, float fr) {
            int n = x.blobColor.length;
            x.blobColor = java.util.Arrays.copyOf(x.blobColor, n + 1); x.blobColor[n] = color;
            x.blobX = java.util.Arrays.copyOf(x.blobX, n + 1); x.blobX[n] = fx;
            x.blobY = java.util.Arrays.copyOf(x.blobY, n + 1); x.blobY[n] = fy;
            x.blobR = java.util.Arrays.copyOf(x.blobR, n + 1); x.blobR[n] = fr;
            return this;
        }
        P stars(int c) { x.stars = c; return this; }
        P keys(int top, int bottom) { x.keyTop = top; x.keyBottom = bottom; return this; }
        P edge(int c) { x.edge = c; return this; }
        P line(int c, float dp) { x.line = c; x.lineDp = dp; return this; }
        P glow(int c, float dp) { x.glow = c; x.glowDp = dp; return this; }
        P rainbow() { x.rainbow = true; return this; }
        P labelGlow() { x.labelGlow = true; return this; }
        P sheen(int c) { x.sheen = c; return this; }
        P shape(int sh) { x.shape = sh; return this; }
        P shape(int sh, float cut) { x.shape = sh; x.cut = cut; return this; }
        P keycap() { x.keycap = true; return this; }
        P glass() { x.glass = true; return this; }
        P plate(int c) { x.plate = c; return this; }
        P fx(int f, int color) { x.fx = f; x.fxColor = color; return this; }
        P art(int a, int color, int color2) { x.art = a; x.artColor = color; x.artColor2 = color2; return this; }
        P text(int t, int h) { text = t; hint = h; return this; }
        P accent(int a) { accent = a; return this; }
        P pressed(int c) { pressed = c; return this; }
        P radius(float r) { radius = r; return this; }

        void done() {
            // 2.6: the function keys differ only a little from the letter keys
            int shift = x.dark ? 0x10FFFFFF : 0x0C000000;
            x.fnTop = ProPainter.over(x.keyTop, shift);
            x.fnBottom = ProPainter.over(x.keyBottom, shift);
            // the artwork stays behind the keys: a backing plate under see-through keys
            if (x.plate == Pro.AUTO) x.plate = x.art != ProPainter.ART_NONE ? plateColor() : 0;
            // Enter is only lightly tinted (ProPainter); its icon carries the accent color
            int on = x.dark ? ProPainter.mix(accent | 0xFF000000, 0xFFFFFFFF, 0.3f)
                    : ProPainter.mix(accent | 0xFF000000, 0xFF000000, 0.12f);
            int key = x.keyBottom != 0 ? x.keyBottom : x.bg[0];
            int fn = x.fnBottom != 0 ? x.fnBottom : key;
            int pressedKey = ProPainter.over(key, pressed);
            int stroke = x.line != 0 ? x.line : 0;
            THEMES.put(id, new KeyboardTheme(id, name, x.bg[0], key, pressedKey, fn, ProPainter.over(fn, pressed),
                    text, hint, accent, on, 0, stroke, radius, false, x));
        }

        /** the darkest (light themes: lightest) background color, nearly opaque */
        private int plateColor() {
            int best = x.bg[0];
            double bl = luminance(best);
            for (int c : x.bg) {
                double l = luminance(c);
                if (x.dark ? l < bl : l > bl) { best = c; bl = l; }
            }
            return (best & 0x00FFFFFF) | 0xDE000000;
        }
    }

    private static P pro(String id, String name, boolean dark) { return new P(id, name, dark); }

    // Pro theme keys are never plain rectangles or ovals (2.6): hexagons, cut corners, leaves,
    // tickets, slanted, notched and drop shapes.
    private static final int CHAMFER = ProPainter.SHAPE_CHAMFER, HEX = ProPainter.SHAPE_HEX,
            TECH = ProPainter.SHAPE_TECH, LEAF = ProPainter.SHAPE_LEAF, TICKET = ProPainter.SHAPE_TICKET,
            SLANT = ProPainter.SHAPE_SLANT, NOTCH = ProPainter.SHAPE_NOTCH, DROP = ProPainter.SHAPE_DROP;

    static {
        // ---------------------------------------------------------------- pro, dark
        pro("pro_neon_cyan", "نئون فیروزه‌ای", true).bg(0xFF000000, 0xFF01141A)
                .blob(0x4400D9FF, 0.15f, 0.05f, 0.7f).blob(0x2200FFD5, 0.9f, 1f, 0.6f).stars(0x9900E5FF)
                .shape(HEX).keys(0xF5030708, 0xF5020405).line(0xFF19E6F0, 1.4f).glow(0xFF00E5FF, 5)
                .text(0xFF8FF8FF, 0xAA19E6F0).labelGlow().accent(0xFF00C8D7).radius(9).done();
        pro("pro_blue_fire", "شعله آبی", true).bg(0xFF020617, 0xFF000208)
                .blob(0x662563EB, 0.05f, 0.95f, 0.55f).blob(0x553B82F6, 0.95f, 0.9f, 0.5f).blob(0x331D4ED8, 0.5f, 0f, 0.6f)
                .stars(0x8860A5FA).shape(TECH).keys(0xFF0B1022, 0xFF03050D).edge(0xFF1D4ED8).glow(0xFF3B82F6, 6)
                .line(0xFF3B82F6, 1).text(0xFFFFFFFF, 0xFF93C5FD).accent(0xFF3B82F6).radius(8).done();
        pro("pro_purple_3d", "بنفش سه‌بعدی", true).bg(0xFF1E1B4B, 0xFF2E1065).diagonal()
                .blob(0x447C3AED, 0.1f, 0.1f, 0.6f).blob(0x33DB2777, 0.95f, 0.2f, 0.4f)
                .shape(CHAMFER, 0.2f).keys(0xFF2A2660, 0xFF1C1940).edge(0xFFA855F7).glow(0xFFC084FC, 5).sheen(0x14FFFFFF)
                .text(0xFFFFFFFF, 0xFFC4B5FD).accent(0xFFA855F7).radius(10).done();
        pro("pro_butterfly", "پروانه نئونی", true).bg(0xFF0A0418, 0xFF12052A)
                .blob(0x66C026D3, 0.0f, 0.5f, 0.45f).blob(0x552563EB, 1f, 0.5f, 0.45f).blob(0x33F43F5E, 0.5f, 0f, 0.5f)
                .stars(0x99F0ABFC).shape(LEAF).keys(0xFF3B0A57, 0xFF14063A).line(0xFFE879F9, 1.3f).glow(0xFFD946EF, 5)
                .sheen(0x1AFFFFFF).text(0xFFFFFFFF, 0xFFF0ABFC).accent(0xFFD946EF).radius(9).done();
        pro("pro_rainbow", "رنگین‌کمان نئونی", true).bg(0xFF05050A, 0xFF0B0B14)
                .blob(0x55F43F5E, 0.05f, 0.1f, 0.45f).blob(0x553B82F6, 0.95f, 0.15f, 0.45f).blob(0x5522C55E, 0.5f, 1f, 0.5f)
                .shape(TICKET).keys(0xF510101A, 0xF508080F).line(0xFFFFFFFF, 1.4f).glow(0xFFFFFFFF, 5).rainbow()
                .text(0xFFFFFFFF, 0xCCFFFFFF).accent(0xFFEC4899).radius(9).done();
        pro("pro_neon_pink", "نئون صورتی", true).bg(0xFF0A0007, 0xFF14000E)
                .blob(0x44FF2DAA, 0.85f, 0.05f, 0.6f).blob(0x228B5CF6, 0.1f, 1f, 0.6f).stars(0x88FF7AD1)
                .shape(SLANT).keys(0xF5080006, 0xF5050004).line(0xFFFF4FC1, 1.4f).glow(0xFFFF2DAA, 5)
                .text(0xFFFFC2EA, 0xAAFF4FC1).labelGlow().accent(0xFFFF2DAA).radius(9).done();
        pro("pro_matrix", "نئون سبز", true).bg(0xFF000000, 0xFF001407)
                .blob(0x3322FF66, 0.5f, 0f, 0.7f).stars(0x8822FF66)
                .shape(NOTCH).keys(0xF5010603, 0xF5000301).line(0xFF22FF66, 1.3f).glow(0xFF00FF55, 5)
                .text(0xFFA8FFC4, 0xAA22FF66).labelGlow().accent(0xFF22FF66).radius(7).done();
        pro("pro_gold", "طلایی لوکس", true).bg(0xFF0B0A08, 0xFF16130C)
                .blob(0x33D4AF37, 0.5f, 0f, 0.7f).stars(0x77F5D27A)
                .shape(TICKET).keys(0xFF1C1912, 0xFF0D0C09).line(0xFFD4AF37, 1.2f).glow(0xFFE6C15A, 4).sheen(0x12FFE7A0)
                .text(0xFFF5D27A, 0xAAD4AF37).accent(0xFFD4AF37).radius(8).done();
        pro("pro_galaxy", "کهکشان پرستاره", true).bg(0xFF0B0620, 0xFF1A0B3A, 0xFF07041A)
                .blob(0x55A855F7, 0.2f, 0.3f, 0.5f).blob(0x443B82F6, 0.85f, 0.7f, 0.5f).blob(0x33EC4899, 0.6f, 0.1f, 0.35f)
                .stars(0xCCFFFFFF).shape(CHAMFER, 0.16f).keys(0x33FFFFFF, 0x1AFFFFFF).line(0x55FFFFFF, 1).sheen(0x14FFFFFF)
                .text(0xFFFFFFFF, 0xB3E9D5FF).accent(0xFFC084FC).radius(10).done();
        pro("pro_lava", "گدازه", true).bg(0xFF120303, 0xFF1F0602)
                .blob(0x66EF4444, 0.1f, 1f, 0.55f).blob(0x55F97316, 0.9f, 1f, 0.55f).stars(0x88FDBA74)
                .shape(HEX).keys(0xFF240A06, 0xFF120403).edge(0xFFEA580C).glow(0xFFF97316, 6).line(0xFFFB923C, 1)
                .text(0xFFFFEDD5, 0xFFFDBA74).accent(0xFFF97316).radius(8).done();
        pro("pro_ocean_glow", "اقیانوس درخشان", true).bg(0xFF021526, 0xFF03304A).diagonal()
                .blob(0x5506B6D4, 0.15f, 0.9f, 0.6f).blob(0x333B82F6, 0.9f, 0.1f, 0.5f)
                .shape(LEAF).keys(0x40FFFFFF, 0x14FFFFFF).line(0x6667E8F9, 1).glow(0xFF22D3EE, 3).sheen(0x1AFFFFFF)
                .text(0xFFFFFFFF, 0xCCA5F3FC).accent(0xFF22D3EE).radius(11).done();
        pro("pro_synthwave", "غروب نئونی", true).bg(0xFF1A0B2E, 0xFF3B0D4F, 0xFF7A1F4F)
                .blob(0x66FB923C, 0.5f, 1f, 0.5f).blob(0x44EC4899, 0.5f, 0.6f, 0.6f).stars(0x99FFFFFF)
                .shape(SLANT).keys(0xF0200B35, 0xF0140622).line(0xFFFF6AD5, 1.3f).glow(0xFFFF2DAA, 5)
                .text(0xFFFFFFFF, 0xFFFFB3E6).labelGlow().accent(0xFFFB923C).radius(8).done();

        // ---------------------------------------------------------------- pro, light
        pro("pro_pastel_rainbow", "رنگین‌کمان پاستلی", false).bg(0xFFFFFFFF, 0xFFF5F3FF)
                .blob(0x55FDA4AF, 0.05f, 0.1f, 0.45f).blob(0x5593C5FD, 0.95f, 0.2f, 0.45f).blob(0x5586EFAC, 0.5f, 1f, 0.5f)
                .shape(TICKET).keys(0xFFFFFFFF, 0xFFF8FAFC).line(0xFF94A3B8, 1.3f).rainbow().edge(0x22000000)
                .text(0xFF334155, 0xFF64748B).accent(0xFF8B5CF6).radius(10).done();
        pro("pro_glass_sky", "شیشه آسمانی", false).bg(0xFF7DD3FC, 0xFFE0F2FE).diagonal()
                .blob(0x66FFFFFF, 0.2f, 0.2f, 0.5f).blob(0x4438BDF8, 0.9f, 0.9f, 0.5f)
                .shape(LEAF).keys(0x8CFFFFFF, 0x59FFFFFF).line(0xCCFFFFFF, 1.2f).sheen(0x33FFFFFF)
                .text(0xFF0C4A6E, 0xFF0369A1).accent(0xFF0284C7).radius(12).done();
        pro("pro_candy_3d", "آبنبات سه‌بعدی", false).bg(0xFFFCE7F3, 0xFFFBCFE8)
                .shape(DROP).keys(0xFFFFFFFF, 0xFFFFF1F8).edge(0xFFF472B6).sheen(0x0F000000)
                .text(0xFF9D174D, 0xFFDB2777).accent(0xFFEC4899).radius(12).done();
        pro("pro_mint_3d", "نعنایی سه‌بعدی", false).bg(0xFFD1FAE5, 0xFFA7F3D0)
                .shape(HEX).keys(0xFFFFFFFF, 0xFFF0FDF4).edge(0xFF34D399).text(0xFF065F46, 0xFF059669)
                .accent(0xFF10B981).radius(12).done();
        pro("pro_sunrise", "طلوع", false).bg(0xFFFDBA74, 0xFFF9A8D4).diagonal()
                .blob(0x66FFFFFF, 0.8f, 0.1f, 0.5f)
                .shape(SLANT).keys(0x99FFFFFF, 0x66FFFFFF).line(0xCCFFFFFF, 1.2f).sheen(0x33FFFFFF)
                .text(0xFF7C2D12, 0xFF9A3412).accent(0xFFEA580C).radius(12).done();
        pro("pro_lavender_dream", "رویای یاسی", false).bg(0xFFEDE9FE, 0xFFDDD6FE)
                .blob(0x55F0ABFC, 0.1f, 0.9f, 0.5f).blob(0x5593C5FD, 0.9f, 0.1f, 0.5f)
                .shape(LEAF).keys(0xFFFFFFFF, 0xFFF5F3FF).edge(0xFFA78BFA).sheen(0x0A000000)
                .text(0xFF4C1D95, 0xFF7C3AED).accent(0xFF8B5CF6).radius(11).done();
        pro("pro_white_gold", "سفید طلایی", false).bg(0xFFFFFBF0, 0xFFF5EBD3)
                .stars(0x99D4AF37).shape(CHAMFER).keys(0xFFFFFFFF, 0xFFFFFCF5).line(0xFFD4AF37, 1.2f).glow(0x88E6C15A, 3)
                .text(0xFF5C4813, 0xFFA8892E).accent(0xFFC9A227).radius(9).done();
        pro("pro_aurora_light", "شفق روشن", false).bg(0xFFCCFBF1, 0xFFE9D5FF).diagonal()
                .blob(0x6667E8F9, 0.1f, 0.2f, 0.5f).blob(0x55F0ABFC, 0.9f, 0.8f, 0.5f)
                .shape(TECH).keys(0x99FFFFFF, 0x66FFFFFF).line(0xCCFFFFFF, 1.2f).sheen(0x26FFFFFF)
                .text(0xFF134E4A, 0xFF6B21A8).accent(0xFF14B8A6).radius(12).done();
        pro("pro_ice_crystal", "کریستال یخی", false).bg(0xFFE0F7FF, 0xFFBAE6FD)
                .stars(0xCCFFFFFF).blob(0x66FFFFFF, 0.5f, 0f, 0.6f)
                .shape(HEX).keys(0xB3FFFFFF, 0x80FFFFFF).line(0xFF67E8F9, 1.3f).glow(0xAA22D3EE, 4).sheen(0x33FFFFFF)
                .text(0xFF0E4F6E, 0xFF0891B2).accent(0xFF06B6D4).radius(10).done();
        pro("pro_rose_gold", "رزگلد", false).bg(0xFFFBE4DD, 0xFFF3C9C0)
                .stars(0x99FFFFFF).shape(TICKET).keys(0xFFFFF7F5, 0xFFFDEDE9).line(0xFFD8A39A, 1.2f).edge(0xFFE0A899)
                .text(0xFF7F3B33, 0xFFB76E64).accent(0xFFC9776A).radius(10).done();
        pro("pro_lemon_3d", "لیمویی سه‌بعدی", false).bg(0xFFFEF9C3, 0xFFFDE68A)
                .shape(SLANT).keys(0xFFFFFFFF, 0xFFFFFDF0).edge(0xFFFACC15).text(0xFF713F12, 0xFFA16207)
                .accent(0xFFCA8A04).radius(12).done();
        pro("pro_sky_3d", "آبی سه‌بعدی", false).bg(0xFFDBEAFE, 0xFFBFDBFE)
                .shape(CHAMFER, 0.2f).keys(0xFFFFFFFF, 0xFFF8FBFF).edge(0xFF60A5FA).sheen(0x0A000000)
                .text(0xFF1E3A8A, 0xFF2563EB).accent(0xFF2563EB).radius(12).done();
    }

    static {
        // ---------------------------------------------------------------- future, dark
        pro("fx_keycap_midnight", "کی‌کپ نیمه‌شب", true).bg(0xFF16161F, 0xFF0A0A10)
                .blob(0x221D4ED8, 0.5f, 0f, 0.7f)
                .keycap().shape(CHAMFER, 0.2f).keys(0xFF26262F, 0xFF14141A).edge(0xFF4A4A57)
                .text(0xFF8FD3FF, 0xFF6BA8D9).accent(0xFF3B6BFF).radius(7).done();
        pro("fx_hud", "رابط آینده", true).bg(0xFF0C2136, 0xFF06111C)
                .art(ProPainter.ART_HEX, 0x2632D7FF, 0).blob(0x3300C8FF, 0.5f, 0.1f, 0.6f)
                .shape(CHAMFER).keys(0xFF0F2C44, 0xFF0A2135).line(0xFF3BE3FF, 1.3f).glow(0xFF00D5FF, 3)
                .fx(ProPainter.FX_INNER_GLOW, 0xFF3BE3FF)
                .text(0xFF8AF2FF, 0xAA3BE3FF).accent(0xFF00D5FF).done();
        pro("fx_heartbeat", "ضربان قلب نئونی", true).bg(0xFF000716, 0xFF00030B)
                .blob(0x441E40FF, 0.5f, 0.45f, 0.55f).stars(0xAA7FB2FF)
                .art(ProPainter.ART_HEARTBEAT, 0xFF2F7BFF, 0xFF66AAFF)
                .shape(CHAMFER).keys(0xD9000816, 0xD900040C).line(0xFF3D8BFF, 1.3f).glow(0xFF2F6BFF, 5)
                .text(0xFFE8F1FF, 0xCC7FB2FF).labelGlow().accent(0xFF3D8BFF).done();
        pro("fx_blue_inferno", "دوزخ آبی", true).bg(0xFF020518, 0xFF000208)
                .art(ProPainter.ART_FLAMES, 0xFF1E5BFF, 0xFF8DBBFF).stars(0x8860A5FA)
                .shape(TECH).keys(0xFF060A18, 0xFF02040A).line(0xFF3B74FF, 1).glow(0xFF2F6BFF, 4)
                .fx(ProPainter.FX_FLAME, 0xFF2F7BFF)
                .text(0xFFFFFFFF, 0xFF9DC2FF).accent(0xFF3B74FF).radius(9).done();
        // redesigned in 2.6: obsidian keys with a red underglow on carbon
        pro("fx_crimson_gloss", "قرمز براق", true).bg(0xFF151518, 0xFF060607)
                .blob(0x55E11D2E, 0.5f, 1.1f, 0.6f).blob(0x26E11D2E, 1f, -0.1f, 0.45f)
                .art(ProPainter.ART_CARBON, 0x12FFFFFF, 0)
                .shape(SLANT).keys(0xFF2B2B30, 0xFF0D0D0F).edge(0xFF9F1220).glow(0xFFFF2A3D, 4)
                .line(0xFF46464E, 0.8f).sheen(0x1FFFFFFF)
                .text(0xFFFFFFFF, 0xFFFF7A84).accent(0xFFFF2A3D).radius(7).done();
        pro("fx_emerald_glint", "زمرد درخشان", true).bg(0xFF06380F, 0xFF021A07)
                .blob(0x5522C55E, 0.2f, 0.2f, 0.5f).blob(0x4484CC16, 0.9f, 0.9f, 0.5f)
                .art(ProPainter.ART_SPARKLE, 0xFF9DFF6B, 0)
                .shape(CHAMFER, 0.18f).keys(0xFF14501A, 0xFF051F08).line(0xFF5EF06E, 1.2f).glow(0xFF22FF44, 3)
                .sheen(0x2EFFFFFF).text(0xFFE1FFD6, 0xFF9AE6A6).accent(0xFF4ADE5A).radius(8).done();
        pro("fx_synth_city", "شهر آینده", true).bg(0xFF0D0221, 0xFF26074D, 0xFF3A0A4F)
                .art(ProPainter.ART_GRID, 0xFFFF2BD6, 0xFFFFD319).stars(0xAAFFFFFF)
                .shape(CHAMFER).keys(0xE61A0638, 0xE60D0221).line(0xFF00F0FF, 1.2f).glow(0xFF00F0FF, 4)
                .text(0xFFE6FDFF, 0xCC7DF9FF).labelGlow().accent(0xFFFF2BD6).done();
        pro("fx_circuit", "مدار الکترونیکی", true).bg(0xFF04130F, 0xFF010705)
                .art(ProPainter.ART_CIRCUIT, 0x6632FFB4, 0).blob(0x2200FFA2, 0.5f, 0.5f, 0.6f)
                .shape(NOTCH).keys(0xF0072219, 0xF003100C).line(0xFF2BFFB0, 1.1f).glow(0xFF00FFA2, 3)
                .fx(ProPainter.FX_INNER_GLOW, 0xFF2BFFB0)
                .text(0xFFA6FFE4, 0xAA2BFFB0).accent(0xFF2BFFB0).radius(4).done();
        pro("fx_keycap_neon", "کی‌کپ رنگین‌کمان", true).bg(0xFF09090D, 0xFF000000)
                .keycap().shape(TECH).keys(0xFF22222B, 0xFF14141A).line(0xFFFFFFFF, 1).glow(0xFFFFFFFF, 4).rainbow()
                .text(0xFFFFFFFF, 0xCCFFFFFF).accent(0xFFEC4899).radius(7).done();
        pro("fx_ocean_waves", "امواج آینده", true).bg(0xFF031427, 0xFF062A44)
                .art(ProPainter.ART_WAVES, 0xFF22D3EE, 0xFF6366F1).stars(0x99BAE6FD)
                .shape(LEAF).glass().keys(0x38FFFFFF, 0x10FFFFFF).line(0x99FFFFFF, 1.1f)
                .text(0xFFFFFFFF, 0xCCBAE6FD).accent(0xFF22D3EE).done();
        // ---------------------------------------------------------------- future, light
        pro("fx_bubble_galaxy", "قطره کهکشانی", false).bg(0xFF8EC5FF, 0xFFC4A8FF, 0xFFF7B8E8).diagonal()
                .blob(0x88FFFFFF, 0.5f, 0.45f, 0.45f).stars(0xEEFFFFFF)
                .shape(DROP).glass().keys(0xA6FFFFFF, 0x59FFFFFF).line(0xE6FFFFFF, 1.2f)
                .text(0xFF2A1F5C, 0xFF5B4B9A).accent(0xFF8B5CF6).done();
        pro("fx_keycap_cream", "کی‌کپ کرم", false).bg(0xFFE9E4DA, 0xFFD6CFC0)
                .keycap().shape(HEX).keys(0xFFFFFDF8, 0xFFF0EBE1)
                .text(0xFF3D3529, 0xFF8C7F6A).accent(0xFFE07B39).radius(7).done();
        pro("fx_holo_pearl", "مروارید هولوگرام", false).bg(0xFFF5F3FF, 0xFFE0F2FE, 0xFFFCE7F3).diagonal()
                .art(ProPainter.ART_HEX, 0x448B5CF6, 0).stars(0xCCFFFFFF)
                .shape(HEX).glass().keys(0xCCFFFFFF, 0x99FFFFFF).line(0xFFFFFFFF, 1.3f).rainbow()
                .text(0xFF3B2F63, 0xFF7C6FA8).accent(0xFF8B5CF6).done();
        pro("fx_hud_light", "رابط روشن", false).bg(0xFFEAF6FF, 0xFFD3EAFE)
                .art(ProPainter.ART_CIRCUIT, 0x400EA5E9, 0)
                .shape(CHAMFER).keys(0xFFFFFFFF, 0xFFEFF8FF).line(0xFF0EA5E9, 1.2f).glow(0x660EA5E9, 3)
                .fx(ProPainter.FX_INNER_GLOW, 0xFF0EA5E9)
                .text(0xFF075985, 0xFF0284C7).accent(0xFF0284C7).done();
    }

}
