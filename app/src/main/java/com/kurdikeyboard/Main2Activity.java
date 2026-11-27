package com.kurdikeyboard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Main2Activity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setLayoutDirection(android.view.View.LAYOUT_DIRECTION_RTL);
        setContentView(R.layout.activity_main2);
    }
    static final int PICK_CONTACT_REQUEST = 1;  // The request code
    public void onClick(View view) {
        EditText editTextphone = (EditText) findViewById(R.id.editText2);
        EditText editTextname = (EditText) findViewById(R.id.editText);
        Button btnsend = (Button) findViewById(R.id.button2send);

        if(view.getId()==R.id.button) {
            Intent pickContactIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
            pickContactIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE); // Show user only contacts w/ phone numbers

            startActivityForResult(pickContactIntent, PICK_CONTACT_REQUEST);
             editTextname.setVisibility(View.VISIBLE);
            editTextphone.setVisibility(View.VISIBLE);
            btnsend.setVisibility(View.VISIBLE);
        }else if(view.getId()==R.id.button2send){
            setClipboard("Mobile:\n"+ editTextname.getText()
                    +"\n"+editTextphone.getText());
            this.finish();
        }else {
            setClipboard(editTextphone.getText().toString());
            this.finish();
        }

    }
    public void setClipboard(String text) {
        if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(text);
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
            clipboard.setPrimaryClip(clip);
            InputMethodManager inputMethodManager =
                    (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputMethodManager != null){
                inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request it is that we're responding to
        if (requestCode == PICK_CONTACT_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // Get the URI that points to the selected contact
                Uri contactUri = data.getData();
                // We only need the NUMBER column, because there will be only one row in the result
                String[] projection = {ContactsContract.CommonDataKinds.Phone.NUMBER
                        , ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME};

                // Perform the query on the contact to get the NUMBER column
                // We don't need a selection or sort order (there's only one result for the given URI)
                // CAUTION: The query() method should be called from a separate thread to avoid blocking
                // your app's UI thread. (For simplicity of the sample, this code doesn't do that.)
                // Consider using CursorLoader to perform the query.
                Cursor cursor = getContentResolver()
                        .query(contactUri, projection, null, null, null);
                cursor.moveToFirst();

                // Retrieve the phone number from the NUMBER column
                int column = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                String number = cursor.getString(column);

                int nameColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                String name = cursor.getString(nameColumnIndex);

                EditText editTextphone= (EditText) findViewById(R.id.editText2);
               editTextphone.setText(number);
               EditText editTextname= (EditText) findViewById(R.id.editText);
               editTextname.setText(name);

                // Do something with the phone number...
            }
        }


    }

}
