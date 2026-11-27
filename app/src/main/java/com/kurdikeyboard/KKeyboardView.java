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

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (getKeyboard() == null) return;

        Paint paint = new Paint();
        Paint paintpopupsymbole=new Paint();
        paint.setTextAlign(Paint.Align.CENTER);
        paintpopupsymbole.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(30);
        paintpopupsymbole.setTextSize(30);
        paint.setColor(Color.BLACK);
        paintpopupsymbole.setColor(Color.BLACK);

        //////new code btn shape

        SharedPreferences preferenceKeyboard=
                this.getContext()
        .getSharedPreferences("MySharedPreference", Context.MODE_PRIVATE);
        String listS=
                preferenceKeyboard.getString(this.getContext().getString(R.string.pref_list_theme_selector), "1");

        int fontsize=preferenceKeyboard.getInt(
                this.getContext().getString(R.string.prefseekbarfont),100);
        fontsize /= 5;
        int convertedtospfontsize;
        final float scale = getResources().getDisplayMetrics().density;
        float size_dpi=fontsize*one_DIP;
        convertedtospfontsize=(int) (size_dpi * scale + 0.5f);
        Paint paintforsize=new Paint();

        paintforsize.setTextSize(convertedtospfontsize);
        paintforsize.setFakeBoldText(true);
        paintforsize.setTextAlign(Paint.Align.CENTER);
        Paint paintforsize2fortwolabelkey=new Paint();
        paintforsize2fortwolabelkey.setTextSize(3 * convertedtospfontsize / 5);
        paintforsize2fortwolabelkey.setFakeBoldText(true);
        paintforsize2fortwolabelkey.setTextAlign(Paint.Align.CENTER);
        paintforsize2fortwolabelkey.setColor(Color.GRAY);

        Paint paintforsizenumbersfixed=new Paint();
        int fontsizenumbersfixed=90;
        fontsizenumbersfixed /= 5;
        float size_dpinumbersfixed=fontsizenumbersfixed*one_DIP;
        int convertedtospfontsizenumbersfixed=
                (int) (size_dpinumbersfixed * scale + 0.5f);
        paintforsizenumbersfixed.setTextSize(convertedtospfontsizenumbersfixed);
        paintforsizenumbersfixed.setColor(Color.BLACK);
        paintforsizenumbersfixed.setTextAlign(Paint.Align.CENTER);
        paintforsizenumbersfixed.setFakeBoldText(true);




        //get all your keys and draw whatever you want
       /* List<Key> keys = getKeyboard().getKeys();

        for(Key key: keys) {

            if(key.label != null) {

               handlebackgroundnumber(canvas,key,listS, paintforsize,paintforsize2fortwolabelkey);




            }else {
                handleicon(key,listS);
            }
        }*/
        ///inja
        boolean isactiveimagebg=
                preferenceKeyboard.getBoolean(mContext.getString(R.string.prefimagefrbackground)
                        , false);

        if(isactiveimagebg){
            opacity=preferenceKeyboard.getInt(mContext.getString(R.string.prefseekbaropacitykey),100);
            List<Key> keys = getKeyboard().getKeys();
            for(Key key: keys) {
                if(key.icon != null) {
                    drawenterkey(canvas, key);

                }
                if(key.label != null) {
/////////////////////////////////
                    String keyLabel = key.label.toString();
                    if (getKeyboard().isShifted()) {
                        keyLabel = keyLabel.toUpperCase();
                    }
                    if(returnvalue(key)){

                        ////////#######################
                        paintforsize.setColor(
                                getResources().getColor(R.color.a_0_mf_theme_0_color_letters));
                        paintforsize2fortwolabelkey.setColor(
                                getResources().getColor(R.color.a_0_mf_theme_0_color_letters_littleword));

                        Drawable npd = cachedDrawable(
                                R.drawable.a_0_mf_theme_0_background_key_letters_selector);
                        int[] drawableState = key.getCurrentDrawableState();
                        if (key.codes[0] != 0) {
                            npd.setState(drawableState);
                        }
                        npd.setBounds(key.x, key.y, key.x + key.width, key.y
                                + key.height);
                        npd.draw(canvas);


                        //////////////////////////###############

                        printcharactor(key,canvas,paintforsize,paintforsize2fortwolabelkey,keyLabel);


                    }else //if(isWordnumbers(key.codes[0]))
                    {

                        Drawable npd = cachedDrawable(
                                R.drawable.a_0_mf_theme_0_background_key_selector);
                        int[] drawableState = key.getCurrentDrawableState();
                        if (key.codes[0] != 0) {
                            npd.setState(drawableState);
                        }
                        npd.setBounds(key.x, key.y, key.x + key.width, key.y
                                + key.height);
                        npd.draw(canvas);
                        Paint paintforsizenumbersfixed2=new Paint();
                        paintforsizenumbersfixed2=paintforsizenumbersfixed;
                        paintforsizenumbersfixed2.setColor(getResources().getColor(R.color.a_31_mf_theme_31_color_letters));
                        drawaarab(canvas, key, keyLabel,paintforsizenumbersfixed2);

                        canvas.drawText(key.label.toString(), key.x + (key.width / 2),
                                key.y + 7 * (key.height / 10), paintforsizenumbersfixed);

                    }


                    //////////////////




                }else {
                    Drawable npd = cachedDrawable(
                            R.drawable.a_0_mf_theme_0_background_key_selector);
                    int[] drawableState = key.getCurrentDrawableState();
                    if (key.codes[0] != 0) {
                        npd.setState(drawableState);
                    }
                    npd.setBounds(key.x, key.y, key.x + key.width, key.y
                            + key.height);
                    npd.draw(canvas);


                }
            }

        }
        else if(listS.equals("1")){ // "else": with an image background the theme keys hid the image
            List<Key> keys = getKeyboard().getKeys();
            for(Key key: keys) {
                if(key.icon != null) {
                    drawenterkey(canvas, key);
                }
                if(key.label != null) {
/////////////////////////////////
                    String keyLabel = key.label.toString();
                    if (getKeyboard().isShifted()) {
                        keyLabel = keyLabel.toUpperCase();
                    }
                    if(returnvalue(key)){

                        ////////#######################
                        paintforsize.setColor(
                                getResources().getColor(R.color.a_1_mf_theme_1_color_letters));
                        paintforsize2fortwolabelkey.setColor(
                                getResources().getColor(R.color.a_1_mf_theme_1_color_letters_littleword));

                        Drawable npd = cachedDrawable(
                                R.drawable.a_1_mf_theme_1_background_key_letters_selector);
                        int[] drawableState = key.getCurrentDrawableState();
                        if (key.codes[0] != 0) {
                            npd.setState(drawableState);
                        }
                        npd.setAlpha(opacity);
                        npd.setBounds(key.x, key.y, key.x + key.width, key.y
                                + key.height);
                        npd.draw(canvas);


                        //////////////////////////###############

                        printcharactor(key,canvas,paintforsize,paintforsize2fortwolabelkey,keyLabel);


                    }else //if(isWordnumbers(key.codes[0]))
                    {

                        Drawable npd = cachedDrawable(
                                R.drawable.a_1_mf_theme_1_background_key_selector);
                        int[] drawableState = key.getCurrentDrawableState();
                        if (key.codes[0] != 0) {
                            npd.setState(drawableState);
                        }
                        npd.setAlpha(opacity);
                        npd.setBounds(key.x, key.y, key.x + key.width, key.y
                                + key.height);
                        npd.draw(canvas);


                        drawaarab(canvas,key,keyLabel,paintforsizenumbersfixed);

                        canvas.drawText(key.label.toString(), key.x + (key.width / 2),
                                key.y + 7 * (key.height / 10), paintforsizenumbersfixed);

                    }


                    //////////////////




                }/*else {
                    Drawable npd = cachedDrawable(
                            R.drawable.a_1_mf_theme_1_background_key_selector);
                    int[] drawableState = key.getCurrentDrawableState();
                    if (key.codes[0] != 0) {
                        npd.setState(drawableState);
                    }
                    npd.setAlpha(opacity);
                    npd.setBounds(key.x, key.y, key.x + key.width, key.y
                            + key.height);
                    npd.draw(canvas);


                }*/
            }

        }else if(listS.equals("1x")){
            List<Key> keys = getKeyboard().getKeys();
            for(Key key: keys) {
                if(key.icon != null) {
                    drawenterkey(canvas, key);
                }
                if(key.label != null) {
/////////////////////////////////
                    String keyLabel = key.label.toString();
                    if (getKeyboard().isShifted()) {
                        keyLabel = keyLabel.toUpperCase();
                    }
                    if(returnvalue(key)){

                        ////////#######################
                        paintforsize.setColor(
                                getResources().getColor(R.color.a_1_mf_theme_1_color_letters));
                        paintforsize2fortwolabelkey.setColor(
                                getResources().getColor(R.color.a_1_mf_theme_1_color_letters_littleword));

                        Drawable npd = cachedDrawable(
                                R.drawable.a_1x_mf_theme_1x_background_key_letters_selector);
                        int[] drawableState = key.getCurrentDrawableState();
                        if (key.codes[0] != 0) {
                            npd.setState(drawableState);
                        }
                        npd.setAlpha(opacity);
                        npd.setBounds(key.x, key.y, key.x + key.width, key.y
                                + key.height);
                        npd.draw(canvas);


                        //////////////////////////###############

                        printcharactor(key,canvas,paintforsize,paintforsize2fortwolabelkey,keyLabel);


                    }else //if(isWordnumbers(key.codes[0]))
                    {

                        Drawable npd = cachedDrawable(
                                R.drawable.a_1x_mf_theme_1x_background_key_selector);
                        int[] drawableState = key.getCurrentDrawableState();
                        if (key.codes[0] != 0) {
                            npd.setState(drawableState);
                        }
                        npd.setAlpha(opacity);
                        npd.setBounds(key.x, key.y, key.x + key.width, key.y
                                + key.height);
                        npd.draw(canvas);


                        drawaarab(canvas,key,keyLabel,paintforsizenumbersfixed);

                        canvas.drawText(key.label.toString(), key.x + (key.width / 2),
                                key.y + 7 * (key.height / 10), paintforsizenumbersfixed);

                    }


                    //////////////////




                }/*else {
                    Drawable npd = cachedDrawable(
                            R.drawable.a_1x_mf_theme_1x_background_key_selector);
                    int[] drawableState = key.getCurrentDrawableState();
                    if (key.codes[0] != 0) {
                        npd.setState(drawableState);
                    }
                    npd.setAlpha(opacity);
                    npd.setBounds(key.x, key.y, key.x + key.width, key.y
                            + key.height);
                    npd.draw(canvas);


                }*/
            }

        }else if(listS.equals("1xx")){
            List<Key> keys = getKeyboard().getKeys();
            for(Key key: keys) {
                if(key.icon != null) {
                    drawenterkey(canvas, key);
                }
                if(key.label != null) {
/////////////////////////////////
                    String keyLabel = key.label.toString();
                    if (getKeyboard().isShifted()) {
                        keyLabel = keyLabel.toUpperCase();
                    }
                    if(returnvalue(key)){

                        ////////#######################
                        paintforsize.setColor(
                                getResources().getColor(R.color.a_1_mf_theme_1_color_letters));
                        paintforsize2fortwolabelkey.setColor(
                                getResources().getColor(R.color.a_1_mf_theme_1_color_letters_littleword));

                        Drawable npd = cachedDrawable(
                                R.drawable.a_1xx_mf_theme_1xx_background_key_letters_selector);
                        int[] drawableState = key.getCurrentDrawableState();
                        if (key.codes[0] != 0) {
                            npd.setState(drawableState);
                        }
                        npd.setAlpha(opacity);
                        npd.setBounds(key.x, key.y, key.x + key.width, key.y
                                + key.height);
                        npd.draw(canvas);


                        //////////////////////////###############

                        printcharactor(key,canvas,paintforsize,paintforsize2fortwolabelkey,keyLabel);


                    }else //if(isWordnumbers(key.codes[0]))
                    {

                        Drawable npd = cachedDrawable(
                                R.drawable.a_1xx_mf_theme_1xx_background_key_selector);
                        int[] drawableState = key.getCurrentDrawableState();
                        if (key.codes[0] != 0) {
                            npd.setState(drawableState);
                        }
                        npd.setAlpha(opacity);
                        npd.setBounds(key.x, key.y, key.x + key.width, key.y
                                + key.height);
                        npd.draw(canvas);


                        drawaarab(canvas,key,keyLabel,paintforsizenumbersfixed);

                        canvas.drawText(key.label.toString(), key.x + (key.width / 2),
                                key.y + 7 * (key.height / 10), paintforsizenumbersfixed);

                    }


                    //////////////////




                }/*else {
                    Drawable npd = cachedDrawable(
                            R.drawable.a_1xx_mf_theme_1xx_background_key_selector);
                    int[] drawableState = key.getCurrentDrawableState();
                    if (key.codes[0] != 0) {
                        npd.setState(drawableState);
                    }
                    npd.setAlpha(opacity);
                    npd.setBounds(key.x, key.y, key.x + key.width, key.y
                            + key.height);
                    npd.draw(canvas);


                }*/
            }

        }else if(listS.equals("2")){
            List<Key> keys = getKeyboard().getKeys();
            for(Key key: keys) {
                if(key.icon != null) {
                    drawenterkey(canvas, key);
                }
                if(key.label != null) {
/////////////////////////////////
                    String keyLabel = key.label.toString();
                    if (getKeyboard().isShifted()) {
                        keyLabel = keyLabel.toUpperCase();
                    }
                    if(returnvalue(key)){

                        ////////#######################
                        paintforsize.setColor(
                                getResources().getColor(R.color.a_2_mf_theme_2_color_letters));
                        paintforsize2fortwolabelkey.setColor(
                                getResources().getColor(R.color.a_2_mf_theme_2_color_letters_littleword));

                        Drawable npd = cachedDrawable(
                                R.drawable.a_2_mf_theme_2_background_key_letters_selector);
                        int[] drawableState = key.getCurrentDrawableState();
                        if (key.codes[0] != 0) {
                            npd.setState(drawableState);
                        }
                        npd.setAlpha(opacity);
                        npd.setBounds(key.x, key.y, key.x + key.width, key.y
                                + key.height);
                        npd.draw(canvas);


                        //////////////////////////###############

                        printcharactor(key,canvas,paintforsize,paintforsize2fortwolabelkey,keyLabel);


                    }else //if(isWordnumbers(key.codes[0]))
                    {

                        Drawable npd = cachedDrawable(
                                R.drawable.a_2_mf_theme_2_background_key_selector);
                        int[] drawableState = key.getCurrentDrawableState();
                        if (key.codes[0] != 0) {
                            npd.setState(drawableState);
                        }
                        npd.setAlpha(opacity);
                        npd.setBounds(key.x, key.y, key.x + key.width, key.y
                                + key.height);
                        npd.draw(canvas);


                        drawaarab(canvas,key,keyLabel,paintforsizenumbersfixed);

                        canvas.drawText(key.label.toString(), key.x + (key.width / 2),
                                key.y + 7 * (key.height / 10), paintforsizenumbersfixed);

                    }


                    //////////////////




                }/*else {
                    Drawable npd = cachedDrawable(
                            R.drawable.a_2_mf_theme_2_background_key_selector);
                    int[] drawableState = key.getCurrentDrawableState();
                    if (key.codes[0] != 0) {
                        npd.setState(drawableState);
                    }
                    npd.setAlpha(opacity);
                    npd.setBounds(key.x, key.y, key.x + key.width, key.y
                            + key.height);
                    npd.draw(canvas);


                }*/
            }

        }else if(listS.equals("2x")){
            List<Key> keys = getKeyboard().getKeys();
            for(Key key: keys) {
                if(key.icon != null) {
                    drawenterkey(canvas, key);
                }
                if(key.label != null) {
/////////////////////////////////
                    String keyLabel = key.label.toString();
                    if (getKeyboard().isShifted()) {
                        keyLabel = keyLabel.toUpperCase();
                    }
                    if(returnvalue(key)){

                        ////////#######################
                        paintforsize.setColor(
                                getResources().getColor(R.color.a_2_mf_theme_2_color_letters));
                        paintforsize2fortwolabelkey.setColor(
                                getResources().getColor(R.color.a_2_mf_theme_2_color_letters_littleword));

                        Drawable npd = cachedDrawable(
                                R.drawable.a_2x_mf_theme_2x_background_key_letters_selector);
                        int[] drawableState = key.getCurrentDrawableState();
                        if (key.codes[0] != 0) {
                            npd.setState(drawableState);
                        }
                        npd.setAlpha(opacity);
                        npd.setBounds(key.x, key.y, key.x + key.width, key.y
                                + key.height);
                        npd.draw(canvas);


                        //////////////////////////###############

                        printcharactor(key,canvas,paintforsize,paintforsize2fortwolabelkey,keyLabel);


                    }else //if(isWordnumbers(key.codes[0]))
                    {

                        Drawable npd = cachedDrawable(
                                R.drawable.a_2x_mf_theme_2x_background_key_selector);
                        int[] drawableState = key.getCurrentDrawableState();
                        if (key.codes[0] != 0) {
                            npd.setState(drawableState);
                        }
                        npd.setAlpha(opacity);
                        npd.setBounds(key.x, key.y, key.x + key.width, key.y
                                + key.height);
                        npd.draw(canvas);


                        drawaarab(canvas,key,keyLabel,paintforsizenumbersfixed);

                        canvas.drawText(key.label.toString(), key.x + (key.width / 2),
                                key.y + 7 * (key.height / 10), paintforsizenumbersfixed);

                    }


                    //////////////////




                }/*else {
                    Drawable npd = cachedDrawable(
                            R.drawable.a_2x_mf_theme_2x_background_key_selector);
                    int[] drawableState = key.getCurrentDrawableState();
                    if (key.codes[0] != 0) {
                        npd.setState(drawableState);
                    }
                    npd.setAlpha(opacity);
                    npd.setBounds(key.x, key.y, key.x + key.width, key.y
                            + key.height);
                    npd.draw(canvas);


                }*/
            }

        }else if(listS.equals("2xx")){
            List<Key> keys = getKeyboard().getKeys();
            for(Key key: keys) {
                if(key.icon != null) {
                    drawenterkey(canvas, key);
                }
                if(key.label != null) {
/////////////////////////////////
                    String keyLabel = key.label.toString();
                    if (getKeyboard().isShifted()) {
                        keyLabel = keyLabel.toUpperCase();
                    }
                    if(returnvalue(key)){

                        ////////#######################
                        paintforsize.setColor(
                                getResources().getColor(R.color.a_2_mf_theme_2_color_letters));
                        paintforsize2fortwolabelkey.setColor(
                                getResources().getColor(R.color.a_2_mf_theme_2_color_letters_littleword));

                        Drawable npd = cachedDrawable(
                                R.drawable.a_2xx_mf_theme_2xx_background_key_letters_selector);
                        int[] drawableState = key.getCurrentDrawableState();
                        if (key.codes[0] != 0) {
                            npd.setState(drawableState);
                        }
                        npd.setAlpha(opacity);
                        npd.setBounds(key.x, key.y, key.x + key.width, key.y
                                + key.height);
                        npd.draw(canvas);


                        //////////////////////////###############

                        printcharactor(key,canvas,paintforsize,paintforsize2fortwolabelkey,keyLabel);


                    }else //if(isWordnumbers(key.codes[0]))
                    {

                        Drawable npd = cachedDrawable(
                                R.drawable.a_2xx_mf_theme_2xx_background_key_selector);
                        int[] drawableState = key.getCurrentDrawableState();
                        if (key.codes[0] != 0) {
                            npd.setState(drawableState);
                        }
                        npd.setAlpha(opacity);
                        npd.setBounds(key.x, key.y, key.x + key.width, key.y
                                + key.height);
                        npd.draw(canvas);


                        drawaarab(canvas,key,keyLabel,paintforsizenumbersfixed);

                        canvas.drawText(key.label.toString(), key.x + (key.width / 2),
                                key.y + 7 * (key.height / 10), paintforsizenumbersfixed);

                    }


                    //////////////////




                }/*else {
                    Drawable npd = cachedDrawable(
                            R.drawable.a_2xx_mf_theme_2xx_background_key_selector);
                    int[] drawableState = key.getCurrentDrawableState();
                    if (key.codes[0] != 0) {
                        npd.setState(drawableState);
                    }
                    npd.setAlpha(opacity);
                    npd.setBounds(key.x, key.y, key.x + key.width, key.y
                            + key.height);
                    npd.draw(canvas);


                }*/
            }

        }else if(listS.equals("3")){
            List<Key> keys = getKeyboard().getKeys();
            for(Key key: keys) {
                if(key.icon != null) {
                    drawenterkey(canvas, key);
                }
                if(key.label != null) {
/////////////////////////////////
                    String keyLabel = key.label.toString();
                    if (getKeyboard().isShifted()) {
                        keyLabel = keyLabel.toUpperCase();
                    }
                    if(returnvalue(key)){

                        ////////#######################
                        paintforsize.setColor(
                                getResources().getColor(R.color.a_3_mf_theme_3_color_letters));
                        paintforsize2fortwolabelkey.setColor(
                                getResources().getColor(R.color.a_3_mf_theme_3_color_letters_littleword));

                        Drawable npd = cachedDrawable(
                                R.drawable.a_3_mf_theme_3_background_key_letters_selector);
                        int[] drawableState = key.getCurrentDrawableState();
                        if (key.codes[0] != 0) {
                            npd.setState(drawableState);
                        }
                        npd.setAlpha(opacity);
                        npd.setBounds(key.x, key.y, key.x + key.width, key.y
                                + key.height);
                        npd.draw(canvas);


                        //////////////////////////###############

                        printcharactor(key,canvas,paintforsize,paintforsize2fortwolabelkey,keyLabel);


                    }else //if(isWordnumbers(key.codes[0]))
                    {

                        Drawable npd = cachedDrawable(
                                R.drawable.a_3_mf_theme_3_background_key_selector);
                        int[] drawableState = key.getCurrentDrawableState();
                        if (key.codes[0] != 0) {
                            npd.setState(drawableState);
                        }
                        npd.setAlpha(opacity);
                        npd.setBounds(key.x, key.y, key.x + key.width, key.y
                                + key.height);
                        npd.draw(canvas);


                        drawaarab(canvas,key,keyLabel,paintforsizenumbersfixed);

                        canvas.drawText(key.label.toString(), key.x + (key.width / 2),
                                key.y + 7 * (key.height / 10), paintforsizenumbersfixed);

                    }


                    //////////////////




                }/*else {
                    Drawable npd = cachedDrawable(
                            R.drawable.a_3_mf_theme_3_background_key_selector);
                    int[] drawableState = key.getCurrentDrawableState();
                    if (key.codes[0] != 0) {
                        npd.setState(drawableState);
                    }
                    npd.setAlpha(opacity);
                    npd.setBounds(key.x, key.y, key.x + key.width, key.y
                            + key.height);
                    npd.draw(canvas);


                }*/
            }

        }else if(listS.equals("3x")){
            List<Key> keys = getKeyboard().getKeys();
            for(Key key: keys) {
                if(key.icon != null) {
                    drawenterkey(canvas, key);
                }
                if(key.label != null) {
/////////////////////////////////
                    String keyLabel = key.label.toString();
                    if (getKeyboard().isShifted()) {
                        keyLabel = keyLabel.toUpperCase();
                    }
                    if(returnvalue(key)){

                        ////////#######################
                        paintforsize.setColor(
                                getResources().getColor(R.color.a_3_mf_theme_3_color_letters));
                        paintforsize2fortwolabelkey.setColor(
                                getResources().getColor(R.color.a_3_mf_theme_3_color_letters_littleword));

                        Drawable npd = cachedDrawable(
                                R.drawable.a_3x_mf_theme_3x_background_key_letters_selector);
                        int[] drawableState = key.getCurrentDrawableState();
                        if (key.codes[0] != 0) {
                            npd.setState(drawableState);
                        }
                        npd.setAlpha(opacity);
                        npd.setBounds(key.x, key.y, key.x + key.width, key.y
                                + key.height);
                        npd.draw(canvas);


                        //////////////////////////###############

                        printcharactor(key,canvas,paintforsize,paintforsize2fortwolabelkey,keyLabel);


                    }else //if(isWordnumbers(key.codes[0]))
                    {

                        Drawable npd = cachedDrawable(
                                R.drawable.a_3x_mf_theme_3x_background_key_selector);
                        int[] drawableState = key.getCurrentDrawableState();
                        if (key.codes[0] != 0) {
                            npd.setState(drawableState);
                        }
                        npd.setAlpha(opacity);
                        npd.setBounds(key.x, key.y, key.x + key.width, key.y
                                + key.height);
                        npd.draw(canvas);


                        drawaarab(canvas,key,keyLabel,paintforsizenumbersfixed);

                        canvas.drawText(key.label.toString(), key.x + (key.width / 2),
                                key.y + 7 * (key.height / 10), paintforsizenumbersfixed);

                    }


                    //////////////////




                }/*else {
                    Drawable npd = cachedDrawable(
                            R.drawable.a_3x_mf_theme_3x_background_key_selector);
                    int[] drawableState = key.getCurrentDrawableState();
                    if (key.codes[0] != 0) {
                        npd.setState(drawableState);
                    }
                    npd.setAlpha(opacity);
                    npd.setBounds(key.x, key.y, key.x + key.width, key.y
                            + key.height);
                    npd.draw(canvas);


                }*/
            }

        }else if(listS.equals("3xx")){
            List<Key> keys = getKeyboard().getKeys();
            for(Key key: keys) {
                if(key.icon != null) {
                    drawenterkey(canvas, key);
                }
                if(key.label != null) {
/////////////////////////////////
                    String keyLabel = key.label.toString();
                    if (getKeyboard().isShifted()) {
                        keyLabel = keyLabel.toUpperCase();
                    }
                    if(returnvalue(key)){

                        ////////#######################
                        paintforsize.setColor(
                                getResources().getColor(R.color.a_3_mf_theme_3_color_letters));
                        paintforsize2fortwolabelkey.setColor(
                                getResources().getColor(R.color.a_3_mf_theme_3_color_letters_littleword));

                        Drawable npd = cachedDrawable(
                                R.drawable.a_3xx_mf_theme_3xx_background_key_letters_selector);
                        int[] drawableState = key.getCurrentDrawableState();
                        if (key.codes[0] != 0) {
                            npd.setState(drawableState);
                        }
                        npd.setAlpha(opacity);
                        npd.setBounds(key.x, key.y, key.x + key.width, key.y
                                + key.height);
                        npd.draw(canvas);


                        //////////////////////////###############

                        printcharactor(key,canvas,paintforsize,paintforsize2fortwolabelkey,keyLabel);


                    }else //if(isWordnumbers(key.codes[0]))
                    {

                        Drawable npd = cachedDrawable(
                                R.drawable.a_3xx_mf_theme_3xx_background_key_selector);
                        int[] drawableState = key.getCurrentDrawableState();
                        if (key.codes[0] != 0) {
                            npd.setState(drawableState);
                        }
                        npd.setAlpha(opacity);
                        npd.setBounds(key.x, key.y, key.x + key.width, key.y
                                + key.height);
                        npd.draw(canvas);


                        drawaarab(canvas,key,keyLabel,paintforsizenumbersfixed);

                        canvas.drawText(key.label.toString(), key.x + (key.width / 2),
                                key.y + 7 * (key.height / 10), paintforsizenumbersfixed);

                    }


                    //////////////////




                }/*else {
                    Drawable npd = cachedDrawable(
                            R.drawable.a_3xx_mf_theme_3xx_background_key_selector);
                    int[] drawableState = key.getCurrentDrawableState();
                    if (key.codes[0] != 0) {
                        npd.setState(drawableState);
                    }
                    npd.setAlpha(opacity);
                    npd.setBounds(key.x, key.y, key.x + key.width, key.y
                            + key.height);
                    npd.draw(canvas);


                }*/
            }

        }else if(listS.equals("4")){
            List<Key> keys = getKeyboard().getKeys();
            for(Key key: keys) {
                if(key.icon != null) {
                    drawenterkey(canvas, key);
                }
                if(key.label != null) {
/////////////////////////////////
                    String keyLabel = key.label.toString();
                    if (getKeyboard().isShifted()) {
                        keyLabel = keyLabel.toUpperCase();
                    }
                    if(returnvalue(key)){

                        ////////#######################
                        paintforsize.setColor(
                                getResources().getColor(R.color.a_4_mf_theme_4_color_letters));
                        paintforsize2fortwolabelkey.setColor(
                                getResources().getColor(R.color.a_4_mf_theme_4_color_letters_littleword));

                        Drawable npd = cachedDrawable(
                                R.drawable.a_4_mf_theme_4_background_key_letters_selector);
                        int[] drawableState = key.getCurrentDrawableState();
                        if (key.codes[0] != 0) {
                            npd.setState(drawableState);
                        }
                        npd.setAlpha(opacity);
                        npd.setBounds(key.x, key.y, key.x + key.width, key.y
                                + key.height);
                        npd.draw(canvas);


                        //////////////////////////###############

                        printcharactor(key,canvas,paintforsize,paintforsize2fortwolabelkey,keyLabel);


                    }else //if(isWordnumbers(key.codes[0]))
                    {

                        Drawable npd = cachedDrawable(
                                R.drawable.a_4_mf_theme_4_background_key_selector);
                        int[] drawableState = key.getCurrentDrawableState();
                        if (key.codes[0] != 0) {
                            npd.setState(drawableState);
                        }
                        npd.setAlpha(opacity);
                        npd.setBounds(key.x, key.y, key.x + key.width, key.y
                                + key.height);
                        npd.draw(canvas);


                        drawaarab(canvas,key,keyLabel,paintforsizenumbersfixed);

                        canvas.drawText(key.label.toString(), key.x + (key.width / 2),
                                key.y + 7 * (key.height / 10), paintforsizenumbersfixed);

                    }


                    //////////////////




                }/*else {
                    Drawable npd = cachedDrawable(
                            R.drawable.a_4_mf_theme_4_background_key_selector);
                    int[] drawableState = key.getCurrentDrawableState();
                    if (key.codes[0] != 0) {
                        npd.setState(drawableState);
                    }
                    npd.setAlpha(opacity);
                    npd.setBounds(key.x, key.y, key.x + key.width, key.y
                            + key.height);
                    npd.draw(canvas);


                }*/
            }

        }else if(listS.equals("5")){
            List<Key> keys = getKeyboard().getKeys();
            for(Key key: keys) {
                if(key.icon != null) {
                    drawenterkey(canvas, key);
                }
                if(key.label != null) {
/////////////////////////////////
                    String keyLabel = key.label.toString();
                    if (getKeyboard().isShifted()) {
                        keyLabel = keyLabel.toUpperCase();
                    }
                    if(returnvalue(key)){

                        ////////#######################
                        paintforsize.setColor(
                                getResources().getColor(R.color.a_5_mf_theme_5_color_letters));
                        paintforsize2fortwolabelkey.setColor(
                                getResources().getColor(R.color.a_5_mf_theme_5_color_letters_littleword));

                        Drawable npd = cachedDrawable(
                                R.drawable.a_5_mf_theme_5_background_key_letters_selector);
                        int[] drawableState = key.getCurrentDrawableState();
                        if (key.codes[0] != 0) {
                            npd.setState(drawableState);
                        }
                        npd.setAlpha(opacity);
                        npd.setBounds(key.x, key.y, key.x + key.width, key.y
                                + key.height);
                        npd.draw(canvas);


                        //////////////////////////###############

                        printcharactor(key,canvas,paintforsize,paintforsize2fortwolabelkey,keyLabel);


                    }else //if(isWordnumbers(key.codes[0]))
                    {

                        Drawable npd = cachedDrawable(
                                R.drawable.a_5_mf_theme_5_background_key_selector);
                        int[] drawableState = key.getCurrentDrawableState();
                        if (key.codes[0] != 0) {
                            npd.setState(drawableState);
                        }
                        npd.setAlpha(opacity);
                        npd.setBounds(key.x, key.y, key.x + key.width, key.y
                                + key.height);
                        npd.draw(canvas);


                        drawaarab(canvas,key,keyLabel,paintforsizenumbersfixed);

                        canvas.drawText(key.label.toString(), key.x + (key.width / 2),
                                key.y + 7 * (key.height / 10), paintforsizenumbersfixed);

                    }


                    //////////////////




                }/*else {
                    Drawable npd = cachedDrawable(
                            R.drawable.a_5_mf_theme_5_background_key_selector);
                    int[] drawableState = key.getCurrentDrawableState();
                    if (key.codes[0] != 0) {
                        npd.setState(drawableState);
                    }
                    npd.setAlpha(opacity);
                    npd.setBounds(key.x, key.y, key.x + key.width, key.y
                            + key.height);
                    npd.draw(canvas);


                }*/
            }

        }else if(listS.equals("5x")){
            List<Key> keys = getKeyboard().getKeys();
            for(Key key: keys) {
                if(key.icon != null) {
                    drawenterkey(canvas, key);
                }
                if(key.label != null) {
/////////////////////////////////
                    String keyLabel = key.label.toString();
                    if (getKeyboard().isShifted()) {
                        keyLabel = keyLabel.toUpperCase();
                    }
                    if(returnvalue(key)){

                        ////////#######################
                        paintforsize.setColor(
                                getResources().getColor(R.color.a_5_mf_theme_5_color_letters));
                        paintforsize2fortwolabelkey.setColor(
                                getResources().getColor(R.color.a_5_mf_theme_5_color_letters_littleword));

                        Drawable npd = cachedDrawable(
                                R.drawable.a_5x_mf_theme_5x_background_key_letters_selector);
                        int[] drawableState = key.getCurrentDrawableState();
                        if (key.codes[0] != 0) {
                            npd.setState(drawableState);
                        }
                        npd.setAlpha(opacity);
                        npd.setBounds(key.x, key.y, key.x + key.width, key.y
                                + key.height);
                        npd.draw(canvas);


                        //////////////////////////###############

                        printcharactor(key,canvas,paintforsize,paintforsize2fortwolabelkey,keyLabel);


                    }else //if(isWordnumbers(key.codes[0]))
                    {

                        Drawable npd = cachedDrawable(
                                R.drawable.a_5x_mf_theme_5x_background_key_selector);
                        int[] drawableState = key.getCurrentDrawableState();
                        if (key.codes[0] != 0) {
                            npd.setState(drawableState);
                        }
                        npd.setAlpha(opacity);
                        npd.setBounds(key.x, key.y, key.x + key.width, key.y
                                + key.height);
                        npd.draw(canvas);


                        drawaarab(canvas,key,keyLabel,paintforsizenumbersfixed);

                        canvas.drawText(key.label.toString(), key.x + (key.width / 2),
                                key.y + 7 * (key.height / 10), paintforsizenumbersfixed);

                    }


                    //////////////////




                }/*else {
                    Drawable npd = cachedDrawable(
                            R.drawable.a_5x_mf_theme_5x_background_key_selector);
                    int[] drawableState = key.getCurrentDrawableState();
                    if (key.codes[0] != 0) {
                        npd.setState(drawableState);
                    }
                    npd.setAlpha(opacity);
                    npd.setBounds(key.x, key.y, key.x + key.width, key.y
                            + key.height);
                    npd.draw(canvas);


                }*/
            }

        }else if(listS.equals("5xx")){
            List<Key> keys = getKeyboard().getKeys();
            for(Key key: keys) {
                if(key.icon != null) {
                    drawenterkey(canvas, key);
                }
                if(key.label != null) {
/////////////////////////////////
                    String keyLabel = key.label.toString();
                    if (getKeyboard().isShifted()) {
                        keyLabel = keyLabel.toUpperCase();
                    }
                    if(returnvalue(key)){

                        ////////#######################
                        paintforsize.setColor(
                                getResources().getColor(R.color.a_5_mf_theme_5_color_letters));
                        paintforsize2fortwolabelkey.setColor(
                                getResources().getColor(R.color.a_5_mf_theme_5_color_letters_littleword));

                        Drawable npd = cachedDrawable(
                                R.drawable.a_5xx_mf_theme_5xx_background_key_letters_selector);
                        int[] drawableState = key.getCurrentDrawableState();
                        if (key.codes[0] != 0) {
                            npd.setState(drawableState);
                        }
                        npd.setAlpha(opacity);
                        npd.setBounds(key.x, key.y, key.x + key.width, key.y
                                + key.height);
                        npd.draw(canvas);


                        //////////////////////////###############

                        printcharactor(key,canvas,paintforsize,paintforsize2fortwolabelkey,keyLabel);


                    }else //if(isWordnumbers(key.codes[0]))
                    {

                        Drawable npd = cachedDrawable(
                                R.drawable.a_5xx_mf_theme_5xx_background_key_selector);
                        int[] drawableState = key.getCurrentDrawableState();
                        if (key.codes[0] != 0) {
                            npd.setState(drawableState);
                        }
                        npd.setAlpha(opacity);
                        npd.setBounds(key.x, key.y, key.x + key.width, key.y
                                + key.height);
                        npd.draw(canvas);


                        drawaarab(canvas,key,keyLabel,paintforsizenumbersfixed);

                        canvas.drawText(key.label.toString(), key.x + (key.width / 2),
                                key.y + 7 * (key.height / 10), paintforsizenumbersfixed);

                    }


                    //////////////////




                }/*else {
                    Drawable npd = cachedDrawable(
                            R.drawable.a_5xx_mf_theme_5xx_background_key_selector);
                    int[] drawableState = key.getCurrentDrawableState();
                    if (key.codes[0] != 0) {
                        npd.setState(drawableState);
                    }
                    npd.setAlpha(opacity);
                    npd.setBounds(key.x, key.y, key.x + key.width, key.y
                            + key.height);
                    npd.draw(canvas);


                }*/
            }

        }else if(listS.equals("6")){
            List<Key> keys = getKeyboard().getKeys();
            for(Key key: keys) {
                if(key.icon != null) {
                    drawenterkey(canvas, key);
                }
                if(key.label != null) {
/////////////////////////////////
                    String keyLabel = key.label.toString();
                    if (getKeyboard().isShifted()) {
                        keyLabel = keyLabel.toUpperCase();
                    }
                    if(returnvalue(key)){

                        ////////#######################
                        paintforsize.setColor(
                                getResources().getColor(R.color.a_6_mf_theme_6_color_letters));
                        paintforsize2fortwolabelkey.setColor(
                                getResources().getColor(R.color.a_6_mf_theme_6_color_letters_littleword));

                        Drawable npd = cachedDrawable(
                                R.drawable.a_6_mf_theme_6_background_key_letters_selector);
                        int[] drawableState = key.getCurrentDrawableState();
                        if (key.codes[0] != 0) {
                            npd.setState(drawableState);
                        }
                        npd.setAlpha(opacity);
                        npd.setBounds(key.x, key.y, key.x + key.width, key.y
                                + key.height);
                        npd.draw(canvas);


                        //////////////////////////###############

                        printcharactor(key,canvas,paintforsize,paintforsize2fortwolabelkey,keyLabel);


                    }else //if(isWordnumbers(key.codes[0]))
                    {

                        Drawable npd = cachedDrawable(
                                R.drawable.a_6_mf_theme_6_background_key_selector);
                        int[] drawableState = key.getCurrentDrawableState();
                        if (key.codes[0] != 0) {
                            npd.setState(drawableState);
                        }
                        npd.setAlpha(opacity);
                        npd.setBounds(key.x, key.y, key.x + key.width, key.y
                                + key.height);
                        npd.draw(canvas);


                        drawaarab(canvas,key,keyLabel,paintforsizenumbersfixed);

                        canvas.drawText(key.label.toString(), key.x + (key.width / 2),
                                key.y + 7 * (key.height / 10), paintforsizenumbersfixed);

                    }


                    //////////////////




                }/*else {
                    Drawable npd = cachedDrawable(
                            R.drawable.a_6_mf_theme_6_background_key_selector);
                    int[] drawableState = key.getCurrentDrawableState();
                    if (key.codes[0] != 0) {
                        npd.setState(drawableState);
                    }
                    npd.setAlpha(opacity);
                    npd.setBounds(key.x, key.y, key.x + key.width, key.y
                            + key.height);
                    npd.draw(canvas);


                }*/
            }

        }else if(listS.equals("6x")){
            List<Key> keys = getKeyboard().getKeys();
            for(Key key: keys) {
                if(key.icon != null) {
                    drawenterkey(canvas, key);
                }
                if(key.label != null) {
/////////////////////////////////
                    String keyLabel = key.label.toString();
                    if (getKeyboard().isShifted()) {
                        keyLabel = keyLabel.toUpperCase();
                    }
                    if(returnvalue(key)){

                        ////////#######################
                        paintforsize.setColor(
                                getResources().getColor(R.color.a_6_mf_theme_6_color_letters));
                        paintforsize2fortwolabelkey.setColor(
                                getResources().getColor(R.color.a_6_mf_theme_6_color_letters_littleword));

                        Drawable npd = cachedDrawable(
                                R.drawable.a_6x_mf_theme_6x_background_key_letters_selector);
                        int[] drawableState = key.getCurrentDrawableState();
                        if (key.codes[0] != 0) {
                            npd.setState(drawableState);
                        }
                        npd.setAlpha(opacity);
                        npd.setBounds(key.x, key.y, key.x + key.width, key.y
                                + key.height);
                        npd.draw(canvas);


                        //////////////////////////###############

                        printcharactor(key,canvas,paintforsize,paintforsize2fortwolabelkey,keyLabel);


                    }else //if(isWordnumbers(key.codes[0]))
                    {

                        Drawable npd = cachedDrawable(
                                R.drawable.a_6x_mf_theme_6x_background_key_selector);
                        int[] drawableState = key.getCurrentDrawableState();
                        if (key.codes[0] != 0) {
                            npd.setState(drawableState);
                        }
                        npd.setAlpha(opacity);
                        npd.setBounds(key.x, key.y, key.x + key.width, key.y
                                + key.height);
                        npd.draw(canvas);


                        drawaarab(canvas,key,keyLabel,paintforsizenumbersfixed);

                        canvas.drawText(key.label.toString(), key.x + (key.width / 2),
                                key.y + 7 * (key.height / 10), paintforsizenumbersfixed);

                    }


                    //////////////////




                }/*else {
                    Drawable npd = cachedDrawable(
                            R.drawable.a_6x_mf_theme_6x_background_key_selector);
                    int[] drawableState = key.getCurrentDrawableState();
                    if (key.codes[0] != 0) {
                        npd.setState(drawableState);
                    }
                    npd.setAlpha(opacity);
                    npd.setBounds(key.x, key.y, key.x + key.width, key.y
                            + key.height);
                    npd.draw(canvas);


                }*/
            }

        }else if(listS.equals("6xx")){
            List<Key> keys = getKeyboard().getKeys();
            for(Key key: keys) {
                if(key.icon != null) {
                    drawenterkey(canvas, key);
                }
                if(key.label != null) {
/////////////////////////////////
                    String keyLabel = key.label.toString();
                    if (getKeyboard().isShifted()) {
                        keyLabel = keyLabel.toUpperCase();
                    }
                    if(returnvalue(key)){

                        ////////#######################
                        paintforsize.setColor(
                                getResources().getColor(R.color.a_6_mf_theme_6_color_letters));
                        paintforsize2fortwolabelkey.setColor(
                                getResources().getColor(R.color.a_6_mf_theme_6_color_letters_littleword));

                        Drawable npd = cachedDrawable(
                                R.drawable.a_6xx_mf_theme_6xx_background_key_letters_selector);
                        int[] drawableState = key.getCurrentDrawableState();
                        if (key.codes[0] != 0) {
                            npd.setState(drawableState);
                        }
                        npd.setAlpha(opacity);
                        npd.setBounds(key.x, key.y, key.x + key.width, key.y
                                + key.height);
                        npd.draw(canvas);


                        //////////////////////////###############

                        printcharactor(key,canvas,paintforsize,paintforsize2fortwolabelkey,keyLabel);


                    }else //if(isWordnumbers(key.codes[0]))
                    {

                        Drawable npd = cachedDrawable(
                                R.drawable.a_6xx_mf_theme_6xx_background_key_selector);
                        int[] drawableState = key.getCurrentDrawableState();
                        if (key.codes[0] != 0) {
                            npd.setState(drawableState);
                        }
                        npd.setAlpha(opacity);
                        npd.setBounds(key.x, key.y, key.x + key.width, key.y
                                + key.height);
                        npd.draw(canvas);


                        drawaarab(canvas,key,keyLabel,paintforsizenumbersfixed);

                        canvas.drawText(key.label.toString(), key.x + (key.width / 2),
                                key.y + 7 * (key.height / 10), paintforsizenumbersfixed);

                    }


                    //////////////////




                }/*else {
                    Drawable npd = cachedDrawable(
                            R.drawable.a_6xx_mf_theme_6xx_background_key_selector);
                    int[] drawableState = key.getCurrentDrawableState();
                    if (key.codes[0] != 0) {
                        npd.setState(drawableState);
                    }
                    npd.setAlpha(opacity);
                    npd.setBounds(key.x, key.y, key.x + key.width, key.y
                            + key.height);
                    npd.draw(canvas);


                }*/
            }

        }else if(listS.equals("7")){
            List<Key> keys = getKeyboard().getKeys();
            for(Key key: keys) {
                if(key.icon != null) {
                    drawenterkey(canvas, key);
                }
                if(key.label != null) {
/////////////////////////////////
                    String keyLabel = key.label.toString();
                    if (getKeyboard().isShifted()) {
                        keyLabel = keyLabel.toUpperCase();
                    }
                    if(returnvalue(key)){

                        ////////#######################
                        paintforsize.setColor(
                                getResources().getColor(R.color.a_7_mf_theme_7_color_letters));
                        paintforsize2fortwolabelkey.setColor(
                                getResources().getColor(R.color.a_7_mf_theme_7_color_letters_littleword));

                        Drawable npd = cachedDrawable(
                                R.drawable.a_7_mf_theme_7_background_key_letters_selector);
                        int[] drawableState = key.getCurrentDrawableState();
                        if (key.codes[0] != 0) {
                            npd.setState(drawableState);
                        }
                        npd.setAlpha(opacity);
                        npd.setBounds(key.x, key.y, key.x + key.width, key.y
                                + key.height);
                        npd.draw(canvas);


                        //////////////////////////###############

                        printcharactor(key,canvas,paintforsize,paintforsize2fortwolabelkey,keyLabel);


                    }else //if(isWordnumbers(key.codes[0]))
                    {

                        Drawable npd = cachedDrawable(
                                R.drawable.a_7_mf_theme_7_background_key_selector);
                        int[] drawableState = key.getCurrentDrawableState();
                        if (key.codes[0] != 0) {
                            npd.setState(drawableState);
                        }
                        npd.setAlpha(opacity);
                        npd.setBounds(key.x, key.y, key.x + key.width, key.y
                                + key.height);
                        npd.draw(canvas);


                        drawaarab(canvas,key,keyLabel,paintforsizenumbersfixed);

                        canvas.drawText(key.label.toString(), key.x + (key.width / 2),
                                key.y + 7 * (key.height / 10), paintforsizenumbersfixed);

                    }


                    //////////////////




                }/*else {
                    Drawable npd = cachedDrawable(
                            R.drawable.a_7_mf_theme_7_background_key_selector);
                    int[] drawableState = key.getCurrentDrawableState();
                    if (key.codes[0] != 0) {
                        npd.setState(drawableState);
                    }
                    npd.setAlpha(opacity);
                    npd.setBounds(key.x, key.y, key.x + key.width, key.y
                            + key.height);
                    npd.draw(canvas);


                }*/
            }

        }else if(listS.equals("7x")){
            List<Key> keys = getKeyboard().getKeys();
            for(Key key: keys) {
                if(key.icon != null) {
                    drawenterkey(canvas, key);
                }
                if(key.label != null) {
/////////////////////////////////
                    String keyLabel = key.label.toString();
                    if (getKeyboard().isShifted()) {
                        keyLabel = keyLabel.toUpperCase();
                    }
                    if(returnvalue(key)){

                        ////////#######################
                        paintforsize.setColor(
                                getResources().getColor(R.color.a_7_mf_theme_7_color_letters));
                        paintforsize2fortwolabelkey.setColor(
                                getResources().getColor(R.color.a_7_mf_theme_7_color_letters_littleword));

                        Drawable npd = cachedDrawable(
                                R.drawable.a_7x_mf_theme_7x_background_key_letters_selector);
                        int[] drawableState = key.getCurrentDrawableState();
                        if (key.codes[0] != 0) {
                            npd.setState(drawableState);
                        }
                        npd.setAlpha(opacity);
                        npd.setBounds(key.x, key.y, key.x + key.width, key.y
                                + key.height);
                        npd.draw(canvas);


                        //////////////////////////###############

                        printcharactor(key,canvas,paintforsize,paintforsize2fortwolabelkey,keyLabel);


                    }else //if(isWordnumbers(key.codes[0]))
                    {

                        Drawable npd = cachedDrawable(
                                R.drawable.a_7x_mf_theme_7x_background_key_selector);
                        int[] drawableState = key.getCurrentDrawableState();
                        if (key.codes[0] != 0) {
                            npd.setState(drawableState);
                        }
                        npd.setAlpha(opacity);
                        npd.setBounds(key.x, key.y, key.x + key.width, key.y
                                + key.height);
                        npd.draw(canvas);


                        drawaarab(canvas,key,keyLabel,paintforsizenumbersfixed);

                        canvas.drawText(key.label.toString(), key.x + (key.width / 2),
                                key.y + 7 * (key.height / 10), paintforsizenumbersfixed);

                    }


                    //////////////////




                }/*else {
                    Drawable npd = cachedDrawable(
                            R.drawable.a_7x_mf_theme_7x_background_key_selector);
                    int[] drawableState = key.getCurrentDrawableState();
                    if (key.codes[0] != 0) {
                        npd.setState(drawableState);
                    }
                    npd.setAlpha(opacity);
                    npd.setBounds(key.x, key.y, key.x + key.width, key.y
                            + key.height);
                    npd.draw(canvas);


                }*/
            }

        }else if(listS.equals("7xx")){
            List<Key> keys = getKeyboard().getKeys();
            for(Key key: keys) {
                if(key.icon != null) {
                    drawenterkey(canvas, key);
                }
                if(key.label != null) {
/////////////////////////////////
                    String keyLabel = key.label.toString();
                    if (getKeyboard().isShifted()) {
                        keyLabel = keyLabel.toUpperCase();
                    }
                    if(returnvalue(key)){

                        ////////#######################
                        paintforsize.setColor(
                                getResources().getColor(R.color.a_7_mf_theme_7_color_letters));
                        paintforsize2fortwolabelkey.setColor(
                                getResources().getColor(R.color.a_7_mf_theme_7_color_letters_littleword));

                        Drawable npd = cachedDrawable(
                                R.drawable.a_7xx_mf_theme_7xx_background_key_letters_selector);
                        int[] drawableState = key.getCurrentDrawableState();
                        if (key.codes[0] != 0) {
                            npd.setState(drawableState);
                        }
                        npd.setAlpha(opacity);
                        npd.setBounds(key.x, key.y, key.x + key.width, key.y
                                + key.height);
                        npd.draw(canvas);


                        //////////////////////////###############

                        printcharactor(key,canvas,paintforsize,paintforsize2fortwolabelkey,keyLabel);


                    }else //if(isWordnumbers(key.codes[0]))
                    {

                        Drawable npd = cachedDrawable(
                                R.drawable.a_7xx_mf_theme_7xx_background_key_selector);
                        int[] drawableState = key.getCurrentDrawableState();
                        if (key.codes[0] != 0) {
                            npd.setState(drawableState);
                        }
                        npd.setAlpha(opacity);
                        npd.setBounds(key.x, key.y, key.x + key.width, key.y
                                + key.height);
                        npd.draw(canvas);


                        drawaarab(canvas,key,keyLabel,paintforsizenumbersfixed);

                        canvas.drawText(key.label.toString(), key.x + (key.width / 2),
                                key.y + 7 * (key.height / 10), paintforsizenumbersfixed);

                    }


                    //////////////////




                }/*else {
                    Drawable npd = cachedDrawable(
                            R.drawable.a_7xx_mf_theme_7xx_background_key_selector);
                    int[] drawableState = key.getCurrentDrawableState();
                    if (key.codes[0] != 0) {
                        npd.setState(drawableState);
                    }
                    npd.setAlpha(opacity);
                    npd.setBounds(key.x, key.y, key.x + key.width, key.y
                            + key.height);
                    npd.draw(canvas);


                }*/
            }

        }else if(listS.equals("8")){
            List<Key> keys = getKeyboard().getKeys();
            for(Key key: keys) {
                if(key.icon != null) {
                    drawenterkey(canvas, key);
                }
                if(key.label != null) {
/////////////////////////////////
                    String keyLabel = key.label.toString();
                    if (getKeyboard().isShifted()) {
                        keyLabel = keyLabel.toUpperCase();
                    }
                    if(returnvalue(key)){

                        ////////#######################
                        paintforsize.setColor(
                                getResources().getColor(R.color.a_8_mf_theme_8_color_letters));
                        paintforsize2fortwolabelkey.setColor(
                                getResources().getColor(R.color.a_8_mf_theme_8_color_letters_littleword));

                        Drawable npd = cachedDrawable(
                                R.drawable.a_8_mf_theme_8_background_key_letters_selector);
                        int[] drawableState = key.getCurrentDrawableState();
                        if (key.codes[0] != 0) {
                            npd.setState(drawableState);
                        }
                        npd.setAlpha(opacity);
                        npd.setBounds(key.x, key.y, key.x + key.width, key.y
                                + key.height);
                        npd.draw(canvas);


                        //////////////////////////###############

                        printcharactor(key,canvas,paintforsize,paintforsize2fortwolabelkey,keyLabel);


                    }else //if(isWordnumbers(key.codes[0]))
                    {

                        Drawable npd = cachedDrawable(
                                R.drawable.a_8_mf_theme_8_background_key_selector);
                        int[] drawableState = key.getCurrentDrawableState();
                        if (key.codes[0] != 0) {
                            npd.setState(drawableState);
                        }
                        npd.setAlpha(opacity);
                        npd.setBounds(key.x, key.y, key.x + key.width, key.y
                                + key.height);
                        npd.draw(canvas);


                        drawaarab(canvas,key,keyLabel,paintforsizenumbersfixed);

                        canvas.drawText(key.label.toString(), key.x + (key.width / 2),
                                key.y + 7 * (key.height / 10), paintforsizenumbersfixed);

                    }


                    //////////////////




                }/*else {
                    Drawable npd = cachedDrawable(
                            R.drawable.a_8_mf_theme_8_background_key_selector);
                    int[] drawableState = key.getCurrentDrawableState();
                    if (key.codes[0] != 0) {
                        npd.setState(drawableState);
                    }
                    npd.setAlpha(opacity);
                    npd.setBounds(key.x, key.y, key.x + key.width, key.y
                            + key.height);
                    npd.draw(canvas);


                }*/
            }

        }else if(listS.equals("8x")){
            List<Key> keys = getKeyboard().getKeys();
            for(Key key: keys) {
                if(key.icon != null) {
                    drawenterkey(canvas, key);
                }
                if(key.label != null) {
/////////////////////////////////
                    String keyLabel = key.label.toString();
                    if (getKeyboard().isShifted()) {
                        keyLabel = keyLabel.toUpperCase();
                    }
                    if(returnvalue(key)){

                        ////////#######################
                        paintforsize.setColor(
                                getResources().getColor(R.color.a_8_mf_theme_8_color_letters));
                        paintforsize2fortwolabelkey.setColor(
                                getResources().getColor(R.color.a_8_mf_theme_8_color_letters_littleword));

                        Drawable npd = cachedDrawable(
                                R.drawable.a_8x_mf_theme_8x_background_key_letters_selector);
                        int[] drawableState = key.getCurrentDrawableState();
                        if (key.codes[0] != 0) {
                            npd.setState(drawableState);
                        }
                        npd.setAlpha(opacity);
                        npd.setBounds(key.x, key.y, key.x + key.width, key.y
                                + key.height);
                        npd.draw(canvas);


                        //////////////////////////###############

                        printcharactor(key,canvas,paintforsize,paintforsize2fortwolabelkey,keyLabel);


                    }else //if(isWordnumbers(key.codes[0]))
                    {

                        Drawable npd = cachedDrawable(
                                R.drawable.a_8x_mf_theme_8x_background_key_selector);
                        int[] drawableState = key.getCurrentDrawableState();
                        if (key.codes[0] != 0) {
                            npd.setState(drawableState);
                        }
                        npd.setAlpha(opacity);
                        npd.setBounds(key.x, key.y, key.x + key.width, key.y
                                + key.height);
                        npd.draw(canvas);


                        drawaarab(canvas,key,keyLabel,paintforsizenumbersfixed);

                        canvas.drawText(key.label.toString(), key.x + (key.width / 2),
                                key.y + 7 * (key.height / 10), paintforsizenumbersfixed);

                    }


                    //////////////////




                }/*else {
                    Drawable npd = cachedDrawable(
                            R.drawable.a_8x_mf_theme_8x_background_key_selector);
                    int[] drawableState = key.getCurrentDrawableState();
                    if (key.codes[0] != 0) {
                        npd.setState(drawableState);
                    }
                    npd.setAlpha(opacity);
                    npd.setBounds(key.x, key.y, key.x + key.width, key.y
                            + key.height);
                    npd.draw(canvas);


                }*/
            }

        }else if(listS.equals("8xx")){
            List<Key> keys = getKeyboard().getKeys();
            for(Key key: keys) {
                if(key.icon != null) {
                    drawenterkey(canvas, key);
                }
                if(key.label != null) {
/////////////////////////////////
                    String keyLabel = key.label.toString();
                    if (getKeyboard().isShifted()) {
                        keyLabel = keyLabel.toUpperCase();
                    }
                    if(returnvalue(key)){

                        ////////#######################
                        paintforsize.setColor(
                                getResources().getColor(R.color.a_8_mf_theme_8_color_letters));
                        paintforsize2fortwolabelkey.setColor(
                                getResources().getColor(R.color.a_8_mf_theme_8_color_letters_littleword));

                        Drawable npd = cachedDrawable(
                                R.drawable.a_8xx_mf_theme_8xx_background_key_letters_selector);
                        int[] drawableState = key.getCurrentDrawableState();
                        if (key.codes[0] != 0) {
                            npd.setState(drawableState);
                        }
                        npd.setAlpha(opacity);
                        npd.setBounds(key.x, key.y, key.x + key.width, key.y
                                + key.height);
                        npd.draw(canvas);


                        //////////////////////////###############

                        printcharactor(key,canvas,paintforsize,paintforsize2fortwolabelkey,keyLabel);


                    }else //if(isWordnumbers(key.codes[0]))
                    {

                        Drawable npd = cachedDrawable(
                                R.drawable.a_8xx_mf_theme_8xx_background_key_selector);
                        int[] drawableState = key.getCurrentDrawableState();
                        if (key.codes[0] != 0) {
                            npd.setState(drawableState);
                        }
                        npd.setAlpha(opacity);
                        npd.setBounds(key.x, key.y, key.x + key.width, key.y
                                + key.height);
                        npd.draw(canvas);


                        drawaarab(canvas,key,keyLabel,paintforsizenumbersfixed);

                        canvas.drawText(key.label.toString(), key.x + (key.width / 2),
                                key.y + 7 * (key.height / 10), paintforsizenumbersfixed);

                    }


                    //////////////////




                }/*else {
                    Drawable npd = cachedDrawable(
                            R.drawable.a_8xx_mf_theme_8xx_background_key_selector);
                    int[] drawableState = key.getCurrentDrawableState();
                    if (key.codes[0] != 0) {
                        npd.setState(drawableState);
                    }
                    npd.setAlpha(opacity);
                    npd.setBounds(key.x, key.y, key.x + key.width, key.y
                            + key.height);
                    npd.draw(canvas);


                }*/
            }

        }else if(listS.equals("9x")){
            List<Key> keys = getKeyboard().getKeys();
            for(Key key: keys) {
                if(key.icon != null) {
                    drawenterkey(canvas, key);
                }
                if(key.label != null) {
/////////////////////////////////
                    String keyLabel = key.label.toString();
                    if (getKeyboard().isShifted()) {
                        keyLabel = keyLabel.toUpperCase();
                    }
                    if(returnvalue(key)){

                        ////////#######################
                        paintforsize.setColor(
                                getResources().getColor(R.color.a_9_mf_theme_9_color_letters));
                        paintforsize2fortwolabelkey.setColor(
                                getResources().getColor(R.color.a_9_mf_theme_9_color_letters_littleword));

                        Drawable npd = cachedDrawable(
                                R.drawable.a_9x_mf_theme_9x_background_key_letters_selector);
                        int[] drawableState = key.getCurrentDrawableState();
                        if (key.codes[0] != 0) {
                            npd.setState(drawableState);
                        }
                        npd.setAlpha(opacity);
                        npd.setBounds(key.x, key.y, key.x + key.width, key.y
                                + key.height);
                        npd.draw(canvas);


                        //////////////////////////###############

                        printcharactor(key,canvas,paintforsize,paintforsize2fortwolabelkey,keyLabel);


                    }else //if(isWordnumbers(key.codes[0]))
                    {

                        Drawable npd = cachedDrawable(
                                R.drawable.a_9x_mf_theme_9x_background_key_selector);
                        int[] drawableState = key.getCurrentDrawableState();
                        if (key.codes[0] != 0) {
                            npd.setState(drawableState);
                        }
                        npd.setAlpha(opacity);
                        npd.setBounds(key.x, key.y, key.x + key.width, key.y
                                + key.height);
                        npd.draw(canvas);


                        drawaarab(canvas,key,keyLabel,paintforsizenumbersfixed);

                        canvas.drawText(key.label.toString(), key.x + (key.width / 2),
                                key.y + 7 * (key.height / 10), paintforsizenumbersfixed);

                    }


                    //////////////////




                }/*else {
                    Drawable npd = cachedDrawable(
                            R.drawable.a_9x_mf_theme_9x_background_key_selector);
                    int[] drawableState = key.getCurrentDrawableState();
                    if (key.codes[0] != 0) {
                        npd.setState(drawableState);
                    }
                    npd.setAlpha(opacity);
                    npd.setBounds(key.x, key.y, key.x + key.width, key.y
                            + key.height);
                    npd.draw(canvas);


                }*/
            }

        }else if(listS.equals("9xx")){
            List<Key> keys = getKeyboard().getKeys();
            for(Key key: keys) {
                if(key.icon != null) {
                    drawenterkey(canvas, key);
                }
                if(key.label != null) {
/////////////////////////////////
                    String keyLabel = key.label.toString();
                    if (getKeyboard().isShifted()) {
                        keyLabel = keyLabel.toUpperCase();
                    }
                    if(returnvalue(key)){

                        ////////#######################
                        paintforsize.setColor(
                                getResources().getColor(R.color.a_9_mf_theme_9_color_letters));
                        paintforsize2fortwolabelkey.setColor(
                                getResources().getColor(R.color.a_9_mf_theme_9_color_letters_littleword));

                        Drawable npd = cachedDrawable(
                                R.drawable.a_9xx_mf_theme_9xx_background_key_letters_selector);
                        int[] drawableState = key.getCurrentDrawableState();
                        if (key.codes[0] != 0) {
                            npd.setState(drawableState);
                        }
                        npd.setAlpha(opacity);
                        npd.setBounds(key.x, key.y, key.x + key.width, key.y
                                + key.height);
                        npd.draw(canvas);


                        //////////////////////////###############

                        printcharactor(key,canvas,paintforsize,paintforsize2fortwolabelkey,keyLabel);


                    }else //if(isWordnumbers(key.codes[0]))
                    {

                        Drawable npd = cachedDrawable(
                                R.drawable.a_9xx_mf_theme_9xx_background_key_selector);
                        int[] drawableState = key.getCurrentDrawableState();
                        if (key.codes[0] != 0) {
                            npd.setState(drawableState);
                        }
                        npd.setAlpha(opacity);
                        npd.setBounds(key.x, key.y, key.x + key.width, key.y
                                + key.height);
                        npd.draw(canvas);


                        drawaarab(canvas,key,keyLabel,paintforsizenumbersfixed);

                        canvas.drawText(key.label.toString(), key.x + (key.width / 2),
                                key.y + 7 * (key.height / 10), paintforsizenumbersfixed);

                    }


                    //////////////////




                }/*else {
                    Drawable npd = cachedDrawable(
                            R.drawable.a_9xx_mf_theme_9xx_background_key_selector);
                    int[] drawableState = key.getCurrentDrawableState();
                    if (key.codes[0] != 0) {
                        npd.setState(drawableState);
                    }
                    npd.setAlpha(opacity);
                    npd.setBounds(key.x, key.y, key.x + key.width, key.y
                            + key.height);
                    npd.draw(canvas);


                }*/
            }

        }else if(listS.equals("10")){
            List<Key> keys = getKeyboard().getKeys();
            for(Key key: keys) {
                if(key.icon != null) {
                    drawenterkey(canvas, key);
                }
                if(key.label != null) {
/////////////////////////////////
                    String keyLabel = key.label.toString();
                    if (getKeyboard().isShifted()) {
                        keyLabel = keyLabel.toUpperCase();
                    }
                    if(returnvalue(key)){

                        ////////#######################
                        paintforsize.setColor(
                                getResources().getColor(R.color.a_10_mf_theme_10_color_letters));
                        paintforsize2fortwolabelkey.setColor(
                                getResources().getColor(R.color.a_10_mf_theme_10_color_letters_littleword));

                        Drawable npd = cachedDrawable(
                                R.drawable.a_10_mf_theme_10_background_key_letters_selector);
                        int[] drawableState = key.getCurrentDrawableState();
                        if (key.codes[0] != 0) {
                            npd.setState(drawableState);
                        }
                        npd.setAlpha(opacity);
                        npd.setBounds(key.x, key.y, key.x + key.width, key.y
                                + key.height);
                        npd.draw(canvas);


                        //////////////////////////###############

                        printcharactor(key,canvas,paintforsize,paintforsize2fortwolabelkey,keyLabel);


                    }else //if(isWordnumbers(key.codes[0]))
                    {

                        Drawable npd = cachedDrawable(
                                R.drawable.a_10_mf_theme_10_background_key_selector);
                        int[] drawableState = key.getCurrentDrawableState();
                        if (key.codes[0] != 0) {
                            npd.setState(drawableState);
                        }
                        npd.setAlpha(opacity);
                        npd.setBounds(key.x, key.y, key.x + key.width, key.y
                                + key.height);
                        npd.draw(canvas);


                        drawaarab(canvas,key,keyLabel,paintforsizenumbersfixed);

                        canvas.drawText(key.label.toString(), key.x + (key.width / 2),
                                key.y + 7 * (key.height / 10), paintforsizenumbersfixed);

                    }


                    //////////////////




                }/*else {
                    Drawable npd = cachedDrawable(
                            R.drawable.a_10_mf_theme_10_background_key_selector);
                    int[] drawableState = key.getCurrentDrawableState();
                    if (key.codes[0] != 0) {
                        npd.setState(drawableState);
                    }
                    npd.setAlpha(opacity);
                    npd.setBounds(key.x, key.y, key.x + key.width, key.y
                            + key.height);
                    npd.draw(canvas);


                }*/
            }

        }else if(listS.equals("10x")){
            List<Key> keys = getKeyboard().getKeys();
            for(Key key: keys) {
                if(key.icon != null) {
                    drawenterkey(canvas, key);
                }
                if(key.label != null) {
/////////////////////////////////
                    String keyLabel = key.label.toString();
                    if (getKeyboard().isShifted()) {
                        keyLabel = keyLabel.toUpperCase();
                    }
                    if(returnvalue(key)){

                        ////////#######################
                        paintforsize.setColor(
                                getResources().getColor(R.color.a_10_mf_theme_10_color_letters));
                        paintforsize2fortwolabelkey.setColor(
                                getResources().getColor(R.color.a_10_mf_theme_10_color_letters_littleword));

                        Drawable npd = cachedDrawable(
                                R.drawable.a_10x_mf_theme_10x_background_key_letters_selector);
                        int[] drawableState = key.getCurrentDrawableState();
                        if (key.codes[0] != 0) {
                            npd.setState(drawableState);
                        }
                        npd.setAlpha(opacity);
                        npd.setBounds(key.x, key.y, key.x + key.width, key.y
                                + key.height);
                        npd.draw(canvas);


                        //////////////////////////###############

                        printcharactor(key,canvas,paintforsize,paintforsize2fortwolabelkey,keyLabel);


                    }else //if(isWordnumbers(key.codes[0]))
                    {

                        Drawable npd = cachedDrawable(
                                R.drawable.a_10x_mf_theme_10x_background_key_selector);
                        int[] drawableState = key.getCurrentDrawableState();
                        if (key.codes[0] != 0) {
                            npd.setState(drawableState);
                        }
                        npd.setAlpha(opacity);
                        npd.setBounds(key.x, key.y, key.x + key.width, key.y
                                + key.height);
                        npd.draw(canvas);


                        drawaarab(canvas,key,keyLabel,paintforsizenumbersfixed);

                        canvas.drawText(key.label.toString(), key.x + (key.width / 2),
                                key.y + 7 * (key.height / 10), paintforsizenumbersfixed);

                    }


                    //////////////////




                }/*else {
                    Drawable npd = cachedDrawable(
                            R.drawable.a_10x_mf_theme_10x_background_key_selector);
                    int[] drawableState = key.getCurrentDrawableState();
                    if (key.codes[0] != 0) {
                        npd.setState(drawableState);
                    }
                    npd.setAlpha(opacity);
                    npd.setBounds(key.x, key.y, key.x + key.width, key.y
                            + key.height);
                    npd.draw(canvas);


                }*/
            }

        }else if(listS.equals("10xx")){
            List<Key> keys = getKeyboard().getKeys();
            for(Key key: keys) {
                if(key.icon != null) {
                    drawenterkey(canvas, key);
                }
                if(key.label != null) {
/////////////////////////////////
                    String keyLabel = key.label.toString();
                    if (getKeyboard().isShifted()) {
                        keyLabel = keyLabel.toUpperCase();
                    }
                    if(returnvalue(key)){

                        ////////#######################
                        paintforsize.setColor(
                                getResources().getColor(R.color.a_10_mf_theme_10_color_letters));
                        paintforsize2fortwolabelkey.setColor(
                                getResources().getColor(R.color.a_10_mf_theme_10_color_letters_littleword));

                        Drawable npd = cachedDrawable(
                                R.drawable.a_10xx_mf_theme_10xx_background_key_letters_selector);
                        int[] drawableState = key.getCurrentDrawableState();
                        if (key.codes[0] != 0) {
                            npd.setState(drawableState);
                        }
                        npd.setAlpha(opacity);
                        npd.setBounds(key.x, key.y, key.x + key.width, key.y
                                + key.height);
                        npd.draw(canvas);


                        //////////////////////////###############

                        printcharactor(key,canvas,paintforsize,paintforsize2fortwolabelkey,keyLabel);


                    }else //if(isWordnumbers(key.codes[0]))
                    {

                        Drawable npd = cachedDrawable(
                                R.drawable.a_10xx_mf_theme_10xx_background_key_selector);
                        int[] drawableState = key.getCurrentDrawableState();
                        if (key.codes[0] != 0) {
                            npd.setState(drawableState);
                        }
                        npd.setAlpha(opacity);
                        npd.setBounds(key.x, key.y, key.x + key.width, key.y
                                + key.height);
                        npd.draw(canvas);


                        drawaarab(canvas,key,keyLabel,paintforsizenumbersfixed);

                        canvas.drawText(key.label.toString(), key.x + (key.width / 2),
                                key.y + 7 * (key.height / 10), paintforsizenumbersfixed);

                    }


                    //////////////////




                }/*else {
                    Drawable npd = cachedDrawable(
                            R.drawable.a_10xx_mf_theme_10xx_background_key_selector);
                    int[] drawableState = key.getCurrentDrawableState();
                    if (key.codes[0] != 0) {
                        npd.setState(drawableState);
                    }
                    npd.setAlpha(opacity);
                    npd.setBounds(key.x, key.y, key.x + key.width, key.y
                            + key.height);
                    npd.draw(canvas);


                }*/
            }

        }else if(listS.equals("11")){
            List<Key> keys = getKeyboard().getKeys();
            for(Key key: keys) {
                if(key.icon != null) {
                    drawenterkey(canvas, key);
                }
                if(key.label != null) {
/////////////////////////////////
                    String keyLabel = key.label.toString();
                    if (getKeyboard().isShifted()) {
                        keyLabel = keyLabel.toUpperCase();
                    }
                    if(returnvalue(key)){

                        ////////#######################
                        paintforsize.setColor(
                                getResources().getColor(R.color.a_11_mf_theme_11_color_letters));
                        paintforsize2fortwolabelkey.setColor(
                                getResources().getColor(R.color.a_11_mf_theme_11_color_letters_littleword));

                        Drawable npd = cachedDrawable(
                                R.drawable.a_11_mf_theme_11_background_key_letters_selector);
                        int[] drawableState = key.getCurrentDrawableState();
                        if (key.codes[0] != 0) {
                            npd.setState(drawableState);
                        }
                        npd.setAlpha(opacity);
                        npd.setBounds(key.x, key.y, key.x + key.width, key.y
                                + key.height);
                        npd.draw(canvas);


                        //////////////////////////###############

                        printcharactor(key,canvas,paintforsize,paintforsize2fortwolabelkey,keyLabel);


                    }else //if(isWordnumbers(key.codes[0]))
                    {

                        Drawable npd = cachedDrawable(
                                R.drawable.a_11_mf_theme_11_background_key_selector);
                        int[] drawableState = key.getCurrentDrawableState();
                        if (key.codes[0] != 0) {
                            npd.setState(drawableState);
                        }
                        npd.setAlpha(opacity);
                        npd.setBounds(key.x, key.y, key.x + key.width, key.y
                                + key.height);
                        npd.draw(canvas);


                        drawaarab(canvas,key,keyLabel,paintforsizenumbersfixed);

                        canvas.drawText(key.label.toString(), key.x + (key.width / 2),
                                key.y + 7 * (key.height / 10), paintforsizenumbersfixed);

                    }


                    //////////////////




                }/*else {
                    Drawable npd = cachedDrawable(
                            R.drawable.a_11_mf_theme_11_background_key_selector);
                    int[] drawableState = key.getCurrentDrawableState();
                    if (key.codes[0] != 0) {
                        npd.setState(drawableState);
                    }
                    npd.setAlpha(opacity);
                    npd.setBounds(key.x, key.y, key.x + key.width, key.y
                            + key.height);
                    npd.draw(canvas);


                }*/
            }

        }else if(listS.equals("11x")){
            List<Key> keys = getKeyboard().getKeys();
            for(Key key: keys) {
                if(key.icon != null) {
                    drawenterkey(canvas, key);
                }
                if(key.label != null) {
/////////////////////////////////
                    String keyLabel = key.label.toString();
                    if (getKeyboard().isShifted()) {
                        keyLabel = keyLabel.toUpperCase();
                    }
                    if(returnvalue(key)){

                        ////////#######################
                        paintforsize.setColor(
                                getResources().getColor(R.color.a_11_mf_theme_11_color_letters));
                        paintforsize2fortwolabelkey.setColor(
                                getResources().getColor(R.color.a_11_mf_theme_11_color_letters_littleword));

                        Drawable npd = cachedDrawable(
                                R.drawable.a_11x_mf_theme_11x_background_key_letters_selector);
                        int[] drawableState = key.getCurrentDrawableState();
                        if (key.codes[0] != 0) {
                            npd.setState(drawableState);
                        }
                        npd.setAlpha(opacity);
                        npd.setBounds(key.x, key.y, key.x + key.width, key.y
                                + key.height);
                        npd.draw(canvas);


                        //////////////////////////###############

                        printcharactor(key,canvas,paintforsize,paintforsize2fortwolabelkey,keyLabel);


                    }else //if(isWordnumbers(key.codes[0]))
                    {

                        Drawable npd = cachedDrawable(
                                R.drawable.a_11x_mf_theme_11x_background_key_selector);
                        int[] drawableState = key.getCurrentDrawableState();
                        if (key.codes[0] != 0) {
                            npd.setState(drawableState);
                        }
                        npd.setAlpha(opacity);
                        npd.setBounds(key.x, key.y, key.x + key.width, key.y
                                + key.height);
                        npd.draw(canvas);


                        drawaarab(canvas,key,keyLabel,paintforsizenumbersfixed);

                        canvas.drawText(key.label.toString(), key.x + (key.width / 2),
                                key.y + 7 * (key.height / 10), paintforsizenumbersfixed);

                    }


                    //////////////////




                }/*else {
                    Drawable npd = cachedDrawable(
                            R.drawable.a_11x_mf_theme_11x_background_key_selector);
                    int[] drawableState = key.getCurrentDrawableState();
                    if (key.codes[0] != 0) {
                        npd.setState(drawableState);
                    }
                    npd.setAlpha(opacity);
                    npd.setBounds(key.x, key.y, key.x + key.width, key.y
                            + key.height);
                    npd.draw(canvas);


                }*/
            }

        }else if(listS.equals("11xx")){
            List<Key> keys = getKeyboard().getKeys();
            for(Key key: keys) {
                if(key.icon != null) {
                    drawenterkey(canvas, key);
                }
                if(key.label != null) {
/////////////////////////////////
                    String keyLabel = key.label.toString();
                    if (getKeyboard().isShifted()) {
                        keyLabel = keyLabel.toUpperCase();
                    }
                    if(returnvalue(key)){

                        ////////#######################
                        paintforsize.setColor(
                                getResources().getColor(R.color.a_11_mf_theme_11_color_letters));
                        paintforsize2fortwolabelkey.setColor(
                                getResources().getColor(R.color.a_11_mf_theme_11_color_letters_littleword));

                        Drawable npd = cachedDrawable(
                                R.drawable.a_11xx_mf_theme_11xx_background_key_letters_selector);
                        int[] drawableState = key.getCurrentDrawableState();
                        if (key.codes[0] != 0) {
                            npd.setState(drawableState);
                        }
                        npd.setAlpha(opacity);
                        npd.setBounds(key.x, key.y, key.x + key.width, key.y
                                + key.height);
                        npd.draw(canvas);


                        //////////////////////////###############

                        printcharactor(key,canvas,paintforsize,paintforsize2fortwolabelkey,keyLabel);


                    }else //if(isWordnumbers(key.codes[0]))
                    {

                        Drawable npd = cachedDrawable(
                                R.drawable.a_11xx_mf_theme_11xx_background_key_selector);
                        int[] drawableState = key.getCurrentDrawableState();
                        if (key.codes[0] != 0) {
                            npd.setState(drawableState);
                        }
                        npd.setAlpha(opacity);
                        npd.setBounds(key.x, key.y, key.x + key.width, key.y
                                + key.height);
                        npd.draw(canvas);


                        drawaarab(canvas,key,keyLabel,paintforsizenumbersfixed);

                        canvas.drawText(key.label.toString(), key.x + (key.width / 2),
                                key.y + 7 * (key.height / 10), paintforsizenumbersfixed);

                    }


                    //////////////////




                }/*else {
                    Drawable npd = cachedDrawable(
                            R.drawable.a_11xx_mf_theme_11xx_background_key_selector);
                    int[] drawableState = key.getCurrentDrawableState();
                    if (key.codes[0] != 0) {
                        npd.setState(drawableState);
                    }
                    npd.setAlpha(opacity);
                    npd.setBounds(key.x, key.y, key.x + key.width, key.y
                            + key.height);
                    npd.draw(canvas);


                }*/
            }

        }else if(listS.equals("12")){
            List<Key> keys = getKeyboard().getKeys();
            for(Key key: keys) {
                if(key.icon != null) {
                    drawenterkey(canvas, key);
                }
                if(key.label != null) {
/////////////////////////////////
                    String keyLabel = key.label.toString();
                    if (getKeyboard().isShifted()) {
                        keyLabel = keyLabel.toUpperCase();
                    }
                    if(returnvalue(key)){

                        ////////#######################
                        paintforsize.setColor(
                                getResources().getColor(R.color.a_12_mf_theme_12_color_letters));
                        paintforsize2fortwolabelkey.setColor(
                                getResources().getColor(R.color.a_12_mf_theme_12_color_letters_littleword));

                        Drawable npd = cachedDrawable(
                                R.drawable.a_12_mf_theme_12_background_key_letters_selector);
                        int[] drawableState = key.getCurrentDrawableState();
                        if (key.codes[0] != 0) {
                            npd.setState(drawableState);
                        }
                        npd.setAlpha(opacity);
                        npd.setBounds(key.x, key.y, key.x + key.width, key.y
                                + key.height);
                        npd.draw(canvas);


                        //////////////////////////###############

                        printcharactor(key,canvas,paintforsize,paintforsize2fortwolabelkey,keyLabel);


                    }else //if(isWordnumbers(key.codes[0]))
                    {

                        Drawable npd = cachedDrawable(
                                R.drawable.a_12_mf_theme_12_background_key_selector);
                        int[] drawableState = key.getCurrentDrawableState();
                        if (key.codes[0] != 0) {
                            npd.setState(drawableState);
                        }
                        npd.setAlpha(opacity);
                        npd.setBounds(key.x, key.y, key.x + key.width, key.y
                                + key.height);
                        npd.draw(canvas);


                        drawaarab(canvas,key,keyLabel,paintforsizenumbersfixed);

                        canvas.drawText(key.label.toString(), key.x + (key.width / 2),
                                key.y + 7 * (key.height / 10), paintforsizenumbersfixed);

                    }


                    //////////////////




                }/*else {
                    Drawable npd = cachedDrawable(
                            R.drawable.a_12_mf_theme_12_background_key_selector);
                    int[] drawableState = key.getCurrentDrawableState();
                    if (key.codes[0] != 0) {
                        npd.setState(drawableState);
                    }
                    npd.setAlpha(opacity);
                    npd.setBounds(key.x, key.y, key.x + key.width, key.y
                            + key.height);
                    npd.draw(canvas);


                }*/
            }

        }else if(listS.equals("13")){
            List<Key> keys = getKeyboard().getKeys();
            for(Key key: keys) {
                if(key.icon != null) {
                    drawenterkey(canvas, key);
                }
                if(key.label != null) {
/////////////////////////////////
                    String keyLabel = key.label.toString();
                    if (getKeyboard().isShifted()) {
                        keyLabel = keyLabel.toUpperCase();
                    }
                    if(returnvalue(key)){

                        ////////#######################
                        paintforsize.setColor(
                                getResources().getColor(R.color.a_13_mf_theme_13_color_letters));
                        paintforsize2fortwolabelkey.setColor(
                                getResources().getColor(R.color.a_13_mf_theme_13_color_letters_littleword));

                        Drawable npd = cachedDrawable(
                                R.drawable.a_13_mf_theme_13_background_key_letters_selector);
                        int[] drawableState = key.getCurrentDrawableState();
                        if (key.codes[0] != 0) {
                            npd.setState(drawableState);
                        }
                        npd.setAlpha(opacity);
                        npd.setBounds(key.x, key.y, key.x + key.width, key.y
                                + key.height);
                        npd.draw(canvas);


                        //////////////////////////###############

                        printcharactor(key,canvas,paintforsize,paintforsize2fortwolabelkey,keyLabel);


                    }else //if(isWordnumbers(key.codes[0]))
                    {

                        Drawable npd = cachedDrawable(
                                R.drawable.a_13_mf_theme_13_background_key_selector);
                        int[] drawableState = key.getCurrentDrawableState();
                        if (key.codes[0] != 0) {
                            npd.setState(drawableState);
                        }
                        npd.setAlpha(opacity);
                        npd.setBounds(key.x, key.y, key.x + key.width, key.y
                                + key.height);
                        npd.draw(canvas);


                        drawaarab(canvas,key,keyLabel,paintforsizenumbersfixed);

                        canvas.drawText(key.label.toString(), key.x + (key.width / 2),
                                key.y + 7 * (key.height / 10), paintforsizenumbersfixed);

                    }


                    //////////////////




                }/*else {
                    Drawable npd = cachedDrawable(
                            R.drawable.a_13_mf_theme_13_background_key_selector);
                    int[] drawableState = key.getCurrentDrawableState();
                    if (key.codes[0] != 0) {
                        npd.setState(drawableState);
                    }
                    npd.setAlpha(opacity);
                    npd.setBounds(key.x, key.y, key.x + key.width, key.y
                            + key.height);
                    npd.draw(canvas);


                }*/
            }

        }else if(listS.equals("14")){
            List<Key> keys = getKeyboard().getKeys();
            for(Key key: keys) {
                if(key.icon != null) {
                    drawenterkey(canvas, key);
                }
                if(key.label != null) {
/////////////////////////////////
                    String keyLabel = key.label.toString();
                    if (getKeyboard().isShifted()) {
                        keyLabel = keyLabel.toUpperCase();
                    }
                    if(returnvalue(key)){

                        ////////#######################
                        paintforsize.setColor(
                                getResources().getColor(R.color.a_14_mf_theme_14_color_letters));
                        paintforsize2fortwolabelkey.setColor(
                                getResources().getColor(R.color.a_14_mf_theme_14_color_letters_littleword));

                        Drawable npd = cachedDrawable(
                                R.drawable.a_14_mf_theme_14_background_key_letters_selector);
                        int[] drawableState = key.getCurrentDrawableState();
                        if (key.codes[0] != 0) {
                            npd.setState(drawableState);
                        }
                        npd.setAlpha(opacity);
                        npd.setBounds(key.x, key.y, key.x + key.width, key.y
                                + key.height);
                        npd.draw(canvas);


                        //////////////////////////###############

                        printcharactor(key,canvas,paintforsize,paintforsize2fortwolabelkey,keyLabel);


                    }else //if(isWordnumbers(key.codes[0]))
                    {

                        Drawable npd = cachedDrawable(
                                R.drawable.a_14_mf_theme_14_background_key_selector);
                        int[] drawableState = key.getCurrentDrawableState();
                        if (key.codes[0] != 0) {
                            npd.setState(drawableState);
                        }
                        npd.setAlpha(opacity);
                        npd.setBounds(key.x, key.y, key.x + key.width, key.y
                                + key.height);
                        npd.draw(canvas);


                        drawaarab(canvas,key,keyLabel,paintforsizenumbersfixed);

                        canvas.drawText(key.label.toString(), key.x + (key.width / 2),
                                key.y + 7 * (key.height / 10), paintforsizenumbersfixed);

                    }


                    //////////////////




                }/*else {
                    Drawable npd = cachedDrawable(
                            R.drawable.a_14_mf_theme_14_background_key_selector);
                    int[] drawableState = key.getCurrentDrawableState();
                    if (key.codes[0] != 0) {
                        npd.setState(drawableState);
                    }
                    npd.setAlpha(opacity);
                    npd.setBounds(key.x, key.y, key.x + key.width, key.y
                            + key.height);
                    npd.draw(canvas);


                }*/
            }

        }else if(listS.equals("15")){
            List<Key> keys = getKeyboard().getKeys();
            for(Key key: keys) {
                if(key.icon != null) {
                    drawenterkey(canvas, key);
                }
                if(key.label != null) {
/////////////////////////////////
                    String keyLabel = key.label.toString();
                    if (getKeyboard().isShifted()) {
                        keyLabel = keyLabel.toUpperCase();
                    }
                    if(returnvalue(key)){

                        ////////#######################
                        paintforsize.setColor(
                                getResources().getColor(R.color.a_15_mf_theme_15_color_letters));
                        paintforsize2fortwolabelkey.setColor(
                                getResources().getColor(R.color.a_15_mf_theme_15_color_letters_littleword));

                        Drawable npd = cachedDrawable(
                                R.drawable.a_15_mf_theme_15_background_key_letters_selector);
                        int[] drawableState = key.getCurrentDrawableState();
                        if (key.codes[0] != 0) {
                            npd.setState(drawableState);
                        }
                        npd.setAlpha(opacity);
                        npd.setBounds(key.x, key.y, key.x + key.width, key.y
                                + key.height);
                        npd.draw(canvas);


                        //////////////////////////###############

                        printcharactor(key,canvas,paintforsize,paintforsize2fortwolabelkey,keyLabel);


                    }else //if(isWordnumbers(key.codes[0]))
                    {

                        Drawable npd = cachedDrawable(
                                R.drawable.a_15_mf_theme_15_background_key_selector);
                        int[] drawableState = key.getCurrentDrawableState();
                        if (key.codes[0] != 0) {
                            npd.setState(drawableState);
                        }
                        npd.setAlpha(opacity);
                        npd.setBounds(key.x, key.y, key.x + key.width, key.y
                                + key.height);
                        npd.draw(canvas);


                        drawaarab(canvas,key,keyLabel,paintforsizenumbersfixed);

                        canvas.drawText(key.label.toString(), key.x + (key.width / 2),
                                key.y + 7 * (key.height / 10), paintforsizenumbersfixed);

                    }


                    //////////////////




                }/*else {
                    Drawable npd = cachedDrawable(
                            R.drawable.a_15_mf_theme_15_background_key_selector);
                    int[] drawableState = key.getCurrentDrawableState();
                    if (key.codes[0] != 0) {
                        npd.setState(drawableState);
                    }
                    npd.setAlpha(opacity);
                    npd.setBounds(key.x, key.y, key.x + key.width, key.y
                            + key.height);
                    npd.draw(canvas);


                }*/
            }

        }else if(listS.equals("16")){
            List<Key> keys = getKeyboard().getKeys();
            for(Key key: keys) {
                if(key.icon != null) {
                    drawenterkey(canvas, key);
                }
                if(key.label != null) {
/////////////////////////////////
                    String keyLabel = key.label.toString();
                    if (getKeyboard().isShifted()) {
                        keyLabel = keyLabel.toUpperCase();
                    }
                    if(returnvalue(key)){

                        ////////#######################
                        paintforsize.setColor(
                                getResources().getColor(R.color.a_16_mf_theme_16_color_letters));
                        paintforsize2fortwolabelkey.setColor(
                                getResources().getColor(R.color.a_16_mf_theme_16_color_letters_littleword));

                        Drawable npd = cachedDrawable(
                                R.drawable.a_16_mf_theme_16_background_key_letters_selector);
                        int[] drawableState = key.getCurrentDrawableState();
                        if (key.codes[0] != 0) {
                            npd.setState(drawableState);
                        }
                        npd.setAlpha(opacity);
                        npd.setBounds(key.x, key.y, key.x + key.width, key.y
                                + key.height);
                        npd.draw(canvas);


                        //////////////////////////###############

                        printcharactor(key,canvas,paintforsize,paintforsize2fortwolabelkey,keyLabel);


                    }else //if(isWordnumbers(key.codes[0]))
                    {

                        Drawable npd = cachedDrawable(
                                R.drawable.a_16_mf_theme_16_background_key_selector);
                        int[] drawableState = key.getCurrentDrawableState();
                        if (key.codes[0] != 0) {
                            npd.setState(drawableState);
                        }
                        npd.setAlpha(opacity);
                        npd.setBounds(key.x, key.y, key.x + key.width, key.y
                                + key.height);
                        npd.draw(canvas);


                        drawaarab(canvas,key,keyLabel,paintforsizenumbersfixed);

                        canvas.drawText(key.label.toString(), key.x + (key.width / 2),
                                key.y + 7 * (key.height / 10), paintforsizenumbersfixed);

                    }


                    //////////////////




                }/*else {
                    Drawable npd = cachedDrawable(
                            R.drawable.a_16_mf_theme_16_background_key_selector);
                    int[] drawableState = key.getCurrentDrawableState();
                    if (key.codes[0] != 0) {
                        npd.setState(drawableState);
                    }
                    npd.setAlpha(opacity);
                    npd.setBounds(key.x, key.y, key.x + key.width, key.y
                            + key.height);
                    npd.draw(canvas);


                }*/
            }

        }else if(listS.equals("17")){
            List<Key> keys = getKeyboard().getKeys();
            for(Key key: keys) {
                if(key.icon != null) {
                    drawenterkey(canvas, key);
                }
                if(key.label != null) {
/////////////////////////////////
                    String keyLabel = key.label.toString();
                    if (getKeyboard().isShifted()) {
                        keyLabel = keyLabel.toUpperCase();
                    }
                    if(returnvalue(key)){

                        ////////#######################
                        paintforsize.setColor(
                                getResources().getColor(R.color.a_17_mf_theme_17_color_letters));
                        paintforsize2fortwolabelkey.setColor(
                                getResources().getColor(R.color.a_17_mf_theme_17_color_letters_littleword));

                        Drawable npd = cachedDrawable(
                                R.drawable.a_17_mf_theme_17_background_key_letters_selector);
                        int[] drawableState = key.getCurrentDrawableState();
                        if (key.codes[0] != 0) {
                            npd.setState(drawableState);
                        }
                        npd.setAlpha(opacity);
                        npd.setBounds(key.x, key.y, key.x + key.width, key.y
                                + key.height);
                        npd.draw(canvas);


                        //////////////////////////###############

                        printcharactor(key,canvas,paintforsize,paintforsize2fortwolabelkey,keyLabel);


                    }else //if(isWordnumbers(key.codes[0]))
                    {

                        Drawable npd = cachedDrawable(
                                R.drawable.a_17_mf_theme_17_background_key_selector);
                        int[] drawableState = key.getCurrentDrawableState();
                        if (key.codes[0] != 0) {
                            npd.setState(drawableState);
                        }
                        npd.setAlpha(opacity);
                        npd.setBounds(key.x, key.y, key.x + key.width, key.y
                                + key.height);
                        npd.draw(canvas);


                        drawaarab(canvas,key,keyLabel,paintforsizenumbersfixed);

                        canvas.drawText(key.label.toString(), key.x + (key.width / 2),
                                key.y + 7 * (key.height / 10), paintforsizenumbersfixed);

                    }


                    //////////////////




                }/*else {
                    Drawable npd = cachedDrawable(
                            R.drawable.a_17_mf_theme_17_background_key_selector);
                    int[] drawableState = key.getCurrentDrawableState();
                    if (key.codes[0] != 0) {
                        npd.setState(drawableState);
                    }
                    npd.setAlpha(opacity);
                    npd.setBounds(key.x, key.y, key.x + key.width, key.y
                            + key.height);
                    npd.draw(canvas);


                }*/
            }

        }else if(listS.equals("18")){
            List<Key> keys = getKeyboard().getKeys();
            for(Key key: keys) {
                if(key.icon != null) {
                    drawenterkey(canvas, key);
                }
                if(key.label != null) {
/////////////////////////////////
                    String keyLabel = key.label.toString();
                    if (getKeyboard().isShifted()) {
                        keyLabel = keyLabel.toUpperCase();
                    }
                    if(returnvalue(key)){

                        ////////#######################
                        paintforsize.setColor(
                                getResources().getColor(R.color.a_18_mf_theme_18_color_letters));
                        paintforsize2fortwolabelkey.setColor(
                                getResources().getColor(R.color.a_18_mf_theme_18_color_letters_littleword));

                        Drawable npd = cachedDrawable(
                                R.drawable.a_18_mf_theme_18_background_key_letters_selector);
                        int[] drawableState = key.getCurrentDrawableState();
                        if (key.codes[0] != 0) {
                            npd.setState(drawableState);
                        }
                        npd.setAlpha(opacity);
                        npd.setBounds(key.x, key.y, key.x + key.width, key.y
                                + key.height);
                        npd.draw(canvas);


                        //////////////////////////###############

                        printcharactor(key,canvas,paintforsize,paintforsize2fortwolabelkey,keyLabel);


                    }else //if(isWordnumbers(key.codes[0]))
                    {

                        Drawable npd = cachedDrawable(
                                R.drawable.a_18_mf_theme_18_background_key_selector);
                        int[] drawableState = key.getCurrentDrawableState();
                        if (key.codes[0] != 0) {
                            npd.setState(drawableState);
                        }
                        npd.setAlpha(opacity);
                        npd.setBounds(key.x, key.y, key.x + key.width, key.y
                                + key.height);
                        npd.draw(canvas);


                        drawaarab(canvas,key,keyLabel,paintforsizenumbersfixed);

                        canvas.drawText(key.label.toString(), key.x + (key.width / 2),
                                key.y + 7 * (key.height / 10), paintforsizenumbersfixed);

                    }


                    //////////////////




                }/*else {
                    Drawable npd = cachedDrawable(
                            R.drawable.a_18_mf_theme_18_background_key_selector);
                    int[] drawableState = key.getCurrentDrawableState();
                    if (key.codes[0] != 0) {
                        npd.setState(drawableState);
                    }
                    npd.setAlpha(opacity);
                    npd.setBounds(key.x, key.y, key.x + key.width, key.y
                            + key.height);
                    npd.draw(canvas);


                }*/
            }

        }else if(listS.equals("19")){
            List<Key> keys = getKeyboard().getKeys();
            for(Key key: keys) {
                if(key.icon != null) {
                    drawenterkey(canvas, key);
                }
                if(key.label != null) {
/////////////////////////////////
                    String keyLabel = key.label.toString();
                    if (getKeyboard().isShifted()) {
                        keyLabel = keyLabel.toUpperCase();
                    }
                    if(returnvalue(key)){

                        ////////#######################
                        paintforsize.setColor(
                                getResources().getColor(R.color.a_19_mf_theme_19_color_letters));
                        paintforsize2fortwolabelkey.setColor(
                                getResources().getColor(R.color.a_19_mf_theme_19_color_letters_littleword));

                        Drawable npd = cachedDrawable(
                                R.drawable.a_19_mf_theme_19_background_key_letters_selector);
                        int[] drawableState = key.getCurrentDrawableState();
                        if (key.codes[0] != 0) {
                            npd.setState(drawableState);
                        }
                        npd.setAlpha(opacity);
                        npd.setBounds(key.x, key.y, key.x + key.width, key.y
                                + key.height);
                        npd.draw(canvas);


                        //////////////////////////###############

                        printcharactor(key,canvas,paintforsize,paintforsize2fortwolabelkey,keyLabel);


                    }else //if(isWordnumbers(key.codes[0]))
                    {

                        Drawable npd = cachedDrawable(
                                R.drawable.a_19_mf_theme_19_background_key_selector);
                        int[] drawableState = key.getCurrentDrawableState();
                        if (key.codes[0] != 0) {
                            npd.setState(drawableState);
                        }
                        npd.setAlpha(opacity);
                        npd.setBounds(key.x, key.y, key.x + key.width, key.y
                                + key.height);
                        npd.draw(canvas);


                        drawaarab(canvas,key,keyLabel,paintforsizenumbersfixed);

                        canvas.drawText(key.label.toString(), key.x + (key.width / 2),
                                key.y + 7 * (key.height / 10), paintforsizenumbersfixed);

                    }


                    //////////////////




                }/*else {
                    Drawable npd = cachedDrawable(
                            R.drawable.a_19_mf_theme_19_background_key_selector);
                    int[] drawableState = key.getCurrentDrawableState();
                    if (key.codes[0] != 0) {
                        npd.setState(drawableState);
                    }
                    npd.setAlpha(opacity);
                    npd.setBounds(key.x, key.y, key.x + key.width, key.y
                            + key.height);
                    npd.draw(canvas);


                }*/
            }

        }else if(listS.equals("20")){
            List<Key> keys = getKeyboard().getKeys();
            for(Key key: keys) {
                if(key.icon != null) {
                    drawenterkey(canvas, key);
                }
                if(key.label != null) {
/////////////////////////////////
                    String keyLabel = key.label.toString();
                    if (getKeyboard().isShifted()) {
                        keyLabel = keyLabel.toUpperCase();
                    }
                    if(returnvalue(key)){

                        ////////#######################
                        paintforsize.setColor(
                                getResources().getColor(R.color.a_20_mf_theme_20_color_letters));
                        paintforsize2fortwolabelkey.setColor(
                                getResources().getColor(R.color.a_20_mf_theme_20_color_letters_littleword));

                        Drawable npd = cachedDrawable(
                                R.drawable.a_20_mf_theme_20_background_key_letters_selector);
                        int[] drawableState = key.getCurrentDrawableState();
                        if (key.codes[0] != 0) {
                            npd.setState(drawableState);
                        }
                        npd.setAlpha(opacity);
                        npd.setBounds(key.x, key.y, key.x + key.width, key.y
                                + key.height);
                        npd.draw(canvas);


                        //////////////////////////###############

                        printcharactor(key,canvas,paintforsize,paintforsize2fortwolabelkey,keyLabel);


                    }else //if(isWordnumbers(key.codes[0]))
                    {

                        Drawable npd = cachedDrawable(
                                R.drawable.a_20_mf_theme_20_background_key_selector);
                        int[] drawableState = key.getCurrentDrawableState();
                        if (key.codes[0] != 0) {
                            npd.setState(drawableState);
                        }
                        npd.setAlpha(opacity);
                        npd.setBounds(key.x, key.y, key.x + key.width, key.y
                                + key.height);
                        npd.draw(canvas);


                        drawaarab(canvas,key,keyLabel,paintforsizenumbersfixed);

                        canvas.drawText(key.label.toString(), key.x + (key.width / 2),
                                key.y + 7 * (key.height / 10), paintforsizenumbersfixed);

                    }


                    //////////////////




                }/*else {
                    Drawable npd = cachedDrawable(
                            R.drawable.a_20_mf_theme_20_background_key_selector);
                    int[] drawableState = key.getCurrentDrawableState();
                    if (key.codes[0] != 0) {
                        npd.setState(drawableState);
                    }
                    npd.setAlpha(opacity);
                    npd.setBounds(key.x, key.y, key.x + key.width, key.y
                            + key.height);
                    npd.draw(canvas);


                }*/
            }

        }else if(listS.equals("21")){
            List<Key> keys = getKeyboard().getKeys();
            for(Key key: keys) {
                if(key.icon != null) {
                    drawenterkey(canvas, key);
                }
                if(key.label != null) {
/////////////////////////////////
                    String keyLabel = key.label.toString();
                    if (getKeyboard().isShifted()) {
                        keyLabel = keyLabel.toUpperCase();
                    }
                    if(returnvalue(key)){

                        ////////#######################
                        paintforsize.setColor(
                                getResources().getColor(R.color.a_21_mf_theme_21_color_letters));
                        paintforsize2fortwolabelkey.setColor(
                                getResources().getColor(R.color.a_21_mf_theme_21_color_letters_littleword));

                        Drawable npd = cachedDrawable(
                                R.drawable.a_21_mf_theme_21_background_key_letters_selector);
                        int[] drawableState = key.getCurrentDrawableState();
                        if (key.codes[0] != 0) {
                            npd.setState(drawableState);
                        }
                        npd.setAlpha(opacity);
                        npd.setBounds(key.x, key.y, key.x + key.width, key.y
                                + key.height);
                        npd.draw(canvas);


                        //////////////////////////###############

                        printcharactor(key,canvas,paintforsize,paintforsize2fortwolabelkey,keyLabel);


                    }else //if(isWordnumbers(key.codes[0]))
                    {

                        Drawable npd = cachedDrawable(
                                R.drawable.a_21_mf_theme_21_background_key_selector);
                        int[] drawableState = key.getCurrentDrawableState();
                        if (key.codes[0] != 0) {
                            npd.setState(drawableState);
                        }
                        npd.setAlpha(opacity);
                        npd.setBounds(key.x, key.y, key.x + key.width, key.y
                                + key.height);
                        npd.draw(canvas);


                        drawaarab(canvas,key,keyLabel,paintforsizenumbersfixed);

                        canvas.drawText(key.label.toString(), key.x + (key.width / 2),
                                key.y + 7 * (key.height / 10), paintforsizenumbersfixed);

                    }


                    //////////////////




                }/*else {
                    Drawable npd = cachedDrawable(
                            R.drawable.a_21_mf_theme_21_background_key_selector);
                    int[] drawableState = key.getCurrentDrawableState();
                    if (key.codes[0] != 0) {
                        npd.setState(drawableState);
                    }
                    npd.setAlpha(opacity);
                    npd.setBounds(key.x, key.y, key.x + key.width, key.y
                            + key.height);
                    npd.draw(canvas);


                }*/
            }

        }else if(listS.equals("22")){
            List<Key> keys = getKeyboard().getKeys();
            for(Key key: keys) {
                if(key.icon != null) {
                    drawenterkey(canvas, key);
                }
                if(key.label != null) {
/////////////////////////////////
                    String keyLabel = key.label.toString();
                    if (getKeyboard().isShifted()) {
                        keyLabel = keyLabel.toUpperCase();
                    }
                    if(returnvalue(key)){

                        ////////#######################
                        paintforsize.setColor(
                                getResources().getColor(R.color.a_22_mf_theme_22_color_letters));
                        paintforsize2fortwolabelkey.setColor(
                                getResources().getColor(R.color.a_22_mf_theme_22_color_letters_littleword));

                        Drawable npd = cachedDrawable(
                                R.drawable.a_22_mf_theme_22_background_key_letters_selector);
                        int[] drawableState = key.getCurrentDrawableState();
                        if (key.codes[0] != 0) {
                            npd.setState(drawableState);
                        }
                        npd.setAlpha(opacity);
                        npd.setBounds(key.x, key.y, key.x + key.width, key.y
                                + key.height);
                        npd.draw(canvas);


                        //////////////////////////###############

                        printcharactor(key,canvas,paintforsize,paintforsize2fortwolabelkey,keyLabel);


                    }else //if(isWordnumbers(key.codes[0]))
                    {

                        Drawable npd = cachedDrawable(
                                R.drawable.a_22_mf_theme_22_background_key_selector);
                        int[] drawableState = key.getCurrentDrawableState();
                        if (key.codes[0] != 0) {
                            npd.setState(drawableState);
                        }
                        npd.setAlpha(opacity);
                        npd.setBounds(key.x, key.y, key.x + key.width, key.y
                                + key.height);
                        npd.draw(canvas);


                        drawaarab(canvas,key,keyLabel,paintforsizenumbersfixed);

                        canvas.drawText(key.label.toString(), key.x + (key.width / 2),
                                key.y + 7 * (key.height / 10), paintforsizenumbersfixed);

                    }


                    //////////////////




                }/*else {
                    Drawable npd = cachedDrawable(
                            R.drawable.a_22_mf_theme_22_background_key_selector);
                    int[] drawableState = key.getCurrentDrawableState();
                    if (key.codes[0] != 0) {
                        npd.setState(drawableState);
                    }
                    npd.setAlpha(opacity);
                    npd.setBounds(key.x, key.y, key.x + key.width, key.y
                            + key.height);
                    npd.draw(canvas);


                }*/
            }

        }else if(listS.equals("23")){
            List<Key> keys = getKeyboard().getKeys();
            for(Key key: keys) {
                if(key.icon != null) {
                    drawenterkey(canvas, key);
                }
                if(key.label != null) {
/////////////////////////////////
                    String keyLabel = key.label.toString();
                    if (getKeyboard().isShifted()) {
                        keyLabel = keyLabel.toUpperCase();
                    }
                    if(returnvalue(key)){

                        ////////#######################
                        paintforsize.setColor(
                                getResources().getColor(R.color.a_23_mf_theme_23_color_letters));
                        paintforsize2fortwolabelkey.setColor(
                                getResources().getColor(R.color.a_23_mf_theme_23_color_letters_littleword));

                        Drawable npd = cachedDrawable(
                                R.drawable.a_23_mf_theme_23_background_key_letters_selector);
                        int[] drawableState = key.getCurrentDrawableState();
                        if (key.codes[0] != 0) {
                            npd.setState(drawableState);
                        }
                        npd.setAlpha(opacity);
                        npd.setBounds(key.x, key.y, key.x + key.width, key.y
                                + key.height);
                        npd.draw(canvas);


                        //////////////////////////###############

                        printcharactor(key,canvas,paintforsize,paintforsize2fortwolabelkey,keyLabel);


                    }else //if(isWordnumbers(key.codes[0]))
                    {

                        Drawable npd = cachedDrawable(
                                R.drawable.a_23_mf_theme_23_background_key_selector);
                        int[] drawableState = key.getCurrentDrawableState();
                        if (key.codes[0] != 0) {
                            npd.setState(drawableState);
                        }
                        npd.setAlpha(opacity);
                        npd.setBounds(key.x, key.y, key.x + key.width, key.y
                                + key.height);
                        npd.draw(canvas);


                        drawaarab(canvas,key,keyLabel,paintforsizenumbersfixed);

                        canvas.drawText(key.label.toString(), key.x + (key.width / 2),
                                key.y + 7 * (key.height / 10), paintforsizenumbersfixed);

                    }


                    //////////////////




                }/*else {
                    Drawable npd = cachedDrawable(
                            R.drawable.a_23_mf_theme_23_background_key_selector);
                    int[] drawableState = key.getCurrentDrawableState();
                    if (key.codes[0] != 0) {
                        npd.setState(drawableState);
                    }
                    npd.setAlpha(opacity);
                    npd.setBounds(key.x, key.y, key.x + key.width, key.y
                            + key.height);
                    npd.draw(canvas);


                }*/
            }

        }else if(listS.equals("24")){
            List<Key> keys = getKeyboard().getKeys();
            for(Key key: keys) {
                if(key.icon != null) {
                    drawenterkey(canvas, key);
                }
                if(key.label != null) {
/////////////////////////////////
                    String keyLabel = key.label.toString();
                    if (getKeyboard().isShifted()) {
                        keyLabel = keyLabel.toUpperCase();
                    }
                    if(returnvalue(key)){

                        ////////#######################
                        paintforsize.setColor(
                                getResources().getColor(R.color.a_24_mf_theme_24_color_letters));
                        paintforsize2fortwolabelkey.setColor(
                                getResources().getColor(R.color.a_24_mf_theme_24_color_letters_littleword));

                        Drawable npd = cachedDrawable(
                                R.drawable.a_24_mf_theme_24_background_key_letters_selector);
                        int[] drawableState = key.getCurrentDrawableState();
                        if (key.codes[0] != 0) {
                            npd.setState(drawableState);
                        }
                        npd.setAlpha(opacity);
                        npd.setBounds(key.x, key.y, key.x + key.width, key.y
                                + key.height);
                        npd.draw(canvas);


                        //////////////////////////###############

                        printcharactor(key,canvas,paintforsize,paintforsize2fortwolabelkey,keyLabel);


                    }else //if(isWordnumbers(key.codes[0]))
                    {

                        Drawable npd = cachedDrawable(
                                R.drawable.a_24_mf_theme_24_background_key_selector);
                        int[] drawableState = key.getCurrentDrawableState();
                        if (key.codes[0] != 0) {
                            npd.setState(drawableState);
                        }
                        npd.setAlpha(opacity);
                        npd.setBounds(key.x, key.y, key.x + key.width, key.y
                                + key.height);
                        npd.draw(canvas);


                        drawaarab(canvas,key,keyLabel,paintforsizenumbersfixed);

                        canvas.drawText(key.label.toString(), key.x + (key.width / 2),
                                key.y + 7 * (key.height / 10), paintforsizenumbersfixed);

                    }


                    //////////////////




                }/*else {
                    Drawable npd = cachedDrawable(
                            R.drawable.a_24_mf_theme_24_background_key_selector);
                    int[] drawableState = key.getCurrentDrawableState();
                    if (key.codes[0] != 0) {
                        npd.setState(drawableState);
                    }
                    npd.setAlpha(opacity);
                    npd.setBounds(key.x, key.y, key.x + key.width, key.y
                            + key.height);
                    npd.draw(canvas);


                }*/
            }

        }else if(listS.equals("25")){
            List<Key> keys = getKeyboard().getKeys();
            for(Key key: keys) {
                if(key.icon != null) {
                    drawenterkey(canvas, key);
                }
                if(key.label != null) {
/////////////////////////////////
                    String keyLabel = key.label.toString();
                    if (getKeyboard().isShifted()) {
                        keyLabel = keyLabel.toUpperCase();
                    }
                    if(returnvalue(key)){

                        ////////#######################
                        paintforsize.setColor(
                                getResources().getColor(R.color.a_25_mf_theme_25_color_letters));
                        paintforsize2fortwolabelkey.setColor(
                                getResources().getColor(R.color.a_25_mf_theme_25_color_letters_littleword));

                        Drawable npd = cachedDrawable(
                                R.drawable.a_25_mf_theme_25_background_key_letters_selector);
                        int[] drawableState = key.getCurrentDrawableState();
                        if (key.codes[0] != 0) {
                            npd.setState(drawableState);
                        }
                        npd.setAlpha(opacity);
                        npd.setBounds(key.x, key.y, key.x + key.width, key.y
                                + key.height);
                        npd.draw(canvas);


                        //////////////////////////###############

                        printcharactor(key,canvas,paintforsize,paintforsize2fortwolabelkey,keyLabel);


                    }else //if(isWordnumbers(key.codes[0]))
                    {

                        Drawable npd = cachedDrawable(
                                R.drawable.a_25_mf_theme_25_background_key_selector);
                        int[] drawableState = key.getCurrentDrawableState();
                        if (key.codes[0] != 0) {
                            npd.setState(drawableState);
                        }
                        npd.setAlpha(opacity);
                        npd.setBounds(key.x, key.y, key.x + key.width, key.y
                                + key.height);
                        npd.draw(canvas);


                        drawaarab(canvas,key,keyLabel,paintforsizenumbersfixed);

                        canvas.drawText(key.label.toString(), key.x + (key.width / 2),
                                key.y + 7 * (key.height / 10), paintforsizenumbersfixed);

                    }


                    //////////////////




                }/*else {
                    Drawable npd = cachedDrawable(
                            R.drawable.a_25_mf_theme_25_background_key_selector);
                    int[] drawableState = key.getCurrentDrawableState();
                    if (key.codes[0] != 0) {
                        npd.setState(drawableState);
                    }
                    npd.setAlpha(opacity);
                    npd.setBounds(key.x, key.y, key.x + key.width, key.y
                            + key.height);
                    npd.draw(canvas);


                }*/
            }

        }else if(listS.equals("26")){
            List<Key> keys = getKeyboard().getKeys();
            for(Key key: keys) {
                if(key.icon != null) {
                    drawenterkey(canvas, key);
                }
                if(key.label != null) {
/////////////////////////////////
                    String keyLabel = key.label.toString();
                    if (getKeyboard().isShifted()) {
                        keyLabel = keyLabel.toUpperCase();
                    }
                    if(returnvalue(key)){

                        ////////#######################
                        paintforsize.setColor(
                                getResources().getColor(R.color.a_26_mf_theme_26_color_letters));
                        paintforsize2fortwolabelkey.setColor(
                                getResources().getColor(R.color.a_26_mf_theme_26_color_letters_littleword));

                        Drawable npd = cachedDrawable(
                                R.drawable.a_26_mf_theme_26_background_key_letters_selector);
                        int[] drawableState = key.getCurrentDrawableState();
                        if (key.codes[0] != 0) {
                            npd.setState(drawableState);
                        }
                        npd.setAlpha(opacity);
                        npd.setBounds(key.x, key.y, key.x + key.width, key.y
                                + key.height);
                        npd.draw(canvas);


                        //////////////////////////###############

                        printcharactor(key,canvas,paintforsize,paintforsize2fortwolabelkey,keyLabel);


                    }else //if(isWordnumbers(key.codes[0]))
                    {

                        Drawable npd = cachedDrawable(
                                R.drawable.a_26_mf_theme_26_background_key_selector);
                        int[] drawableState = key.getCurrentDrawableState();
                        if (key.codes[0] != 0) {
                            npd.setState(drawableState);
                        }
                        npd.setAlpha(opacity);
                        npd.setBounds(key.x, key.y, key.x + key.width, key.y
                                + key.height);
                        npd.draw(canvas);


                        drawaarab(canvas,key,keyLabel,paintforsizenumbersfixed);

                        canvas.drawText(key.label.toString(), key.x + (key.width / 2),
                                key.y + 7 * (key.height / 10), paintforsizenumbersfixed);

                    }


                    //////////////////




                }/*else {
                    Drawable npd = cachedDrawable(
                            R.drawable.a_26_mf_theme_26_background_key_selector);
                    int[] drawableState = key.getCurrentDrawableState();
                    if (key.codes[0] != 0) {
                        npd.setState(drawableState);
                    }
                    npd.setAlpha(opacity);
                    npd.setBounds(key.x, key.y, key.x + key.width, key.y
                            + key.height);
                    npd.draw(canvas);


                }*/
            }

        }else if(listS.equals("27")){
            List<Key> keys = getKeyboard().getKeys();
            for(Key key: keys) {
                if(key.icon != null) {
                    drawenterkey(canvas, key);
                }
                if(key.label != null) {
/////////////////////////////////
                    String keyLabel = key.label.toString();
                    if (getKeyboard().isShifted()) {
                        keyLabel = keyLabel.toUpperCase();
                    }
                    if(returnvalue(key)){

                        ////////#######################
                        paintforsize.setColor(
                                getResources().getColor(R.color.a_27_mf_theme_27_color_letters));
                        paintforsize2fortwolabelkey.setColor(
                                getResources().getColor(R.color.a_27_mf_theme_27_color_letters_littleword));

                        Drawable npd = cachedDrawable(
                                R.drawable.a_27_mf_theme_27_background_key_letters_selector);
                        int[] drawableState = key.getCurrentDrawableState();
                        if (key.codes[0] != 0) {
                            npd.setState(drawableState);
                        }
                        npd.setAlpha(opacity);
                        npd.setBounds(key.x, key.y, key.x + key.width, key.y
                                + key.height);
                        npd.draw(canvas);


                        //////////////////////////###############

                        printcharactor(key,canvas,paintforsize,paintforsize2fortwolabelkey,keyLabel);


                    }else //if(isWordnumbers(key.codes[0]))
                    {

                        Drawable npd = cachedDrawable(
                                R.drawable.a_27_mf_theme_27_background_key_selector);
                        int[] drawableState = key.getCurrentDrawableState();
                        if (key.codes[0] != 0) {
                            npd.setState(drawableState);
                        }
                        npd.setAlpha(opacity);
                        npd.setBounds(key.x, key.y, key.x + key.width, key.y
                                + key.height);
                        npd.draw(canvas);


                        drawaarab(canvas,key,keyLabel,paintforsizenumbersfixed);

                        canvas.drawText(key.label.toString(), key.x + (key.width / 2),
                                key.y + 7 * (key.height / 10), paintforsizenumbersfixed);

                    }


                    //////////////////




                }/*else {
                    Drawable npd = cachedDrawable(
                            R.drawable.a_27_mf_theme_27_background_key_selector);
                    int[] drawableState = key.getCurrentDrawableState();
                    if (key.codes[0] != 0) {
                        npd.setState(drawableState);
                    }
                    npd.setAlpha(opacity);
                    npd.setBounds(key.x, key.y, key.x + key.width, key.y
                            + key.height);
                    npd.draw(canvas);


                }*/
            }

        }else if(listS.equals("28")){
            List<Key> keys = getKeyboard().getKeys();
            for(Key key: keys) {
                if(key.icon != null) {
                    drawenterkey(canvas, key);
                }
                if(key.label != null) {
/////////////////////////////////
                    String keyLabel = key.label.toString();
                    if (getKeyboard().isShifted()) {
                        keyLabel = keyLabel.toUpperCase();
                    }
                    if(returnvalue(key)){

                        ////////#######################
                        paintforsize.setColor(
                                getResources().getColor(R.color.a_28_mf_theme_28_color_letters));
                        paintforsize2fortwolabelkey.setColor(
                                getResources().getColor(R.color.a_28_mf_theme_28_color_letters_littleword));

                        Drawable npd = cachedDrawable(
                                R.drawable.a_28_mf_theme_28_background_key_letters_selector);
                        int[] drawableState = key.getCurrentDrawableState();
                        if (key.codes[0] != 0) {
                            npd.setState(drawableState);
                        }
                        npd.setAlpha(opacity);
                        npd.setBounds(key.x, key.y, key.x + key.width, key.y
                                + key.height);
                        npd.draw(canvas);


                        //////////////////////////###############

                        printcharactor(key,canvas,paintforsize,paintforsize2fortwolabelkey,keyLabel);


                    }else //if(isWordnumbers(key.codes[0]))
                    {

                        Drawable npd = cachedDrawable(
                                R.drawable.a_28_mf_theme_28_background_key_selector);
                        int[] drawableState = key.getCurrentDrawableState();
                        if (key.codes[0] != 0) {
                            npd.setState(drawableState);
                        }
                        npd.setAlpha(opacity);
                        npd.setBounds(key.x, key.y, key.x + key.width, key.y
                                + key.height);
                        npd.setAlpha(100);
                        npd.draw(canvas);


                        drawaarab(canvas, key, keyLabel,paintforsizenumbersfixed);

                        canvas.drawText(key.label.toString(), key.x + (key.width / 2),
                                key.y + 7 * (key.height / 10), paintforsizenumbersfixed);

                    }


                    //////////////////




                }else {
                    Drawable npd = cachedDrawable(
                            R.drawable.a_28_mf_theme_28_background_key_selector);
                    int[] drawableState = key.getCurrentDrawableState();
                    if (key.codes[0] != 0) {
                        npd.setState(drawableState);
                    }
                    npd.setAlpha(opacity);
                    npd.setBounds(key.x, key.y, key.x + key.width, key.y
                            + key.height);
                    npd.draw(canvas);


                }
            }

        }else  if(listS.equals("29")){
            List<Key> keys = getKeyboard().getKeys();
            for(Key key: keys) {
                if(key.icon != null) {
                    drawenterkey(canvas, key);
                }
                if(key.label != null) {
/////////////////////////////////
                    String keyLabel = key.label.toString();
                    if (getKeyboard().isShifted()) {
                        keyLabel = keyLabel.toUpperCase();
                    }
                    if(returnvalue(key)){

                        ////////#######################
                        paintforsize.setColor(
                                getResources().getColor(R.color.a_29_mf_theme_29_color_letters));
                        paintforsize2fortwolabelkey.setColor(
                                getResources().getColor(R.color.a_29_mf_theme_29_color_letters_littleword));

                        Drawable npd = cachedDrawable(
                                R.drawable.a_29_mf_theme_29_background_key_letters_selector);
                        int[] drawableState = key.getCurrentDrawableState();
                        if (key.codes[0] != 0) {
                            npd.setState(drawableState);
                        }

                        npd.setBounds(key.x, key.y, key.x + key.width, key.y
                                + key.height);
                        npd.draw(canvas);


                        //////////////////////////###############

                        printcharactor(key,canvas,paintforsize,paintforsize2fortwolabelkey,keyLabel);


                    }else //if(isWordnumbers(key.codes[0]))
                    {

                        Drawable npd = cachedDrawable(
                                R.drawable.a_29_mf_theme_29_background_key_selector);
                        int[] drawableState = key.getCurrentDrawableState();
                        if (key.codes[0] != 0) {
                            npd.setState(drawableState);
                        }

                        npd.setBounds(key.x, key.y, key.x + key.width, key.y
                                + key.height);
                        npd.draw(canvas);


                        drawaarab(canvas,key,keyLabel,paintforsizenumbersfixed);

                        canvas.drawText(key.label.toString(), key.x + (key.width / 2),
                                key.y + 7 * (key.height / 10), paintforsizenumbersfixed);

                    }


                    //////////////////




                }else {
                    Drawable npd = cachedDrawable(
                            R.drawable.a_29_mf_theme_29_background_key_selector);
                    int[] drawableState = key.getCurrentDrawableState();
                    if (key.codes[0] != 0) {
                        npd.setState(drawableState);
                    }
                    npd.setAlpha(opacity);
                    npd.setBounds(key.x, key.y, key.x + key.width, key.y
                            + key.height);
                    npd.draw(canvas);


                }
            }

        } else if(listS.equals("30")){
            List<Key> keys = getKeyboard().getKeys();
            for(Key key: keys) {
                if(key.icon != null) {
                    drawenterkey(canvas, key);
                }
                if(key.label != null) {
/////////////////////////////////
                    String keyLabel = key.label.toString();
                    if (getKeyboard().isShifted()) {
                        keyLabel = keyLabel.toUpperCase();
                    }
                    if(returnvalue(key)){

                        ////////#######################
                        paintforsize.setColor(
                                getResources().getColor(R.color.a_30_mf_theme_30_color_letters));
                        paintforsize2fortwolabelkey.setColor(
                                getResources().getColor(R.color.a_30_mf_theme_30_color_letters_littleword));

                        Drawable npd = cachedDrawable(
                                R.drawable.a_30_mf_theme_30_background_key_letters_selector);
                        int[] drawableState = key.getCurrentDrawableState();
                        if (key.codes[0] != 0) {
                            npd.setState(drawableState);
                        }
                        npd.setBounds(key.x, key.y, key.x + key.width, key.y
                                + key.height);
                        npd.draw(canvas);


                        //////////////////////////###############

                        printcharactor(key,canvas,paintforsize,paintforsize2fortwolabelkey,keyLabel);


                    }else //if(isWordnumbers(key.codes[0]))
                    {

                        Drawable npd = cachedDrawable(
                                R.drawable.a_30_mf_theme_30_background_key_selector);
                        int[] drawableState = key.getCurrentDrawableState();
                        if (key.codes[0] != 0) {
                            npd.setState(drawableState);
                        }
                        npd.setBounds(key.x, key.y, key.x + key.width, key.y
                                + key.height);
                        npd.draw(canvas);


                        drawaarab(canvas,key,keyLabel,paintforsizenumbersfixed);

                        canvas.drawText(key.label.toString(), key.x + (key.width / 2),
                                key.y + 7 * (key.height / 10), paintforsizenumbersfixed);

                    }


                    //////////////////




                }else {
                    Drawable npd = cachedDrawable(
                            R.drawable.a_30_mf_theme_30_background_key_selector);
                    int[] drawableState = key.getCurrentDrawableState();
                    if (key.codes[0] != 0) {
                        npd.setState(drawableState);
                    }
                    npd.setBounds(key.x, key.y, key.x + key.width, key.y
                            + key.height);
                    npd.draw(canvas);


                }
            }

        }else  if(listS.equals("31")){
            List<Key> keys = getKeyboard().getKeys();
            for(Key key: keys) {
                if(key.icon != null) {
                    drawenterkey(canvas, key);

                }
                if(key.label != null) {
/////////////////////////////////
                    String keyLabel = key.label.toString();
                    if (getKeyboard().isShifted()) {
                        keyLabel = keyLabel.toUpperCase();
                    }
                    if(returnvalue(key)){

                        ////////#######################
                        paintforsize.setColor(
                                getResources().getColor(R.color.a_31_mf_theme_31_color_letters));
                        paintforsize2fortwolabelkey.setColor(
                                getResources().getColor(R.color.a_31_mf_theme_31_color_letters_littleword));

                        Drawable npd = cachedDrawable(
                                R.drawable.a_31_mf_theme_31_background_key_letters_selector);
                        int[] drawableState = key.getCurrentDrawableState();
                        if (key.codes[0] != 0) {
                            npd.setState(drawableState);
                        }
                        npd.setBounds(key.x, key.y, key.x + key.width, key.y
                                + key.height);
                        npd.draw(canvas);


                        //////////////////////////###############

                        printcharactor(key,canvas,paintforsize,paintforsize2fortwolabelkey,keyLabel);


                    }else //if(isWordnumbers(key.codes[0]))
                    {

                        Drawable npd = cachedDrawable(
                                R.drawable.a_31_mf_theme_31_background_key_selector);
                        int[] drawableState = key.getCurrentDrawableState();
                        if (key.codes[0] != 0) {
                            npd.setState(drawableState);
                        }
                        npd.setBounds(key.x, key.y, key.x + key.width, key.y
                                + key.height);
                        npd.draw(canvas);
                        Paint paintforsizenumbersfixed2=new Paint();
                        paintforsizenumbersfixed2=paintforsizenumbersfixed;
                        paintforsizenumbersfixed2.setColor(getResources().getColor(R.color.a_31_mf_theme_31_color_letters));
                        drawaarab(canvas, key, keyLabel,paintforsizenumbersfixed2);

                        canvas.drawText(key.label.toString(), key.x + (key.width / 2),
                                key.y + 7 * (key.height / 10), paintforsizenumbersfixed);

                    }


                    //////////////////




                }else {
                    Drawable npd = cachedDrawable(
                            R.drawable.a_31_mf_theme_31_background_key_selector);
                    int[] drawableState = key.getCurrentDrawableState();
                    if (key.codes[0] != 0) {
                        npd.setState(drawableState);
                    }
                    npd.setBounds(key.x, key.y, key.x + key.width, key.y
                            + key.height);
                    npd.draw(canvas);


                }
            }

        } else if(listS.equals("32")){
            List<Key> keys = getKeyboard().getKeys();
            for(Key key: keys) {
                if(key.icon != null) {
                    drawenterkey(canvas, key);
                }
                if(key.label != null) {
/////////////////////////////////
                    String keyLabel = key.label.toString();
                    if (getKeyboard().isShifted()) {
                        keyLabel = keyLabel.toUpperCase();
                    }
                    if(returnvalue(key)){

                        ////////#######################
                        paintforsize.setColor(
                                getResources().getColor(R.color.a_32_mf_theme_32_color_letters));
                        paintforsize2fortwolabelkey.setColor(
                                getResources().getColor(R.color.a_32_mf_theme_32_color_letters_littleword));

                        Drawable npd = cachedDrawable(
                                R.drawable.a_32_mf_theme_32_background_key_letters_selector);
                        int[] drawableState = key.getCurrentDrawableState();
                        if (key.codes[0] != 0) {
                            npd.setState(drawableState);
                        }
                        npd.setBounds(key.x, key.y, key.x + key.width, key.y
                                + key.height);
                        npd.draw(canvas);


                        //////////////////////////###############

                        printcharactor(key,canvas,paintforsize,paintforsize2fortwolabelkey,keyLabel);


                    }else //if(isWordnumbers(key.codes[0]))
                    {

                        Drawable npd = cachedDrawable(
                                R.drawable.a_32_mf_theme_32_background_key_selector);
                        int[] drawableState = key.getCurrentDrawableState();
                        if (key.codes[0] != 0) {
                            npd.setState(drawableState);
                        }
                        npd.setBounds(key.x, key.y, key.x + key.width, key.y
                                + key.height);
                        npd.draw(canvas);


                        drawaarab(canvas,key,keyLabel,paintforsizenumbersfixed);

                        canvas.drawText(key.label.toString(), key.x + (key.width / 2),
                                key.y + 7 * (key.height / 10), paintforsizenumbersfixed);

                    }


                    //////////////////




                }else {
                    Drawable npd = cachedDrawable(
                            R.drawable.a_32_mf_theme_32_background_key_selector);
                    int[] drawableState = key.getCurrentDrawableState();
                    if (key.codes[0] != 0) {
                        npd.setState(drawableState);
                    }
                    npd.setBounds(key.x, key.y, key.x + key.width, key.y
                            + key.height);
                    npd.draw(canvas);


                }
            }

        }else  if(listS.equals("33")){
            List<Key> keys = getKeyboard().getKeys();
            for(Key key: keys) {
                if(key.icon != null) {
                    drawenterkey(canvas, key);

                }
                if(key.label != null) {
/////////////////////////////////
                    String keyLabel = key.label.toString();
                    if (getKeyboard().isShifted()) {
                        keyLabel = keyLabel.toUpperCase();
                    }
                    if(returnvalue(key)){

                        ////////#######################
                        paintforsize.setColor(
                                getResources().getColor(R.color.a_33_mf_theme_33_color_letters));
                        paintforsize2fortwolabelkey.setColor(
                                getResources().getColor(R.color.a_33_mf_theme_33_color_letters_littleword));

                        Drawable npd = cachedDrawable(
                                R.drawable.a_33_mf_theme_33_background_key_letters_selector);
                        int[] drawableState = key.getCurrentDrawableState();
                        if (key.codes[0] != 0) {
                            npd.setState(drawableState);
                        }
                        npd.setBounds(key.x, key.y, key.x + key.width, key.y
                                + key.height);
                        npd.draw(canvas);


                        //////////////////////////###############

                        printcharactor(key,canvas,paintforsize,paintforsize2fortwolabelkey,keyLabel);


                    }else //if(isWordnumbers(key.codes[0]))
                    {

                        Drawable npd = cachedDrawable(
                                R.drawable.a_33_mf_theme_33_background_key_selector);
                        int[] drawableState = key.getCurrentDrawableState();
                        if (key.codes[0] != 0) {
                            npd.setState(drawableState);
                        }
                        npd.setBounds(key.x, key.y, key.x + key.width, key.y
                                + key.height);
                        npd.draw(canvas);
                        Paint paintforsizenumbersfixed2=new Paint();
                        paintforsizenumbersfixed2=paintforsizenumbersfixed;
                        paintforsizenumbersfixed2.setColor(getResources().getColor(R.color.a_33_mf_theme_33_color_letters));
                        drawaarab(canvas, key, keyLabel,paintforsizenumbersfixed2);

                        canvas.drawText(key.label.toString(), key.x + (key.width / 2),
                                key.y + 7 * (key.height / 10), paintforsizenumbersfixed);

                    }


                    //////////////////




                }else {
                    Drawable npd = cachedDrawable(
                            R.drawable.a_33_mf_theme_33_background_key_selector);
                    int[] drawableState = key.getCurrentDrawableState();
                    if (key.codes[0] != 0) {
                        npd.setState(drawableState);
                    }
                    npd.setBounds(key.x, key.y, key.x + key.width, key.y
                            + key.height);
                    npd.draw(canvas);


                }
            }

        }else  if(listS.equals("34")){
            List<Key> keys = getKeyboard().getKeys();
            for(Key key: keys) {
                if(key.icon != null) {
                    drawenterkey(canvas, key);

                }
                if(key.label != null) {
/////////////////////////////////
                    String keyLabel = key.label.toString();
                    if (getKeyboard().isShifted()) {
                        keyLabel = keyLabel.toUpperCase();
                    }
                    if(returnvalue(key)){

                        ////////#######################
                        paintforsize.setColor(
                                getResources().getColor(R.color.a_34_mf_theme_34_color_letters));
                        paintforsize2fortwolabelkey.setColor(
                                getResources().getColor(R.color.a_34_mf_theme_34_color_letters_littleword));

                        Drawable npd = cachedDrawable(
                                R.drawable.a_34_mf_theme_34_background_key_letters_selector);
                        int[] drawableState = key.getCurrentDrawableState();
                        if (key.codes[0] != 0) {
                            npd.setState(drawableState);
                        }
                        npd.setBounds(key.x, key.y, key.x + key.width, key.y
                                + key.height);
                        npd.draw(canvas);


                        //////////////////////////###############

                        printcharactor(key,canvas,paintforsize,paintforsize2fortwolabelkey,keyLabel);


                    }else //if(isWordnumbers(key.codes[0]))
                    {

                        Drawable npd = cachedDrawable(
                                R.drawable.a_34_mf_theme_34_background_key_selector);
                        int[] drawableState = key.getCurrentDrawableState();
                        if (key.codes[0] != 0) {
                            npd.setState(drawableState);
                        }
                        npd.setBounds(key.x, key.y, key.x + key.width, key.y
                                + key.height);
                        npd.draw(canvas);
                        Paint paintforsizenumbersfixed2=new Paint();
                        paintforsizenumbersfixed2=paintforsizenumbersfixed;
                        paintforsizenumbersfixed2.setColor(getResources().getColor(R.color.a_34_mf_theme_34_color_numersinup));
                        drawaarab(canvas, key, keyLabel,paintforsizenumbersfixed2);

                        canvas.drawText(key.label.toString(), key.x + (key.width / 2),
                                key.y + 7 * (key.height / 10), paintforsizenumbersfixed);

                    }


                    //////////////////




                }else {
                    Drawable npd = cachedDrawable(
                            R.drawable.a_34_mf_theme_34_background_key_selector);
                    int[] drawableState = key.getCurrentDrawableState();
                    if (key.codes[0] != 0) {
                        npd.setState(drawableState);
                    }
                    npd.setBounds(key.x, key.y, key.x + key.width, key.y
                            + key.height);
                    npd.draw(canvas);


                }
            }

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
    private void handleicon(Key key,String listS){
        //if(listS.equals())

    }




    ///////// design background key
    private Context  mContext;
    private void drawtextforKeywith2function(int drawableId, Canvas canvas, Key key) {

        if (key.icon == null) {


            Drawable npd = cachedDrawable(
                    drawableId);
            int[] drawableState = key.getCurrentDrawableState();
            if (key.codes[0] != 0) {
                npd.setState(drawableState);
            }
            npd.setBounds(key.x + key.width / 5, key.y + key.height / 7, key.x + 4 * key.width / 5, key.y
                    + 20 * key.height / 21);


            npd.draw(canvas);
        }
    }
    private void drawKeyBackground(int drawableId, Canvas canvas, Key key) {
        Drawable npd = cachedDrawable(
                drawableId);
        int[] drawableState = key.getCurrentDrawableState();
        if (key.codes[0] != 0) {
            npd.setState(drawableState);
        }
        npd.setBounds(key.x, key.y, key.x + key.width, key.y
                + key.height);
        npd.draw(canvas);
    }




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
    public void drawaarab(Canvas canvas,Key key,String keyLabel,Paint paintforsizenumbersfixed){

        if(keyLabel.equals("\u0660")||keyLabel.equals("\u0030")){

           // canvas.drawText("\u064E", key.x + 5*(key.width /15   ),
           //         key.y + 5*(key.height/8) , paintforsizenumbersfixed);
           Drawable npd = cachedDrawable(
                    R.drawable.ic_menu_paste_holo_light);
            int[] drawableState = key.getCurrentDrawableState();
            if (key.codes[0] != 0) {
                npd.setState(drawableState);
            }
            npd.setBounds(key.x+ key.width/2, key.y+ key.height/2, key.x + 6*key.width/7, key.y
                    + 6*key.height/7);
            npd.draw(canvas);




        }else if(keyLabel.equals("\u0669")||keyLabel.equals("\u0039")){

           // canvas.drawText("\u0650", key.x + 5*(key.width /15   ),
            //        key.y + 5*(key.height/8) , paintforsizenumbersfixed);
            Drawable npd = cachedDrawable(
                    R.drawable.ic_menu_copy_holo_light);
            int[] drawableState = key.getCurrentDrawableState();
            if (key.codes[0] != 0) {
                npd.setState(drawableState);
            }
            npd.setBounds(key.x+ key.width/2, key.y+ key.height/2, key.x + 6*key.width/7, key.y
                    + 6*key.height/7);
            npd.draw(canvas);


        }else if(keyLabel.equals("\u0668")||keyLabel.equals("\u0038")){

           // canvas.drawText("\u064F", key.x + 5*(key.width /15   ),
            //        key.y + 5*(key.height/8) , paintforsizenumbersfixed);
            Drawable npd = cachedDrawable(
                    R.drawable.ic_menu_cut_holo_light);
            int[] drawableState = key.getCurrentDrawableState();
            if (key.codes[0] != 0) {
                npd.setState(drawableState);
            }
            npd.setBounds(key.x+ key.width/2, key.y+ key.height/2, key.x + 6*key.width/7, key.y
                    + 6*key.height/7);
            npd.draw(canvas);


        }else if(keyLabel.equals("\u0667")||keyLabel.equals("\u0037")){

          //  canvas.drawText("\u064B", key.x + 5*(key.width /15   ),
           //         key.y + 5*(key.height/8) , paintforsizenumbersfixed);
            Drawable npd = cachedDrawable(
                    R.drawable.ic_menu_selectall_holo_light);
            int[] drawableState = key.getCurrentDrawableState();
            if (key.codes[0] != 0) {
                npd.setState(drawableState);
            }
            npd.setBounds(key.x+ key.width/2, key.y+ key.height/2, key.x + 6*key.width/7, key.y
                    + 6*key.height/7);
            npd.draw(canvas);


        }/*else if(keyLabel.equals("\u0666")){

            canvas.drawText("\u064D", key.x + 5*(key.width /15   ),
                    key.y + 5*(key.height/8) , paintforsizenumbersfixed);


        }*/else if(keyLabel.equals("\u0665")){

            canvas.drawText("\u064E", key.x + 5 * (key.width / 15),
                    key.y + 5 * (key.height / 8), paintforsizenumbersfixed);


        }else if(keyLabel.equals("\u0664")){

            canvas.drawText("\u0650", key.x + 5*(key.width /15   ),
                    key.y + 5*(key.height/8) , paintforsizenumbersfixed);


        }else if(keyLabel.equals("\u0663")){

            canvas.drawText("\u064F", key.x + 5*(key.width /15   ),
                    key.y + 5*(key.height/8) , paintforsizenumbersfixed);


        }else if(keyLabel.equals("\u0662")){

            canvas.drawText("\u0651", key.x + 5*(key.width /15   ),
                    key.y + 5*(key.height/8) , paintforsizenumbersfixed);


        }else if(keyLabel.equals("\u0661")){

            /*
            if(!kurdikeyboard.language.equals("kr")){
            canvas.drawText("\u0672", key.x + 5*(key.width /15   ),
                    key.y + 6*(key.height/8) , paintforsizenumbersfixed);
            }else {
                canvas.drawText("\u030A", key.x + 5*(key.width /15   ),
                        key.y + 6*(key.height/8) , paintforsizenumbersfixed);

            }
            */
            canvas.drawText("\u0672", key.x + 5*(key.width /15   ),
                    key.y + 6*(key.height/8) , paintforsizenumbersfixed);



        }
    }
    public void drawenterkey(Canvas canvas,Key key){
        int isportrait= (int) getResources().getDimension(R.dimen.isportraitdevice);

        if(isportrait!=0&&!kurdikeyboard.isnumbersqwrty) {
       // if(!kurdikeyboard.isnumbersqwrty) {

            if (key.codes[0] == 10&&!kurdikeyboard.getCurrentlanguage().equals("en")) {

                Drawable npd = cachedDrawable(
                        R.drawable.aarabpng);
                int[] drawableState = key.getCurrentDrawableState();
                if (key.codes[0] != 0) {
                    npd.setState(drawableState);
                }
                npd.setBounds(key.x + key.width / 6, key.y + key.width / 6, key.x + key.width / 2, key.y
                        + 2 * key.width / 5);
                npd.draw(canvas);
            }
            /*else if (key.codes[0] == -158) {

                Drawable npd = cachedDrawable(
                        R.drawable.dotpng);
                int[] drawableState = key.getCurrentDrawableState();
                if (key.codes[0] != 0) {
                    npd.setState(drawableState);
                }
                npd.setBounds(key.x + key.width / 6, key.y + key.width / 6, key.x + 6 * key.width / 10, key.y
                        + key.width / 2);
                npd.draw(canvas);

            } */else if (key.codes[0] == -2) {

                Drawable npd = cachedDrawable(
                        R.drawable.numberpng);
                int[] drawableState = key.getCurrentDrawableState();
                if (key.codes[0] != 0) {
                    npd.setState(drawableState);
                }
                npd.setBounds(key.x + key.width / 6, key.y + key.width / 6, key.x + 6 * key.width / 10, key.y
                        + 4 * key.width / 9);
                npd.draw(canvas);
            } else if (key.codes[0] == -3) {

                Drawable npd = cachedDrawable(
                        R.drawable.emojipng);
                int[] drawableState = key.getCurrentDrawableState();
                if (key.codes[0] != 0) {
                    npd.setState(drawableState);
                }
                npd.setBounds(key.x + key.width / 6, key.y + key.width / 6, key.x + 6 * key.width / 10, key.y
                        + 4 * key.width / 9);
                npd.draw(canvas);
            } else if (key.codes[0] == 8204) {

                Drawable npd = cachedDrawable(
                        R.drawable.keshidehpng);
                int[] drawableState = key.getCurrentDrawableState();
                if (key.codes[0] != 0) {
                    npd.setState(drawableState);
                }
                npd.setBounds(key.x + key.width / 6, key.y + key.width / 6, key.x + 6 * key.width / 10, key.y
                        + 4 * key.width / 9);
                npd.draw(canvas);
            }
        }
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
}
