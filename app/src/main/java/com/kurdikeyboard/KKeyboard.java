package com.kurdikeyboard;

/**
 * Created by Stat on 11/8/2016.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.Keyboard;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.util.List;


public class KKeyboard extends Keyboard  {

    private Key mEnterKey;
    private Key mSpaceKey;
    private Key mModeChangeKey;
    private Key mLanguageSwitchKey;
    private Key mSavedModeChangeKey;
    //private Key mhalfspace;
    //private Key mdelete;
    //private Key mclose;
    //private Key mnumbers;

    //private Key mdot;

    private Key mshift;
    /** editor action of the current field (EditorInfo action | flags), for themes drawing the Enter key */
    public int enterAction = 0;
    private Context mcontext;

    private Key mMulti_change;
    static int mMulti_change_value=46;


    private Key mSavedLanguageSwitchKey;

    public KKeyboard(Context context, int xmlLayoutResId) {
        super(context, xmlLayoutResId);
        mcontext=context;
    }


    public KKeyboard(Context context, int layoutTemplateResId,
                         CharSequence characters, int columns, int horizontalPadding) {
        super(context, layoutTemplateResId, characters, columns, horizontalPadding);
        mcontext=context;
    }

    public KKeyboard(Context context, int xmlLayoutResId,
                     int modeId, int width, int height){
        super(context,xmlLayoutResId,
                modeId, width, height);
        mcontext=context;

    }


    @Override
    protected Key createKeyFromXml(Resources res, Row parent, int x, int y,
                                   XmlResourceParser parser) {
        Key key = new KurdishKey(res, parent, x, y, parser);
        if (key.codes[0] == 10) {
            mEnterKey = key;
        } else if (key.codes[0] == 32) {
            mSpaceKey = key;
        } else if (key.codes[0] == Keyboard.KEYCODE_MODE_CHANGE) {
            mModeChangeKey = key;
            mSavedModeChangeKey = new KurdishKey(res, parent, x, y, parser);
        } else if (key.codes[0] == KKeyboardView.KEYCODE_LANGUAGE_SWITCH) {
            mLanguageSwitchKey = key;
            mSavedLanguageSwitchKey = new KurdishKey(res, parent, x, y, parser);
        } else if (key.codes[0] == Keyboard.KEYCODE_SHIFT) {
            mshift = key; // without this the shift / caps-lock icon never changed
        }
        /*else if(key.codes[0]==8204){
            mhalfspace=key;
        }else if(key.codes[0]==-5){
            mdelete=key;
        }else if(key.codes[0]==-1){
            mshift=key;
        }else if(key.codes[0]==-3){
            mclose=key;
        }else if(key.codes[0]==-2){
            mnumbers=key;
        }else if(key.codes[0]==46){
            mdot=key;
        }
        */
        else if(key.codes[0]==-158){
            mMulti_change=key;
        }
        return key;
    }
    void changeMultiKey(Resources res,int primarykey){
        if(mMulti_change==null){
            return;
        }
        switch (primarykey){

            case 33:// !
                mMulti_change_value=33;
                mMulti_change.label=null;
                mMulti_change.icon=res.getDrawable(R.drawable.dot_black_en_33);
                break;
            case 34:// "
                mMulti_change_value=34;
                mMulti_change.label=null;
                mMulti_change.icon=res.getDrawable(R.drawable.dot_black_en_34);
                break;
            case 39:// '
                mMulti_change_value=39;
                mMulti_change.label=null;
                mMulti_change.icon=res.getDrawable(R.drawable.dot_black_en_39);
                break;
            case 40:// (
                mMulti_change_value=40;
                mMulti_change.label=null;
                mMulti_change.icon=res.getDrawable(R.drawable.dot_black_en_40);
                break;
            case 41:// )
                mMulti_change_value=41;
                mMulti_change.label=null;
                mMulti_change.icon=res.getDrawable(R.drawable.dot_black_en_41);
                break;
            case 43:// +
                mMulti_change_value=43;
                mMulti_change.label=null;
                mMulti_change.icon=res.getDrawable(R.drawable.dot_black_en_43);
                break;
            case 44:// ,
                mMulti_change_value=44;
                mMulti_change.label=null;
                mMulti_change.icon=res.getDrawable(R.drawable.dot_black_en_44);
                break;
            case 45:// -
                mMulti_change_value=45;
                mMulti_change.label=null;
                mMulti_change.icon=res.getDrawable(R.drawable.dot_black_en_45);
                break;
            case 46:// .
                mMulti_change_value=46;
                mMulti_change.label=null;
                mMulti_change.icon=res.getDrawable(R.drawable.dot_black_en);
                break;
            case 47:// /
                mMulti_change_value=47;
                mMulti_change.label=null;
                mMulti_change.icon=res.getDrawable(R.drawable.dot_black_en_47);
                break;
            case 60:// <
                mMulti_change_value=60;
                mMulti_change.label=null;
                mMulti_change.icon=res.getDrawable(R.drawable.dot_black_en_60);
                break;
            case 62:// >
                mMulti_change_value=62;
                mMulti_change.label=null;
                mMulti_change.icon=res.getDrawable(R.drawable.dot_black_en_62);
                break;

            case 63:// ?
                mMulti_change_value=63;
                mMulti_change.label=null;
                mMulti_change.icon=res.getDrawable(R.drawable.dot_black_en_63);
                break;
            case 64:// @
                mMulti_change_value=64;
                mMulti_change.label=null;
                mMulti_change.icon=res.getDrawable(R.drawable.dot_black_en_64);
                break;

            case 92:// \
                mMulti_change_value=92;
                mMulti_change.label=null;
                mMulti_change.icon=res.getDrawable(R.drawable.dot_black_en_92);
                break;
            case 124:// |
                mMulti_change_value=124;
                mMulti_change.label=null;
                mMulti_change.icon=res.getDrawable(R.drawable.dot_black_en_124);
                break;
            case 1548:// persian , ==> ،
                mMulti_change_value=1548;
                mMulti_change.label=null;
                mMulti_change.icon=res.getDrawable(R.drawable.dot_black_en_1548);
                break;
            case 1567:// persian ? ==> ؟
                mMulti_change_value=1567;
                mMulti_change.label=null;
                mMulti_change.icon=res.getDrawable(R.drawable.dot_black_en_1567);
                break;
            case -112:// copy
                mMulti_change_value=-112;
                mMulti_change.label=null;
                mMulti_change.icon=res.getDrawable(R.drawable.copy_black);
                break;
            case -116:// paste
                mMulti_change_value=-116;
                mMulti_change.label=null;
                mMulti_change.icon=res.getDrawable(R.drawable.paste_black);
                break;
            case -150:// clipboard
                mMulti_change_value=-150;
                mMulti_change.label=null;
                mMulti_change.icon=res.getDrawable(R.drawable.clipboard_black);
                break;
            case -152:// ready sentences
                mMulti_change_value=-152;
                mMulti_change.label=null;
                mMulti_change.icon=res.getDrawable(R.drawable.ready_sentences_black);
                break;
            case -157:// emoji
                mMulti_change_value=-157;
                mMulti_change.label=null;
                mMulti_change.icon=res.getDrawable(R.drawable.emoji_black);
                break;

            default:
                mMulti_change_value=46;
                mMulti_change.label=null;
                mMulti_change.icon=res.getDrawable(R.drawable.dot_black_en);

                break;
        }
    }
   /* @Override
    public int[] getNearestKeys(int x, int y) {
        List<Key> keys = getKeys();
        Key[] mKeys = keys.toArray(new Key[keys.size()]);
        int i = 0;
        for (Key key : mKeys) {
            if(key.isInside(x, y))
                return new int[]{i};
            i++;
        }
        return new int[0];
    }*/



    static class KurdishKey extends Keyboard.Key {

        public KurdishKey(Resources res, Keyboard.Row parent, int x, int y,
                        XmlResourceParser parser) {
            super(res, parent, x, y, parser);
        }

        /**
         * Overriding this method so that we can reduce the target area for the key that
         * closes the keyboard.
         */
        @Override
        public boolean isInside(int x, int y) {
            return super.isInside(x, codes[0] == KEYCODE_CANCEL ? y - 10 : y);
        }
    }

    void setLanguageSwitchKeyVisibility(boolean visible) {
        if (visible) {
            // The language switch key should be visible. Restore the size of the mode change key
            // and language switch key using the saved layout.
            mModeChangeKey.width = mSavedModeChangeKey.width;
            mModeChangeKey.x = mSavedModeChangeKey.x;
            mLanguageSwitchKey.width = mSavedLanguageSwitchKey.width;
            mLanguageSwitchKey.icon = mSavedLanguageSwitchKey.icon;
            mLanguageSwitchKey.iconPreview = mSavedLanguageSwitchKey.iconPreview;
        } else {
            // The language switch key should be hidden. Change the width of the mode change key
            // to fill the space of the language key so that the user will not see any strange gap.
            mModeChangeKey.width = mSavedModeChangeKey.width + mSavedLanguageSwitchKey.width;
            mLanguageSwitchKey.width = 0;
            mLanguageSwitchKey.icon = null;
            mLanguageSwitchKey.iconPreview = null;
        }
    }

    void setImeOptions(Resources res, EditorInfo ei)
    {
        if (mEnterKey == null)
        {
            return;
        }
     //   SharedPreferences preferenceKeyboard=mcontext.
         //       getSharedPreferences("MySharedPreference", Context.MODE_PRIVATE);
     //   String listS=
           //     preferenceKeyboard.getString(mcontext.getString(R.string.pref_list_theme_selector),"1");



        enterAction = ei.imeOptions & (EditorInfo.IME_MASK_ACTION | EditorInfo.IME_FLAG_NO_ENTER_ACTION);
        switch (ei.imeOptions & (EditorInfo.IME_MASK_ACTION | EditorInfo.IME_FLAG_NO_ENTER_ACTION))
        {

            case EditorInfo.IME_ACTION_SEND:
               // mEnterKey.iconPreview = null;
               // mEnterKey.icon = null;
               // mEnterKey.label ="Send";
              //  if(listS.equals("5")||listS.equals("6")){
                //    mEnterKey.icon = res.getDrawable(R.drawable.enter_send_white);

               // }else {
                    mEnterKey.icon = res.getDrawable(R.drawable.enter_send_black);
               // }


                mEnterKey.label = null;

                break;
            case EditorInfo.IME_ACTION_GO:
              //  mEnterKey.iconPreview = null;
              //  mEnterKey.icon = null;
              //  mEnterKey.label ="Go";
              //  if(listS.equals("5")||listS.equals("6")){
                //    mEnterKey.icon = res.getDrawable(R.drawable.enter_go_white);

               // }else {
                    mEnterKey.icon = res.getDrawable(R.drawable.enter_go_black);
               // }


                mEnterKey.label = null;
                break;
            case EditorInfo.IME_ACTION_NEXT:
               // mEnterKey.iconPreview = null;
               // mEnterKey.icon = null;
               // mEnterKey.label = "Next";
               // if(listS.equals("5")||listS.equals("6")){
                 //   mEnterKey.icon = res.getDrawable(R.drawable.enter_next_white);

              //  }else {
                    mEnterKey.icon = res.getDrawable(R.drawable.enter_next_black);
               // }


                mEnterKey.label = null;
                break;
            case EditorInfo.IME_ACTION_SEARCH:
               // mEnterKey.icon = res.getDrawable(R.drawable.a_1_theme_1_background_key_icon_search);
               // mEnterKey.label = null;
              //  if(listS.equals("5")||listS.equals("6")){
              //      mEnterKey.icon = res.getDrawable(R.drawable.enter_search_white);

              //  }else {
                    mEnterKey.icon = res.getDrawable(R.drawable.enter_search_black);
               // }


                mEnterKey.label = null;

                break;
            default:
              //  mEnterKey.iconPreview = null;
              //  mEnterKey.label = "Enter";
               // mEnterKey.icon = null;
              //  if(listS.equals("5")||listS.equals("6")){
               //     mEnterKey.icon = res.getDrawable(R.drawable.enter_enter_white);

              //  }else {
                    mEnterKey.icon = res.getDrawable(R.drawable.enter_enter_black);
              //  }


                mEnterKey.label = null;

                break;
        }

        if (ei.actionLabel != null)
        {
            mEnterKey.iconPreview = null;
            mEnterKey.icon = null;
            mEnterKey.label = ei.actionLabel;
        }
      /*  if(mhalfspace!=null){
       //          if(listS.equals("5")||listS.equals("6")){

        //         mhalfspace.icon=res.getDrawable(R.drawable.halfspace_white);
       // }else {
                     mhalfspace.icon=res.getDrawable(R.drawable.half_space_black);
              //   }
        }
        if(mdelete!=null){
          //  if(listS.equals("5")||listS.equals("6")){

           //     mdelete.icon=res.getDrawable(R.drawable.delete_white);
           // }else {
                mdelete.icon=res.getDrawable(R.drawable.delete_black);
           // }
        }
        if(mclose!=null){
          //  if(listS.equals("5")||listS.equals("6")){

           //     mclose.icon=res.getDrawable(R.drawable.close_keyboard_white);
           // }else {
                mclose.icon=res.getDrawable(R.drawable.close_keyboard_black);
            //}
        }
        if(mnumbers!=null){
            if(listS.equals("5")||listS.equals("6")){

                mnumbers.icon=res.getDrawable(R.drawable.numbers_white);
            }else {
                mnumbers.icon=res.getDrawable(R.drawable.numbers_black);
            }
            mnumbers.label=null;
        }
        if(mdot!=null){
            if(listS.equals("5")||listS.equals("6")){

                mdot.icon=res.getDrawable(R.drawable.dot_white);
            }else {
                mdot.icon=res.getDrawable(R.drawable.dot_black);
            }
            mdot.label=null;
        }*/


    }

    void setSpaceIcon(final Drawable icon) {
        if (mSpaceKey != null) {
            mSpaceKey.icon = icon;
        }
    }
    void setShiftIcon(final Drawable icon) {
        if (mshift != null) {
            mshift.icon = icon;
        }
    }




}


