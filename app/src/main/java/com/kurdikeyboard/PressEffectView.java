package com.kurdikeyboard;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.SystemClock;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * The view inside every keyboard popup window: the letter of a pressed key, or a popup with
 * several options (long-press menu, language list), all drawn by {@link PressEffect} in the
 * chosen popup style. Light effects keep moving while the finger is down; on release the popup
 * fades out.
 */
final class PressEffectView extends View {

    static final long FADE = 140;

    KeyboardTheme theme;
    String style = PressEffect.SIMPLE;
    int color;
    float tipX, tipY;

    // the letter popup
    String text;
    boolean alt;
    final RectF bubble = new RectF(), key = new RectF();

    // a popup with several options
    boolean menu;
    final RectF panel = new RectF();
    final List<PressEffect.Item> items = new ArrayList<>();
    private int selected = -1;
    private long selTime;

    /** the part of the view the effect draws into (the fade-out layer covers only this) */
    final RectF bounds = new RectF();

    private long start, release;
    private int seed;
    private final Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);

    PressEffectView(Context c) {
        super(c);
    }

    /** a key went down or a popup opened: the animation starts again */
    void begin() {
        start = SystemClock.uptimeMillis();
        selTime = start;
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

    /** a popup with several options: the option under the finger (-1 none) */
    void choose(int index) {
        if (index != selected) {
            selected = index;
            selTime = SystemClock.uptimeMillis();
            invalidate();
        }
    }

    /** the option chosen when the popup opens (the current language), without an animation */
    void preselect(int index) {
        selected = index;
    }

    /** the finger moved (the light follows it) */
    void moveTip(float x, float y) {
        tipX = x;
        tipY = y;
        if (!PressEffect.fancy(style)) return;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas c) {
        if (theme == null || (!menu && text == null)) return;
        long now = SystemClock.uptimeMillis();
        float fade = 1;
        if (release != 0) {
            fade = 1 - (now - release) / (float) FADE;
            if (fade <= 0) return;
        }
        int saved = fade < 1 ? c.saveLayerAlpha(Math.max(0, bounds.left), Math.max(0, bounds.top),
                Math.min(getWidth(), bounds.right), Math.min(getHeight(), bounds.bottom), (int) (255 * fade)) : -1;
        float d = getResources().getDisplayMetrics().density;
        if (menu) {
            PressEffect.menu(c, p, style, theme, panel, items, selected, now - selTime, tipX, tipY, now - start,
                    seed, d, color);
        } else {
            PressEffect.draw(c, p, style, theme, bubble, key, tipX, tipY, now - start, seed, text, alt, d, color);
        }
        if (saved >= 0) c.restoreToCount(saved);
        if (PressEffect.fancy(style) || release != 0) postInvalidateOnAnimation();
    }
}
