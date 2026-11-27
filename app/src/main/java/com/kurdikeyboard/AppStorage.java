package com.kurdikeyboard;

import android.content.Context;

import java.io.File;

/**
 * Where the keyboard keeps its files (clipboard history, ready sentences).
 *
 * The 2017 version wrote to "/sdcard/data/data/com.kurdikeyboard/...". Since Android 10
 * (scoped storage) apps cannot create such folders, so clipboard history and ready
 * sentences silently stopped working. The same folder layout now lives in the app's
 * private storage, which needs no permission.
 */
public final class AppStorage {

    private AppStorage() {}

    /** Drop-in replacement for Environment.getExternalStorageDirectory() in this app. */
    public static File root(Context ctx) {
        File root = new File(ctx.getApplicationContext().getFilesDir(), "storage");
        File base = new File(root, "data/data/com.kurdikeyboard");
        if (!base.exists()) base.mkdirs();
        return root;
    }

    /**
     * Files of a folder, oldest first (listFiles() has no defined order, so the clipboard
     * history used to appear shuffled) and never null (it crashed when the folder was missing).
     */
    public static File[] sortedFiles(File folder) {
        File[] files = folder == null ? null : folder.listFiles();
        if (files == null) return new File[0];
        java.util.Arrays.sort(files, new java.util.Comparator<File>() {
            @Override public int compare(File a, File b) {
                long d = a.lastModified() - b.lastModified();
                if (d != 0) return d < 0 ? -1 : 1;
                return a.getName().compareTo(b.getName());
            }
        });
        return files;
    }

    /** The readers append a newline after every line, so each pasted text got an extra line break. */
    public static String trimTrailingNewline(CharSequence sb) {
        String s = sb.toString();
        return s.endsWith("\n") ? s.substring(0, s.length() - 1) : s;
    }
}
