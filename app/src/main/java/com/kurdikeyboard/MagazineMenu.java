package com.kurdikeyboard;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * The menu of the tools row's last button: a revolver cylinder with seven chambers that rolls
 * in from the keyboard's right edge and clicks into place over the keys. Drawn like a
 * schematic: pitch circle, spokes to the hub, flutes cut into the rim and a callout label for
 * each chamber. Colors come from the keyboard theme. (2.9.0 had half a cylinder, three chambers.)
 */
final class MagazineMenu extends View {

    interface Listener {
        void onPick(int index);
        void onClosed();
    }

    /** a chamber: an icon, or a character drawn as the icon (♞), and its name */
    static final class Item {
        final int icon;
        final String glyph;
        final String label;

        Item(int icon, String label) {
            this.icon = icon;
            this.glyph = null;
            this.label = label;
        }

        Item(String glyph, String label) {
            this.icon = 0;
            this.glyph = glyph;
            this.label = label;
        }
    }

    /**
     * Where the seven chambers settle, in degrees (0 = right, 90 = up), clockwise from the upper
     * left. One sits at the bottom with its label under the cylinder; three on each side have
     * their labels beside it, far enough apart in height not to touch.
     */
    private static final float[] ANGLES = {115.714f, 64.286f, 12.857f, -38.571f, -90f, -141.429f, 167.143f};

    private final KeyboardTheme t;
    private final Item[] items;
    private final Drawable[] icons;
    private final float anchorBottom;
    private final Listener listener;
    private final float d;
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final TextPaint text = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    private final TextPaint glyph = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    private final Path face = new Path(), cuts = new Path();
    private final RectF[] labelRects;

    private float progress;   // 0 closed .. 1 open
    private ValueAnimator anim;
    private boolean closing, closedNotified;
    private int pressed = -1;

    // layout when open
    private float radius, pitch, chamberR, hubX, hubY;

    MagazineMenu(Context c, KeyboardTheme theme, Item[] items, float anchorBottom, Listener l) {
        super(c);
        this.t = theme;
        this.items = items;
        this.anchorBottom = anchorBottom;
        this.listener = l;
        d = c.getResources().getDisplayMetrics().density;
        float sp = c.getResources().getDisplayMetrics().scaledDensity;
        icons = new Drawable[items.length];
        labelRects = new RectF[items.length];
        for (int i = 0; i < items.length; i++) {
            if (items[i].icon != 0) {
                Drawable ic = c.getResources().getDrawable(items[i].icon, null).mutate();
                ic.setTint(theme.text);
                icons[i] = ic;
            }
            labelRects[i] = new RectF();
        }
        text.setTextAlign(Paint.Align.CENTER);
        text.setTextSize(12.5f * sp);
        glyph.setTextAlign(Paint.Align.CENTER);
        setLayerType(LAYER_TYPE_HARDWARE, null);
    }

    void open() {
        animateTo(1f, 560);
    }

    void close() {
        if (closing) return;
        closing = true;
        pressed = -1;
        animateTo(0f, 260);
    }

    private void animateTo(final float target, long ms) {
        if (anim != null) anim.cancel();
        anim = ValueAnimator.ofFloat(progress, target);
        anim.setDuration(ms);
        anim.setInterpolator(new LinearInterpolator()); // the easing is done per part in onDraw
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override public void onAnimationUpdate(ValueAnimator a) {
                progress = (float) a.getAnimatedValue();
                invalidate();
                if (target == 0f && progress <= 0f && !closedNotified && listener != null) {
                    closedNotified = true;
                    listener.onClosed();
                }
            }
        });
        anim.start();
    }

    @Override
    protected void onSizeChanged(int w, int h, int ow, int oh) {
        float top = Math.max(0, anchorBottom) + 8 * d;
        float bottom = h - 6 * d;
        // room under the cylinder for the bottom label, beside it for the others
        radius = Math.min(Math.min((bottom - top - 40 * d) / 2f, w / 2f - 86 * d), 140 * d);
        radius = Math.max(radius, 60 * d);
        hubX = w / 2f;
        hubY = top + radius;
        pitch = radius * 0.60f;
        chamberR = radius * 0.22f;
        glyph.setTextSize(chamberR * 1.05f);
    }

    // ------------------------------------------------------------------ drawing

    private static float easeOutCubic(float x) {
        float f = 1 - x;
        return 1 - f * f * f;
    }

    private static float easeOutBack(float x) {
        float s = 1.4f, f = x - 1;
        return 1 + (s + 1) * f * f * f + s * f * f;
    }

    private static int alpha(int color, float a) {
        int al = Math.round(Color.alpha(color) * Math.max(0, Math.min(1, a)));
        return (color & 0x00FFFFFF) | (al << 24);
    }

    private static int mix(int a, int b, float f) {
        return Color.argb(255,
                Math.round(Color.red(a) + (Color.red(b) - Color.red(a)) * f),
                Math.round(Color.green(a) + (Color.green(b) - Color.green(a)) * f),
                Math.round(Color.blue(a) + (Color.blue(b) - Color.blue(a)) * f));
    }

    /** how far the cylinder still has to roll; a little past the end and back = the click */
    private float travel(float p) {
        return (1 - easeOutBack(p)) * (getWidth() / 2f + radius + 24 * d);
    }

    @Override
    protected void onDraw(Canvas c) {
        if (radius <= 0) return;
        float p = Math.max(0, Math.min(1, progress));
        float tr = travel(p);
        float cxHub = hubX + tr;
        // rolling to the left turns it counter-clockwise: the turn is the distance / radius
        float rot = (float) Math.toDegrees(-tr / radius);
        boolean dark = t.isDark();
        int disc = t.key | 0xFF000000;
        int discIn = mix(disc, dark ? Color.WHITE : Color.BLACK, 0.06f);
        int discOut = mix(disc, dark ? Color.BLACK : Color.WHITE, dark ? 0.25f : 0.0f);
        int line = t.accent;

        // the keys dim behind the menu
        paint.setShader(null);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(alpha(0xFF000000, 0.34f * easeOutCubic(p)));
        c.drawRect(0, 0, getWidth(), getHeight(), paint);

        // the cylinder face, with a flute cut into the rim between each two chambers
        face.reset();
        face.addCircle(cxHub, hubY, radius, Path.Direction.CW);
        cuts.reset();
        float fr = radius * 0.095f;
        for (float a : ANGLES) {
            double r = Math.toRadians(a + 180f / ANGLES.length + rot);
            cuts.addCircle(cxHub + (float) Math.cos(r) * (radius + fr * 0.45f),
                    hubY - (float) Math.sin(r) * (radius + fr * 0.45f), fr, Path.Direction.CW);
        }
        face.op(cuts, Path.Op.DIFFERENCE);
        paint.setColor(Color.BLACK);
        paint.setShader(new RadialGradient(cxHub, hubY, radius, discIn, discOut, Shader.TileMode.CLAMP));
        if (p > 0.01f) paint.setShadowLayer(14 * d, 0, 5 * d, 0x55000000);
        c.drawPath(face, paint);
        paint.clearShadowLayer();
        paint.setShader(null);

        // rim: a strong line along the fluted edge and a thin circle outside it
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2.5f * d);
        paint.setColor(line);
        c.drawPath(face, paint);
        paint.setStrokeWidth(1 * d);
        paint.setColor(alpha(line, 0.35f));
        c.drawCircle(cxHub, hubY, radius + 5 * d, paint);

        // pitch circle through the chambers, dashed like a drawing
        paint.setColor(alpha(t.hint, 0.7f));
        paint.setPathEffect(new DashPathEffect(new float[]{4 * d, 4 * d}, rot * d * 0.2f));
        c.drawCircle(cxHub, hubY, pitch, paint);
        paint.setPathEffect(null);

        // spokes, chambers, icons
        float iconScale = 0.65f + 0.35f * Math.min(1, easeOutBack(p));
        for (int i = 0; i < items.length && i < ANGLES.length; i++) {
            double r = Math.toRadians(ANGLES[i] + rot);
            float cx = cxHub + (float) Math.cos(r) * pitch, cy = hubY - (float) Math.sin(r) * pitch;
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(1 * d);
            paint.setColor(alpha(line, 0.45f));
            c.drawLine(cxHub, hubY, cx, cy, paint);

            boolean down = i == pressed;
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(down ? line : mix(t.fn | 0xFF000000, disc, 0.35f));
            c.drawCircle(cx, cy, chamberR, paint);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(2 * d);
            paint.setColor(line);
            c.drawCircle(cx, cy, chamberR, paint);
            paint.setStrokeWidth(1 * d);
            paint.setColor(alpha(down ? t.onAccent : t.hint, 0.6f));
            c.drawCircle(cx, cy, chamberR * 0.80f, paint); // the bore

            int fg = down ? t.onAccent : t.text;
            if (icons[i] != null) {
                Drawable ic = icons[i];
                int s = Math.round(Math.min(chamberR * 1.05f, 28 * d) * iconScale);
                ic.setTint(fg);
                ic.setBounds(Math.round(cx - s / 2f), Math.round(cy - s / 2f), Math.round(cx + s / 2f), Math.round(cy + s / 2f));
                ic.draw(c);
            } else if (items[i].glyph != null) {
                glyph.setColor(fg);
                c.save();
                c.scale(iconScale, iconScale, cx, cy);
                Paint.FontMetrics gm = glyph.getFontMetrics();
                c.drawText(items[i].glyph, cx, cy - (gm.ascent + gm.descent) / 2, glyph);
                c.restore();
            }
        }

        // the hub (axle)
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(line);
        c.drawCircle(cxHub, hubY, radius * 0.14f, paint);
        paint.setColor(disc);
        c.drawCircle(cxHub, hubY, radius * 0.06f, paint);

        // a ring that runs out when the cylinder clicks into place
        if (!closing && p > 0.75f && p < 1f) {
            float k = (p - 0.75f) / 0.25f;
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(2 * d);
            paint.setColor(alpha(line, 0.55f * (1 - k)));
            c.drawCircle(cxHub, hubY, radius + (5 + 14 * k) * d, paint);
        }

        // callout labels, joined to their chambers
        float la = Math.max(0, Math.min(1, (p - 0.6f) / 0.4f));
        if (la > 0) drawLabels(c, cxHub, rot, la);
    }

    /** labels beside the cylinder at each chamber's height; the bottom one under it */
    private void drawLabels(Canvas c, float cxHub, float rot, float la) {
        Paint.FontMetrics fm = text.getFontMetrics();
        float th = fm.descent - fm.ascent;
        for (int i = 0; i < items.length && i < ANGLES.length; i++) {
            double r = Math.toRadians(ANGLES[i] + rot);
            float cx = cxHub + (float) Math.cos(r) * pitch, cy = hubY - (float) Math.sin(r) * pitch;
            float w = text.measureText(items[i].label) + 18 * d, h = th + 10 * d;
            double settled = Math.toRadians(ANGLES[i]);
            float side = (float) Math.cos(settled); // < 0 left, > 0 right, about 0 at the bottom
            RectF box = labelRects[i];
            float lx1, ly1, lx2, ly2; // leader line
            if (Math.abs(side) < 0.3f) {
                float top = hubY + radius + 9 * d;
                box.set(cx - w / 2, top, cx + w / 2, top + h);
                lx1 = cx; ly1 = cy + chamberR; lx2 = cx; ly2 = top;
            } else {
                float dy = cy - hubY;
                float edge = (float) Math.sqrt(Math.max(0, radius * radius - dy * dy)); // rim at this height
                if (side < 0) {
                    float right = cxHub - edge - 12 * d;
                    box.set(right - w, cy - h / 2, right, cy + h / 2);
                    lx1 = cx - chamberR; lx2 = right;
                } else {
                    float left = cxHub + edge + 12 * d;
                    box.set(left, cy - h / 2, left + w, cy + h / 2);
                    lx1 = cx + chamberR; lx2 = left;
                }
                ly1 = cy; ly2 = cy;
                // keep it on screen on narrow phones
                if (box.left < 4 * d) box.offset(4 * d - box.left, 0);
                if (box.right > getWidth() - 4 * d) box.offset(getWidth() - 4 * d - box.right, 0);
            }

            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(1 * d);
            paint.setColor(alpha(t.accent, 0.7f * la));
            c.drawLine(lx1, ly1, lx2, ly2, paint);
            c.drawCircle(lx1, ly1, 1.8f * d, paint);

            boolean down = i == pressed;
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(alpha(down ? t.accent : (t.key | 0xFF000000), la));
            c.drawRoundRect(box, h / 2, h / 2, paint);
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(alpha(t.accent, la));
            c.drawRoundRect(box, h / 2, h / 2, paint);
            text.setColor(alpha(down ? t.onAccent : t.text, la));
            c.drawText(items[i].label, box.centerX(), box.centerY() - (fm.ascent + fm.descent) / 2, text);
        }
    }

    // ------------------------------------------------------------------ touch

    /** chamber or label under a point, or -1 */
    private int itemAt(float x, float y) {
        if (progress < 0.85f) return -1;
        for (int i = 0; i < items.length && i < ANGLES.length; i++) {
            double r = Math.toRadians(ANGLES[i]);
            float cx = hubX + (float) Math.cos(r) * pitch, cy = hubY - (float) Math.sin(r) * pitch;
            float dx = x - cx, dy = y - cy, hit = chamberR * 1.15f;
            if (dx * dx + dy * dy <= hit * hit) return i;
            if (labelRects[i].contains(x, y)) return i;
        }
        return -1;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (closing) return true;
        switch (e.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                pressed = itemAt(e.getX(), e.getY());
                if (pressed < 0) close(); // outside the chambers: put it away
                invalidate();
                return true;
            case MotionEvent.ACTION_MOVE:
                if (pressed >= 0 && itemAt(e.getX(), e.getY()) != pressed) {
                    pressed = -1;
                    invalidate();
                }
                return true;
            case MotionEvent.ACTION_UP: {
                int pick = pressed;
                pressed = -1;
                if (pick >= 0 && listener != null) {
                    performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP);
                    listener.onPick(pick);
                }
                return true;
            }
            case MotionEvent.ACTION_CANCEL:
                pressed = -1;
                invalidate();
                return true;
        }
        return true;
    }

    @Override
    protected void onDetachedFromWindow() {
        if (anim != null) anim.cancel();
        super.onDetachedFromWindow();
    }
}
