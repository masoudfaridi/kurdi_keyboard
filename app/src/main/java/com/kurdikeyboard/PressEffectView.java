package com.kurdikeyboard;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.SystemClock;
import android.view.View;

/**
 * The view inside the key popup window: draws {@link PressEffect} for the pressed key and keeps
 * the light effects moving while the key is held. On release the popup fades out.
 */
final class PressEffectView extends View {

    static final long FADE = 140;

    KeyboardTheme theme;
    String style = PressEffect.SIMPLE;
    String text;
    boolean alt;
    final RectF bubble = new RectF(), key = new RectF();
    /** the part of the view the effect draws into (the fade-out layer covers only this) */
    final RectF bounds = new RectF();
    float tipX, tipY;
    int color;

    private long start, release;
    private int seed;
    private final Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);

    PressEffectView(Context c) {
        super(c);
    }

    /** a key went down: the animation starts again */
    void begin() {
        start = SystemClock.uptimeMillis();
        release = 0;
        seed = (int) (start * 2654435761L >>> 8);
        invalidate();
    }

    /** the key went up: fade out */
    void release() {
        if (release == 0) {
            release = SystemClock.uptimeMillis();
            invalidate();
        }
    }

    boolean releasing() {
        return release != 0;
    }

    @Override
    protected void onDraw(Canvas c) {
        if (theme == null || text == null) return;
        long now = SystemClock.uptimeMillis();
        float fade = 1;
        if (release != 0) {
            fade = 1 - (now - release) / (float) FADE;
            if (fade <= 0) return;
        }
        int saved = fade < 1 ? c.saveLayerAlpha(Math.max(0, bounds.left), Math.max(0, bounds.top),
                Math.min(getWidth(), bounds.right), Math.min(getHeight(), bounds.bottom), (int) (255 * fade)) : -1;
        PressEffect.draw(c, p, style, theme, bubble, key, tipX, tipY, now - start, seed, text, alt,
                getResources().getDisplayMetrics().density, color);
        if (saved >= 0) c.restoreToCount(saved);
        if (PressEffect.fancy(style) || release != 0) postInvalidateOnAnimation();
    }
}
