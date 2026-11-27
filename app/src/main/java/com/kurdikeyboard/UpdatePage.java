package com.kurdikeyboard;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

/**
 * Created by Stat on 5/2/2017.
 */
public class UpdatePage extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(android.R.style.Theme_Holo_Light);
        setContentView(R.layout.activity_updatepage);
    }

    public void onClick(View view) {

        if(view.getId()==R.id.buttonupdate) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("bazaar://details?id=" + "com.kurdikeyboard"));
            intent.setPackage("com.farsitel.bazaar");
            startActivity(intent);
        }else if(view.getId()==R.id.buttonnazarat){
            Intent intent = new Intent(Intent.ACTION_EDIT);
            intent.setData(Uri.parse("bazaar://details?id=" + "com.kurdikeyboard"));
            intent.setPackage("com.farsitel.bazaar");
            startActivity(intent);
        }

    }
}
