package com.kurdikeyboard;

/**
 * Text chosen in one of the keyboard's own screens (contacts, ready sentences) that the
 * keyboard types into the original app once it has the focus again.
 */
final class PendingText {

    private static String sText;
    private static long sTime;
    /** app the screen was opened from; the text is only typed there */
    private static String sTargetPackage;
    /** number of keyboard screens currently in front (their own fields must not get the text) */
    private static int sScreensShowing;

    private PendingText() {}

    static synchronized void set(String text) {
        sText = text;
        sTime = System.currentTimeMillis();
    }

    static synchronized void openedFrom(String packageName) {
        sTargetPackage = packageName;
        sText = null;
    }

    /** the text to type, once, if this is the app it was meant for and it is recent */
    static synchronized String take(String packageName) {
        String t = sText;
        if (t == null) return null;
        sText = null; // used or dropped: never typed into a later, different field
        if (System.currentTimeMillis() - sTime > 60000) return null;
        if (sTargetPackage != null && !sTargetPackage.equals(packageName)) return null;
        return t;
    }

    static synchronized void screenShown() { sScreensShowing++; }

    static synchronized void screenHidden() { sScreensShowing = Math.max(0, sScreensShowing - 1); }

    static synchronized boolean screenShowing() { return sScreensShowing > 0; }
}
