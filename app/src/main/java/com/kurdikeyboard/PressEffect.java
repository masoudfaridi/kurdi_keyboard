package com.kurdikeyboard;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;

import java.util.Random;

/**
 * Key press popups: what appears above a key while it is pressed (setting "popup theme").
 *
 * SIMPLE is the plain bubble of 2.x. The others are animated light effects, all drawn in code:
 * BEAM       a triangular beam of light from the key up to a glowing frame with the letter
 * LIGHTNING  lightning strikes from the key into the frame, sparks fly, electricity crackles
 * RADIANCE   rays of light turn behind the frame, shock rings spread from the key
 * PLASMA     the letter in an energy orb full of electric arcs, tied to the key by a thin arc
 * HOLOGRAM   a projector on the key casts the letter as a flickering hologram with scan lines
 * AUTO picks BEAM for dark pro themes and SIMPLE for the others.
 *
 * Used by the keyboard (KKeyboardView through PressEffectView) and by the previews in
 * PopupStyleActivity. Colors come from the keyboard theme.
 */
final class PressEffect {

    static final String AUTO = "auto", SIMPLE = "simple", BEAM = "beam", LIGHTNING = "lightning",
            RADIANCE = "radiance", PLASMA = "plasma", HOLOGRAM = "hologram";
    /** in the order of the settings screen */
    static final String[] ALL = {AUTO, SIMPLE, BEAM, LIGHTNING, RADIANCE, PLASMA, HOLOGRAM};

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
            case BEAM: case HOLOGRAM: case LIGHTNING: return 38;
            case PLASMA: return 30;
            case RADIANCE: return 26;
            default: return 0;
        }
    }

    /** room around the frame the effect draws into (px) */
    static float margin(String style, float bw, float bh, float d) {
        switch (style) {
            case RADIANCE: return Math.max(bw, bh) * 0.85f;
            case PLASMA: return Math.max(bw, bh) * 0.58f;
            case LIGHTNING: return Math.max(bw, bh) * 0.5f + 8 * d;
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

    // ------------------------------------------------------------------ drawing

    private static final Path P1 = new Path(), P2 = new Path(), PB = new Path();
    private static final RectF R1 = new RectF();
    private static final float[] PT = new float[4];
    private static final Typeface BOLD = Typeface.create(Typeface.DEFAULT, Typeface.BOLD);

    /**
     * Draws the popup. Coordinates are those of the canvas.
     * @param bubble where the letter is shown
     * @param key    the pressed key
     * @param tipX   where the light leaves the key (under the finger)
     * @param t      milliseconds since the key went down
     * @param seed   different for every press (shapes of lightning and arcs)
     */
    static void draw(Canvas c, Paint p, String style, KeyboardTheme theme, RectF bubble, RectF key,
                     float tipX, float tipY, long t, int seed, String label, boolean alt, float d, int color) {
        p.setShader(null);
        p.clearShadowLayer();
        p.setStyle(Paint.Style.FILL);
        boolean dark = theme.isDark();
        switch (style) {
            case BEAM:
                beam(c, p, bubble, tipX, tipY, t, seed, color, d, dark);
                frame(c, p, theme, bubble, color, d, label, alt, pop(t), 0);
                break;
            case LIGHTNING:
                lightning(c, p, bubble, tipX, tipY, t, seed, color, d);
                frame(c, p, theme, bubble, color, d, label, alt, pop(t), t < 170 ? 1 - t / 170f : 0);
                crackle(c, p, bubble, pop(t), t, seed, color, d);
                break;
            case RADIANCE:
                radiance(c, p, bubble, tipX, tipY, t, color, d, dark);
                frame(c, p, theme, bubble, color, d, label, alt, pop(t), 0);
                break;
            case PLASMA:
                plasma(c, p, theme, bubble, tipX, tipY, t, seed, color, d, label, alt);
                break;
            case HOLOGRAM:
                hologram(c, p, theme, bubble, key, tipX, tipY, t, color, d, label, alt);
                break;
            default:
                simple(c, p, theme, bubble, label, alt, d);
        }
        p.setShader(null);
        p.clearShadowLayer();
        p.setStyle(Paint.Style.FILL);
    }

    // ------------------------------------------------------------------ simple

    /** the plain bubble: the theme's key color, the letter in the label color */
    static void simple(Canvas c, Paint p, KeyboardTheme theme, RectF b, String text, boolean alt, float d) {
        R1.set(b.left, b.top, b.right, b.bottom - 2 * d);
        p.setStyle(Paint.Style.FILL);
        p.setColor(0x40000000);
        R1.offset(0, 2 * d);
        c.drawRoundRect(R1, 12 * d, 12 * d, p);
        R1.offset(0, -2 * d);
        int bg = theme.isDark() ? theme.keyPressed : 0xFFFFFFFF;
        if (theme.pro != null) bg = ProPainter.over(theme.bg | 0xFF000000, bg); // see-through pro keys
        p.setColor(bg);
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

    // ------------------------------------------------------------------ the glowing frame

    private static float pop(long t) {
        return 0.72f + 0.28f * easeOutBack(t / 130f);
    }

    /** a frame in the theme's key shape: dark glass (light themes: white), glowing edge, the letter */
    private static void frame(Canvas c, Paint p, KeyboardTheme theme, RectF b, int color, float d,
                              String label, boolean alt, float pop, float flash) {
        boolean dark = theme.isDark();
        c.save();
        c.scale(pop, pop, b.centerX(), b.bottom);
        Path s = ProPainter.shapePath(theme.pro, b, Math.min(b.width(), b.height()) * 0.24f, d, PB);
        // halo
        float hr = Math.max(b.width(), b.height()) * (0.75f + 0.25f * flash); // inside the window margin
        p.setColor(Color.BLACK);
        p.setShader(new RadialGradient(b.centerX(), b.centerY(), hr,
                a(color, (int) ((dark ? 110 : 80) + 120 * flash)), a(color, 0), Shader.TileMode.CLAMP));
        c.drawCircle(b.centerX(), b.centerY(), hr, p);
        // body
        int top = dark ? ProPainter.mix(0xFF000000, color, 0.18f) : 0xFFFFFFFF;
        int bottom = dark ? 0xFF030305 : ProPainter.mix(0xFFFFFFFF, color, 0.12f);
        p.setShader(new LinearGradient(0, b.top, 0, b.bottom, top, bottom, Shader.TileMode.CLAMP));
        c.drawPath(s, p);
        p.setShader(null);
        // light inside the edge
        c.save();
        c.clipPath(s);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(8 * d);
        p.setColor(a(color, dark ? 80 : 50));
        c.drawPath(s, p);
        c.restore();
        // the edge: soft glow, the color, a white-hot core
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
        int fill = dark ? ProPainter.mix(color, 0xFFFFFFFF, alt ? 0.8f : 0.55f)
                : ProPainter.mix(color, 0xFF000000, alt ? 0.15f : 0.35f);
        letter(c, p, b, label, fill, color, d, dark || alt, 0);
        c.restore();
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

    // ------------------------------------------------------------------ beam

    private static void beam(Canvas c, Paint p, RectF b, float tipX, float tipY, long t, int seed, int color,
                             float d, boolean dark) {
        float topY = b.bottom - 2 * d, len = tipY - topY;
        if (len < 8 * d) return;
        float grow = easeOut(t / 120f);
        float curTop = tipY - len * grow, fullHalf = b.width() * 0.40f, half = fullHalf * grow, bx = b.centerX();
        // the cone of light, brightest at the key
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
        // a brighter core
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
        // sharp edges
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
        // the bright spot on the key and a thin flare across it
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

    private static void lightning(Canvas c, Paint p, RectF b, float tipX, float tipY, long t, int seed,
                                  int color, float d) {
        float len = tipY - b.bottom;
        int step = (int) (t / 45);
        // strikes flicker for a moment, then fade; the frame keeps crackling (crackle)
        float strike = t < 260 ? (step % 3 == 1 ? 0.55f : 1f) : Math.max(0, 1 - (t - 260) / 180f);
        if (strike > 0 && len > 6 * d) {
            Random r = new Random(seed * 7919L + step);
            float ex = b.centerX() + (r.nextFloat() - 0.5f) * b.width() * 0.35f;
            float[] pts = bolt(tipX, tipY, ex, b.bottom - d, len * 0.16f + 3 * d, 5, r);
            int branches = 2 + r.nextInt(2), n = pts.length / 2;
            for (int k = 0; k < branches; k++) {
                int idx = (int) ((0.2f + r.nextFloat() * 0.5f) * (n - 1));
                float sx = pts[idx * 2], sy = pts[idx * 2 + 1];
                float side = (r.nextBoolean() ? 1 : -1) * (0.25f + r.nextFloat() * 0.45f) * len;
                float tx = sx + side, ty = sy - (0.12f + r.nextFloat() * 0.3f) * len;
                float[] bp = bolt(sx, sy, tx, ty, (float) Math.hypot(tx - sx, ty - sy) * 0.2f, 4, r);
                glow(c, p, line(bp, P2), color, 1.1f * d, strike * 0.75f);
            }
            glow(c, p, line(pts, P1), color, 2.1f * d, strike);
        }
        // sparks flying off the key
        if (t < 340) {
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
        // the flash where it strikes the key
        if (t < 200) {
            float fa = 1 - t / 200f, fr = 18 * d;
            p.setColor(Color.BLACK);
            p.setShader(new RadialGradient(tipX, tipY, fr, new int[]{a(0xFFFFFF, (int) (230 * fa)),
                    a(color, (int) (150 * fa)), a(color, 0)}, new float[]{0f, 0.35f, 1f}, Shader.TileMode.CLAMP));
            c.drawCircle(tipX, tipY, fr, p);
            p.setShader(null);
        }
    }

    /** electricity running along the edge of the frame while the key is held */
    private static void crackle(Canvas c, Paint p, RectF b, float pop, long t, int seed, int color, float d) {
        c.save();
        c.scale(pop, pop, b.centerX(), b.bottom);
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
        c.restore();
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

    private static void radiance(Canvas c, Paint p, RectF b, float tipX, float tipY, long t, int color,
                                 float d, boolean dark) {
        float grow = Math.max(0, easeOutBack(t / 220f));
        float cx = b.centerX(), cy = b.centerY(), big = Math.max(b.width(), b.height()) * 1.05f * grow;
        if (big > 1) {
            // rays, turning slowly
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
            // white-hot centre
            p.setShader(new RadialGradient(cx, cy, big * 0.6f, a(0xFFFFFF, dark ? 150 : 110), a(color, 0),
                    Shader.TileMode.CLAMP));
            c.drawCircle(cx, cy, big * 0.6f, p);
            p.setShader(null);
            // glints twinkling near the ends of the rays
            for (int i = 0; i < 4; i++) {
                double ang = rot * 1.6 + i * Math.PI / 2 + 0.4;
                float gx = cx + (float) Math.cos(ang) * big * 0.92f, gy = cy + (float) Math.sin(ang) * big * 0.92f;
                float tw = 0.5f + 0.5f * (float) Math.sin(t * 0.018 + i * 1.7);
                glint(c, p, gx, gy, (3 + 3 * tw) * d, a(0xFFFFFF, (int) (230 * tw)));
            }
        }
        // shock rings spreading from the key
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

    private static void plasma(Canvas c, Paint p, KeyboardTheme theme, RectF b, float tipX, float tipY, long t,
                               int seed, int color, float d, String label, boolean alt) {
        boolean dark = theme.isDark();
        float pop = 0.6f + 0.4f * easeOutBack(t / 170f);
        float cx = b.centerX(), cy = b.centerY(), ro = Math.min(b.width(), b.height()) * 0.55f * pop;
        // halo
        float hr = ro * 1.75f;
        p.setColor(Color.BLACK);
        p.setShader(new RadialGradient(cx, cy, hr, a(color, dark ? 150 : 100), a(color, 0), Shader.TileMode.CLAMP));
        c.drawCircle(cx, cy, hr, p);
        p.setShader(null);
        // a thin arc ties the orb to the key
        float gap = tipY - (cy + ro);
        if (gap > 6 * d) {
            Random r = new Random(seed * 13L + t / 50);
            float[] pts = bolt(tipX, tipY, cx + (r.nextFloat() - 0.5f) * ro * 0.4f, cy + ro * 0.9f,
                    gap * 0.18f + 2 * d, 4, r);
            glow(c, p, line(pts, P1), color, 1.0f * d, 0.85f);
        }
        // the orb
        int inner = dark ? ProPainter.mix(color, 0xFF000000, 0.85f) : ProPainter.mix(color, 0xFFFFFFFF, 0.9f);
        int outer = dark ? ProPainter.mix(color, 0xFF000000, 0.45f) : ProPainter.mix(color, 0xFFFFFFFF, 0.55f);
        p.setColor(Color.BLACK);
        p.setShader(new RadialGradient(cx, cy, ro, inner, outer, Shader.TileMode.CLAMP));
        c.drawCircle(cx, cy, ro, p);
        p.setShader(null);
        // electric arcs inside, never the same twice
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
        // rim and glass highlight
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(2 * d);
        p.setColor(color);
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
        R1.set(cx - ro, cy - ro, cx + ro, cy + ro);
        letter(c, p, R1, label, dark || alt ? 0xFFFFFFFF : ProPainter.mix(color, 0xFF000000, 0.4f), color, d, true, 0);
    }

    // ------------------------------------------------------------------ hologram

    private static void hologram(Canvas c, Paint p, KeyboardTheme theme, RectF b, RectF key, float tipX, float tipY,
                                 long t, int color, float d, String label, boolean alt) {
        boolean dark = theme.isDark();
        float unfold = easeOut(t / 160f);
        float ex = tipX, ey = tipY + 3 * d, erx = Math.min(key.width() * 0.42f, 18 * d), ery = 3.2f * d;
        float topY = b.bottom;
        float step = 3.2f * d, off = (t * 0.03f * d) % step;
        if (ey - topY > 6 * d) {
            // the projection, growing from the key
            float curTop = ey - (ey - topY) * unfold;
            float cl = ex - erx + ((b.left + 4 * d) - (ex - erx)) * unfold;
            float cr = ex + erx + ((b.right - 4 * d) - (ex + erx)) * unfold;
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
            float left = Math.min(ex - erx, cl) - 2 * d, right = Math.max(ex + erx, cr) + 2 * d;
            for (float y = ey - off; y > curTop; y -= step) c.drawRect(left, y, right, y + 0.9f * d, p);
            c.restore();
            p.setStyle(Paint.Style.STROKE);
            p.setStrokeWidth(d);
            p.setColor(a(color, 150));
            c.drawLine(ex - erx, ey, cl, curTop, p);
            c.drawLine(ex + erx, ey, cr, curTop, p);
            p.setStyle(Paint.Style.FILL);
        }
        // the projector on the key
        R1.set(ex - erx, ey - ery, ex + erx, ey + ery);
        p.setColor(a(color, 70));
        c.drawOval(R1, p);
        PB.reset();
        PB.addOval(R1, Path.Direction.CW);
        glow(c, p, PB, color, 1.2f * d, 1f);
        // the panel opens once the light reaches it
        float open = easeOut((t - 60) / 150f);
        if (open <= 0) return;
        c.save();
        c.scale(1, Math.max(0.04f, open), b.centerX(), b.bottom);
        Path s = ProPainter.shapePath(theme.pro, b, Math.min(b.width(), b.height()) * 0.2f, d, PB);
        // a dim base, so the keys underneath do not show through the letter
        p.setColor(dark ? a(ProPainter.mix(0xFF000000, color, 0.12f), 200) : a(0xFFFFFF, 215));
        c.drawPath(s, p);
        p.setColor(a(color, dark ? 46 : 30));
        c.drawPath(s, p);
        c.save();
        c.clipPath(s);
        p.setColor(a(color, dark ? 55 : 40));
        for (float y = b.bottom - off; y > b.top; y -= step) c.drawRect(b.left, y, b.right, y + 0.9f * d, p);
        c.restore();
        brackets(c, p, b, color, d);
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
