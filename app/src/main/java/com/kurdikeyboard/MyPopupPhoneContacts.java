
package com.kurdikeyboard;

        import android.app.Activity;
        import android.app.AlertDialog;
        import android.content.ContentResolver;
        import android.content.ContentUris;
        import android.content.Context;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.content.pm.PackageManager;
        import android.database.Cursor;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.graphics.Rect;
        import android.graphics.Typeface;
        import android.net.Uri;
        import android.os.Build;
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

        import java.io.InputStream;
        import java.util.ArrayList;
        import java.util.HashMap;
        import java.util.List;
        import java.util.Vector;
        import java.util.jar.Manifest;

/**
 * Created by Stat on 2/28/2017.
 */
public class MyPopupPhoneContacts extends PopupWindow {
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




    public MyPopupPhoneContacts(View rootView, Context mContext
            , kurdikeyboard kv){
        super(mContext);
        this.mContext = mContext;
        this.rootView = rootView;
        this.kk=kv;
        View customView = createCustomView(mContext);
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
                    if (isOpened == false) {
                        if (onSoftKeyboardOpenCloseListenerpopupsetting != null)
                            onSoftKeyboardOpenCloseListenerpopupsetting.onKeyboardOpen(keyBoardHeight);
                    }
                    isOpened = true;
                    if (pendingOpen) {
                        showAtBottom();
                        pendingOpen = false;
                    }
                } else {
                    isOpened = false;
                    if (onSoftKeyboardOpenCloseListenerpopupsetting != null)
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



    private View createCustomView(Context contexttemp) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.layout_phone_contacts2, null, false);


        final ListView lview = (ListView) view.findViewById(R.id.list);


        final List<Contact> contacts = new ArrayList<Contact>();
       // List<ContactModel> listcontacts=getContacts(MyPopupPhoneContacts.this.mContext);
       /* for (int i = 0; i < listcontacts.size(); i++) {

          //  System.out.println("Index: " + i + " - Item: " + list.get(i));
            String nametemp,phonetemp;
            nametemp=listcontacts.get(i).name.toString();
            phonetemp=listcontacts.get(i).mobileNumber.toString();
            contacts.add(new Contact(nametemp,phonetemp));


        }*/

            ArrayList arrContacts = new ArrayList();

            Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
            String selection = ContactsContract.Contacts.HAS_PHONE_NUMBER;
            Cursor cursor =
                    //MyPopupPhoneContacts.this.mContext.getContentResolver().query(uri, new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone._ID, ContactsContract.Contacts._ID}, selection, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
                    contexttemp.getContentResolver().query(uri, new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone._ID, ContactsContract.Contacts._ID}, selection, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");

        if(cursor!=null && cursor.getCount()>0 ) {
            cursor.moveToFirst();


            while (!cursor.isAfterLast()) {

                String contactNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                String contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                int phoneContactID = cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID));
                int contactID = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                contacts.add(new Contact(contactName, contactNumber));
                cursor.moveToNext();

               // cursor.close();

            }
            cursor.close();
           cursor = null;
        }
      /*  contacts.add(new Contact("Gloria Miller","86-(216)605-9056"));
        contacts.add(new Contact("Teresa Garrett","86-(131)666-7051"));
        contacts.add(new Contact("Jeremy Diaz","370-(899)196-6943"));
        contacts.add(new Contact("Rose Henry","420-(240)605-2163"));
        contacts.add(new Contact("Maria Kelly","62-(488)190-3222"));
        */
        final ContactsAdapter adapter = new ContactsAdapter(MyPopupPhoneContacts.this.mContext,
                R.layout.listview_textview_textview, contacts);
        lview.setAdapter(adapter);


        lview.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent,final  View view,int position, long id) {

                Contact contacttemp=(Contact)
                parent.getItemAtPosition(position);
              /*  kk.onText("Mobile:\n");
                kk.onText(contacttemp.getName().toString());
                kk.onText("\n");
                kk.onText(contacttemp.getPhone().toString());
                */
                onText("Mobile:\n");
                onText(contacttemp.getName().toString());
                onText("\n");
                onText(contacttemp.getPhone().toString());

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


    public class ContactsAdapter extends ArrayAdapter<Contact>{

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
        public View getView(int position, View convertView, ViewGroup parent)
        {
            LinearLayout contactsView;
            Contact contact = getItem(position);
            if(convertView==null) {
                contactsView = new LinearLayout(getContext());
                String inflater = Context.LAYOUT_INFLATER_SERVICE;
                LayoutInflater vi;
                vi = (LayoutInflater)getContext().getSystemService(inflater);
                vi.inflate(resource, contactsView, true);
            } else {
                contactsView = (LinearLayout) convertView;
            }
            final TextView contactName =(TextView)contactsView.findViewById(R.id.contactName);
            contactName.setTypeface(null, Typeface.BOLD);

            ////#################################################


            SharedPreferences preferenceKeyboard=
                    this.getContext()
                            .getSharedPreferences("MySharedPreference", Context.MODE_PRIVATE);
            int fontsize=preferenceKeyboard.getInt(mContext.getString(R.string.prefseekbarfontcontacts),72);
            fontsize /= 4;
            int convertedtospfontsize;
           // final float scale = MyPopupPhoneContacts.this.mContext.getResources().getDisplayMetrics().density;
           // float size_dpi=fontsize*one_DIP;
           // convertedtospfontsize=(int) (size_dpi * scale + 0.5f);
            convertedtospfontsize=fontsize;

            /////###############################
            contactName.setTextSize(TypedValue.COMPLEX_UNIT_SP,convertedtospfontsize);
            final TextView contactPhone = (TextView)contactsView.findViewById(R.id.contactPhone);
            contactPhone.setTextSize(TypedValue.COMPLEX_UNIT_SP, convertedtospfontsize);
            contactName.setText(contact.getName());
            contactPhone.setText(contact.getPhone());
            ImageView btnImg= (ImageView) contactsView.findViewById(R.id.imageviewhumancontacts);
            btnImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onText("Mobile:\n");
                    onText(contactName.getText());
                    onText("\n");
                    onText(contactPhone.getText());
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
    public List<ContactModel> getContacts(Context ctx) {
        List<ContactModel> list = new ArrayList<>();
        ContentResolver contentResolver = ctx.getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                if (cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor cursorInfo = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            null,null,null);

                          //  ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                //    InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(ctx.getContentResolver(),
                 //           ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, new Long(id)));

                 //   Uri person = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, new Long(id));
                  //  Uri pURI = Uri.withAppendedPath(person, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);

                  //  Bitmap photo = null;
                  //  if (inputStream != null) {
                  //      photo = BitmapFactory.decodeStream(inputStream);
                   // }
                    while (cursorInfo.moveToNext()) {
                        ContactModel info = new ContactModel();
                     //   info.id = id;
                        info.name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        info.mobileNumber = cursorInfo.getString(cursorInfo.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                      //  info.photo = photo;
                      // info.photoURI= pURI;
                        list.add(info);
                    }

                    cursorInfo.close();
                }
            }
        }
        return list;
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

