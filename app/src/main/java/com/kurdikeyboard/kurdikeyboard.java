package com.kurdikeyboard;


/**
 * Created by Stat on 11/8/2016.
 */


import android.app.Activity;
import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.text.InputType;
import android.text.Selection;
import android.text.method.MetaKeyKeyListener;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.HapticFeedbackConstants;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.CompletionInfo;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.ExtractedText;
import android.view.inputmethod.ExtractedTextRequest;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.view.inputmethod.InputMethodSubtype;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.content.Context;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import github.ankushsachdeva.emojicon.EmojiconEditText;
import github.ankushsachdeva.emojicon.EmojiconGridView.OnEmojiconClickedListener;
import github.ankushsachdeva.emojicon.EmojiconsPopup;
import github.ankushsachdeva.emojicon.EmojiconsPopup.OnEmojiconBackspaceClickedListener;
import github.ankushsachdeva.emojicon.EmojiconsPopup.OnSoftKeyboardOpenCloseListener;
import github.ankushsachdeva.emojicon.emoji.Emojicon;
import github.ankushsachdeva.emojicon.EmojiconsPopup.*;


import android.view.KeyEvent;
import android.view.Menu;

import android.view.View.OnClickListener;

import android.widget.ArrayAdapter;

import android.widget.ListView;
import android.widget.PopupWindow.OnDismissListener;
import java.util.Calendar;
import android.view.ViewGroup;
import com.kurdikeyboard.predict.PredictionEngine;
import com.kurdikeyboard.predict.SwipeDecoder;
import com.kurdikeyboard.predict.TextNormalizer;
import com.kurdikeyboard.predict.WordDictionary;
//import github.ankushsachdeva.emojicon.



public class kurdikeyboard extends InputMethodService
        implements KeyboardView.OnKeyboardActionListener {
    static final boolean DEBUG = false;
    static boolean isnumbersqwrty=false;
    private ImageView ivKeyboard,ivKeyboardSelectall;
    static int heightkeyboard,widthkeyboard;
    /**
     * This boolean indicates the optional example code for performing
     * processing of hard keys in addition to regular text generation
     * from on-screen interaction.  It would be used for input methods that
     * perform language translations (such as converting text entered on
     * a QWERTY keyboard to Chinese), but may not be used for input methods
     * that are primarily intended to be used for on-screen text entry.
     */
    static final boolean PROCESS_HARD_KEYS = true;
    static Bitmap bitmap,convertedbitmap;
    static Drawable dr;
    //trialall
    //private boolean trialversionmonth10isend;








    ////// 1 need in onCreate()
    private String mWordSeparators;
    private String mwordtwofunction;
    private String mwordtwofunction2;
    private String mWordmultichoiceprimaries;
    private InputMethodManager mInputMethodManager;
    ////// end 1

    //////// 2 need in onCreateInputView
    private KKeyboardView mInputView;
    /////// end 2

    /////// 3 define KKeyboard
    private KKeyboard mQwertyKeyboard; ///need in onCreateInputView()

    private KKeyboard mSymbolsKeyboard;
    private KKeyboard mSymbolsnumbers;
    private KKeyboard mSymbolsfarsiKeyboard;
    private KKeyboard mSymbolsShiftedKeyboard;

    private KKeyboard mSymbolsShiftedKeyboard3;
    private KKeyboard mSymbolsShiftedKeyboard4;
    private KKeyboard mSymbolsShiftedKeyboard5;
    private KKeyboard mSymbolsShiftedKeyboard6;
    private KKeyboard mSymbolsShiftedKeyboard7;
    private KKeyboard mSymbolsShiftedKeyboard8;
    private KKeyboard mSymbolsShiftedKeyboard9;
    private KKeyboard mSymbolsShiftedKeyboard10;
    private KKeyboard mSymbolsShiftedKeyboard11;
    private KKeyboard mSymbolsShiftedKeyboard12;
    private KKeyboard mSymbolsShiftedKeyboard13;
    private KKeyboard mSymbolsShiftedKeyboard14;

    private KKeyboard mQwertyShiftedKeyboard;
    ////// end 3 will be initialize in onInitializeInterface()

    //////4 define farsi and kurdi KKeyboard
    private KKeyboard mkurdishKeyboard;
    private KKeyboard mkurdishKeyboard_extend;
    private KKeyboard mkurdishShiftedKeyboard;
    private KKeyboard mfarsiKeyboard;
    private KKeyboard mfarsiShiftedKeyboard;

    //////end 4 will be initialize in onInitializeInterface()

    //////4 define need in onInitializeInterface()
    private int mLastDisplayWidth;
    //////end 4

    //////5 define need in onkey()
    private boolean mPredictionOn;
    private StringBuilder mComposing = new StringBuilder();
    ////// end 5

    //////6 define need in updateShiftKeyState
    private boolean mCapsLock;
    ////// end 6

    ////// 7 define need in onstartinput
    private long mMetaState;
    private boolean mCompletionOn;
    private KKeyboard mCurKeyboard;

    private CompletionInfo[] mCompletions;

    ////// end 7
    private long mLastShiftTime;

    public static String language="en";
    private String prelanguage="en";
    private CandidateView mCandidateView;

    private static String strCopytemp=null;
    private static int endETselection;
    private static int startETselection;



    private View popUpView;
    private LinearLayout parentLayout;
    private boolean mIsDown=false;
    private float spacex=0;
    private float spacey=0;
    private float spaceH;
    private float spaceW;
    private boolean spacemove=false;
    private boolean spacemovetoright=false;
    float x1 = 0, x2, y1 = 0, y2, dx, dy , oldx =0,oldy=0;


    ////////////////////////////////////
    //////// preferences
    //private boolean vibreh;
    //private String preftext;
    private static SharedPreferences preferenceKeyboard;
    private View rootTemp;

    private  ImageView ivTemp;
    //////////
    //popup long press key
   // private PopupWindow popuppreviewlongclick;
    private  PopupWindow mPreviewPopup;
    private TextView mPreviewText;
    private int mPreviewTextSizeLarge;
    private int keyx;
    private int keywidth;
    private int keyy;
    private int keyheight;
    private  int tempint;
    ///is about long press preview when tools bar is
    ///  visible or gone
    private int tempintfullscreen;

    ////////
   // private String[] clipboardString;
   // private int clipboardStringLen;
    public List<String> mallclipboardStrings ;
    public List<String> mallclipboarddate ;

    ////////////////////////////////////////////////////////////


    private String listS;
    private boolean language_kurdi_enable;
    private boolean language_farsi_enable;
    private LinearLayout toollayout;
    private boolean toolskayout_enable;
    private boolean correct_kurdi_v_abouve;
    private boolean doubleclick_on_farsi;
    private boolean doubleclick_on_kurdi;
    private boolean extend_on_kurdi;

    private int timewordtwofuction;



    private boolean isdowncursorbacl;
    ////////////////////////////////////////////////////////////
    @Override
    public void onCreate() {
        super.onCreate();
        mPredict = PredictionEngine.get(this);
        //trialall
      // trialversionmonth10isend=false;
         mInputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);

        /////// get sumbols like # @
       // mWordSeparators = getResources().getString(R.string.word_separators);
        mWordSeparators = getResources().getString(R.string.word_separators2);
        mWordmultichoiceprimaries=getResources().getString(R.string.mWordmultichoiceprimaries);
        mwordtwofunction= getResources().getString(R.string.word_twofunction);
        mwordtwofunction2= getResources().getString(R.string.word_twofunction2);

        mCapsLock=false;
        endETselection=0;
        startETselection=0;
        tempint= (int) getResources().getDimension(R.dimen.longclickyadd);
        tempintfullscreen= (int) getResources().getDimension(R.dimen.fullscreenadd);

        registerClipboardListener();
       // clipboardString=new String[];
       // clipboardString[0]="firststring";
      // clipboardStringLen=1;

        mallclipboardStrings=new ArrayList<String>();
        mallclipboarddate=new ArrayList<String>();

        //  Toast.makeText(getBaseContext(),"onCreate",Toast.LENGTH_SHORT).show();
       /* try{
            startService(new Intent(this, CBWatcherService.class));
            //toastM("start clicked");
        }catch (Exception ei){
            toastM(ei.getMessage());

        }*/
        listS="";
        language_kurdi_enable=true;
        language_farsi_enable=true;
        toolskayout_enable=false;
        correct_kurdi_v_abouve=false;
        doubleclick_on_farsi=true;
        doubleclick_on_kurdi=true;
        extend_on_kurdi=false;
        isdowncursorbacl=false;
        timewordtwofuction=0;

    }

    ////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////

    @Override public void onInitializeInterface() {
      // toastM("onInitializeInterface");
       // WindowManager wm = (WindowManager) this.getBaseContext().getSystemService(Context.WINDOW_SERVICE);
       // Display display = wm.getDefaultDisplay();
       // DisplayMetrics size = new DisplayMetrics();
       // int totalwidth = display.getWidth();  // deprecated
       // int totalheight = display.getHeight();  // deprecated
       // Toast.makeText(getBaseContext(),"onInitializeInference",Toast.LENGTH_LONG).show();

        if (mQwertyKeyboard != null) {
            // Configuration changes can happen after the keyboard gets recreated,
            // so we need to be able to re-build the keyboards if the available
            // space has changed.
            int displayWidth = getMaxWidth();
            if (displayWidth == mLastDisplayWidth) return;
            mLastDisplayWidth = displayWidth;
        }
        if (preferenceKeyboard == null) {
            preferenceKeyboard=
                    getSharedPreferences("MySharedPreference", Context.MODE_PRIVATE);
        }
        timewordtwofuction=preferenceKeyboard.getInt(getString(R.string.prefseekbartimewordtwofuction), 300);
        doubleclick_on_farsi=
                preferenceKeyboard.getBoolean(getString(R.string.preflanguage_farsi_doublecick), true);
        doubleclick_on_kurdi=
                preferenceKeyboard.getBoolean(getString(R.string.preflanguage_kurdi_doublecick), true);
        extend_on_kurdi=preferenceKeyboard.getBoolean(getString(R.string.preflanguage_kurdi_extend), false);

        mQwertyKeyboard = new KKeyboard(this,R.xml.qwerty_en_2);




        mfarsiKeyboard= new KKeyboard(this,R.xml.qwerty_fa_2);
        if(!doubleclick_on_farsi){
            mfarsiKeyboard = new KKeyboard(this,R.xml.qwerty_fa_2_notdouble_click);
        }

        mSymbolsKeyboard= new KKeyboard(this,R.xml.symbols_en_1);
        mSymbolsnumbers= new KKeyboard(this,R.xml.symbols_numbers);
        mSymbolsShiftedKeyboard= new KKeyboard(this,R.xml.symbols_en_2);

        mSymbolsShiftedKeyboard3= new KKeyboard(this,R.xml.symbols_en_3);

        mSymbolsShiftedKeyboard4= new KKeyboard(this,R.xml.symbols_en_4);
        mSymbolsShiftedKeyboard5= new KKeyboard(this,R.xml.symbols_en_5);
        mSymbolsShiftedKeyboard6= new KKeyboard(this,R.xml.symbols_en_6);
        mSymbolsShiftedKeyboard7= new KKeyboard(this,R.xml.symbols_en_7);

        mSymbolsShiftedKeyboard8= new KKeyboard(this,R.xml.symbols_en_8);

        mSymbolsShiftedKeyboard9= new KKeyboard(this,R.xml.symbols_en_9);
        mSymbolsShiftedKeyboard10= new KKeyboard(this,R.xml.symbols_en_10);
        mSymbolsShiftedKeyboard11= new KKeyboard(this,R.xml.symbols_en_11);
        mSymbolsShiftedKeyboard12= new KKeyboard(this,R.xml.symbols_en_12);
        mSymbolsShiftedKeyboard13= new KKeyboard(this,R.xml.symbols_en_13);
        mSymbolsShiftedKeyboard14= new KKeyboard(this,R.xml.symbols_en_13);
        mSymbolsfarsiKeyboard= new KKeyboard(this,R.xml.symbols_fa_1);

        mkurdishKeyboard= new KKeyboard(this,R.xml.qwerty_kr_2);
        // mkurdishKeyboard_extend= new KKeyboard(this,R.xml.qwerty_kr_extend);
        if(!doubleclick_on_kurdi){
            mkurdishKeyboard = new KKeyboard(this,R.xml.qwerty_kr_2_nodouble_click);
        }
        if(extend_on_kurdi){
            mkurdishKeyboard = new KKeyboard(this,R.xml.qwerty_kr_extend);
        }









    }

    ////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////


    private void setKKeyboard(KKeyboard nextKeyboard) {
        Calendar cal2 = Calendar.getInstance();
      //  Toast.makeText(getBaseContext(),"onsetKKeyboard",Toast.LENGTH_LONG).show();
        //trialall
        /*
        if(trialversionmonth10isend) {
            Toast.makeText(getBaseContext(),"زمان استفاده از نسخه آزمایشی به پایان رسیده است.",Toast.LENGTH_LONG).show();
            InputMethodManager imeManager = (InputMethodManager) getApplicationContext().getSystemService(INPUT_METHOD_SERVICE);

            imeManager.showInputMethodPicker();

        }else  if (
                cal2.get(Calendar.MONTH)!=9  ){
            toastM("زمان نسخه آزمایشی به پایان رسیده است. برای استفاده از کیبورد لطفا اقدام به خرید آن از بازار نمایید.");
            //  toastM("month: "+cal.getTime().getMonth());

            InputMethodManager imeManager = (InputMethodManager) getApplicationContext().getSystemService(INPUT_METHOD_SERVICE);

            imeManager.showInputMethodPicker();}
        else if(!(isTimeAutomatic(getApplicationContext()))){
            Intent intent = new Intent(
                    android.provider.Settings.ACTION_DATE_SETTINGS);

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            try {
                toastM("لطفا تیک زمان و تاریخ خودکار(اتوماتیک) را فعال نمایید.(Automatic date and time)");
                startActivity(intent);
            }catch (Exception ei){
                toastM("لطفا به صورت دستی، تیک اتوماتیک تایم را در بخش زمان و تاریخ گوشی خود فعال نمایید.");
            }

        }  else{

            mInputView.setKeyboard(nextKeyboard);
        if(nextKeyboard==mSymbolsnumbers){
            isnumbersqwrty=true;
        }else {
            isnumbersqwrty=false;
        }
        }
        */
        mInputView.setKeyboard(nextKeyboard);
       mInputView.invalidateAllKeys();
        mSwipeDecoder = null;
        if (mPredict != null) {
            mPredict.preload(keyboardLang());
            mKeepSwipeAlternatives = false;
            scheduleSuggestionUpdate();
        }

    }

    ////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////
    @Override
    public View onCreateInputView() {
      //  toastM("oncreateinputview happen");
       // trimCache(this);
       /// Trial version

       // Calendar cal = Calendar.getInstance();
        //toastM(""+cal.get(Calendar.YEAR));
        /*
         if(cal.get(Calendar.YEAR)!=2017){
            toastM("لطفا برنامه را آپدیت نمایید. این ورژن دیگر معتبر نیست.");

            InputMethodManager imeManager = (InputMethodManager) getApplicationContext().getSystemService(INPUT_METHOD_SERVICE);
            // toastM("year: " + cal.getTime().getYear());
            imeManager.showInputMethodPicker();
           // trial=false;
            requestHideSelf(0);

        }
        */
        //trialall

        /*
        if (
                cal.get(Calendar.MONTH)!=9  ){
            toastM("زمان نسخه آزمایشی به پایان رسیده است. برای استفاده از کیبورد لطفا اقدام به خرید آن از بازار نمایید.");
            //  toastM("month: "+cal.getTime().getMonth());

            InputMethodManager imeManager = (InputMethodManager) getApplicationContext().getSystemService(INPUT_METHOD_SERVICE);

            imeManager.showInputMethodPicker();
            trialversionmonth10isend=true;
             Intent intent = new Intent(getApplicationContext(),
                  UpdatePage.class);

              intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
             startActivity(intent);
            requestHideSelf(0);


        }
        */
        //trialall
        /*
        if(isTimeAutomatic(getApplicationContext())){

        }else{
           // Toast.makeText(getApplicationContext(),"لطفا تنظیم اتوماتیک زمان را فعال نمایید و از تغییر زمان خودداری نمایید. نسخه آزمایشی تنها در صورت فعال بودن تنظیم اتوماتیک زمان کار میکند."
           // ,Toast.LENGTH_LONG).show();
          //  InputMethodManager imeManager = (InputMethodManager) getApplicationContext().getSystemService(INPUT_METHOD_SERVICE);
           // startActivity(
            //        new Intent(android.provider.Settings.ACTION_DATE_SETTINGS));
            Intent intent = new Intent(
                    android.provider.Settings.ACTION_DATE_SETTINGS);

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
           try {
               toastM("لطفا تیک زمان و تاریخ خودکار(اتوماتیک) را فعال نمایید.(Automatic date and time)");
               startActivity(intent);
           }catch (Exception ei){
               toastM("لطفا به صورت دستی، تیک اتوماتیک تایم را در بخش زمان و تاریخ گوشی خود فعال نمایید.");
           }

            //imeManager.showInputMethodPicker();
            //trial=false;
            requestHideSelf(0);
        }
        */








        tempint= (int) getResources().getDimension(R.dimen.longclickyadd);
        tempintfullscreen= (int) getResources().getDimension(R.dimen.fullscreenadd);


        if (preferenceKeyboard == null) {
            preferenceKeyboard=
                   getSharedPreferences("MySharedPreference", Context.MODE_PRIVATE);
                  }
        timewordtwofuction=preferenceKeyboard.getInt(getString(R.string.prefseekbartimewordtwofuction),300);

        correct_kurdi_v_abouve=preferenceKeyboard.
                getBoolean(getString(R.string.preflanguage_kurdi_vabouve), false);


         listS=
        preferenceKeyboard.getString(getString(R.string.pref_list_theme_selector),"1");
        language_kurdi_enable=
                preferenceKeyboard.getBoolean(getString(R.string.preflanguage_kurdi), true);
        language_farsi_enable=
                preferenceKeyboard.getBoolean(getString(R.string.preflanguage_farsi),true);


        View root = getLayoutInflater().inflate(R.layout.a_0_mf_theme_0_input, null);
        boolean isactiveimagebg=
                preferenceKeyboard.getBoolean(getString(R.string.prefimagefrbackground)
                        , false);
        if(isactiveimagebg) {
            //toastM(listS);
            root = getLayoutInflater().inflate(R.layout.a_0_mf_theme_0_input, null);
        }else if(listS.equals("1")) {
            //toastM(listS);
             root = getLayoutInflater().inflate(R.layout.a_1_mf_theme_1_input, null);
        }else if(listS.equals("1x")) {
            //toastM(listS);
            root = getLayoutInflater().inflate(R.layout.a_1x_mf_theme_1x_input, null);
        }else if(listS.equals("1xx")) {
            //toastM(listS);
            root = getLayoutInflater().inflate(R.layout.a_1xx_mf_theme_1xx_input, null);
        }else if (listS.equals("2")){
            //toastM(listS);
             root = getLayoutInflater().inflate(R.layout.a_2_mf_theme_2_input, null);

        }else if (listS.equals("2x")){
            //toastM(listS);
            root = getLayoutInflater().inflate(R.layout.a_2x_mf_theme_2x_input, null);

        }else if (listS.equals("2xx")){
            //toastM(listS);
            root = getLayoutInflater().inflate(R.layout.a_2xx_mf_theme_2xx_input, null);

        }else if (listS.equals("3")){
            //toastM(listS);
            root = getLayoutInflater().inflate(R.layout.a_3_mf_theme_3_input, null);

        }else if (listS.equals("3x")){
            //toastM(listS);
            root = getLayoutInflater().inflate(R.layout.a_3x_mf_theme_3x_input, null);

        }else if (listS.equals("3xx")){
            //toastM(listS);
            root = getLayoutInflater().inflate(R.layout.a_3xx_mf_theme_3xx_input, null);

        }else if (listS.equals("4")){
            //toastM(listS);
            root = getLayoutInflater().inflate(R.layout.a_4_mf_theme_4_input, null);

        }else if (listS.equals("5")){
            //toastM(listS);
            root = getLayoutInflater().inflate(R.layout.a_5_mf_theme_5_input, null);

        }else if (listS.equals("5x")){
            //toastM(listS);
            root = getLayoutInflater().inflate(R.layout.a_5x_mf_theme_5x_input, null);

        }else if (listS.equals("5xx")){
            //toastM(listS);
            root = getLayoutInflater().inflate(R.layout.a_5xx_mf_theme_5xx_input, null);

        }else if (listS.equals("6")){
            //toastM(listS);
            root = getLayoutInflater().inflate(R.layout.a_6_mf_theme_6_input, null);

        }else if (listS.equals("6x")){
            //toastM(listS);
            root = getLayoutInflater().inflate(R.layout.a_6x_mf_theme_6x_input, null);

        }else if (listS.equals("6xx")){
            //toastM(listS);
            root = getLayoutInflater().inflate(R.layout.a_6xx_mf_theme_6xx_input, null);

        }else if (listS.equals("7")){
            //toastM(listS);
            root = getLayoutInflater().inflate(R.layout.a_7_mf_theme_7_input, null);

        }else if (listS.equals("7x")){
            //toastM(listS);
            root = getLayoutInflater().inflate(R.layout.a_7x_mf_theme_7x_input, null);

        }else if (listS.equals("7xx")){
            //toastM(listS);
            root = getLayoutInflater().inflate(R.layout.a_7xx_mf_theme_7xx_input, null);

        }else if (listS.equals("8")){
            //toastM(listS);
            root = getLayoutInflater().inflate(R.layout.a_8_mf_theme_8_input, null);

        }else if (listS.equals("8x")){
            //toastM(listS);
            root = getLayoutInflater().inflate(R.layout.a_8x_mf_theme_8x_input, null);

        }else if (listS.equals("8xx")){
            //toastM(listS);
            root = getLayoutInflater().inflate(R.layout.a_8xx_mf_theme_8xx_input, null);

        }else if (listS.equals("9x")){
            //toastM(listS);
            root = getLayoutInflater().inflate(R.layout.a_9x_mf_theme_9x_input, null);

        }else if (listS.equals("9xx")){
            //toastM(listS);
            root = getLayoutInflater().inflate(R.layout.a_9xx_mf_theme_9xx_input, null);

        }else if (listS.equals("10")){
            //toastM(listS);
            root = getLayoutInflater().inflate(R.layout.a_10_mf_theme_10_input, null);

        }else if (listS.equals("10x")){
            //toastM(listS);
            root = getLayoutInflater().inflate(R.layout.a_10x_mf_theme_10x_input, null);

        }else if (listS.equals("10xx")){
            //toastM(listS);
            root = getLayoutInflater().inflate(R.layout.a_10xx_mf_theme_10xx_input, null);

        }else if (listS.equals("11")){
            //toastM(listS);
            root = getLayoutInflater().inflate(R.layout.a_11_mf_theme_11_input, null);

        }else if (listS.equals("11x")){
            //toastM(listS);
            root = getLayoutInflater().inflate(R.layout.a_11x_mf_theme_11x_input, null);

        }else if (listS.equals("11xx")){
            //toastM(listS);
            root = getLayoutInflater().inflate(R.layout.a_11xx_mf_theme_11xx_input, null);

        }else if (listS.equals("12")){
            //toastM(listS);
            root = getLayoutInflater().inflate(R.layout.a_12_mf_theme_12_input, null);

        }else if (listS.equals("13")){
            //toastM(listS);
            root = getLayoutInflater().inflate(R.layout.a_13_mf_theme_13_input, null);

        }else if (listS.equals("14")){
            //toastM(listS);
            root = getLayoutInflater().inflate(R.layout.a_14_mf_theme_14_input, null);

        }else if (listS.equals("15")){
            //toastM(listS);
            root = getLayoutInflater().inflate(R.layout.a_15_mf_theme_15_input, null);

        }else if (listS.equals("16")){
            //toastM(listS);
            root = getLayoutInflater().inflate(R.layout.a_16_mf_theme_16_input, null);

        }else if (listS.equals("17")){
            //toastM(listS);
            root = getLayoutInflater().inflate(R.layout.a_17_mf_theme_17_input, null);

        }else if (listS.equals("18")){
            //toastM(listS);
            root = getLayoutInflater().inflate(R.layout.a_18_mf_theme_18_input, null);

        }else if (listS.equals("19")){
            //toastM(listS);
            root = getLayoutInflater().inflate(R.layout.a_19_mf_theme_19_input, null);

        }else if (listS.equals("20")){
            //toastM(listS);
            root = getLayoutInflater().inflate(R.layout.a_20_mf_theme_20_input, null);

        }else if (listS.equals("21")){
            //toastM(listS);
            root = getLayoutInflater().inflate(R.layout.a_21_mf_theme_21_input, null);

        }else if (listS.equals("22")){
            //toastM(listS);
            root = getLayoutInflater().inflate(R.layout.a_22_mf_theme_22_input, null);

        }else if (listS.equals("23")){
            //toastM(listS);
            root = getLayoutInflater().inflate(R.layout.a_23_mf_theme_23_input, null);

        }else if (listS.equals("24")){
            //toastM(listS);
            root = getLayoutInflater().inflate(R.layout.a_24_mf_theme_24_input, null);

        }else if (listS.equals("25")){
            //toastM(listS);
            root = getLayoutInflater().inflate(R.layout.a_25_mf_theme_25_input, null);

        }else if (listS.equals("26")){
            //toastM(listS);
            root = getLayoutInflater().inflate(R.layout.a_26_mf_theme_26_input, null);

        }else if (listS.equals("27")){
            //toastM(listS);
            root = getLayoutInflater().inflate(R.layout.a_27_mf_theme_27_input, null);

        }else if (listS.equals("28")){
            //toastM(listS);
            root = getLayoutInflater().inflate(R.layout.a_28_mf_theme_28_input, null);

        }else if (listS.equals("29")){
            //toastM(listS);
            root = getLayoutInflater().inflate(R.layout.a_29_mf_theme_29_input, null);

        }else if (listS.equals("30")){
            //toastM(listS);
            root = getLayoutInflater().inflate(R.layout.a_30_mf_theme_30_input, null);

        }else if (listS.equals("31")){
            //toastM(listS);
            root = getLayoutInflater().inflate(R.layout.a_31_mf_theme_31_input, null);

        }else if (listS.equals("32")){
            //toastM(listS);
            root = getLayoutInflater().inflate(R.layout.a_32_mf_theme_32_input, null);

        }else if (listS.equals("33")){
            //toastM(listS);
            root = getLayoutInflater().inflate(R.layout.a_33_mf_theme_33_input, null);

        }else if (listS.equals("34")){
            //toastM(listS);
            root = getLayoutInflater().inflate(R.layout.a_34_mf_theme_34_input, null);

        }
        mInputView = (KKeyboardView) root.findViewById(R.id.keyboard);


         ivKeyboard = (ImageView) root.findViewById(R.id.ivKeyboard);
         ivKeyboardSelectall = (ImageView) root.findViewById(R.id.ivKeyboardselectall);
        ImageView ivKeyboardCopy = (ImageView) root.findViewById(R.id.ivKeyboardcopy);
        final ImageView ivKeyboardBack = (ImageView) root.findViewById(R.id.ivKeyboardback);
        ImageView ivKeyboardForward = (ImageView) root.findViewById(R.id.ivKeyboardforward);
        ImageView ivKeyboardCut = (ImageView) root.findViewById(R.id.ivKeyboardcut);
        ImageView ivKeyboardPaste = (ImageView) root.findViewById(R.id.ivKeyboardpaste);
        ImageView ivKeyboardSetting = (ImageView) root.findViewById(R.id.ivKeyboardsetting);
        boolean booleanremoveivkeyboard=  preferenceKeyboard.getBoolean(getString(R.string.preftoolbarsivkeyboard),  false);
        if(booleanremoveivkeyboard){
            ivKeyboard.setVisibility(View.GONE);
        }
        //preftoolbarsivkeyboardsetting1002
        boolean booleanremoveivkeyboardsetting=  preferenceKeyboard.getBoolean(getString(R.string.preftoolbarsivkeyboardsetting),  false);
        if(booleanremoveivkeyboardsetting){
            ivKeyboardSetting.setVisibility(View.GONE);
        }



        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);

       final EmojiconsPopup popupWindow = new EmojiconsPopup(root,getApplicationContext());
        popupWindow.setSizeForSoftKeyboard();
        final float popUpheight = getResources().getDimension(
                R.dimen.keyboard_height);
        popupWindow.setSize(WindowManager.LayoutParams.MATCH_PARENT, (int) popUpheight);
        widthkeyboard=WindowManager.LayoutParams.MATCH_PARENT;
        ivKeyboard.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                if (popupWindow.isShowing()) {

                    popupWindow.dismiss();


                } else {

                    //popupWindow = new EmojiconsPopup(popupView, this);
                    // popupWindow.setSize(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);


                    int screenHeight = mInputView.getRootView().getHeight();
                    int bottomHeight = (int) getResources().getDimension(R.dimen.key_height_firstrow);
                    int equalizer = (int) getResources().getDimension(R.dimen.hiegh_for_emoji);
                    int defHeight = (screenHeight - 2 * bottomHeight - equalizer);

                    int locationforwhatsup[] = new int[2];
                    ivKeyboardSelectall.getLocationOnScreen(locationforwhatsup);
                    int bottomHeight3forwhatsup = locationforwhatsup[1];

                    int height2 = (int) (locationforwhatsup[1] +
                            ivKeyboardSelectall.getMeasuredHeight());

                    int equalizerforwhatsup = (int) getResources().getDimension(R.dimen.hiegh_for_emoji_whatsup);

                    int defHeight2 = Math.min(defHeight, height2 - equalizerforwhatsup);
                    if (isPortrait()) {
                        defHeight2 -= getResources().getDimension(
                                R.dimen.key_height_firstrow
                        );
                    }
                    widthkeyboard=defHeight2;
                    popupWindow.setSize(WindowManager.LayoutParams.MATCH_PARENT,
                            defHeight2);
                    popupWindow.showAtLocation(mInputView.getRootView(), Gravity.BOTTOM, 0, 0);


                    // Bring soft keyboard up : NOT WORKING
                    // final
                    InputMethodManager mInputMethodManager = (InputMethodManager) getBaseContext()
                            .getSystemService(Context.INPUT_METHOD_SERVICE);

                    // mInputMethodManager.showSoftInput(root, 0);

                    mInputMethodManager.showSoftInput(mInputView, 0);

                    popupWindow.setOnSoftKeyboardOpenCloseListener(new OnSoftKeyboardOpenCloseListener() {

                        @Override
                        public void onKeyboardOpen(int keyBoardHeight) {

                        }

                        @Override
                        public void onKeyboardClose() {
                            if (popupWindow.isShowing())
                                popupWindow.dismiss();
                        }
                    });

                    popupWindow.setOnEmojiconClickedListener(new OnEmojiconClickedListener() {

                        @Override
                        public void onEmojiconClicked(Emojicon emojicon) {
                            mComposing.append(emojicon.getEmoji());
                            commitTyped(getCurrentInputConnection());

                            //  customToast("" + emojicon.getEmoji());
                        }
                    });

                    popupWindow.setOnEmojiconBackspaceClickedListener(new OnEmojiconBackspaceClickedListener() {

                        @Override
                        public void onEmojiconBackspaceClicked(View v) {
                            KeyEvent event = new KeyEvent(0, 0, 0, KeyEvent.KEYCODE_DEL, 0, 0, 0, 0, KeyEvent.KEYCODE_ENDCALL);
                            //customToast(" " + event);
                            handleBackspace();

                        }
                    });
                    popupWindow.setOnEmojiconCloseClickedListener(new OnEmojiconCloseClickedListener() {

                        @Override
                        public void onEmojiconCloseClicked(View v) {

                            if(popupWindow.isShowing()) {

                                popupWindow.dismiss();
                            }


                        }
                    });


                }


            }

        });




        ivKeyboardSelectall.setOnClickListener(new OnClickListener() {
                                                   @Override
                                                   public void onClick(View view) {
                                                        //getOnKeyboardActionListener()
                                                       commitTyped(getCurrentInputConnection());
                                                       onKey(KKeyboardView.KEYCODE_SELECTALL,null);
                                                     //  onKey(KKeyboardView.KEYCODE_SELECTALL,null);


                                                   }

                                               });

        ivKeyboardCopy.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                onKey(KKeyboardView.KEYCODE_COPY,null);

            }
            
        });

        ivKeyboardBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mInputView.getKeyboard() == mQwertyKeyboard) {
                    onKey(KKeyboardView.KEYCODE_CURSOR_B, null);
                } else {
                    onKey(KKeyboardView.KEYCODE_CURSOR_F, null);

                }


            }
        });


      /*  ivKeyboardBack.setOnTouchListener(

                new View.OnTouchListener() {


                    private Handler mHandler;

                    @Override public boolean onTouch(View v, MotionEvent event) {

                        switch(event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                if (mHandler != null) return true;
                                mHandler = new Handler();
                                isdowncursorbacl=true;
                               // mHandler.postDelayed(mAction, 500);
                                onKey(KKeyboardView.KEYCODE_CURSOR_B,null);
                                break;
                            case MotionEvent.ACTION_UP:
                                if (mHandler == null) return true;
                                isdowncursorbacl=false;
                               // mHandler.removeCallbacks(mAction);
                                mHandler = null;
                                break;
                        }
                        return false;
                    }

                    Runnable mAction = new Runnable() {
                        @Override public void run() {
                            if (mHandler != null&&isdowncursorbacl){
                                onKey(KKeyboardView.KEYCODE_CURSOR_B,null);
                            }
                            mHandler.postDelayed(this, 500);
                        }
                    };

                }

        );    */
        ivKeyboardForward.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mInputView.getKeyboard()==mQwertyKeyboard){
                    onKey(KKeyboardView.KEYCODE_CURSOR_F,null);
                }else{
                    onKey(KKeyboardView.KEYCODE_CURSOR_B,null);

                }

            }
        });
        ivKeyboardCut.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                onKey(KKeyboardView.KEYCODE_CUT,null);

            }
        });
        ivKeyboardPaste.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                onKey(KKeyboardView.KEYCODE_PASTE,null);


            }
        });
        final myPopupWindow popupWindowsetting = new myPopupWindow(root,getApplicationContext()
        ,this);

        popupWindowsetting.setSizeForSoftKeyboard();
        final float popUpheight2 = getResources().getDimension(
                R.dimen.keyboard_height);
        popupWindowsetting.setSize(WindowManager.LayoutParams.MATCH_PARENT,(int) popUpheight2);

        ivKeyboardSetting.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

            //  option
                PopupWindow popupWindow1=
                        new PopupWindow();
                LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                final View viewtemp = inflater.inflate(R.layout.layout_contacts, null, false);

                final ListView listview = (ListView) viewtemp.findViewById(R.id.listView);

                popupWindow1.setContentView(listview);
                if(popupWindow1.isShowing()){

                    popupWindow1.dismiss();


                }

                if(popupWindowsetting.isShowing()){

                    popupWindowsetting.dismiss();


                }
                else {


                    int screenHeight = mInputView.getRootView().getHeight();
                    int bottomHeight = (int) getResources().getDimension(R.dimen.key_height_firstrow);
                    int equalizer = (int) getResources().getDimension(R.dimen.hiegh_for_emoji);
                    int defHeight = (screenHeight - 2 * bottomHeight - equalizer);

                    int locationforwhatsup[] = new int[2];
                    ivKeyboardSelectall.getLocationOnScreen(locationforwhatsup);
                    int bottomHeight3forwhatsup = locationforwhatsup[1];

                    int height2 = (int) (locationforwhatsup[1] +
                            ivKeyboardSelectall.getMeasuredHeight());

                    int equalizerforwhatsup = (int) getResources().getDimension(R.dimen.hiegh_for_emoji_whatsup);

                    int defHeight2 = Math.min(defHeight, height2 - equalizerforwhatsup);
                    if(isPortrait()){
                        defHeight2-=getResources().getDimension(
                                R.dimen.key_height_firstrow
                        );
                    }
                    popupWindowsetting.setSize(WindowManager.LayoutParams.MATCH_PARENT,
                            defHeight2);
                    popupWindowsetting.showAtLocation(mInputView.getRootView(), Gravity.BOTTOM, 0, 0);


                    InputMethodManager mInputMethodManager = (InputMethodManager) getBaseContext()
                            .getSystemService(Context.INPUT_METHOD_SERVICE);

                    // mInputMethodManager.showSoftInput(root, 0);

                    mInputMethodManager.showSoftInput(mInputView, 0);

                    popupWindowsetting.setOnSoftKeyboardOpenCloseListener(
                            new myPopupWindow.OnSoftKeyboardOpenCloseListener() {

                                @Override
                                public void onKeyboardOpen(int keyBoardHeight) {

                                }

                                @Override
                                public void onKeyboardClose() {
                                    if (popupWindowsetting.isShowing())
                                        popupWindowsetting.dismiss();
                                }
                            });


                }



            }
        });
        ivKeyboardSetting.setOnLongClickListener(
                new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        Intent i1 = new Intent(getBaseContext(),Main2Activity.class);
                        i1.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE); // Show user only contacts w/ phone numbers
                        i1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i1); return true;
                    }
                }

        );

        mInputView.setOnKeyboardActionListener(this);
        if(mCurKeyboard!=null){
            //setKKeyboard(mCurKeyboard);
            if(prelanguage.equals("en")) {
                setKKeyboard(mQwertyKeyboard);
            }else if (prelanguage.equals("fa")){
                setKKeyboard(mfarsiKeyboard);
            }else if (prelanguage.equals("kr")){
                setKKeyboard(mkurdishKeyboard);
            }else {
                setKKeyboard(mQwertyKeyboard);
            }

        }
        else {
            setKKeyboard(mQwertyKeyboard);
        }

        View custom =getLayoutInflater().inflate(R.layout.preview_long_click,null);
        mPreviewText= (TextView) custom.findViewById(R.id.textviewLongClick);

        mPreviewPopup = new PopupWindow(custom);
        mPreviewPopup.setOutsideTouchable(true);
        //popupcontacts.setFocusable(true);
        // Removes default background.
        //popupcontacts.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mPreviewPopup.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // popupWindow.setFocusable(true);
        //mPreviewPopup.setContentView(custom);
       // mPreviewPopup.setOutsideTouchable(true);

       // mPreviewPopup.showAsDropDown(root);
       // mPreviewPopup.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);


        mInputView.setOnTouchListener(new View.OnTouchListener() {
            private float currX;
            private float currY;
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                String direction="nodirection";

                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

                    handlehapticfeedback();
                    List<Keyboard.Key> keys = mInputView.getKeyboard().getKeys();
                   // toastM("down");
                    for(Keyboard.Key key: keys) {

                        if(key.isInside((int)motionEvent.getX(),(int)motionEvent.getY())){
                            if(key.label!=null){
                                mPreviewPopup.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
                                mPreviewPopup.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
                                mPreviewText.setText("");
                                if(key.label.equals("گ")) {
                                    if(language.equals("fa")) {
                                        mPreviewText.setText("ء");
                                    }
                                }else if(key.label.equals("ک")){
                                    mPreviewText.setText("\u0626");

                                }else if(key.label.equals("د")){
                                    if(language.equals("fa")) {
                                        mPreviewText.setText("ذ");
                                    }

                                }else if(key.label.equals("ر")){
                                    if(!language.equals("fa")) {
                                        mPreviewText.setText("\u0695");
                                    }

                                }else if(key.label.equals("ز")){
                                    mPreviewText.setText("ژ");

                                }else if(key.label.equals("ط")){
                                    mPreviewText.setText("ظ");

                                }else if(key.label.equals("ا")){
                                    if(language.equals("fa")) {
                                        mPreviewText.setText("آ");
                                    }

                                }else if(key.label.equals("ل")){
                                    if(!language.equals("fa")) {
                                        mPreviewText.setText("\u06B5");
                                    }

                                }else if(key.label.equals("و")){
                                    if(language.equals("fa")) {
                                        mPreviewText.setText("\u0624");
                                    }

                                }else if(key.label.equals("ع")){
                                    mPreviewText.setText("غ");

                                }else if(key.label.equals("ف")){
                                    if(!language.equals("fa")) {
                                        mPreviewText.setText("\u06A4");
                                    }

                                }else if(key.label.equals("ص")){
                                    mPreviewText.setText("ض");

                                }else if(key.label.equals("\u06C6")){
                                    mPreviewText.setText("\u06CA");

                                }
                                keyx=key.x;
                               // keyx= (int) motionEvent.getRawX();
                                keywidth=key.width;
                                keywidth=key.width;
                                keyy=key.y;
                               // keyy=(int) motionEvent.getRawY();
                                keyheight=key.height;

                            }
                            if(key.codes[0]==32){

                                  //  Toast.makeText(getApplicationContext(), "space key a", Toast.LENGTH_SHORT).show();
                                spaceH=key.height;
                                spaceW=key.width;

                                mIsDown=true;

                            }else{
                                mIsDown=false;
                            }
                        }

                    }

                    oldx = motionEvent.getX();
                    oldy = motionEvent.getY();
                    x1=oldx;
                    y1=oldy;

                   // mInputView.closing(); // Close popup keyboard if it's showing
                }
                if(motionEvent.getAction()==MotionEvent.ACTION_OUTSIDE){
                  // toastM("out");
                  // mInputView.closing();
                    return true;
                }
                if(motionEvent.getAction()==MotionEvent.ACTION_MOVE) {
                   // toastM("move");
                  // if(mPreviewPopup.isShowing()){ mPreviewPopup.dismiss();}

                    if(mIsDown==true){
                        x2 = motionEvent.getX();
                        y2 = motionEvent.getY();
                       // Toast.makeText(getApplicationContext(),"x1= "+x1+"x2= "+x2, Toast.LENGTH_LONG).show();

                        dx = x2 - x1;
                        dy = y2 - y1;
                        if(Math.abs(dx)>Math.abs(dy)){
                          //  Toast.makeText(getApplicationContext(), "drag", Toast.LENGTH_SHORT).show();
                          if(Math.abs(dx)>spaceW/4){
                              spacemove=true;
                             // handleLanguageSwitch();
                              if(dx>0){
                                  spacemovetoright=true;
                              }else{
                                  spacemovetoright=false;
                              }

                                }else {
                              spacemove=false;
                          }

                        }


                    }




                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    //  mInputView.closing();
                    //   toastM("up");
                    //   mPreviewPopup.dismiss();
                   if(mPreviewPopup.isShowing()){
                       mPreviewPopup.dismiss();
                    }


                }
                    return false;
            }

        });


        ///////////////////
        /// listener to clipboardmanager


        ///////////////////
         rootTemp=root;
         ivTemp=ivKeyboard;
       //  changeIconShift(0);
        // changeIconspace();
        mInputView.setPreviewEnabled(false);
       try {
           toollayout = (LinearLayout) root.findViewById(R.id.layouttools);
       }catch (Exception ei){

       }
         if(toolskayout_enable){
             toollayout.setVisibility(View.VISIBLE);
         }else {
             toollayout.setVisibility(View.GONE);
            // toollayout.setVisibility(View.GONE);
           /*  toollayout.setBackgroundColor(
                     getResources().getColor(R.color.mycolor_Transparent)
             );
             toollayout.setAlpha((float) 0.01);
            */

         }
        if(toollayout!=null){
            if(toollayout.getVisibility()==View.VISIBLE){
              //  toollayout.setVisibility(View.GONE);
              //  toollayout.setAlpha((float) 0.20);
                toollayout.setVisibility(View.GONE);

                /*
                android.view.ViewGroup.
                        LayoutParams params = toollayout.getLayoutParams();

                params.height = 1;
                //params.width = 100;
                toollayout.setLayoutParams(params);
                */
            }else {
                toollayout.setVisibility(View.VISIBLE);
              /*  android.view.ViewGroup.
                        LayoutParams params = toollayout.getLayoutParams();

                params.height = (int)getResources().
                        getDimension(R.dimen.key_height_firstrow);
                //params.width = 100;
                toollayout.setLayoutParams(params);
                */
            }
        }
        readPredictionPrefs();
        installSuggestionStrip();
        mInputView.setSwipeListener(mSwipeListener);
        mSwipeDecoder = null;

        String pathimage=
        preferenceKeyboard.getString(getString(R.string.prefkeypathimage), "unknown");


        if((isactiveimagebg)&&(!pathimage.equals("unknown"))){
            File file = new File(pathimage);
            if(file.exists()) {
                //BitmapFactory.Options options = new BitmapFactory.Options();
                //options.inPreferredConfig=Bitmap.Config.RGB_565;
                //Bitmap bitmapImage = BitmapFactory.decodeFile(pathimage,options);

               // downsampled, RGB_565: a full-size camera photo used to crash the keyboard (out of memory)
               bitmap = ImagePickerActivity.decodeBackground(pathimage,
                       Math.max(getResources().getDisplayMetrics().widthPixels,
                               getResources().getDisplayMetrics().heightPixels));
                convertedbitmap = bitmap;

                 dr = new BitmapDrawable(getResources(), convertedbitmap);

                try {

                    mInputView.setBackgroundDrawable(dr);


                } catch (Exception ei) {
                    Toast.makeText(getApplicationContext(), ei.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }

        }


        //mInputView.setb
         return root;
    }
    ////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////
    @Override public View onCreateCandidatesView() {
        mCandidateView = new CandidateView(this);

        mCandidateView.setService(this);

        setCandidatesViewShown(false);



        return mCandidateView;
    }



    ////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////
    @Override
    public void onStartInput(EditorInfo attribute, boolean restarting) {

      //  Toast.makeText(getBaseContext(),"onStartInput",Toast.LENGTH_LONG).show();

       // super.onStartInput(attribute, restarting);
        // keyboard with no special features.
        super.onStartInput(attribute, restarting);
        //handleevent(attribute);
        // Reset our state.  We want to do this even if restarting, because
        // the underlying state of the text editor could have changed in any way.
        mComposing.setLength(0);


        if (!restarting) {
            // Clear shift states.
            mMetaState = 0;
        }

        mPredictionOn = false;
        mCompletionOn = false;
        mCompletions = null;

        // We are now going to initialize our state based on the type of
        // text being edited.
        switch (attribute.inputType & InputType.TYPE_MASK_CLASS) {
            case InputType.TYPE_CLASS_NUMBER:
                mCurKeyboard = mSymbolsnumbers;
                break;

            case InputType.TYPE_CLASS_DATETIME:
                // Numbers and dates default to the symbols keyboard, with
                // no extra features.
                mCurKeyboard = mSymbolsKeyboard;
                break;

            case InputType.TYPE_CLASS_PHONE:
                // Phones will also default to the symbols keyboard, though
                // often you will want to have a dedicated phone keyboard.
                mCurKeyboard = mSymbolsnumbers;
                break;

            case InputType.TYPE_CLASS_TEXT:
                // This is general text editing.  We will default to the
                // normal alphabetic keyboard, and assume that we should
                // be doing predictive text (showing candidates as the
                // user types).

               // mCurKeyboard = mQwertyKeyboard;
                setCurrentLanguage();
                if(mCurKeyboard==mSymbolsKeyboard||
                        mCurKeyboard==mSymbolsfarsiKeyboard) {
                    if(prelanguage.equals("en")){
                        mCurKeyboard=mQwertyKeyboard;
                    }else if(prelanguage.equals("kr")){
                        mCurKeyboard=mkurdishKeyboard;
                    }else if(prelanguage.equals("fa")){
                        mCurKeyboard=mfarsiKeyboard;
                    }
                }

            ;

               // mPredictionOn = true;

                // We now look for a few special variations of text that will
                // modify our behavior.
                int variation = attribute.inputType & InputType.TYPE_MASK_VARIATION;
                if (variation == InputType.TYPE_TEXT_VARIATION_PASSWORD ||
                        variation == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                    // Do not display predictions / what the user is typing
                    // when they are entering a password.
                   // mPredictionOn = false;
                }

                if (variation == InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
                        || variation == InputType.TYPE_TEXT_VARIATION_URI
                        || variation == InputType.TYPE_TEXT_VARIATION_FILTER) {
                    // Our predictions are not useful for e-mail addresses
                    // or URIs.
                    mPredictionOn = false;
                }

                if ((attribute.inputType & InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE) != 0) {
                    // If this is an auto-complete text view, then our predictions
                    // will not be shown and instead we will allow the editor
                    // to supply their own.  We only show the editor's
                    // candidates when in fullscreen mode, otherwise relying
                    // own it displaying its own UI.
                    mPredictionOn = false;
                    mCompletionOn = isFullscreenMode();
                }

                // We also want to look at the current state of the editor
                // to decide whether our alphabetic keyboard should start out
                // shifted.
               // updateShiftKeyState(attribute);
                break;

            default:
                // For all unknown input types, default to the alphabetic
                // keyboard with no special features.
               // mCurKeyboard = mQwertyKeyboard;
                setCurrentLanguage();
                if(mCurKeyboard==mSymbolsKeyboard||
                        mCurKeyboard==mSymbolsfarsiKeyboard) {
                    if(prelanguage.equals("en")){
                        mCurKeyboard=mQwertyKeyboard;
                    }else if(prelanguage.equals("kr")){
                        mCurKeyboard=mkurdishKeyboard;
                    }else if(prelanguage.equals("fa")){
                        mCurKeyboard=mfarsiKeyboard;
                    }
                }

              //  updateShiftKeyState(attribute);
        }

        // Update the label on the enter key, depending on what the application
        // says it will do.
       // if(attribute.imeOptions!=EditorInfo.IME_FLAG_NO_EXTRACT_UI)

      //  EditorInfo temp=new EditorInfo();
       // temp=attribute;
       // temp.imeOptions=EditorInfo.IME_FLAG_NO_EXTRACT_UI;
       mCurKeyboard.setImeOptions(getResources(), attribute);


    }
    ////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////


    @Override public void onFinishInput() {

       // Toast.makeText(getBaseContext(),"onFinishInput",Toast.LENGTH_LONG).show();

        super.onFinishInput();
        mKeepSwipeAlternatives = false;
        mSuggestHandler.removeCallbacks(mUpdateSuggestionsRunnable);
        if (mPredict != null) mPredict.saveAsync();
        InputConnection ic = getCurrentInputConnection();
        if (ic != null) {
            commitTyped(ic);
            ic.finishComposingText();

        }
       // trimCache(this);
        mComposing.setLength(0);





        // We only hide the candidates window when finishing input on
        // a particular editor, to avoid popping the underlying application
        // up and down if the user is entering text into the bottom of
        // its window.


       /// ????? ino man intoori kardam setCandidatesViewShown(false);



        if (mInputView != null) {
            mInputView.closing();
        }

    }

    ////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////


    @Override
    public void onStartInputView(EditorInfo attribute, boolean restarting) {

      // Toast.makeText(getBaseContext(),"onStartInputView",Toast.LENGTH_LONG).show();

        super.onStartInputView(attribute, restarting);
        readPredictionPrefs();
        mFieldAllowsPrediction = fieldAllowsPrediction(attribute);
        mKeepSwipeAlternatives = false;
        mJustAutoSpaced = false;
        mLastInserted = null;
       // handleevent(attribute);
        if (mCurKeyboard == null) {
            mCurKeyboard = mQwertyKeyboard;
        }
        setKKeyboard(mCurKeyboard);
       //Action Unspecified

       mInputView.closing();
      //  final InputMethodSubtype subtype = mInputMethodManager.getCurrentInputMethodSubtype();
      //  mInputView.setSubtypeOnSpaceKey(subtype);



        mCurKeyboard.setImeOptions(getResources(), attribute);
        boolean booleanchangetheme=  preferenceKeyboard.getBoolean(getString(R.string.prefchangethemboolean),  false);

        if(booleanchangetheme) {
            setInputView(onCreateInputView());
            //toastM("پس از انتخاب تم لطفا ");
        }
        mPredict.preload(keyboardLang());
        refreshStripVisibility();
        scheduleSuggestionUpdate();


    }


    ////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////


    /*

    @Override
    public void onCurrentInputMethodSubtypeChanged(InputMethodSubtype subtype) {
        mInputView.setSubtypeOnSpaceKey(subtype);
        switch(subtype.getLocale()) {
            case "fa_IR":
                setKKeyboard(mfarsiKeyboard);
                break;
            case "en_US":
                setKKeyboard(mQwertyKeyboard);
                break;
        };
    }
    */

    ////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////

    @Override
    public void onUpdateSelection(int oldSelStart, int oldSelEnd,
                                            int newSelStart, int newSelEnd,
                                            int candidatesStart, int candidatesEnd) {

      //  Toast.makeText(getBaseContext(),"onUpdateSelection",Toast.LENGTH_LONG).show();

        super.onUpdateSelection(oldSelStart, oldSelEnd, newSelStart, newSelEnd,
                candidatesStart, candidatesEnd);
        scheduleSuggestionUpdate();

        // If the current selection in the text view changes, we should
        // clear whatever candidate text we have.
        if (mComposing.length() > 0 && (newSelStart != candidatesEnd
                || newSelEnd != candidatesEnd)) {
            mComposing.setLength(0);

            InputConnection ic = getCurrentInputConnection();
            if (ic != null) {
                commitTyped(ic);
                ic.finishComposingText();
            }
        }
    }

    ////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////

    private boolean translateKeyDown(int keyCode, KeyEvent event) {

       // Toast.makeText(getBaseContext(),"translatekeydown",Toast.LENGTH_LONG).show();

        mMetaState = MetaKeyKeyListener.handleKeyDown(mMetaState,
                keyCode, event);
        int c = event.getUnicodeChar(MetaKeyKeyListener.getMetaState(mMetaState));
        mMetaState = MetaKeyKeyListener.adjustMetaAfterKeypress(mMetaState);
        InputConnection ic = getCurrentInputConnection();
        if (c == 0 || ic == null) {
            return false;
        }

        boolean dead = false;

        if ((c & KeyCharacterMap.COMBINING_ACCENT) != 0) {
            dead = true;
            c = c & KeyCharacterMap.COMBINING_ACCENT_MASK;
        }

        if (mComposing.length() > 0) {
            char accent = mComposing.charAt(mComposing.length() -1 );
            int composed = KeyEvent.getDeadChar(accent, c);

            if (composed != 0) {
                c = composed;
                mComposing.setLength(mComposing.length()-1);
            }
        }

        onKey(c, null);

        return true;
    }

    ////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

      //  Toast.makeText(getApplicationContext(),"onkeydown",Toast.LENGTH_LONG).show();


        switch (keyCode) {

            case KeyEvent.KEYCODE_BACK:

                // The InputMethodService already takes care of the back
                // key for us, to dismiss the input method if it is shown.
                // However, our keyboard could be showing a pop-up window
                // that back should dismiss, so we first allow it to do that.
                if (event.getRepeatCount() == 0 && mInputView != null) {
                    if (mInputView.handleBack()) {
                        return true;
                    }
                }
                break;

            case KeyEvent.KEYCODE_DEL:
                // Special handling of the delete key: if we currently are
                // composing text for the user, we want to modify that instead
                // of let the application to the delete itself.
                if (mComposing.length() > 0) {
                    onKey(Keyboard.KEYCODE_DELETE, null);
                    return true;
                }
                break;

            case KeyEvent.KEYCODE_ENTER:

                // Let the underlying text editor always handle these.
              // Toast.makeText(getBaseContext(),"Enter",Toast.LENGTH_SHORT).show();

                return false;


            default:

                // For all other keys, if we want to do transformations on
                // text being entered with a hard keyboard, we need to process
                // it and do the appropriate action.
                if (PROCESS_HARD_KEYS) {
                    if (keyCode == KeyEvent.KEYCODE_SPACE
                            && (event.getMetaState()&KeyEvent.META_ALT_ON) != 0) {
                        // A silly example: in our input method, Alt+Space
                        // is a shortcut for 'android' in lower case.
                        InputConnection ic = getCurrentInputConnection();
                        if (ic != null) {
                            // First, tell the editor that it is no longer in the
                            // shift state, since we are consuming this.
                            ic.clearMetaKeyStates(KeyEvent.META_ALT_ON);
                            keyDownUp(KeyEvent.KEYCODE_A);
                            keyDownUp(KeyEvent.KEYCODE_N);
                            keyDownUp(KeyEvent.KEYCODE_D);
                            keyDownUp(KeyEvent.KEYCODE_R);
                            keyDownUp(KeyEvent.KEYCODE_O);
                            keyDownUp(KeyEvent.KEYCODE_I);
                            keyDownUp(KeyEvent.KEYCODE_D);
                            // And we consume this event.
                            return true;
                        }
                    }
                    if (mPredictionOn && translateKeyDown(keyCode, event)) {
                        return true;
                    }
                }
        }


        return super.onKeyDown(keyCode, event);
    }

    ////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

     //   Toast.makeText(getBaseContext(),"onkeyup",Toast.LENGTH_LONG).show();

        if (PROCESS_HARD_KEYS) {
            if (mPredictionOn) {
                mMetaState = MetaKeyKeyListener.handleKeyUp(mMetaState,
                        keyCode, event);
            }
        }



        return super.onKeyUp(keyCode, event);

    }
    ////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////

    private void keyDownUp(int keyEventCode) {


        getCurrentInputConnection().sendKeyEvent(
                new KeyEvent(KeyEvent.ACTION_DOWN, keyEventCode));
        getCurrentInputConnection().sendKeyEvent(
                new KeyEvent(KeyEvent.ACTION_UP, keyEventCode));
    }
    ////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////
    private void commitTyped(InputConnection inputConnection) {

     //   Toast.makeText(getBaseContext(),"committyped",Toast.LENGTH_LONG).show();

        if (mComposing.length() > 0) {
            inputConnection.commitText(mComposing, mComposing.length());
            mComposing.setLength(0);

        }
    }

    ////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////
    private void updateShiftKeyState(EditorInfo attr) {


        //  Toast.makeText(getBaseContext(),"updateshiftstat",Toast.LENGTH_LONG).show();

      /*  if (attr != null
                && mInputView != null && mQwertyKeyboard == mInputView.getKeyboard()) {
            int caps = 0;
            EditorInfo ei = getCurrentInputEditorInfo();
            if (ei != null && ei.inputType != InputType.TYPE_NULL) {
                caps = getCurrentInputConnection().getCursorCapsMode(attr.inputType);
            }
            mInputView.setShifted(mCapsLock || caps != 0);
        }*/

    }
    ///////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////
    private void updateShiftKeyState2(Keyboard nextkeyboard2) {

        if (mInputView != null && mQwertyKeyboard == nextkeyboard2) {
            if(mInputView.isShifted()){
                if(mCapsLock==false){
                    mInputView.setShifted(false);
                }
            }

        }

    }

    ////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////

    private boolean isAlphabet(int code) {

      //  Toast.makeText(getBaseContext(),"isAlphabet",Toast.LENGTH_LONG).show();

        if (Character.isLetter(code)) {
            return true;
        } else {
            return false;
        }
    }
    ////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////


    private void sendKey(int keyCode) {



        switch (keyCode) {
            case '\n':
                //keyDownUp(KeyEvent.KEYCODE_ENTER);
                final int options = this.getCurrentInputEditorInfo().imeOptions;
               // final int actionId = options & EditorInfo.IME_MASK_ACTION;
                final int actionId =options&(EditorInfo.IME_MASK_ACTION
                        |EditorInfo.IME_FLAG_NO_ENTER_ACTION);
                switch (  actionId) {
                        case EditorInfo.IME_ACTION_SEARCH:
                            sendDefaultEditorAction(true);
                     //     toastM("search");
                        break;
                    case EditorInfo.IME_ACTION_GO:
                        sendDefaultEditorAction(true);
                    //  toastM("go");
                        break;
                    case EditorInfo.IME_ACTION_SEND:
                        sendDefaultEditorAction(true);
                        getCurrentInputConnection().sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                        getCurrentInputConnection().sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_ENTER));

                      //  toastM("send");
                        break;
                    case EditorInfo.IME_ACTION_DONE:

                      // handledone();
                        /*
                        if(( options)==30198994) {
                            sendDefaultEditorAction(true);
                            getCurrentInputConnection().sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));

                            toastM("Imeoptions is: 301" );
                        }else {

                            keyDownUp(KeyEvent.KEYCODE_ENTER);
                        }*/
                       // toastM("Imeoptions is:" +actionId);
                        //getCurrentInputConnection().sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                        //getCurrentInputConnection().sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_ENTER));


                        //getCurrentInputConnection().sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                        sendDefaultEditorAction(true);
                        //getCurrentInputConnection().sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_ENTER));

                       // toastM("done");
                       break;
                    case EditorInfo.IME_ACTION_NEXT:
                        sendDefaultEditorAction(true);
                        getCurrentInputConnection().sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));

                       // toastM("next");
                        break;

                    case EditorInfo.IME_ACTION_PREVIOUS:
                        sendDefaultEditorAction(true);
                        getCurrentInputConnection().sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));

                      //  toastM("previse");
                        break;

                    case EditorInfo.IME_ACTION_NONE:
                        sendDefaultEditorAction(true);
                        getCurrentInputConnection().sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));

                      //  toastM("none");
                        break;
                    case EditorInfo.IME_ACTION_UNSPECIFIED:
                        sendDefaultEditorAction(true);

                     // toastM("none");
                        break;



                    default:
                     // toastM("default");

                      getCurrentInputConnection().sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                 }

                  break;


            default:
               // toastM("spacedefault");
                if(keyCode==' '){
                toastM("space");
                onKey(8203,null);
            }
                if (keyCode >= '0' && keyCode <= '9') {
                    toastM("0---9");
                    keyDownUp(keyCode - '0' + KeyEvent.KEYCODE_0);
                } else {
                    toastM("separator");
                    commitTyped(getCurrentInputConnection());
                    getCurrentInputConnection().commitText(String.valueOf((char) keyCode), 1);
                }

        }
    }


    
    ////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////


    public void onKey(int primaryCode, int[] keyCodes) {
        boolean wasAutoSpaced = mJustAutoSpaced;
        mJustAutoSpaced = false;
        mKeepSwipeAlternatives = false;
        if (primaryCode > 0 && !TextNormalizer.isWordChar((char) primaryCode)) {
            if (wasAutoSpaced && primaryCode == 32 && !spacemove) {
                // the space is already there (added after a swipe / suggestion)
                InputConnection icSpace = getCurrentInputConnection();
                CharSequence last = icSpace != null ? icSpace.getTextBeforeCursor(1, 0) : null;
                if (last != null && " ".contentEquals(last)) {
                    scheduleSuggestionUpdate();
                    return;
                }
            }
            learnWordBeforeCursor();
            if (wasAutoSpaced) {
                mJustAutoSpaced = true;
                removeAutoSpaceBeforePunctuation(primaryCode);
                mJustAutoSpaced = false;
            }
        }

        boolean soundb=  preferenceKeyboard.getBoolean(getString(R.string.prefsound),  true);
        int volsound=preferenceKeyboard.getInt(getString(R.string.prefseekbarsound),0);
        if(soundb) {
            // volsound /= 50;
            // volsound -= 1;

            //  toastM("sound: "+volsound);
            // float volume=(float)volsound;
            float volume=volsound/10.0f;
            playClick(primaryCode,volume);

        }

        InputConnection ic = getCurrentInputConnection();
        // used by the cursor / select / copy keys; null when the editor does not support it
        ExtractedText et = ic.getExtractedText(new ExtractedTextRequest(), 0);
        if (et == null) et = new ExtractedText();



        if (primaryCode == KKeyboardView.KEYCODE_OPTIONS) {
            Intent i = new Intent(this, ImePreferences.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

           // startActivity(i);
            try {
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Toast toast = Toast.makeText(getBaseContext(), "ورود به تنظیمات", Toast.LENGTH_SHORT);
                toast.show();
                startActivity(i);
            } catch (Exception e){
                Toast toast = Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT);
                toast.show();
            }


        }else if(primaryCode=='\n') {
            sendKey(primaryCode);
        }
       else if (primaryCode == Keyboard.KEYCODE_DELETE) {
            handleBackspace();
        } else if (primaryCode == Keyboard.KEYCODE_SHIFT) {

            handleShift();

        }
        else if (primaryCode == Keyboard.KEYCODE_CANCEL)
        {
            tempint= (int) getResources().getDimension(R.dimen.longclickyadd);
            tempintfullscreen= (int) getResources().getDimension(R.dimen.fullscreenadd);

            toolskayout_enable=!toolskayout_enable;
            if(toollayout!=null){
                if(toollayout.getVisibility()==View.VISIBLE){
                  //  toollayout.setAlpha((float) 0.01);
                  //  toollayout.setVisibility(View.INVISIBLE);
                   toollayout.setVisibility(View.GONE);

                   // toollayout.setAlpha((float) 0.01);


                   /* android.view.ViewGroup.
                    LayoutParams params = toollayout.getLayoutParams();

                    params.height = 1;
                    //params.width = 100;
                    toollayout.setLayoutParams(params);
                    */
                }else {
                    toollayout.setVisibility(View.VISIBLE);
                    //toollayout.setAlpha((float) 1);
                   /* android.view.ViewGroup.
                    LayoutParams params = toollayout.getLayoutParams();

                    params.height = (int)getResources().
                            getDimension(R.dimen.key_height_firstrow);
                    //params.width = 100;
                    toollayout.setLayoutParams(params);
                    */
                }
            }
            refreshStripVisibility();
           // handleClose();


            return;
        } else if (primaryCode == KKeyboardView.KEYCODE_LANGUAGE_SWITCH) {
            //handleLanguageSwitch();
            return;
        } else if (primaryCode == Keyboard.KEYCODE_MODE_CHANGE
                && mInputView != null) {
            Keyboard current = mInputView.getKeyboard();
            /*
            if (current != mfarsiKeyboard && current != mQwertyKeyboard
                    &&current != mkurdishKeyboard ) {
                //setKKeyboard(mQwertyKeyboard);
                if( prelanguage.equals("en")){
                    setKKeyboard(mQwertyKeyboard);
                    language="en";
                }else if( prelanguage.equals("fa")){
                    setKKeyboard(mfarsiKeyboard);
                    language="fa";

                }else {
                    setKKeyboard(mkurdishKeyboard);
                    language="kr";

                }
            } else
            */
            if(current ==mQwertyKeyboard){
                prelanguage="en";
                setKKeyboard(mSymbolsKeyboard);
                //mCurKeyboard=mSymbolsKeyboard;
                //language="sym1";
                mSymbolsKeyboard.setShifted(false);

            }else if(current ==mfarsiKeyboard){
                prelanguage="fa";
                setKKeyboard(mSymbolsfarsiKeyboard);
                //mCurKeyboard=mSymbolsfarsiKeyboard;
                //language="symfarsi1";
                mSymbolsfarsiKeyboard.setShifted(false);

            }
            else if(current ==mkurdishKeyboard){
                prelanguage="kr";
                setKKeyboard(mSymbolsfarsiKeyboard);
                //mCurKeyboard=mSymbolsfarsiKeyboard;
                //language="symfarsi1";
                mSymbolsfarsiKeyboard.setShifted(false);

            }else {
                //setKKeyboard(mQwertyKeyboard);
                if( prelanguage.equals("en")){
                    setKKeyboard(mQwertyKeyboard);
                    language="en";
                }else if( prelanguage.equals("fa")){
                    setKKeyboard(mfarsiKeyboard);
                    //language="fa";

                }else {
                    setKKeyboard(mkurdishKeyboard);
                    //language="kr";

                }
            }


        }else if(primaryCode==KKeyboardView.KEYCODE_Numbers_page_goto_symbols){
            setKKeyboard(mSymbolsKeyboard);

        }
        else if (primaryCode ==KKeyboardView.KEYCODE_SHIFT_LOCK){
            if(mInputView.getKeyboard()==mQwertyKeyboard
                    ||mInputView.getKeyboard()==mSymbolsShiftedKeyboard10){
              //  handleShift();


                if(mCapsLock==true) {
                    mCapsLock = !mCapsLock;

                    if(mInputView.getKeyboard().isShifted()){
                        mInputView.getKeyboard().setShifted(false);
                        changeIconShift(0);
                        mInputView.invalidateAllKeys();

                    }

                }else{
                    mCapsLock = !mCapsLock;

                    if(!mInputView.getKeyboard().isShifted()){
                        commitTyped(getCurrentInputConnection());
                        mInputView.getKeyboard().setShifted(true);
                        changeIconShift(2);
                        mInputView.invalidateAllKeys();
                    }else{
                        handleShift();// handleShift();
                    }
                   // changeIconShift(2);
                }
            }


        }else if(primaryCode==KKeyboardView.KEYCODE_COPY){

            try{
           if(ic.getSelectedText(0)==null){
               Toast.makeText(getBaseContext(),"متنی برا کپی وجود ندارد"
                       ,Toast.LENGTH_SHORT).show();
               return;
           }
            }catch (Exception ei){

            }
            try{
            ic.beginBatchEdit();
            strCopytemp=ic.getSelectedText(0).toString();
            ic.setSelection(et.selectionEnd,et.selectionEnd);
            ic.endBatchEdit();
            }catch (Exception ei){

            }
            setClipboard(strCopytemp);
           // Toast.makeText(getBaseContext(),"کپی"
            //        ,Toast.LENGTH_SHORT).show();



        }else if(primaryCode==KKeyboardView.KEYCODE_PASTE){
            try {
                paste();
            }catch (Exception eio){
                toastM(getString(R.string.NoStoredClipboard));
            }




        }else if(primaryCode==KKeyboardView.KEYCODE_SELECTALL){

           // getCurrentInputConnection().
            CharSequence totaltext;
            ic.beginBatchEdit();

            endETselection=et.selectionEnd;
            startETselection=et.selectionEnd;
            if(ic.getSelectedText(0)==null){
                totaltext=  ic.getTextBeforeCursor(999999,0).toString();
                int lenB=totaltext.length();
                totaltext=ic.getTextAfterCursor(999999,0).toString();
                int lenA=totaltext.length();
                int lenTotal=lenA+lenB;

                ic.setSelection(0,0);
                ic.setSelection(0,lenTotal);
                //   Toast.makeText(getBaseContext(),"start: "+et.selectionStart +"  end: "+et.selectionEnd
                //         ,Toast.LENGTH_SHORT).show();
                //   Toast.makeText(getBaseContext(),"Ù…ØªÙ† Ø§Ù†ØªØ®Ø§Ø¨ Ø´Ø¯"
                //          ,Toast.LENGTH_SHORT).show();
            }
            ic.endBatchEdit();




          /*  CharSequence totaltextbeforcursor;
            CharSequence totalaftercursor;







            ic.beginBatchEdit();

            endETselection=et.selectionEnd;
            startETselection=et.selectionStart;
            if(ic.getSelectedText(0)==null){

            totaltextbeforcursor=  ic.getTextBeforeCursor(999999,0).toString();
                int lenB=totaltextbeforcursor.length();
                totalaftercursor=ic.getTextAfterCursor(999999,0).toString();
                int lenA=totalaftercursor.length();
                int lenTotal=lenA+lenB;
                toastM("lena: "+lenA+"lenb:  "+lenB+
                "total:  "+lenTotal);
                ic.setSelection(0, 0);
                ic.setSelection(0, lenTotal);

           }
            ic.endBatchEdit();



*/
        }else if(primaryCode==KKeyboardView.KEYCODE_CUT){

            if(ic.getSelectedText(0)==null){
                Toast.makeText(getBaseContext(),"متنی برای برش وجود ندارد"
                        ,Toast.LENGTH_SHORT).show();
                return;
            }
            ic.beginBatchEdit();
            strCopytemp=ic.getSelectedText(0).toString();
            setClipboard(strCopytemp);
                ic.commitText("", et.selectionStart);
                ic.setSelection(et.selectionStart,et.selectionStart);
            ic.endBatchEdit();

            Toast.makeText(getBaseContext(),"برش"
                    ,Toast.LENGTH_SHORT).show();




        }else if(primaryCode==KKeyboardView.KEYCODE_CURSOR_F) {

            if(ic.getSelectedText(0)!=null){
                //toastM("start: "+et.selectionStart                +"end:   "+et.selectionEnd);
                ic.beginBatchEdit();
                ic.setSelection(et.selectionStart, et.selectionEnd+1);
                ic.endBatchEdit();
            }else{

                ic.beginBatchEdit();


                ic.setSelection(et.selectionEnd+1,et.selectionEnd+1);
                ic.endBatchEdit();
            }


        }else if(primaryCode==KKeyboardView.KEYCODE_CURSOR_B) {

            if(ic.getSelectedText(0)!=null){
                //toastM("start: "+et.selectionStart                        +"end:   "+et.selectionEnd);
                ic.beginBatchEdit();
                ic.setSelection(et.selectionStart, et.selectionEnd - 1);
                ic.endBatchEdit();
            }else{
            ic.beginBatchEdit();


            ic.setSelection(et.selectionStart - 1, et.selectionStart - 1);
            ic.endBatchEdit();
        }


        }else if(primaryCode==KKeyboardView.KEYCODE_SYMBOLS_SWITCH){
        handlesymboleswitch();


    }else  if(primaryCode==KKeyboardView.KEYCODE_Farsi_Tatweel_3){
            onText("\u0640\u0640\u0640");


        }else  if(primaryCode==KKeyboardView.KEYCODE_Farsi_Tatweel_5){
            onText("\u0640\u0640\u0640\u0640\u0640");

        }else  if(primaryCode==KKeyboardView.KEYCODE_Farsi_Tatweel_10){
            onText("\u0640\u0640\u0640\u0640\u0640\u0640\u0640\u0640\u0640\u0640");

        }else if (primaryCode==KKeyboardView.KEYCODE_Farsi_Tatweel_15){
            onText("\u0640\u0640\u0640\u0640\u0640\u0640\u0640\u0640\u0640\u0640\u0640\u0640\u0640\u0640\u0640");


        }else  if(primaryCode==KKeyboardView.KEYCODE_Farsi_Tatweel_20){
            onText("\u0640\u0640\u0640\u0640\u0640\u0640\u0640\u0640\u0640\u0640\u0640\u0640\u0640\u0640\u0640\u0640\u0640\u0640\u0640\u0640");
           // for(int i=0;i<20;i++){handleCharacter(1600,null);}

        }else  if(primaryCode==KKeyboardView.KEYCODE_WWW_COM) {
            onText("www..com");
            for (int j = 0; j < 4; j++) {
                onKey(KKeyboardView.KEYCODE_CURSOR_B, null);
            }
        }else  if(primaryCode==KKeyboardView.KEYCODE_SYMBOLS_PAGE_1){
            if(prelanguage.equals("en")) {
                setKKeyboard(mSymbolsKeyboard);
                //mCurKeyboard=mSymbolsKeyboard;
                //language="sym1";
            }else {
                setKKeyboard(mSymbolsfarsiKeyboard);
                //mCurKeyboard=mSymbolsfarsiKeyboard;
                //language="symfarsi1";
            }

        }else  if(primaryCode==KKeyboardView.KEYCODE_SYMBOLS_PAGE_2){
            setKKeyboard(mSymbolsShiftedKeyboard);
            //mCurKeyboard=mSymbolsShiftedKeyboard;
            //language="sym2";

        }else  if(primaryCode==KKeyboardView.KEYCODE_SYMBOLS_PAGE_3){
            setKKeyboard(mSymbolsShiftedKeyboard3);
            //mCurKeyboard=mSymbolsShiftedKeyboard3;
            //language="sym3";

        }else  if(primaryCode==KKeyboardView.KEYCODE_SYMBOLS_PAGE_4){
            setKKeyboard(mSymbolsShiftedKeyboard4);
            //mCurKeyboard=mSymbolsShiftedKeyboard4;
            //language="sym4";

        }else  if(primaryCode==KKeyboardView.KEYCODE_SYMBOLS_PAGE_5){
            setKKeyboard(mSymbolsShiftedKeyboard5);
            //mCurKeyboard=mSymbolsShiftedKeyboard5;
            //language="sym5";

        }else  if(primaryCode==KKeyboardView.KEYCODE_SYMBOLS_PAGE_6){
            setKKeyboard(mSymbolsShiftedKeyboard6);
            //mCurKeyboard=mSymbolsShiftedKeyboard6;
            //language="sym6";

        }else  if(primaryCode==KKeyboardView.KEYCODE_SYMBOLS_PAGE_7){
            setKKeyboard(mSymbolsShiftedKeyboard7);
            //mCurKeyboard=mSymbolsShiftedKeyboard7;
            //language="sym7";

        }else  if(primaryCode==KKeyboardView.KEYCODE_SYMBOLS_PAGE_8){
            setKKeyboard(mSymbolsShiftedKeyboard8);
            //mCurKeyboard=mSymbolsShiftedKeyboard8;
            //language="sym8";

        }else  if(primaryCode==KKeyboardView.KEYCODE_SYMBOLS_PAGE_9){
            setKKeyboard(mSymbolsShiftedKeyboard9);
            //mCurKeyboard=mSymbolsShiftedKeyboard9;
            //language="sym9";

        }else  if(primaryCode==KKeyboardView.KEYCODE_SYMBOLS_PAGE_10){
            setKKeyboard(mSymbolsShiftedKeyboard10);
            //mCurKeyboard=mSymbolsShiftedKeyboard10;
            //language="sym10";

        }else  if(primaryCode==KKeyboardView.KEYCODE_SYMBOLS_PAGE_11){
            setKKeyboard(mSymbolsShiftedKeyboard11);
            //mCurKeyboard=mSymbolsShiftedKeyboard11;
            //language="sym11";

        }else  if(primaryCode==KKeyboardView.KEYCODE_SYMBOLS_PAGE_12){
            setKKeyboard(mSymbolsShiftedKeyboard12);
            //mCurKeyboard=mSymbolsShiftedKeyboard12;
            //language="sym12";

        }else  if(primaryCode==KKeyboardView.KEYCODE_SYMBOLS_PAGE_13){
            setKKeyboard(mSymbolsShiftedKeyboard13);
            //mCurKeyboard=mSymbolsShiftedKeyboard13;
            //language="sym13";

        }else  if(primaryCode==KKeyboardView.KEYCODE_LONG_P_GEH){
            if(mInputView.getKeyboard()==mfarsiKeyboard) {
                mPreviewPopup.showAtLocation(mInputView.getRootView(), Gravity.NO_GRAVITY, keyx,
                        keyy +  (keyheight / 2)-tempint-tempintfullscreen
                                );

                onKey(1569, null);

            }else if(mInputView.getKeyboard()==mkurdishKeyboard) {


                onKey(1711, null);

            }

        }else  if(primaryCode==KKeyboardView.KEYCODE_LONG_P_KEH){
            //if(mInputView.getKeyboard()==mfarsiKeyboard) {
                mPreviewPopup.showAtLocation(mInputView.getRootView(), Gravity.NO_GRAVITY, keyx,
                        keyy + (keyheight / 2) - tempint - tempintfullscreen
                );


                onKey(1574, null);
           // }



        }else  if(primaryCode==KKeyboardView.KEYCODE_LONG_P_DEH){
            if(mInputView.getKeyboard()==mfarsiKeyboard) {
                mPreviewPopup.showAtLocation(mInputView.getRootView(), Gravity.NO_GRAVITY, keyx,
                        keyy +  (keyheight / 2)
                        -tempint-tempintfullscreen

                );

                onKey(1584, null);

            }else if(mInputView.getKeyboard()==mkurdishKeyboard) {


                onKey(1583, null);

            }


        }else  if(primaryCode==KKeyboardView.KEYCODE_LONG_P_REH){
            if(mInputView.getKeyboard()==mfarsiKeyboard) {


                onKey(1585, null);

            }else if(mInputView.getKeyboard()==mkurdishKeyboard) {
                mPreviewPopup.showAtLocation(mInputView.getRootView(), Gravity.NO_GRAVITY, keyx,
                        keyy +  (keyheight / 2)-tempint-tempintfullscreen
                );

                onKey(1685, null);

            }


        }else  if(primaryCode==KKeyboardView.KEYCODE_LONG_P_ZEH){
            mPreviewPopup.showAtLocation(mInputView.getRootView(), Gravity.NO_GRAVITY, keyx,
                    keyy + (keyheight / 2)-tempint-tempintfullscreen
            );



            onKey(1688, null);



        }else  if(primaryCode==KKeyboardView.KEYCODE_LONG_P_TEH){
            mPreviewPopup.showAtLocation(mInputView.getRootView(), Gravity.NO_GRAVITY, keyx,
                    keyy + (keyheight / 2)-tempint-tempintfullscreen
            );



            onKey(1592, null);



        }else  if(primaryCode==KKeyboardView.KEYCODE_LONG_P_ALEF){
            if(mInputView.getKeyboard()==mfarsiKeyboard) {
                mPreviewPopup.showAtLocation(mInputView.getRootView(), Gravity.NO_GRAVITY, keyx,
                        keyy +  (keyheight / 2)-tempint-tempintfullscreen
                );


                onKey(1570, null);

            }


        }else  if(primaryCode==KKeyboardView.KEYCODE_LONG_P_LAM){

            if(mInputView.getKeyboard()==mfarsiKeyboard) {


                onKey(1604, null);

            }else if(mInputView.getKeyboard()==mkurdishKeyboard) {
                mPreviewPopup.showAtLocation(mInputView.getRootView(), Gravity.NO_GRAVITY, keyx,
                        keyy +  (keyheight / 2)-tempint-tempintfullscreen
                );

                onKey(1717, null);

            }


        }else  if(primaryCode==KKeyboardView.KEYCODE_LONG_P_VEH){
            if(mInputView.getKeyboard()==mkurdishKeyboard) {

              //  toastM("veh kurdi");
                onKey(1608, null);

            }else if(mInputView.getKeyboard()==mfarsiKeyboard
                    ) {
                mPreviewPopup.showAtLocation(mInputView.getRootView(), Gravity.NO_GRAVITY, keyx,
                        keyy +  (keyheight / 2)-tempint-tempintfullscreen
                );
               // toastM("veh farsi");
                onKey(1572, null);

            }


        }else  if(primaryCode==KKeyboardView.KEYCODE_LONG_P_AYN){
              mPreviewPopup.showAtLocation(mInputView.getRootView(), Gravity.NO_GRAVITY, keyx,
                        keyy +  (keyheight / 2)-tempint-tempintfullscreen
              );


                onKey(1594, null);




        }else  if(primaryCode==KKeyboardView.KEYCODE_LONG_P_FEH){
            if(mInputView.getKeyboard()==mfarsiKeyboard) {


                onKey(1601, null);

            }else if(mInputView.getKeyboard()==mkurdishKeyboard) {
                mPreviewPopup.showAtLocation(mInputView.getRootView(), Gravity.NO_GRAVITY, keyx,
                        keyy +  (keyheight / 2)-tempint-tempintfullscreen
                );

                onKey(1700, null);

            }


        }else  if(primaryCode==KKeyboardView.KEYCODE_LONG_P_YEH){
               mPreviewPopup.showAtLocation(mInputView.getRootView(), Gravity.NO_GRAVITY, keyx,
                        keyy +  (keyheight / 2)-tempint-tempintfullscreen
                );

                onKey(1742, null);


        } else if (primaryCode == KKeyboardView.KEYCODE_LONG_P_SAD) {
            mPreviewPopup.showAtLocation(mInputView.getRootView(), Gravity.NO_GRAVITY, keyx,
                    keyy + (keyheight / 2)-tempint-tempintfullscreen
            );



            onKey(1590, null);



        }else if (primaryCode == KKeyboardView.KEYCODE_LONG_VEHHADKURDI) {
            mPreviewPopup.showAtLocation(mInputView.getRootView(), Gravity.NO_GRAVITY, keyx,
                    keyy + (keyheight / 2)-tempint-tempintfullscreen
            );



            onKey(1738, null);



        }else  if(primaryCode==KKeyboardView.KEYCODE_LONG_RUN_POPUPOPTONS){

            ///////////////////////
            //File folder = new File(getFilesDir(),Constants.ClipboardCacheFolderPath);
            File directory = AppStorage.root(getApplicationContext());
            File folder = new File( directory + "/data/data/com.kurdikeyboard" + Constants.ClipboardCacheFolderPath );

            if (!folder.exists()) {
                Toast.makeText(this, "nothings exists", Toast.LENGTH_LONG).show();

                try {
                    folder.mkdir();
                } catch (Exception e) {
                    Toast.makeText(this,
                            e.getMessage()
                            ,Toast.LENGTH_SHORT).show();


                }
            }
           // List<File> files =
            //        (List<File>) FileUtils.listFiles(folder, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
           // ArrayList<File> inFiles = new ArrayList<File>();
           // toastM("lenth is:");
           // File directory = Environment.getExternalStorageDirectory();

           // File folder2 = new File( directory +"/"+Constants.ClipboardCacheFolderPath );
          //  File directory = Environment.getExternalStorageDirectory();
          //  File folder = new File( directory + "/" + Constants.ClipboardCacheFolderPath );

                //toastM("path: " + directory.getPath());
                File[] files = AppStorage.sortedFiles(folder);
               // toastM("lenth is:" + files.length);
                mallclipboardStrings.clear();
                mallclipboarddate.clear();

            for (File file : files) {
                    if(file.getName().endsWith(".txt")) {
                        try {
                            //  File tempfile=file;
                            //  toastM("123");
                            FileReader fr = new FileReader(file);
                            BufferedReader br = new BufferedReader(fr);
                            StringBuilder sb = new StringBuilder();

                            String sCurrentLine = null;

                            while ((sCurrentLine = br.readLine()) != null) {
                                //sCurrentLine+=  String.valueOf(br.readLine());
                                sb.append(sCurrentLine).append("\n");
                                //toastM(br.readLine());
                            }
                            fr.close();
                            br.close();
                            mallclipboardStrings.add(AppStorage.trimTrailingNewline(sb));
                            String nametemp=file.getName();
                            nametemp = nametemp.substring(0, nametemp.length()-4);
                            mallclipboarddate.add(nametemp);
                        } catch (IOException ioe) {
                            toastM(ioe.getMessage());
                        }
                    }
            }




           // Collections.sort(mallclipboardStrings,Collections.<String>reverseOrder());
            final MyPopupClipboard
                    popupphonecontacts =new MyPopupClipboard(
                    rootTemp,getApplicationContext()
                    ,this);

            popupphonecontacts.setOutsideTouchable(true);
            //popupcontacts.setFocusable(true);
            // Removes default background.
            //popupcontacts.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            popupphonecontacts.setBackgroundDrawable(new ColorDrawable(Color.GRAY));

            final float popUpheight2 = getResources().getDimension(
                    R.dimen.keyboard_height);
            popupphonecontacts.setSizeForSoftKeyboard();
            popupphonecontacts.setSize(WindowManager.LayoutParams.MATCH_PARENT,(int) popUpheight2);
            if(popupphonecontacts.isShowing()){

                popupphonecontacts.dismiss();


            }
            else {



                int screenHeight = mInputView.getRootView().getHeight();
                int bottomHeight= (int) getResources().getDimension(R.dimen.key_height_firstrow);
                int equalizer=(int) getResources().getDimension(R.dimen.hiegh_for_emoji);
                int defHeight=(screenHeight-2*bottomHeight-equalizer);

                int locationforwhatsup[] = new int[2];
                ivTemp.getLocationOnScreen(locationforwhatsup);
                int bottomHeight3forwhatsup= locationforwhatsup[1];

                int height2 = (int) (locationforwhatsup[1] +
                        ivTemp.getMeasuredHeight());

                int equalizerforwhatsup=(int) getResources().getDimension(R.dimen.hiegh_for_emoji_whatsup);

                int defHeight2=Math.min(defHeight, height2-equalizerforwhatsup);
                if(isPortrait()){
                    defHeight2-=getResources().getDimension(
                            R.dimen.key_height_firstrow
                    );
                }
                popupphonecontacts.setSize(WindowManager.LayoutParams.MATCH_PARENT,
                        defHeight2);
                // popupcontacts.showAtLocation(mInputView.getRootView(), Gravity.BOTTOM, 0, 0);



                InputMethodManager mInputMethodManager = (InputMethodManager) getBaseContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);

                // mInputMethodManager.showSoftInput(root, 0);

                mInputMethodManager.showSoftInput(mInputView, 0);

                popupphonecontacts.setOnSoftKeyboardOpenCloseListener(
                        new MyPopupClipboard.OnSoftKeyboardOpenCloseListener() {

                            @Override
                            public void onKeyboardOpen(int keyBoardHeight) {

                            }

                            @Override
                            public void onKeyboardClose() {
                                if (popupphonecontacts.isShowing())
                                    popupphonecontacts.dismiss();
                            }
                        });











            }




            try {
                popupphonecontacts.showAtLocation(mInputView.getRootView(), Gravity.BOTTOM, 0, 0);
            }catch (Exception ei){
                toastM(ei.getMessage());
            }




        }else  if(primaryCode==KKeyboardView.KEYCODE_CONTACCTS_READ_2){
            if (!PermissionActivity.ensure(this, android.Manifest.permission.READ_CONTACTS)) {
                return; // the permission dialog opens; tap the key again afterwards
            }
            final MyPopupPhoneContacts
                    popupphonecontacts =new MyPopupPhoneContacts(
                    rootTemp,getApplicationContext()
                    ,this);

            popupphonecontacts.setOutsideTouchable(true);
            //popupcontacts.setFocusable(true);
            // Removes default background.
            //popupcontacts.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            popupphonecontacts.setBackgroundDrawable(new ColorDrawable(Color.GRAY));

            final float popUpheight2 = getResources().getDimension(
                    R.dimen.keyboard_height);
            popupphonecontacts.setSizeForSoftKeyboard();
            popupphonecontacts.setSize(WindowManager.LayoutParams.MATCH_PARENT, (int) popUpheight2);
            if(popupphonecontacts.isShowing()){

                popupphonecontacts.dismiss();


            }
            else {



                int screenHeight = mInputView.getRootView().getHeight();
                int bottomHeight= (int) getResources().getDimension(R.dimen.key_height_firstrow);
                int equalizer=(int) getResources().getDimension(R.dimen.hiegh_for_emoji);
                int defHeight=(screenHeight-2*bottomHeight-equalizer);

                int locationforwhatsup[] = new int[2];
                ivTemp.getLocationOnScreen(locationforwhatsup);
                int bottomHeight3forwhatsup= locationforwhatsup[1];

                int height2 = (int) (locationforwhatsup[1] +
                        ivTemp.getMeasuredHeight());

                int equalizerforwhatsup=(int) getResources().getDimension(R.dimen.hiegh_for_emoji_whatsup);

                int defHeight2=Math.min(defHeight, height2-equalizerforwhatsup);
                if(isPortrait()){
                    defHeight2-=getResources().getDimension(
                            R.dimen.key_height_firstrow
                    );
                }
                popupphonecontacts.setSize(WindowManager.LayoutParams.MATCH_PARENT,
                        defHeight2);
                // popupcontacts.showAtLocation(mInputView.getRootView(), Gravity.BOTTOM, 0, 0);



                InputMethodManager mInputMethodManager = (InputMethodManager) getBaseContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);

                // mInputMethodManager.showSoftInput(root, 0);

                mInputMethodManager.showSoftInput(mInputView, 0);

                popupphonecontacts.setOnSoftKeyboardOpenCloseListener(
                        new MyPopupPhoneContacts.OnSoftKeyboardOpenCloseListener() {

                            @Override
                            public void onKeyboardOpen(int keyBoardHeight) {

                            }

                            @Override
                            public void onKeyboardClose() {
                                if (popupphonecontacts.isShowing())
                                    popupphonecontacts.dismiss();
                            }
                        });











            }




           try {
               popupphonecontacts.showAtLocation(mInputView.getRootView(), Gravity.BOTTOM, 0, 0);
           }catch (Exception ei){
               toastM(ei.getMessage());
           }


        }else  if(primaryCode==KKeyboardView.KEYCODE_Ready_Sentences){
            final MyPopupReadySentences
                    popupphonecontacts2 =new MyPopupReadySentences(
                    rootTemp,getApplicationContext()
                    ,this);

            popupphonecontacts2.setOutsideTouchable(true);
            //popupcontacts.setFocusable(true);
            // Removes default background.
            //popupcontacts.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            popupphonecontacts2.setBackgroundDrawable(new ColorDrawable(Color.GRAY));

            final float popUpheight2 = getResources().getDimension(
                    R.dimen.keyboard_height);
            popupphonecontacts2.setSizeForSoftKeyboard();
            popupphonecontacts2.setSize(WindowManager.LayoutParams.MATCH_PARENT,(int) popUpheight2);
            if(popupphonecontacts2.isShowing()){

                popupphonecontacts2.dismiss();


            }
            else {



                int screenHeight = mInputView.getRootView().getHeight();
                int bottomHeight= (int) getResources().getDimension(R.dimen.key_height_firstrow);
                int equalizer=(int) getResources().getDimension(R.dimen.hiegh_for_emoji);
                int defHeight=(screenHeight-2*bottomHeight-equalizer);

                int locationforwhatsup[] = new int[2];
                ivTemp.getLocationOnScreen(locationforwhatsup);
                int bottomHeight3forwhatsup= locationforwhatsup[1];

                int height2 = (int) (locationforwhatsup[1] +
                        ivTemp.getMeasuredHeight());

                int equalizerforwhatsup=(int) getResources().getDimension(R.dimen.hiegh_for_emoji_whatsup);

                int defHeight2=Math.min(defHeight, height2-equalizerforwhatsup);
                if(isPortrait()){
                    defHeight2-=getResources().getDimension(
                            R.dimen.key_height_firstrow
                    );
                }
                popupphonecontacts2.setSize(WindowManager.LayoutParams.MATCH_PARENT,
                        defHeight2);
                // popupcontacts.showAtLocation(mInputView.getRootView(), Gravity.BOTTOM, 0, 0);



                InputMethodManager mInputMethodManager = (InputMethodManager) getBaseContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);


                // mInputMethodManager.showSoftInput(root, 0);

                mInputMethodManager.showSoftInput(mInputView, 0);

                popupphonecontacts2.setOnSoftKeyboardOpenCloseListener(
                        new MyPopupReadySentences.OnSoftKeyboardOpenCloseListener() {

                            @Override
                            public void onKeyboardOpen(int keyBoardHeight) {

                            }

                            @Override
                            public void onKeyboardClose() {
                                if (popupphonecontacts2.isShowing())
                                    popupphonecontacts2.dismiss();
                            }
                        });











            }




            try {
                popupphonecontacts2.showAtLocation(mInputView.getRootView(), Gravity.BOTTOM, 0, 0);
            }catch (Exception ei){
                toastM(ei.getMessage());
            }


        }else if(primaryCode==1717){
            if(correct_kurdi_v_abouve){
                handleCharacter(1604, null);
                onText("\u0658");
                handleCharacter(1600, null);

            }else {
                handleCharacter(primaryCode,null);
            }
        }else if(primaryCode==1742){
            if(correct_kurdi_v_abouve){
                handleCharacter(1740, null);
                onText("\u065A");
            }else {
                handleCharacter(primaryCode,null);
            }
        }else if(primaryCode==1734){
            if(correct_kurdi_v_abouve){
                handleCharacter(1608,null);
                onText("\u065A");
            }else {
                handleCharacter(primaryCode,null);
            }
        }else if(primaryCode==KKeyboardView.KEYCODE_symbols_Numbers_page){
            setKKeyboard(mSymbolsnumbers);
        }else if(primaryCode==KKeyboardView.KEYCODE_LONG_closetobringupemoji){
           // Toast.makeText(getApplicationContext(),
             //       "KEYCODE_LONG_closetobringupemoji",Toast.LENGTH_LONG).show();
            final EmojiconsPopup popupWindow = new EmojiconsPopup(rootTemp,getApplicationContext());
            popupWindow.setSizeForSoftKeyboard();
            final float popUpheight = getResources().getDimension(
                    R.dimen.keyboard_height);
            popupWindow.setSize(WindowManager.LayoutParams.MATCH_PARENT, (int) popUpheight);

            if (popupWindow.isShowing()) {

                popupWindow.dismiss();


            } else {

                //popupWindow = new EmojiconsPopup(popupView, this);
                // popupWindow.setSize(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);


                int screenHeight = mInputView.getRootView().getHeight();
                int bottomHeight = (int) getResources().getDimension(R.dimen.key_height_firstrow);
                int equalizer = (int) getResources().getDimension(R.dimen.hiegh_for_emoji);
                int defHeight = (screenHeight - 2 * bottomHeight - equalizer);

                int locationforwhatsup[] = new int[2];
                ivKeyboardSelectall.getLocationOnScreen(locationforwhatsup);
                int bottomHeight3forwhatsup = locationforwhatsup[1];

                int height2 = (int) (locationforwhatsup[1] +
                        ivKeyboardSelectall.getMeasuredHeight());

                int equalizerforwhatsup = (int) getResources().getDimension(R.dimen.hiegh_for_emoji_whatsup);

                int defHeight2 = Math.min(defHeight, height2 - equalizerforwhatsup);

                /*
                if (!isPortrait()&&(toollayout.getVisibility()==View.GONE)&&
                        preferenceKeyboard.getBoolean(getString(R.string.preftoolbarsivkeyboard),  false)) {

                    defHeight2 += getResources().getDimension(
                            R.dimen.key_height_firstrow
                    );

                    defHeight2 += .5*defHeight2;

                }
                */


                popupWindow.setSize(WindowManager.LayoutParams.MATCH_PARENT,
                        defHeight2);
                popupWindow.showAtLocation(mInputView.getRootView(), Gravity.BOTTOM, 0, 0);


                // Bring soft keyboard up : NOT WORKING
                // final
                InputMethodManager mInputMethodManager = (InputMethodManager) getBaseContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);

                // mInputMethodManager.showSoftInput(root, 0);

                mInputMethodManager.showSoftInput(mInputView, 0);

                popupWindow.setOnSoftKeyboardOpenCloseListener(new OnSoftKeyboardOpenCloseListener() {

                    @Override
                    public void onKeyboardOpen(int keyBoardHeight) {

                    }

                    @Override
                    public void onKeyboardClose() {
                        if (popupWindow.isShowing())
                            popupWindow.dismiss();
                    }
                });

                popupWindow.setOnEmojiconClickedListener(new OnEmojiconClickedListener() {

                    @Override
                    public void onEmojiconClicked(Emojicon emojicon) {
                        mComposing.append(emojicon.getEmoji());
                        commitTyped(getCurrentInputConnection());

                        //  customToast("" + emojicon.getEmoji());
                    }
                });

                popupWindow.setOnEmojiconBackspaceClickedListener(new OnEmojiconBackspaceClickedListener() {

                    @Override
                    public void onEmojiconBackspaceClicked(View v) {
                        KeyEvent event = new KeyEvent(0, 0, 0, KeyEvent.KEYCODE_DEL, 0, 0, 0, 0, KeyEvent.KEYCODE_ENDCALL);
                        //customToast(" " + event);
                        handleBackspace();

                    }
                });
                popupWindow.setOnEmojiconCloseClickedListener(new OnEmojiconCloseClickedListener() {

                    @Override
                    public void onEmojiconCloseClicked(View v) {

                        if(popupWindow.isShowing()) {

                            popupWindow.dismiss();
                        }


                    }
                });


            }



        }else if(primaryCode==-158){
            //onja
            /*
            try {
                mCurKeyboard.changeMultiKey(getResources(), primaryCode);
            }catch (Exception ex){
                Toast.makeText(getBaseContext(),"er: mCurKeyboard.setIme ",Toast.LENGTH_SHORT).show();
            }
            */

            onKey(KKeyboard.mMulti_change_value, null);
            //Toast.makeText(getBaseContext(),"value: "+KKeyboard.mMulti_change_value,Toast.LENGTH_SHORT).show();

        }




        else  {
            handleCharacter(primaryCode, keyCodes);


        }

       /* if(isAlphabet(primaryCode)){

        }else{
            ic.finishComposingText();
        }*/



    }


    ////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////

    public void onGetText() {

       // Toast.makeText(getBaseContext(),"ontext",Toast.LENGTH_LONG).show();

        InputConnection ic = getCurrentInputConnection();
        if (ic == null) {
            //Toast.makeText(getBaseContext(),"null text",Toast.LENGTH_SHORT).show();
            return;
        }
        ic.beginBatchEdit();
        if (mComposing.length() > 0) {
            commitTyped(ic);
        }
        if(ic.getSelectedText(0)==null) {
            Toast.makeText(getBaseContext(),"متنی انتخاب نشده",Toast.LENGTH_SHORT).show();

            return;

        }
        strCopytemp=ic.getSelectedText(0).toString();

        ic.endBatchEdit();
       // updateShiftKeyState(getCurrentInputEditorInfo());


    }
    public void onText(CharSequence text) {

        // Toast.makeText(getBaseContext(),"ontext",Toast.LENGTH_LONG).show();

        InputConnection ic = getCurrentInputConnection();
        if (ic == null) {
            //Toast.makeText(getBaseContext(),"null text",Toast.LENGTH_SHORT).show();
            return;
        }
        ic.beginBatchEdit();
        if (mComposing.length() > 0) {
            commitTyped(ic);
        }
        ic.commitText(text, 0);
        ic.endBatchEdit();
        // updateShiftKeyState(getCurrentInputEditorInfo());


    }



    ////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////


    private void handleBackspace() {

      //  Toast.makeText(getBaseContext(),"handleBackspace",Toast.LENGTH_LONG).show();

        final int length = mComposing.length();

        if (length > 1) {
            mComposing.delete(length - 1, length);
            getCurrentInputConnection().setComposingText(mComposing, 1);

        } else if (length > 0) {
            mComposing.setLength(0);
            getCurrentInputConnection().commitText("", 0);

        } else {
            keyDownUp(KeyEvent.KEYCODE_DEL);
        }
       // updateShiftKeyState(getCurrentInputEditorInfo());
    }

    ////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////
    private void handleShift(){
        if (mInputView == null) {
            return;
        }
        KKeyboard currentKeyboard = (KKeyboard) mInputView.getKeyboard();
        if ( currentKeyboard==mQwertyKeyboard
                ||currentKeyboard==mSymbolsShiftedKeyboard10) {
            if(mInputView.isShifted()){
                if(mCapsLock) {
                    mInputView.setShifted(false);
                    mCapsLock = false;
                    changeIconShift(0);
                    mInputView.invalidateAllKeys();
                }else {
                    mCapsLock = true;
                    changeIconShift(2);
                    mInputView.invalidateAllKeys();
                }
            }else {
                // mInputView.setShifted(mCapsLock || !mInputView.isShifted());
                mInputView.setShifted(true);
                changeIconShift(1);

                mInputView.invalidateAllKeys();
            }
        }
    }
    private void handleShift3() {

       // Toast.makeText(getBaseContext(),"handleshift",Toast.LENGTH_LONG).show();

        if (mInputView == null) {
            return;
        }

        KKeyboard currentKeyboard = (KKeyboard) mInputView.getKeyboard();
        if ( currentKeyboard==mQwertyKeyboard
                ||currentKeyboard==mSymbolsShiftedKeyboard10) {
            // Alphabet keyboard
           // checkToggleCapsLock();
           // mInputView.setShifted(mCapsLock || !mInputView.isShifted());
            if(mInputView.isShifted()){
                mInputView.setShifted(false);
                mCapsLock=false;
               changeIconShift(0);
                mInputView.invalidateAllKeys();
            }else {
               // mInputView.setShifted(mCapsLock || !mInputView.isShifted());
                mInputView.setShifted(true);
                changeIconShift(1);
                mInputView.invalidateAllKeys();
            }
        }



    }

    ////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////

    private void handleCharacter(int primaryCode, int[] keyCodes) {
        //hc144
        //   Toast.makeText(getBaseContext(),"handlecharacter",Toast.LENGTH_LONG).show();

        if (isInputViewShown()) {
            if (mInputView.isShifted()) {
                primaryCode = Character.toUpperCase(primaryCode);
            }else {
                primaryCode = Character.toLowerCase(primaryCode);
            }
        }


        mPredictionOn=false;

        if(primaryCode==32&&spacemove){
            if(spacemovetoright) {
                handleLanguageSwitch();

            }else{
                handleLanguageSwitch2();
            }
            spacemove = false;
            commitTyped(getCurrentInputConnection());
        }else {

            getCurrentInputConnection().finishComposingText();
            getCurrentInputConnection().commitText(
                    String.valueOf((char) primaryCode), 1);
        }

        if(isAlphabet(primaryCode)){
            if(mInputView.getKeyboard()==mQwertyKeyboard){

                if (mInputView.isShifted()&&!mCapsLock){
                    mInputView.setShifted(false);
                    changeIconShift(0);
                }
            }
            if(mInputView.getKeyboard()==mSymbolsShiftedKeyboard10){

                if (mInputView.isShifted()&&!mCapsLock){
                    mInputView.setShifted(false);
                    changeIconShift(0);
                }
            }

        }




        // toastM("handlecharactor");

    }

    ////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////



    private void handleClose() {

      // Toast.makeText(getBaseContext(),"handleclose",Toast.LENGTH_LONG).show();

        commitTyped(getCurrentInputConnection());
        requestHideSelf(0);
        mInputView.closing();
        trimCache(this);

    }

    private IBinder getToken() {

       // Toast.makeText(getBaseContext(),"gettoken",Toast.LENGTH_LONG).show();

        final Dialog dialog = getWindow();
        if (dialog == null) {
            return null;
        }
        final Window window = dialog.getWindow();
        if (window == null) {
            return null;
        }
        return window.getAttributes().token;
    }


    private void handleLanguageSwitch() {

     //   Toast.makeText(getBaseContext(),"handleLanguageswitch",Toast.LENGTH_LONG).show();

        Keyboard currentKeyboard = mInputView.getKeyboard();
        if(currentKeyboard==mQwertyKeyboard){
            if(language_farsi_enable) {
                setKKeyboard(mfarsiKeyboard);
                mCurKeyboard = mfarsiKeyboard;
                language = "fa";
                prelanguage = "fa";
            }else if(language_kurdi_enable) {
                setKKeyboard(mkurdishKeyboard);
                mCurKeyboard = mkurdishKeyboard;
                language = "kr";
                prelanguage = "kr";
            }else {
                setKKeyboard(mQwertyKeyboard);
                mCurKeyboard=mQwertyKeyboard;
                language="en";
                prelanguage="en";
            }
        }
        if(currentKeyboard==mfarsiKeyboard){
            if(language_kurdi_enable) {
                setKKeyboard(mkurdishKeyboard);
                mCurKeyboard = mkurdishKeyboard;
                language = "kr";
                prelanguage = "kr";
            }else {
                setKKeyboard(mQwertyKeyboard);
                mCurKeyboard=mQwertyKeyboard;
                language="en";
                prelanguage="en";
            }
        }
        if(currentKeyboard==mkurdishKeyboard){

            setKKeyboard(mQwertyKeyboard);
            mCurKeyboard=mQwertyKeyboard;
            language="en";
            prelanguage="en";
        }

      //  handleBackspace();
      //  final int options = this.getCurrentInputEditorInfo().imeOptions;
       // final int actionId = options & EditorInfo.IME_MASK_ACTION;
            try {
                mCurKeyboard.setImeOptions(getResources(), getCurrentInputEditorInfo());
            }catch (Exception ex){
                Toast.makeText(getBaseContext(),"er: mCurKeyboard.setIme ",Toast.LENGTH_SHORT).show();
            }


      //  changeIconspace();
        commitTyped(getCurrentInputConnection());
        getCurrentInputConnection().finishComposingText();
      //  mInputView.invalidateAllKeys();
       // handleBackspace();

    }

    private void handleLanguageSwitch2() {

        //   Toast.makeText(getBaseContext(),"handleLanguageswitch",Toast.LENGTH_LONG).show();

        Keyboard currentKeyboard = mInputView.getKeyboard();
        if(currentKeyboard==mQwertyKeyboard){
            if(language_kurdi_enable) {
                setKKeyboard(mkurdishKeyboard);
                mCurKeyboard = mkurdishKeyboard;
                language = "kr";
                prelanguage = "kr";
            }
            else if(language_farsi_enable) {
                setKKeyboard(mfarsiKeyboard);
                mCurKeyboard = mfarsiKeyboard;
                language = "fa";
                prelanguage = "fa";
            }else {
                setKKeyboard(mQwertyKeyboard);
                mCurKeyboard=mQwertyKeyboard;
                language="en";
                prelanguage="en";
            }
        }
        if(currentKeyboard==mfarsiKeyboard){
            setKKeyboard(mQwertyKeyboard);
            mCurKeyboard=mQwertyKeyboard;
            language="en";
            prelanguage="en";

        }
        if(currentKeyboard==mkurdishKeyboard){
            if(language_farsi_enable) {
                setKKeyboard(mfarsiKeyboard);
                mCurKeyboard = mfarsiKeyboard;
                language = "fa";
                prelanguage = "fa";
            }else {
                setKKeyboard(mQwertyKeyboard);
                mCurKeyboard=mQwertyKeyboard;
                language="en";
                prelanguage="en";
            }


        }

        //  handleBackspace();
        //  final int options = this.getCurrentInputEditorInfo().imeOptions;
        // final int actionId = options & EditorInfo.IME_MASK_ACTION;
        try {
            mCurKeyboard.setImeOptions(getResources(), getCurrentInputEditorInfo());
        }catch (Exception ex){
            Toast.makeText(getBaseContext(),"er: mCurKeyboard.setIme ",Toast.LENGTH_SHORT).show();
        }


        //  changeIconspace();
         commitTyped(getCurrentInputConnection());
        getCurrentInputConnection().finishComposingText();

       // mInputView.invalidateAllKeys();
        // handleBackspace();

    }


    private void checkToggleCapsLock() {

       // Toast.makeText(getBaseContext(),"checktogglecapslock",Toast.LENGTH_LONG).show();

        long now = System.currentTimeMillis();
        if (mLastShiftTime + 800 > now) {
            mCapsLock = !mCapsLock;
            mLastShiftTime = 0;
        } else {
            mLastShiftTime = now;
        }

    }

    private String getWordSeparators() {

       // Toast.makeText(getBaseContext(),"getwordseparators",Toast.LENGTH_LONG).show();

        return mWordSeparators;
    }
    private String getWordtwofunction() {

        // Toast.makeText(getBaseContext(),"getwordseparators",Toast.LENGTH_LONG).show();

        return mwordtwofunction;
    }
    private String getWordtwofunction2() {

        // Toast.makeText(getBaseContext(),"getwordseparators",Toast.LENGTH_LONG).show();

        return mwordtwofunction2;
    }

    public boolean isWordSeparator(int code) {

       // Toast.makeText(getBaseContext(),"iswordseparator",Toast.LENGTH_LONG).show();

        String separators = getWordSeparators();
        return separators.contains(String.valueOf((char)code));
    }
    public boolean isWordtwofunction(int code) {

        // Toast.makeText(getBaseContext(),"iswordseparator",Toast.LENGTH_LONG).show();

        String separators = getWordtwofunction();
        return separators.contains(String.valueOf((char)code));
    }
    public boolean isWordtwofunction2(int code) {

        // Toast.makeText(getBaseContext(),"iswordseparator",Toast.LENGTH_LONG).show();

        String separators = getWordtwofunction2();
        return separators.contains(String.valueOf((char)code));
    }






    public void pickDefaultCandidate() {

       // Toast.makeText(getBaseContext(),"pickdefaultcandidate",Toast.LENGTH_LONG).show();

        pickSuggestionManually(0);
    }

    public void pickSuggestionManually(int index) {

      // Toast.makeText(getBaseContext(),"ppicksuggestionmanually",Toast.LENGTH_LONG).show();

        if (mCompletionOn && mCompletions != null && index >= 0
                && index < mCompletions.length) {
            CompletionInfo ci = mCompletions[index];
            getCurrentInputConnection().commitCompletion(ci);

           // updateShiftKeyState(getCurrentInputEditorInfo());
        } else if (mComposing.length() > 0) {
            // If we were generating candidate suggestions for the current
            // text, we would commit one of them here.  But for this sample,
            // we will just commit the current text.
            commitTyped(getCurrentInputConnection());
        }
    }


    /////////////////////////////////
        /////////// public methods
       /////////////////////////////////
       /////////////////////////////////


private void setCurrentLanguage(){


    if(mCurKeyboard!=null){
        if(language.equals("en")){
                         mCurKeyboard = mQwertyKeyboard;
                            prelanguage="en";
          }else if(language.equals("fa")){
             mCurKeyboard=mfarsiKeyboard;
            prelanguage="fa";
           }else if(language.equals("kr")){
            mCurKeyboard=mkurdishKeyboard;
            prelanguage="kr";
        }else if(language.equals("sym1")){
            mCurKeyboard=mSymbolsKeyboard;
             }else if(language.equals("symfarsi1")){
            mCurKeyboard=mSymbolsfarsiKeyboard;
        }
        else if(language.equals("sym2")){
            mCurKeyboard=mSymbolsShiftedKeyboard;
             }else if(language.equals("sym3")){
            mCurKeyboard=mSymbolsShiftedKeyboard3;
        }else if(language.equals("sym4")){
            mCurKeyboard=mSymbolsShiftedKeyboard4;
        }else if(language.equals("sym5")){
            mCurKeyboard=mSymbolsShiftedKeyboard5;
        }else if(language.equals("sym6")){
            mCurKeyboard=mSymbolsShiftedKeyboard6;
        }else if(language.equals("sym7")){
            mCurKeyboard=mSymbolsShiftedKeyboard7;
        }else if(language.equals("sym8")){
            mCurKeyboard=mSymbolsShiftedKeyboard8;
        }else if(language.equals("sym9")){
            mCurKeyboard=mSymbolsShiftedKeyboard9;
        }else if(language.equals("sym10")){
            mCurKeyboard=mSymbolsShiftedKeyboard10;
        }else if(language.equals("sym11")){
            mCurKeyboard=mSymbolsShiftedKeyboard11;
        }else if(language.equals("sym12")){
            mCurKeyboard=mSymbolsShiftedKeyboard12;

        }else if(language.equals("sym13")){
            mCurKeyboard=mSymbolsShiftedKeyboard13;
        }
    }else {
        mCurKeyboard = mQwertyKeyboard;
    }



}














    public void swipeRight() {

        handleLanguageSwitch();

    }

    public void swipeLeft() {



    }

    public void swipeDown() {
        handleClose();

    }

    public void swipeUp() {
    }

    public void handlehapticfeedback(){
        boolean vibreh=  preferenceKeyboard.getBoolean(getString(R.string.prefvibreh),  true);
        int volvib=preferenceKeyboard.getInt(getString(R.string.prefseekbarvibrate),400);

        if(!mInputView.isHapticFeedbackEnabled()){
            mInputView.setHapticFeedbackEnabled(true);

        }
        if(vibreh) {
            //handlevibreh(volvib);
            // mInputView.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP);
           /////////  mInputView.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
            // mInputView.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
            // mInputView.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP);
            // mInputView.performHapticFeedback(HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING);
            // mInputView.performHapticFeedback(HapticFeedbackConstants.);
            mInputView.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY,
                    HapticFeedbackConstants.FLAG_IGNORE_VIEW_SETTING
                            | HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING);
        }

    }
    public void onPress(int primaryCode) {
       // handlehapticfeedback();
       // handlevibreh(200);
        // 32=space -3=done(exit) -2= 123 10=return(go,enter)
        // -5=delete  -1=shift
        if(isFullscreenMode()) {
            if (isTopRowVisible()) {
                //  if(isPortrait()) {
                tempint = -(int) getResources().
                        getDimension(R.dimen.key_height_firstrow);
                tempintfullscreen=-(int) getResources().
                        getDimension(R.dimen.key_height_firstrow);

                //            }
            }//else if(!isPortrait()){

            //}
            else {
                tempint = (int) getResources().getDimension(R.dimen.longclickyadd);
                tempintfullscreen=-(int) getResources().
                        getDimension(R.dimen.key_height_firstrow);
                tempintfullscreen*=2;

            }
        }else{
            if (isTopRowVisible()) {
                //  if(isPortrait()) {
                tempint = -(int) getResources().
                        getDimension(R.dimen.key_height_firstrow);
                //            }
            }//else if(!isPortrait()){

            //}
            else {
                tempint = (int) getResources().getDimension(R.dimen.longclickyadd);

            }
        }

        if(preferenceKeyboard.getBoolean(getString(R.string.prefnotshowpreview),false)) {


            if (primaryCode == 32 || primaryCode == -3 || primaryCode == -2
                    || primaryCode == 10 || primaryCode == -5
                    || primaryCode == -1 || primaryCode == 46
                    || primaryCode == 8204) {
                // mInputView.setPreviewEnabled(false);
            } else {
                mInputView.setPreviewEnabled(true);
            }
        }

       // toastM("press");


    }

    public void onRelease(int primaryCode) {

       // mInputView.setPreviewEnabled(true);
        mInputView.setPreviewEnabled(false);
      /*  if(spacemove==true){
            handleLanguageSwitch();
            handleBackspace();
            spacemove=false;
        }*/

       //if(mPreviewPopup.isShowing()){ mPreviewPopup.dismiss();}

       // toastM("release");


        if(mComposing.length()>1){
            getCurrentInputConnection().finishComposingText();
           // getCurrentInputConnection().finishComposingText();
        }else  if(mComposing.length()>0){

            mInputView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Do something after 1000 ms
                  //  commitTyped(getCurrentInputConnection());
                    getCurrentInputConnection().finishComposingText();
                }
            }, timewordtwofuction);

        }
        if(ismultichoiceprimaries(primaryCode)||isfunctioned(primaryCode)) {
            setmultichangekeyvalue(primaryCode);
           // Toast.makeText(getApplicationContext(),
             //       "multichice",Toast.LENGTH_SHORT).show();
        }



    }
    public boolean isfunctioned(int primaryCode){
        boolean retvalue=false;
        if((primaryCode==-116)||(primaryCode==-112)||(primaryCode==-157)||(primaryCode==-150)||(primaryCode==-152)){
           retvalue=true;
        }
        return retvalue;

    }
    public void setmultichangekeyvalue(int primaryCode){
        try {
            mCurKeyboard.changeMultiKey(getResources(), primaryCode);
        }catch (Exception ex){
            Toast.makeText(getBaseContext(),"er: mCurKeyboard.setIme ",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
 public  void onDestroy(){

   //  Toast.makeText(getBaseContext(),"onDestroy",Toast.LENGTH_LONG).show();

     super.onDestroy();
        if (mPredict != null) mPredict.saveAsync();
      //  toastM("onDestroy");
        try {
            trimCache(this);
            unregisterClipboardListener();
            // toastM("trimCache");
        } catch (Exception e) {
            // TODO Auto-generated catch block
           // e.printStackTrace();
        }
    }



    public void changeIconShift(int valueshift){
        KKeyboard keyboardtemp = (KKeyboard)mInputView.getKeyboard();
        if(keyboardtemp==mQwertyKeyboard){

          //  final KKeyboard keyboard = (KKeyboard)mInputView.getKeyboard();
            if(valueshift==1){

                    keyboardtemp.setShiftIcon(
                            getResources().getDrawable(R.drawable.shift_shifted)
                    );



            } else if(valueshift==0){

               // if(listS.equals("5")||listS.equals("6")){
                //    keyboardtemp.setShiftIcon(
                   //         getResources().getDrawable(R.drawable.shift_white));
             //   }else {
                    keyboardtemp.setShiftIcon(
                            getResources().getDrawable(R.drawable.shift_black)
                    );
               // }

            }else if(valueshift==2){
                keyboardtemp.setShiftIcon(
                        getResources().getDrawable(R.drawable.shift_lock_black));
            }
        }

        mInputView.invalidateKey(Keyboard.KEYCODE_SHIFT);
       // mInputView.invalidateAllKeys();


    }
    public void changeIconShift2(int valueshift)
    {
        Keyboard currentKeyboard = mInputView.getKeyboard();
        List<Keyboard.Key> keys = currentKeyboard.getKeys();

        if(currentKeyboard==mQwertyKeyboard){
            mInputView.invalidateKey(Keyboard.KEYCODE_SHIFT);
            for(int i = 0; i < keys.size() - 1; i++ )
            {
                Keyboard.Key currentKey = keys.get(i);

                //If your Key contains more than one code, then you will have to check if the codes array contains the primary code
                if(currentKey.codes[0] == Keyboard.KEYCODE_SHIFT)
                {
                    if(valueshift==1){
                        currentKey.label = null;
                        currentKey.icon =
                                getResources().getDrawable(R.drawable.a_1_theme_1_background_key_icon_shift_shifted);

                    }
                    else if(valueshift==0){
                        currentKey.label = null;
                        currentKey.icon = getResources().getDrawable(R.drawable.a_1_theme_1_background_key_icon_shift);


                    }else if(valueshift==2){
                        currentKey.label = null;
                        currentKey.icon = getResources().getDrawable(R.drawable.a_1_theme_1_background_key_icon_shift_shifted);


                    }
                    break; // leave the loop once you find your match
                }
            }
        }
    }


    public void onclickclosepopup(View view) {
     //   Toast.makeText(getApplicationContext(),"btn close",Toast.LENGTH_SHORT).show();
        mInputView.closing();


    }
    public void handlesymboleswitch()
    {
        setCurrentLanguage();
        Keyboard currentKeyboard =  mInputView.getKeyboard();
        if(currentKeyboard==mSymbolsKeyboard||
                currentKeyboard==mSymbolsfarsiKeyboard){

            setKKeyboard(mSymbolsShiftedKeyboard);
            //language="sym2";

        }else if(currentKeyboard==mSymbolsShiftedKeyboard){

            setKKeyboard(mSymbolsShiftedKeyboard3);
            //language="sym3";

        }else if(currentKeyboard==mSymbolsShiftedKeyboard3){

            setKKeyboard(mSymbolsShiftedKeyboard4);
            //language="sym4";
        }else if(currentKeyboard==mSymbolsShiftedKeyboard4){

            setKKeyboard(mSymbolsShiftedKeyboard5);
            //language="sym5";

        }else if(currentKeyboard==mSymbolsShiftedKeyboard5){

            setKKeyboard(mSymbolsShiftedKeyboard6);
            //language="sym6";
        }else if(currentKeyboard==mSymbolsShiftedKeyboard6){

            setKKeyboard(mSymbolsShiftedKeyboard7);
            //language="sym7";
        }else if(currentKeyboard==mSymbolsShiftedKeyboard7){

            setKKeyboard(mSymbolsShiftedKeyboard8);
            //language="sym8";
        }else if(currentKeyboard==mSymbolsShiftedKeyboard8){

            setKKeyboard(mSymbolsShiftedKeyboard9);
            //language="sym9";
        }else if(currentKeyboard==mSymbolsShiftedKeyboard9){

            setKKeyboard(mSymbolsShiftedKeyboard10);
            //language="sym10";
        }else if(currentKeyboard==mSymbolsShiftedKeyboard10){

            setKKeyboard(mSymbolsShiftedKeyboard11);
            //language="sym11";
        }else if(currentKeyboard==mSymbolsShiftedKeyboard11){

            setKKeyboard(mSymbolsShiftedKeyboard12);
            //language="sym12";
        }else if(currentKeyboard==mSymbolsShiftedKeyboard12){

            setKKeyboard(mSymbolsShiftedKeyboard13);
            //language="sym13";
        }
        else if(currentKeyboard==mSymbolsShiftedKeyboard13){
            if(prelanguage.equals("en")) {
                setKKeyboard(mSymbolsKeyboard);
                //language = "sym1";
            }else {
                setKKeyboard(mSymbolsfarsiKeyboard);
                //language = "symfarsi1";
            }
        }

    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
         super.onConfigurationChanged(newConfig);
       // handlecurrentkeyboard();
        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){

        }

    }
    public void handlecurrentkeyboard(){
        Keyboard currentKeyboardtemp =  mInputView.getKeyboard();


        if(currentKeyboardtemp==mQwertyKeyboard){
            mCurKeyboard= mQwertyKeyboard;
        }else if(currentKeyboardtemp==mfarsiKeyboard){
            mCurKeyboard=mfarsiKeyboard;
        }else if(currentKeyboardtemp==mSymbolsKeyboard){
            mCurKeyboard=mSymbolsKeyboard;
        }else if(currentKeyboardtemp==mSymbolsShiftedKeyboard){
            mCurKeyboard=mSymbolsShiftedKeyboard;
        }else if(currentKeyboardtemp==mSymbolsShiftedKeyboard3){
            mCurKeyboard=mSymbolsShiftedKeyboard3;
        }else if(currentKeyboardtemp==mSymbolsShiftedKeyboard4){
            mCurKeyboard=mSymbolsShiftedKeyboard4;
        }else if(currentKeyboardtemp==mSymbolsShiftedKeyboard5){
            mCurKeyboard=mSymbolsShiftedKeyboard5;
        }else if(currentKeyboardtemp==mSymbolsShiftedKeyboard6){
            mCurKeyboard=mSymbolsShiftedKeyboard6;
        }else if(currentKeyboardtemp==mSymbolsShiftedKeyboard7){
            mCurKeyboard=mSymbolsShiftedKeyboard7;
        }else if(currentKeyboardtemp==mSymbolsShiftedKeyboard8){
            mCurKeyboard=mSymbolsShiftedKeyboard8;
        }else if(currentKeyboardtemp==mSymbolsShiftedKeyboard9){
            mCurKeyboard=mSymbolsShiftedKeyboard9;
        }

        setKKeyboard(mCurKeyboard);

    }
    public void handlevibreh(int pk){
        Vibrator v = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);

        v.vibrate(pk);



    }


    public  void toastM(String str){
        Toast.makeText(getBaseContext(),str,Toast.LENGTH_SHORT).show();

    }
    public void setClipboard(String text) {
        try {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                clipboard.setText(text);
            } else {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
                clipboard.setPrimaryClip(clip);
            }
        }catch (Exception ei){

        }
    }
    public void paste() {
        int sdk = Build.VERSION.SDK_INT;
        if (sdk < Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            if (clipboard.getText() != null) {
                onText(clipboard.getText());
               }else {
                Toast.makeText(getBaseContext(),"متنی برا چسپاندن وجود ندارد"
                        ,Toast.LENGTH_SHORT).show();
            }
        } else {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);
            if (item.getText() != null) {
                onText(item.getText());
             }else {
                Toast.makeText(getBaseContext(),"متنی برا چسپاندن وجود ندارد"
                        ,Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void playClick(int keyCode,float vol){

        AudioManager am = (AudioManager)getSystemService(AUDIO_SERVICE);
         switch(keyCode) {
            case 32:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_SPACEBAR, vol);
                break;
            case Keyboard.KEYCODE_DONE:
            case 10:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_RETURN, vol);
                break;
            case Keyboard.KEYCODE_DELETE:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_DELETE, vol);
                break;
            default:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_STANDARD, vol);
        }

    }
    private void changeIconspace(){
        KKeyboard currentKeyboardtemp = (KKeyboard) mInputView.getKeyboard();

        if(listS.equals("5")||listS.equals("6")){
            if(currentKeyboardtemp==mfarsiKeyboard){
                currentKeyboardtemp.setSpaceIcon(
                        getResources().getDrawable(R.drawable.spacebarfa_white)
                );

            }else if(currentKeyboardtemp==mkurdishKeyboard){
                currentKeyboardtemp.setSpaceIcon(
                        getResources().getDrawable(R.drawable.spacebarkur_white)
                );

            }else if(currentKeyboardtemp==mQwertyKeyboard){
                currentKeyboardtemp.setSpaceIcon(
                        getResources().getDrawable(R.drawable.spacebaren_white)
                );

            }else {
                currentKeyboardtemp.setSpaceIcon(
                        getResources().getDrawable(R.drawable.spacebarwhite)
                );

            }

        }else{
            if(currentKeyboardtemp==mfarsiKeyboard){
                currentKeyboardtemp.setSpaceIcon(
                        getResources().getDrawable(R.drawable.spacebarfa)
                );

            }else if(currentKeyboardtemp==mkurdishKeyboard){
                currentKeyboardtemp.setSpaceIcon(
                        getResources().getDrawable(R.drawable.spacebarkur)
                );

            }else if(currentKeyboardtemp==mQwertyKeyboard){
                currentKeyboardtemp.setSpaceIcon(
                        getResources().getDrawable(R.drawable.spacebaren)
                );

            }else {
                currentKeyboardtemp.setSpaceIcon(
                        getResources().getDrawable(R.drawable.spacebar)
                );

            }

        }
        mInputView.invalidateKey(32);

    }

    public boolean isPortrait(){
        boolean isprot=false;
        float isportr=
                getResources().getDimension(R.dimen.isportraitdevice);
        if(isportr>0){
            isprot=true;
        }else {
            isprot=false;
        }

        return isprot;
    }


    public static boolean isTimeAutomatic(Context c) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return Settings.Global.getInt(c.getContentResolver(), Settings.Global.AUTO_TIME, 0) == 1;
        } else {
            return Settings.System.getInt(c.getContentResolver(), Settings.System.AUTO_TIME, 0) == 1;
        }


    }

    public static void trimCache(Context context) {
        try {
            File dir = context.getCacheDir();
            if (dir != null && dir.isDirectory()) {

                deleteDir(dir);
            }
        } catch (Exception e) {


            // TODO: handle exception
        }
    }
    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        // The directory is now empty so delete it
        return dir.delete();
    }
    public static String getCurrentlanguage(){
        return language;
    }
    public Bitmap bitmapconvert(Bitmap bitmap, Bitmap.Config config) {
        Bitmap convertedBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), config);
        Canvas canvas = new Canvas(convertedBitmap);
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        canvas.drawBitmap(bitmap, 0, 0, paint);
        return convertedBitmap;
    }

    //inja
    private String getmultichoiceprimaries() {

        // Toast.makeText(getBaseContext(),"getwordseparators",Toast.LENGTH_LONG).show();

        return mWordmultichoiceprimaries;
    }
    public boolean ismultichoiceprimaries(int primarycode){
       // boolean retvalue=false;

        String separators = getmultichoiceprimaries();
        //retvalue= separators.contains(String.valueOf((char)primarycode));



        return separators.contains(String.valueOf((char)primarycode));
    }



    // =====================================================================
    // Word prediction (English / Persian / Kurdish) and swipe typing
    // =====================================================================

    private PredictionEngine mPredict;
    private SuggestionStripView mStrip;
    private FrameLayout mTopRow;
    private final Handler mSuggestHandler = new Handler();
    private boolean mPredictionEnabled = true;
    private boolean mSwipeEnabled = true;
    private boolean mAutoSpace = true;
    /** false in password, e-mail, URL and number fields */
    private boolean mFieldAllowsPrediction = false;
    /** the strip shows swipe alternatives; keep them until the next key press */
    private boolean mKeepSwipeAlternatives = false;
    /** text the last swipe (or suggestion pick) inserted, so another pick can replace it */
    private String mLastInserted = null;
    private String mLastInsertedLang = null;
    private boolean mJustAutoSpaced = false;
    private SwipeDecoder mSwipeDecoder;
    private Keyboard mSwipeDecoderKeyboard;
    private int mSwipeDecoderWidth;
    private List<String> mStripWords = new ArrayList<String>();
    private String mStripLang = PredictionEngine.EN;

    private static final String PUNCT_AFTER_WORD = ".,!?;:،؛؟";

    private final Runnable mUpdateSuggestionsRunnable = new Runnable() {
        @Override public void run() {
            updateSuggestions();
        }
    };

    private void readPredictionPrefs() {
        if (preferenceKeyboard == null) {
            preferenceKeyboard = getSharedPreferences("MySharedPreference", Context.MODE_PRIVATE);
        }
        mPredictionEnabled = preferenceKeyboard.getBoolean(getString(R.string.prefprediction), true);
        mSwipeEnabled = preferenceKeyboard.getBoolean(getString(R.string.prefswipe), true);
        mAutoSpace = preferenceKeyboard.getBoolean(getString(R.string.prefautospace), true);
    }

    private static boolean fieldAllowsPrediction(EditorInfo attribute) {
        if (attribute == null) return false;
        int type = attribute.inputType;
        if ((type & InputType.TYPE_MASK_CLASS) != InputType.TYPE_CLASS_TEXT) return false;
        int variation = type & InputType.TYPE_MASK_VARIATION;
        return variation != InputType.TYPE_TEXT_VARIATION_PASSWORD
                && variation != InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                && variation != InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD
                && variation != InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
                && variation != InputType.TYPE_TEXT_VARIATION_WEB_EMAIL_ADDRESS
                && variation != InputType.TYPE_TEXT_VARIATION_URI;
    }

    private boolean predictionActive() {
        return mPredictionEnabled && mFieldAllowsPrediction && mStrip != null;
    }

    /** Dictionary language of the keyboard on screen (symbol pages keep the last language). */
    private String keyboardLang() {
        Keyboard cur = mInputView != null ? mInputView.getKeyboard() : null;
        if (cur != null) {
            if (cur == mQwertyKeyboard) return PredictionEngine.EN;
            if (cur == mfarsiKeyboard) return PredictionEngine.FA;
            if (cur == mkurdishKeyboard) return PredictionEngine.CKB;
        }
        if ("fa".equals(language)) return PredictionEngine.FA;
        if ("kr".equals(language)) return PredictionEngine.CKB;
        return PredictionEngine.EN;
    }

    private boolean isAlphabetKeyboardShown() {
        Keyboard cur = mInputView != null ? mInputView.getKeyboard() : null;
        return cur != null && (cur == mQwertyKeyboard || cur == mfarsiKeyboard || cur == mkurdishKeyboard);
    }

    /** Puts the suggestion strip on top of the tools row (same place, same background). */
    private void installSuggestionStrip() {
        mStrip = null;
        mTopRow = null;
        if (toollayout == null || !(toollayout.getParent() instanceof ViewGroup)) return;
        ViewGroup parent = (ViewGroup) toollayout.getParent();
        int index = parent.indexOfChild(toollayout);
        ViewGroup.LayoutParams lp = toollayout.getLayoutParams();
        parent.removeView(toollayout);
        mTopRow = new FrameLayout(this);
        parent.addView(mTopRow, index, lp);
        mTopRow.addView(toollayout, new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mStrip = new SuggestionStripView(this);
        mStrip.matchBackground(toollayout.getBackground());
        mTopRow.addView(mStrip, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                (int) getResources().getDimension(R.dimen.key_height_firstrow)));
        mStrip.setListener(new SuggestionStripView.Listener() {
            @Override public void onSuggestionPicked(int index, String word) {
                pickSuggestion(word);
            }

            @Override public void onSuggestionLongPressed(int index, String word) {
                String lang = mStripLang;
                WordDictionary d = mPredict.dictionary(lang);
                boolean inDict = d != null && d.indexOfKey(TextNormalizer.key(word)) >= 0;
                if (!inDict) {
                    mPredict.forget(lang, word);
                    mPredict.saveAsync();
                    toastM(getString(R.string.suggestion_removed, word));
                    updateSuggestions();
                }
            }
        });
        refreshStripVisibility();
    }

    /** The tools row or the suggestion strip takes up the row above the keys. */
    private boolean isTopRowVisible() {
        return (toollayout != null && toollayout.getVisibility() == View.VISIBLE)
                || (mStrip != null && mStrip.getVisibility() == View.VISIBLE);
    }

    private void refreshStripVisibility() {
        if (mStrip == null) return;
        boolean toolsShown = toollayout != null && toollayout.getVisibility() == View.VISIBLE;
        boolean show = predictionActive() && (!mStrip.isEmpty() || !toolsShown);
        mStrip.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void showSuggestions(List<String> words, int literalIndex, String lang) {
        if (mStrip == null) return;
        mStripWords = words == null ? new ArrayList<String>() : new ArrayList<String>(words);
        mStripLang = lang;
        mStrip.setRtl(!PredictionEngine.EN.equals(lang));
        mStrip.setSuggestions(mStripWords, literalIndex);
        refreshStripVisibility();
    }

    private void scheduleSuggestionUpdate() {
        mSuggestHandler.removeCallbacks(mUpdateSuggestionsRunnable);
        mSuggestHandler.postDelayed(mUpdateSuggestionsRunnable, 30);
    }

    /** length of the run of word characters at the end of s */
    private static int trailingWordLength(CharSequence s) {
        int i = s.length();
        while (i > 0 && TextNormalizer.isWordChar(s.charAt(i - 1))) i--;
        return s.length() - i;
    }

    private static int leadingWordLength(CharSequence s) {
        int i = 0;
        while (i < s.length() && TextNormalizer.isWordChar(s.charAt(i))) i++;
        return i;
    }

    /** the word before the current one, if only spaces separate them */
    private static String previousWord(CharSequence before, int currentLen) {
        int end = before.length() - currentLen;
        int i = end;
        while (i > 0 && before.charAt(i - 1) == ' ') i--;
        if (i == end && currentLen > 0) return null;  // no space between: not a separate word
        int wordEnd = i;
        while (i > 0 && TextNormalizer.isWordChar(before.charAt(i - 1))) i--;
        if (i == wordEnd) return null;
        return before.subSequence(i, wordEnd).toString();
    }

    private void updateSuggestions() {
        if (mKeepSwipeAlternatives) return;
        if (!predictionActive()) {
            if (mStrip != null) showSuggestions(null, -1, keyboardLang());
            return;
        }
        InputConnection ic = getCurrentInputConnection();
        if (ic == null) return;
        CharSequence selected = ic.getSelectedText(0);
        if (selected != null && selected.length() > 0) {
            showSuggestions(null, -1, keyboardLang());
            return;
        }
        CharSequence before = ic.getTextBeforeCursor(80, 0);
        if (before == null) before = "";
        int curLen = trailingWordLength(before);
        String current = before.subSequence(before.length() - curLen, before.length()).toString();
        String prev = previousWord(before, curLen);
        String lang = PredictionEngine.languageFor(keyboardLang(), current.length() > 0 ? current : prev);
        if (current.length() == 0 && (before.length() == 0
                || !Character.isWhitespace(before.charAt(before.length() - 1)))) {
            prev = null; // only predict the next word right after a space
        }
        List<String> words = mPredict.suggest(lang, prev, current, 3);
        int literal = current.length() > 0 && !words.isEmpty() && words.get(0).equals(current)
                && !mPredict.isKnown(lang, current) ? 0 : -1;
        showSuggestions(words, literal, lang);
    }

    /** Inserts a word picked on the strip: it replaces the word at the cursor. */
    private void pickSuggestion(String word) {
        InputConnection ic = getCurrentInputConnection();
        if (ic == null) return;
        handlehapticfeedback();
        ic.finishComposingText();
        mComposing.setLength(0);
        String lang = mStripLang;
        ic.beginBatchEdit();
        CharSequence before = ic.getTextBeforeCursor(80, 0);
        if (before == null) before = "";
        String prev;
        if (mKeepSwipeAlternatives && mLastInserted != null && before.toString().endsWith(mLastInserted)) {
            // replace what the swipe inserted
            ic.deleteSurroundingText(mLastInserted.length(), 0);
            CharSequence b2 = ic.getTextBeforeCursor(80, 0);
            prev = previousWord(b2 == null ? "" : b2, 0);
        } else {
            int curLen = trailingWordLength(before);
            CharSequence after = ic.getTextAfterCursor(40, 0);
            int afterLen = after == null ? 0 : leadingWordLength(after);
            prev = previousWord(before, curLen);
            ic.deleteSurroundingText(curLen, afterLen);
        }
        CharSequence after = ic.getTextAfterCursor(1, 0);
        boolean spaceFollows = after != null && after.length() > 0 && Character.isWhitespace(after.charAt(0));
        String insert = word + (mAutoSpace && !spaceFollows ? " " : "");
        ic.commitText(insert, 1);
        ic.endBatchEdit();
        mPredict.learn(lang, prev, word);
        mLastInserted = insert;
        mLastInsertedLang = lang;
        mJustAutoSpaced = insert.endsWith(" ");
        mKeepSwipeAlternatives = false;
        scheduleSuggestionUpdate();
    }

    /** Learns the word that the user just finished typing (called before a separator is typed). */
    private void learnWordBeforeCursor() {
        if (!predictionActive()) return;
        InputConnection ic = getCurrentInputConnection();
        if (ic == null) return;
        CharSequence before = ic.getTextBeforeCursor(80, 0);
        if (before == null) return;
        int curLen = trailingWordLength(before);
        if (curLen == 0) return;
        String word = before.subSequence(before.length() - curLen, before.length()).toString();
        String lang = PredictionEngine.languageFor(keyboardLang(), word);
        mPredict.learn(lang, previousWord(before, curLen), word);
    }

    /** "word ." → "word." when the space was added automatically */
    private void removeAutoSpaceBeforePunctuation(int primaryCode) {
        if (!mJustAutoSpaced || PUNCT_AFTER_WORD.indexOf((char) primaryCode) < 0) return;
        InputConnection ic = getCurrentInputConnection();
        if (ic == null) return;
        CharSequence before = ic.getTextBeforeCursor(1, 0);
        if (before != null && " ".contentEquals(before)) ic.deleteSurroundingText(1, 0);
    }

    // ---------------------------------------------------------------- swipe

    private final KKeyboardView.SwipeListener mSwipeListener = new KKeyboardView.SwipeListener() {
        @Override public boolean isSwipeAllowed() {
            return mSwipeEnabled && mFieldAllowsPrediction && isAlphabetKeyboardShown();
        }

        @Override public void onSwipeStarted() {
            if (mPreviewPopup != null && mPreviewPopup.isShowing()) mPreviewPopup.dismiss();
            mInputView.setPreviewEnabled(false);
        }

        @Override public void onSwipe(float[] xs, float[] ys, int count) {
            handleSwipe(xs, ys, count);
        }
    };

    private SwipeDecoder swipeDecoderFor(Keyboard kb) {
        if (mSwipeDecoder == null || mSwipeDecoderKeyboard != kb || mSwipeDecoderWidth != mInputView.getWidth()) {
            List<int[]> codes = new ArrayList<int[]>();
            List<int[]> rects = new ArrayList<int[]>();
            for (Keyboard.Key k : kb.getKeys()) {
                codes.add(k.codes);
                rects.add(new int[]{k.x, k.y, k.width, k.height});
            }
            mSwipeDecoder = new SwipeDecoder(codes, rects);
            mSwipeDecoderKeyboard = kb;
            mSwipeDecoderWidth = mInputView.getWidth();
        }
        return mSwipeDecoder;
    }

    private void handleSwipe(float[] xs, float[] ys, int count) {
        InputConnection ic = getCurrentInputConnection();
        Keyboard kb = mInputView.getKeyboard();
        if (ic == null || kb == null) return;
        String lang = keyboardLang();
        WordDictionary dict = mPredict.dictionary(lang);
        if (dict == null) {
            toastM(getString(R.string.dictionary_loading));
            return;
        }
        SwipeDecoder decoder = swipeDecoderFor(kb);
        if (!decoder.isUsable()) return;
        ic.finishComposingText();
        mComposing.setLength(0);
        CharSequence before = ic.getTextBeforeCursor(80, 0);
        if (before == null) before = "";
        int curLen = trailingWordLength(before);
        String prev = previousWord(before, curLen);
        List<String> words = decoder.decode(xs, ys, count, dict, mPredict, lang, prev, 3);
        if (words.isEmpty()) {
            handlehapticfeedback();
            return;
        }
        boolean shifted = mInputView.isShifted();
        List<String> shown = new ArrayList<String>();
        for (String w : words) {
            if (PredictionEngine.EN.equals(lang) && shifted) {
                w = mCapsLock ? w.toUpperCase(java.util.Locale.ROOT) : TextNormalizer.capitalize(w);
            }
            shown.add(w);
        }
        String word = shown.get(0);
        ic.beginBatchEdit();
        if (curLen > 0 || (before.length() > 0 && !Character.isWhitespace(before.charAt(before.length() - 1))
                && PUNCT_AFTER_WORD.indexOf(before.charAt(before.length() - 1)) >= 0)) {
            ic.commitText(" ", 1); // a swiped word never sticks to the text before it
        }
        String insert = word + (mAutoSpace ? " " : "");
        ic.commitText(insert, 1);
        ic.endBatchEdit();
        mPredict.learn(lang, prev, words.get(0));
        mLastInserted = insert;
        mLastInsertedLang = lang;
        mJustAutoSpaced = mAutoSpace;
        if (shifted && !mCapsLock && (kb == mQwertyKeyboard)) {
            mInputView.setShifted(false);
            changeIconShift(0);
        }
        showSuggestions(shown, -1, lang);
        mKeepSwipeAlternatives = true;
    }

    // ---------------------------------------------------------------- clipboard history

    private ClipboardManager.OnPrimaryClipChangedListener mClipListener;

    private void registerClipboardListener() {
        try {
            final ClipboardManager cm = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            if (cm == null || mClipListener != null) return;
            mClipListener = new ClipboardManager.OnPrimaryClipChangedListener() {
                @Override public void onPrimaryClipChanged() {
                    CBWatcherService.saveClip(kurdikeyboard.this);
                }
            };
            cm.addPrimaryClipChangedListener(mClipListener);
        } catch (RuntimeException ignored) {
        }
    }

    private void unregisterClipboardListener() {
        try {
            ClipboardManager cm = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            if (cm != null && mClipListener != null) cm.removePrimaryClipChangedListener(mClipListener);
        } catch (RuntimeException ignored) {
        }
        mClipListener = null;
    }

    // ---------------------------------------------------------------- safety

    /** used when the editor has gone away: every call is a harmless no-op */
    private static final InputConnection NO_OP_INPUT_CONNECTION = (InputConnection)
            java.lang.reflect.Proxy.newProxyInstance(InputConnection.class.getClassLoader(),
                    new Class<?>[]{InputConnection.class}, new java.lang.reflect.InvocationHandler() {
                        @Override
                        public Object invoke(Object proxy, java.lang.reflect.Method m, Object[] args) {
                            Class<?> t = m.getReturnType();
                            if (t == boolean.class) return false;
                            if (t == int.class) return 0;
                            if (t == long.class) return 0L;
                            if (m.getName().equals("equals")) return proxy == args[0];
                            if (m.getName().equals("hashCode")) return 0;
                            return null;
                        }
                    });

    /**
     * The 2017 code calls getCurrentInputConnection().commitText(...) etc. everywhere without
     * a null check; it is null for a moment when the app being typed in closes or loses
     * focus, which crashed the keyboard.
     */
    @Override
    public InputConnection getCurrentInputConnection() {
        InputConnection ic = super.getCurrentInputConnection();
        return ic != null ? ic : NO_OP_INPUT_CONNECTION;
    }
}



