package com.shanlin.sxf;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.shanlin.sxf.popubwindows.PopOnItemClickListener;
import com.shanlin.sxf.popubwindows.PopubBeanInfo;
import com.shanlin.sxf.popubwindows.PopubBeanWindow;

import java.util.ArrayList;

/**
 * Created by Sxf on 2017/5/15.
 *
 * @project: Demo.
 * @detail:
 */
public class PopubWindowActivity extends AppCompatActivity {
    private TextView textView;
    private PopubBeanWindow popubBeanWindow;
    private ArrayList<PopubBeanInfo>arrayList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popubview);
        textView= (TextView) findViewById(R.id.textView);
        for (int i=0;i<5;i++){
            PopubBeanInfo popubBeanInfo=new PopubBeanInfo();
            popubBeanInfo.contentTitle="Hello"+i;
            if(i==0){
                popubBeanInfo.isCheck=true;
            }
            arrayList.add(popubBeanInfo);
        }
        popubBeanWindow=new PopubBeanWindow(this,arrayList);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               popubBeanWindow.show(v);
            }
        });

        popubBeanWindow.setOnItemClickListener(new PopOnItemClickListener() {
            @Override
            public void onItemClickListener(int position) {
                textView.setText(arrayList.get(position).contentTitle);
            }
        });
    }
}