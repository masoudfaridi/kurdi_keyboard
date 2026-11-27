package com.kurdikeyboard;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/** Theme list with a live preview of each theme (all drawn in code, no images). */
public class ThemePickerActivity extends Activity {

    private List<KeyboardTheme> themes;
    private String selected;
    private SharedPreferences prefs;
    private BaseAdapter mAdapter;

    private void pick(String id) {
        selected = id;
        // the keyboard notices the new value the next time it opens and rebuilds itself
        prefs.edit().putString(getString(R.string.pref_list_theme_selector), selected).apply();
        if (mAdapter != null) mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        setContentView(R.layout.activity_theme_picker);
        setTitle(R.string.theme_picker_title);
        prefs = getSharedPreferences("MySharedPreference", Context.MODE_PRIVATE);
        KeyboardTheme current = KeyboardTheme.forId(prefs.getString(getString(R.string.pref_list_theme_selector),
                KeyboardTheme.DEFAULT_ID));
        selected = current.id;
        // two sections, light and dark; open on the one that holds the current theme
        themes = new java.util.ArrayList<>(KeyboardTheme.section(current.isDark()));
        tabLight = (TextView) findViewById(R.id.tabLight);
        tabDark = (TextView) findViewById(R.id.tabDark);
        tabLight.setText(getString(R.string.theme_tab_light, faDigits(KeyboardTheme.section(false).size())));
        tabDark.setText(getString(R.string.theme_tab_dark, faDigits(KeyboardTheme.section(true).size())));
        tabLight.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { showSection(false); }
        });
        tabDark.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { showSection(true); }
        });
        markTabs(current.isDark());

        final GridView grid = (GridView) findViewById(R.id.themeGrid);
        final BaseAdapter adapter = new BaseAdapter() {
            @Override public int getCount() { return themes.size(); }
            @Override public Object getItem(int i) { return themes.get(i); }
            @Override public long getItemId(int i) { return i; }

            @Override
            public View getView(int i, View convert, ViewGroup parent) {
                View v = convert != null ? convert
                        : getLayoutInflater().inflate(R.layout.item_theme, parent, false);
                KeyboardTheme t = themes.get(i);
                ((ThemePreviewView) v.findViewById(R.id.themePreview)).setTheme(t);
                TextView name = (TextView) v.findViewById(R.id.themeName);
                boolean on = t.id.equals(selected);
                name.setText(on ? "✔  " + t.name : t.name);
                name.setTextColor(getColor(on ? R.color.kk_primary : R.color.kk_text));
                v.setSelected(on);
                final String themeId = t.id;
                // click on the card itself: GridView mixes up positions in right-to-left layouts
                v.setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View view) {
                        pick(themeId);
                    }
                });
                return v;
            }
        };
        grid.setAdapter(adapter);
        mAdapter = adapter;
        mGrid = grid;
    }

    private static String faDigits(int n) {
        StringBuilder b = new StringBuilder();
        for (char ch : String.valueOf(n).toCharArray()) b.append((char) (0x06F0 + ch - '0'));
        return b.toString();
    }

    private TextView tabLight, tabDark;
    private GridView mGrid;

    private void showSection(boolean dark) {
        themes.clear();
        themes.addAll(KeyboardTheme.section(dark));
        markTabs(dark);
        if (mAdapter != null) mAdapter.notifyDataSetChanged();
        if (mGrid != null) mGrid.setSelection(0);
    }

    private void markTabs(boolean dark) {
        tabLight.setSelected(!dark);
        tabDark.setSelected(dark);
        tabLight.setTextColor(getColor(dark ? R.color.kk_text_secondary : R.color.kk_primary));
        tabDark.setTextColor(getColor(dark ? R.color.kk_primary : R.color.kk_text_secondary));
        tabLight.setTypeface(null, dark ? android.graphics.Typeface.NORMAL : android.graphics.Typeface.BOLD);
        tabDark.setTypeface(null, dark ? android.graphics.Typeface.BOLD : android.graphics.Typeface.NORMAL);
    }

    /** A small keyboard drawn with a theme's palette. */
    public static class ThemePreviewView extends View {
        private KeyboardTheme theme;
        private final Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        private final RectF r = new RectF();

        public ThemePreviewView(Context c) { super(c); }
        public ThemePreviewView(Context c, AttributeSet a) { super(c, a); }

        void setTheme(KeyboardTheme t) {
            theme = t;
            invalidate();
        }

        @Override
        protected void onMeasure(int w, int h) {
            int width = MeasureSpec.getSize(w);
            setMeasuredDimension(width, (int) (width * 0.62f));
        }

        @Override
        protected void onDraw(Canvas c) {
            if (theme == null) return;
            float d = getResources().getDisplayMetrics().density;
            float W = getWidth(), H = getHeight();
            p.setStyle(Paint.Style.FILL);
            p.setColor(theme.bg);
            r.set(0, 0, W, H);
            c.drawRoundRect(r, 12 * d, 12 * d, p);

            float pad = 6 * d, gap = 2.5f * d;
            float rowH = (H - 2 * pad) / 4f;
            int[][] rows = {{10, 0}, {9, 0}, {7, 1}, {0, 2}};
            String letters = "قوەرتیئحجچ";
            for (int row = 0; row < 4; row++) {
                float y = pad + row * rowH;
                if (row < 3) {
                    int n = rows[row][0];
                    boolean fnEnds = rows[row][1] == 1;
                    float slots = fnEnds ? n + 3 : n;
                    float kw = (W - 2 * pad) / (fnEnds ? 10 : 10f);
                    float start = pad + (W - 2 * pad - (fnEnds ? kw * 10 : kw * n)) / 2f;
                    if (fnEnds) {
                        drawKey(c, start, y, kw * 1.5f, rowH, theme.fn, gap, d, null);
                        for (int k = 0; k < n; k++) {
                            drawKey(c, start + kw * 1.5f + k * kw, y, kw, rowH, theme.key, gap, d, null);
                        }
                        drawKey(c, start + kw * 8.5f, y, kw * 1.5f, rowH, theme.fn, gap, d, null);
                    } else {
                        for (int k = 0; k < n; k++) {
                            String l = row == 0 ? String.valueOf(letters.charAt(k % letters.length())) : null;
                            drawKey(c, start + k * kw, y, kw, rowH, theme.key, gap, d, l);
                        }
                    }
                } else {
                    float kw = (W - 2 * pad) / 10f;
                    drawKey(c, pad, y, kw * 1.5f, rowH, theme.fn, gap, d, null);
                    drawKey(c, pad + kw * 1.5f, y, kw * 6.5f, rowH, theme.key, gap, d, null);
                    drawKey(c, pad + kw * 8f, y, kw * 2f, rowH, theme.accent, gap, d, null);
                }
            }
        }

        private void drawKey(Canvas c, float x, float y, float w, float h, int color, float gap, float d, String label) {
            r.set(x + gap, y + gap, x + w - gap, y + h - gap);
            float rad = Math.min(theme.radiusDp * d * 0.6f, h / 2);
            if (!theme.borderless || color == theme.accent) {
                p.setStyle(Paint.Style.FILL);
                p.setColor(color);
                c.drawRoundRect(r, rad, rad, p);
                if (theme.stroke != 0) {
                    p.setStyle(Paint.Style.STROKE);
                    p.setStrokeWidth(d * 0.8f);
                    p.setColor(theme.stroke);
                    c.drawRoundRect(r, rad, rad, p);
                }
            }
            if (label != null) {
                p.setStyle(Paint.Style.FILL);
                p.setColor(theme.text);
                p.setTextAlign(Paint.Align.CENTER);
                p.setTextSize(h * 0.5f);
                c.drawText(label, r.centerX(), r.centerY() + p.getTextSize() / 3, p);
            }
        }
    }
}
