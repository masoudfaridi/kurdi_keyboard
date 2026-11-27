package com.kurdikeyboard;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Calendar;

/**
 * Writes the clipboard text into the clipboard-history folder, in the format that
 * MyPopupClipboard reads: line 1 = date, line 2 = Shamsi date, then the text.
 *
 * Replaces CBWatcherService.performClipboardCheck(): the keyboard now listens to the
 * clipboard itself, because starting a background service is not allowed since Android 8
 * and only the active keyboard may read the clipboard since Android 10.
 */
final class ClipSaver {

    private static String sLastSaved;
    private final Context ctx;

    ClipSaver(Context ctx) {
        this.ctx = ctx.getApplicationContext();
    }

    void save() {
        try {
            ClipboardManager cb = (ClipboardManager) ctx.getSystemService(Context.CLIPBOARD_SERVICE);
            if (cb == null || !cb.hasPrimaryClip()) return;
            ClipData cd = cb.getPrimaryClip();
            if (cd == null || cd.getItemCount() == 0) return;
            // passwords copied from a password manager are marked sensitive: never stored
            android.content.ClipDescription desc = cd.getDescription();
            if (desc != null && desc.getExtras() != null
                    && desc.getExtras().getBoolean("android.content.extra.IS_SENSITIVE", false)) return;
            CharSequence text = cd.getItemAt(0).coerceToText(ctx);
            if (text == null || text.length() == 0) return;
            String s = text.toString();
            if (s.equals(sLastSaved)) return; // same text copied again: no duplicate entry
            sLastSaved = s;

            File folder = new File(AppStorage.root(ctx) + "/data/data/com.kurdikeyboard"
                    + Constants.ClipboardCacheFolderPath);
            if (!folder.exists()) folder.mkdirs();

            Calendar cal = Calendar.getInstance();
            String date = cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1)
                    + "-" + cal.get(Calendar.DAY_OF_MONTH) + "-" + cal.get(Calendar.HOUR_OF_DAY)
                    + "-" + cal.get(Calendar.MINUTE) + "-" + cal.get(Calendar.SECOND);
            File file = new File(folder, date + "-" + cal.get(Calendar.MILLISECOND) + ".txp");
            BufferedWriter w = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
            try {
                w.write(date);
                w.newLine();
                w.write(Utilities.getCurrentShamsidate());
                w.newLine();
                w.write(s);
            } finally {
                w.close();
            }
            File[] all = AppStorage.sortedFiles(folder);
            for (int i = 0; i < all.length - 200; i++) all[i].delete(); // keep the newest 200
        } catch (IOException | RuntimeException ignored) {
            // clipboard history is best effort; never crash the keyboard for it
        }
    }
}
