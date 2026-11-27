package com.kurdikeyboard;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

/**
 * Ready sentences: a title (key) and a value, e.g. "بانک ملی" -> "6037 9912 ...".
 * Same files as the 2017 version (one ".txp" file per sentence, named after the title,
 * holding the value), so existing sentences keep working.
 */
final class ReadySentenceStore {

    static final class Item {
        final String title;
        final String value;
        final File file;
        Item(String title, String value, File file) { this.title = title; this.value = value; this.file = file; }
    }

    private ReadySentenceStore() {}

    static File dir(Context ctx) {
        File d = new File(AppStorage.root(ctx) + "/data/data/com.kurdikeyboard/" + Constants.ReadySentencesFolderPath);
        if (!d.exists()) d.mkdirs();
        return d;
    }

    /** newest first */
    static List<Item> list(Context ctx) {
        List<Item> out = new ArrayList<>();
        File[] files = AppStorage.sortedFiles(dir(ctx));
        for (int i = files.length - 1; i >= 0; i--) {
            File f = files[i];
            if (!f.getName().endsWith(".txp")) continue;
            String name = f.getName();
            out.add(new Item(fromFileName(name.substring(0, name.length() - 4)), read(f), f));
        }
        return out;
    }

    /** saves (or replaces) a sentence; returns false on error */
    static boolean save(Context ctx, String title, String value, File replacing) {
        File f = new File(dir(ctx), toFileName(title) + ".txp");
        try {
            Writer w = new OutputStreamWriter(new FileOutputStream(f), "UTF-8");
            try {
                w.write(value);
            } finally {
                w.close();
            }
            if (replacing != null && !replacing.equals(f)) replacing.delete();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    static boolean exists(Context ctx, String title) {
        return new File(dir(ctx), toFileName(title) + ".txp").exists();
    }

    private static String read(File f) {
        StringBuilder sb = new StringBuilder();
        BufferedReader r = null;
        try {
            r = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"));
            String line;
            while ((line = r.readLine()) != null) {
                if (sb.length() > 0) sb.append('\n');
                sb.append(line);
            }
        } catch (IOException ignored) {
        } finally {
            if (r != null) try { r.close(); } catch (IOException ignored) {}
        }
        return sb.toString();
    }

    /** "/" and other characters a file name cannot hold are swapped for look-alikes */
    private static String toFileName(String title) {
        String t = title.trim();
        if (t.isEmpty()) t = "—";
        return t.replace('/', '\u2215').replace('\\', '\u29F5').replace(':', '\uA789')
                .replace('*', '\u2217').replace('?', '\uFF1F').replace('"', '\u201D')
                .replace('<', '\u2039').replace('>', '\u203A').replace('|', '\u2223')
                .replace('\n', ' ');
    }

    private static String fromFileName(String name) {
        return name.replace('\u2215', '/').replace('\u29F5', '\\').replace('\uA789', ':')
                .replace('\u2217', '*').replace('\uFF1F', '?').replace('\u2039', '<')
                .replace('\u203A', '>').replace('\u2223', '|');
    }
}
