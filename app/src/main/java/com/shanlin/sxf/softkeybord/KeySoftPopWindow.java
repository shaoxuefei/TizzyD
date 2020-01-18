package com.shanlin.sxf.softkeybord;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.PopupWindow;

import com.shanlin.sxf.softkeybord.SoftInputKeyBordView;

public class KeySoftPopWindow extends PopupWindow {

    public KeySoftPopWindow(Context context, AttributeSet attrs) {
        super(context, attrs);
        SoftInputKeyBordView softInputKeyBordView = new SoftInputKeyBordView(context, attrs);
        setContentView(softInputKeyBordView);
        setOutsideTouchable(true);
        setTouchable(true);
    }

    @Override
    public void showAsDropDown(View anchor) {
        super.showAsDropDown(anchor);


    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);

    }
}
