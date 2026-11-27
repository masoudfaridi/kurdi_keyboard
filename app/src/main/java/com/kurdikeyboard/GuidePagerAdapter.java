package com.kurdikeyboard;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;

/**
 * The in-app guide (2.x): one page per feature, an icon, a title and short tips.
 * Texts are in res/values/guide.xml. Replaces the 2017 WebView pages.
 */
final class GuidePagerAdapter extends PagerAdapter {

    private static final int[] ICONS = {
            R.drawable.ic_kk_keyboard, R.drawable.ic_kk_language, R.drawable.ic_kk_sparkle,
            R.drawable.ic_kk_send, R.drawable.ic_kk_arrow_right, R.drawable.ic_kk_text_size,
            R.drawable.ic_kk_add, R.drawable.ic_kk_select_all, R.drawable.ic_kk_notes,
            R.drawable.ic_kk_person, R.drawable.ic_kk_emoji, R.drawable.ic_kk_palette,
    };

    private final Context ctx;
    private final String[] titles;
    private final String[] bodies;

    GuidePagerAdapter(Context ctx) {
        this.ctx = ctx;
        titles = ctx.getResources().getStringArray(R.array.guide_titles);
        bodies = ctx.getResources().getStringArray(R.array.guide_bodies);
    }

    @Override public int getCount() { return Math.min(titles.length, bodies.length); }

    @Override public boolean isViewFromObject(View view, Object object) { return view == object; }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        float d = ctx.getResources().getDisplayMetrics().density;
        ScrollView scroll = new ScrollView(ctx);
        scroll.setFillViewport(true);

        LinearLayout page = new LinearLayout(ctx);
        page.setOrientation(LinearLayout.VERTICAL);
        page.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        int pad = (int) (20 * d);
        page.setPadding(pad, (int) (28 * d), pad, pad);

        // icon in a soft circle
        ImageView icon = new ImageView(ctx);
        icon.setImageResource(ICONS[position % ICONS.length]);
        icon.setImageTintList(android.content.res.ColorStateList.valueOf(ctx.getColor(R.color.kk_primary)));
        icon.setBackgroundResource(R.drawable.kk_icon_circle);
        int ip = (int) (18 * d);
        icon.setPadding(ip, ip, ip, ip);
        LinearLayout.LayoutParams ilp = new LinearLayout.LayoutParams((int) (76 * d), (int) (76 * d));
        ilp.gravity = Gravity.CENTER_HORIZONTAL;
        page.addView(icon, ilp);

        TextView step = text(ctx.getString(R.string.guide_step, position + 1, getCount()), 13, R.color.kk_text_secondary);
        step.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams slp = full();
        slp.topMargin = (int) (16 * d);
        page.addView(step, slp);

        TextView title = text(titles[position], 22, R.color.kk_text);
        title.setGravity(Gravity.CENTER);
        title.setTypeface(null, android.graphics.Typeface.BOLD);
        LinearLayout.LayoutParams tlp = full();
        tlp.topMargin = (int) (4 * d);
        page.addView(title, tlp);

        // tips in a card
        LinearLayout card = new LinearLayout(ctx);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setBackgroundResource(R.drawable.card_background);
        int cp = (int) (16 * d);
        card.setPadding(cp, (int) (8 * d), cp, (int) (8 * d));
        for (String line : bodies[position].split("\n")) {
            line = line.trim();
            if (line.isEmpty()) continue;
            card.addView(tip(line, d), full());
        }
        LinearLayout.LayoutParams clp = full();
        clp.topMargin = (int) (20 * d);
        page.addView(card, clp);

        scroll.addView(page);
        container.addView(scroll);
        return scroll;
    }

    /** one tip: a small blue dot and the text */
    private View tip(String line, float d) {
        LinearLayout row = new LinearLayout(ctx);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setPadding(0, (int) (10 * d), 0, (int) (10 * d));
        View dot = new View(ctx);
        GradientDrawable g = new GradientDrawable();
        g.setShape(GradientDrawable.OVAL);
        g.setColor(ctx.getColor(R.color.kk_primary));
        dot.setBackground(g);
        LinearLayout.LayoutParams dlp = new LinearLayout.LayoutParams((int) (7 * d), (int) (7 * d));
        dlp.topMargin = (int) (10 * d);
        dlp.setMarginEnd((int) (12 * d));
        row.addView(dot, dlp);
        TextView tv = text(line, 16, R.color.kk_text);
        tv.setLineSpacing(0, 1.35f);
        row.addView(tv, new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
        return row;
    }

    private TextView text(String s, int sp, int color) {
        TextView tv = new TextView(ctx);
        tv.setText(s);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, sp);
        tv.setTextColor(ctx.getColor(color));
        tv.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
        return tv;
    }

    private static LinearLayout.LayoutParams full() {
        return new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
