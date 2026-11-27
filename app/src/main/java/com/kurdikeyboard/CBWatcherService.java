package com.kurdikeyboard;

/**
 * Created by Stat on 3/5/2017.
 */
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

import android.app.Service;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.ClipboardManager.OnPrimaryClipChangedListener;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.provider.SyncStateContract;
import android.widget.Toast;

public class CBWatcherService extends Service {

    private final String tag = "[[ClipboardWatcherService]] ";
    private OnPrimaryClipChangedListener listener = new OnPrimaryClipChangedListener() {
        public void onPrimaryClipChanged() {
            performClipboardCheck();
        }
    };

    @Override
    public void onCreate() {
        ((ClipboardManager) getSystemService(CLIPBOARD_SERVICE)).addPrimaryClipChangedListener(listener);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //this.getFilesDir()
       // File folder = new File(this.getCacheDir(),Constants.ClipboardCacheFolderPath);
        File directory = AppStorage.root(getApplicationContext());
        File folder = new File( directory + "/data/data/com.kurdikeyboard" + Constants.ClipboardCacheFolderPath );
        /*if(getFilesDir().exists()){
            Toast.makeText(this, "getFilesDir() exists", Toast.LENGTH_LONG).show();

        }else{
            Toast.makeText(this, "getFilesDir() not exists", Toast.LENGTH_LONG).show();

        }*/
        //File folderroot = new File(getFilesDir());
        // ClipboardCacheFolderPath is a predefined constant with the path
        // where the clipboard contents will be written

        if (!folder.exists()) {
           try {
              // Toast.makeText(this, "!!!folder.exists", Toast.LENGTH_LONG).show();

               folder.mkdir();
           } catch (Exception e) {
               Toast.makeText(this,
                       e.getMessage()
                       ,Toast.LENGTH_SHORT).show();


           }
        }
     //   Toast.makeText(this, "Service start", Toast.LENGTH_LONG).show();

        return START_STICKY;
    }

    public void  CreatAllDirecories(String path2){
        File directory = AppStorage.root(getApplicationContext());
        File folder = new File( directory + path2 );

        if (!folder.exists()) {
            // Toast.makeText(this, "!!!folder.exists222", Toast.LENGTH_LONG).show();

            try {
                folder.mkdir();
            } catch (Exception e) {
                Toast.makeText(this,
                        e.getMessage()
                        ,Toast.LENGTH_SHORT).show();


            }
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void performClipboardCheck() {
        saveClip(this);
    }

    /** Saves the current clipboard text into the clipboard history folder. */
    public static void saveClip(android.content.Context ctx) {
        new ClipSaver(ctx).save();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        /* IF YOU WANT THIS SERVICE KILLED WITH THE APP THEN UNCOMMENT THE FOLLOWING LINE */
        //handler.removeCallbacks(runnable);
    }


}