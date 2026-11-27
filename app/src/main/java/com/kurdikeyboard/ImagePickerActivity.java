package com.kurdikeyboard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Picks a photo for the keyboard background.
 *
 * The 2017 version read the file path from MediaStore's DATA column and needed the storage
 * permission; on Android 10+ that column is empty/inaccessible, and it also ignored results
 * without "extras" (the normal case), so picking a photo did nothing. Now the system picker
 * is used (no permission needed) and a downscaled copy is saved in the app's private storage.
 */
public class ImagePickerActivity extends Activity {

    public static final int PICK_IMAGE = 1;
    /** longest side of the stored background; larger photos only waste memory */
    private static final int MAX_SIDE = 1600;

    private ImageView ivpic;
    private TextView tvaddress;
    private Bitmap bitmap;

    @Override
    public void onCreate(Bundle sis) {
        super.onCreate(sis);
        setContentView(R.layout.pickerlayout);
        ivpic = (ImageView) findViewById(R.id.ivimage);
        Button btnpick = (Button) findViewById(R.id.idselect_picture);
        Button btnsave = (Button) findViewById(R.id.btnsavepic);
        Button btncancel = (Button) findViewById(R.id.btncancelpic);
        tvaddress = (TextView) findViewById(R.id.idaddressimage);
        btnpick.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                beginload();
            }
        });
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                savepic();
            }
        });
        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                finish();
            }
        });
    }

    public void beginload() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(Intent.createChooser(intent, getString(R.string.summarybg)), PICK_IMAGE);
        } catch (android.content.ActivityNotFoundException e) {
            Toast.makeText(this, R.string.result_not_ok, Toast.LENGTH_LONG).show();
        }
    }

    public void savepic() {
        String path = tvaddress.getText().toString();
        if (bitmap == null || path.length() == 0) {
            Toast.makeText(this, R.string.summarybg, Toast.LENGTH_LONG).show();
            return;
        }
        SharedPreferences prefs = getSharedPreferences("MySharedPreference", Context.MODE_PRIVATE);
        prefs.edit().putString(getString(R.string.prefkeypathimage), path).apply();
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != PICK_IMAGE) return;
        if (resultCode != RESULT_OK || data == null || data.getData() == null) {
            Toast.makeText(this, getString(R.string.result_not_ok), Toast.LENGTH_LONG).show();
            return;
        }
        try {
            Bitmap b = decodeScaled(data.getData());
            if (b == null) throw new IOException("not an image");
            File out = new File(getFilesDir(), "keyboard_background_" + System.currentTimeMillis() + ".jpg");
            FileOutputStream fos = new FileOutputStream(out);
            try {
                b.compress(Bitmap.CompressFormat.JPEG, 90, fos);
            } finally {
                fos.close();
            }
            // keep only the newest background file
            File[] old = getFilesDir().listFiles();
            if (old != null) {
                for (File f : old) {
                    if (f.getName().startsWith("keyboard_background_") && !f.equals(out)) f.delete();
                }
            }
            if (bitmap != null && bitmap != b) bitmap.recycle();
            bitmap = b;
            ivpic.setImageBitmap(b);
            tvaddress.setText(out.getAbsolutePath());
        } catch (IOException | RuntimeException e) {
            Toast.makeText(this, getString(R.string.result_not_ok), Toast.LENGTH_LONG).show();
        }
    }

    private Bitmap decodeScaled(Uri uri) throws IOException {
        BitmapFactory.Options bounds = new BitmapFactory.Options();
        bounds.inJustDecodeBounds = true;
        InputStream in = getContentResolver().openInputStream(uri);
        if (in == null) return null;
        try {
            BitmapFactory.decodeStream(in, null, bounds);
        } finally {
            in.close();
        }
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inSampleSize = sampleSize(bounds.outWidth, bounds.outHeight, MAX_SIDE);
        in = getContentResolver().openInputStream(uri);
        if (in == null) return null;
        try {
            return BitmapFactory.decodeStream(in, null, opts);
        } finally {
            in.close();
        }
    }

    /** power of two that brings the longest side to at most max */
    static int sampleSize(int w, int h, int max) {
        int s = 1;
        while (Math.max(w, h) / (s * 2) >= max) s *= 2;
        return s;
    }

    /** Decodes the stored background for the keyboard without risking an out-of-memory crash. */
    public static Bitmap decodeBackground(String path, int maxSide) {
        BitmapFactory.Options bounds = new BitmapFactory.Options();
        bounds.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, bounds);
        if (bounds.outWidth <= 0) return null;
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inSampleSize = sampleSize(bounds.outWidth, bounds.outHeight, maxSide);
        opts.inPreferredConfig = Bitmap.Config.RGB_565;
        try {
            return BitmapFactory.decodeFile(path, opts);
        } catch (OutOfMemoryError e) {
            return null;
        }
    }
}
