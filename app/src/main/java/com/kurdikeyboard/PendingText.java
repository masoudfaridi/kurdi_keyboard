package com.kurdikeyboard;

/**
 * Text chosen in one of the keyboard's own screens (contacts, ready sentences) that the
 * keyboard types into the original app once it has the focus again.
 */
final class PendingText {

    private static String sText;
    private static long sTime;
    /** number of keyboard screens currently in front (their own fields must not get the text) */
    private static int sScreensShowing;

    private PendingText() {}

    static synchronized void set(String text) {
        sText = text;
        sTime = System.currentTimeMillis();
    }

    /** the text to type, once; null if none or older than 2 minutes */
    static synchronized String take() {
        String t = sText;
        sText = null;
        if (t != null && System.currentTimeMillis() - sTime > 120000) return null;
        return t;
    }

    static synchronized void screenShown() { sScreensShowing++; }

    static synchronized void screenHidden() { sScreensShowing = Math.max(0, sScreensShowing - 1); }

    static synchronized boolean screenShowing() { return sScreensShowing > 0; }
}
