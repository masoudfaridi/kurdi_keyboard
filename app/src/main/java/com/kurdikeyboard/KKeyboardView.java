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
import android.view.View;
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
    /** number pad: back to the letters of the current language */
    static final int KEYCODE_BACK_TO_LETTERS=-170;
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
        // number pad / symbol pages: long press = hold-and-slide options (the copy/paste
        // shortcuts on 7-0 belong to the number row above the letters)
        if (!keyboardHasLetters() && (key.popupCharacters != null || key.popupResId != 0)
                && openSlidePopup(key)) {
            mSlideDownX = mLastTouchX;
            mSlideDownY = mLastTouchY;
            return true;
        }






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
            // bottom-left key: tap = emoji, long press = hide / show suggestions and tools row
            getOnKeyboardActionListener().onKey(KEYCODE_TOGGLE_TOP_ROW, null);
            return true;
        }





        else{
            if (openSlidePopup(key)) {
                mSlideDownX = mLastTouchX;
                mSlideDownY = mLastTouchY;
                return true;
            }
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
    private float mLastTouchX, mLastTouchY;
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
        mLastTouchX = me.getX();
        mLastTouchY = me.getY();
        boolean consumed = onTouchEventInner(me);
        trackKeyPreview(me); // after the gesture handlers, so swipe / slide / space states are known
        return consumed;
    }

    private boolean onTouchEventInner(android.view.MotionEvent me) {
        if (mSlideActive && me.getActionMasked() == android.view.MotionEvent.ACTION_POINTER_UP
                && me.getActionIndex() == 0) {
            // the finger that opened the popup was lifted while another one is down: close it
            closeSlidePopup();
            return true;
        }
        if (mSlideActive) {
            int a = me.getActionMasked();
            boolean r = handleSlideTouch(me);
            if (a == android.view.MotionEvent.ACTION_UP || a == android.view.MotionEvent.ACTION_CANCEL) {
                // let KeyboardView release the pressed key without typing it
                android.view.MotionEvent cancel = android.view.MotionEvent.obtain(me);
                cancel.setAction(android.view.MotionEvent.ACTION_CANCEL);
                super.onTouchEvent(cancel);
                cancel.recycle();
                endSwipe();
            }
            return r;
        }
        if (handleSpaceGesture(me)) return true;
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
            case android.view.MotionEvent.ACTION_POINTER_UP:
                if (mSwiping && me.getActionIndex() == 0) {
                    // the swiping finger was lifted first: finish the word with the path so far
                    float[] xs = java.util.Arrays.copyOf(mSwipeX, mSwipeCount);
                    float[] ys = java.util.Arrays.copyOf(mSwipeY, mSwipeCount);
                    int n = mSwipeCount;
                    endSwipe();
                    mSwipeListener.onSwipe(xs, ys, n);
                    return true;
                }
                if (mSwiping) return true;
                return super.onTouchEvent(me);
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
        int font = prefs.getInt(mContext.getString(R.string.prefseekbarfont), 116) / 5; // 2017 setting
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

        // pro themes: glows need a software layer before Android 9
        if (t.pro != null && android.os.Build.VERSION.SDK_INT < 28 && getLayerType() != LAYER_TYPE_SOFTWARE) {
            setLayerType(LAYER_TYPE_SOFTWARE, null);
        }
        mLabelPaint.clearShadowLayer();
        mNumberPaint.clearShadowLayer();
        mFnTextPaint.clearShadowLayer();
        if (!photo) {
            if (t.pro != null) ProPainter.background(canvas, t, getWidth(), getHeight(), d);
            else canvas.drawColor(t.bg);
        }

        boolean letters = keyboardHasLetters();
        KKeyboard kk = kb instanceof KKeyboard ? (KKeyboard) kb : null;
        float insetX = 2.5f * d, insetY = 3.5f * d, r = t.radiusDp * d;

        for (Key key : kb.getKeys()) {
            if (key.height < 8 * d || key.width < 8 * d) continue; // zero-size helper keys in the layouts
            int code = key.codes != null && key.codes.length > 0 ? key.codes[0] : 0;
            boolean isEnter = code == 10;
            boolean isFn = code == -1 || code == -5 || code == -2 || code == -3 || code == -117
                    || code == -170 || code == -153
                    || code == -158 || code == 8204 || code == 46 || (key.label == null && code != 32 && !isEnter);

            mKeyRect.set(key.x + insetX, key.y + insetY, key.x + key.width - insetX, key.y + key.height - insetY);
            int fill = isEnter ? (key.pressed ? darker(t.accent) : t.accent)
                    : isFn ? (key.pressed ? t.fnPressed : t.fn)
                    : (key.pressed ? t.keyPressed : t.key);
            if (t.pro != null) {
                // pro theme: gradients, glow, 3D edge, rainbow (ProPainter)
                int tint = t.pro.rainbow ? ProPainter.rainbow((key.x + key.width / 2f) / Math.max(1, getWidth()),
                        (key.y + key.height / 2f) / Math.max(1, getHeight()), t.isDark()) : 0;
                ProPainter.key(canvas, mKeyPaint, mKeyRect, r, t,
                        isEnter ? ProPainter.ENTER : isFn ? ProPainter.FUNCTION : ProPainter.LETTER,
                        key.pressed, tint, d, keyAlpha);
                mKeyRect.offset(0, ProPainter.sink(t, key.pressed, d));
                int lc = t.pro.rainbow ? tint : t.text;
                mLabelPaint.setColor(lc);
                mNumberPaint.setColor(lc);
                mFnTextPaint.setColor(lc);
                if (t.pro.rainbow) mHintPaint.setColor((tint & 0x00FFFFFF) | 0xB3000000);
                if (t.pro.labelGlow) {
                    mLabelPaint.setShadowLayer(4 * d, 0, 0, lc);
                    mNumberPaint.setShadowLayer(4 * d, 0, 0, lc);
                    mFnTextPaint.setShadowLayer(3 * d, 0, 0, lc);
                }
            } else if (!t.borderless || key.pressed || isEnter) {
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
                    if (!letters) { // number pad / symbols: a plain space bar
                        mFnTextPaint.setColor(t.hint);
                        canvas.drawText("␣", cx, cy + mFnTextPaint.getTextSize() / 3, mFnTextPaint);
                        mFnTextPaint.setColor(t.text);
                        continue;
                    }
                    String lang = "fa".equals(kurdikeyboard.language) ? "فارسی"
                            : "kr".equals(kurdikeyboard.language) ? "کُردی" : "English";
                    float base = cy + mFnTextPaint.getTextSize() / 3;
                    mFnTextPaint.setColor(t.hint);
                    canvas.drawText(lang, cx, base, mFnTextPaint); // hold space to switch language
                    mFnTextPaint.setColor(t.text);
                    continue;
                }
                case -170: {
                    String s = "en".equals(kurdikeyboard.language) ? "ABC"
                            : "kr".equals(kurdikeyboard.language) ? "ئ ب پ" : "الف";
                    canvas.drawText(s, cx, cy + mFnTextPaint.getTextSize() / 3, mFnTextPaint);
                    continue;
                }
                case -153:
                    canvas.drawText("#+=", cx, cy + mFnTextPaint.getTextSize() / 3, mFnTextPaint);
                    continue;
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

            if (key.label != null && !letters && key.popupCharacters != null && key.popupCharacters.length() > 0) {
                // number pad / symbols: the first long-press option as a corner hint
                String pc = key.popupCharacters.toString();
                String first = new String(Character.toChars(pc.codePointAt(0)));
                canvas.drawText(first, mKeyRect.right - 9 * d, mKeyRect.top + mSmallHintPaint.getTextSize() + 2 * d,
                        mSmallHintPaint);
            }
            if (key.label != null) {
                String keyLabel = key.label.toString();
                if (isShifted()) keyLabel = keyLabel.toUpperCase();
                if (returnvalue(key)) {
                    printcharactor(key, canvas, mLabelPaint, mHintPaint, keyLabel);
                } else {
                    if (letters) drawNumberHint(canvas, key, keyLabel, t); // shortcuts only above the letters
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

    // =====================================================================
    // Hold-and-slide: a long press opens a strip of alternatives above the key; slide the
    // finger to one and lift to type it (lift without sliding = nothing). Replaces the 2017
    // mini keyboard, which needed a second tap.
    // =====================================================================

    private static final class SlideItem {
        final int code;
        final String text;
        final Drawable icon;
        float x, y, w, h; // in the popup
        SlideItem(int code, String text, Drawable icon) { this.code = code; this.text = text; this.icon = icon; }
    }

    private android.widget.PopupWindow mSlideWindow;
    private SlidePopupView mSlideView;
    private final List<List<SlideItem>> mSlideRows = new java.util.ArrayList<>();
    private int mSlideSelected = -1;
    private boolean mSlideActive, mSlideMoved;
    private float mSlideDownX, mSlideDownY;
    private final int[] mSlideOrigin = new int[2]; // popup position relative to this view

    /** opens the slide popup for a key with alternatives; false if it has none */
    private boolean openSlidePopup(Key key) {
        List<SlideItem> items = new java.util.ArrayList<>();
        if (key.popupCharacters != null && key.popupCharacters.length() > 0) {
            String chars = key.popupCharacters.toString();
            for (int i = 0; i < chars.length(); ) {
                int cp = chars.codePointAt(i);
                items.add(new SlideItem(cp, new String(Character.toChars(cp)), null));
                i += Character.charCount(cp);
            }
        } else if (key.popupResId != 0) {
            Keyboard popup;
            try {
                popup = new Keyboard(getContext(), key.popupResId);
            } catch (RuntimeException e) {
                return false;
            }
            for (Key k : popup.getKeys()) {
                int code = k.codes != null && k.codes.length > 0 ? k.codes[0] : 0;
                String text = k.label != null ? k.label.toString() : null;
                if (text == null && k.icon == null && code > 0) text = new String(Character.toChars(code));
                if (text == null && k.icon == null) continue;
                SlideItem it = new SlideItem(code, text, k.icon);
                it.y = k.y; it.x = k.x; // used for grouping into rows
                items.add(it);
            }
        }
        if (items.isEmpty()) return false;

        // rows as in the popup layout (by y), max 8 per row
        mSlideRows.clear();
        java.util.TreeMap<Integer, List<SlideItem>> byY = new java.util.TreeMap<>();
        for (SlideItem it : items) {
            int row = Math.round(it.y);
            if (!byY.containsKey(row)) byY.put(row, new java.util.ArrayList<SlideItem>());
            byY.get(row).add(it);
        }
        for (List<SlideItem> row : byY.values()) {
            for (int i = 0; i < row.size(); i += 8) mSlideRows.add(new java.util.ArrayList<>(row.subList(i, Math.min(row.size(), i + 8))));
        }
        float d = getResources().getDisplayMetrics().density;
        int maxCols = 1;
        for (List<SlideItem> r : mSlideRows) maxCols = Math.max(maxCols, r.size());
        float cellW = Math.min(Math.max(key.width * 0.95f, 40 * d), (getWidth() - 16 * d) / maxCols);
        float cellH = Math.max(key.height * 0.9f, 44 * d);
        float pad = 6 * d;
        int popW = (int) (cellW * maxCols + 2 * pad);
        int popH = (int) (cellH * mSlideRows.size() + 2 * pad);
        for (int r = 0; r < mSlideRows.size(); r++) {
            List<SlideItem> row = mSlideRows.get(r);
            float rowStart = pad + (popW - 2 * pad - cellW * row.size()) / 2f;
            for (int c = 0; c < row.size(); c++) {
                SlideItem it = row.get(c);
                it.x = rowStart + c * cellW;
                it.y = pad + r * cellH;
                it.w = cellW;
                it.h = cellH;
            }
        }
        // above the key, centred on it, inside the screen width
        int x = (int) (key.x + key.width / 2f - popW / 2f);
        x = Math.max(0, Math.min(getWidth() - popW, x));
        // same distance from the finger as the key preview (setting "prefpreviewgap")
        int gapDp = getContext().getSharedPreferences("MySharedPreference", Context.MODE_PRIVATE)
                .getInt(getContext().getString(R.string.prefpreviewgap), 24);
        int y = (int) (key.y - popH - gapDp * d);
        int[] win = new int[2];
        getLocationInWindow(win);
        y = Math.max(y, -win[1]); // stay inside the keyboard window: over the tools row if needed
        mSlideOrigin[0] = x;
        mSlideOrigin[1] = y;

        if (mSlideView == null) {
            mSlideView = new SlidePopupView(getContext());
            mSlideWindow = new android.widget.PopupWindow(mSlideView);
            mSlideWindow.setTouchable(false);
            mSlideWindow.setClippingEnabled(false);
            mSlideWindow.setBackgroundDrawable(new android.graphics.drawable.ColorDrawable(0));
        }
        mSlideView.theme = currentTheme();
        mSlideWindow.setWidth(popW);
        mSlideWindow.setHeight(popH);
        mSlideSelected = -1;
        int[] loc = new int[2];
        getLocationInWindow(loc);
        try {
            mSlideWindow.showAtLocation(this, android.view.Gravity.NO_GRAVITY, loc[0] + x, loc[1] + y);
        } catch (RuntimeException e) {
            return false;
        }
        mSlideActive = true;
        mSlideMoved = false;
        hideKeyPreview();
        return true;
    }

    /** item under a point of this view (nearest in the row when between cells), or -1 */
    private int slideItemAt(float vx, float vy) {
        float px = vx - mSlideOrigin[0], py = vy - mSlideOrigin[1];
        if (mSlideRows.isEmpty()) return -1;
        // the row: by height, clamped (the finger is usually below the popup, on the key)
        int r = 0;
        float rowH = mSlideRows.get(0).get(0).h;
        float top = mSlideRows.get(0).get(0).y;
        r = (int) Math.floor((py - top) / rowH);
        r = Math.max(0, Math.min(mSlideRows.size() - 1, r));
        List<SlideItem> row = mSlideRows.get(r);
        int best = 0;
        float bestD = Float.MAX_VALUE;
        for (int i = 0; i < row.size(); i++) {
            float cx = row.get(i).x + row.get(i).w / 2;
            float dist = Math.abs(cx - px);
            if (dist < bestD) { bestD = dist; best = i; }
        }
        int index = 0;
        for (int i = 0; i < r; i++) index += mSlideRows.get(i).size();
        return index + best;
    }

    private SlideItem slideItem(int index) {
        for (List<SlideItem> row : mSlideRows) {
            if (index < row.size()) return row.get(index);
            index -= row.size();
        }
        return null;
    }

    private void closeSlidePopup() {
        mSlideActive = false;
        if (mSlideWindow != null && mSlideWindow.isShowing()) {
            try { mSlideWindow.dismiss(); } catch (RuntimeException ignored) {}
        }
    }

    /** touch handling while the slide popup is open; true = consumed */
    private boolean handleSlideTouch(android.view.MotionEvent me) {
        switch (me.getActionMasked()) {
            case android.view.MotionEvent.ACTION_MOVE: {
                float dx = me.getX() - mSlideDownX, dy = me.getY() - mSlideDownY;
                float slop = 6 * getResources().getDisplayMetrics().density;
                if (dx * dx + dy * dy > slop * slop) mSlideMoved = true;
                if (mSlideMoved) {
                    int sel = slideItemAt(me.getX(), me.getY());
                    if (sel != mSlideSelected) {
                        mSlideSelected = sel;
                        if (mSlideView != null) mSlideView.invalidate();
                        performHapticFeedback(android.view.HapticFeedbackConstants.CLOCK_TICK);
                    }
                }
                return true;
            }
            case android.view.MotionEvent.ACTION_UP: {
                SlideItem it = mSlideMoved && mSlideSelected >= 0 ? slideItem(mSlideSelected) : null;
                closeSlidePopup();
                if (it != null && getOnKeyboardActionListener() != null) {
                    // a special key (negative code, e.g. "ـ5ـ" = five kashidas) runs its action;
                    // otherwise a one-letter label is a key and a longer label is typed as text
                    if (it.code < 0 || (it.code > 0 && (it.text == null || it.text.codePointCount(0, it.text.length()) <= 1))) {
                        getOnKeyboardActionListener().onKey(it.code, new int[]{it.code});
                    } else if (it.text != null) {
                        getOnKeyboardActionListener().onText(it.text);
                    }
                }
                return true;
            }
            case android.view.MotionEvent.ACTION_CANCEL:
                closeSlidePopup();
                return true;
            default:
                return true;
        }
    }

    /** the popup's drawing, in the keyboard theme's colors */
    private final class SlidePopupView extends View {
        KeyboardTheme theme;
        private final Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        private final android.graphics.RectF r = new android.graphics.RectF();

        SlidePopupView(Context c) { super(c); }

        @Override
        protected void onDraw(Canvas c) {
            if (theme == null) return;
            float d = getResources().getDisplayMetrics().density;
            r.set(0, 0, getWidth(), getHeight());
            p.setStyle(Paint.Style.FILL);
            p.setColor(0x33000000);
            r.offset(0, 2 * d);
            c.drawRoundRect(r, 14 * d, 14 * d, p);
            r.offset(0, -2 * d);
            r.inset(0, 1 * d);
            p.setColor(theme.isDark() ? theme.keyPressed : 0xFFFFFFFF);
            c.drawRoundRect(r, 14 * d, 14 * d, p);
            int index = 0;
            p.setTextAlign(Paint.Align.CENTER);
            for (List<SlideItem> row : mSlideRows) {
                for (SlideItem it : row) {
                    boolean sel = index == mSlideSelected;
                    if (sel) {
                        r.set(it.x + 2 * d, it.y + 2 * d, it.x + it.w - 2 * d, it.y + it.h - 2 * d);
                        p.setColor(theme.accent);
                        c.drawRoundRect(r, 9 * d, 9 * d, p);
                    }
                    int color = sel ? theme.onAccent : theme.text;
                    float cx = it.x + it.w / 2, cy = it.y + it.h / 2;
                    int vec = it.code == -112 ? R.drawable.ic_kk_copy : it.code == -116 ? R.drawable.ic_kk_paste
                            : it.code == -150 ? R.drawable.ic_kk_clipboard : it.code == -152 ? R.drawable.ic_kk_notes
                            : it.code == -157 ? R.drawable.ic_kk_emoji : it.code == -151 ? R.drawable.ic_kk_person
                            : it.code == -111 ? R.drawable.ic_kk_select_all : it.code == -115 ? R.drawable.ic_kk_cut : 0;
                    if (vec != 0) {
                        Drawable ic = cachedDrawable(vec);
                        ic.setTint(color);
                        int s = (int) (Math.min(it.w, it.h) * 0.48f);
                        ic.setBounds((int) (cx - s / 2f), (int) (cy - s / 2f), (int) (cx + s / 2f), (int) (cy + s / 2f));
                        ic.draw(c);
                    } else if (it.icon != null && (it.text == null || it.text.isEmpty())) {
                        Drawable ic = it.icon.getConstantState() != null
                                ? it.icon.getConstantState().newDrawable(getResources()).mutate() : it.icon;
                        ic.setColorFilter(new android.graphics.PorterDuffColorFilter(color, android.graphics.PorterDuff.Mode.SRC_IN));
                        int s = (int) (Math.min(it.w, it.h) * 0.5f);
                        ic.setBounds((int) (cx - s / 2f), (int) (cy - s / 2f), (int) (cx + s / 2f), (int) (cy + s / 2f));
                        ic.draw(c);
                    } else if (it.text != null) {
                        p.setColor(color);
                        float size = it.h * 0.42f;
                        p.setTextSize(size);
                        while (p.measureText(it.text) > it.w - 6 * d && size > 8 * d) {
                            size -= d;
                            p.setTextSize(size);
                        }
                        c.drawText(it.text, cx, cy + size / 3, p);
                    }
                    index++;
                }
            }
        }
    }

    // =====================================================================
    // Space bar gestures (2.0)
    //   tap                      -> space
    //   hold still               -> language strip above the space bar; slide up onto a
    //                               language and lift to switch
    //   hold/slide left or right -> move the cursor, one character per step
    // (The 2017 "slide on space switches language" is gone.)
    // =====================================================================

    static final int KEYCODE_LANG_EN = -171, KEYCODE_LANG_FA = -172, KEYCODE_LANG_KR = -173;
    /** long press on the bottom-left (emoji) key: hide / show the suggestion strip and tools row */
    static final int KEYCODE_TOGGLE_TOP_ROW = -174;

    private boolean mSpaceTracking, mSpaceCursor, mSpaceLangOpen;
    private boolean mSpaceCursorOn = true, mSpaceLangOn = true;
    private int mLangPopupBottom;
    private float mSpaceStepDp = 14.8f;
    private float mSpaceDownX, mSpaceDownY, mSpaceLastStepX;
    private long mSpaceDownTime;
    private Key mSpaceKey;
    private final android.os.Handler mSpaceHandler = new android.os.Handler(android.os.Looper.getMainLooper());
    private final Runnable mSpaceLongPress = new Runnable() {
        @Override public void run() {
            if (mSpaceTracking && !mSpaceCursor) openLanguageStrip();
        }
    };

    private Key keyAt(float x, float y) {
        Keyboard kb = getKeyboard();
        if (kb == null) return null;
        for (Key k : kb.getKeys()) {
            if (k.height > 0 && k.isInside((int) x, (int) y)) return k;
        }
        return null;
    }

    private void cancelSuperTouch(long downTime, float x, float y) {
        android.view.MotionEvent cancel = android.view.MotionEvent.obtain(downTime,
                android.os.SystemClock.uptimeMillis(), android.view.MotionEvent.ACTION_CANCEL, x, y, 0);
        super.onTouchEvent(cancel);
        cancel.recycle();
    }

    private void openLanguageStrip() {
        if (mSpaceKey == null) return;
        SharedPreferences prefs = getContext().getSharedPreferences("MySharedPreference", Context.MODE_PRIVATE);
        List<SlideItem> items = new java.util.ArrayList<>();
        items.add(new SlideItem(KEYCODE_LANG_EN, "English", null));
        if (prefs.getBoolean(getContext().getString(R.string.preflanguage_farsi), true)) {
            items.add(new SlideItem(KEYCODE_LANG_FA, "فارسی", null));
        }
        if (prefs.getBoolean(getContext().getString(R.string.preflanguage_kurdi), false)) {
            items.add(new SlideItem(KEYCODE_LANG_KR, "کُردی", null));
        }
        // one language per row, stacked above the space bar: the finger only has to go up
        mSlideRows.clear();
        for (SlideItem it : items) {
            List<SlideItem> row = new java.util.ArrayList<>();
            row.add(it);
            mSlideRows.add(row);
        }
        float d = getResources().getDisplayMetrics().density;
        float cellW = Math.max(mSpaceKey.width * 0.6f, 150 * d);
        float cellH = 50 * d;
        float pad = 6 * d;
        int popW = (int) (cellW + 2 * pad);
        int popH = (int) (cellH * items.size() + 2 * pad);
        for (int i = 0; i < items.size(); i++) {
            SlideItem it = items.get(i);
            it.x = pad; it.y = pad + i * cellH; it.w = cellW; it.h = cellH;
        }
        int x = (int) (mSpaceKey.x + mSpaceKey.width / 2f - popW / 2f);
        x = Math.max(0, Math.min(getWidth() - popW, x));
        int y = (int) (mSpaceKey.y - popH - 4 * d);
        mSlideOrigin[0] = x;
        mSlideOrigin[1] = y;
        mLangPopupBottom = y + popH;
        if (mSlideView == null) {
            mSlideView = new SlidePopupView(getContext());
            mSlideWindow = new android.widget.PopupWindow(mSlideView);
            mSlideWindow.setTouchable(false);
            mSlideWindow.setClippingEnabled(false);
            mSlideWindow.setBackgroundDrawable(new android.graphics.drawable.ColorDrawable(0));
        }
        mSlideView.theme = currentTheme();
        // the current language starts highlighted
        String cur = kurdikeyboard.language;
        mSlideSelected = 0;
        for (int i = 0; i < items.size(); i++) {
            int c = items.get(i).code;
            if (("fa".equals(cur) && c == KEYCODE_LANG_FA) || ("kr".equals(cur) && c == KEYCODE_LANG_KR)) mSlideSelected = i;
        }
        mSlideWindow.setWidth(popW);
        mSlideWindow.setHeight(popH);
        int[] loc = new int[2];
        getLocationInWindow(loc);
        try {
            mSlideWindow.showAtLocation(this, android.view.Gravity.NO_GRAVITY, loc[0] + x, loc[1] + y);
        } catch (RuntimeException e) {
            return;
        }
        mSpaceLangOpen = true;
        cancelSuperTouch(mSpaceDownTime, mSpaceDownX, mSpaceDownY); // the space is not typed
        performHapticFeedback(android.view.HapticFeedbackConstants.LONG_PRESS);
    }

    private void resetSpace() {
        mSpaceHandler.removeCallbacks(mSpaceLongPress);
        mSpaceTracking = false;
        mSpaceCursor = false;
        if (mSpaceLangOpen) {
            mSpaceLangOpen = false;
            if (mSlideWindow != null && mSlideWindow.isShowing()) {
                try { mSlideWindow.dismiss(); } catch (RuntimeException ignored) {}
            }
        }
    }

    /** @return true if the event was consumed by a space gesture */
    private boolean handleSpaceGesture(android.view.MotionEvent me) {
        float d = getResources().getDisplayMetrics().density;
        int am = me.getActionMasked();
        if (am == android.view.MotionEvent.ACTION_POINTER_DOWN
                || am == android.view.MotionEvent.ACTION_POINTER_UP) {
            // fast two-thumb typing: a second finger ends any space gesture, so the other
            // finger's key is typed normally (it used to start moving the cursor)
            boolean was = mSpaceCursor || mSpaceLangOpen;
            resetSpace();
            return was;
        }
        switch (am) {
            case android.view.MotionEvent.ACTION_DOWN: {
                resetSpace();
                Key k = keyAt(me.getX(), me.getY());
                SharedPreferences sp = getContext().getSharedPreferences("MySharedPreference", Context.MODE_PRIVATE);
                mSpaceCursorOn = sp.getBoolean(getContext().getString(R.string.prefspacecursor), true);
                mSpaceLangOn = sp.getBoolean(getContext().getString(R.string.prefspacelang), true);
                // sensitivity 1..10 -> 22..6 dp of finger travel per character
                int speed = Math.max(1, Math.min(10, sp.getInt(getContext().getString(R.string.prefspacecursorspeed), 5)));
                mSpaceStepDp = 23.8f - 1.8f * speed;
                int hold = Math.max(250, Math.min(1000, sp.getInt(getContext().getString(R.string.prefspaceholdms), 450)));
                if (k != null && k.codes != null && k.codes.length > 0 && k.codes[0] == 32
                        && keyboardHasLetters() && (mSpaceCursorOn || mSpaceLangOn)) {
                    mSpaceTracking = true;
                    mSpaceKey = k;
                    mSpaceDownX = me.getX();
                    mSpaceDownY = me.getY();
                    mSpaceDownTime = me.getDownTime();
                    if (mSpaceLangOn) mSpaceHandler.postDelayed(mSpaceLongPress, hold);
                }
                return false; // KeyboardView still shows the key as pressed / types it on a tap
            }
            case android.view.MotionEvent.ACTION_MOVE: {
                if (!mSpaceTracking) return false;
                float x = me.getX(), y = me.getY();
                if (mSpaceLangOpen) {
                    // the list stays open until the finger is lifted; only the height counts,
                    // so moving sideways while sliding up does not lose it
                    if (y < mLangPopupBottom) {
                        int sel = slideItemAt(mSlideOrigin[0] + 10 * d, y);
                        if (sel != mSlideSelected) {
                            mSlideSelected = sel;
                            if (mSlideView != null) mSlideView.invalidate();
                            performHapticFeedback(android.view.HapticFeedbackConstants.CLOCK_TICK);
                        }
                    }
                    return true;
                }
                if (!mSpaceCursor) {
                    if (mSpaceCursorOn && Math.abs(x - mSpaceDownX) > 12 * d) {
                        mSpaceHandler.removeCallbacks(mSpaceLongPress);
                        cancelSuperTouch(mSpaceDownTime, x, y);
                        mSpaceCursor = true;
                        mSpaceLastStepX = x;
                    } else {
                        return false;
                    }
                }
                // one character per step; in Persian / Kurdish the text runs right to left
                float step = mSpaceStepDp * d;
                boolean rtl = "fa".equals(kurdikeyboard.language) || "kr".equals(kurdikeyboard.language);
                while (Math.abs(x - mSpaceLastStepX) >= step) {
                    boolean right = x > mSpaceLastStepX;
                    mSpaceLastStepX += right ? step : -step;
                    boolean forward = right != rtl;
                    if (getOnKeyboardActionListener() != null) {
                        getOnKeyboardActionListener().onKey(forward ? KEYCODE_CURSOR_F : KEYCODE_CURSOR_B, null);
                    }
                }
                return true;
            }
            case android.view.MotionEvent.ACTION_UP: {
                if (!mSpaceTracking) return false;
                boolean consumed = mSpaceCursor || mSpaceLangOpen;
                SlideItem pick = mSpaceLangOpen && mSlideSelected >= 0 ? slideItem(mSlideSelected) : null;
                resetSpace();
                if (pick != null && getOnKeyboardActionListener() != null) {
                    getOnKeyboardActionListener().onKey(pick.code, null);
                }
                return consumed;
            }
            case android.view.MotionEvent.ACTION_CANCEL:
                resetSpace();
                return false;
            default:
                return mSpaceCursor || mSpaceLangOpen;
        }
    }

    // =====================================================================
    // Key preview (2.0): the pressed letter shown large above the finger, in the theme's
    // colors. Replaces KeyboardView's old preview. Setting: "show key while typing".
    // =====================================================================

    private android.widget.PopupWindow mPreviewWindow;
    private KeyPreviewView mPreviewView;
    private Key mPreviewKey;
    private final Runnable mHidePreview = new Runnable() {
        @Override public void run() { hideKeyPreview(); }
    };

    private boolean keyPreviewEnabled() {
        return getContext().getSharedPreferences("MySharedPreference", Context.MODE_PRIVATE)
                .getBoolean(getContext().getString(R.string.prefnotshowpreview), true);
    }

    /** text shown for a key, or null for keys without a preview (function keys) */
    private String previewText(Key key) {
        if (key == null || key.label == null || key.codes == null || key.codes.length == 0) return null;
        int code = key.codes[0];
        if (code <= 0 || code == 32 || code == 10) return null;
        String s = key.label.toString();
        if (s.isEmpty() || s.length() > 3) return null;
        return isShifted() ? s.toUpperCase() : s;
    }

    /** key whose second letter is shown in the bubble (held two-letter key), or null */
    private Key mAltKey;

    /**
     * Held two-letter key (ا → آ, ص → ض ...): shows the second letter in the key preview
     * bubble, in the theme's colors and at the preview distance from the finger. Replaces
     * the 2017 white popup. Shown even when the normal key preview is turned off.
     */
    void showAltPreview(int keyX, int keyY, int keyW, int keyH, String text) {
        if (text == null || text.isEmpty()) return;
        Key key = keyAt(keyX + keyW / 2f, keyY + keyH / 2f);
        float d = getResources().getDisplayMetrics().density;
        int w = (int) Math.max(keyW * 1.25f, 44 * d);
        int h = (int) Math.max(keyH * 1.35f, 58 * d);
        if (mPreviewView == null) {
            mPreviewView = new KeyPreviewView(getContext());
            mPreviewWindow = new android.widget.PopupWindow(mPreviewView);
            mPreviewWindow.setTouchable(false);
            mPreviewWindow.setClippingEnabled(false);
            mPreviewWindow.setBackgroundDrawable(new android.graphics.drawable.ColorDrawable(0));
            mPreviewWindow.setAnimationStyle(0);
        }
        mSpaceHandler.removeCallbacks(mHidePreview);
        mPreviewView.theme = currentTheme();
        mPreviewView.text = text;
        mPreviewView.alt = true;
        mPreviewView.invalidate();
        mAltKey = key;
        mPreviewKey = key;
        int x = (int) (keyX + keyW / 2f - w / 2f);
        x = Math.max(0, Math.min(getWidth() - w, x));
        int gapDp = getContext().getSharedPreferences("MySharedPreference", Context.MODE_PRIVATE)
                .getInt(getContext().getString(R.string.prefpreviewgap), 24);
        int y = (int) (keyY - h - gapDp * d);
        int[] loc = new int[2];
        getLocationInWindow(loc);
        y = Math.max(y, -loc[1]);
        try {
            if (mPreviewWindow.isShowing()) {
                mPreviewWindow.update(loc[0] + x, loc[1] + y, w, h);
            } else {
                mPreviewWindow.setWidth(w);
                mPreviewWindow.setHeight(h);
                mPreviewWindow.showAtLocation(this, android.view.Gravity.NO_GRAVITY, loc[0] + x, loc[1] + y);
            }
        } catch (RuntimeException ignored) {
        }
    }

    private void showKeyPreview(Key key) {
        mSpaceHandler.removeCallbacks(mHidePreview);
        // the held key's second letter stays until the finger leaves the key or lifts
        if (mAltKey != null && key == mAltKey && mPreviewWindow != null && mPreviewWindow.isShowing()) return;
        mAltKey = null;
        if (mPreviewView != null) mPreviewView.alt = false;
        String text = previewText(key);
        if (text == null || !keyPreviewEnabled()) {
            hideKeyPreview();
            return;
        }
        if (key == mPreviewKey && mPreviewWindow != null && mPreviewWindow.isShowing()) return;
        mPreviewKey = key;
        float d = getResources().getDisplayMetrics().density;
        int w = (int) Math.max(key.width * 1.25f, 44 * d);
        int h = (int) Math.max(key.height * 1.35f, 58 * d);
        if (mPreviewView == null) {
            mPreviewView = new KeyPreviewView(getContext());
            mPreviewWindow = new android.widget.PopupWindow(mPreviewView);
            mPreviewWindow.setTouchable(false);
            mPreviewWindow.setClippingEnabled(false);
            mPreviewWindow.setBackgroundDrawable(new android.graphics.drawable.ColorDrawable(0));
            mPreviewWindow.setAnimationStyle(0);
        }
        mPreviewView.theme = currentTheme();
        mPreviewView.text = text;
        mPreviewView.invalidate();
        int x = (int) (key.x + key.width / 2f - w / 2f);
        x = Math.max(0, Math.min(getWidth() - w, x));
        // gap between the bubble and the key, so the finger does not cover it (setting, dp)
        int gapDp = getContext().getSharedPreferences("MySharedPreference", Context.MODE_PRIVATE)
                .getInt(getContext().getString(R.string.prefpreviewgap), 24);
        int y = (int) (key.y - h - gapDp * d);
        int[] loc = new int[2];
        getLocationInWindow(loc);
        // stay inside the keyboard window (nothing is drawn above it): over the tools row if needed
        y = Math.max(y, -loc[1]);
        try {
            if (mPreviewWindow.isShowing()) {
                mPreviewWindow.update(loc[0] + x, loc[1] + y, w, h);
            } else {
                mPreviewWindow.setWidth(w);
                mPreviewWindow.setHeight(h);
                mPreviewWindow.showAtLocation(this, android.view.Gravity.NO_GRAVITY, loc[0] + x, loc[1] + y);
            }
        } catch (RuntimeException ignored) {
        }
    }

    /** hides the preview a moment later, so a quick tap is still visible */
    private void hideKeyPreviewSoon() {
        mSpaceHandler.removeCallbacks(mHidePreview);
        mSpaceHandler.postDelayed(mHidePreview, 70);
    }

    private void hideKeyPreview() {
        mSpaceHandler.removeCallbacks(mHidePreview);
        mPreviewKey = null;
        mAltKey = null;
        if (mPreviewWindow != null && mPreviewWindow.isShowing()) {
            try { mPreviewWindow.dismiss(); } catch (RuntimeException ignored) {}
        }
    }

    /** keeps the preview in step with the finger (called for every touch event) */
    private void trackKeyPreview(android.view.MotionEvent me) {
        switch (me.getActionMasked()) {
            case android.view.MotionEvent.ACTION_DOWN:
            case android.view.MotionEvent.ACTION_MOVE:
                if (mSlideActive || mSwiping || mSpaceCursor || mSpaceLangOpen) {
                    hideKeyPreview();
                } else {
                    showKeyPreview(keyAt(me.getX(), me.getY()));
                }
                break;
            case android.view.MotionEvent.ACTION_POINTER_DOWN:
                showKeyPreview(keyAt(me.getX(me.getActionIndex()), me.getY(me.getActionIndex())));
                break;
            case android.view.MotionEvent.ACTION_UP:
            case android.view.MotionEvent.ACTION_POINTER_UP:
                hideKeyPreviewSoon();
                break;
            case android.view.MotionEvent.ACTION_CANCEL:
                hideKeyPreview();
                break;
        }
    }

    private static final class KeyPreviewView extends View {
        KeyboardTheme theme;
        String text;
        boolean alt; // second letter of a held key: drawn in the accent color
        private final Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        private final android.graphics.RectF r = new android.graphics.RectF();

        KeyPreviewView(Context c) { super(c); }

        @Override
        protected void onDraw(Canvas c) {
            if (theme == null || text == null) return;
            float d = getResources().getDisplayMetrics().density;
            r.set(0, 0, getWidth(), getHeight() - 2 * d);
            p.setStyle(Paint.Style.FILL);
            p.setColor(0x40000000);
            r.offset(0, 2 * d);
            c.drawRoundRect(r, 12 * d, 12 * d, p);
            r.offset(0, -2 * d);
            int bg = theme.isDark() ? theme.keyPressed : 0xFFFFFFFF;
            p.setColor(bg);
            c.drawRoundRect(r, 12 * d, 12 * d, p);
            if (theme.stroke != 0) {
                p.setStyle(Paint.Style.STROKE);
                p.setStrokeWidth(d);
                p.setColor(theme.stroke);
                c.drawRoundRect(r, 12 * d, 12 * d, p);
                p.setStyle(Paint.Style.FILL);
            }
            p.setColor(alt ? theme.accent : theme.text);
            p.setTextAlign(Paint.Align.CENTER);
            float size = getHeight() * 0.48f;
            p.setTextSize(size);
            while (p.measureText(text) > getWidth() - 8 * d && size > 10 * d) {
                size -= d;
                p.setTextSize(size);
            }
            c.drawText(text, getWidth() / 2f, r.height() * 0.5f + size / 3f, p);
        }
    }

    /** keyboard hidden or reset: nothing of ours may stay on screen or half-done */
    @Override
    public void closing() {
        super.closing();
        cleanUpGestures();
    }

    @Override
    public void onDetachedFromWindow() {
        cleanUpGestures();
        super.onDetachedFromWindow();
    }

    void cleanUpGestures() {
        closeSlidePopup();
        resetSpace();
        hideKeyPreview();
        endSwipe();
    }
}
