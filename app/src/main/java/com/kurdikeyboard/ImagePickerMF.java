package com.kurdikeyboard;

import android.app.Activity;
import android.content.Context;

import android.content.Intent;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Stat on 11/28/2017.
 */
public class ImagePickerMF extends Preference {
    private final String TAG = getClass().getName();
   // private final Listener mListener = new Listener();
    private static final String ANDROIDNS="http://schemas.android.com/apk/res/android";
    private static final String KURDIKEYBOARDNS="http://kurdikeyboard.com";
    private String mCurrentAddress;
    private Button btnpicker;
    private TextView tvaddress;
    private Context mcontext;


    public ImagePickerMF(Context context, AttributeSet attrs) {
        super(context, attrs);
        mcontext=context;
        initPreference(context, attrs);
    }

    public ImagePickerMF(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mcontext=context;
        initPreference(context, attrs);
    }


    private void initPreference(Context context, AttributeSet attrs) {
        //setValuesFromXml(attrs);
       // mSeekBar = new SeekBar(context, attrs);
       // mSeekBar.setMax(mMaxValue - mMinValue);
       // mSeekBar.setOnSeekBarChangeListener(this);
        /*
        if (btnpicker!=null) {
            btnpicker.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mcontext.getApplicationContext(), TAG + " btn clicked", Toast.LENGTH_SHORT).show();

                }
            });
        }
        */
    }
    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {

        if(restoreValue) {
            mCurrentAddress = getPersistedString(mCurrentAddress);
            tvaddress.setText(mCurrentAddress);
        }else{
            mCurrentAddress="notset";
        }


    }

    @Override
    public void onBindView(View view) {
        super.onBindView(view);
        //btnpicker.setOnClickListener(mListener);
        btnpicker= (Button) view.findViewById(R.id.idselect_picture);

        tvaddress= (TextView) view.findViewById(R.id.idaddressimage);


        if (btnpicker!=null) {
            btnpicker.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(mcontext.getApplicationContext(), TAG + " btn clicked", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    intent.putExtra("name", "imagelunch");
                    //mcontext.startActivity(intent);
                    //startActivity(Intent.createChooser(intent, "Select Picture"));
                    ((Activity) mcontext).startActivityForResult(intent,1234);
                }
            });
        }


    }


    /*
    @Override
    public void startActivity(Intent intent) {
        if (intent.getAction()==Intent.ACTION_GET_CONTENT) {
            super.startActivityForResult(intent, 1234);
        } else {
            super.startActivity(intent);
        }
    }
    */


    /*
    @Override
    protected void onActivityResult(int reqCode, int resCode, Intent data) {
        if (reqCode == 1234) {
            // should be getting called now
        }
    }
    */
    @Override
    protected View onCreateView(ViewGroup parent){
        super.onCreateView(parent);
        LinearLayout layout =  null;

        try {
            LayoutInflater mInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            layout = (LinearLayout) mInflater.inflate(R.layout.pickerlayout, parent, false);


        }
        catch(Exception e)
        {
            Log.e(TAG, "Error creating image picker preference", e);
            Toast.makeText(mcontext,TAG+" Error creating image picker preference",Toast.LENGTH_SHORT).show();

        }

        return layout;

    }



}
