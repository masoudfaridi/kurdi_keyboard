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

    private KeyboardTheme(String id, String name, int bg, int key, int keyPressed, int fn, int fnPressed,
                          int text, int hint, int accent, int onAccent, int shadow, int stroke,
                          float radiusDp, boolean borderless) {
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
    }

    public boolean isDark() {
        return luminance(bg) < 0.5;
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
                accent, onAccent, shadow, stroke, radius, borderless));
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
        for (KeyboardTheme t : THEMES.values()) if (t.isDark() == dark) out.add(t);
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
}
