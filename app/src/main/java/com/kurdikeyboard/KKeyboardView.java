package com.kurdikeyboard;

/**
 * Created by Stat on 11/8/2016.
 */
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.Keyboard.Key;
import android.inputmethodservice.KeyboardView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.inputmethod.InputMethodSubtype;
import android.widget.Toast;

import java.util.List;


public class KKeyboardView extends KeyboardView {

    static final int KEYCODE_OPTIONS = -100;
    // TODO: Move this into android.inputmethodservice.Keyboard
    static final int KEYCODE_LANGUAGE_SWITCH = -101;
    static final int KEYCODE_SHIFT_LOCK=-105;

    static final int KEYCODE_SELECTALL=-111;
    static final int KEYCODE_COPY=-112;
    static final int KEYCODE_CURSOR_B=-113;
    static final int KEYCODE_CURSOR_F=-114;
    static final int KEYCODE_CUT=-115;
    static final int KEYCODE_PASTE=-116;
    static final int KEYCODE_SYMBOLS_SWITCH = -117;

    static final int KEYCODE_Farsi_Tatweel_3 = -118;
    static final int KEYCODE_Farsi_Tatweel_5 = -119;
    static final int KEYCODE_Farsi_Tatweel_10 = -120;
    static final int KEYCODE_Farsi_Tatweel_15 = -121;
    static final int KEYCODE_Farsi_Tatweel_20 = -122;
    static final int KEYCODE_WWW_COM = -123;

    static final int KEYCODE_SYMBOLS_PAGE_1 = -124;
    static final int KEYCODE_SYMBOLS_PAGE_2 = -125;
    static final int KEYCODE_SYMBOLS_PAGE_3 = -126;
    static final int KEYCODE_SYMBOLS_PAGE_4 = -127;
    static final int KEYCODE_SYMBOLS_PAGE_5 = -128;
    static final int KEYCODE_SYMBOLS_PAGE_6 = -129;
    static final int KEYCODE_SYMBOLS_PAGE_7 = -130;
    static final int KEYCODE_SYMBOLS_PAGE_8 = -131;
    static final int KEYCODE_SYMBOLS_PAGE_9 = -132;
    static final int KEYCODE_SYMBOLS_PAGE_10 = -133;
    static final int KEYCODE_SYMBOLS_PAGE_11 = -134;
    static final int KEYCODE_SYMBOLS_PAGE_12 = -135;
    static final int KEYCODE_SYMBOLS_PAGE_13 = -136;

    static final int KEYCODE_LONG_P_GEH = -137;
    static final int KEYCODE_LONG_P_KEH = -138;
    static final int KEYCODE_LONG_P_DEH = -139;


    static final int KEYCODE_LONG_P_REH = -140;
    static final int KEYCODE_LONG_P_ZEH = -141;
    static final int KEYCODE_LONG_P_TEH = -142;
    static final int KEYCODE_LONG_P_ALEF = -143;
    static final int KEYCODE_LONG_P_LAM = -144;
    static final int KEYCODE_LONG_P_HEH = -145;
    static final int KEYCODE_LONG_P_AYN = -146;
    static final int KEYCODE_LONG_P_FEH = -147;
    static final int KEYCODE_LONG_P_YEH = -148;
    static final int KEYCODE_LONG_P_SAD = -149;
    static final int KEYCODE_LONG_P_VEH = -154;

    static final int KEYCODE_LONG_RUN_POPUPOPTONS = -150;
    static final int KEYCODE_CONTACCTS_READ_2=-151;
    static final int KEYCODE_Ready_Sentences
    =-152;
    static final int KEYCODE_Numbers_page_goto_symbols
            =-153;
    static final int KEYCODE_symbols_Numbers_page
            =-155;
    static final int KEYCODE_LONG_VEHHADKURDI=-156;
    static final int KEYCODE_LONG_closetobringupemoji=-157;
    static final int KEYCODE_MULTI_CHANGE=-158;
    // KEYCODE_MULTI_CHANGE used in xml file





    private float mPreviousX=0;
    private float  mPreviousY=0;
    private boolean mIsDown=false;

    private static final float one_DIP = 1.0f;

    /////////////
    private int opacity=255;
    //////////////////
   // private PopupWindow popup;

    public KKeyboardView(Context context, AttributeSet attrs) {
         super(context, attrs);

        this.mContext = context;

    }

    public KKeyboardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
    }
    @Override
    protected boolean onLongPress(Key key) {
        mSwipeLongPressed = true; // a popup is opening: this touch is not a swipe






         if (key.codes[0] ==Keyboard.KEYCODE_SHIFT){
           // Toast.makeText(getContext(),"Long press shift",Toast.LENGTH_LONG).show();
            getOnKeyboardActionListener().onKey(KEYCODE_SHIFT_LOCK,null);

            return true;
        }else if((key.codes[0]==1711)){
            getOnKeyboardActionListener().onKey(KEYCODE_LONG_P_GEH,null);

            return true;


        }else if((key.codes[0]==1705)){
            getOnKeyboardActionListener().onKey(KEYCODE_LONG_P_KEH,null);

            return true;


        }else if((key.codes[0]==1583)){
            getOnKeyboardActionListener().onKey(KEYCODE_LONG_P_DEH,null);

            return true;


        }else if((key.codes[0]==1585)){
            getOnKeyboardActionListener().onKey(KEYCODE_LONG_P_REH,null);

            return true;


        }else if((key.codes[0]==1586)){
            getOnKeyboardActionListener().onKey(KEYCODE_LONG_P_ZEH,null);

            return true;


        }else if((key.codes[0]==1591)){
            getOnKeyboardActionListener().onKey(KEYCODE_LONG_P_TEH,null);

            return true;


        }else if((key.codes[0]==1575)){
            getOnKeyboardActionListener().onKey(KEYCODE_LONG_P_ALEF,null);

            return true;


        }else if((key.codes[0]==1604)){
            getOnKeyboardActionListener().onKey(KEYCODE_LONG_P_LAM,null);

            return true;


        }else if((key.codes[0]==1608)){
            getOnKeyboardActionListener().onKey(KEYCODE_LONG_P_VEH,null);

            return true;


        }else if((key.codes[0]==1593)){
            getOnKeyboardActionListener().onKey(KEYCODE_LONG_P_AYN,null);

            return true;


        }else if((key.codes[0]==1601)){
            getOnKeyboardActionListener().onKey(KEYCODE_LONG_P_FEH,null);

            return true;


        }else if((key.codes[0]==1574)){
            getOnKeyboardActionListener().onKey(KEYCODE_LONG_P_YEH,null);

            return true;


        }else if((key.codes[0]==1589)){
            getOnKeyboardActionListener().onKey(KEYCODE_LONG_P_SAD,null);

            return true;


        }else if((key.codes[0]==-2)){
            getOnKeyboardActionListener().onKey(KEYCODE_symbols_Numbers_page,null);

            return true;


        }else if((key.codes[0]==1734)){
            getOnKeyboardActionListener().onKey(KEYCODE_LONG_VEHHADKURDI,null);

            return true;


        }else if(key.codes[0]==1632||key.codes[0]==48){
            //getOnKeyboardActionListener().onKey(1614,null);
            getOnKeyboardActionListener().onKey(KEYCODE_PASTE,null);

            return true;
        }else if(key.codes[0]==1641||key.codes[0]==57){
           // getOnKeyboardActionListener().onKey(1616,null);
            getOnKeyboardActionListener().onKey(KEYCODE_COPY,null);

            return true;
        }
        else if(key.codes[0]==1640||key.codes[0]==56){
            //getOnKeyboardActionListener().onKey(1615,null);
            getOnKeyboardActionListener().onKey(KEYCODE_CUT,null);

            return true;
        }else if(key.codes[0]==1639||key.codes[0]==55){
            //getOnKeyboardActionListener().onKey(1611,null);
            getOnKeyboardActionListener().onKey(KEYCODE_SELECTALL,null);

            return true;
        }/*else if(key.codes[0]==1638){
           // getOnKeyboardActionListener().onKey(1613,null);

            return true;
        }*/else if(key.codes[0]==1637){
           // getOnKeyboardActionListener().onKey(1612,null);
             getOnKeyboardActionListener().onKey(1614,null);

            return true;
        }else if(key.codes[0]==1636){
            getOnKeyboardActionListener().onKey(1616,null);

             return true;
        }else if(key.codes[0]==1635){
           // getOnKeyboardActionListener().onKey(1618,null);
             getOnKeyboardActionListener().onKey(1615,null);

             return true;
        }else if(key.codes[0]==1634){
           // getOnKeyboardActionListener().onKey(1620,null);
             getOnKeyboardActionListener().onKey(1617,null);


             return true;
        }else if(key.codes[0]==1633){
            /*
            if(!kurdikeyboard.language.equals("kr")) {
                getOnKeyboardActionListener().onKey(1650, null);
            }else {
                getOnKeyboardActionListener().onKey(778, null);

            }
            */
             getOnKeyboardActionListener().onKey(1650, null);


             return true;
        }else if (key.codes[0] == -3) {
            getOnKeyboardActionListener().onKey(KEYCODE_LONG_closetobringupemoji, null);
            return true;
        }





        else{

            return super.onLongPress(key);

        }

    }



    void setSubtypeOnSpaceKey(final InputMethodSubtype subtype) {
        try {
            final KKeyboard keyboard = (KKeyboard) getKeyboard();
            keyboard.setSpaceIcon(getResources().getDrawable(subtype.getIconResId()));
            invalidateAllKeys();
        }catch (Exception ei){
            Toast.makeText(mContext,
                   "error subtype"+ ei.getMessage(),Toast.LENGTH_LONG).show();
        }
    }






    /////////////////////////////////////////////////////


        private void printcharactor2(Key key,Canvas canvas,Paint paintforsize,Paint paintforsize2fortwolabelkey
            ,String keyLabel){
        SharedPreferences preferenceKeyboard=
                this.getContext()
                        .getSharedPreferences("MySharedPreference", Context.MODE_PRIVATE);
        boolean iskurdishextend=
                preferenceKeyboard.getBoolean(mContext.getString(R.string.preflanguage_kurdi_extend), false);

        //  String keyLabel= (String) key.label;
        if(keyLabel.equals("د")){
            if(kurdikeyboard.language.equals("fa")) {
                canvas.drawText("د", key.x + (key.width / 2),
                        key.y + 7 * (key.height / 10), paintforsize);
                canvas.drawText("ذ", key.x + (key.width / 3),
                        key.y + 4 * (key.height / 8), paintforsize2fortwolabelkey);
            }else if(iskurdishextend) {
                canvas.drawText("د", key.x + (key.width / 2),
                        key.y + 7 * (key.height / 10), paintforsize);
            }else {
                canvas.drawText("د", key.x + (key.width / 2),
                        key.y + 7 * (key.height / 10), paintforsize);
            }


        }else if(keyLabel.equals("ز")){
            if(iskurdishextend){
                canvas.drawText(keyLabel, key.x + (key.width / 2),
                        key.y + 7*(key.height/10) , paintforsize);

            }else {
                canvas.drawText(keyLabel, key.x + (key.width / 2),
                        key.y + 7*(key.height/10) , paintforsize);
                canvas.drawText("ژ", key.x + (key.width / 3),
                        key.y + 4*(key.height/7) , paintforsize2fortwolabelkey);

            }


        }else if(keyLabel.equals("ط")){
            if(iskurdishextend){
                canvas.drawText(keyLabel, key.x + 5 * (key.width / 8),
                        key.y + 7 * (key.height / 10), paintforsize);
            }else {
                canvas.drawText(keyLabel, key.x + 5 * (key.width / 8),
                        key.y + 7 * (key.height / 10), paintforsize);
                canvas.drawText("ظ", key.x + (key.width / 4),
                        key.y + 4 * (key.height / 7), paintforsize2fortwolabelkey);
            }


        }else if(keyLabel.equals("گ")){
            if(kurdikeyboard.language.equals("fa")) {
                canvas.drawText("گ", key.x + (key.width / 2),
                        key.y + 7 * (key.height / 10), paintforsize);
                canvas.drawText("ء", key.x + (key.width / 3),
                        key.y + 3 * (key.height / 7), paintforsize2fortwolabelkey);
            }else {
                canvas.drawText("گ", key.x + (key.width / 2),
                        key.y + 7 * (key.height / 10), paintforsize);
            }



        }else if(keyLabel.equals("ک")){
            if(iskurdishextend){
                canvas.drawText(keyLabel, key.x + (key.width / 2),
                        key.y + 7 * (key.height / 10), paintforsize);

            }else {
                canvas.drawText(keyLabel, key.x + (key.width / 2),
                        key.y + 7 * (key.height / 10), paintforsize);
                canvas.drawText("ئ", key.x + 3 * (key.width / 11),
                        key.y + 3 * (key.height / 7), paintforsize2fortwolabelkey);

            }





        }else if(keyLabel.equals("ی")){
            canvas.drawText(keyLabel, key.x + (key.width / 2),
                    key.y + 7*(key.height/10) , paintforsize);


        }else if(keyLabel.equals("ا")){
            if(kurdikeyboard.language.equals("fa")) {

                canvas.drawText("ا", key.x + 3*(key.width / 5),
                        key.y + 7*(key.height/10) , paintforsize);
                canvas.drawText("آ", key.x + (key.width / 3),
                        key.y + 4*(key.height/7) , paintforsize2fortwolabelkey);
            }else {
                canvas.drawText("ا", key.x + (key.width / 2),
                        key.y + 7 * (key.height / 10), paintforsize);
            }


        }else if(keyLabel.equals("و")){
            if(kurdikeyboard.language.equals("fa")) {

                canvas.drawText("و", key.x + 3*(key.width / 5),
                        key.y + 7*(key.height/10) , paintforsize);
                canvas.drawText("\u0624", key.x + (key.width / 3),
                        key.y + 4*(key.height/8) , paintforsize2fortwolabelkey);


            }else {
                canvas.drawText("و", key.x + 3*(key.width / 5),
                        key.y + 7*(key.height/10) , paintforsize);
            }


        }else if(keyLabel.equals("ف")){

            if(kurdikeyboard.language.equals("fa")) {
                canvas.drawText("ف", key.x + 4*(key.width / 8),
                        key.y + 7*(key.height/10) , paintforsize);
            }else if(iskurdishextend){
                canvas.drawText("ف", key.x + 4*(key.width / 8),
                        key.y + 7*(key.height/10) , paintforsize);

            }else {
                canvas.drawText("ف", key.x + (key.width / 2),
                        key.y + 8*(key.height/10) , paintforsize);
                canvas.drawText("\u06A4", key.x + (key.width / 3),
                        key.y + 4*(key.height/7) , paintforsize2fortwolabelkey);

            }

        }else if(keyLabel.equals("ث")){
            canvas.drawText("ث", key.x + 4*(key.width / 8),
                    key.y + 7*(key.height/10) , paintforsize);

        }else if(keyLabel.equals("خ")){
            canvas.drawText("خ", key.x + 4*(key.width / 8),
                    key.y + 7*(key.height/10) , paintforsize);

        }else if(keyLabel.equals("ل")){

            if(kurdikeyboard.language.equals("fa")) {
                canvas.drawText("ل", key.x + 3 * (key.width / 5),
                        key.y + 7 * (key.height / 10), paintforsize);
            }else if(iskurdishextend){
                canvas.drawText("ل", key.x + 3 * (key.width / 5),
                        key.y + 7 * (key.height / 10), paintforsize);

            }else {
                canvas.drawText("ل", key.x + 3 * (key.width / 5),
                        key.y + 7 * (key.height / 10), paintforsize);
                canvas.drawText("\u06B5", key.x + (key.width / 3),
                        key.y + 4 * (key.height / 7), paintforsize2fortwolabelkey);

            }


        }else if(keyLabel.equals("ر")){
            if(kurdikeyboard.language.equals("fa")||iskurdishextend) {

                canvas.drawText("ر", key.x + 3 * (key.width / 5),
                        key.y + 7 * (key.height / 10), paintforsize);
            }else {
                canvas.drawText("ر", key.x + 3 * (key.width / 5),
                        key.y + 7 * (key.height / 10), paintforsize);
                canvas.drawText("\u0695", key.x + (key.width / 3),
                        key.y + 4 * (key.height / 7), paintforsize2fortwolabelkey);

            }


        }else if(keyLabel.equals("ص")){

            canvas.drawText(keyLabel, key.x + (key.width / 2),
                    key.y + 7*(key.height/10) , paintforsize);
            canvas.drawText("ض", key.x + (key.width / 3),
                    key.y + 3*(key.height/7) , paintforsize2fortwolabelkey);


        }else if(keyLabel.equals("ع")){
            if(iskurdishextend){
                canvas.drawText(keyLabel, key.x + 5*(key.width / 8),
                        key.y + 7*(key.height/10) , paintforsize);

            }else {
                canvas.drawText("غ", key.x + 5*(key.width /15   ),
                        key.y + 4*(key.height/8) , paintforsize2fortwolabelkey);
                canvas.drawText(keyLabel, key.x + 5*(key.width / 8),
                        key.y + 7*(key.height/10) , paintforsize);

            }


        }else if(keyLabel.equals("\u06C6")){
            if(iskurdishextend){
                canvas.drawText("\u06C6", key.x + 3*(key.width / 5),
                        key.y + 7*(key.height/10) , paintforsize);

            }else {
                canvas.drawText("\u06C6", key.x + 3*(key.width / 5),
                        key.y + 7*(key.height/10) , paintforsize);
                canvas.drawText("\u06CA", key.x + (key.width / 3),
                        key.y + 4*(key.height/7) , paintforsize2fortwolabelkey);

            }


        }
        /*else if(keyLabel.equals("i")
                ||keyLabel.equals("d")
                ||keyLabel.equals("f")
                ||keyLabel.equals("h")
                ||keyLabel.equals("k")
                ||keyLabel.equals("l")
                ||keyLabel.equals("b")){
            canvas.drawText(keyLabel, key.x + (key.width / 2),
                    key.y + 6*(key.height/7) , paintforsize);


        }*/
        else{
            canvas.drawText(keyLabel, key.x + (key.width / 2),
                    key.y + 7*(key.height/10) , paintforsize);


        }

    }

    ////////////////////////////////////////////////////////

    private void printcharactor(Key key,Canvas canvas,Paint paintforsize,Paint paintforsize2fortwolabelkey
            ,String keyLabel){
        SharedPreferences preferenceKeyboard=
                this.getContext()
                        .getSharedPreferences("MySharedPreference", Context.MODE_PRIVATE);
        boolean iskurdishextend=
                preferenceKeyboard.getBoolean(mContext.getString(R.string.preflanguage_kurdi_extend), false);

        //  String keyLabel= (String) key.label;
        if(kurdikeyboard.language.equals("fa")){

            //////////////////////pfa
            if(keyLabel.equals("د")){
                canvas.drawText("د", key.x + (key.width / 2),
                        key.y + 7 * (key.height / 10), paintforsize);
                canvas.drawText("ذ", key.x + (key.width / 3),
                        key.y + 4 * (key.height / 8), paintforsize2fortwolabelkey);

            }else if(keyLabel.equals("ز")){
                canvas.drawText(keyLabel, key.x + (key.width / 2),
                        key.y + 7*(key.height/10) , paintforsize);
                canvas.drawText("ژ", key.x + (key.width / 3),
                        key.y + 4*(key.height/7) , paintforsize2fortwolabelkey);

            }else if(keyLabel.equals("ط")){
                canvas.drawText(keyLabel, key.x + 5*(key.width / 8),
                        key.y + 7*(key.height/10) , paintforsize);
                canvas.drawText("ظ", key.x + (key.width / 4),
                        key.y + 4*(key.height/7) , paintforsize2fortwolabelkey);
            }else if(keyLabel.equals("گ")){
                canvas.drawText("گ", key.x + (key.width / 2),
                        key.y + 7 * (key.height / 10), paintforsize);
                canvas.drawText("ء", key.x + (key.width / 3),
                        key.y + 3 * (key.height / 7), paintforsize2fortwolabelkey);

            }else if(keyLabel.equals("ک")){
                canvas.drawText(keyLabel, key.x + (key.width / 2),
                        key.y + 7 * (key.height / 10), paintforsize);
                canvas.drawText("ئ", key.x + 3 * (key.width / 11),
                        key.y + 3 * (key.height / 7), paintforsize2fortwolabelkey);

            } else if(keyLabel.equals("ی")){
                canvas.drawText(keyLabel, key.x + (key.width / 2),
                        key.y + 7*(key.height/10) , paintforsize);

            } else if(keyLabel.equals("ا")){
                canvas.drawText("ا", key.x + 3*(key.width / 5),
                        key.y + 7*(key.height/10) , paintforsize);
                canvas.drawText("آ", key.x + (key.width / 3),
                        key.y + 4*(key.height/7) , paintforsize2fortwolabelkey);

            } else if(keyLabel.equals("و")){
                canvas.drawText("و", key.x + 3*(key.width / 5),
                        key.y + 7*(key.height/10) , paintforsize);
                canvas.drawText("\u0624", key.x + (key.width / 3),
                        key.y + 4*(key.height/8) , paintforsize2fortwolabelkey);

            }else if(keyLabel.equals("ف")){
                canvas.drawText("ف", key.x + 4*(key.width / 8),
                        key.y + 7*(key.height/10) , paintforsize);

            } else if(keyLabel.equals("ث")){
                canvas.drawText("ث", key.x + 4*(key.width / 8),
                        key.y + 7*(key.height/10) , paintforsize);

            }else if(keyLabel.equals("خ")){
                canvas.drawText("خ", key.x + 4*(key.width / 8),
                        key.y + 7*(key.height/10) , paintforsize);

            } else if(keyLabel.equals("ل")){
                canvas.drawText("ل", key.x + 3 * (key.width / 5),
                        key.y + 7 * (key.height / 10), paintforsize);

            }else if(keyLabel.equals("ر")){
                canvas.drawText("ر", key.x + 3 * (key.width / 5),
                        key.y + 7 * (key.height / 10), paintforsize);

            }else if(keyLabel.equals("ص")){

                canvas.drawText(keyLabel, key.x + (key.width / 2),
                        key.y + 7*(key.height/10) , paintforsize);
                canvas.drawText("ض", key.x + (key.width / 3),
                        key.y + 3*(key.height/7) , paintforsize2fortwolabelkey);


            }else if(keyLabel.equals("ع")){
                canvas.drawText("غ", key.x + 5*(key.width /15   ),
                        key.y + 4*(key.height/8) , paintforsize2fortwolabelkey);
                canvas.drawText(keyLabel, key.x + 5*(key.width / 8),
                        key.y + 7*(key.height/10) , paintforsize);

            }else{
                canvas.drawText(keyLabel, key.x + (key.width / 2),
                        key.y + 7*(key.height/10) , paintforsize);


            }

            ///////////////////////pfa

        }else if(kurdikeyboard.language.equals("kr")&&(!iskurdishextend)){
            //////////////////////pkr
            if(keyLabel.equals("د")){
                canvas.drawText("د", key.x + (key.width / 2),
                        key.y + 7 * (key.height / 10), paintforsize);

            }else if(keyLabel.equals("ز")){
                canvas.drawText(keyLabel, key.x + (key.width / 2),
                        key.y + 7*(key.height/10) , paintforsize);
                canvas.drawText("ژ", key.x + (key.width / 3),
                        key.y + 4*(key.height/7) , paintforsize2fortwolabelkey);

            }else if(keyLabel.equals("گ")){
                canvas.drawText("گ", key.x + (key.width / 2),
                        key.y + 7 * (key.height / 10), paintforsize);

            }else if(keyLabel.equals("ک")){
                canvas.drawText(keyLabel, key.x + (key.width / 2),
                        key.y + 7 * (key.height / 10), paintforsize);

            } else if(keyLabel.equals("ی")){
                canvas.drawText(keyLabel, key.x + (key.width / 2),
                        key.y + 7*(key.height/10) , paintforsize);


            } else if(keyLabel.equals("ا")){
                canvas.drawText("ا", key.x + (key.width / 2),
                        key.y + 7*(key.height/10) , paintforsize);

            } else if(keyLabel.equals("و")){
                canvas.drawText("و", key.x + 3*(key.width / 5),
                        key.y + 7*(key.height/10) , paintforsize);

            }else if(keyLabel.equals("ف")){
                canvas.drawText("ف", key.x + (key.width / 2),
                        key.y + 8*(key.height/10) , paintforsize);
                canvas.drawText("\u06A4", key.x + (key.width / 3),
                        key.y + 4*(key.height/7) , paintforsize2fortwolabelkey);

            } else if(keyLabel.equals("ث")){
                canvas.drawText("ث", key.x + 4*(key.width / 8),
                        key.y + 7*(key.height/10) , paintforsize);

            }else if(keyLabel.equals("خ")){
                canvas.drawText("خ", key.x + 4*(key.width / 8),
                        key.y + 7*(key.height/10) , paintforsize);

            } else if(keyLabel.equals("ل")){
                canvas.drawText("ل", key.x + 3 * (key.width / 5),
                        key.y + 7 * (key.height / 10), paintforsize);
                canvas.drawText("\u06B5", key.x + (key.width / 3),
                        key.y + 4 * (key.height / 7), paintforsize2fortwolabelkey);

            }else if(keyLabel.equals("ر")){
                canvas.drawText("ر", key.x + 3 * (key.width / 5),
                        key.y + 7 * (key.height / 10), paintforsize);
                canvas.drawText("\u0695", key.x + (key.width / 3),
                        key.y + 4 * (key.height / 7), paintforsize2fortwolabelkey);

            }else if(keyLabel.equals("ع")){
                canvas.drawText("غ", key.x + 5*(key.width /15   ),
                        key.y + 4*(key.height/8) , paintforsize2fortwolabelkey);
                canvas.drawText(keyLabel, key.x + 5*(key.width / 8),
                        key.y + 7*(key.height/10) , paintforsize);

            }else if(keyLabel.equals("\u06C6")){
                canvas.drawText("\u06C6", key.x + 3*(key.width / 5),
                        key.y + 7*(key.height/10) , paintforsize);
                canvas.drawText("\u06CA", key.x + (key.width / 3),
                        key.y + 4*(key.height/7) , paintforsize2fortwolabelkey);

            }else{
                canvas.drawText(keyLabel, key.x + (key.width / 2),
                        key.y + 7*(key.height/10) , paintforsize);


            }
            ///////////////////////pkr
        }else{
            canvas.drawText(keyLabel, key.x + (key.width / 2),
                    key.y + 7*(key.height/10) , paintforsize);


        }








    }


    //////////////////////////////////////////////////////////
    ///////////





    ///////// design background key
    private Context  mContext;






    private boolean returnvalue(Key key){
        boolean returnvaluetemp=true;

        if(47<key.codes[0]&&key.codes[0]<58){
            returnvaluetemp= false;
        }
        if(1631<key.codes[0]&&key.codes[0]<1642){
            returnvaluetemp= false;
        }

        if(key.codes[0]==10||key.codes[0]== 46 ||key.codes[0]== 32
                ||key.codes[0]==-2 ||key.codes[0]==-3
                ||key.codes[0]==-5 ||key.codes[0]==-1
                ||key.codes[0]==8204 ||key.codes[0]==-117
                ){
            returnvaluetemp= false;
        }
        if(key.codes[0]==182||key.codes[0]== 167 ||key.codes[0]== 186
                ||key.codes[0]==177 ||key.codes[0]==449
                ||key.codes[0]==450 ||key.codes[0]==181
                ||key.codes[0]==8204 ||key.codes[0]==-117
                ||key.codes[0]==169 ||key.codes[0]==174
                ||key.codes[0]==170 ||key.codes[0]==185 ||key.codes[0]==178
                ||key.codes[0]==179 ||key.codes[0]==8308 ||key.codes[0]==8309
                ||key.codes[0]==8310 ||key.codes[0]==8311  ||key.codes[0]==8312
                ||key.codes[0]==8313 ||key.codes[0]==8304  ||key.codes[0]==8230
                ||key.codes[0]==1160 ||key.codes[0]==8986  ||key.codes[0]==10147
                ||key.codes[0]==9756 ||key.codes[0]==9754  ||key.codes[0]==9759
                ||key.codes[0]==9757 ||key.codes[0]==9755  ||key.codes[0]==9758
                ||key.codes[0]==8596 ||key.codes[0]==8597  ||key.codes[0]==8598
                ||key.codes[0]==8599 ||key.codes[0]==8600  ||key.codes[0]==8601
                ||key.codes[0]==8592 ||key.codes[0]==8593  ||key.codes[0]==8594
                ||key.codes[0]==8595 ||key.codes[0]==10143 ||key.codes[0]==10144
                ||key.codes[0]==10148 ||key.codes[0]==10153 ||key.codes[0]==10155
                ||key.codes[0]==10174 ||key.codes[0]==10173 ||key.codes[0]==10172
                ||key.codes[0]==10171 ||key.codes[0]==10170 ||key.codes[0]==9728
                ||key.codes[0]==9729 ||key.codes[0]==9730  ||key.codes[0]==9731
                ||key.codes[0]==9732 ||key.codes[0]==9735  ||key.codes[0]==9736
                ||key.codes[0]==9737 ||key.codes[0]==9738  ||key.codes[0]==9739
                ||key.codes[0]==9777 ||key.codes[0]==9778  ||key.codes[0]==9779
                ||key.codes[0]==9780 ||key.codes[0]==9781  ||key.codes[0]==9782
                ||key.codes[0]==9783 ||key.codes[0]==9784  ||key.codes[0]==9789
                ||key.codes[0]==9790 ||key.codes[0]==8560  ||key.codes[0]==8561
                ||key.codes[0]==8562 ||key.codes[0]==8563  ||key.codes[0]==8564
                ||key.codes[0]==8565 ||key.codes[0]==8566  ||key.codes[0]==8567
                ||key.codes[0]==8568 ||key.codes[0]==8569  ||key.codes[0]==  9776




                ){
            returnvaluetemp= false;
        }




        return returnvaluetemp;
    }


    private String getWordSeparators() {

        // Toast.makeText(getBaseContext(),"getwordseparators",Toast.LENGTH_LONG).show();
        String mnumbers="0123456789٠١٢٣٤٥٦٧٨٩۰۱۲۳۴۵۶۷۸۹";
        return mnumbers;
    }
    public boolean isWordnumbers(int code) {

        // Toast.makeText(getBaseContext(),"iswordseparator",Toast.LENGTH_LONG).show();

        String separators = getWordSeparators();
        return separators.contains(String.valueOf((char)code));
    }




    // =====================================================================
    // Swipe (glide) typing: hold a letter and slide over the keys.
    // =====================================================================

    public interface SwipeListener {
        /** whether swiping is enabled for the keyboard currently shown */
        boolean isSwipeAllowed();
        /** the finger was lifted after a swipe; points are in this view's coordinates */
        void onSwipe(float[] xs, float[] ys, int count);
        /** a swipe started (the key under the finger will not be typed) */
        void onSwipeStarted();
    }

    private SwipeListener mSwipeListener;
    private boolean mSwipeTracking;   // finger is down on a letter key, may become a swipe
    private boolean mSwiping;         // the touch is a swipe
    private boolean mSwipeLongPressed;
    private float[] mSwipeX = new float[256], mSwipeY = new float[256];
    private int mSwipeCount;
    private long mSwipeDownTime;
    private Key mSwipeLastKey;
    private int mSwipeKeysVisited;
    private SwipeTrail mTrail;

    public void setSwipeListener(SwipeListener l) {
        mSwipeListener = l;
    }

    public boolean isSwiping() {
        return mSwiping;
    }

    private Key letterKeyAt(float x, float y) {
        Keyboard kb = getKeyboard();
        if (kb == null) return null;
        for (Key k : kb.getKeys()) {
            if (k.isInside((int) x, (int) y)) {
                if (k.codes != null && k.codes.length > 0 && k.codes[0] > 0
                        && Character.isLetter(k.codes[0])) return k;
                return null;
            }
        }
        return null;
    }

    private void addSwipePoint(float x, float y) {
        if (mSwipeCount == mSwipeX.length) {
            mSwipeX = java.util.Arrays.copyOf(mSwipeX, mSwipeCount * 2);
            mSwipeY = java.util.Arrays.copyOf(mSwipeY, mSwipeCount * 2);
        }
        mSwipeX[mSwipeCount] = x;
        mSwipeY[mSwipeCount] = y;
        mSwipeCount++;
        if (mSwiping && mTrail != null) mTrail.add(x, y);
    }

    private void endSwipe() {
        mSwipeTracking = false;
        mSwiping = false;
        mSwipeCount = 0;
        if (mTrail != null) mTrail.clear();
    }

    @Override
    public boolean onTouchEvent(android.view.MotionEvent me) {
        if (mSwipeListener == null) return super.onTouchEvent(me);
        int action = me.getActionMasked();
        switch (action) {
            case android.view.MotionEvent.ACTION_DOWN: {
                endSwipe();
                mSwipeLongPressed = false;
                Key k = letterKeyAt(me.getX(), me.getY());
                if (k != null && mSwipeListener.isSwipeAllowed()) {
                    mSwipeTracking = true;
                    mSwipeDownTime = me.getEventTime();
                    mSwipeLastKey = k;
                    mSwipeKeysVisited = 1;
                    addSwipePoint(me.getX(), me.getY());
                }
                return super.onTouchEvent(me);
            }
            case android.view.MotionEvent.ACTION_POINTER_DOWN:
                if (mSwiping) return true;     // ignore a second finger during a swipe
                mSwipeTracking = false;        // two-finger typing: never a swipe
                return super.onTouchEvent(me);
            case android.view.MotionEvent.ACTION_MOVE: {
                if (mSwiping) {
                    for (int h = 0; h < me.getHistorySize(); h++) {
                        addSwipePoint(me.getHistoricalX(0, h), me.getHistoricalY(0, h));
                    }
                    addSwipePoint(me.getX(0), me.getY(0));
                    return true;
                }
                if (mSwipeTracking && !mSwipeLongPressed && me.getPointerCount() == 1) {
                    for (int h = 0; h < me.getHistorySize(); h++) {
                        addSwipePoint(me.getHistoricalX(0, h), me.getHistoricalY(0, h));
                    }
                    addSwipePoint(me.getX(), me.getY());
                    Key k = letterKeyAt(me.getX(), me.getY());
                    if (k != null && k != mSwipeLastKey) {
                        mSwipeKeysVisited++;
                        mSwipeLastKey = k;
                    }
                    float dx = me.getX() - mSwipeX[0], dy = me.getY() - mSwipeY[0];
                    float keyW = mSwipeLastKey != null ? mSwipeLastKey.width : 60;
                    boolean far = dx * dx + dy * dy > keyW * keyW * 0.8f;
                    boolean heldStill = me.getEventTime() - mSwipeDownTime > 450 && mSwipeKeysVisited < 2;
                    if (heldStill) {
                        mSwipeTracking = false; // a long press: leave it to the normal handling
                    } else if (far && mSwipeKeysVisited >= 2) {
                        startSwipe(me);
                        return true;
                    }
                }
                return super.onTouchEvent(me);
            }
            case android.view.MotionEvent.ACTION_UP: {
                if (mSwiping) {
                    addSwipePoint(me.getX(), me.getY());
                    float[] xs = java.util.Arrays.copyOf(mSwipeX, mSwipeCount);
                    float[] ys = java.util.Arrays.copyOf(mSwipeY, mSwipeCount);
                    int n = mSwipeCount;
                    endSwipe();
                    mSwipeListener.onSwipe(xs, ys, n);
                    return true;
                }
                endSwipe();
                return super.onTouchEvent(me);
            }
            case android.view.MotionEvent.ACTION_CANCEL:
                endSwipe();
                return super.onTouchEvent(me);
            default:
                if (mSwiping) return true;
                return super.onTouchEvent(me);
        }
    }

    private void startSwipe(android.view.MotionEvent me) {
        mSwiping = true;
        // make KeyboardView forget the pressed key so it is not typed
        android.view.MotionEvent cancel = android.view.MotionEvent.obtain(me);
        cancel.setAction(android.view.MotionEvent.ACTION_CANCEL);
        super.onTouchEvent(cancel);
        cancel.recycle();
        if (mTrail == null) {
            mTrail = new SwipeTrail(getResources().getDisplayMetrics().density);
            getOverlay().add(mTrail);
        }
        mTrail.setBounds(0, 0, getWidth(), getHeight());
        mTrail.clear();
        for (int i = 0; i < mSwipeCount; i++) mTrail.add(mSwipeX[i], mSwipeY[i]);
        mSwipeListener.onSwipeStarted();
    }

    /** The line drawn under the finger while swiping (drawn in the view overlay). */
    static final class SwipeTrail extends Drawable {
        private final android.graphics.Path path = new android.graphics.Path();
        private final Paint glow = new Paint(Paint.ANTI_ALIAS_FLAG);
        private final Paint line = new Paint(Paint.ANTI_ALIAS_FLAG);
        private boolean empty = true;

        SwipeTrail(float density) {
            for (Paint p : new Paint[]{glow, line}) {
                p.setStyle(Paint.Style.STROKE);
                p.setStrokeCap(Paint.Cap.ROUND);
                p.setStrokeJoin(Paint.Join.ROUND);
            }
            glow.setColor(0x5539A7FF);
            glow.setStrokeWidth(12 * density);
            line.setColor(0xDD1E88E5);
            line.setStrokeWidth(5 * density);
        }

        void add(float x, float y) {
            if (empty) {
                path.moveTo(x, y);
                empty = false;
            } else {
                path.lineTo(x, y);
            }
            invalidateSelf();
        }

        void clear() {
            path.reset();
            empty = true;
            invalidateSelf();
        }

        @Override public void draw(Canvas c) {
            if (empty) return;
            c.drawPath(path, glow);
            c.drawPath(path, line);
        }

        @Override public void setAlpha(int a) {}
        @Override public void setColorFilter(android.graphics.ColorFilter cf) {}
        @Override public int getOpacity() { return android.graphics.PixelFormat.TRANSLUCENT; }
    }

    // onDraw used to create every key drawable again for every key on every frame
    // (~170 call sites); keep one instance per resource instead.
    private final android.util.SparseArray<Drawable> mDrawableCache = new android.util.SparseArray<Drawable>();

    private Drawable cachedDrawable(int resId) {
        Drawable d = mDrawableCache.get(resId);
        if (d == null) {
            d = getContext().getDrawable(resId).mutate();
            mDrawableCache.put(resId, d);
        }
        d.setAlpha(255); // some branches set an opacity; start from opaque every time
        return d;
    }

    // =====================================================================
    // 2.0 "modern" themes (35 = light, 36 = dark): flat rounded keys drawn in code,
    // vector icons for the function keys, no bitmaps.
    // =====================================================================












    // =====================================================================
    // Key drawing (2.0): every theme is a palette (KeyboardTheme), drawn in code.
    // Replaces the 2017 per-theme bitmap drawing (~4900 lines).
    // =====================================================================

    private final Paint mKeyPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint mLabelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint mHintPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint mNumberPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint mSmallHintPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint mFnTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final android.graphics.RectF mKeyRect = new android.graphics.RectF();

    /** the theme chosen in the settings (old 2017 ids are mapped to new themes) */
    public KeyboardTheme currentTheme() {
        SharedPreferences prefs = getContext().getSharedPreferences("MySharedPreference", Context.MODE_PRIVATE);
        return KeyboardTheme.forId(prefs.getString(
                getContext().getString(R.string.pref_list_theme_selector), KeyboardTheme.DEFAULT_ID));
    }

    @Override
    public void onDraw(Canvas canvas) {
        // KeyboardView's own drawing is not used: everything is drawn below
        Keyboard kb = getKeyboard();
        if (kb == null) return;
        SharedPreferences prefs = getContext().getSharedPreferences("MySharedPreference", Context.MODE_PRIVATE);
        KeyboardTheme t = currentTheme();
        boolean photo = prefs.getBoolean(mContext.getString(R.string.prefimagefrbackground), false)
                && getBackground() instanceof android.graphics.drawable.BitmapDrawable;
        int keyAlpha = photo
                ? Math.max(40, Math.min(255, prefs.getInt(mContext.getString(R.string.prefseekbaropacitykey), 100)))
                : 255;

        float d = getResources().getDisplayMetrics().density;
        float sp = getResources().getDisplayMetrics().scaledDensity;
        int font = prefs.getInt(mContext.getString(R.string.prefseekbarfont), 100) / 5; // 2017 setting
        mLabelPaint.setTextAlign(Paint.Align.CENTER);
        mLabelPaint.setTextSize(font * d);
        mLabelPaint.setColor(t.text);
        mHintPaint.setTextAlign(Paint.Align.CENTER);
        mHintPaint.setTextSize(font * d * 0.6f);
        mHintPaint.setColor(t.hint);
        mNumberPaint.setTextAlign(Paint.Align.CENTER);
        mNumberPaint.setTextSize(18 * d);
        mNumberPaint.setColor(t.text);
        mSmallHintPaint.setTextAlign(Paint.Align.CENTER);
        mSmallHintPaint.setTextSize(11 * d);
        mSmallHintPaint.setColor(t.hint);
        mFnTextPaint.setTextAlign(Paint.Align.CENTER);
        mFnTextPaint.setTextSize(15 * sp);
        mFnTextPaint.setColor(t.text);

        if (!photo) canvas.drawColor(t.bg);

        boolean letters = keyboardHasLetters();
        KKeyboard kk = kb instanceof KKeyboard ? (KKeyboard) kb : null;
        float insetX = 2.5f * d, insetY = 3.5f * d, r = t.radiusDp * d;

        for (Key key : kb.getKeys()) {
            if (key.height < 8 * d || key.width < 8 * d) continue; // zero-size helper keys in the layouts
            int code = key.codes != null && key.codes.length > 0 ? key.codes[0] : 0;
            boolean isEnter = code == 10;
            boolean isFn = code == -1 || code == -5 || code == -2 || code == -3 || code == -117
                    || code == -158 || code == 8204 || code == 46 || (key.label == null && code != 32 && !isEnter);

            mKeyRect.set(key.x + insetX, key.y + insetY, key.x + key.width - insetX, key.y + key.height - insetY);
            int fill = isEnter ? (key.pressed ? darker(t.accent) : t.accent)
                    : isFn ? (key.pressed ? t.fnPressed : t.fn)
                    : (key.pressed ? t.keyPressed : t.key);
            if (!t.borderless || key.pressed || isEnter) {
                mKeyPaint.setStyle(Paint.Style.FILL);
                if (t.shadow != 0 && !photo) {
                    mKeyPaint.setColor(t.shadow);
                    mKeyRect.offset(0, 1.2f * d);
                    canvas.drawRoundRect(mKeyRect, r, r, mKeyPaint);
                    mKeyRect.offset(0, -1.2f * d);
                }
                mKeyPaint.setColor(withAlpha(fill, keyAlpha));
                canvas.drawRoundRect(mKeyRect, r, r, mKeyPaint);
                if (t.stroke != 0) {
                    mKeyPaint.setStyle(Paint.Style.STROKE);
                    mKeyPaint.setStrokeWidth(1 * d);
                    mKeyPaint.setColor(t.stroke);
                    canvas.drawRoundRect(mKeyRect, r, r, mKeyPaint);
                }
            }

            float cx = mKeyRect.centerX(), cy = mKeyRect.centerY();
            float icon = Math.min(mKeyRect.height() * 0.46f, 26 * d);

            switch (code) {
                case 10: {
                    int action = kk != null ? kk.enterAction : 0;
                    int res = R.drawable.ic_kk_return;
                    if ((action & android.view.inputmethod.EditorInfo.IME_FLAG_NO_ENTER_ACTION) == 0) {
                        switch (action & android.view.inputmethod.EditorInfo.IME_MASK_ACTION) {
                            case android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH: res = R.drawable.ic_kk_search; break;
                            case android.view.inputmethod.EditorInfo.IME_ACTION_SEND: res = R.drawable.ic_kk_send; break;
                            case android.view.inputmethod.EditorInfo.IME_ACTION_GO:
                            case android.view.inputmethod.EditorInfo.IME_ACTION_NEXT: res = R.drawable.ic_kk_arrow_right; break;
                            case android.view.inputmethod.EditorInfo.IME_ACTION_DONE: res = R.drawable.ic_kk_check; break;
                        }
                    }
                    drawIcon(canvas, res, cx, cy, icon, t.onAccent);
                    continue;
                }
                case -1:
                    drawIcon(canvas, isShifted() ? R.drawable.ic_kk_shift_filled : R.drawable.ic_kk_shift,
                            cx, cy, icon, isShifted() ? t.accent : t.text);
                    continue;
                case -5:
                    drawIcon(canvas, R.drawable.ic_kk_backspace, cx, cy, icon, t.text);
                    continue;
                case -3:
                    drawIcon(canvas, R.drawable.ic_kk_emoji, cx, cy, icon, t.text);
                    continue;
                case 32: {
                    String lang = "fa".equals(kurdikeyboard.language) ? "فارسی"
                            : "kr".equals(kurdikeyboard.language) ? "کوردی" : "English";
                    float base = cy + mFnTextPaint.getTextSize() / 3;
                    float half = mFnTextPaint.measureText(lang) / 2 + 10 * d;
                    mFnTextPaint.setColor(t.hint);
                    canvas.drawText(lang, cx, base, mFnTextPaint);
                    canvas.drawText("◂", cx - half, base, mFnTextPaint); // slide space to switch language
                    canvas.drawText("▸", cx + half, base, mFnTextPaint);
                    mFnTextPaint.setColor(t.text);
                    continue;
                }
                case -2: {
                    String s = letters ? "?123" : ("en".equals(kurdikeyboard.language) ? "ABC" : "الف");
                    canvas.drawText(s, cx, cy + mFnTextPaint.getTextSize() / 3, mFnTextPaint);
                    continue;
                }
                case -158: {
                    int v = KKeyboard.mMulti_change_value;
                    int res = v == -112 ? R.drawable.ic_kk_copy : v == -116 ? R.drawable.ic_kk_paste
                            : v == -150 ? R.drawable.ic_kk_clipboard : v == -152 ? R.drawable.ic_kk_notes
                            : v == -157 ? R.drawable.ic_kk_emoji : 0;
                    if (res != 0) {
                        drawIcon(canvas, res, cx, cy, icon * 0.9f, t.text);
                    } else {
                        canvas.drawText(new String(Character.toChars(v > 0 ? v : 46)), cx,
                                cy + mNumberPaint.getTextSize() / 3, mNumberPaint);
                        // a tiny dot tells that a long press opens more symbols
                        mKeyPaint.setStyle(Paint.Style.FILL);
                        mKeyPaint.setColor(t.hint);
                        canvas.drawCircle(mKeyRect.right - 6 * d, mKeyRect.top + 6 * d, 1.6f * d, mKeyPaint);
                    }
                    continue;
                }
                case 8204: {
                    float size = mFnTextPaint.getTextSize();
                    mFnTextPaint.setTextSize(size * 0.8f);
                    String s = "نیم‌فاصله";
                    if (mFnTextPaint.measureText(s) > mKeyRect.width() - 6 * d) s = "␣";
                    canvas.drawText(s, cx, cy + mFnTextPaint.getTextSize() / 3, mFnTextPaint);
                    mFnTextPaint.setTextSize(size);
                    continue;
                }
            }

            if (key.label != null) {
                String keyLabel = key.label.toString();
                if (isShifted()) keyLabel = keyLabel.toUpperCase();
                if (returnvalue(key)) {
                    printcharactor(key, canvas, mLabelPaint, mHintPaint, keyLabel);
                } else {
                    drawNumberHint(canvas, key, keyLabel, t);
                    canvas.drawText(key.label.toString(), cx, key.y + 7 * (key.height / 10), mNumberPaint);
                }
            } else if (key.icon != null) {
                // any other function key: its own icon, recolored to the theme
                Drawable ic = key.icon.getConstantState() != null
                        ? key.icon.getConstantState().newDrawable(getResources()).mutate() : key.icon;
                ic.setColorFilter(new android.graphics.PorterDuffColorFilter(t.text, android.graphics.PorterDuff.Mode.SRC_IN));
                int s = (int) icon;
                ic.setBounds((int) (cx - s / 2f), (int) (cy - s / 2f), (int) (cx + s / 2f), (int) (cy + s / 2f));
                ic.draw(canvas);
            }
        }
    }

    /** number row: shortcuts on 7-0 (select all, cut, copy, paste) and Arabic marks on ١-٥ */
    private void drawNumberHint(Canvas canvas, Key key, String label, KeyboardTheme t) {
        int res = 0;
        switch (label) {
            case "7": case "٧": res = R.drawable.ic_kk_select_all; break;
            case "8": case "٨": res = R.drawable.ic_kk_cut; break;
            case "9": case "٩": res = R.drawable.ic_kk_copy; break;
            case "0": case "٠": res = R.drawable.ic_kk_paste; break;
        }
        if (res != 0) {
            float s = Math.min(key.width, key.height) * 0.26f;
            drawIcon(canvas, res, key.x + key.width * 0.78f, key.y + key.height * 0.70f, s, t.hint);
            return;
        }
        String mark = null;
        switch (label) {
            case "٥": mark = "َ"; break;
            case "٤": mark = "ِ"; break;
            case "٣": mark = "ُ"; break;
            case "٢": mark = "ّ"; break;
            case "١": mark = "ٲ"; break;
        }
        if (mark != null) {
            canvas.drawText(mark, key.x + key.width * 0.25f, key.y + key.height * 0.62f, mSmallHintPaint);
        }
    }

    private boolean keyboardHasLetters() {
        for (Key k : getKeyboard().getKeys()) {
            if (k.codes != null && k.codes.length > 0 && k.codes[0] > 64 && Character.isLetter(k.codes[0])) {
                return true;
            }
        }
        return false;
    }

    private void drawIcon(Canvas canvas, int res, float cx, float cy, float size, int color) {
        Drawable dr = cachedDrawable(res);
        dr.setTint(color);
        int s = (int) size;
        dr.setBounds((int) (cx - s / 2f), (int) (cy - s / 2f), (int) (cx + s / 2f), (int) (cy + s / 2f));
        dr.draw(canvas);
    }

    private static int withAlpha(int color, int alpha255) {
        int a = ((color >>> 24) * alpha255) / 255;
        return (a << 24) | (color & 0x00FFFFFF);
    }

    private static int darker(int c) {
        float[] hsv = new float[3];
        Color.colorToHSV(c, hsv);
        hsv[2] *= 0.82f;
        return Color.HSVToColor(Color.alpha(c), hsv);
    }
}
