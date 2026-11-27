
/**
 * Created by Stat on 3/6/2017.
 */

package com.kurdikeyboard;

        import android.app.Activity;
        import android.app.AlertDialog;
        import android.content.ContentResolver;
        import android.content.ContentUris;
        import android.content.Context;
        import android.content.Intent;
        import android.content.SharedPreferences;
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
        import android.support.v4.view.ViewPager;
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



public class MyPopupClipboard extends PopupWindow {
    private static final float one_DIP = 1.0f;

    private kurdikeyboard kk;
    View rootView;
    Context mContext;
    private int keyBoardHeight = 0;
    private Boolean pendingOpen = false;
    private Boolean isOpened = false;

    OnSoftKeyboardOpenCloseListener onSoftKeyboardOpenCloseListenerpopupsetting;




    public MyPopupClipboard(View rootView, Context mContext
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
        final View view = inflater.inflate(R.layout.layout_clipboard, null, false);


        final ListView lview = (ListView) view.findViewById(R.id.listclipboard);

        final List<Contact> contacts = new ArrayList<Contact>();
       // List<ContactModel> listcontacts=getContacts(MyPopupClipboard.this.mContext);
        final List<String> mallclipboardStrings=new ArrayList<String>();
        final List<String> mallclipboarddate=new ArrayList<String>();
        final List<String> mallclipboardID=new ArrayList<String>();

        File directory = AppStorage.root(MyPopupClipboard.this.mContext);
            File folder = new File( directory + "/data/data/com.kurdikeyboard" + Constants.ClipboardCacheFolderPath );

            if (!folder.exists()) {
                Toast.makeText(MyPopupClipboard.this.mContext, "nothings exists", Toast.LENGTH_LONG).show();

                try {
                    folder.mkdir();
                } catch (Exception e) {
                    Toast.makeText(MyPopupClipboard.this.mContext,
                            e.getMessage()
                            ,Toast.LENGTH_SHORT).show();


                }
            }
        File[] files = AppStorage.sortedFiles(folder);

        mallclipboardStrings.clear();
        mallclipboarddate.clear();
        mallclipboardID.clear();
        for (File file : files) {
            if(file.getName().endsWith(".txp")) {
                try {
                    //  File tempfile=file;
                    //  toastM("123");
                    FileReader fr = new FileReader(file);
                    BufferedReader br = new BufferedReader(fr);
                    StringBuilder sb = new StringBuilder();

                    String sCurrentLine = br.readLine();
                    String miladidate=sCurrentLine;
                    sCurrentLine = br.readLine();
                    String shamsidate=sCurrentLine;

                    while ((sCurrentLine = br.readLine()) != null) {
                        //sCurrentLine+=  String.valueOf(br.readLine());
                        sb.append(sCurrentLine).append("\n");
                        //toastM(br.readLine());
                    }
                    fr.close();
                    br.close();
                    mallclipboardStrings.add(AppStorage.trimTrailingNewline(sb));
                   // String nametemp1=
                    //        shamsidate;
                    // file.getName();
                   // nametemp1 = nametemp1.substring(0, nametemp1.length()-4);
                    mallclipboarddate.add(shamsidate);
                    mallclipboardID.add(miladidate);
                } catch (IOException ioe) {

                }
            }
        }
        for (int i = (mallclipboardStrings.size()-1); i >-1 ; i--) {

            //  System.out.println("Index: " + i + " - Item: " + list.get(i));
            String nametemp,phonetemp,idtemp;

            try {
                //nametemp=mallclipboardStrings.get(i).toString();
                nametemp = String.valueOf(mallclipboardStrings.get(i));
                //  mallclipboardStrings.get(i).toString();

                //listcontacts.get(i).name.toString();
                phonetemp = String.valueOf(mallclipboarddate.get(i));
                //listcontacts.get(i).mobileNumber.toString();
                idtemp = String.valueOf(mallclipboardID.get(i));
                 }catch (ClassCastException ei){
                Toast.makeText(mContext.getApplicationContext()
                ,"Error 144: "+ei.getMessage(),Toast.LENGTH_SHORT).show();
                continue;
            }
            contacts.add(new Contact(nametemp, phonetemp));



        }

      /*  contacts.add(new Contact("Gloria Miller","86-(216)605-9056"));
        contacts.add(new Contact("Teresa Garrett","86-(131)666-7051"));
        contacts.add(new Contact("Jeremy Diaz","370-(899)196-6943"));
        contacts.add(new Contact("Rose Henry","420-(240)605-2163"));
        contacts.add(new Contact("Maria Kelly","62-(488)190-3222"));
        */
        final ContactsAdapter adapter = new ContactsAdapter(MyPopupClipboard.this.mContext,
                R.layout.listview_textview_textview_clipboard, contacts);
        lview.setAdapter(adapter);

        final ImageView imageViewIV= (ImageView) view.findViewById(R.id.clipboarddelete);

        lview.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent,final  View view,int position, long id) {

                Contact contacttemp=(Contact)
                        parent.getItemAtPosition(position);

              onText(String.valueOf(contacttemp.getName()));
                }

        });
      //
        lview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Contact contacttemp=(Contact)
                        parent.getItemAtPosition(position);

              // contacts.remove(position);

             //   adapter.notifyDataSetChanged();
              //  removefiles(contacts.get(position).getPhone());
                Toast.makeText(MyPopupClipboard.this.mContext,
                             position+"  "+
                                     contacttemp.getName()
                        ,Toast.LENGTH_SHORT).show();
               // int indextemp=mallclipboarddate.size()-position-1;
               // Toast.makeText(MyPopupClipboard.this.mContext,
                //        mallclipboardID.get(indextemp).toString()
                 //       ,Toast.LENGTH_SHORT).show();
                //Toast.makeText(MyPopupClipboard.this.mContext,
                  //      mallclipboardStrings.get(indextemp).toString()
                    //    ,Toast.LENGTH_SHORT).show();
                /*
                for(String sidtemp :mallclipboarddate){
                    if(sidtemp.equals(contacttemp.getPhone())){
                        removefiles(sidtemp);

                    }
                }
                */

                return true;
            }
        });


        // get last selected page
        // last page was recents, check if there are recents to use
        // if none was found, go to page 1
        final  ImageView imageViewdeletAll= (ImageView) view.findViewById(R.id.deletecopiedfileall);
        imageViewdeletAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeallfiles();
                contacts.clear();
                adapter.notifyDataSetChanged();

            }
        });

        final ImageView imageexit= (ImageView) view.findViewById(R.id.exitpopupclipboard);
        imageexit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               MyPopupClipboard.this.dismiss();
            }
        });




        return view;
    }
    public void removeallfiles() {
        File directory = AppStorage.root(MyPopupClipboard.this.mContext);
        File folder = new File( directory + "/data/data/com.kurdikeyboard" + Constants.ClipboardCacheFolderPath );
        if (!folder.exists()) {
            Toast.makeText(MyPopupClipboard.this.mContext, "nothings exists", Toast.LENGTH_LONG).show();

            try {
                folder.mkdir();
            } catch (Exception e) {
                Toast.makeText(MyPopupClipboard.this.mContext,
                        e.getMessage()
                        ,Toast.LENGTH_SHORT).show();


            }
        }
        File[] files = AppStorage.sortedFiles(folder);
        for (File file : files) {
            if(file.getName().endsWith(".txp")) {
                try {
                     file.delete();

                   // Toast.makeText(MyPopupClipboard.this.mContext,
                    //        "کل حافظه خالی شد",Toast.LENGTH_SHORT).show();


                } catch (Exception ioe) {
                    Toast.makeText(MyPopupClipboard.this.mContext,
                            ioe.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        }

    }
    public void removefiles(String IDdatemiladi){
        File directory = AppStorage.root(MyPopupClipboard.this.mContext);
        File folder = new File( directory + "/data/data/com.kurdikeyboard" + Constants.ClipboardCacheFolderPath );

        if (!folder.exists()) {
            Toast.makeText(MyPopupClipboard.this.mContext, "nothings exists", Toast.LENGTH_LONG).show();

            try {
                folder.mkdir();
            } catch (Exception e) {
                Toast.makeText(MyPopupClipboard.this.mContext,
                        e.getMessage()
                        ,Toast.LENGTH_SHORT).show();


            }
        }
        File[] files = AppStorage.sortedFiles(folder);

        for (File file : files) {
            if(file.getName().endsWith(".txp")) {
                try {
                    //  File tempfile=file;
                    //  toastM("123");
                    FileReader fr = new FileReader(file);
                    BufferedReader br = new BufferedReader(fr);
                    StringBuilder sb = new StringBuilder();

                    String sCurrentLine = br.readLine();
                    String miladidate=sCurrentLine;
                    sCurrentLine = br.readLine();
                    String shamsidate=sCurrentLine;


                    fr.close();
                    br.close();
                    if(IDdatemiladi.equals(shamsidate)){
                        file.delete();

                    }
                } catch (IOException ioe) {
                    Toast.makeText(MyPopupClipboard.this.mContext,
                            ioe.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        }



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
            final TextView contactName =(TextView)contactsView.findViewById(R.id.clipboarddate);
            contactName.setTypeface(null, Typeface.BOLD);
             ////#################################################


            SharedPreferences preferenceKeyboard=
                    this.getContext()
                            .getSharedPreferences("MySharedPreference", Context.MODE_PRIVATE);
            int fontsize=preferenceKeyboard.getInt(mContext.getString(R.string.prefseekbarfontclipbooard),72);
            fontsize /= 4;
            int convertedtospfontsize;
           // final float scale = MyPopupClipboard.this.mContext.getResources().getDisplayMetrics().density;
           // float size_dpi=fontsize*one_DIP;
           // convertedtospfontsize=(int) (size_dpi * scale + 0.5f);
            convertedtospfontsize=fontsize;

            /////###############################

            contactName.setTextSize(TypedValue.COMPLEX_UNIT_SP,convertedtospfontsize);
            final TextView contactPhone = (TextView)contactsView.findViewById(R.id.clipboardstring);
            contactPhone.setTextSize(TypedValue.COMPLEX_UNIT_SP,convertedtospfontsize);
            contactName.setText(contact.getName());
            contactPhone.setText(contact.getPhone());
            ImageView btnImgsend= (ImageView)
                    contactsView.findViewById(R.id.sendclipboardtext);
            btnImgsend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    onText(contactName.getText());

                }
            });
            btnImgsend.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onText(contactPhone.getText());
                    return true;
                }
            });
            final ImageView imageViewdelete= (ImageView) contactsView.findViewById(
                    R.id.clipboarddelete);
            imageViewdelete.setTag(position);
           // final String tempfordelet=contact.getName().toString();
           // final ViewGroup parentfinal=parent;
            imageViewdelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   // final int position = listview1.getPositionForView((View) v.getParent());
                    Integer index = (Integer) v.getTag();
                   // Contact contacttemp=(Contact)
                    //        parentfinal.
             //     Toast.makeText(MyPopupClipboard.this.mContext,
               //           "i: "+index
                 //                 +"   "+contact.getName()
                   //               +"   "+contact.getPhone(),Toast.LENGTH_SHORT).show();
                   /* for(String sidtemp :mallclipboarddate){
                        if(sidtemp.equals(contact.getPhone())){
                            removefiles(sidtemp);

                        }
                    }*/
                    remove(contact);
                    removefiles(contact.getPhone());
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

        ic.commitText(text, 0);
        ic.endBatchEdit();


    }
}


