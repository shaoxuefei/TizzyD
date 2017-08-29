package com.shanlin.sxf.diyview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shanlin.sxf.R;

/**
 * Created by Sxf on 2017/5/16.
 *
 * @project: Demo.
 * @detail:
 */

public class TreasureChildView extends LinearLayout {
    public TreasureChildView(Context context,String contentTxt) {
        super(context);
        View view = LayoutInflater.from(context).inflate(R.layout.child_view, this, true);
        TextView content= (TextView) view.findViewById(R.id.content);
        content.setText(contentTxt);
    }
}
