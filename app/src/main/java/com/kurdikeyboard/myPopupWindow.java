package com.kurdikeyboard;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.inputmethodservice.KeyboardView;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.ContactsContract;
import androidx.viewpager.widget.ViewPager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;
import android.content.Intent;
import com.kurdikeyboard.KKeyboardView;
/**
 * Created by Stat on 2/28/2017.
 */
public class myPopupWindow extends PopupWindow {
    private kurdikeyboard kk;
    View rootView;
    Context mContext;
    private int keyBoardHeight = 0;
    private Boolean pendingOpen = false;
    private Boolean isOpened = false;
    private ImageView imgview1;
    private ImageView imgview2;
    private ImageView imgview3;
    private ImageView imgview4;
    private ImageView imgview5;
    private ImageView imgview6;
    OnSoftKeyboardOpenCloseListener onSoftKeyboardOpenCloseListenerpopupsetting;


    public myPopupWindow(View rootView, Context mContext
       , kurdikeyboard kv){
        super(mContext);
        this.mContext = mContext;
        this.rootView = rootView;
        this.kk=kv;
        View customView = createCustomView();
        setContentView(customView);
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        //default size
        setSize((int) mContext.getResources().getDimension(R.dimen.keyboard_height), WindowManager.LayoutParams.MATCH_PARENT);
    }
    public void showAtBottom(){
        showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
    }
    public void showAtBottomPending(){

        showAtBottom();

    }

    public void setSizeForSoftKeyboard(){
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                rootView.getWindowVisibleDisplayFrame(r);

                int screenHeight = getUsableScreenHeight();
                int heightDifference = screenHeight
                        - (r.bottom - r.top);
                int resourceId = mContext.getResources()
                        .getIdentifier("status_bar_height",
                                "dimen", "android");
                if (resourceId > 0) {
                    heightDifference -= mContext.getResources()
                            .getDimensionPixelSize(resourceId);
                }
                if (heightDifference > 100) {
                    keyBoardHeight = heightDifference;
                    setSize(WindowManager.LayoutParams.MATCH_PARENT, keyBoardHeight);
                    if(isOpened == false){
                        if(onSoftKeyboardOpenCloseListenerpopupsetting!=null)
                            onSoftKeyboardOpenCloseListenerpopupsetting.onKeyboardOpen(keyBoardHeight);
                    }
                    isOpened = true;
                    if(pendingOpen){
                        showAtBottom();
                        pendingOpen = false;
                    }
                }
                else{
                    isOpened = false;
                    if(onSoftKeyboardOpenCloseListenerpopupsetting!=null)
                        onSoftKeyboardOpenCloseListenerpopupsetting.onKeyboardClose();
                }
            }
        });
    }
    private int getUsableScreenHeight() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            DisplayMetrics metrics = new DisplayMetrics();

            WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            windowManager.getDefaultDisplay().getMetrics(metrics);

            return metrics.heightPixels;

        } else {
            return rootView.getRootView().getHeight();
        }
    }
    /**
     * Manually set the popup window size
     * @param width Width of the popup
     * @param height Height of the popup
     */
    public void setSize(int width, int height){
        setWidth(width);
        setHeight(height);
    }

    private View createCustomView() {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.layout_setting_option, null, false);

        imgview1=(ImageView)view.findViewById(R.id.imageview1setting);
        imgview2=(ImageView)view.findViewById(R.id.imageview2setting);
        imgview3=(ImageView)view.findViewById(R.id.imageview3setting);
        imgview4=(ImageView)view.findViewById(R.id.imageview4setting);
        imgview5=(ImageView)view.findViewById(R.id.imageview5setting);
        imgview6=(ImageView)view.findViewById(R.id.imageview6setting);

        imgview3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i1 = new Intent(mContext,Main2Activity.class);
                i1.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE); // Show user only contacts w/ phone numbers
              //  Toast.makeText(myPopupWindow.this.mContext, "ورود به بخش ارسال مخاطبین از طریق دسترسی غیر مستقیم (کپی شدن اتوماتیک مخاطب) و با قابلیت جستجوی مخاطب. در صورتی که تعداد مخاطبین زیاد است از این طریق اقدام کنید.", Toast.LENGTH_SHORT).show();

                try {
                    i1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(i1);
                    

                } catch (Exception e){
                    Toast toast = Toast.makeText(myPopupWindow.this.mContext, e.getMessage(), Toast.LENGTH_SHORT);
                    toast.show();
                }
                 }
        });
        imgview2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {


                    myPopupWindow.this.kk.onKey( KKeyboardView.KEYCODE_LONG_RUN_POPUPOPTONS
                    ,null);

                   // getOnKeyboardActionListener()
                    // .
                    myPopupWindow.this.dismiss();
                  //  Toast toast = Toast.makeText(myPopupWindow.this.mContext, "ورود به بخش کلیپبورد. لطفا برای تنظیم فونت این بخش از قسمت تنظیمات، تنظیمات فونت و بخش فونت کلیپبورد استفاده نمایید.", Toast.LENGTH_SHORT);
                   // toast.show();


                } catch (Exception e){
                    Toast toast = Toast.makeText(myPopupWindow.this.mContext, e.getMessage(), Toast.LENGTH_SHORT);
                    toast.show();
                }


            }
        });
        imgview1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myPopupWindow.this.kk.onKey( KKeyboardView.KEYCODE_Ready_Sentences
                        ,null);
               // Toast toast = Toast.makeText(myPopupWindow.this.mContext, "ورود به بخش جملات آماده. لطفا برای تنظیم فونت این بخش از قسمت تنظیمات، تنظیمات فونت و بخش فونت جملات آماده استفاده نمایید.", Toast.LENGTH_LONG);
               // toast.show();

            }
        });
        imgview6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {


                    myPopupWindow.this.kk.onKey( KKeyboardView.KEYCODE_CONTACCTS_READ_2
                            ,null);

                    // getOnKeyboardActionListener()
                    // .
                    myPopupWindow.this.dismiss();
                   // Toast toast = Toast.makeText(myPopupWindow.this.mContext, "دسترسی مستقیم به مخاطبین و بدون امکان جستجوی مخاطبین. لطفا برای تنظیم فونت این بخش به تنظیمات، از قسمت تنظیمات، تنظیمات فونت و بخش فونت مخاطبین استفاده نمایید.", Toast.LENGTH_LONG);
                  //  toast.show();



                } catch (Exception e){
                    Toast toast = Toast.makeText(myPopupWindow.this.mContext, e.getMessage(), Toast.LENGTH_SHORT);
                    toast.show();
                }

            }
        });
        imgview5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast toast = Toast.makeText(myPopupWindow.this.mContext, "در دست طراحی", Toast.LENGTH_SHORT);
               // toast.show();
                myPopupWindow.this.dismiss();

            }
        });
        imgview4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(myPopupWindow.this.mContext, ImePreferences.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                // startActivity(i);
                try {
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                   // Toast toast = Toast.makeText(myPopupWindow.this.mContext, "ورود به تنظیمات", Toast.LENGTH_SHORT);
                   // toast.show();
                    mContext.startActivity(i);
                } catch (Exception e){
                    Toast toast = Toast.makeText(myPopupWindow.this.mContext, e.getMessage(), Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        // get last selected page
        // last page was recents, check if there are recents to use
        // if none was found, go to page 1

        return view;
    }
    public static class RepeatListener implements View.OnTouchListener {

        private Handler handler = new Handler();

        private int initialInterval;
        private final int normalInterval;
        private final View.OnClickListener clickListener;

        private Runnable handlerRunnable = new Runnable() {
            @Override
            public void run() {
                if (downView == null) {
                    return;
                }
                handler.removeCallbacksAndMessages(downView);
                handler.postAtTime(this, downView, SystemClock.uptimeMillis() + normalInterval);
                clickListener.onClick(downView);
            }
        };

        private View downView;

        /**
         * @param initialInterval The interval before first click event
         * @param normalInterval  The interval before second and subsequent click
         *                        events
         * @param clickListener   The OnClickListener, that will be called
         *                        periodically
         */
        public RepeatListener(int initialInterval, int normalInterval, View.OnClickListener clickListener) {
            if (clickListener == null)
                throw new IllegalArgumentException("null runnable");
            if (initialInterval < 0 || normalInterval < 0)
                throw new IllegalArgumentException("negative interval");

            this.initialInterval = initialInterval;
            this.normalInterval = normalInterval;
            this.clickListener = clickListener;
        }

        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    downView = view;
                    handler.removeCallbacks(handlerRunnable);
                    handler.postAtTime(handlerRunnable, downView, SystemClock.uptimeMillis() + initialInterval);
                    clickListener.onClick(view);
                    return true;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_OUTSIDE:
                    handler.removeCallbacksAndMessages(downView);
                    downView = null;
                    return true;
            }
            return false;
        }
    }

    public void setOnSoftKeyboardOpenCloseListener(OnSoftKeyboardOpenCloseListener listener){
        this.onSoftKeyboardOpenCloseListenerpopupsetting = listener;
    }

    public interface OnSoftKeyboardOpenCloseListener{
        void onKeyboardOpen(int keyBoardHeight);
        void onKeyboardClose();
    }


    /**
     * Dismiss the popup
     */
    @Override
    public void dismiss() {
        super.dismiss();

    }




}
