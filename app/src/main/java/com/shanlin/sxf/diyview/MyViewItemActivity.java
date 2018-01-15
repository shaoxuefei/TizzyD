package com.shanlin.sxf.diyview;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.shanlin.sxf.MyCameraActivity;
import com.shanlin.sxf.R;

public class MyViewItemActivity extends AppCompatActivity {
    private MyViewGroup myViewGroup;
    private String[] strings = new String[]{"我的得", "标签大动静", "的骄傲收到都跑", "大连实德", "的撒旦", "审查判断克拉克打开的", "啊是独立判断拉票的撒旦", "表打击哦啊都怕", "我的哦", "但是扩大"};
    private MyView myView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_view_item);
        initView();
    }

    private void initView() {
        myViewGroup = (MyViewGroup) findViewById(R.id.myViewGroup);
        for (int i = 0; i < 10; i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.group_item_view, null);
            TextView tvTitle = (TextView) view.findViewById(R.id.title);
            tvTitle.setText(strings[i]);
            myViewGroup.addView(view);
        }

        myViewGroup.setCallBack(new ItemClickCallBack() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(MyViewItemActivity.this, strings[position], Toast.LENGTH_SHORT).show();
            }
        });

        myView = (MyView) findViewById(R.id.myView);
        myView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyViewItemActivity.this, MyCameraActivity.class);
                startActivity(intent);
            }
        });
    }
}
