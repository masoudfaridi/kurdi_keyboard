package com.kurdikeyboard;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Emoji panel (2.0): modern Unicode emoji drawn by the phone's own emoji font, grouped in
 * tabs, with recents. Replaces the 2017 emojicon library and its old bitmap emoji.
 * Colors follow the keyboard theme.
 */
final class EmojiPanel extends LinearLayout {

    interface Listener {
        void onEmoji(String emoji);
        void onBackspace();
        void onSpace();
        void onClose();
    }

    private static final String[] TABS = {"🕘", "😀", "👋", "🐻",
            "🍔", "⚽", "🚗", "💡", "❤️", "🏳️"};
    private static final int RECENTS = 0;
    private static final int MAX_RECENTS = 40;
    private static final String PREF_RECENTS = "emoji_recents_v2";

    private final Listener listener;
    private final KeyboardTheme theme;
    private final SharedPreferences prefs;
    private final List<List<String>> groups = new ArrayList<>();
    private final List<String> shown = new ArrayList<>();
    private final TextView[] tabViews = new TextView[TABS.length];
    private final BaseAdapter adapter;
    private int tab = 1;

    EmojiPanel(Context ctx, KeyboardTheme theme, Listener listener) {
        super(ctx);
        this.listener = listener;
        this.theme = theme;
        this.prefs = ctx.getSharedPreferences("MySharedPreference", Context.MODE_PRIVATE);
        setOrientation(VERTICAL);
        setBackgroundColor(theme.bg);
        setLayoutDirection(LAYOUT_DIRECTION_LTR);

        Paint probe = new Paint();
        for (String g : EmojiData.GROUPS) {
            List<String> list = new ArrayList<>();
            for (String e : g.split(" ")) {
                if (e.isEmpty()) continue;
                // hide emoji the phone's font cannot draw (older Android versions)
                if (Build.VERSION.SDK_INT >= 23 && !probe.hasGlyph(e)) continue;
                list.add(e);
            }
            groups.add(list);
        }

        // tabs
        LinearLayout tabs = new LinearLayout(ctx);
        tabs.setOrientation(HORIZONTAL);
        for (int i = 0; i < TABS.length; i++) {
            final int index = i;
            TextView tv = new TextView(ctx);
            tv.setText(TABS[i]);
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            tv.setOnClickListener(new OnClickListener() {
                @Override public void onClick(View v) { selectTab(index); }
            });
            tabs.addView(tv, new LayoutParams(0, dp(46), 1f));
            tabViews[i] = tv;
        }
        addView(tabs, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        // grid
        final GridView grid = new GridView(ctx);
        grid.setColumnWidth(dp(46));
        grid.setNumColumns(GridView.AUTO_FIT);
        grid.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
        grid.setVerticalSpacing(0);
        grid.setSelector(R.drawable.kk_icon_button);
        grid.setPadding(dp(4), 0, dp(4), 0);
        grid.setClipToPadding(false);
        adapter = new BaseAdapter() {
            @Override public int getCount() { return shown.size(); }
            @Override public Object getItem(int i) { return shown.get(i); }
            @Override public long getItemId(int i) { return i; }

            @Override
            public View getView(int i, View convert, ViewGroup parent) {
                TextView tv = convert instanceof TextView ? (TextView) convert : new TextView(getContext());
                tv.setText(shown.get(i));
                tv.setGravity(Gravity.CENTER);
                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 27);
                tv.setLayoutParams(new GridView.LayoutParams(GridView.LayoutParams.MATCH_PARENT, dp(50)));
                return tv;
            }
        };
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(android.widget.AdapterView<?> p, View v, int pos, long id) {
                String e = shown.get(pos);
                addRecent(e);
                EmojiPanel.this.listener.onEmoji(e);
            }
        });
        addView(grid, new LayoutParams(LayoutParams.MATCH_PARENT, 0, 1f));

        // bottom bar: ABC / space / backspace
        LinearLayout bar = new LinearLayout(ctx);
        bar.setOrientation(HORIZONTAL);
        bar.setGravity(Gravity.CENTER_VERTICAL);
        bar.setPadding(dp(4), dp(4), dp(4), dp(4));
        TextView abc = barKey(ctx, "ABC", theme.fn);
        abc.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) { EmojiPanel.this.listener.onClose(); }
        });
        bar.addView(abc, new LayoutParams(0, dp(44), 1.4f));
        TextView space = barKey(ctx, "", theme.key);
        space.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) { EmojiPanel.this.listener.onSpace(); }
        });
        bar.addView(space, new LayoutParams(0, dp(44), 4f));
        ImageView del = new ImageView(ctx);
        del.setImageResource(R.drawable.ic_kk_backspace);
        del.setImageTintList(android.content.res.ColorStateList.valueOf(theme.text));
        del.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        del.setPadding(dp(10), dp(10), dp(10), dp(10));
        del.setBackground(keyShape(theme.fn));
        installRepeat(del);
        bar.addView(del, new LayoutParams(0, dp(44), 1.4f));
        addView(bar, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        selectTab(loadRecents().isEmpty() ? 1 : RECENTS);
    }

    private TextView barKey(Context ctx, String text, int color) {
        TextView tv = new TextView(ctx);
        tv.setText(text);
        tv.setGravity(Gravity.CENTER);
        tv.setTextColor(theme.text);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        tv.setBackground(keyShape(color));
        return tv;
    }

    private android.graphics.drawable.Drawable keyShape(int color) {
        android.graphics.drawable.GradientDrawable g = new android.graphics.drawable.GradientDrawable();
        g.setColor(color);
        g.setCornerRadius(theme.radiusDp * getResources().getDisplayMetrics().density);
        android.graphics.drawable.InsetDrawable inset = new android.graphics.drawable.InsetDrawable(g, dp(3));
        return inset;
    }

    /** backspace repeats while held */
    private void installRepeat(View v) {
        final Handler h = new Handler(Looper.getMainLooper());
        final Runnable repeat = new Runnable() {
            @Override public void run() {
                listener.onBackspace();
                h.postDelayed(this, 60);
            }
        };
        v.setOnTouchListener(new OnTouchListener() {
            @Override public boolean onTouch(View view, MotionEvent e) {
                switch (e.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        listener.onBackspace();
                        h.postDelayed(repeat, 400);
                        view.setPressed(true);
                        return true;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        h.removeCallbacks(repeat);
                        view.setPressed(false);
                        return true;
                }
                return true;
            }
        });
    }

    private void selectTab(int index) {
        tab = index;
        shown.clear();
        shown.addAll(index == RECENTS ? loadRecents() : groups.get(index - 1));
        for (int i = 0; i < tabViews.length; i++) {
            tabViews[i].setAlpha(i == index ? 1f : 0.45f);
            if (i == index) {
                android.graphics.drawable.GradientDrawable underline = new android.graphics.drawable.GradientDrawable();
                underline.setColor(theme.accent);
                android.graphics.drawable.LayerDrawable ld = new android.graphics.drawable.LayerDrawable(
                        new android.graphics.drawable.Drawable[]{underline});
                ld.setLayerInset(0, dp(10), dp(43), dp(10), 0);
                tabViews[i].setBackground(ld);
            } else {
                tabViews[i].setBackground(null);
            }
        }
        adapter.notifyDataSetChanged();
    }

    private List<String> loadRecents() {
        List<String> out = new ArrayList<>();
        String s = prefs.getString(PREF_RECENTS, "");
        for (String e : s.split(" ")) if (!e.isEmpty()) out.add(e);
        return out;
    }

    private void addRecent(String e) {
        List<String> r = loadRecents();
        r.remove(e);
        r.add(0, e);
        while (r.size() > MAX_RECENTS) r.remove(r.size() - 1);
        StringBuilder sb = new StringBuilder();
        for (String x : r) {
            if (sb.length() > 0) sb.append(' ');
            sb.append(x);
        }
        prefs.edit().putString(PREF_RECENTS, sb.toString()).apply();
    }

    private int dp(int v) {
        return (int) (v * getResources().getDisplayMetrics().density + 0.5f);
    }
}
