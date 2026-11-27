package com.kurdikeyboard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

/**
 * A keyboard cannot ask for runtime permissions itself (Android 6+), so features that need
 * one (phone contacts) open this invisible activity, which asks and closes.
 */
public class PermissionActivity extends Activity {

    public static final String EXTRA_PERMISSION = "permission";
    private static final int REQUEST = 7;

    /** true if granted; otherwise starts the request and returns false */
    public static boolean ensure(Context ctx, String permission) {
        if (Build.VERSION.SDK_INT < 23
                || ctx.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        Intent i = new Intent(ctx, PermissionActivity.class);
        i.putExtra(EXTRA_PERMISSION, permission);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(i);
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String permission = getIntent().getStringExtra(EXTRA_PERMISSION);
        if (permission == null || Build.VERSION.SDK_INT < 23) {
            finish();
            return;
        }
        requestPermissions(new String[]{permission}, REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] results) {
        if (results.length > 0 && results[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, R.string.permission_granted, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, R.string.permission_denied, Toast.LENGTH_LONG).show();
        }
        finish();
    }
}
