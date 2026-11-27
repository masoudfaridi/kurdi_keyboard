package com.kurdikeyboard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Main22Activity extends Activity {
    private static final float one_DIP = 1.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(android.R.style.Theme_Holo_Light);
        setContentView(R.layout.activity_main22);
        reloadlistview();

    }
    public void createfile(String namefile, String txtfile){
        boolean duplicated=false;
        CreatAllDirecories("/data");
        CreatAllDirecories("/data/data");
        CreatAllDirecories("/data/data/com.kurdikeyboard");
        CreatAllDirecories("/data/data/com.kurdikeyboard/"+ Constants.ReadySentencesFolderPath);

        File directory = AppStorage.root(getApplicationContext());
        File folder = new File( directory + "/data/data/com.kurdikeyboard/" + Constants.ReadySentencesFolderPath );
        if (!folder.exists()) {
            Toast.makeText(getApplicationContext(), "nothings exists", Toast.LENGTH_LONG).show();

            try {
                folder.mkdir();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),
                        e.getMessage()
                        ,Toast.LENGTH_SHORT).show();


            }
        }
        File[] files = AppStorage.sortedFiles(folder);
        for (File file : files) {
            if(file.getName().endsWith(".txp")) {
                try {
                    String filenameexist=
                    file.getName().substring(0,file.getName().length()-4);
                    if(
                           filenameexist.equals(namefile)){
                        Toast.makeText(getApplicationContext(),
                                "این عنوان وجود دارد لطفا عنوان دیگری را انتخاب نمایید",Toast.LENGTH_SHORT).show();

                        duplicated=true;
                        break;
                    }

                } catch (Exception ei) {

                }
            }
        }
        if(!duplicated) {
            try {
                File filetemp = new File(folder.getPath(), namefile+".txp");
                // Toast.makeText(this,folder.getPath().toString(),Toast.LENGTH_LONG).show();
                filetemp.createNewFile();
                BufferedWriter bWriter = new BufferedWriter(new FileWriter(filetemp));
                bWriter.write(txtfile);
                bWriter.close();
                reloadlistview();

            } catch (IOException ei) {
                Toast.makeText(getApplicationContext(),
                        ei.getMessage(), Toast.LENGTH_SHORT).show();


            }
        }




    }
    public void reloadlistview(){
        final ListView lview = (ListView) findViewById(R.id.listView3);

        final List<Contact> contacts = new ArrayList<Contact>();
        final List<String> mallclipboardStrings=new ArrayList<String>();
        final List<String> mallclipboarddate=new ArrayList<String>();
        // final List<String> mallclipboardID=new ArrayList<String>();
        CreatAllDirecories("/data");
        CreatAllDirecories("/data/data");
        CreatAllDirecories("/data/data/com.kurdikeyboard");
        CreatAllDirecories("/data/data/com.kurdikeyboard/"+ Constants.ReadySentencesFolderPath);

        File directory = AppStorage.root(getApplicationContext());
        File folder = new File( directory + "/data/data/com.kurdikeyboard/" + Constants.ReadySentencesFolderPath );

        if (!folder.exists()) {
            Toast.makeText(getApplicationContext(), "nothings exists", Toast.LENGTH_LONG).show();

            try {
                folder.mkdir();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),
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
        final ContactsAdapter adapter = new ContactsAdapter(getApplicationContext(),
                R.layout.textviewactivity22, contacts);
        lview.setAdapter(adapter);

        lview.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent,final  View view,int position, long id) {
                SharedPreferences preferenceKeyboard=
                        Main22Activity.this
                                .getSharedPreferences("MySharedPreference", Context.MODE_PRIVATE);
                int fontsize=preferenceKeyboard.getInt(Main22Activity.this
                        .getString(R.string.prefseekbarfontreadysentences), 72);
                fontsize /= 4;
                int convertedtospfontsize;
               // final float scale = Main22Activity.this.getResources().getDisplayMetrics().density;
               // float size_dpi=fontsize*one_DIP;
                //convertedtospfontsize=(int) (size_dpi * scale + 0.5f);
                convertedtospfontsize=fontsize;
                Contact contacttemp=(Contact)
                        parent.getItemAtPosition(position);
                EditText editTexttitle=
                        (EditText) findViewById(R.id.editTextTiltle);

                EditText editTextsavedtxt=
                        (EditText) findViewById(R.id.editTextText);
                editTexttitle.setText(
                        contacttemp.getName().substring(0,
                                contacttemp.getName().length() - 4)
                );
                editTextsavedtxt.setText(contacttemp.getPhone());

                editTexttitle.setTextSize(TypedValue.COMPLEX_UNIT_SP,convertedtospfontsize);
                editTextsavedtxt.setTextSize(TypedValue.COMPLEX_UNIT_SP,convertedtospfontsize);



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

                removefiles(
                        contacttemp.getName()//.substring(0, contacttemp.getName().length()-4)
                );
                Toast.makeText(getApplicationContext(),
                        contacttemp.getName().substring(0, contacttemp.getName().length()-4)

                        ,Toast.LENGTH_SHORT).show();
                return true;
            }
        });


    }
    public void removefiles(String namefile) {
        File directory = AppStorage.root(getApplicationContext());
        File folder = new File(directory + "/data/data/com.kurdikeyboard/" + Constants.ReadySentencesFolderPath);
        Toast.makeText(getApplicationContext(),
                "removing"+   namefile
                ,Toast.LENGTH_LONG).show();
        if (!folder.exists()) {
            Toast.makeText(getApplicationContext(), "nothings exists", Toast.LENGTH_LONG).show();

            try {
                folder.mkdir();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),
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
                        Toast.makeText(getApplicationContext(),
                                ioe.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }


        }
    }

    public void onClick(View view) {
        EditText editTexttitle=
                (EditText) findViewById(R.id.editTextTiltle);
        EditText editTextsavedtxt=
                (EditText) findViewById(R.id.editTextText);
        if(view.getId()==R.id.btnexitactivity22){
            this.finish();

        }
        else if(view.getId()==R.id.btncancel){
              editTexttitle.setText("");
            editTextsavedtxt.setText("");

        }else  if(view.getId()==R.id.btnsave){
            if((editTexttitle.getText().toString().length()>0)&&
                    (editTextsavedtxt.getText().toString().length()>0)){
                createfile(editTexttitle.getText().toString(),
                        editTextsavedtxt.getText().toString());
                editTexttitle.setText("");
                editTextsavedtxt.setText("");
                editTexttitle.requestFocus();


            }else{
                Toast.makeText(getApplicationContext()
                        ,"لطفا عنوان و متن را وارد نمایید",
                        Toast.LENGTH_SHORT).show();

            }


        }else if(view.getId()==R.id.btnexitactivity22){
            finish();

        }
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
            contactName.setTextSize(TypedValue.COMPLEX_UNIT_PX,70);
            TextView contactPhone = (TextView)contactsView.findViewById(R.id.clipboardstring2);
            contactPhone.setTextSize(TypedValue.COMPLEX_UNIT_PX,60);
            contactName.setText(contact.getName().
                    substring(0, contact.getName().length()-4));
            contactPhone.setText(contact.getPhone());

            final ImageView imageViewdelete= (ImageView) contactsView.findViewById(
                    R.id.clipboarddelete2);
            imageViewdelete.setTag(position);
            // final String tempfordelet=contact.getName().toString();
            // final ViewGroup parentfinal=parent;
            imageViewdelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // final int position = listview1.getPositionForView((View) v.getParent());
                    Integer index = (Integer) v.getTag();

                    remove(contact);
                    removefiles(contact.getName());
                    Toast.makeText(getApplicationContext(),
                            contact.getName().substring(0, contact.getName().length()-4)
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
    public void  CreatAllDirecories(String path2){
        File directory = AppStorage.root(getApplicationContext());
        File folder = new File( directory + path2 );

        if (!folder.exists()) {
            // Toast.makeText(this, "!!!folder.exists222", Toast.LENGTH_LONG).show();

            try {
                folder.mkdir();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),
                        e.getMessage()
                        ,Toast.LENGTH_SHORT).show();


            }
        }
    }

}
