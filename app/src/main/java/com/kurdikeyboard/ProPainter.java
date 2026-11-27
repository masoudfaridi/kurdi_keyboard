package com.kurdikeyboard;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;

import java.util.Random;

/**
 * Draws the effects of the pro themes (see {@link KeyboardTheme.Pro}): gradient backgrounds
 * with glows, sparkles and artwork (heartbeat, circuits, perspective grid, flames, hexagons,
 * waves, carbon), and keys in many outlines (hexagon, cut corners, leaf, ticket, slanted,
 * notched, drop ...) drawn flat, as mechanical keycaps or as glass, with gradients, neon
 * outlines, 3D edges, flames, gloss and rainbow outlines.
 *
 * Rules of the pro themes (2.6): the artwork stays behind the keys (a backing plate under
 * see-through keys keeps the letters readable), Enter and the function keys differ only a
 * little from the letter keys.
 *
 * Used by the keyboard itself, by the theme list previews and by the key press popups.
 * Everything is drawn in code; there are no images.
 */
final class ProPainter {

    private ProPainter() {}

    // key outlines
    static final int SHAPE_ROUND = 0, SHAPE_CHAMFER = 2, SHAPE_BUBBLE = 3, SHAPE_HEX = 4, SHAPE_TECH = 5,
            SHAPE_LEAF = 6, SHAPE_TICKET = 7, SHAPE_SLANT = 8, SHAPE_NOTCH = 9, SHAPE_DROP = 10;
    // key effects
    static final int FX_NONE = 0, FX_FLAME = 1, FX_GLOSS = 2, FX_INNER_GLOW = 3;
    // background artwork
    static final int ART_NONE = 0, ART_HEARTBEAT = 1, ART_CIRCUIT = 2, ART_GRID = 3, ART_FLAMES = 4,
            ART_SPARKLE = 5, ART_HEX = 6, ART_WAVES = 7, ART_CARBON = 8;

    public static final int LETTER = 0, FUNCTION = 1, ENTER = 2;

    /** a color laid over another (alpha blending), used for pressed keys and tints */
    static int over(int base, int top) {
        float a = Color.alpha(top) / 255f;
        int r = (int) (Color.red(base) * (1 - a) + Color.red(top) * a);
        int g = (int) (Color.green(base) * (1 - a) + Color.green(top) * a);
        int b = (int) (Color.blue(base) * (1 - a) + Color.blue(top) * a);
        return Color.argb(Math.max(Color.alpha(base), Color.alpha(top)), r, g, b);
    }

    /** two colors mixed: f = 0 gives a, f = 1 gives b */
    static int mix(int a, int b, float f) {
        f = Math.max(0, Math.min(1, f));
        return Color.argb((int) (Color.alpha(a) + (Color.alpha(b) - Color.alpha(a)) * f),
                (int) (Color.red(a) + (Color.red(b) - Color.red(a)) * f),
                (int) (Color.green(a) + (Color.green(b) - Color.green(a)) * f),
                (int) (Color.blue(a) + (Color.blue(b) - Color.blue(a)) * f));
    }

    /** rainbow color for a spot across the keyboard (0..1 from the left edge) */
    static int rainbow(float fx, float fy, boolean dark) {
        float hue = ((fx * 300f) + fy * 40f) % 360f;
        return Color.HSVToColor(new float[]{hue, dark ? 0.72f : 0.62f, dark ? 1f : 0.78f});
    }

    // ------------------------------------------------------------------ background

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
            p.setColor(Color.BLACK); // opaque: a shader takes the paint's alpha
            p.setShader(new RadialGradient(cx, cy, r, col, col & 0x00FFFFFF, Shader.TileMode.CLAMP));
            c.drawCircle(cx, cy, r, p);
        }
        p.setShader(null);
        Random rnd = new Random(t.id.hashCode());
        switch (x.art) {
            case ART_HEARTBEAT: heartbeat(c, p, x, w, h, d); break;
            case ART_CIRCUIT: circuit(c, p, x, w, h, d, rnd); break;
            case ART_GRID: grid(c, p, x, w, h, d); break;
            case ART_FLAMES: edgeFlames(c, p, x, w, h, d, rnd); break;
            case ART_HEX: hexes(c, p, x, w, h, d); break;
            case ART_WAVES: waves(c, p, x, w, h, d); break;
            case ART_CARBON: carbon(c, p, x, w, h, d); break;
            default: break;
        }
        if (x.stars != 0) {
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
        if (x.art == ART_SPARKLE) sparkles(c, p, x, w, h, d, rnd);
    }

    /** glowing line: soft wide strokes under a crisp one with a white core */
    static void glowPath(Canvas c, Paint p, Path path, int color, float width, float d) {
        p.setShader(null);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeCap(Paint.Cap.ROUND);
        p.setStrokeJoin(Paint.Join.ROUND);
        p.setColor((color & 0x00FFFFFF) | 0x33000000);
        p.setStrokeWidth(width * 5);
        c.drawPath(path, p);
        p.setColor((color & 0x00FFFFFF) | 0x66000000);
        p.setStrokeWidth(width * 2.4f);
        c.drawPath(path, p);
        p.setColor(color);
        p.setStrokeWidth(width);
        c.drawPath(path, p);
        p.setColor(Color.argb(200, 255, 255, 255));
        p.setStrokeWidth(width * 0.35f);
        c.drawPath(path, p);
        p.setStyle(Paint.Style.FILL);
    }

    private static void heartbeat(Canvas c, Paint p, KeyboardTheme.Pro x, float w, float h, float d) {
        float cx = w * 0.5f, cy = h * 0.48f, s = Math.min(w * 0.5f, h * 0.62f);
        Path heart = new Path();
        heart.moveTo(cx, cy + s * 0.42f);
        heart.cubicTo(cx - s * 0.95f, cy - s * 0.15f, cx - s * 0.45f, cy - s * 0.75f, cx, cy - s * 0.28f);
        heart.cubicTo(cx + s * 0.45f, cy - s * 0.75f, cx + s * 0.95f, cy - s * 0.15f, cx, cy + s * 0.42f);
        heart.close();
        p.setColor(Color.BLACK);
        p.setShader(new RadialGradient(cx, cy, s, (x.artColor & 0x00FFFFFF) | 0x26000000,
                x.artColor & 0x00FFFFFF, Shader.TileMode.CLAMP));
        c.drawPath(heart, p);
        p.setShader(null);
        glowPath(c, p, heart, x.artColor, 2.2f * d, d);
        // the heartbeat line across the whole width
        Path ecg = new Path();
        float y = cy + s * 0.05f;
        ecg.moveTo(0, y);
        float[] beat = {0.30f, 0f, 0.33f, -0.05f, 0.36f, 0f, 0.40f, 0f, 0.43f, 0.12f, 0.46f, -0.42f,
                0.50f, 0.30f, 0.53f, -0.12f, 0.56f, 0f, 0.62f, 0f, 0.65f, -0.06f, 0.68f, 0f};
        for (int i = 0; i < beat.length; i += 2) ecg.lineTo(w * beat[i], y + beat[i + 1] * s);
        ecg.lineTo(w, y);
        glowPath(c, p, ecg, x.artColor2 != 0 ? x.artColor2 : x.artColor, 1.6f * d, d);
    }

    private static void circuit(Canvas c, Paint p, KeyboardTheme.Pro x, float w, float h, float d, Random rnd) {
        int n = (int) Math.max(10, w / (d * 26));
        p.setStrokeCap(Paint.Cap.ROUND);
        for (int i = 0; i < n; i++) {
            Path path = new Path();
            float px = rnd.nextFloat() * w, py = rnd.nextFloat() * h;
            path.moveTo(px, py);
            int segs = 2 + rnd.nextInt(3);
            for (int k = 0; k < segs; k++) {
                float len = (14 + rnd.nextFloat() * 40) * d;
                int dir = rnd.nextInt(4);
                if (dir == 0) px += len; else if (dir == 1) px -= len;
                else if (dir == 2) { px += len * 0.7f; py += len * 0.7f; } else { py -= len; }
                path.lineTo(px, py);
            }
            p.setStyle(Paint.Style.STROKE);
            p.setStrokeWidth(1.1f * d);
            p.setColor(x.artColor);
            c.drawPath(path, p);
            p.setStyle(Paint.Style.FILL);
            c.drawCircle(px, py, 2.2f * d, p);
            p.setColor((x.artColor & 0x00FFFFFF) | 0x33000000);
            c.drawCircle(px, py, 5f * d, p);
        }
    }

    private static void grid(Canvas c, Paint p, KeyboardTheme.Pro x, float w, float h, float d) {
        float horizon = h * 0.30f, vx = w * 0.5f;
        // a striped sun on the horizon
        if (x.artColor2 != 0) {
            float sr = Math.min(w, h) * 0.28f;
            p.setColor(Color.BLACK);
            p.setShader(new LinearGradient(0, horizon - sr, 0, horizon, x.artColor2, x.artColor,
                    Shader.TileMode.CLAMP));
            c.save();
            c.clipRect(0, 0, w, horizon);
            c.drawCircle(vx, horizon, sr, p);
            p.setShader(null);
            p.setColor(x.bg[x.bg.length - 1]);
            for (int i = 1; i <= 5; i++) {
                float sy = horizon - sr * (0.08f + i * 0.13f);
                c.drawRect(vx - sr, sy, vx + sr, sy + i * 0.9f * d, p);
            }
            c.restore();
        }
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(1.2f * d);
        for (int i = -12; i <= 12; i++) {
            float bx = vx + i * w * 0.11f;
            p.setColor(i == 0 ? x.artColor : (x.artColor & 0x00FFFFFF) | 0xAA000000);
            c.drawLine(vx + i * w * 0.012f, horizon, bx, h, p);
        }
        for (int i = 0; i < 12; i++) {
            float f = i / 12f;
            float y = horizon + (h - horizon) * f * f;
            p.setColor((x.artColor & 0x00FFFFFF) | ((int) (60 + 195 * f) << 24));
            c.drawLine(0, y, w, y, p);
        }
        p.setStyle(Paint.Style.FILL);
        p.setColor(Color.BLACK);
        p.setShader(new LinearGradient(0, horizon - 4 * d, 0, horizon + 18 * d,
                (x.artColor & 0x00FFFFFF) | 0x88000000, x.artColor & 0x00FFFFFF, Shader.TileMode.CLAMP));
        c.drawRect(0, horizon - 4 * d, w, horizon + 18 * d, p);
        p.setShader(null);
    }

    /** one flame tongue standing on (x, base), pointing up */
    private static void flame(Canvas c, Paint p, float fx, float base, float fw, float fh, int color, float lean) {
        Path f = new Path();
        f.moveTo(fx - fw / 2, base);
        f.cubicTo(fx - fw * 0.55f, base - fh * 0.45f, fx + lean - fw * 0.15f, base - fh * 0.6f, fx + lean, base - fh);
        f.cubicTo(fx + lean + fw * 0.05f, base - fh * 0.55f, fx + fw * 0.6f, base - fh * 0.4f, fx + fw / 2, base);
        f.close();
        p.setColor(Color.BLACK);
        p.setShader(new LinearGradient(0, base, 0, base - fh, new int[]{color, (color & 0x00FFFFFF) | 0x99000000,
                color & 0x00FFFFFF}, new float[]{0f, 0.5f, 1f}, Shader.TileMode.CLAMP));
        c.drawPath(f, p);
        p.setShader(null);
    }

    private static void edgeFlames(Canvas c, Paint p, KeyboardTheme.Pro x, float w, float h, float d, Random rnd) {
        int n = (int) (w / (d * 7));
        for (int i = 0; i < n; i++) { // along the bottom
            float fx = rnd.nextFloat() * w;
            float fh = (14 + rnd.nextFloat() * rnd.nextFloat() * 70) * d;
            flame(c, p, fx, h, (8 + rnd.nextFloat() * 14) * d, fh, i % 3 == 0 ? x.artColor2 : x.artColor,
                    (rnd.nextFloat() - 0.5f) * 14 * d);
        }
        c.save(); // and hanging from the top
        c.scale(1, -1, w / 2, h / 2);
        for (int i = 0; i < n / 2; i++) {
            float fx = rnd.nextFloat() * w;
            float fh = (8 + rnd.nextFloat() * rnd.nextFloat() * 40) * d;
            flame(c, p, fx, h, (8 + rnd.nextFloat() * 12) * d, fh, x.artColor, (rnd.nextFloat() - 0.5f) * 10 * d);
        }
        c.restore();
    }

    private static void sparkles(Canvas c, Paint p, KeyboardTheme.Pro x, float w, float h, float d, Random rnd) {
        int n = (int) Math.max(8, w * h / (d * d * 9000));
        for (int i = 0; i < n; i++) {
            float sx = rnd.nextFloat() * w, sy = rnd.nextFloat() * h;
            float s = (4 + rnd.nextFloat() * rnd.nextFloat() * 16) * d;
            p.setColor(Color.BLACK);
            p.setShader(new RadialGradient(sx, sy, s * 1.4f, (x.artColor & 0x00FFFFFF) | 0x88000000,
                    x.artColor & 0x00FFFFFF, Shader.TileMode.CLAMP));
            c.drawCircle(sx, sy, s * 1.4f, p);
            p.setShader(null);
            Path star = new Path(); // a four-pointed glint
            star.moveTo(sx, sy - s);
            star.quadTo(sx, sy, sx + s, sy);
            star.quadTo(sx, sy, sx, sy + s);
            star.quadTo(sx, sy, sx - s, sy);
            star.quadTo(sx, sy, sx, sy - s);
            p.setColor(Color.WHITE);
            c.drawPath(star, p);
        }
    }

    private static void hexes(Canvas c, Paint p, KeyboardTheme.Pro x, float w, float h, float d) {
        float r = 16 * d, hw = (float) (Math.sqrt(3) * r);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(0.9f * d);
        int row = 0;
        for (float y = 0; y < h + r; y += r * 1.5f, row++) {
            for (float cx = (row % 2) * hw / 2; cx < w + hw; cx += hw) {
                float fade = 0.35f + 0.65f * (1 - Math.abs(cx / w - 0.5f) * 2);
                p.setColor((x.artColor & 0x00FFFFFF) | ((int) (Color.alpha(x.artColor) * fade) << 24));
                Path hex = new Path();
                for (int k = 0; k < 6; k++) {
                    double a = Math.PI / 3 * k + Math.PI / 6;
                    float px = cx + (float) Math.cos(a) * r, py = y + (float) Math.sin(a) * r;
                    if (k == 0) hex.moveTo(px, py); else hex.lineTo(px, py);
                }
                hex.close();
                c.drawPath(hex, p);
            }
        }
        p.setStyle(Paint.Style.FILL);
    }

    private static void waves(Canvas c, Paint p, KeyboardTheme.Pro x, float w, float h, float d) {
        for (int k = 0; k < 4; k++) {
            Path wave = new Path();
            float base = h * (0.35f + k * 0.17f), amp = h * (0.06f + 0.02f * k);
            wave.moveTo(0, base);
            for (float px = 0; px <= w; px += 6 * d) {
                wave.lineTo(px, base + (float) Math.sin(px / w * Math.PI * (2 + k * 0.5) + k) * amp);
            }
            wave.lineTo(w, h);
            wave.lineTo(0, h);
            wave.close();
            int col = k % 2 == 0 ? x.artColor : (x.artColor2 != 0 ? x.artColor2 : x.artColor);
            p.setColor(Color.BLACK);
            p.setShader(new LinearGradient(0, base - amp, 0, h, (col & 0x00FFFFFF) | 0x40000000,
                    col & 0x00FFFFFF, Shader.TileMode.CLAMP));
            c.drawPath(wave, p);
        }
        p.setShader(null);
    }

    /** carbon fibre: a fine woven pattern of short diagonal strokes */
    private static void carbon(Canvas c, Paint p, KeyboardTheme.Pro x, float w, float h, float d) {
        float s = 4 * d;
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(1.4f * d);
        int row = 0;
        for (float y = 0; y < h + s; y += s, row++) {
            for (float px = (row % 2) * s; px < w + s; px += 2 * s) {
                p.setColor(x.artColor);
                c.drawLine(px, y, px + s, y + s, p);
                p.setColor((x.artColor & 0x00FFFFFF) | ((Color.alpha(x.artColor) / 2) << 24));
                c.drawLine(px + s, y, px + 2 * s, y - s, p);
            }
        }
        p.setStyle(Paint.Style.FILL);
    }

    // ------------------------------------------------------------------ key outlines

    private static final Path sPath = new Path();
    private static final RectF sR = new RectF(), sArc = new RectF();

    private static void poly(Path out, float... xy) {
        out.moveTo(xy[0], xy[1]);
        for (int i = 2; i < xy.length; i += 2) out.lineTo(xy[i], xy[i + 1]);
        out.close();
    }

    /**
     * The outline of a key (or a popup bubble) in the theme's shape, written into {@code out}.
     * Not a plain rectangle and not an oval: hexagon, cut corners, leaf, ticket, slanted,
     * notched or drop. {@code x} null or SHAPE_ROUND: a rounded rectangle.
     */
    static Path shapePath(KeyboardTheme.Pro x, RectF r, float rad, float d, Path out) {
        out.reset();
        float w = r.width(), h = r.height(), m = Math.min(w, h);
        int shape = x == null ? SHAPE_ROUND : x.shape;
        float cut = x != null ? x.cut : 0;
        switch (shape) {
            case SHAPE_CHAMFER: {
                float k = m * (cut > 0 ? cut : 0.24f);
                poly(out, r.left + k, r.top, r.right - k, r.top, r.right, r.top + k, r.right, r.bottom - k,
                        r.right - k, r.bottom, r.left + k, r.bottom, r.left, r.bottom - k, r.left, r.top + k);
                break;
            }
            case SHAPE_HEX: {
                float k = m * (cut > 0 ? cut : 0.3f), cy = r.centerY();
                poly(out, r.left + k, r.top, r.right - k, r.top, r.right, cy, r.right - k, r.bottom,
                        r.left + k, r.bottom, r.left, cy);
                break;
            }
            case SHAPE_TECH: { // top-left and bottom-right corners cut, the others slightly round
                float k = m * (cut > 0 ? cut : 0.32f), s = Math.min(2.5f * d, m * 0.1f);
                out.moveTo(r.left + k, r.top);
                out.lineTo(r.right - s, r.top);
                out.quadTo(r.right, r.top, r.right, r.top + s);
                out.lineTo(r.right, r.bottom - k);
                out.lineTo(r.right - k, r.bottom);
                out.lineTo(r.left + s, r.bottom);
                out.quadTo(r.left, r.bottom, r.left, r.bottom - s);
                out.lineTo(r.left, r.top + k);
                out.close();
                break;
            }
            case SHAPE_LEAF: { // two opposite corners round, the other two sharp
                float big = m * (cut > 0 ? cut : 0.5f), s = Math.min(2.5f * d, m * 0.1f);
                out.addRoundRect(r, new float[]{big, big, s, s, big, big, s, s}, Path.Direction.CW);
                break;
            }
            case SHAPE_TICKET: { // corners cut inwards in a quarter circle
                float q = m * (cut > 0 ? cut : 0.2f);
                out.moveTo(r.left + q, r.top);
                out.lineTo(r.right - q, r.top);
                sArc.set(r.right - q, r.top - q, r.right + q, r.top + q);
                out.arcTo(sArc, 180, -90, false);
                out.lineTo(r.right, r.bottom - q);
                sArc.set(r.right - q, r.bottom - q, r.right + q, r.bottom + q);
                out.arcTo(sArc, 270, -90, false);
                out.lineTo(r.left + q, r.bottom);
                sArc.set(r.left - q, r.bottom - q, r.left + q, r.bottom + q);
                out.arcTo(sArc, 0, -90, false);
                out.lineTo(r.left, r.top + q);
                sArc.set(r.left - q, r.top - q, r.left + q, r.top + q);
                out.arcTo(sArc, 90, -90, false);
                out.close();
                break;
            }
            case SHAPE_SLANT: { // a parallelogram, leaning forward
                float s = Math.min(h * 0.16f, w * 0.2f);
                poly(out, r.left + s, r.top, r.right, r.top, r.right - s, r.bottom, r.left, r.bottom);
                break;
            }
            case SHAPE_NOTCH: { // a chip: small round corners and a notch in the middle of the top edge
                float rr = Math.min(3 * d, m * 0.12f), nw = Math.min(w * 0.36f, 16 * d),
                        nd = Math.min(h * 0.09f, 3.2f * d), cx = r.centerX();
                out.moveTo(r.left + rr, r.top);
                out.lineTo(cx - nw / 2 - nd, r.top);
                out.lineTo(cx - nw / 2, r.top + nd);
                out.lineTo(cx + nw / 2, r.top + nd);
                out.lineTo(cx + nw / 2 + nd, r.top);
                out.lineTo(r.right - rr, r.top);
                out.quadTo(r.right, r.top, r.right, r.top + rr);
                out.lineTo(r.right, r.bottom - rr);
                out.quadTo(r.right, r.bottom, r.right - rr, r.bottom);
                out.lineTo(r.left + rr, r.bottom);
                out.quadTo(r.left, r.bottom, r.left, r.bottom - rr);
                out.lineTo(r.left, r.top + rr);
                out.quadTo(r.left, r.top, r.left + rr, r.top);
                out.close();
                break;
            }
            case SHAPE_DROP: { // a water drop, pointed at the top
                float cx = r.centerX(), t = r.top, b = r.bottom, l = r.left, rt = r.right;
                float sw = Math.min(w, h * 1.15f); // curvature of a drop of normal width; wide keys stay flat
                float sh = t + h * 0.6f;
                out.moveTo(cx, t);
                out.cubicTo(cx + sw * 0.10f, t + h * 0.10f, rt, t + h * 0.26f, rt, sh);
                out.cubicTo(rt, t + h * 0.88f, rt - sw * 0.20f, b, rt - sw * 0.5f, b);
                out.lineTo(l + sw * 0.5f, b);
                out.cubicTo(l + sw * 0.20f, b, l, t + h * 0.88f, l, sh);
                out.cubicTo(l, t + h * 0.26f, cx - sw * 0.10f, t + h * 0.10f, cx, t);
                out.close();
                break;
            }
            case SHAPE_BUBBLE: {
                float rr = m * 0.42f;
                out.addRoundRect(r, rr, rr, Path.Direction.CW);
                break;
            }
            default:
                out.addRoundRect(r, rad, rad, Path.Direction.CW);
        }
        return out;
    }

    // ------------------------------------------------------------------ keys

    /**
     * Draws one key. {@code r} is the key's rectangle; it is restored before returning.
     * @param tint rainbow color of this key (used when the theme is a rainbow theme)
     */
    static void key(Canvas c, Paint p, RectF r, float rad, KeyboardTheme t, int kind, boolean pressed,
                    int tint, float d, int alpha) {
        KeyboardTheme.Pro x = t.pro;
        if (x.keycap) {
            keycap(c, p, r, rad, t, kind, pressed, tint, d, alpha);
            return;
        }
        int line = x.rainbow ? tint : (kind == ENTER && x.line != 0 ? enterLine(t) : x.line);
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
            c.drawPath(shapePath(x, r, rad, d, sPath), p);
            r.offset(0, -lift);
            p.clearShadowLayer();
        }
        r.offset(0, sink);
        Path s = shapePath(x, r, rad, d, sPath);

        // glow behind the key (a neon key without a 3D edge)
        if (glow != 0 && x.edge == 0 && line != 0) {
            p.setStyle(Paint.Style.STROKE);
            p.setStrokeWidth(Math.max(1, x.lineDp) * d);
            p.setColor(withAlpha(glow, Math.min(alpha, 170)));
            p.setShadowLayer(x.glowDp * d, 0, 0, glow);
            c.drawPath(s, p);
            p.clearShadowLayer();
            p.setStyle(Paint.Style.FILL);
        }

        // backing plate: the artwork stays behind see-through keys
        if (x.plate != 0) {
            p.setColor(withAlpha(x.plate, alpha));
            c.drawPath(s, p);
        }

        // the key face
        int top = faceTop(t, kind), bottom = faceBottom(t, kind);
        if (top != bottom) {
            p.setColor(Color.BLACK); // opaque: a shader takes the paint's alpha
            p.setShader(new LinearGradient(0, r.top, 0, r.bottom, withAlpha(top, alpha), withAlpha(bottom, alpha),
                    Shader.TileMode.CLAMP));
        } else {
            p.setColor(withAlpha(top, alpha));
        }
        c.drawPath(s, p);
        p.setShader(null);

        effects(c, p, r, s, x, line != 0 ? line : x.fxColor, d);

        // light reflection on the upper half
        if (x.sheen != 0) {
            p.setColor(Color.BLACK);
            p.setShader(new LinearGradient(0, r.top, 0, r.centerY(), x.sheen, x.sheen & 0x00FFFFFF,
                    Shader.TileMode.CLAMP));
            c.drawPath(s, p);
            p.setShader(null);
        }

        if (pressed) {
            p.setColor(x.dark ? 0x33FFFFFF : 0x1A000000);
            c.drawPath(s, p);
        }

        // crisp outline on top
        if (line != 0) {
            p.setStyle(Paint.Style.STROKE);
            p.setStrokeWidth(Math.max(0.8f, x.lineDp) * d);
            p.setColor(withAlpha(line, alpha));
            c.drawPath(s, p);
            p.setStyle(Paint.Style.FILL);
            if (x.shape == SHAPE_CHAMFER) hudTicks(c, p, r, line, d);
        }
        r.offset(0, -sink);
    }

    /** face colors: letters as set; function keys and Enter only a little different (2.6) */
    static int faceTop(KeyboardTheme t, int kind) {
        KeyboardTheme.Pro x = t.pro;
        return kind == ENTER ? over(x.keyTop, enterTint(t)) : kind == FUNCTION ? x.fnTop : x.keyTop;
    }

    static int faceBottom(KeyboardTheme t, int kind) {
        KeyboardTheme.Pro x = t.pro;
        return kind == ENTER ? over(x.keyBottom, enterTint(t)) : kind == FUNCTION ? x.fnBottom : x.keyBottom;
    }

    private static int enterTint(KeyboardTheme t) {
        return (t.accent & 0x00FFFFFF) | ((t.pro.dark ? 0x30 : 0x26) << 24);
    }

    private static int enterLine(KeyboardTheme t) {
        return mix(t.pro.line, t.accent | 0xFF000000, 0.6f);
    }

    /** key effects inside the key face: flames, gloss, inner glow, glass */
    private static void effects(Canvas c, Paint p, RectF r, Path s, KeyboardTheme.Pro x, int color, float d) {
        if (x.fx == FX_NONE && !x.glass) return;
        c.save();
        c.clipPath(s);
        switch (x.fx) {
            case FX_FLAME: {
                Random rnd = new Random((long) (r.left * 31 + r.top * 17));
                int n = Math.max(3, (int) (r.width() / (6 * d)));
                p.setColor(Color.BLACK);
                p.setShader(new LinearGradient(0, r.bottom, 0, r.top, (x.fxColor & 0x00FFFFFF) | 0x66000000,
                        x.fxColor & 0x00FFFFFF, Shader.TileMode.CLAMP));
                c.drawRect(r, p);
                p.setShader(null);
                for (int i = 0; i < n; i++) {
                    float fx = r.left + r.width() * (i + 0.5f) / n + (rnd.nextFloat() - 0.5f) * 4 * d;
                    float fh = r.height() * (0.25f + rnd.nextFloat() * 0.35f);
                    flame(c, p, fx, r.bottom + d, (5 + rnd.nextFloat() * 5) * d, fh,
                            i % 2 == 0 ? x.fxColor : lighten(x.fxColor), (rnd.nextFloat() - 0.5f) * 5 * d);
                }
                break;
            }
            case FX_GLOSS: {
                float band = r.height() * 0.24f;
                p.setColor(Color.BLACK);
                p.setShader(new LinearGradient(0, r.bottom - band, 0, r.bottom, darken(x.fxColor), x.fxColor,
                        Shader.TileMode.CLAMP));
                c.drawRect(r.left, r.bottom - band, r.right, r.bottom, p);
                p.setShader(null);
                p.setColor(withAlpha(lighten(x.fxColor), 200));
                c.drawRect(r.left, r.bottom - band, r.right, r.bottom - band + 1.2f * d, p);
                sR.set(r.left + 3 * d, r.top + 2 * d, r.right - 3 * d, r.top + r.height() * 0.42f);
                p.setColor(Color.BLACK);
                p.setShader(new LinearGradient(0, sR.top, 0, sR.bottom, 0x55FFFFFF, 0x00FFFFFF,
                        Shader.TileMode.CLAMP));
                c.drawRoundRect(sR, 6 * d, 6 * d, p);
                p.setShader(null);
                break;
            }
            case FX_INNER_GLOW: {
                p.setColor(Color.BLACK);
                p.setShader(new LinearGradient(0, r.top, 0, r.bottom, color & 0x00FFFFFF,
                        (color & 0x00FFFFFF) | 0x59000000, Shader.TileMode.CLAMP));
                c.drawRect(r, p);
                p.setShader(null);
                break;
            }
            default: break;
        }
        if (x.glass) {
            // glass: darker towards the rim, a bright highlight at the top, a small reflection
            p.setColor(Color.BLACK);
            p.setShader(new RadialGradient(r.centerX(), r.top + r.height() * 0.15f, r.width() * 0.9f,
                    0x00000000, x.dark ? 0x40000000 : 0x26000000, Shader.TileMode.CLAMP));
            c.drawRect(r, p);
            p.setShader(null);
            // a soft band of light across the top, above the letter (never over it)
            sR.set(r.left + r.width() * 0.16f, r.top + r.height() * 0.06f, r.right - r.width() * 0.16f,
                    r.top + r.height() * 0.30f);
            p.setColor(Color.BLACK);
            p.setShader(new LinearGradient(0, sR.top, 0, sR.bottom, x.dark ? 0x4DFFFFFF : 0x99FFFFFF, 0x00FFFFFF,
                    Shader.TileMode.CLAMP));
            c.drawOval(sR, p);
            p.setShader(null);
            p.setColor(x.dark ? 0x55FFFFFF : 0x88FFFFFF);
            c.drawCircle(r.right - r.width() * 0.26f, r.bottom - r.height() * 0.2f,
                    Math.min(r.width(), r.height()) * 0.04f, p);
        }
        c.restore();
    }

    /** sci-fi corner marks next to a chamfered key */
    private static void hudTicks(Canvas c, Paint p, RectF r, int color, float d) {
        float k = Math.min(r.width(), r.height()) * 0.24f, o = 2.4f * d * 0.707f;
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(1.6f * d);
        p.setStrokeCap(Paint.Cap.ROUND);
        p.setColor(color);
        // short strokes parallel to the top-left and bottom-right cut corners, just outside them
        c.drawLine(r.left - o + k * 0.2f, r.top + k * 0.8f - o, r.left + k * 0.8f - o, r.top + k * 0.2f - o, p);
        c.drawLine(r.right + o - k * 0.2f, r.bottom - k * 0.8f + o, r.right - k * 0.8f + o, r.bottom - k * 0.2f + o, p);
        p.setStyle(Paint.Style.FILL);
        p.setColor(withAlpha(color, 230));
        c.drawRect(r.left + k * 1.3f, r.bottom - 1.6f * d, r.left + k * 2.3f, r.bottom, p);
    }

    private static final Path sFace = new Path();
    private static final RectF sFaceR = new RectF();

    /** a mechanical keycap in the theme's outline: a darker body with a lighter top face */
    private static void keycap(Canvas c, Paint p, RectF r, float rad, KeyboardTheme t, int kind, boolean pressed,
                               int tint, float d, int alpha) {
        KeyboardTheme.Pro x = t.pro;
        int top = faceTop(t, kind), bottom = faceBottom(t, kind);
        float press = pressed ? 1.5f * d : 0;
        p.setShader(null);
        p.setStyle(Paint.Style.FILL);
        // shadow under the cap
        p.setColor(x.dark ? 0x99000000 : 0x33000000);
        sR.set(r.left, r.top + 2 * d, r.right, r.bottom + 2 * d);
        c.drawPath(shapePath(x, sR, rad, d, sPath), p);
        // body (the sides)
        int side = x.edge != 0 ? x.edge : darken(bottom);
        if (kind == ENTER) side = over(side, enterTint(t));
        Path body = shapePath(x, r, rad, d, sPath);
        p.setColor(Color.BLACK);
        p.setShader(new LinearGradient(r.left, 0, r.right, 0, new int[]{darken(side), side, darken(side)},
                null, Shader.TileMode.CLAMP));
        c.drawPath(body, p);
        p.setShader(null);
        if (x.glow != 0 || x.line != 0) {
            int rim = x.rainbow ? tint : (x.line != 0 ? x.line : x.glow);
            p.setStyle(Paint.Style.STROKE);
            p.setStrokeWidth(Math.max(0.8f, x.lineDp) * d);
            p.setColor(withAlpha(rim, alpha));
            if (x.glow != 0) p.setShadowLayer(x.glowDp * d, 0, 0, x.rainbow ? tint : x.glow);
            c.drawPath(body, p);
            p.clearShadowLayer();
            p.setStyle(Paint.Style.FILL);
        }
        // top face, set in from the sides: more of the front side shows
        float inX = Math.min(r.width() * 0.09f, 4.5f * d);
        sFaceR.set(r.left + inX, r.top + 1.5f * d + press, r.right - inX, r.bottom - 6 * d + press);
        Path face = shapePath(x, sFaceR, Math.max(2 * d, rad * 0.75f), d, sFace);
        p.setColor(Color.BLACK);
        p.setShader(new LinearGradient(0, sFaceR.top, 0, sFaceR.bottom, withAlpha(top, alpha),
                withAlpha(bottom, alpha), Shader.TileMode.CLAMP));
        c.drawPath(face, p);
        p.setShader(null);
        // a thin light edge around the face
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(0.8f * d);
        p.setColor(x.dark ? 0x40FFFFFF : 0x99FFFFFF);
        c.drawPath(face, p);
        p.setStyle(Paint.Style.FILL);
        if (pressed) {
            p.setColor(x.dark ? 0x26FFFFFF : 0x14000000);
            c.drawPath(face, p);
        }
    }

    /**
     * How far a key's label moves down: pressed 3D keys sink, keycap labels sit on the top
     * face, drop keys have their middle lower.
     */
    static float labelShift(KeyboardTheme t, boolean pressed, float d, float keyH) {
        if (t.pro == null) return 0;
        float s = 0;
        if (t.pro.keycap) s += (pressed ? 1.5f * d : 0) - 2.25f * d;
        else if (pressed && t.pro.edge != 0) s += 3f * d * 0.7f;
        if (t.pro.shape == SHAPE_DROP) s += keyH * 0.07f;
        return s;
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
