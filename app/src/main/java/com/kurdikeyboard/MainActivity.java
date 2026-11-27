package com.kurdikeyboard;

import android.app.Activity;
import android.content.res.Resources;
import android.inputmethodservice.InputMethodService;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;
import android.content.Intent;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(android.R.style.Theme_Holo_Light);
        setContentView(R.layout.activity_main);




        Button buttonactivation
                = (Button) findViewById(R.id.buttonactivationIME);
        if(!enabledIME()){
            buttonactivation.setText("فعال سازی مرحله اول");


        }else if(enabledIME()&&!defaultedIME()){
            buttonactivation.setText("فعال سازی تنظیم پیش فرض");

        }else {
            buttonactivation.setText("ورود به تنظیمات");


        }


        //startService(new Intent(this, CBWatcherService.class));
    }
    @Override
    protected void onPostResume(){
        super.onPostResume();
        if(defaultedIME()) {

        }
        Button buttonactivation
                = (Button) findViewById(R.id.buttonactivationIME);

        if(!enabledIME()){
            buttonactivation.setText("فعال سازی مرحله اول");


        }else if(enabledIME()&&!defaultedIME()){
            buttonactivation.setText("فعال سازی تنظیم پیش فرض");

        }else {

            buttonactivation.setText("ورود به تنظیمات");


        }

    }




    public void onClick(View view) {
        Button buttonactivation
                = (Button) findViewById(R.id.buttonactivationIME);

        Button buttonGuid= (Button) findViewById(R.id.buttonGuid);

        switch (view.getId()) {
            case R.id.buttonactivationIME:

            if (!enabledIME()) {
                Intent i = new Intent(Settings.ACTION_INPUT_METHOD_SETTINGS);
                startActivity(i);
                Toast.makeText(getBaseContext(), "لطفا تیک مربوط به کیبورد کردی را فعال نمایید", Toast.LENGTH_LONG).show();


            } else if (enabledIME() && !defaultedIME()) {


                InputMethodManager imeManager = (InputMethodManager) getApplicationContext().getSystemService(INPUT_METHOD_SERVICE);

                imeManager.showInputMethodPicker();


                if (defaultedIME()) {
                    buttonactivation.setText("ورود به تنظیمات");

                }

            } else {

                Toast.makeText(getBaseContext(), "کیبورد فعال است. ورود به تنظیمات", Toast.LENGTH_LONG).show();
                Intent isetting = new Intent(this, ImePreferences.class);

                isetting.setAction(Intent.ACTION_VIEW);
                isetting.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                isetting.addCategory(Intent.CATEGORY_LAUNCHER);
                isetting.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                try {


                    startActivity(isetting);
                } catch (Exception e) {
                    Toast toast = Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT);
                    toast.show();
                }

            }
                break;

            case R.id.buttonGuid:
                Intent i = new Intent(this,MainActivityViewPager.class);

                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                try {
                    startActivity(i);
                    Toast.makeText(getBaseContext(), "راهنمای استفاده از کیبورد", Toast.LENGTH_LONG).show();
                }catch (Exception ei){
                    Toast.makeText(getBaseContext(),
                          ei.getMessage()
                            , Toast.LENGTH_LONG).show();

                }


            break;
            case R.id.buttonTelegram:
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/MAPRYS"));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case R.id.buttonTelegramtrain:
                Intent intent2 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/MAPRYS"));
                intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent2);
                break;
        }
    }
    boolean enabledIME(){
        boolean returnenabled=false;
        String kurdiKeyboardstring="com.kurdikeyboard/.kurdikeyboard";

        InputMethodManager imeManager = (InputMethodManager) getApplicationContext().getSystemService(INPUT_METHOD_SERVICE);
        List<InputMethodInfo> listIME=
                imeManager.getEnabledInputMethodList();
        for(InputMethodInfo info: listIME){
         //   Toast.makeText(getBaseContext(), info.getPackageName().toString(),Toast.LENGTH_SHORT).show();


            if(info.getPackageName().toString().equals("com.kurdikeyboard")){
                returnenabled=true;
            }
        }
        return returnenabled;
    }
    boolean defaultedIME(){
        String kurdiKeyboardstring="com.kurdikeyboard/.kurdikeyboard";
        boolean returndefault=false;
        String currentKeyboardstring=Settings.Secure.
                getString(getContentResolver(), Settings.Secure.DEFAULT_INPUT_METHOD);
        if(currentKeyboardstring.equals(kurdiKeyboardstring)){
            returndefault=true;
        }
        return returndefault;
    }
}
