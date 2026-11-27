package com.kurdikeyboard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Stat on 12/6/2017.
 */
public class ImagePickerActivity extends Activity {
    int MY_READ_PERMISSION_REQUEST_CODE =1 ;
    public static final int PICK_IMAGE = 1;
    private ImageView ivpic;
    private Button btnpick,btnsave,btncancel;
    private TextView tvaddress;
    private Bitmap bitmap;
    public void onCreate(Bundle sis){
        super.onCreate(sis);
        setContentView(R.layout.pickerlayout);
        ivpic= (ImageView) findViewById(R.id.ivimage);
        btnpick= (Button) findViewById(R.id.idselect_picture);
        btnsave= (Button) findViewById(R.id.btnsavepic);
        btncancel= (Button) findViewById(R.id.btncancelpic);
        tvaddress= (TextView) findViewById(R.id.idaddressimage);
        btnpick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                beginload();
                Toast.makeText(getApplicationContext(),
                        getString(R.string.summarybg)
                        ,Toast.LENGTH_LONG).show();
            }
        });
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savepic();
            }
        });
        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public void beginload(){

        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra("return-data", true);
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

        startActivityForResult(chooserIntent, PICK_IMAGE);

        /*
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra("return-data", true);
        startActivityForResult(intent, PICK_IMAGE);
        */


    }
    public void savepic(){
        SharedPreferences preferenceKeyboard=
                ImagePickerActivity.this.getBaseContext()
                        .getSharedPreferences("MySharedPreference", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferenceKeyboard.edit();
        editor.putString(
                ImagePickerActivity.this.getString(R.string.prefkeypathimage)
                , tvaddress.getText().toString());
        editor.apply();
        finish();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            Toast.makeText(getApplicationContext(),getString(R.string.result_not_ok),Toast.LENGTH_LONG).show();
            return;
        }else{
            final Bundle extras = data.getExtras();
            if (extras == null) {
                //Get image
                Toast.makeText(getApplicationContext(),"access to data deny",Toast.LENGTH_LONG).show();

            }
        }
        if (requestCode == PICK_IMAGE) {
            final Bundle extras = data.getExtras();
            if (extras != null) {
                //Get image
                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();

                 bitmap = BitmapFactory.decodeFile(picturePath);
                ivpic.setImageBitmap(bitmap);
                tvaddress.setText(picturePath);



            }
        }
    }
    private void CHECKGALLERYPERMISSION()
    {
        int MY_READ_PERMISSION_REQUEST_CODE =1 ;
        int PICK_IMAGE_REQUEST = 2;
        if (ContextCompat.checkSelfPermission(ImagePickerActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED)
        {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
        }
        else
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                requestPermissions(
                        new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_READ_PERMISSION_REQUEST_CODE);
            }
            else {
                beginload();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[]
            permissions, int[] grantResults) {
        if (requestCode == MY_READ_PERMISSION_REQUEST_CODE
                && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
           beginload();
        }
    }
}
