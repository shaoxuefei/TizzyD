package com.shanlin.sxf;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;


/**
 * Created by Sxf on 2017/5/11.
 *
 * @project: Demo.
 * @detail:
 */

public class SignNameView extends LinearLayout {

    public SignNameView(final Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.sign_nameview,this,true);

    }

}
