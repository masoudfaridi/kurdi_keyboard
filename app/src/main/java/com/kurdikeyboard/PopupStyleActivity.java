package com.kurdikeyboard;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.Switch;
import android.widget.TextView;

/**
 * "Popup theme": how the pressed letter is shown above the finger. Every style plays a live
 * preview in the colors of the current keyboard theme; a text field at the bottom lets the
 * user try the chosen style on the real keyboard.
 */
public class PopupStyleActivity extends Activity {

    private SharedPreferences prefs;
    private String selected;
    private BaseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        setContentView(R.layout.activity_popup_style);
        setTitle(R.string.popup_style_title);
        prefs = getSharedPreferences("MySharedPreference", Context.MODE_PRIVATE);
        selected = prefs.getString(getString(R.string.prefpopupstyle), PressEffect.AUTO);
        final KeyboardTheme theme = KeyboardTheme.forId(prefs.getString(getString(R.string.pref_list_theme_selector),
                KeyboardTheme.DEFAULT_ID));
        final String[] names = getResources().getStringArray(R.array.popup_style_names);
        final String[] descs = getResources().getStringArray(R.array.popup_style_descs);

        Switch sw = (Switch) findViewById(R.id.previewSwitch);
        sw.setChecked(prefs.getBoolean(getString(R.string.prefnotshowpreview), true));
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override public void onCheckedChanged(CompoundButton b, boolean on) {
                prefs.edit().putBoolean(getString(R.string.prefnotshowpreview), on).apply();
            }
        });

        GridView grid = (GridView) findViewById(R.id.styleGrid);
        adapter = new BaseAdapter() {
            @Override public int getCount() { return PressEffect.ALL.length; }
            @Override public Object getItem(int i) { return PressEffect.ALL[i]; }
            @Override public long getItemId(int i) { return i; }

            @Override
            public View getView(int i, View convert, ViewGroup parent) {
                View v = convert != null ? convert
                        : getLayoutInflater().inflate(R.layout.item_popup_style, parent, false);
                final String style = PressEffect.ALL[i];
                ((StylePreview) v.findViewById(R.id.stylePreview)).set(theme, style);
                boolean on = style.equals(selected);
                TextView name = (TextView) v.findViewById(R.id.styleName);
                name.setText(on ? "✔  " + names[i] : names[i]);
                name.setTextColor(getColor(on ? R.color.kk_primary : R.color.kk_text));
                ((TextView) v.findViewById(R.id.styleDesc)).setText(descs[i]);
                v.setSelected(on);
                // click on the card itself: GridView mixes up positions in right-to-left layouts
                v.setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View view) {
                        selected = style;
                        prefs.edit().putString(getString(R.string.prefpopupstyle), style).apply();
                        adapter.notifyDataSetChanged();
                    }
                });
                return v;
            }
        };
        grid.setAdapter(adapter);
    }

    /** A few keys in the theme's colors; the middle one is pressed again and again. */
    public static class StylePreview extends View {
        private static final long CYCLE = 1700, HOLD = 950;
        private static final String[] LABELS = {"پ", "ب", "ا"};
        private KeyboardTheme theme;
        private String style = PressEffect.SIMPLE;
        private final long t0 = SystemClock.uptimeMillis();
        private final Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        private final RectF r = new RectF(), bubble = new RectF(), key = new RectF();
        private final Path clip = new Path();

        public StylePreview(Context c) { super(c); }
        public StylePreview(Context c, AttributeSet a) { super(c, a); }

        void set(KeyboardTheme t, String s) {
            theme = t;
            style = s;
            invalidate();
        }

        @Override
        protected void onMeasure(int w, int h) {
            int width = MeasureSpec.getSize(w);
            setMeasuredDimension(width, (int) (width * 0.95f));
        }

        @Override
        protected void onDraw(Canvas c) {
            if (theme == null) return;
            float d = getResources().getDisplayMetrics().density, W = getWidth(), H = getHeight();
            r.set(0, 0, W, H);
            clip.reset();
            clip.addRoundRect(r, 12 * d, 12 * d, Path.Direction.CW);
            c.save();
            c.clipPath(clip);
            if (theme.pro != null) {
                ProPainter.paintBackground(c, theme, W, H, d * 0.6f);
            } else {
                c.drawColor(theme.bg);
            }
            String resolved = PressEffect.resolve(style, theme);
            long now = SystemClock.uptimeMillis(), age = now - t0, ph = age % CYCLE;
            boolean down = ph < HOLD;

            float pad = 7 * d, gap = 5 * d, kh = H * 0.25f, kw = (W - 2 * pad - 2 * gap) / 3f, ky = H - pad - kh;
            for (int i = 0; i < 3; i++) {
                float kx = pad + i * (kw + gap);
                r.set(kx, ky, kx + kw, ky + kh);
                boolean pressed = i == 1 && down;
                if (i == 1) key.set(r);
                float shift = 0;
                if (theme.pro != null) {
                    ProPainter.key(c, p, r, theme.radiusDp * d * 0.8f, theme, ProPainter.LETTER, pressed, 0, d * 0.8f, 255);
                    shift = ProPainter.labelShift(theme, pressed, d * 0.8f, kh);
                    p.clearShadowLayer();
                } else {
                    p.setStyle(Paint.Style.FILL);
                    p.setColor(pressed ? theme.keyPressed : theme.key);
                    float rad = theme.radiusDp * d * 0.8f;
                    c.drawRoundRect(r, rad, rad, p);
                    if (theme.stroke != 0) {
                        p.setStyle(Paint.Style.STROKE);
                        p.setStrokeWidth(d);
                        p.setColor(theme.stroke);
                        c.drawRoundRect(r, rad, rad, p);
                        p.setStyle(Paint.Style.FILL);
                    }
                }
                p.setShader(null);
                p.setStyle(Paint.Style.FILL);
                p.setColor(theme.text);
                p.setTextAlign(Paint.Align.CENTER);
                p.setTextSize(kh * 0.45f);
                c.drawText(LABELS[i], r.centerX(), r.centerY() + kh * 0.15f + shift, p);
            }

            // the popup over the middle key, fading out after it is let go
            long fadeEnd = HOLD + PressEffectView.FADE;
            if (ph < fadeEnd) {
                boolean fancy = PressEffect.fancy(resolved);
                float bw = kw * (fancy ? 1.3f : 1.15f), bh = kh * (fancy ? 1.3f : 1.4f);
                float lift = fancy ? PressEffect.minGapDp(resolved) * d * 0.55f : 8 * d;
                float bx = key.centerX() - bw / 2, by = Math.max(5 * d, key.top - bh - lift);
                bubble.set(bx, by, bx + bw, by + bh);
                int alpha = ph <= HOLD ? 255 : (int) (255 * (1 - (ph - HOLD) / (float) PressEffectView.FADE));
                int saved = alpha < 255 ? c.saveLayerAlpha(0, 0, W, H, alpha) : -1;
                PressEffect.draw(c, p, resolved, theme, bubble, key, key.centerX(), key.top + kh * 0.38f, ph,
                        (int) (age / CYCLE) * 7919, LABELS[1], false, d * 0.85f, PressEffect.color(theme, 0.5f, 0.8f));
                if (saved >= 0) c.restoreToCount(saved);
            }
            c.restore();
            postInvalidateOnAnimation();
        }
    }
}
