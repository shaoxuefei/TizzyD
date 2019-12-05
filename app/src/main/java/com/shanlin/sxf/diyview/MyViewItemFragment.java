package com.shanlin.sxf.diyview;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.shanlin.sxf.BaseFragment;
import com.shanlin.sxf.MyCameraActivity;
import com.shanlin.sxf.R;

public class MyViewItemFragment extends BaseFragment {
    private MyViewGroup myViewGroup;
    private String[] strings = new String[]{"我的得", "标签大动静", "的骄傲收到都跑", "大连实德", "的撒旦", "审查判断克拉克打开的", "啊是独立判断拉票的撒旦", "表打击哦啊都怕", "我的哦", "但是扩大"};
    private MyView myView;

    @Override
    public int getLayoutId() {
        Log.e("aa", "MyViewItemFragment----onCreateView");
        return R.layout.activity_my_view_item;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e("aa", "MyViewItemFragment-----onViewCreated");
    }

    public static MyViewItemFragment newInstance() {

        Bundle args = new Bundle();

        MyViewItemFragment fragment = new MyViewItemFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initView(View inflate) {
        myViewGroup = (MyViewGroup) inflate.findViewById(R.id.myViewGroup);
        for (int i = 0; i < 10; i++) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.group_item_view, null);
            TextView tvTitle = (TextView) view.findViewById(R.id.title);
            tvTitle.setText(strings[i]);
            myViewGroup.addView(view);
        }

        myViewGroup.setCallBack(new ItemClickCallBack() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(getContext(), strings[position], Toast.LENGTH_SHORT).show();
            }
        });

        myView = (MyView) inflate.findViewById(R.id.myView);
        myView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MyCameraActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e("aa", "MyViewItemFragment-----onDestroyView");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("aa", "MyViewItemFragment-----onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("aa", "MyViewItemFragment-----onPause");
    }


    @Override
    public void fragmentIsVisible(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            Toast.makeText(getContext(), "MyViewItemFragment--LoadData", Toast.LENGTH_SHORT).show();
            Log.e("aa", "MyViewItemFragment-----isShow");
        } else {
            Log.e("aa", "MyViewItemFragment-----isHide");
        }
    }
}
