package com.kurdikeyboard;

/**
 * Created by Stat on 11/8/2016.
 */

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.os.Bundle;

import android.os.IBinder;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.widget.Toast;
import com.kurdikeyboard.BuildConfig;

import com.farsitel.bazaar.IUpdateCheckService;


public class ImePreferences extends PreferenceActivity
{
    private static SharedPreferences preferenceKeyboard;
    private CheckBoxPreference checkboxchangetheme;
    private int oldversion=0;
    private int newvarsion=1;
    IUpdateCheckService service;
    UpdateServiceConnection connection;
    private static final String TAG = "UpdateCheck";
    private void toastM(String str){
        Toast.makeText(getApplicationContext(),str,Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setLayoutDirection(android.view.View.LAYOUT_DIRECTION_RTL);

        PreferenceManager manager=getPreferenceManager();

        manager.setSharedPreferencesName("MySharedPreference");
        manager.setSharedPreferencesMode(MODE_PRIVATE);
        addPreferencesFromResource(R.xml.ime_preferences);

        checkboxchangetheme=
        (CheckBoxPreference)manager.findPreference(getString(R.string.prefchangethemboolean));
       // if(checkboxchangetheme.isChecked()){toastM("checked");}
                //findPreference("myPreference");

        oldversion= BuildConfig.VERSION_CODE;
        try {
            initService();
        }catch (Exception eio){
            Toast.makeText(ImePreferences.this.getApplicationContext(),
            eio.getMessage(),Toast.LENGTH_LONG).show();

        }
        if(!checkboxchangetheme.isChecked()){
            checkboxchangetheme.setChecked(true);
        }

        Preference clearHistory = manager.findPreference(getString(R.string.prefclearhistory));
        if (clearHistory != null) {
            clearHistory.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    com.kurdikeyboard.predict.PredictionEngine.get(ImePreferences.this).clearHistory();
                    toastM(getString(R.string.history_cleared));
                    return true;
                }
            });
        }



    }

    @Override
    public void onPause(){
        super.onPause();
        if(checkboxchangetheme.isChecked()){
            checkboxchangetheme.setChecked(false);
        }
    }
    private void initService() {
       // Log.i(TAG, "initService()");
        connection = new UpdateServiceConnection();
        Intent i = new Intent(
                "com.farsitel.bazaar.service.UpdateCheckService.BIND");
        i.setPackage("com.farsitel.bazaar");
        boolean ret = bindService(i, connection, Context.BIND_AUTO_CREATE);

       // Log.d(TAG, "initService() bound value: " + ret);
    }

    /** This is our function to un-binds this activity from our service. */
    private void releaseService() {
        unbindService(connection);
        connection = null;
       // Log.d(TAG, "releaseService(): unbound.");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseService();
    }







    /*
    @Override
    public void startActivity(Intent intent) {
        if ((intent.getExtras().getString("name")!=null)&&intent.getExtras().getString("name").equals("imagelunch")) {
            super.startActivityForResult(intent, 1234);
        } else {
            super.startActivity(intent);
        }
    }
    */

    @Override
    protected void onActivityResult(int reqCode, int resCode, Intent data) {
        if (reqCode == 1234) {
            // should be getting called now
        }
    }



    class UpdateServiceConnection implements ServiceConnection {

        public void onServiceConnected(ComponentName name, IBinder boundService) {
            if (preferenceKeyboard == null) {
                preferenceKeyboard=
                        getSharedPreferences("MySharedPreference", Context.MODE_PRIVATE);
            }



            service = IUpdateCheckService.Stub
                    .asInterface((IBinder) boundService);
            try {
                long vCode = service.getVersionCode("com.kurdikeyboard");
                newvarsion= (int) vCode;

                String tempprefisupadat="";
                Preference prefupdateavailable
                        =findPreference(getString(R.string.pref_forceupdate));

                if(vCode==-1 || vCode<=oldversion){ // -1: not on Bazaar; otherwise only newer versions count
                    tempprefisupadat=getString(R.string.isupdate);
                }else {
                    Toast.makeText(ImePreferences.this,
                           "ورژن جدید رسید لطفا برنامه را آپدیت نمایید.",
                            Toast.LENGTH_LONG).show();
                    tempprefisupadat=getString(R.string.updateAvailable);



                }
                if (prefupdateavailable != null) prefupdateavailable.setSummary(tempprefisupadat);

            } catch (Exception e) {
                e.printStackTrace();
            }

            //  Log.d(TAG, "onServiceConnected(): Connected");
        }

        public void onServiceDisconnected(ComponentName name) {
            service = null;
           // Log.d(TAG, "onServiceDisconnected(): Disconnected");
        }
    }


}
