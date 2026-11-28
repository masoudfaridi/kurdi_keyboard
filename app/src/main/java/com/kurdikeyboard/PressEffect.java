package com.kurdikeyboard;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;

import java.util.List;
import java.util.Random;

/**
 * Every popup of the keyboard, drawn in one style (setting "popup theme"): the letter shown
 * above a pressed key, and the popups with several options (the long-press menu of a key,
 * the language list of the space bar).
 *
 * SIMPLE is the plain look of 2.x. The others are animated light effects, all drawn in code:
 * BEAM       a triangular beam of light from the finger up to a glowing frame
 * LIGHTNING  lightning strikes from the finger into the frame, sparks fly, electricity crackles
 * RADIANCE   rays of light turn behind the frame, shock rings spread from the key
 * PLASMA     energy orbs full of electric arcs, tied to the finger by a thin arc
 * HOLOGRAM   a projector on the key casts a flickering hologram with scan lines
 * FIRE       low flames burn under the frame, a little flame under the finger, embers rise
 * SMOKE      smoke rises from the finger and curls around the frame, which appears out of it
 * FROST      ice crystals grow on the frame, a frost star under the finger, snow falls
 * STARDUST   glittering stars spiral from the finger up around the frame
 * BUBBLES    bubbles rise from the finger and pop at a glossy frame
 * All of them take the color of the theme: blue fire and blue smoke on a blue theme.
 * AUTO picks BEAM for dark pro themes and SIMPLE for the others.
 *
 * Frames take the shape and colors of the theme's keys. Used by the keyboard (KKeyboardView
 * through PressEffectView) and by the previews in PopupStyleActivity.
 */
final class PressEffect {

    static final String AUTO = "auto", SIMPLE = "simple", BEAM = "beam", LIGHTNING = "lightning",
            RADIANCE = "radiance", PLASMA = "plasma", HOLOGRAM = "hologram", FIRE = "fire", SMOKE = "smoke",
            FROST = "frost", STARDUST = "stardust", BUBBLES = "bubbles";
    /** in the order of the settings screen */
    static final String[] ALL = {AUTO, SIMPLE, BEAM, LIGHTNING, FIRE, SMOKE, RADIANCE, PLASMA, HOLOGRAM, FROST,
            STARDUST, BUBBLES};

    private PressEffect() {}

    static String setting(Context c) {
        return c.getSharedPreferences("MySharedPreference", Context.MODE_PRIVATE)
                .getString(c.getString(R.string.prefpopupstyle), AUTO);
    }

    /** the style to draw for a setting and a theme */
    static String resolve(String style, KeyboardTheme t) {
        if (style == null || AUTO.equals(style)) return t != null && t.isPro() && t.isDark() ? BEAM : SIMPLE;
        for (String s : ALL) if (s.equals(style)) return style;
        return SIMPLE;
    }

    static boolean fancy(String style) {
        return style != null && !SIMPLE.equals(style);
    }

    /** distance between key and frame the effect needs to be seen (dp) */
    static float minGapDp(String style) {
        switch (style) {
            case BEAM: case HOLOGRAM: case LIGHTNING: case SMOKE: case BUBBLES: return 38;
            case FIRE: case STARDUST: return 34;
            case PLASMA: case FROST: return 30;
            case RADIANCE: return 26;
            default: return 0;
        }
    }

    /** room around the frame the effect draws into (px) */
    static float margin(String style, float bw, float bh, float d) {
        switch (style) {
            case RADIANCE: return Math.max(bw, bh) * 0.85f;
            case PLASMA: return Math.max(bw, bh) * 0.58f;
            case LIGHTNING: case FIRE: case FROST: case BUBBLES: return Math.max(bw, bh) * 0.5f + 8 * d;
            case SMOKE: case STARDUST: return Math.max(bw, bh) * 0.75f;
            default: return Math.max(bw, bh) * 0.25f + 8 * d; // the frame's halo fades out inside it
        }
    }

    /** main color of the effect: the theme's glow, outline or accent color */
    static int color(KeyboardTheme t, float fx, float fy) {
        if (t.pro != null) {
            KeyboardTheme.Pro x = t.pro;
            if (x.rainbow) return ProPainter.rainbow(fx, fy, t.isDark());
            if (x.glow != 0 && !grayish(x.glow)) return x.glow | 0xFF000000;
            if (x.line != 0 && !grayish(x.line)) return x.line | 0xFF000000;
        }
        return t.accent | 0xFF000000;
    }

    private static boolean grayish(int c) {
        int r = Color.red(c), g = Color.green(c), b = Color.blue(c);
        return Math.max(r, Math.max(g, b)) - Math.min(r, Math.min(g, b)) < 40;
    }

    /** one option of a popup with several options; {@code r} in the canvas' coordinates */
    static final class Item {
        final String text;
        final Drawable icon; // drawn instead of the text when set
        final RectF r = new RectF();

        Item(String text, Drawable icon) {
            this.text = text;
            this.icon = icon;
        }
    }

    // ------------------------------------------------------------------ the letter popup

    private static final Path P1 = new Path(), P2 = new Path(), PB = new Path(), PC = new Path();
    private static final RectF R1 = new RectF(), R2 = new RectF();
    private static final float[] PT = new float[4];
    private static final Typeface BOLD = Typeface.create(Typeface.DEFAULT, Typeface.BOLD);

    /**
     * Draws the popup of a pressed key. Coordinates are those of the canvas.
     * @param bubble where the letter is shown
     * @param key    the pressed key
     * @param tipX   where the light leaves the key (under the finger)
     * @param t      milliseconds since the key went down
     * @param seed   different for every press (shapes of lightning and arcs)
     */
    static void draw(Canvas c, Paint p, String style, KeyboardTheme theme, RectF bubble, RectF key,
                     float tipX, float tipY, long t, int seed, String label, boolean alt, float d, int color) {
        reset(p);
        boolean dark = theme.isDark();
        switch (style) {
            case BEAM:
                beamTo(c, p, bubble.centerX(), bubble.bottom - 2 * d, bubble.width() * 0.4f, tipX, tipY, t, seed,
                        color, d, dark);
                frame(c, p, theme, bubble, color, d, label, alt, pop(t), 0);
                break;
            case LIGHTNING:
                if (t < 440) strike(c, p, tipX, tipY, bubble.centerX(), bubble.bottom - d, bubble.width(), t, seed, color, d);
                sparks(c, p, tipX, tipY, t, seed, color, d);
                frame(c, p, theme, bubble, color, d, label, alt, pop(t), t < 170 ? 1 - t / 170f : 0);
                c.save();
                c.scale(pop(t), pop(t), bubble.centerX(), bubble.bottom);
                crackle(c, p, bubble, t, seed, color, d);
                c.restore();
                break;
            case RADIANCE:
                rays(c, p, bubble.centerX(), bubble.centerY(),
                        Math.max(bubble.width(), bubble.height()) * 1.05f * Math.max(0, easeOutBack(t / 220f)), t, color, d, dark);
                rings(c, p, tipX, tipY, t, color, d);
                frame(c, p, theme, bubble, color, d, label, alt, pop(t), 0);
                break;
            case PLASMA: {
                float pop = 0.6f + 0.4f * easeOutBack(t / 170f);
                float ro = Math.min(bubble.width(), bubble.height()) * 0.55f * pop;
                tether(c, p, tipX, tipY, bubble.centerX(), bubble.centerY() + ro * 0.9f, t, seed, color, d);
                orb(c, p, dark, bubble.centerX(), bubble.centerY(), ro, true, t, seed, color, d);
                R1.set(bubble.centerX() - ro, bubble.centerY() - ro, bubble.centerX() + ro, bubble.centerY() + ro);
                letter(c, p, R1, label, dark || alt ? 0xFFFFFFFF : ProPainter.mix(color, 0xFF000000, 0.4f), color, d, true, 0);
                break;
            }
            case HOLOGRAM:
                hologram(c, p, theme, bubble, key, tipX, tipY, t, color, d, label, alt);
                break;
            case FIRE:
                fireBed(c, p, bubble, 7, bubble.height() * 0.42f, true, t, seed, color, d, dark);
                frame(c, p, theme, bubble, color, d, label, alt, pop(t), 0);
                fireRise(c, p, bubble, tipX, tipY, t, seed, color, d, dark, true);
                break;
            case SMOKE: {
                smoke(c, p, bubble, tipX, tipY, t, seed, color, d, dark, false);
                float in = easeOut(t / 260f); // the frame appears out of the smoke
                int sv = in < 1 ? c.saveLayerAlpha(bubble.left - 24 * d, bubble.top - 24 * d, bubble.right + 24 * d,
                        bubble.bottom + 24 * d, (int) (255 * in)) : -1;
                frame(c, p, theme, bubble, color, d, label, alt, pop(t), 0);
                if (sv >= 0) c.restoreToCount(sv);
                smoke(c, p, bubble, tipX, tipY, t, seed, color, d, dark, true);
                break;
            }
            case FROST: {
                int ice = ice(color);
                frostMist(c, p, bubble, t, ice, d, dark);
                frame(c, p, theme, bubble, ice, d, label, alt, pop(t), 0);
                frost(c, p, bubble, tipX, tipY, t, seed, ice, d);
                break;
            }
            case STARDUST:
                stardust(c, p, bubble, tipX, tipY, t, seed, color, d, dark);
                frame(c, p, theme, bubble, color, d, label, alt, pop(t), 0);
                twinkles(c, p, bubble, t, color, d, dark);
                break;
            case BUBBLES: {
                frame(c, p, theme, bubble, color, d, label, alt, pop(t), 0);
                c.save();
                c.scale(pop(t), pop(t), bubble.centerX(), bubble.bottom);
                gloss(c, p, ProPainter.shapePath(theme.pro, bubble, Math.min(bubble.width(), bubble.height()) * 0.24f,
                        d, PB), bubble, dark);
                c.restore();
                bubbles(c, p, bubble, tipX, tipY, t, seed, color, d, dark);
                break;
            }
            default:
                simple(c, p, theme, bubble, label, alt, d);
        }
        reset(p);
    }

    private static void reset(Paint p) {
        p.setShader(null);
        p.clearShadowLayer();
        p.setColorFilter(null);
        p.setStyle(Paint.Style.FILL);
    }

    /** the plain bubble: the theme's key color, the letter in the label color */
    static void simple(Canvas c, Paint p, KeyboardTheme theme, RectF b, String text, boolean alt, float d) {
        R1.set(b.left, b.top, b.right, b.bottom - 2 * d);
        p.setStyle(Paint.Style.FILL);
        p.setColor(0x40000000);
        R1.offset(0, 2 * d);
        c.drawRoundRect(R1, 12 * d, 12 * d, p);
        R1.offset(0, -2 * d);
        p.setColor(simpleFill(theme));
        c.drawRoundRect(R1, 12 * d, 12 * d, p);
        if (theme.stroke != 0) {
            p.setStyle(Paint.Style.STROKE);
            p.setStrokeWidth(d);
            p.setColor(theme.stroke);
            c.drawRoundRect(R1, 12 * d, 12 * d, p);
            p.setStyle(Paint.Style.FILL);
        }
        p.setColor(alt ? theme.accent | 0xFF000000 : theme.text);
        p.setTextAlign(Paint.Align.CENTER);
        float size = R1.height() * 0.5f;
        p.setTextSize(size);
        while (p.measureText(text) > R1.width() - 8 * d && size > 10 * d) {
            size -= d;
            p.setTextSize(size);
        }
        c.drawText(text, R1.centerX(), R1.centerY() + size / 3f, p);
    }

    private static int simpleFill(KeyboardTheme theme) {
        int bg = theme.isDark() ? theme.keyPressed : 0xFFFFFFFF;
        return theme.pro != null ? ProPainter.over(theme.bg | 0xFF000000, bg) : bg; // see-through pro keys
    }

    private static float pop(long t) {
        return 0.72f + 0.28f * easeOutBack(t / 130f);
    }

    /** a frame in the theme's key shape with the letter inside */
    private static void frame(Canvas c, Paint p, KeyboardTheme theme, RectF b, int color, float d,
                              String label, boolean alt, float pop, float flash) {
        boolean dark = theme.isDark();
        c.save();
        c.scale(pop, pop, b.centerX(), b.bottom);
        Path s = ProPainter.shapePath(theme.pro, b, Math.min(b.width(), b.height()) * 0.24f, d, PB);
        frameBody(c, p, s, b, color, d, dark, 0.75f, flash);
        int fill = dark ? ProPainter.mix(color, 0xFFFFFFFF, alt ? 0.8f : 0.55f)
                : ProPainter.mix(color, 0xFF000000, alt ? 0.15f : 0.35f);
        letter(c, p, b, label, fill, color, d, dark || alt, 0);
        c.restore();
    }

    /**
     * The glowing frame: a halo (halo = its size against the frame, 0 = none), dark glass
     * (light themes: white), light inside the edge, and the edge itself with a white-hot core.
     */
    private static void frameBody(Canvas c, Paint p, Path s, RectF b, int color, float d, boolean dark,
                                  float halo, float flash) {
        if (halo > 0) {
            float hr = Math.max(b.width(), b.height()) * (halo + 0.25f * flash);
            p.setColor(Color.BLACK);
            p.setShader(new RadialGradient(b.centerX(), b.centerY(), hr,
                    a(color, (int) ((dark ? 110 : 80) + 120 * flash)), a(color, 0), Shader.TileMode.CLAMP));
            c.drawCircle(b.centerX(), b.centerY(), hr, p);
        }
        int top = dark ? ProPainter.mix(0xFF000000, color, 0.18f) : 0xFFFFFFFF;
        int bottom = dark ? 0xFF030305 : ProPainter.mix(0xFFFFFFFF, color, 0.12f);
        p.setColor(Color.BLACK);
        p.setShader(new LinearGradient(0, b.top, 0, b.bottom, top, bottom, Shader.TileMode.CLAMP));
        c.drawPath(s, p);
        p.setShader(null);
        c.save();
        c.clipPath(s);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(8 * d);
        p.setColor(a(color, dark ? 80 : 50));
        c.drawPath(s, p);
        c.restore();
        p.setStrokeJoin(Paint.Join.ROUND);
        p.setStrokeWidth(5 * d);
        p.setColor(a(color, dark ? 70 : 45));
        c.drawPath(s, p);
        p.setStrokeWidth(2.2f * d);
        p.setColor(color);
        c.drawPath(s, p);
        p.setStrokeWidth(0.8f * d);
        p.setColor(dark ? 0xAAFFFFFF : 0xE6FFFFFF);
        c.drawPath(s, p);
        p.setStyle(Paint.Style.FILL);
    }

    private static void letter(Canvas c, Paint p, RectF b, String text, int fill, int glow, float d,
                               boolean glowOn, float dx) {
        p.setStyle(Paint.Style.FILL);
        p.setTextAlign(Paint.Align.CENTER);
        p.setTypeface(BOLD);
        float size = b.height() * 0.5f;
        p.setTextSize(size);
        while (p.measureText(text) > b.width() - 10 * d && size > 10 * d) {
            size -= d;
            p.setTextSize(size);
        }
        if (glowOn) p.setShadowLayer(6 * d, 0, 0, glow);
        p.setColor(fill);
        c.drawText(text, b.centerX() + dx, b.centerY() + size / 3f, p);
        p.clearShadowLayer();
        p.setTypeface(Typeface.DEFAULT);
    }

    // ------------------------------------------------------------------ popups with several options

    /**
     * A popup with several options, in the same style as the letter popup: the panel takes the
     * theme's colors and key shape, the chosen option lights up in a frame of its own, and the
     * effect follows the choice (a beam or lightning from the finger to it, rays around it,
     * plasma orbs, a hologram of the whole panel).
     * @param sel  the chosen option, -1 none yet
     * @param tSel milliseconds since the choice changed (or since the popup opened)
     * @param tipX the finger
     * @param t    milliseconds since the popup opened
     */
    static void menu(Canvas c, Paint p, String style, KeyboardTheme theme, RectF panel, List<Item> items, int sel,
                     long tSel, float tipX, float tipY, long t, int seed, float d, int color) {
        reset(p);
        boolean dark = theme.isDark();
        Item chosen = sel >= 0 && sel < items.size() ? items.get(sel) : null;
        float open = 0.86f + 0.14f * easeOutBack(t / 150f);
        long since = Math.min(t, tSel); // the light answers the opening and every new choice
        c.save();
        c.scale(open, open, panel.centerX(), panel.bottom);
        float targetX = chosen != null ? chosen.r.centerX() : panel.centerX();
        float targetW = chosen != null ? chosen.r.width() : Math.min(panel.width(), panel.height() * 2);

        // the light reaching up from the finger, behind the panel
        switch (style) {
            case BEAM:
                beamTo(c, p, targetX, panel.bottom - 2 * d, targetW * 0.4f, tipX, tipY, t, seed, color, d, dark);
                break;
            case LIGHTNING:
                if (since < 440) strike(c, p, tipX, tipY, targetX, panel.bottom - d, targetW, since,
                        seed + sel * 131, color, d);
                sparks(c, p, tipX, tipY, t, seed, color, d);
                break;
            case RADIANCE:
                rings(c, p, tipX, tipY, t, color, d);
                break;
            case PLASMA:
                if (chosen != null) tether(c, p, tipX, tipY, targetX, panel.bottom - d, t, seed, color, d);
                break;
            case HOLOGRAM:
                projector(c, p, panel.left + 6 * d, panel.right - 6 * d, panel.bottom, tipX, tipY, 16 * d, t,
                        color, d, dark);
                break;
            case FIRE:
                fireBed(c, p, panel, Math.max(4, (int) (panel.width() / (13 * d))),
                        Math.min(panel.height() * 0.4f, 26 * d), false, t, seed, color, d, dark);
                break;
            case SMOKE:
                smoke(c, p, chosen != null ? chosen.r : panel, tipX, tipY, t, seed, color, d, dark, false);
                break;
            case FROST:
                frostMist(c, p, panel, t, ice(color), d, dark);
                break;
            case STARDUST:
                stardust(c, p, chosen != null ? chosen.r : panel, tipX, tipY, t, seed + sel * 7, color, d, dark);
                break;
            default:
                break;
        }
        int frameColor = FROST.equals(style) ? ice(color) : color;

        // the panel
        Path s = panelPath(theme, panel, d, PC);
        if (SIMPLE.equals(style)) {
            simplePanel(c, p, theme, s, d);
        } else if (HOLOGRAM.equals(style)) {
            holoPanel(c, p, s, panel, t, color, d, dark);
        } else {
            frameBody(c, p, s, panel, frameColor, d, dark, 0, 0);
        }

        // rays bursting from the chosen option
        if (RADIANCE.equals(style) && chosen != null) {
            rays(c, p, chosen.r.centerX(), chosen.r.centerY(),
                    Math.max(chosen.r.width(), chosen.r.height()) * Math.max(0, easeOutBack(since / 200f)), t, color, d, dark);
        }
        // the options
        for (int i = 0; i < items.size(); i++) {
            item(c, p, style, theme, items.get(i), i == sel, tSel, t, seed, frameColor, d, dark);
        }
        // electricity running around the chosen option
        if (LIGHTNING.equals(style) && chosen != null) {
            R2.set(chosen.r);
            R2.inset(2.5f * d, 2.5f * d);
            crackle(c, p, R2, t, seed + sel, color, d);
        }
        // in front: flames, smoke, frost, stars or bubbles going to the chosen option
        RectF target = chosen != null ? chosen.r : panel;
        switch (style) {
            case FIRE:
                fireRise(c, p, target, tipX, tipY, t, seed, color, d, dark, chosen != null);
                break;
            case SMOKE:
                if (chosen != null) smoke(c, p, chosen.r, tipX, tipY, t, seed, color, d, dark, true);
                break;
            case FROST:
                frost(c, p, panel, tipX, tipY, t, seed, frameColor, d);
                break;
            case STARDUST:
                if (chosen != null) twinkles(c, p, chosen.r, t, color, d, dark);
                break;
            case BUBBLES:
                bubbles(c, p, target, tipX, tipY, t, seed, color, d, dark);
                break;
            default:
                break;
        }
        c.restore();
        reset(p);
    }

    private static final KeyboardTheme.Pro PANEL = new KeyboardTheme.Pro();

    /** the outline of a whole panel: the theme's key shape with corner cuts of a fixed size */
    private static Path panelPath(KeyboardTheme theme, RectF r, float d, Path out) {
        int shape = theme.pro != null ? theme.pro.shape : ProPainter.SHAPE_ROUND;
        float m = Math.max(1, Math.min(r.width(), r.height())), want;
        switch (shape) {
            case ProPainter.SHAPE_CHAMFER: want = 9 * d; break;
            case ProPainter.SHAPE_HEX: want = 11 * d; break;
            case ProPainter.SHAPE_TECH: want = 12 * d; break;
            case ProPainter.SHAPE_LEAF: want = 18 * d; break;
            case ProPainter.SHAPE_TICKET: want = 9 * d; break;
            case ProPainter.SHAPE_SLANT:
            case ProPainter.SHAPE_NOTCH: want = 0; break;
            default:
                out.reset();
                out.addRoundRect(r, 14 * d, 14 * d, Path.Direction.CW);
                return out;
        }
        PANEL.shape = shape;
        PANEL.cut = want > 0 ? want / m : 0;
        return ProPainter.shapePath(PANEL, r, 14 * d, d, out);
    }

    /** the outline of one option: the theme's key shape */
    private static Path cellPath(KeyboardTheme theme, RectF r, float d, Path out) {
        if (theme.pro == null || theme.pro.shape == ProPainter.SHAPE_ROUND || theme.pro.shape == ProPainter.SHAPE_BUBBLE) {
            out.reset();
            out.addRoundRect(r, 9 * d, 9 * d, Path.Direction.CW);
            return out;
        }
        return ProPainter.shapePath(theme.pro, r, 9 * d, d, out);
    }

    private static void simplePanel(Canvas c, Paint p, KeyboardTheme theme, Path s, float d) {
        c.save();
        c.translate(0, 2 * d);
        p.setColor(0x33000000);
        c.drawPath(s, p);
        c.restore();
        p.setColor(simpleFill(theme));
        c.drawPath(s, p);
        if (theme.stroke != 0) {
            p.setStyle(Paint.Style.STROKE);
            p.setStrokeWidth(d);
            p.setColor(theme.stroke);
            c.drawPath(s, p);
            p.setStyle(Paint.Style.FILL);
        }
    }

    /** a hologram panel: dim, tinted, with moving scan lines and corner brackets */
    private static void holoPanel(Canvas c, Paint p, Path s, RectF b, long t, int color, float d, boolean dark) {
        p.setColor(dark ? a(ProPainter.mix(0xFF000000, color, 0.12f), 228) : a(0xFFFFFF, 232));
        c.drawPath(s, p);
        p.setColor(a(color, dark ? 46 : 30));
        c.drawPath(s, p);
        c.save();
        c.clipPath(s);
        float step = 3.2f * d, off = (t * 0.03f * d) % step;
        p.setColor(a(color, dark ? 55 : 40));
        for (float y = b.bottom - off; y > b.top; y -= step) c.drawRect(b.left, y, b.right, y + 0.9f * d, p);
        c.restore();
        brackets(c, p, b, color, d);
    }

    private static void item(Canvas c, Paint p, String style, KeyboardTheme theme, Item it, boolean chosen,
                             long tSel, long t, int seed, int color, float d, boolean dark) {
        RectF r = it.r;
        int idle = theme.text | 0xFF000000;
        if (dark && fancy(style)) idle = ProPainter.mix(idle, color, 0.25f);
        if (SIMPLE.equals(style)) {
            int fg = idle;
            if (chosen) {
                R1.set(r);
                R1.inset(2 * d, 2 * d);
                int accent = theme.accent | 0xFF000000;
                p.setColor(accent);
                c.drawPath(cellPath(theme, R1, d, P2), p);
                fg = KeyboardTheme.luminance(accent) > 0.62 ? 0xFF111111 : 0xFFFFFFFF;
            }
            label(c, p, it, r, fg, 0, false, 0, d, 1f);
            return;
        }
        if (PLASMA.equals(style) && r.width() > r.height() * 1.6f) {
            // a wide option (a language name): its frame fills with electric arcs instead of an orb
            if (!chosen) {
                label(c, p, it, r, idle, 0, false, 0, d, 1f);
                return;
            }
            R1.set(r);
            R1.inset(2.5f * d, 2.5f * d);
            Path s = cellPath(theme, R1, d, P2);
            frameBody(c, p, s, R1, color, d, dark, 0.5f, 0);
            c.save();
            c.clipPath(s);
            Random rnd = new Random(seed * 31L + t / 55);
            float cx = R1.centerX(), cy = R1.centerY();
            for (int k = 0; k < 6; k++) {
                float ex = R1.left + rnd.nextFloat() * R1.width(), ey = rnd.nextBoolean() ? R1.top : R1.bottom;
                glow(c, p, line(bolt(cx, cy, ex, ey, R1.height() * 0.3f, 4, rnd), PB), color, 0.9f * d, 0.6f);
            }
            c.restore();
            label(c, p, it, r, dark ? 0xFFFFFFFF : ProPainter.mix(color, 0xFF000000, 0.35f), color, true, 0, d, 1.05f);
            return;
        }
        if (PLASMA.equals(style)) {
            float rr = Math.min(r.width(), r.height()) * (chosen ? 0.48f : 0.36f);
            orb(c, p, dark, r.centerX(), r.centerY(), rr, chosen, t, seed, color, d);
            int fg = chosen ? (dark ? 0xFFFFFFFF : ProPainter.mix(color, 0xFF000000, 0.4f)) : idle;
            label(c, p, it, r, fg, color, chosen, 0, d, chosen ? 1.05f : 0.85f);
            return;
        }
        if (!chosen) {
            label(c, p, it, r, idle, 0, false, 0, d, 1f);
            return;
        }
        float bump = 1 + 0.12f * (1 - easeOut(tSel / 140f));
        c.save();
        c.scale(bump, bump, r.centerX(), r.centerY());
        R1.set(r);
        R1.inset(2.5f * d, 2.5f * d);
        Path s = cellPath(theme, R1, d, P2);
        if (HOLOGRAM.equals(style)) {
            p.setColor(a(color, dark ? 60 : 40));
            c.drawPath(s, p);
            brackets(c, p, R1, color, d);
            float fl = 0.85f + 0.15f * (float) Math.sin(t * 0.07);
            label(c, p, it, r, a(0x00FFFF, (int) (150 * fl)), 0, false, -1.4f * d, d, 1.1f);
            label(c, p, it, r, a(0xFF00FF, (int) (150 * fl)), 0, false, 1.4f * d, d, 1.1f);
            label(c, p, it, r, dark ? ProPainter.mix(color, 0xFFFFFFFF, 0.75f) : ProPainter.mix(color, 0xFF000000, 0.35f),
                    color, true, 0, d, 1.1f);
        } else {
            frameBody(c, p, s, R1, color, d, dark, 0.55f, 0);
            if (BUBBLES.equals(style)) gloss(c, p, s, R1, dark);
            int fg = dark ? ProPainter.mix(color, 0xFFFFFFFF, 0.65f) : ProPainter.mix(color, 0xFF000000, 0.3f);
            label(c, p, it, r, fg, color, dark, 0, d, 1.1f);
        }
        c.restore();
    }

    /** an option's text or icon */
    private static void label(Canvas c, Paint p, Item it, RectF r, int fill, int glow, boolean glowOn, float dx,
                              float d, float scale) {
        float cx = r.centerX() + dx, cy = r.centerY();
        if (it.icon != null) {
            int s = (int) (Math.min(r.width(), r.height()) * 0.48f * scale);
            it.icon.setColorFilter(new PorterDuffColorFilter(fill, PorterDuff.Mode.SRC_IN));
            it.icon.setBounds((int) (cx - s / 2f), (int) (cy - s / 2f), (int) (cx + s / 2f), (int) (cy + s / 2f));
            it.icon.draw(c);
            return;
        }
        if (it.text == null) return;
        p.setStyle(Paint.Style.FILL);
        p.setTextAlign(Paint.Align.CENTER);
        float size = r.height() * 0.42f * scale;
        p.setTextSize(size);
        while (p.measureText(it.text) > r.width() - 6 * d && size > 8 * d) {
            size -= d;
            p.setTextSize(size);
        }
        if (glowOn) p.setShadowLayer(5 * d, 0, 0, glow);
        p.setColor(fill);
        c.drawText(it.text, cx, cy + size / 3f, p);
        p.clearShadowLayer();
    }

    // ------------------------------------------------------------------ light beam

    /** a cone of light from the finger (tip) up to a target (top centre, half width at the top) */
    private static void beamTo(Canvas c, Paint p, float bx, float topY, float fullHalf, float tipX, float tipY,
                               long t, int seed, int color, float d, boolean dark) {
        float len = tipY - topY;
        if (len < 8 * d) return;
        float grow = easeOut(t / 120f);
        float curTop = tipY - len * grow, half = fullHalf * grow;
        Path cone = P1;
        cone.reset();
        cone.moveTo(tipX, tipY);
        cone.lineTo(bx - half, curTop);
        cone.lineTo(bx + half, curTop);
        cone.close();
        p.setColor(Color.BLACK);
        p.setShader(new LinearGradient(0, tipY, 0, curTop,
                new int[]{a(color, dark ? 205 : 150), a(color, dark ? 110 : 80), a(color, dark ? 40 : 30)},
                new float[]{0f, 0.45f, 1f}, Shader.TileMode.CLAMP));
        c.drawPath(cone, p);
        Path core = P2;
        core.reset();
        core.moveTo(tipX, tipY);
        core.lineTo(bx - half * 0.35f, curTop);
        core.lineTo(bx + half * 0.35f, curTop);
        core.close();
        p.setShader(new LinearGradient(0, tipY, 0, curTop, a(0xFFFFFF, dark ? 150 : 110), a(0xFFFFFF, 0),
                Shader.TileMode.CLAMP));
        c.drawPath(core, p);
        p.setShader(null);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(1.2f * d);
        p.setColor(a(color, dark ? 220 : 170));
        c.drawLine(tipX, tipY, bx - half, curTop, p);
        c.drawLine(tipX, tipY, bx + half, curTop, p);
        p.setStyle(Paint.Style.FILL);
        // dust floating up in the light
        Random r = new Random(seed);
        int dust = ProPainter.mix(color, 0xFFFFFFFF, 0.6f);
        for (int i = 0; i < 16; i++) {
            float ph = ((t * 0.0011f) + r.nextFloat()) % 1f;
            float side = r.nextFloat() * 2 - 1, size = (0.7f + r.nextFloat() * 0.9f) * d;
            if (ph > grow) continue;
            float y = tipY - len * ph, x = tipX + (bx - tipX) * ph + side * fullHalf * ph * 0.85f;
            p.setColor(a(dust, (int) (220 * Math.sin(ph * Math.PI))));
            c.drawCircle(x, y, size, p);
        }
        // the bright spot under the finger and a thin flare across it
        float fr = 13 * d * (0.85f + 0.15f * (float) Math.sin(t * 0.02));
        p.setColor(Color.BLACK);
        p.setShader(new RadialGradient(tipX, tipY, fr, new int[]{a(0xFFFFFF, 230), a(color, 170), a(color, 0)},
                new float[]{0f, 0.35f, 1f}, Shader.TileMode.CLAMP));
        c.drawCircle(tipX, tipY, fr, p);
        p.setShader(new LinearGradient(tipX - 24 * d, 0, tipX + 24 * d, 0,
                new int[]{a(color, 0), a(0xFFFFFF, 190), a(color, 0)}, null, Shader.TileMode.CLAMP));
        c.drawRect(tipX - 24 * d, tipY - 0.6f * d, tipX + 24 * d, tipY + 0.6f * d, p);
        p.setShader(null);
    }

    // ------------------------------------------------------------------ lightning

    /**
     * Lightning from the finger to a point above it, flickering for a moment and then fading
     * (t = milliseconds since the strike); a flash where it hits the key.
     */
    private static void strike(Canvas c, Paint p, float tipX, float tipY, float tx, float ty, float spread, long t,
                               int seed, int color, float d) {
        float len = tipY - ty;
        int step = (int) (t / 45);
        float strike = t < 260 ? (step % 3 == 1 ? 0.55f : 1f) : Math.max(0, 1 - (t - 260) / 180f);
        if (strike > 0 && len > 6 * d) {
            Random r = new Random(seed * 7919L + step);
            float ex = tx + (r.nextFloat() - 0.5f) * spread * 0.35f;
            float[] pts = bolt(tipX, tipY, ex, ty, len * 0.16f + 3 * d, 5, r);
            int branches = 2 + r.nextInt(2), n = pts.length / 2;
            for (int k = 0; k < branches; k++) {
                int idx = (int) ((0.2f + r.nextFloat() * 0.5f) * (n - 1));
                float sx = pts[idx * 2], sy = pts[idx * 2 + 1];
                float side = (r.nextBoolean() ? 1 : -1) * (0.25f + r.nextFloat() * 0.45f) * len;
                float bx = sx + side, by = sy - (0.12f + r.nextFloat() * 0.3f) * len;
                float[] bp = bolt(sx, sy, bx, by, (float) Math.hypot(bx - sx, by - sy) * 0.2f, 4, r);
                glow(c, p, line(bp, P2), color, 1.1f * d, strike * 0.75f);
            }
            glow(c, p, line(pts, P1), color, 2.1f * d, strike);
        }
        if (t < 200) {
            float fa = 1 - t / 200f, fr = 18 * d;
            p.setColor(Color.BLACK);
            p.setShader(new RadialGradient(tipX, tipY, fr, new int[]{a(0xFFFFFF, (int) (230 * fa)),
                    a(color, (int) (150 * fa)), a(color, 0)}, new float[]{0f, 0.35f, 1f}, Shader.TileMode.CLAMP));
            c.drawCircle(tipX, tipY, fr, p);
            p.setShader(null);
        }
    }

    /** sparks flying off the key for a moment after it is touched */
    private static void sparks(Canvas c, Paint p, float tipX, float tipY, long t, int seed, int color, float d) {
        if (t >= 340) return;
        Random sr = new Random(seed);
        float al = 1 - t / 340f;
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeCap(Paint.Cap.ROUND);
        p.setStrokeWidth(1.3f * d);
        int spark = ProPainter.mix(color, 0xFFFFFFFF, 0.5f);
        for (int i = 0; i < 11; i++) {
            double ang = -Math.PI / 2 + (sr.nextFloat() - 0.5f) * Math.PI * 1.7;
            float sp = (0.07f + sr.nextFloat() * 0.13f) * d;
            float dist = sp * t, fall = 0.00012f * t * t * d;
            float x0 = tipX + (float) Math.cos(ang) * dist, y0 = tipY + (float) Math.sin(ang) * dist + fall;
            float x1 = x0 - (float) Math.cos(ang) * 4.5f * d, y1 = y0 - (float) Math.sin(ang) * 4.5f * d;
            p.setColor(a(spark, (int) (255 * al)));
            c.drawLine(x1, y1, x0, y0, p);
        }
        p.setStyle(Paint.Style.FILL);
    }

    /** electricity running along the edge of a frame */
    private static void crackle(Canvas c, Paint p, RectF b, long t, int seed, int color, float d) {
        Random r = new Random(seed * 31L + t / 70);
        float per = 2 * (b.width() + b.height());
        int arcs = 2 + r.nextInt(2);
        for (int k = 0; k < arcs; k++) {
            float start = r.nextFloat() * per, len = per * (0.12f + r.nextFloat() * 0.16f);
            Path path = P2;
            path.reset();
            int n = 9;
            for (int i = 0; i <= n; i++) {
                perimeter(b, start + len * i / n, PT);
                float off = (i == 0 || i == n) ? 0 : (r.nextFloat() - 0.3f) * 5 * d; // mostly outwards
                float px = PT[0] + PT[2] * off, py = PT[1] + PT[3] * off;
                if (i == 0) path.moveTo(px, py); else path.lineTo(px, py);
            }
            glow(c, p, path, color, 1.0f * d, 0.85f);
        }
    }

    /** a point on the rectangle's edge (u along the edge) and the outward direction */
    private static void perimeter(RectF b, float u, float[] out) {
        float w = b.width(), h = b.height(), per = 2 * (w + h);
        u = ((u % per) + per) % per;
        if (u < w) { out[0] = b.left + u; out[1] = b.top; out[2] = 0; out[3] = -1; return; }
        u -= w;
        if (u < h) { out[0] = b.right; out[1] = b.top + u; out[2] = 1; out[3] = 0; return; }
        u -= h;
        if (u < w) { out[0] = b.right - u; out[1] = b.bottom; out[2] = 0; out[3] = 1; return; }
        u -= w;
        out[0] = b.left; out[1] = b.bottom - u; out[2] = -1; out[3] = 0;
    }

    // ------------------------------------------------------------------ radiance

    /** rays of light turning slowly around a point, a white-hot centre, twinkling glints */
    private static void rays(Canvas c, Paint p, float cx, float cy, float big, long t, int color, float d, boolean dark) {
        if (big <= 1) return;
        Path rays = P1;
        rays.reset();
        int n = 18;
        double rot = Math.toRadians(t * 0.05);
        for (int i = 0; i < n; i++) {
            double ang = rot + i * 2 * Math.PI / n;
            float len = big * (i % 2 == 0 ? 1f : 0.6f);
            double aw = i % 2 == 0 ? 0.07 : 0.045;
            rays.moveTo(cx, cy);
            rays.lineTo(cx + (float) Math.cos(ang - aw) * len, cy + (float) Math.sin(ang - aw) * len);
            rays.lineTo(cx + (float) Math.cos(ang + aw) * len, cy + (float) Math.sin(ang + aw) * len);
            rays.close();
        }
        p.setColor(Color.BLACK);
        p.setShader(new RadialGradient(cx, cy, big,
                new int[]{a(color, dark ? 230 : 170), a(color, dark ? 120 : 90), a(color, 0)},
                new float[]{0.15f, 0.55f, 1f}, Shader.TileMode.CLAMP));
        c.drawPath(rays, p);
        p.setShader(new RadialGradient(cx, cy, big * 0.6f, a(0xFFFFFF, dark ? 150 : 110), a(color, 0),
                Shader.TileMode.CLAMP));
        c.drawCircle(cx, cy, big * 0.6f, p);
        p.setShader(null);
        for (int i = 0; i < 4; i++) {
            double ang = rot * 1.6 + i * Math.PI / 2 + 0.4;
            float gx = cx + (float) Math.cos(ang) * big * 0.92f, gy = cy + (float) Math.sin(ang) * big * 0.92f;
            float tw = 0.5f + 0.5f * (float) Math.sin(t * 0.018 + i * 1.7);
            glint(c, p, gx, gy, (3 + 3 * tw) * d, a(0xFFFFFF, (int) (230 * tw)));
        }
    }

    /** shock rings spreading from the key */
    private static void rings(Canvas c, Paint p, float tipX, float tipY, long t, int color, float d) {
        for (int k = 0; k < 2; k++) {
            long tk = t - k * 120;
            if (tk <= 0 || tk >= 460) continue;
            float f = tk / 460f, rad = (5 + 34 * easeOut(f)) * d;
            R1.set(tipX - rad, tipY - rad * 0.5f, tipX + rad, tipY + rad * 0.5f);
            p.setStyle(Paint.Style.STROKE);
            p.setStrokeWidth((2.6f * (1 - f) + 0.6f) * d);
            p.setColor(a(color, (int) (230 * (1 - f))));
            c.drawOval(R1, p);
            p.setStyle(Paint.Style.FILL);
        }
    }

    private static void glint(Canvas c, Paint p, float x, float y, float s, int color) {
        Path g = P2;
        g.reset();
        g.moveTo(x, y - s);
        g.quadTo(x, y, x + s, y);
        g.quadTo(x, y, x, y + s);
        g.quadTo(x, y, x - s, y);
        g.quadTo(x, y, x, y - s);
        p.setColor(color);
        c.drawPath(g, p);
    }

    // ------------------------------------------------------------------ plasma

    /** a thin flickering arc from the finger to a point above it */
    private static void tether(Canvas c, Paint p, float tipX, float tipY, float tx, float ty, long t, int seed,
                               int color, float d) {
        float gap = tipY - ty;
        if (gap <= 6 * d) return;
        Random r = new Random(seed * 13L + t / 50);
        float[] pts = bolt(tipX, tipY, tx + (r.nextFloat() - 0.5f) * 6 * d, ty, gap * 0.18f + 2 * d, 4, r);
        glow(c, p, line(pts, P1), color, 1.0f * d, 0.85f);
    }

    /**
     * An orb: active = full of electric arcs with a hot core and a halo (the plasma ball),
     * otherwise a small quiet glass sphere.
     */
    private static void orb(Canvas c, Paint p, boolean dark, float cx, float cy, float ro, boolean active, long t,
                            int seed, int color, float d) {
        if (active) {
            float hr = ro * 1.75f;
            p.setColor(Color.BLACK);
            p.setShader(new RadialGradient(cx, cy, hr, a(color, dark ? 150 : 100), a(color, 0), Shader.TileMode.CLAMP));
            c.drawCircle(cx, cy, hr, p);
        }
        int inner = dark ? ProPainter.mix(color, 0xFF000000, active ? 0.85f : 0.9f) : ProPainter.mix(color, 0xFFFFFFFF, 0.9f);
        int outer = dark ? ProPainter.mix(color, 0xFF000000, active ? 0.45f : 0.7f) : ProPainter.mix(color, 0xFFFFFFFF, active ? 0.55f : 0.75f);
        p.setColor(Color.BLACK);
        p.setShader(new RadialGradient(cx, cy, ro, inner, outer, Shader.TileMode.CLAMP));
        c.drawCircle(cx, cy, ro, p);
        p.setShader(null);
        if (active) {
            c.save();
            PB.reset();
            PB.addCircle(cx, cy, ro, Path.Direction.CW);
            c.clipPath(PB);
            Random r = new Random(seed * 31L + t / 55);
            for (int k = 0; k < 6; k++) {
                double ang = r.nextDouble() * Math.PI * 2;
                float ex = cx + (float) Math.cos(ang) * ro * 0.98f, ey = cy + (float) Math.sin(ang) * ro * 0.98f;
                glow(c, p, line(bolt(cx, cy, ex, ey, ro * 0.3f, 4, r), P2), color, 1.0f * d, 0.9f);
            }
            p.setColor(Color.BLACK);
            p.setShader(new RadialGradient(cx, cy, ro * 0.5f, new int[]{a(0xFFFFFF, 220), a(color, 150), a(color, 0)},
                    new float[]{0f, 0.4f, 1f}, Shader.TileMode.CLAMP));
            c.drawCircle(cx, cy, ro * 0.5f, p);
            p.setShader(null);
            c.restore();
        }
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth((active ? 2 : 1.2f) * d);
        p.setColor(active ? color : a(color, dark ? 150 : 170));
        c.drawCircle(cx, cy, ro, p);
        p.setStrokeWidth(0.8f * d);
        p.setColor(0x88FFFFFF);
        c.drawCircle(cx, cy, ro - 1.5f * d, p);
        p.setStyle(Paint.Style.FILL);
        R1.set(cx - ro * 0.55f, cy - ro * 0.88f, cx + ro * 0.55f, cy - ro * 0.2f);
        p.setColor(Color.BLACK);
        p.setShader(new LinearGradient(0, R1.top, 0, R1.bottom, 0x66FFFFFF, 0x00FFFFFF, Shader.TileMode.CLAMP));
        c.drawOval(R1, p);
        p.setShader(null);
    }

    // ------------------------------------------------------------------ hologram

    /**
     * The projector on the key and its light growing up to a target between left and right
     * at topY, with moving scan lines.
     */
    private static void projector(Canvas c, Paint p, float left, float right, float topY, float tipX, float tipY,
                                  float erx, long t, int color, float d, boolean dark) {
        float unfold = easeOut(t / 160f);
        float ex = tipX, ey = tipY + 3 * d, ery = 3.2f * d;
        float step = 3.2f * d, off = (t * 0.03f * d) % step;
        if (ey - topY > 6 * d) {
            float curTop = ey - (ey - topY) * unfold;
            float cl = ex - erx + (left - (ex - erx)) * unfold;
            float cr = ex + erx + (right - (ex + erx)) * unfold;
            Path cone = P1;
            cone.reset();
            cone.moveTo(ex - erx, ey);
            cone.lineTo(cl, curTop);
            cone.lineTo(cr, curTop);
            cone.lineTo(ex + erx, ey);
            cone.close();
            p.setColor(Color.BLACK);
            p.setShader(new LinearGradient(0, ey, 0, curTop, a(color, dark ? 110 : 70), a(color, dark ? 30 : 20),
                    Shader.TileMode.CLAMP));
            c.drawPath(cone, p);
            p.setShader(null);
            c.save();
            c.clipPath(cone);
            p.setColor(a(color, dark ? 60 : 45));
            float l = Math.min(ex - erx, cl) - 2 * d, rr = Math.max(ex + erx, cr) + 2 * d;
            for (float y = ey - off; y > curTop; y -= step) c.drawRect(l, y, rr, y + 0.9f * d, p);
            c.restore();
            p.setStyle(Paint.Style.STROKE);
            p.setStrokeWidth(d);
            p.setColor(a(color, 150));
            c.drawLine(ex - erx, ey, cl, curTop, p);
            c.drawLine(ex + erx, ey, cr, curTop, p);
            p.setStyle(Paint.Style.FILL);
        }
        R1.set(ex - erx, ey - ery, ex + erx, ey + ery);
        p.setColor(a(color, 70));
        c.drawOval(R1, p);
        PB.reset();
        PB.addOval(R1, Path.Direction.CW);
        glow(c, p, PB, color, 1.2f * d, 1f);
    }

    private static void hologram(Canvas c, Paint p, KeyboardTheme theme, RectF b, RectF key, float tipX, float tipY,
                                 long t, int color, float d, String label, boolean alt) {
        boolean dark = theme.isDark();
        projector(c, p, b.left + 4 * d, b.right - 4 * d, b.bottom, tipX, tipY, Math.min(key.width() * 0.42f, 18 * d),
                t, color, d, dark);
        // the panel opens once the light reaches it
        float open = easeOut((t - 60) / 150f);
        if (open <= 0) return;
        c.save();
        c.scale(1, Math.max(0.04f, open), b.centerX(), b.bottom);
        Path s = ProPainter.shapePath(theme.pro, b, Math.min(b.width(), b.height()) * 0.2f, d, PB);
        holoPanel(c, p, s, b, t, color, d, dark);
        // the letter with colour fringes, flickering, with a small glitch now and then
        boolean glitch = (t / 260) % 3 == 2 && t % 260 < 70;
        float gx = glitch ? 2.5f * d : 0, fl = 0.85f + 0.15f * (float) Math.sin(t * 0.07);
        letter(c, p, b, label, a(0x00FFFF, (int) (160 * fl)), 0, d, false, gx - 1.6f * d);
        letter(c, p, b, label, a(0xFF00FF, (int) (160 * fl)), 0, d, false, gx + 1.6f * d);
        int fill = dark ? ProPainter.mix(color, 0xFFFFFFFF, alt ? 0.85f : 0.7f) : ProPainter.mix(color, 0xFF000000, 0.35f);
        letter(c, p, b, label, a(fill, (int) (255 * fl)), color, d, true, gx);
        c.restore();
    }

    /** HUD corner brackets around a rectangle */
    private static void brackets(Canvas c, Paint p, RectF b, int color, float d) {
        float l = Math.min(b.width(), b.height()) * 0.3f;
        Path path = P2;
        path.reset();
        path.moveTo(b.left, b.top + l); path.lineTo(b.left, b.top); path.lineTo(b.left + l, b.top);
        path.moveTo(b.right - l, b.top); path.lineTo(b.right, b.top); path.lineTo(b.right, b.top + l);
        path.moveTo(b.right, b.bottom - l); path.lineTo(b.right, b.bottom); path.lineTo(b.right - l, b.bottom);
        path.moveTo(b.left + l, b.bottom); path.lineTo(b.left, b.bottom); path.lineTo(b.left, b.bottom - l);
        glow(c, p, path, color, 1.6f * d, 1f);
    }

    // ------------------------------------------------------------------ fire

    /** one living flame standing on (x, base): it sways and flickers with time */
    private static void flameTongue(Canvas c, Paint p, float x, float base, float w, float h, long t, float phase,
                                    int color, boolean dark) {
        if (h < 1) return;
        float hh = h * (0.8f + 0.14f * (float) Math.sin(t * 0.013 + phase) + 0.06f * (float) Math.sin(t * 0.037 + phase * 1.9));
        float lean = w * 0.38f * (float) Math.sin(t * 0.008 + phase * 0.7);
        Path f = P2;
        f.reset();
        f.moveTo(x - w / 2, base);
        f.cubicTo(x - w * 0.6f, base - hh * 0.42f, x + lean - w * 0.18f, base - hh * 0.62f, x + lean, base - hh);
        f.cubicTo(x + lean + w * 0.08f, base - hh * 0.58f, x + w * 0.62f, base - hh * 0.38f, x + w / 2, base);
        f.close();
        int mid = ProPainter.mix(color, 0xFFFFFFFF, 0.45f), core = ProPainter.mix(color, 0xFFFFFFFF, 0.85f);
        p.setColor(Color.BLACK);
        p.setShader(new LinearGradient(0, base, 0, base - hh, new int[]{a(mid, 235), a(color, 205), a(color, 0)},
                new float[]{0f, 0.45f, 1f}, Shader.TileMode.CLAMP));
        c.drawPath(f, p);
        // the hot core
        float cw = w * 0.45f, ch = hh * 0.5f, cl = lean * 0.5f;
        f.reset();
        f.moveTo(x - cw / 2, base);
        f.cubicTo(x - cw * 0.5f, base - ch * 0.5f, x + cl - cw * 0.1f, base - ch * 0.7f, x + cl, base - ch);
        f.cubicTo(x + cl + cw * 0.1f, base - ch * 0.6f, x + cw * 0.5f, base - ch * 0.4f, x + cw / 2, base);
        f.close();
        p.setShader(new LinearGradient(0, base, 0, base - ch, a(core, dark ? 240 : 190), a(core, 0),
                Shader.TileMode.CLAMP));
        c.drawPath(f, p);
        p.setShader(null);
    }

    /**
     * Low flames burning along the bottom of a frame (and up its sides), drawn behind it: the
     * frame stands in a small fire of the theme's color.
     */
    private static void fireBed(Canvas c, Paint p, RectF b, int n, float h, boolean sides, long t, int seed,
                                int color, float d, boolean dark) {
        float grow = easeOut(t / 200f);
        float gr = Math.max(b.width(), b.height() * 2) * 0.6f;
        p.setColor(Color.BLACK);
        p.setShader(new RadialGradient(b.centerX(), b.bottom, gr, a(color, dark ? 120 : 80), a(color, 0),
                Shader.TileMode.CLAMP));
        c.drawCircle(b.centerX(), b.bottom, gr, p);
        p.setShader(null);
        Random r = new Random(seed);
        float w = b.width() / n * 1.6f;
        for (int i = 0; i < n; i++) {
            float fx = b.left + b.width() * (i + 0.5f) / n + (r.nextFloat() - 0.5f) * 3 * d;
            float fh = h * (0.75f + r.nextFloat() * 0.5f) * grow;
            flameTongue(c, p, fx, b.bottom + fh * 0.45f, w, fh, t, i * 1.7f + r.nextFloat() * 3, color, dark);
        }
        if (!sides) return;
        for (int side = -1; side <= 1; side += 2) {
            for (int k = 0; k < 2; k++) {
                float fx = side < 0 ? b.left + 3 * d : b.right - 3 * d;
                float fh = h * (0.9f - 0.3f * k) * grow;
                float base = b.bottom - b.height() * 0.32f * k + fh * 0.3f;
                flameTongue(c, p, fx + side * 2 * d, base, 11 * d, fh, t, side * 2.3f + k * 1.3f, color, dark);
            }
        }
    }

    /** a little flame where the finger touches, embers rising to the frame, low flames in front of it */
    private static void fireRise(Canvas c, Paint p, RectF b, float tipX, float tipY, long t, int seed, int color,
                                 float d, boolean dark, boolean flamesOnFrame) {
        float grow = easeOut(t / 150f);
        flameTongue(c, p, tipX, tipY + 5 * d, 10 * d, 17 * d * grow, t, 0.5f, color, dark);
        if (flamesOnFrame) {
            for (int i = 0; i < 3; i++) {
                float fx = b.left + b.width() * (0.2f + 0.3f * i);
                flameTongue(c, p, fx, b.bottom + 2 * d, b.width() * 0.22f, b.height() * 0.24f * grow, t,
                        4.1f + i * 2.2f, color, dark);
            }
        }
        Random r = new Random(seed * 3L);
        int ember = ProPainter.mix(color, 0xFFFFFFFF, 0.55f);
        float top = b.top - 24 * d;
        for (int i = 0; i < 18; i++) {
            float ph = (t * 0.0009f + r.nextFloat()) % 1f;
            float sx = r.nextFloat(), amp = (3 + r.nextFloat() * 9) * d, size = (0.8f + r.nextFloat()) * d;
            boolean fromFinger = i % 2 == 0;
            float x0 = fromFinger ? tipX : b.left + b.width() * sx, y0 = fromFinger ? tipY : b.bottom + 4 * d;
            float x = x0 + (b.centerX() - x0) * ph * (fromFinger ? 1 : 0.2f) + (float) Math.sin(ph * 9 + i) * amp;
            float y = y0 - (y0 - top) * ph;
            float al = (float) Math.sin(ph * Math.PI) * (0.6f + 0.4f * (float) Math.sin(t * 0.05 + i));
            p.setColor(a(ember, (int) (230 * al * grow)));
            c.drawCircle(x, y, size, p);
        }
    }

    // ------------------------------------------------------------------ smoke

    /** smoke of the theme's color rising from the finger and curling around a frame */
    private static void smoke(Canvas c, Paint p, RectF b, float tipX, float tipY, long t, int seed, int color, float d,
                              boolean dark, boolean front) {
        int col = dark ? ProPainter.mix(color, 0xFFFFFFFF, 0.3f) : ProPainter.mix(color, 0xFF000000, 0.05f);
        float appear = easeOut(t / 300f), topY = b.top - b.height() * 0.6f;
        Random r = new Random(seed * 5L + (front ? 1 : 0));
        int n = front ? 7 : 26;
        for (int i = 0; i < n; i++) {
            float ph = (t * 0.00055f + r.nextFloat()) % 1f;
            float sway = r.nextFloat() - 0.5f, extra = r.nextFloat() * 6;
            float x = tipX + (b.centerX() - tipX) * Math.min(1, ph * 1.6f)
                    + (float) Math.sin(ph * 5.5f + i * 1.3f) * b.width() * 0.4f * ph + sway * b.width() * 0.6f * ph;
            float y = tipY - (tipY - topY) * ph;
            float rad = (6 + 28 * ph + extra) * d;
            float al = (float) Math.sin(Math.min(1, ph * 1.15f) * Math.PI) * appear
                    * (front ? 0.26f : (dark ? 0.52f : 0.4f));
            p.setColor(Color.BLACK);
            p.setShader(new RadialGradient(x, y, rad, a(col, (int) (255 * al)), a(col, 0), Shader.TileMode.CLAMP));
            c.drawCircle(x, y, rad, p);
        }
        p.setShader(null);
        if (front) return;
        // thin wisps curling up around the frame
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeCap(Paint.Cap.ROUND);
        p.setStrokeWidth(2 * d);
        for (int k = -1; k <= 1; k++) {
            float ph = (t * 0.0004f + (k + 1) / 3f) % 1f;
            float x0 = tipX + k * 6 * d, y0 = tipY - ph * 24 * d;
            Path w = P1;
            w.reset();
            w.moveTo(x0, y0);
            w.cubicTo(x0 + 22 * d * k + 10 * d * (float) Math.sin(t * 0.004 + k), y0 - 30 * d,
                    b.centerX() - 24 * d * k, b.bottom + 12 * d,
                    b.centerX() + k * b.width() * 0.45f, b.top - 8 * d);
            p.setColor(Color.BLACK);
            p.setShader(new LinearGradient(0, y0, 0, b.top, a(col, (int) (170 * appear)), a(col, 0),
                    Shader.TileMode.CLAMP));
            c.drawPath(w, p);
        }
        p.setShader(null);
        p.setStyle(Paint.Style.FILL);
    }

    // ------------------------------------------------------------------ frost

    /** the icy version of a color */
    private static int ice(int color) {
        return ProPainter.mix(color, 0xFFFFFFFF, 0.45f);
    }

    /** cold mist at the foot of a frame */
    private static void frostMist(Canvas c, Paint p, RectF b, long t, int ice, float d, boolean dark) {
        float gr = Math.max(b.width(), b.height()) * 0.8f * easeOut(t / 300f);
        if (gr < 1) return;
        p.setColor(Color.BLACK);
        p.setShader(new RadialGradient(b.centerX(), b.bottom, gr, a(ice, dark ? 110 : 90), a(ice, 0),
                Shader.TileMode.CLAMP));
        c.drawCircle(b.centerX(), b.bottom, gr, p);
        p.setShader(null);
    }

    /** ice crystals growing from the corners of a frame, a frost star under the finger, snowflakes */
    private static void frost(Canvas c, Paint p, RectF b, float tipX, float tipY, long t, int seed, int ice, float d) {
        float grow = easeOut(t / 320f);
        float[][] corners = {{b.left, b.top, -1, -1}, {b.right, b.top, 1, -1}, {b.right, b.bottom, 1, 1},
                {b.left, b.bottom, -1, 1}};
        for (float[] k : corners) {
            crystal(c, p, k[0], k[1], (float) Math.atan2(k[3], k[2]), 15 * d * grow, ice, d); // outwards
            crystal(c, p, k[0], k[1], (float) Math.atan2(0, -k[2]), 11 * d * grow, ice, d);   // along the edge
            crystal(c, p, k[0], k[1], (float) Math.atan2(-k[3], 0), 11 * d * grow, ice, d);   // along the side
        }
        for (int i = 0; i < 6; i++) {
            crystal(c, p, tipX, tipY, (float) (i * Math.PI / 3 + 0.3), 12 * d * grow, ice, d);
        }
        Random r = new Random(seed * 19L);
        for (int i = 0; i < 9; i++) {
            float ph = (t * 0.00035f + r.nextFloat()) % 1f;
            float x = b.left - 12 * d + r.nextFloat() * (b.width() + 24 * d) + (float) Math.sin(ph * 6 + i) * 6 * d;
            float y = b.top - 22 * d + (b.height() + 44 * d) * ph;
            flake(c, p, x, y, (2.5f + r.nextFloat() * 2.5f) * d, ph * 3 + i, a(ice, (int) (230 * Math.sin(ph * Math.PI))), d);
        }
    }

    /** one ice crystal: a needle with two pairs of side needles */
    private static void crystal(Canvas c, Paint p, float x, float y, float ang, float len, int ice, float d) {
        if (len < d) return;
        Path path = P1;
        path.reset();
        float ex = x + (float) Math.cos(ang) * len, ey = y + (float) Math.sin(ang) * len;
        path.moveTo(x, y);
        path.lineTo(ex, ey);
        for (int k = 1; k <= 2; k++) {
            float bx = x + (ex - x) * k / 3f, by = y + (ey - y) * k / 3f, bl = len * (0.42f - 0.12f * k);
            for (int sd = -1; sd <= 1; sd += 2) {
                double a2 = ang + sd * Math.PI / 3;
                path.moveTo(bx, by);
                path.lineTo(bx + (float) Math.cos(a2) * bl, by + (float) Math.sin(a2) * bl);
            }
        }
        glow(c, p, path, ice, 1.0f * d, 0.9f);
    }

    /** a six-armed snowflake */
    private static void flake(Canvas c, Paint p, float x, float y, float s, float rot, int color, float d) {
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeCap(Paint.Cap.ROUND);
        p.setStrokeWidth(0.9f * d);
        p.setColor(color);
        for (int i = 0; i < 6; i++) {
            double ang = rot + i * Math.PI / 3;
            c.drawLine(x, y, x + (float) Math.cos(ang) * s, y + (float) Math.sin(ang) * s, p);
        }
        p.setStyle(Paint.Style.FILL);
    }

    // ------------------------------------------------------------------ stardust

    /** glittering stars spiralling from the finger up around a frame, a burst when the key is touched */
    private static void stardust(Canvas c, Paint p, RectF b, float tipX, float tipY, long t, int seed, int color, float d,
                                 boolean dark) {
        int gold = dark ? ProPainter.mix(color, 0xFFFFFFFF, 0.5f) : color;
        int white = dark ? 0xFFFFFF : ProPainter.mix(color, 0xFF000000, 0.25f);
        Random r = new Random(seed * 11L);
        float appear = easeOut(t / 200f);
        for (int i = 0; i < 46; i++) {
            float ph = (t * 0.0008f + r.nextFloat()) % 1f;
            float spin = r.nextFloat() * 6.28f, size = 2.2f + r.nextFloat() * 3.2f;
            float e = easeOut(ph);
            float cx = tipX + (b.centerX() - tipX) * e, cy = tipY + (b.centerY() - tipY) * e;
            float ang = spin + ph * 4.2f, sr = b.width() * 0.62f * ph;
            float x = cx + (float) Math.cos(ang) * sr, y = cy + (float) Math.sin(ang) * sr * 0.6f;
            float tw = 0.5f + 0.5f * (float) Math.sin(t * 0.03 + i * 1.9);
            float sz = size * d * (0.6f + 0.4f * tw);
            int al = (int) (255 * Math.min(1, 1.4f * Math.sin(ph * Math.PI)) * (0.55f + 0.45f * tw) * appear);
            if (sz > 3 * d) {
                p.setColor(Color.BLACK);
                p.setShader(new RadialGradient(x, y, sz * 2.4f, a(color, al / 2), a(color, 0), Shader.TileMode.CLAMP));
                c.drawCircle(x, y, sz * 2.4f, p);
                p.setShader(null);
            }
            glint(c, p, x, y, sz, a(i % 3 == 0 ? white : gold, al));
        }
        if (t < 320) {
            Random br = new Random(seed * 23L);
            float f = t / 320f;
            for (int i = 0; i < 12; i++) {
                double ang = br.nextFloat() * Math.PI * 2;
                float dist = (8 + 26 * br.nextFloat()) * d * easeOut(f);
                glint(c, p, tipX + (float) Math.cos(ang) * dist, tipY + (float) Math.sin(ang) * dist * 0.7f,
                        (2 + 2 * br.nextFloat()) * d, a(gold, (int) (240 * (1 - f))));
            }
        }
    }

    /** four stars twinkling on the corners of a frame, in turn */
    private static void twinkles(Canvas c, Paint p, RectF b, long t, int color, float d, boolean dark) {
        int gold = dark ? ProPainter.mix(color, 0xFFFFFFFF, 0.5f) : color;
        int white = dark ? 0xFFFFFF : ProPainter.mix(color, 0xFF000000, 0.25f);
        float[] xs = {b.left + 2 * d, b.right - 2 * d, b.right - 2 * d, b.left + 2 * d};
        float[] ys = {b.top + 2 * d, b.top + 2 * d, b.bottom - 2 * d, b.bottom - 2 * d};
        for (int i = 0; i < 4; i++) {
            float tw = Math.max(0, (float) Math.sin(t * 0.012 + i * 1.6));
            if (tw <= 0.05f) continue;
            float sz = (2.5f + 3.5f * tw) * d;
            p.setColor(Color.BLACK);
            p.setShader(new RadialGradient(xs[i], ys[i], sz * 2.2f, a(color, (int) (120 * tw)), a(color, 0),
                    Shader.TileMode.CLAMP));
            c.drawCircle(xs[i], ys[i], sz * 2.2f, p);
            p.setShader(null);
            glint(c, p, xs[i], ys[i], sz, a(i % 2 == 0 ? white : gold, (int) (255 * tw)));
        }
    }

    // ------------------------------------------------------------------ bubbles

    /** bubbles rising from the finger and popping at the frame */
    private static void bubbles(Canvas c, Paint p, RectF b, float tipX, float tipY, long t, int seed, int color, float d,
                                boolean dark) {
        float endY = b.bottom - 3 * d;
        if (tipY - endY < 6 * d) return;
        int rim = dark ? ProPainter.mix(color, 0xFFFFFFFF, 0.35f) : color;
        Random r = new Random(seed * 17L);
        float appear = easeOut(t / 160f);
        for (int i = 0; i < 14; i++) {
            float life = 1300 + r.nextFloat() * 700, offset = r.nextFloat() * life;
            float startX = tipX + (r.nextFloat() - 0.5f) * 12 * d, endX = b.left + b.width() * (0.15f + 0.7f * r.nextFloat());
            float baseR = (3 + r.nextFloat() * 5) * d;
            float ph = ((t + offset) % life) / life, go = Math.min(1, ph / 0.9f);
            float y = tipY - (tipY - endY) * go;
            float x = startX + (endX - startX) * go + (float) Math.sin(ph * 8 + i) * 4 * d;
            float rad = baseR * (0.6f + 0.4f * ph);
            if (ph > 0.9f) { // pop
                float f = (ph - 0.9f) / 0.1f;
                p.setStyle(Paint.Style.STROKE);
                p.setStrokeWidth(d);
                p.setColor(a(rim, (int) (200 * (1 - f) * appear)));
                c.drawCircle(x, y, rad * (1 + f * 0.9f), p);
                p.setStyle(Paint.Style.FILL);
                continue;
            }
            p.setColor(Color.BLACK);
            p.setShader(new RadialGradient(x, y, rad, a(rim, 10), a(rim, (int) ((dark ? 90 : 70) * appear)),
                    Shader.TileMode.CLAMP));
            c.drawCircle(x, y, rad, p);
            p.setShader(null);
            p.setStyle(Paint.Style.STROKE);
            p.setStrokeWidth(0.9f * d);
            p.setColor(a(rim, (int) (210 * appear)));
            c.drawCircle(x, y, rad, p);
            p.setStyle(Paint.Style.FILL);
            p.setColor(a(0xFFFFFF, (int) (204 * appear)));
            c.drawCircle(x - rad * 0.35f, y - rad * 0.35f, Math.max(0.8f * d, rad * 0.22f), p);
        }
    }

    /** a glossy reflection across the top of a frame (bubble style), above the letter */
    private static void gloss(Canvas c, Paint p, Path s, RectF b, boolean dark) {
        c.save();
        c.clipPath(s);
        R2.set(b.left + b.width() * 0.18f, b.top + b.height() * 0.05f, b.right - b.width() * 0.18f,
                b.top + b.height() * 0.22f);
        p.setColor(Color.BLACK);
        p.setShader(new LinearGradient(0, R2.top, 0, R2.bottom, dark ? 0x33FFFFFF : 0x99FFFFFF, 0x00FFFFFF,
                Shader.TileMode.CLAMP));
        c.drawOval(R2, p);
        p.setShader(null);
        c.restore();
    }

    // ------------------------------------------------------------------ helpers

    /** a jagged lightning path from (x1, y1) to (x2, y2): midpoint displacement */
    private static float[] bolt(float x1, float y1, float x2, float y2, float disp, int levels, Random r) {
        int n = 1 << levels;
        float[] pts = new float[(n + 1) * 2];
        pts[0] = x1;
        pts[1] = y1;
        pts[n * 2] = x2;
        pts[n * 2 + 1] = y2;
        float dx = x2 - x1, dy = y2 - y1, len = (float) Math.max(1, Math.hypot(dx, dy));
        float nx = -dy / len, ny = dx / len;
        for (int span = n; span > 1; span /= 2) {
            for (int i = 0; i < n; i += span) {
                int m = i + span / 2;
                float mx = (pts[i * 2] + pts[(i + span) * 2]) / 2, my = (pts[i * 2 + 1] + pts[(i + span) * 2 + 1]) / 2;
                float o = (r.nextFloat() - 0.5f) * 2 * disp;
                pts[m * 2] = mx + nx * o;
                pts[m * 2 + 1] = my + ny * o;
            }
            disp *= 0.55f;
        }
        return pts;
    }

    private static Path line(float[] pts, Path out) {
        out.reset();
        out.moveTo(pts[0], pts[1]);
        for (int i = 2; i < pts.length; i += 2) out.lineTo(pts[i], pts[i + 1]);
        return out;
    }

    /** a glowing stroke: two soft wide strokes, the color, a white core */
    private static void glow(Canvas c, Paint p, Path path, int color, float w, float alpha) {
        p.setShader(null);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeCap(Paint.Cap.ROUND);
        p.setStrokeJoin(Paint.Join.ROUND);
        p.setColor(a(color, (int) (40 * alpha)));
        p.setStrokeWidth(w * 4.5f);
        c.drawPath(path, p);
        p.setColor(a(color, (int) (95 * alpha)));
        p.setStrokeWidth(w * 2.2f);
        c.drawPath(path, p);
        p.setColor(a(color, (int) (255 * alpha)));
        p.setStrokeWidth(w);
        c.drawPath(path, p);
        p.setColor(a(0xFFFFFF, (int) (230 * alpha)));
        p.setStrokeWidth(w * 0.4f);
        c.drawPath(path, p);
        p.setStyle(Paint.Style.FILL);
    }

    /** a color with the given alpha (0..255) */
    static int a(int color, int alpha) {
        return (color & 0x00FFFFFF) | (Math.max(0, Math.min(255, alpha)) << 24);
    }

    private static float clamp01(float v) {
        return v < 0 ? 0 : v > 1 ? 1 : v;
    }

    private static float easeOut(float x) {
        x = clamp01(x);
        float u = 1 - x;
        return 1 - u * u * u;
    }

    private static float easeOutBack(float x) {
        x = clamp01(x);
        float c1 = 1.70158f, c3 = c1 + 1, u = x - 1;
        return 1 + c3 * u * u * u + c1 * u * u;
    }
}
