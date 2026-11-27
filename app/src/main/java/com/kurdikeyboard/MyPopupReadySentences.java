
/**
 * Created by Stat on 3/6/2017.
 */

package com.kurdikeyboard;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.ContactsContract;
import androidx.viewpager.widget.ViewPager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import android.widget.BaseAdapter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

/**
 * Created by Stat on 2/28/2017.
 */



public class MyPopupReadySentences extends PopupWindow {
    // removed on dismiss: it used to stay registered forever (one leak per opening)
    private android.view.ViewTreeObserver.OnGlobalLayoutListener mLayoutListener;
    private android.view.View mLayoutListenerOwner;

    private static final float one_DIP = 1.0f;

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

    ///////////////////////
//    private InputMethodManager mInputMethodManager;



    public MyPopupReadySentences(View rootView, Context mContext
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
        mLayoutListener = new android.view.ViewTreeObserver.OnGlobalLayoutListener() {
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
        };
		if (mLayoutListenerOwner != null) mLayoutListenerOwner.getViewTreeObserver().removeOnGlobalLayoutListener(mLayoutListener);
		mLayoutListenerOwner = rootView;
		rootView.getViewTreeObserver().addOnGlobalLayoutListener(mLayoutListener);
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
    public void  CreatAllDirecories(String path2){
        File directory = AppStorage.root(MyPopupReadySentences.this.mContext);
        File folder = new File( directory + path2 );

        if (!folder.exists()) {
            // Toast.makeText(this, "!!!folder.exists222", Toast.LENGTH_LONG).show();

            try {
                folder.mkdir();
            } catch (Exception e) {
                Toast.makeText(mContext,
                        e.getMessage()
                        ,Toast.LENGTH_SHORT).show();


            }
        }
    }
    private View createCustomView() {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.layout_ready_sentences, null, false);


        final ListView lview = (ListView) view.findViewById(R.id.listreadysentences);

        final List<Contact> contacts = new ArrayList<Contact>();
        // List<ContactModel> listcontacts=getContacts(MyPopupReadySentences.this.mContext);
        final List<String> mallclipboardStrings=new ArrayList<String>();
        final List<String> mallclipboarddate=new ArrayList<String>();
       // final List<String> mallclipboardID=new ArrayList<String>();
        CreatAllDirecories("/data");
        CreatAllDirecories("/data/data");
        CreatAllDirecories("/data/data/com.kurdikeyboard");
        CreatAllDirecories("/data/data/com.kurdikeyboard/"+ Constants.ReadySentencesFolderPath);

        File directory = AppStorage.root(MyPopupReadySentences.this.mContext);
        File folder = new File( directory + "/data/data/com.kurdikeyboard/" + Constants.ReadySentencesFolderPath );

        if (!folder.exists()) {
            Toast.makeText(MyPopupReadySentences.this.mContext, "nothings exists", Toast.LENGTH_LONG).show();

            try {
                folder.mkdir();
            } catch (Exception e) {
                Toast.makeText(MyPopupReadySentences.this.mContext,
                        e.getMessage()
                        ,Toast.LENGTH_SHORT).show();


            }
        }
        File[] files = AppStorage.sortedFiles(folder);

        mallclipboardStrings.clear();
        mallclipboarddate.clear();
       // mallclipboardID.clear();
        for (File file : files) {
            if(file.getName().endsWith(".txp")) {
                try {
                    //  File tempfile=file;
                    //  toastM("123");
                    FileReader fr = new FileReader(file);
                    BufferedReader br = new BufferedReader(fr);
                    StringBuilder sb = new StringBuilder();


                    String sCurrentLine = br.readLine();
                    sb.append(sCurrentLine).append("\n");


                    while ((sCurrentLine = br.readLine()) != null) {
                        //sCurrentLine+=  String.valueOf(br.readLine());
                        sb.append(sCurrentLine).append("\n");
                        //toastM(br.readLine());
                    }
                    fr.close();
                    br.close();
                    mallclipboardStrings.add(AppStorage.trimTrailingNewline(sb));
                     mallclipboarddate.add(file.getName());

                } catch (IOException ioe) {

                }
            }
        }
        for (int i = (mallclipboardStrings.size()-1); i >-1 ; i--) {

            //  System.out.println("Index: " + i + " - Item: " + list.get(i));
            String nametemp,phonetemp,idtemp;
            nametemp=mallclipboardStrings.get(i).toString();
            //listcontacts.get(i).name.toString();
            phonetemp=mallclipboarddate.get(i).toString();
            //listcontacts.get(i).mobileNumber.toString();
           // idtemp=mallclipboardID.get(i).toString();
            contacts.add(new Contact(phonetemp,nametemp));


        }

      /*  contacts.add(new Contact("Gloria Miller","86-(216)605-9056"));
        contacts.add(new Contact("Teresa Garrett","86-(131)666-7051"));
        contacts.add(new Contact("Jeremy Diaz","370-(899)196-6943"));
        contacts.add(new Contact("Rose Henry","420-(240)605-2163"));
        contacts.add(new Contact("Maria Kelly","62-(488)190-3222"));
        */
        final ContactsAdapter adapter = new ContactsAdapter(MyPopupReadySentences.this.mContext,
                R.layout.listview_textview_textview_readysentences, contacts);
        lview.setAdapter(adapter);

      //  final ImageView imageViewIV= (ImageView) view.findViewById(R.id.delete_ready_sentences_all);

        lview.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent,final  View view,int position, long id) {

                Contact contacttemp=(Contact)
                        parent.getItemAtPosition(position);
                onText(contacttemp.getPhone().toString());

            }

        });
        //
        lview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Contact contacttemp=(Contact)
                        parent.getItemAtPosition(position);

                contacts.remove(position);

                adapter.notifyDataSetChanged();
                //  (contacts.get(position).getPhone());

                // int indextemp=mallclipboarddate.size()-position-1;
                // Toast.makeText(MyPopupReadySentences.this.mContext,
                //        mallclipboardID.get(indextemp).toString()
                //       ,Toast.LENGTH_SHORT).show();
                //Toast.makeText(MyPopupReadySentences.this.mContext,
                //      mallclipboardStrings.get(indextemp).toString()
                //    ,Toast.LENGTH_SHORT).show();
             //   for(String sidtemp :mallclipboarddate){
                ///    if(sidtemp.equals(contacttemp.getPhone())){
               //         removefiles(sidtemp);

               //     }
               // }
                removefiles(
                        contacttemp.getName()//.substring(0, contacttemp.getName().length()-4)
                );
              //  Toast.makeText(MyPopupReadySentences.this.mContext,
                 //       contacttemp.getName().substring(0, contacttemp.getName().length()-4)

                   //     ,Toast.LENGTH_SHORT).show();
                return true;
            }
        });


        // get last selected page
        // last page was recents, check if there are recents to use
        // if none was found, go to page 1
        final  ImageView imageViewdeletAll= (ImageView) view.findViewById(R.id.delete_ready_sentences_all);
        imageViewdeletAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  removeallfiles();
              //  contacts.clear();
               // adapter.notifyDataSetChanged();
                alertdialogshow(contacts,adapter);

            }
        });
        final  ImageView imageViewCreateSentences= (ImageView) view.findViewById(R.id.create_ready_sentences);
        imageViewCreateSentences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // alertdialogshow();
                Intent i1 = new Intent(mContext,Main22Activity.class);
                i1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(i1);
                
            }
        });
        final  ImageView imageViewexit= (ImageView) view.findViewById(R.id.exitpopupreadys);
        imageViewexit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // alertdialogshow();
              MyPopupReadySentences.this.dismiss();
            }
        });




        return view;
    }
    public void alertdialogshow(final List<Contact> contacts,
                                final ContactsAdapter adapter ){
        try {
            LayoutInflater inflater2 = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
          //  final View dialogView = inflater2.inflate(R.layout.layout_input_ready_sentences, null);
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext);


            //dialogBuilder.setView(dialogView);
            dialogBuilder.setMessage(R.string.areyousure);
            dialogBuilder.setTitle(R.string.warrning);
            dialogBuilder.setPositiveButton(R.string.yes,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                              removeallfiles();
                            contacts.clear();
                             adapter.notifyDataSetChanged();
                        }
                    }
            );
            dialogBuilder.setNegativeButton(R.string.no,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }
            );

            AlertDialog alertDialog = dialogBuilder.create();
            // A dialog shown from a keyboard must be attached to the keyboard window
            // (TYPE_SYSTEM_ALERT needs an extra permission and crashes on Android 8+).
            WindowManager.LayoutParams dlp = alertDialog.getWindow().getAttributes();
            dlp.token = rootView.getWindowToken();
            dlp.type = WindowManager.LayoutParams.TYPE_APPLICATION_ATTACHED_DIALOG;
            alertDialog.getWindow().setAttributes(dlp);
            alertDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);


            alertDialog.show();
        } catch (Exception e) {
        Toast.makeText(MyPopupReadySentences.this.mContext,
                e.getMessage()
                ,Toast.LENGTH_SHORT).show();


    }


    }
    public void removeallfiles() {
        File directory = AppStorage.root(MyPopupReadySentences.this.mContext);
        File folder = new File( directory + "/data/data/com.kurdikeyboard/" + Constants.ReadySentencesFolderPath );
        if (!folder.exists()) {
            Toast.makeText(MyPopupReadySentences.this.mContext, "nothings exists", Toast.LENGTH_LONG).show();

            try {
                folder.mkdir();
            } catch (Exception e) {
                Toast.makeText(MyPopupReadySentences.this.mContext,
                        e.getMessage()
                        ,Toast.LENGTH_SHORT).show();


            }
        }
        File[] files = AppStorage.sortedFiles(folder);
        for (File file : files) {
            if(file.getName().endsWith(".txp")) {
                try {
                    file.delete();

                    // Toast.makeText(MyPopupReadySentences.this.mContext,
                    //        "کل حافظه خالی شد",Toast.LENGTH_SHORT).show();


                } catch (Exception ioe) {
                    Toast.makeText(MyPopupReadySentences.this.mContext,
                            ioe.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        }

    }
    public void removefiles(String namefile) {
        File directory = AppStorage.root(MyPopupReadySentences.this.mContext);
        File folder = new File(directory + "/data/data/com.kurdikeyboard/" + Constants.ReadySentencesFolderPath);
        Toast.makeText(MyPopupReadySentences.this.mContext,
            "removing"+   namefile
                ,Toast.LENGTH_LONG).show();
        if (!folder.exists()) {
            Toast.makeText(MyPopupReadySentences.this.mContext, "nothings exists", Toast.LENGTH_LONG).show();

            try {
                folder.mkdir();
            } catch (Exception e) {
                Toast.makeText(MyPopupReadySentences.this.mContext,
                        e.getMessage()
                        , Toast.LENGTH_SHORT).show();


            }
        }
        File[] files = AppStorage.sortedFiles(folder);

        for (File file : files) {
            if (file.getName().endsWith(".txp")) {
                if (file.getName().equals(namefile)) {
                    try {
                        file.delete();

                    } catch (Exception ioe) {
                        Toast.makeText(MyPopupReadySentences.this.mContext,
                                ioe.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }


        }
    }
    public static class RepeatListener implements View.OnTouchListener {

        private Handler handler = new Handler(android.os.Looper.getMainLooper());

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
        if (mLayoutListener != null && mLayoutListenerOwner != null) {
            mLayoutListenerOwner.getViewTreeObserver().removeOnGlobalLayoutListener(mLayoutListener);
            mLayoutListener = null;
            mLayoutListenerOwner = null;
        }
        super.dismiss();

    }


    public class ContactsAdapter extends ArrayAdapter<Contact> {

        int resource;
        String response;
        Context context;
        List<Contact> allcontacts;
        public ContactsAdapter(Context context, int resource, List<Contact> items) {
            super(context, resource, items);
            this.resource=resource;
            this.allcontacts=items;
        }
        @Override
        public Contact getItem(int position) {
            // TODO Auto-generated method stub
            return this.allcontacts.get(position);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent)
        {
            final LinearLayout contactsView;
            final Contact contact = getItem(position);
            if(convertView==null) {
                contactsView = new LinearLayout(getContext());
                String inflater = Context.LAYOUT_INFLATER_SERVICE;
                LayoutInflater vi;
                vi = (LayoutInflater)getContext().getSystemService(inflater);
                vi.inflate(resource, contactsView, true);
            } else {
                contactsView = (LinearLayout) convertView;
            }
            TextView contactName =(TextView)contactsView.findViewById(R.id.clipboarddate2);
            contactName.setTypeface(null, Typeface.BOLD);
            ////#################################################


            SharedPreferences preferenceKeyboard=
                    this.getContext()
                            .getSharedPreferences("MySharedPreference", Context.MODE_PRIVATE);
            int fontsize=preferenceKeyboard.getInt(mContext.getString(R.string.prefseekbarfontreadysentences),72);
            fontsize /= 4;
            int convertedtospfontsize;
            //final float scale = MyPopupReadySentences.this.mContext.getResources().getDisplayMetrics().density;
           // float size_dpi=fontsize*one_DIP;
           // convertedtospfontsize=(int) (size_dpi * scale + 0.5f);
            convertedtospfontsize= fontsize;

            /////###############################
           // contactName.setTextSize(TypedValue.COMPLEX_UNIT_PX,70);
            contactName.setTextSize(TypedValue.COMPLEX_UNIT_SP,convertedtospfontsize);

            final TextView contactPhone = (TextView)contactsView.findViewById(R.id.clipboardstring2);
           // contactPhone.setTextSize(TypedValue.COMPLEX_UNIT_PX,60);
            contactPhone.setTextSize(TypedValue.COMPLEX_UNIT_SP,convertedtospfontsize);
            contactName.setText(contact.getName().
                    substring(0, contact.getName().length()-4));
            contactPhone.setText(contact.getPhone());

            final ImageView imageViewdelete= (ImageView) contactsView.findViewById(
                    R.id.clipboarddelete2);
            imageViewdelete.setTag(position);
            // final String tempfordelet=contact.getName().toString();
            // final ViewGroup parentfinal=parent;
            ImageView btnImgsend= (ImageView)
                    contactsView.findViewById(R.id.imageviewsendreadysentences);
            btnImgsend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    onText(contactPhone.getText());

                }
            });
            imageViewdelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // final int position = listview1.getPositionForView((View) v.getParent());
                    Integer index = (Integer) v.getTag();
                    // Contact contacttemp=(Contact)
                    //        parentfinal.
                //    Toast.makeText(MyPopupReadySentences.this.mContext,
                  //          "i: "+index
                   //                 +"   "+contact.getName()
                   //                 +"   "+contact.getPhone(),Toast.LENGTH_SHORT).show();
                   /* for(String sidtemp :mallclipboarddate){
                        if(sidtemp.equals(contact.getPhone())){
                            removefiles(sidtemp);

                        }
                    }*/
                    remove(contact);
                    removefiles(contact.getName()//.substring(0, contact.getName().length()-4)
                             );
                   //
                    Toast.makeText(mContext,
                           "حذف شد"
                                ,Toast.LENGTH_LONG).show();
                    notifyDataSetChanged();

                }
            });



            return contactsView;
        }
    }

    public class ContactModel {
        public String id;
        public String name;
        public String mobileNumber;
        public Bitmap photo;
        public Uri photoURI;
    }
    public void onText(CharSequence text) {

        // Toast.makeText(getBaseContext(),"ontext",Toast.LENGTH_LONG).show();

        InputConnection ic = kk.getCurrentInputConnection();
        if (ic == null) {
            //Toast.makeText(getBaseContext(),"null text",Toast.LENGTH_SHORT).show();
            return;
        }
        ic.beginBatchEdit();

        ic.commitText(text, 1); // cursor after the text (was 0: before it)
        ic.endBatchEdit();


    }


}


