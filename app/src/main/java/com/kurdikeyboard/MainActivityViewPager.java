package com.kurdikeyboard;

import android.app.Activity;
import android.os.Bundle;
import androidx.viewpager.widget.ViewPager;
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
        getWindow().getDecorView().setLayoutDirection(android.view.View.LAYOUT_DIRECTION_RTL);
        setContentView(R.layout.layout_viewpage);
        setTitle(R.string.textbuttonguid);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        final GuidePagerAdapter adapter = new GuidePagerAdapter(this);
        viewPager.setAdapter(adapter);
        final android.widget.Button prev = (android.widget.Button) findViewById(R.id.guidePrev);
        final android.widget.Button next = (android.widget.Button) findViewById(R.id.guideNext);
        final TextView page = (TextView) findViewById(R.id.guidePage);
        final Runnable refresh = new Runnable() {
            @Override public void run() {
                int i = viewPager.getCurrentItem(), n = adapter.getCount();
                page.setText((i + 1) + " از " + n);
                prev.setEnabled(i > 0);
                prev.setAlpha(i > 0 ? 1f : 0.4f);
                next.setText(i == n - 1 ? R.string.guide_done : R.string.guide_next);
            }
        };
        prev.setOnClickListener(new android.view.View.OnClickListener() {
            @Override public void onClick(android.view.View v) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() - 1, true);
            }
        });
        next.setOnClickListener(new android.view.View.OnClickListener() {
            @Override public void onClick(android.view.View v) {
                if (viewPager.getCurrentItem() >= adapter.getCount() - 1) finish();
                else viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override public void onPageSelected(int position) {
                refresh.run();
            }
        });
        refresh.run();


    }
}
