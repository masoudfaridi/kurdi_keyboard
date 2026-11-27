package com.kurdikeyboard;

/**
 * Created by Stat on 4/23/2017.
 */
public enum ModelObject {

    PAGE1(R.string.page1, R.layout.view_start),
    PAGE2(R.string.page2, R.layout.view_page2),
    PAGE3(R.string.page3, R.layout.view_page3),
    PAGE4(R.string.page4, R.layout.view_page4),
    PAGE5(R.string.page5, R.layout.view_page5),
    PAGE6(R.string.page6, R.layout.view_page6),
   PAGE7(R.string.page7, R.layout.view_page7),
   PAGE8(R.string.page8, R.layout.view_page8),
   PAGE9(R.string.page9, R.layout.view_page9),
   PAGE10(R.string.page10, R.layout.view_page10),
    PAGE11(R.string.page11, R.layout.view_page11),
   PAGE1end(R.string.pageend, R.layout.view_end);


    private int mTitleResId;
    private int mLayoutResId;

    ModelObject(int titleResId, int layoutResId) {
        mTitleResId = titleResId;
        mLayoutResId = layoutResId;
    }

    public int getTitleResId() {
        return mTitleResId;
    }

    public int getLayoutResId() {
        return mLayoutResId;
    }

}