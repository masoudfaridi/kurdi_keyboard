package com.kurdikeyboard;

/**
 * Created by Stat on 4/23/2017.
 */
import android.content.Context;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

public class CustomPagerAdapter extends PagerAdapter {

    private Context mContext;

    public CustomPagerAdapter(Context context) {
        mContext = context;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        ModelObject modelObject = ModelObject.values()[position];
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(modelObject.getLayoutResId(), collection, false);
        collection.addView(layout);
        try {
            loadpage1data(layout);
        }catch (Exception ei){

        }
        try {
            loadpage2data(layout);
        }catch (Exception ei){

        }
        try {
            loadpage3data(layout);
        }catch (Exception ei){

        }
        try {
            loadpage4data(layout);
        }catch (Exception ei){

        }
        try {
            loadpage5data(layout);
        }catch (Exception ei){

        }
        try {
            loadpage6data(layout);
        }catch (Exception ei){

        }
        try {
            loadpage7data(layout);
        }catch (Exception ei){

        }
        try {
            loadpage8data(layout);
        }catch (Exception ei){

        }
        try {
            loadpage9data(layout);
        }catch (Exception ei){

        }
        try {
            loadpage10data(layout);
        }catch (Exception ei){

        }
        try {
            loadpage11data(layout);
        }catch (Exception ei){

        }

        try {
            loadpageenddata(layout);
        }catch (Exception ei){

        }

        return layout;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return ModelObject.values().length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        ModelObject customPagerEnum = ModelObject.values()[position];
        return mContext.getString(customPagerEnum.getTitleResId());
    }
    public void loadpage1data(ViewGroup layout){

        ////////////////////////////webview 1 page 1

        WebView webviewguidpage1 = (WebView) layout.findViewById(R.id.vebviewguidpage1_1);
        WebSettings settings1 = webviewguidpage1.getSettings();
        settings1.setDefaultTextEncodingName("utf-8");
        settings1.setTextSize(WebSettings.TextSize.LARGER);
        if(webviewguidpage1!=null) {
            String strtemp1=
                    mContext.getResources().getText(R.string.pagestartDescriptive_page1_wv1).toString();

            webviewSetText(webviewguidpage1,strtemp1);
        }
        //////////////////////////////webview 2 page 1
            WebView webviewguidpage2 = (WebView) layout.findViewById(R.id.vebviewguidpage1_2);
            WebSettings settings2 = webviewguidpage2.getSettings();
            settings2.setDefaultTextEncodingName("utf-8");
           settings2.setTextSize(WebSettings.TextSize.NORMAL);
        if(webviewguidpage2!=null) {
            String strtemp2=
                    mContext.getResources().getText(R.string.pagestartDescriptive_page1_wv2).toString();

            webviewSetText(webviewguidpage2,strtemp2);
        }

        //////////////////////////////////////////////////////////


    }
    public void loadpage2data(ViewGroup layout){

        ////////////////////////////webview 1 page 1

        WebView webview1guidpage2 = (WebView) layout.findViewById(R.id.webViewguidpage2_1);
        WebSettings settings1 = webview1guidpage2.getSettings();
        settings1.setDefaultTextEncodingName("utf-8");
        settings1.setTextSize(WebSettings.TextSize.LARGER);
        if(webview1guidpage2!=null) {
            String strtemp1=
                    mContext.getResources().getText(R.string.pagestartDescriptive_page2_wv1).toString();

            webviewSetText(webview1guidpage2,strtemp1);
        }
        //////////////////////////////webview 2 page 1
        WebView webview2guidpage2 = (WebView) layout.findViewById(R.id.webViewguidpage2_2);
        WebSettings settings2 = webview2guidpage2.getSettings();
        settings2.setDefaultTextEncodingName("utf-8");
        settings2.setTextSize(WebSettings.TextSize.NORMAL);
        if(webview2guidpage2!=null) {
            String strtemp2=
                    mContext.getResources().getText(R.string.pagestartDescriptive_page2_wv2).toString();

            webviewSetText(webview2guidpage2,strtemp2);
        }


        //////////////////////////////////////////////////////////


    }
    public void loadpage3data(ViewGroup layout) {

        ////////////////////////////webview 1 page 1

        WebView webview1guidpage2 = (WebView) layout.findViewById(R.id.webViewguidpage3_1);
        WebSettings settings1 = webview1guidpage2.getSettings();
        settings1.setDefaultTextEncodingName("utf-8");
        settings1.setTextSize(WebSettings.TextSize.LARGER);
        if (webview1guidpage2 != null) {
            String strtemp1 =
                    mContext.getResources().getText(R.string.pagestartDescriptive_page3_wv1).toString();

            webviewSetText(webview1guidpage2, strtemp1);
        }
        //////////////////////////////webview 2 page 1
        WebView webview2guidpage2 = (WebView) layout.findViewById(R.id.webViewguidpage3_2);
        WebSettings settings2 = webview2guidpage2.getSettings();
        settings2.setDefaultTextEncodingName("utf-8");
        settings2.setTextSize(WebSettings.TextSize.NORMAL);
        if (webview2guidpage2 != null) {
            String strtemp2 =
                    mContext.getResources().getText(R.string.pagestartDescriptive_page3_wv2).toString();

            webviewSetText(webview2guidpage2, strtemp2);
        }
    }
    public void loadpage4data(ViewGroup layout) {

        ////////////////////////////webview 1 page 1

        WebView webview1guidpage2 = (WebView) layout.findViewById(R.id.webViewguidpage4_1);
        WebSettings settings1 = webview1guidpage2.getSettings();
        settings1.setDefaultTextEncodingName("utf-8");
        settings1.setTextSize(WebSettings.TextSize.LARGER);
        if (webview1guidpage2 != null) {
            String strtemp1 =
                    mContext.getResources().getText(R.string.pagestartDescriptive_page4_wv1).toString();

            webviewSetText(webview1guidpage2, strtemp1);
        }
        //////////////////////////////webview 2 page 1
        WebView webview2guidpage2 = (WebView) layout.findViewById(R.id.webViewguidpage4_2);
        WebSettings settings2 = webview2guidpage2.getSettings();
        settings2.setDefaultTextEncodingName("utf-8");
        settings2.setTextSize(WebSettings.TextSize.NORMAL);
        if (webview2guidpage2 != null) {
            String strtemp2 =
                    mContext.getResources().getText(R.string.pagestartDescriptive_page4_wv2).toString();

            webviewSetText(webview2guidpage2, strtemp2);
        }
    }
    public void loadpage5data(ViewGroup layout) {

        ////////////////////////////webview 1 page 1

        WebView webview1guidpage2 = (WebView) layout.findViewById(R.id.webViewguidpage5_1);
        WebSettings settings1 = webview1guidpage2.getSettings();
        settings1.setDefaultTextEncodingName("utf-8");
        settings1.setTextSize(WebSettings.TextSize.LARGER);
        if (webview1guidpage2 != null) {
            String strtemp1 =
                    mContext.getResources().getText(R.string.pagestartDescriptive_page5_wv1).toString();

            webviewSetText(webview1guidpage2, strtemp1);
        }
        //////////////////////////////webview 2 page 1
        WebView webview2guidpage2 = (WebView) layout.findViewById(R.id.webViewguidpage5_2);
        WebSettings settings2 = webview2guidpage2.getSettings();
        settings2.setDefaultTextEncodingName("utf-8");
        settings2.setTextSize(WebSettings.TextSize.NORMAL);
        if (webview2guidpage2 != null) {
            String strtemp2 =
                    mContext.getResources().getText(R.string.pagestartDescriptive_page5_wv2).toString();

            webviewSetText(webview2guidpage2, strtemp2);
        }
    }
    public void loadpage6data(ViewGroup layout) {

        ////////////////////////////webview 1 page 1

        WebView webview1guidpage2 = (WebView) layout.findViewById(R.id.webViewguidpage6_1);
        WebSettings settings1 = webview1guidpage2.getSettings();
        settings1.setDefaultTextEncodingName("utf-8");
        settings1.setTextSize(WebSettings.TextSize.LARGER);
        if (webview1guidpage2 != null) {
            String strtemp1 =
                    mContext.getResources().getText(R.string.pagestartDescriptive_page6_wv1).toString();

            webviewSetText(webview1guidpage2, strtemp1);
        }
        //////////////////////////////webview 2 page 1
        WebView webview2guidpage2 = (WebView) layout.findViewById(R.id.webViewguidpage6_2);
        WebSettings settings2 = webview2guidpage2.getSettings();
        settings2.setDefaultTextEncodingName("utf-8");
        settings2.setTextSize(WebSettings.TextSize.NORMAL);
        if (webview2guidpage2 != null) {
            String strtemp2 =
                    mContext.getResources().getText(R.string.pagestartDescriptive_page6_wv2).toString()
                                        ;

            webviewSetText(webview2guidpage2, strtemp2);
        }
        ////////////////////////////////////

    }

    public void loadpage7data(ViewGroup layout) {

        ////////////////////////////webview 1 page 1

        WebView webview1guidpage2 = (WebView) layout.findViewById(R.id.webViewguidpage7_1);
        WebSettings settings1 = webview1guidpage2.getSettings();
        settings1.setDefaultTextEncodingName("utf-8");
        settings1.setTextSize(WebSettings.TextSize.LARGER);
        if (webview1guidpage2 != null) {
            String strtemp1 =
                    mContext.getResources().getText(R.string.pagestartDescriptive_page7_wv1).toString();

            webviewSetText(webview1guidpage2, strtemp1);
        }
        //////////////////////////////webview 2 page 1
        WebView webview2guidpage2 = (WebView) layout.findViewById(R.id.webViewguidpage7_2);
        WebSettings settings2 = webview2guidpage2.getSettings();
        settings2.setDefaultTextEncodingName("utf-8");
        settings2.setTextSize(WebSettings.TextSize.NORMAL);
        if (webview2guidpage2 != null) {
            String strtemp2 =
                    mContext.getResources().getText(R.string.pagestartDescriptive_page7_wv2).toString();

            webviewSetText(webview2guidpage2, strtemp2);
        }
    }

    public void loadpage8data(ViewGroup layout) {

        ////////////////////////////webview 1 page 1

        WebView webview1guidpage2 = (WebView) layout.findViewById(R.id.webViewguidpage8_1);
        WebSettings settings1 = webview1guidpage2.getSettings();
        settings1.setDefaultTextEncodingName("utf-8");
        settings1.setTextSize(WebSettings.TextSize.LARGER);
        if (webview1guidpage2 != null) {
            String strtemp1 =
                    mContext.getResources().getText(R.string.pagestartDescriptive_page8_wv1).toString();

            webviewSetText(webview1guidpage2, strtemp1);
        }
        //////////////////////////////webview 2 page 1
        WebView webview2guidpage2 = (WebView) layout.findViewById(R.id.webViewguidpage8_2);
        WebSettings settings2 = webview2guidpage2.getSettings();
        settings2.setDefaultTextEncodingName("utf-8");
        settings2.setTextSize(WebSettings.TextSize.NORMAL);
        if (webview2guidpage2 != null) {
            String strtemp2 =
                    mContext.getResources().getText(R.string.pagestartDescriptive_page8_wv2).toString();

            webviewSetText(webview2guidpage2, strtemp2);
        }
    }

    public void loadpage9data(ViewGroup layout) {

        ////////////////////////////webview 1 page 1

        WebView webview1guidpage2 = (WebView) layout.findViewById(R.id.webViewguidpage9_1);
        WebSettings settings1 = webview1guidpage2.getSettings();
        settings1.setDefaultTextEncodingName("utf-8");
        settings1.setTextSize(WebSettings.TextSize.LARGER);
        if (webview1guidpage2 != null) {
            String strtemp1 =
                    mContext.getResources().getText(R.string.pagestartDescriptive_page9_wv1).toString();

            webviewSetText(webview1guidpage2, strtemp1);
        }
        //////////////////////////////webview 2 page 1
        WebView webview2guidpage2 = (WebView) layout.findViewById(R.id.webViewguidpage9_2);
        WebSettings settings2 = webview2guidpage2.getSettings();
        settings2.setDefaultTextEncodingName("utf-8");
        settings2.setTextSize(WebSettings.TextSize.NORMAL);
        if (webview2guidpage2 != null) {
            String strtemp2 =
                    mContext.getResources().getText(R.string.pagestartDescriptive_page9_wv2).toString();

            webviewSetText(webview2guidpage2, strtemp2);
        }
    }


    public void loadpage10data(ViewGroup layout) {

        ////////////////////////////webview 1 page 1

        WebView webview1guidpage2 = (WebView) layout.findViewById(R.id.webViewguidpage10_1);
        WebSettings settings1 = webview1guidpage2.getSettings();
        settings1.setDefaultTextEncodingName("utf-8");
        settings1.setTextSize(WebSettings.TextSize.LARGER);
        if (webview1guidpage2 != null) {
            String strtemp1 =
                    mContext.getResources().getText(R.string.pagestartDescriptive_page10_wv1).toString();

            webviewSetText(webview1guidpage2, strtemp1);
        }
        //////////////////////////////webview 2 page 1
        WebView webview2guidpage2 = (WebView) layout.findViewById(R.id.webViewguidpage10_2);
        WebSettings settings2 = webview2guidpage2.getSettings();
        settings2.setDefaultTextEncodingName("utf-8");
        settings2.setTextSize(WebSettings.TextSize.NORMAL);
        if (webview2guidpage2 != null) {
            String strtemp2 =
                    mContext.getResources().getText(R.string.pagestartDescriptive_page10_wv2).toString();

            webviewSetText(webview2guidpage2, strtemp2);
        }
    }


    public void loadpage11data(ViewGroup layout) {

        ////////////////////////////webview 1 page 1

        WebView webview1guidpage2 = (WebView) layout.findViewById(R.id.webViewguidpage11_1);
        WebSettings settings1 = webview1guidpage2.getSettings();
        settings1.setDefaultTextEncodingName("utf-8");
        settings1.setTextSize(WebSettings.TextSize.LARGER);
        if (webview1guidpage2 != null) {
            String strtemp1 =
                    mContext.getResources().getText(R.string.pagestartDescriptive_page11_wv1).toString();

            webviewSetText(webview1guidpage2, strtemp1);
        }
        //////////////////////////////webview 2 page 1
        WebView webview2guidpage2 = (WebView) layout.findViewById(R.id.webViewguidpage11_2);
        WebSettings settings2 = webview2guidpage2.getSettings();
        settings2.setDefaultTextEncodingName("utf-8");
        settings2.setTextSize(WebSettings.TextSize.NORMAL);
        if (webview2guidpage2 != null) {
            String strtemp2 =
                    mContext.getResources().getText(R.string.pagestartDescriptive_page11_wv2).toString();

            webviewSetText(webview2guidpage2, strtemp2);
        }
    }


    public void loadpageenddata(ViewGroup layout) {

        ////////////////////////////webview 1 page 1

        WebView webview1guidpage2 = (WebView) layout.findViewById(R.id.webViewguidpageend_1);
        WebSettings settings1 = webview1guidpage2.getSettings();
        settings1.setDefaultTextEncodingName("utf-8");
        settings1.setTextSize(WebSettings.TextSize.LARGER);
        if (webview1guidpage2 != null) {
            String strtemp1 =
                    mContext.getResources().getText(R.string.pagestartDescriptive_pageend_wv1).toString();

            webviewSetText(webview1guidpage2, strtemp1);
        }
        //////////////////////////////webview 2 page 1
        WebView webview2guidpage2 = (WebView) layout.findViewById(R.id.webViewguidpageend_2);
        WebSettings settings2 = webview2guidpage2.getSettings();
        settings2.setDefaultTextEncodingName("utf-8");
        settings2.setTextSize(WebSettings.TextSize.NORMAL);
        if (webview2guidpage2 != null) {
            String strtemp2 =
                    mContext.getResources().getText(R.string.pagestartDescriptive_pageend_wv2).toString();

            webviewSetText(webview2guidpage2, strtemp2);
        }
    }


    public void webviewSetText(WebView webviewguidpage1
    ,String str){
        if(webviewguidpage1!=null) {
            String text;
            text = "<html><head><meta name=\"viewport\" content=\"width=device-width\"><style>"
                    + "body{margin:0;padding:16px 18px;background:#F4F6F8;color:#1A2433;font-size:17px;line-height:1.85;text-align:justify}"
                    + "</style></head><body dir=\"rtl\">";
            text += str;
            text += "</body></html>";
            //  webviewguidpage1.loadData(text, "text/html", "utf-8",null);
            webviewguidpage1.loadDataWithBaseURL(
                    null,
                    //mContext.getResources().getText(R.string.pagestartDescriptive).toString()
                    text

                    , "text/html", "UTF-8", null
            );

        }
    }

}
