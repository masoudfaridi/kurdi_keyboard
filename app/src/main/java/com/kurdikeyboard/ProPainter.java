package com.kurdikeyboard;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;

import java.util.Random;

/**
 * Draws the effects of the pro themes (see {@link KeyboardTheme.Pro}): gradient backgrounds
 * with soft glows and sparkles, gradient keys, neon outlines, 3D key edges and rainbow colors.
 * Used by the keyboard itself and by the previews in the theme list. Everything is drawn in
 * code; there are no images.
 */
final class ProPainter {

    private ProPainter() {}

    /** a color laid over another (alpha blending), used for pressed keys */
    static int over(int base, int top) {
        float a = Color.alpha(top) / 255f;
        int r = (int) (Color.red(base) * (1 - a) + Color.red(top) * a);
        int g = (int) (Color.green(base) * (1 - a) + Color.green(top) * a);
        int b = (int) (Color.blue(base) * (1 - a) + Color.blue(top) * a);
        return Color.argb(Math.max(Color.alpha(base), Color.alpha(top)), r, g, b);
    }

    /** rainbow color for a spot across the keyboard (0..1 from the left edge) */
    static int rainbow(float fx, float fy, boolean dark) {
        float hue = ((fx * 300f) + fy * 40f) % 360f;
        return Color.HSVToColor(new float[]{hue, dark ? 0.72f : 0.62f, dark ? 1f : 0.78f});
    }

    private static Bitmap sBgCache;
    private static String sBgKey;

    /** The background, cached as a bitmap for the given size. */
    static void background(Canvas c, KeyboardTheme t, int w, int h, float d) {
        if (w <= 0 || h <= 0) return;
        String key = t.id + w + "x" + h;
        if (sBgCache == null || !key.equals(sBgKey)) {
            if (sBgCache != null) sBgCache.recycle();
            sBgCache = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            paintBackground(new Canvas(sBgCache), t, w, h, d);
            sBgKey = key;
        }
        c.drawBitmap(sBgCache, 0, 0, null);
    }

    /** Draws the background directly (no cache): the small previews in the theme list. */
    static void paintBackground(Canvas c, KeyboardTheme t, float w, float h, float d) {
        KeyboardTheme.Pro x = t.pro;
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        if (x.bg.length == 1) {
            p.setColor(x.bg[0]);
        } else {
            float[] pos = new float[x.bg.length];
            for (int i = 0; i < pos.length; i++) pos[i] = i / (float) (pos.length - 1);
            p.setShader(new LinearGradient(0, 0, x.diagonal ? w : 0, h, x.bg, pos, Shader.TileMode.CLAMP));
        }
        c.drawRect(0, 0, w, h, p);
        p.setShader(null);
        float size = Math.max(w, h);
        for (int i = 0; i < x.blobColor.length; i++) {
            float cx = x.blobX[i] * w, cy = x.blobY[i] * h, r = Math.max(1, x.blobR[i] * size);
            int col = x.blobColor[i];
            p.setShader(new RadialGradient(cx, cy, r, col, col & 0x00FFFFFF, Shader.TileMode.CLAMP));
            c.drawCircle(cx, cy, r, p);
        }
        p.setShader(null);
        if (x.stars != 0) {
            Random rnd = new Random(t.id.hashCode());
            int n = (int) Math.min(140, w * h / (d * d * 700));
            for (int i = 0; i < n; i++) {
                float sx = rnd.nextFloat() * w, sy = rnd.nextFloat() * h;
                float sr = (0.4f + rnd.nextFloat() * rnd.nextFloat() * 1.4f) * d;
                int a = (int) (Color.alpha(x.stars) * (0.25f + 0.75f * rnd.nextFloat()));
                p.setColor((x.stars & 0x00FFFFFF) | (a << 24));
                c.drawCircle(sx, sy, sr, p);
                if (sr > 1.3f * d) { // a few bigger ones get a small cross of light
                    p.setStrokeWidth(0.6f * d);
                    c.drawLine(sx - sr * 2.6f, sy, sx + sr * 2.6f, sy, p);
                    c.drawLine(sx, sy - sr * 2.6f, sx, sy + sr * 2.6f, p);
                }
            }
        }
    }

    public static final int LETTER = 0, FUNCTION = 1, ENTER = 2;

    /**
     * Draws one key. {@code r} is the key's rectangle; it is restored before returning.
     * @param tint rainbow color of this key (used when the theme is a rainbow theme)
     */
    static void key(Canvas c, Paint p, RectF r, float rad, KeyboardTheme t, int kind, boolean pressed,
                    int tint, float d, int alpha) {
        KeyboardTheme.Pro x = t.pro;
        int line = x.rainbow ? tint : x.line;
        int glow = x.rainbow && x.glow != 0 ? tint : x.glow;
        float lift = x.edge != 0 ? 3f * d : 0;
        float sink = pressed ? lift * 0.7f : 0; // a pressed 3D key goes down
        p.setShader(null);
        p.setStyle(Paint.Style.FILL);

        // 3D edge under the key
        if (x.edge != 0) {
            int edge = x.rainbow ? darken(tint) : x.edge;
            p.setColor(withAlpha(edge, alpha));
            if (glow != 0) p.setShadowLayer(x.glowDp * d, 0, 0, glow);
            r.offset(0, lift);
            c.drawRoundRect(r, rad, rad, p);
            r.offset(0, -lift);
            p.clearShadowLayer();
        }
        r.offset(0, sink);

        // glow behind the key (a neon key without a 3D edge)
        if (glow != 0 && x.edge == 0 && line != 0) {
            p.setColor(0);
            p.setStyle(Paint.Style.STROKE);
            p.setStrokeWidth(Math.max(1, x.lineDp) * d);
            p.setColor(withAlpha(glow, Math.min(alpha, 170)));
            p.setShadowLayer(x.glowDp * d, 0, 0, glow);
            c.drawRoundRect(r, rad, rad, p);
            p.clearShadowLayer();
            p.setStyle(Paint.Style.FILL);
        }

        // the key face
        int top, bottom;
        if (kind == ENTER) {
            top = lighten(t.accent);
            bottom = t.accent;
        } else if (kind == FUNCTION) {
            top = x.fnTop;
            bottom = x.fnBottom;
        } else {
            top = x.keyTop;
            bottom = x.keyBottom;
        }
        if (top != bottom) {
            p.setShader(new LinearGradient(0, r.top, 0, r.bottom, withAlpha(top, alpha), withAlpha(bottom, alpha),
                    Shader.TileMode.CLAMP));
        } else {
            p.setColor(withAlpha(top, alpha));
        }
        c.drawRoundRect(r, rad, rad, p);
        p.setShader(null);

        // light reflection on the upper half
        if (x.sheen != 0 && kind != ENTER) {
            p.setShader(new LinearGradient(0, r.top, 0, r.centerY(), x.sheen, x.sheen & 0x00FFFFFF,
                    Shader.TileMode.CLAMP));
            c.drawRoundRect(r, rad, rad, p);
            p.setShader(null);
        }

        if (pressed) {
            p.setColor(kind == ENTER ? 0x33000000 : (x.dark ? 0x33FFFFFF : 0x1A000000));
            c.drawRoundRect(r, rad, rad, p);
        }

        // crisp outline on top
        if (line != 0 && kind != ENTER) {
            p.setStyle(Paint.Style.STROKE);
            p.setStrokeWidth(Math.max(0.8f, x.lineDp) * d);
            p.setColor(withAlpha(line, alpha));
            c.drawRoundRect(r, rad, rad, p);
            p.setStyle(Paint.Style.FILL);
        }
        r.offset(0, -sink);
    }

    /** how far a key's label moves down when the key is pressed (3D keys sink) */
    static float sink(KeyboardTheme t, boolean pressed, float d) {
        return pressed && t.pro != null && t.pro.edge != 0 ? 3f * d * 0.7f : 0;
    }

    static int withAlpha(int color, int alpha255) {
        if (alpha255 >= 255) return color;
        int a = Color.alpha(color) * alpha255 / 255;
        return (color & 0x00FFFFFF) | (a << 24);
    }

    static int darken(int c) {
        return Color.argb(Color.alpha(c), (int) (Color.red(c) * 0.6f), (int) (Color.green(c) * 0.6f),
                (int) (Color.blue(c) * 0.6f));
    }

    static int lighten(int c) {
        return Color.argb(Color.alpha(c), Color.red(c) + (255 - Color.red(c)) / 4,
                Color.green(c) + (255 - Color.green(c)) / 4, Color.blue(c) + (255 - Color.blue(c)) / 4);
    }
}
