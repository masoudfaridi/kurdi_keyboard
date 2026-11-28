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
 * The menu of the tools row's last button (2.9.0): a half revolver cylinder that slides out of
 * the keyboard's right edge and turns into place, its chambers holding settings, contacts and
 * ready sentences. Drawn like a schematic: pitch circle, spokes to the hub, flutes on the rim
 * and a callout label for each chamber. Colors come from the keyboard theme.
 */
final class MagazineMenu extends View {

    interface Listener {
        void onPick(int index);
        void onClosed();
    }

    static final class Item {
        final int icon;
        final String label;

        Item(int icon, String label) {
            this.icon = icon;
            this.label = label;
        }
    }

    /** where the chambers settle, in degrees (0 = right, 90 = up) */
    private static final float[] ANGLES = {132f, 180f, 228f};
    /** empty chambers at the edge, cut off by the screen side: the cylinder goes on */
    private static final float[] EMPTY = {84f, 276f};
    private static final float SPIN = 150f;

    private final KeyboardTheme t;
    private final Item[] items;
    private final Drawable[] icons;
    private final float anchorBottom;
    private final Listener listener;
    private final float d;
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final TextPaint text = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    private final Path face = new Path(), cuts = new Path();
    private final RectF[] labelRects;

    private float progress;   // 0 closed .. 1 open
    private ValueAnimator anim;
    private boolean closing, closedNotified;
    private int pressed = -1;

    // layout when open
    private float radius, pitch, chamberR, hubY;

    MagazineMenu(Context c, KeyboardTheme theme, Item[] items, float anchorBottom, Listener l) {
        super(c);
        this.t = theme;
        this.items = items;
        this.anchorBottom = anchorBottom;
        this.listener = l;
        d = c.getResources().getDisplayMetrics().density;
        icons = new Drawable[items.length];
        labelRects = new RectF[items.length];
        for (int i = 0; i < items.length; i++) {
            Drawable ic = c.getResources().getDrawable(items[i].icon, null).mutate();
            ic.setTint(theme.text);
            icons[i] = ic;
            labelRects[i] = new RectF();
        }
        text.setTextAlign(Paint.Align.CENTER);
        text.setTextSize(14 * c.getResources().getDisplayMetrics().scaledDensity);
        setLayerType(LAYER_TYPE_HARDWARE, null);
    }

    void open() {
        animateTo(1f, 460);
    }

    void close() {
        if (closing) return;
        closing = true;
        pressed = -1;
        animateTo(0f, 230);
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
        float bottom = h - 8 * d;
        radius = Math.min(Math.min((bottom - top) / 2f, w * 0.40f), 170 * d);
        hubY = top + radius;
        pitch = radius * 0.60f;
        chamberR = radius * 0.23f;
    }

    // ------------------------------------------------------------------ drawing

    private static float easeOutCubic(float x) {
        float f = 1 - x;
        return 1 - f * f * f;
    }

    private static float easeOutBack(float x) {
        float s = 1.7f, f = x - 1;
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

    @Override
    protected void onDraw(Canvas c) {
        if (radius <= 0) return;
        float p = Math.max(0, Math.min(1, progress));
        float out = easeOutCubic(p);
        float hubX = getWidth() + (1 - out) * (radius + 32 * d);
        float hy = hubY + (1 - out) * 48 * d; // it comes up as it slides in
        float rot = (1 - easeOutBack(p)) * SPIN;
        boolean dark = t.isDark();
        int disc = t.key | 0xFF000000;
        int discIn = mix(disc, dark ? Color.WHITE : Color.BLACK, 0.06f);
        int discOut = mix(disc, dark ? Color.BLACK : Color.WHITE, dark ? 0.25f : 0.0f);
        int line = t.accent;

        // the keys dim behind the menu
        paint.setShader(null);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(alpha(0xFF000000, 0.32f * out));
        c.drawRect(0, 0, getWidth(), getHeight(), paint);

        // the cylinder face, with flutes cut into the rim between the chambers
        face.reset();
        face.addCircle(hubX, hy, radius, Path.Direction.CW);
        cuts.reset();
        float fr = radius * 0.10f;
        for (float a = 108f; a <= 252f; a += 48f) {
            double r = Math.toRadians(a + rot);
            cuts.addCircle(hubX + (float) Math.cos(r) * (radius + fr * 0.45f),
                    hy - (float) Math.sin(r) * (radius + fr * 0.45f), fr, Path.Direction.CW);
        }
        face.op(cuts, Path.Op.DIFFERENCE);
        paint.setColor(Color.BLACK);
        paint.setShader(new RadialGradient(hubX, hy, radius, discIn, discOut, Shader.TileMode.CLAMP));
        if (out > 0.01f) paint.setShadowLayer(14 * d, -3 * d, 4 * d, 0x55000000);
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
        c.drawCircle(hubX, hy, radius + 5 * d, paint);

        // pitch circle through the chambers, dashed like a drawing
        paint.setStrokeWidth(1 * d);
        paint.setColor(alpha(t.hint, 0.7f));
        paint.setPathEffect(new DashPathEffect(new float[]{4 * d, 4 * d}, rot * d * 0.2f));
        c.drawCircle(hubX, hy, pitch, paint);
        paint.setPathEffect(null);

        // empty chambers at the screen edge
        paint.setStrokeWidth(1.4f * d);
        paint.setColor(alpha(t.hint, 0.8f));
        for (float a : EMPTY) {
            double r = Math.toRadians(a + rot);
            c.drawCircle(hubX + (float) Math.cos(r) * pitch, hy - (float) Math.sin(r) * pitch, chamberR, paint);
        }

        // spokes, chambers, icons
        float iconScale = 0.6f + 0.4f * easeOutBack(p);
        for (int i = 0; i < items.length && i < ANGLES.length; i++) {
            double r = Math.toRadians(ANGLES[i] + rot);
            float cx = hubX + (float) Math.cos(r) * pitch, cy = hy - (float) Math.sin(r) * pitch;
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(1 * d);
            paint.setColor(alpha(line, 0.45f));
            c.drawLine(hubX, hy, cx, cy, paint);

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

            Drawable ic = icons[i];
            int s = Math.round(Math.min(chamberR * 1.05f, 30 * d) * iconScale);
            ic.setTint(down ? t.onAccent : t.text);
            ic.setBounds(Math.round(cx - s / 2f), Math.round(cy - s / 2f), Math.round(cx + s / 2f), Math.round(cy + s / 2f));
            ic.draw(c);
        }

        // the hub (axle) on the edge
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(line);
        c.drawCircle(hubX, hy, radius * 0.15f, paint);
        paint.setColor(disc);
        c.drawCircle(hubX, hy, radius * 0.065f, paint);

        // a ring that runs out when the cylinder clicks into place
        if (!closing && p > 0.72f && p < 1f) {
            float k = (p - 0.72f) / 0.28f;
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(2 * d);
            paint.setColor(alpha(line, 0.55f * (1 - k)));
            c.drawCircle(hubX, hy, radius + (5 + 14 * k) * d, paint);
        }

        // callout labels left of the cylinder, joined to their chambers
        float la = Math.max(0, Math.min(1, (p - 0.55f) / 0.45f));
        if (la > 0) drawLabels(c, hubX, hy, rot, la);
    }

    private void drawLabels(Canvas c, float hubX, float hy, float rot, float la) {
        Paint.FontMetrics fm = text.getFontMetrics();
        float th = fm.descent - fm.ascent;
        float shift = (1 - la) * 10 * d;
        for (int i = 0; i < items.length && i < ANGLES.length; i++) {
            double r = Math.toRadians(ANGLES[i] + rot);
            float cx = hubX + (float) Math.cos(r) * pitch, cy = hy - (float) Math.sin(r) * pitch;
            float dy = cy - hy;
            float edge = hubX - (float) Math.sqrt(Math.max(0, radius * radius - dy * dy)); // rim at this height
            float w = text.measureText(items[i].label) + 20 * d, h = th + 12 * d;
            float right = edge - 14 * d + shift;
            RectF box = labelRects[i];
            box.set(right - w, cy - h / 2, right, cy + h / 2);

            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(1 * d);
            paint.setColor(alpha(t.accent, 0.7f * la));
            c.drawLine(right, cy, cx - chamberR, cy, paint); // leader line
            c.drawCircle(cx - chamberR, cy, 1.8f * d, paint);

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
            float cx = getWidth() + (float) Math.cos(r) * pitch, cy = hubY - (float) Math.sin(r) * pitch;
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
                if (pressed < 0) close(); // outside the cylinder: put it away
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
                } else if (pick < 0) {
                    close();
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
