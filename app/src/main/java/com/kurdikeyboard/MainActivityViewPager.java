package com.kurdikeyboard;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Stat on 4/23/2017.
 */
public class MainActivityViewPager extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_viewpage);
        setTheme(android.R.style.Theme_Holo_Light);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new CustomPagerAdapter(this));


    }
}
