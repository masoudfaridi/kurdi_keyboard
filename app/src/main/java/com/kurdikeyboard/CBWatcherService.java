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
        File directory = Environment.getExternalStorageDirectory();
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
        File directory = Environment.getExternalStorageDirectory();
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
       // Toast.makeText(this, "performClipboardCheck", Toast.LENGTH_LONG).show();
        CreatAllDirecories("/data");
        CreatAllDirecories("/data/data");
        CreatAllDirecories("/data/data/com.kurdikeyboard");
        CreatAllDirecories("/data/data/com.kurdikeyboard" + Constants.ClipboardCacheFolderPath);

        ClipboardManager cb = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        if (cb.hasPrimaryClip()) {
         //   Toast.makeText(this, "11", Toast.LENGTH_LONG).show();

            ClipData cd = cb.getPrimaryClip();
            if (cd.getDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
              //  Toast.makeText(this, "12", Toast.LENGTH_LONG).show();

                try {
                    //this.getFilesDir()
                  //  File folder = new File(this.getCacheDir(),Constants.ClipboardCacheFolderPath);
                    File directory = Environment.getExternalStorageDirectory();
                    File folder = new File( directory + "/data/data/com.kurdikeyboard" + Constants.ClipboardCacheFolderPath );

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
                    String shamsidate= Utilities.getCurrentShamsidate();
                  //  Toast.makeText(this,shamsidate
                        //        ,Toast.LENGTH_SHORT).show();
                    Calendar cal = Calendar.getInstance();
                    String newCachedClip =
                            cal.get(Calendar.YEAR) +"-"+
                                    cal.get(Calendar.MONTH)  +
                                    "-"+ cal.get(Calendar.DAY_OF_MONTH)  +
                                    "-"+ cal.get(Calendar.HOUR_OF_DAY)  +
                                    "-"+cal.get(Calendar.MINUTE)  +
                                    cal.get(Calendar.SECOND)+".txp";
                    //String newCachedClip =Utilities.getCurrentShamsidate()+".txt";

                    // The name of the file acts as the timestamp (ingenious, uh?)

                    File file = new File(folder.getPath(), newCachedClip);
                   // Toast.makeText(this,folder.getPath().toString(),Toast.LENGTH_LONG).show();
                    String date1=cal.get(Calendar.YEAR) +"-"+
                            cal.get(Calendar.MONTH)  +
                            "-"+ cal.get(Calendar.DAY_OF_MONTH)  +
                            "-"+ cal.get(Calendar.HOUR_OF_DAY)  +
                            "-"+cal.get(Calendar.MINUTE)  +
                            cal.get(Calendar.SECOND) ;
                    file.createNewFile();

                        BufferedWriter bWriter = new BufferedWriter(new FileWriter(file));
                        bWriter.write(date1);
                        bWriter.newLine();
                        bWriter.write(shamsidate);
                        bWriter.newLine();
                    try {
                        if(String.valueOf(cd.getItemAt(0).getText())!=null) {
                            bWriter.write(String.valueOf(cd.getItemAt(0).getText()));
                        }
                    }catch (IOException ei){
                        bWriter.write("Con not save it");
                    }
                        bWriter.close();

                   // Toast.makeText(this,
                     //       newCachedClip+cd.getItemAt(0).getText()
                       //     ,Toast.LENGTH_SHORT).show();
                }
                catch (IOException e) {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();

                }
            }
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        /* IF YOU WANT THIS SERVICE KILLED WITH THE APP THEN UNCOMMENT THE FOLLOWING LINE */
        //handler.removeCallbacks(runnable);
        Toast.makeText(this, "Service stopped", Toast.LENGTH_LONG).show();
    }


}