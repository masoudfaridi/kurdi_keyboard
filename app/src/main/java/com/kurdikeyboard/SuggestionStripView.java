package com.kurdikeyboard;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * The row of word suggestions above the keys. It sits on top of the tools row and uses
 * the same background, so every theme gets a matching strip.
 */
public class SuggestionStripView extends LinearLayout {

    public interface Listener {
        void onSuggestionPicked(int index, String word);
        void onSuggestionLongPressed(int index, String word);
        /** the tools button at the edge of the strip: show the tools row instead */
        void onShowToolsRequested();
    }

    /** number of suggestions shown: a setting, 3 to 6 (default 4) */
    public static final int MIN_SLOTS = 3, MAX_SLOTS = 6, DEFAULT_SLOTS = 4;
    private TextView[] slots = new TextView[0];
    private View[] dividers = new View[0];
    /** slotForWord[k] = slot that shows the k-th best word */
    private int[] slotForWord = new int[0];
    private final TextView toolsButton;
    private final List<String> words = new ArrayList<>();
    private Listener listener;
    private int textColor = Color.WHITE;
    /** index of the entry shown as "typed literally" (drawn in a lighter style), or -1 */
    private int literalIndex = -1;

    public SuggestionStripView(Context context) {
        super(context);
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);
        setClickable(true); // swallow touches so the tools row below never gets them
        toolsButton = new TextView(context);
        toolsButton.setText("☰"); // ☰
        toolsButton.setGravity(Gravity.CENTER);
        toolsButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        toolsButton.setContentDescription("tools");
        toolsButton.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {
                if (listener != null) listener.onShowToolsRequested();
            }
        });
        addView(toolsButton, new LayoutParams(dp(44), LayoutParams.MATCH_PARENT));
        setSlotCount(DEFAULT_SLOTS);
        applyColors();
    }

    public void setListener(Listener l) { listener = l; }

    /**
     * Sets how many suggestions the strip shows (clamped to 3..6). The best one goes in the
     * middle, the next ones alternate outwards (the 2nd on the side where the user's text
     * starts: right for RTL languages).
     */
    public void setSlotCount(int n) {
        n = Math.max(MIN_SLOTS, Math.min(MAX_SLOTS, n));
        if (n == slots.length) return;
        for (TextView tv : slots) removeView(tv);
        for (View d : dividers) removeView(d);
        slots = new TextView[n];
        dividers = new View[n - 1];
        float sp = n <= 4 ? 17 : n == 5 ? 15.5f : 14.5f;
        for (int i = 0; i < n; i++) {
            if (i > 0) {
                View d = new View(getContext());
                LayoutParams lp = new LayoutParams(dp(1), LayoutParams.MATCH_PARENT);
                lp.topMargin = lp.bottomMargin = dp(9);
                addView(d, lp);
                dividers[i - 1] = d;
            }
            final TextView tv = new TextView(getContext());
            tv.setGravity(Gravity.CENTER);
            tv.setSingleLine(true);
            tv.setEllipsize(TextUtils.TruncateAt.END);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, sp);
            tv.setPadding(dp(n <= 4 ? 4 : 2), 0, dp(n <= 4 ? 4 : 2), 0);
            final int index = i;
            tv.setOnClickListener(new OnClickListener() {
                @Override public void onClick(View v) {
                    int wi = wordIndexForSlot(index);
                    if (listener != null && wi >= 0 && wi < words.size()) {
                        listener.onSuggestionPicked(wi, words.get(wi));
                    }
                }
            });
            tv.setOnLongClickListener(new OnLongClickListener() {
                @Override public boolean onLongClick(View v) {
                    int wi = wordIndexForSlot(index);
                    if (listener != null && wi >= 0 && wi < words.size()) {
                        listener.onSuggestionLongPressed(wi, words.get(wi));
                        return true;
                    }
                    return false;
                }
            });
            addView(tv, new LayoutParams(0, LayoutParams.MATCH_PARENT, 1f));
            slots[i] = tv;
        }
        // slots by distance from the centre; on a tie the start side (right in RTL) first
        final double centre = (n - 1) / 2.0;
        Integer[] order = new Integer[n];
        for (int i = 0; i < n; i++) order[i] = i;
        java.util.Arrays.sort(order, new java.util.Comparator<Integer>() {
            @Override public int compare(Integer a, Integer b) {
                int c = Double.compare(Math.abs(a - centre), Math.abs(b - centre));
                return c != 0 ? c : Integer.compare(a, b);
            }
        });
        slotForWord = new int[n];
        for (int k = 0; k < n; k++) slotForWord[k] = order[k];
        applyColors();
        setSuggestions(new ArrayList<>(words), literalIndex);
    }

    public int getSlotCount() { return slots.length; }

    private int wordIndexForSlot(int slot) {
        // slots are laid out start → end, so slot 0 is at the right in RTL
        for (int k = 0; k < slotForWord.length; k++) if (slotForWord[k] == slot) return k;
        return -1;
    }

    public void setRtl(boolean rtl) {
        setLayoutDirection(rtl ? LAYOUT_DIRECTION_RTL : LAYOUT_DIRECTION_LTR);
    }

    /** @param literalIndex index in {@code list} of the "keep what I typed" entry, or -1 */
    public void setSuggestions(List<String> list, int literalIndex) {
        words.clear();
        if (list != null) words.addAll(list.subList(0, Math.min(slots.length, list.size())));
        this.literalIndex = literalIndex;
        for (int s = 0; s < slots.length; s++) {
            int wi = wordIndexForSlot(s);
            TextView tv = slots[s];
            if (wi < words.size()) {
                String w = words.get(wi);
                tv.setText(wi == literalIndex ? "“" + w + "”" : w);
                tv.setTypeface(null, wi == 0 && wi != literalIndex ? Typeface.BOLD : Typeface.NORMAL);
                tv.setAlpha(wi == literalIndex ? 0.75f : 1f);
            } else {
                tv.setText("");
            }
        }
        for (int i = 0; i < dividers.length; i++) {
            dividers[i].setVisibility(words.size() > 1 ? VISIBLE : INVISIBLE);
        }
    }

    public boolean isEmpty() { return words.isEmpty(); }

    public void clear() { setSuggestions(null, -1); }

    /**
     * Copies the background of the tools row and picks black or white text, whichever
     * reads better on it.
     */
    public void matchBackground(Drawable bg) {
        Drawable copy = null;
        if (bg != null && bg.getConstantState() != null) {
            copy = bg.getConstantState().newDrawable(getResources()).mutate();
        }
        if (copy == null) copy = new ColorDrawable(Color.parseColor("#263238"));
        setBackground(copy);
        textColor = luminance(copy) > 0.55 ? Color.parseColor("#212121") : Color.WHITE;
        applyColors();
    }

    private void applyColors() {
        for (TextView tv : slots) tv.setTextColor(textColor);
        toolsButton.setTextColor(textColor);
        toolsButton.setAlpha(0.8f);
        for (View d : dividers) d.setBackgroundColor((textColor & 0x00FFFFFF) | 0x55000000);
    }

    /** average brightness (0..1) of a drawable; transparent parts count as dark */
    public static double luminance(Drawable d) {
        try {
            Bitmap b = Bitmap.createBitmap(16, 4, Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(b);
            c.drawColor(Color.BLACK); // transparent parts count as dark
            d.setBounds(0, 0, 16, 4);
            d.draw(c);
            double sum = 0;
            for (int x = 0; x < 16; x++) {
                for (int y = 0; y < 4; y++) {
                    int p = b.getPixel(x, y);
                    sum += (0.299 * Color.red(p) + 0.587 * Color.green(p) + 0.114 * Color.blue(p)) / 255.0;
                }
            }
            b.recycle();
            return sum / 64;
        } catch (Throwable t) {
            return 0;
        }
    }

    private int dp(int v) {
        return (int) (v * getResources().getDisplayMetrics().density + 0.5f);
    }
}
