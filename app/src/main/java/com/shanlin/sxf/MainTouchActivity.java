package com.shanlin.sxf;

import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.shanlin.sxf.diytouch.MyLinearLayout;
import com.shanlin.sxf.diytouch.MyTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author : SXF
 * @ date   : 2018/3/23
 * e-mail  : shaoxf@go-goal.com
 * desc    :
 */

public class MainTouchActivity extends AppCompatActivity {

    @BindView(R.id.myTextView)
    MyTextView myTextView;
    @BindView(R.id.linearLayout)
    MyLinearLayout linearLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_touch);
        ButterKnife.bind(this);
        initView();
        ShapeDrawable shapeDrawable = new ShapeDrawable();

        GradientDrawable gradientDrawable = new GradientDrawable();
        //左上右上右下左下
//        gradientDrawable.setCornerRadii(new float[]);
        gradientDrawable.setCornerRadius(Utils.getInstance().getMetrics(this).density * 5);
    }

    private void initView() {
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainTouchActivity.this, "linearLayout", Toast.LENGTH_SHORT).show();
            }
        });


        myTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainTouchActivity.this, "textView", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
