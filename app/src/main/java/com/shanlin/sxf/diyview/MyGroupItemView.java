package com.shanlin.sxf.diyview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shanlin.sxf.R;

/**
 * @Description:
 * @Auther: Sxf
 * @Date: 2017/11/28
 */

public class MyGroupItemView extends LinearLayout {
    TextView btn;

    public MyGroupItemView(Context context, String btnTxt) {
        this(context, null, btnTxt);
    }

    public MyGroupItemView(Context context, @Nullable AttributeSet attrs, String btnTxt) {
        this(context, attrs, 0, btnTxt);
    }

    public MyGroupItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, String btnTxt) {
        super(context, attrs, defStyleAttr);
        View view = LayoutInflater.from(context).inflate(R.layout.main_viewgroup_item, this, true);
        btn = (TextView) view.findViewById(R.id.title);
        btn.setText(btnTxt);
    }
}
