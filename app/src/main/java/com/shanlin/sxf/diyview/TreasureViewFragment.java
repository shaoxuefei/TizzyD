package com.shanlin.sxf.diyview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.shanlin.sxf.BaseFragment;
import com.shanlin.sxf.R;
import com.shanlin.sxf.canvas.YuanBingView;

import butterknife.BindView;

/**
 * Created by Sxf on 2017/5/16.
 *
 * @project: Demo.
 * @detail:
 */
public class TreasureViewFragment extends BaseFragment {

    @BindView(R.id.yuanBing)
    YuanBingView yuanBing;
    @BindView(R.id.yuanBiao)
    YuanBingView yuanBiao;

    @Override
    public int getLayoutId() {
        Log.e("aa", "TreasureViewFragment----onCreateView");
        return R.layout.treasure_view_activity;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e("aa", "TreasureViewFragment-----onViewCreated");
    }

    @Override
    public void initView(View inflate) {
        initView();
    }


    public static TreasureViewFragment newInstance() {

        Bundle args = new Bundle();

        TreasureViewFragment fragment = new TreasureViewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private void initView() {
        yuanBing.setAnima(true);
        yuanBiao.setAnima(false);
        yuanBiao.setTable(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e("aa", "TreasureViewFragment-----onDestroyView");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("aa", "TreasureViewFragment-----onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("aa", "TreasureViewFragment-----onPause");
    }

    @Override
    public void fragmentIsVisible(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            Toast.makeText(getContext(), "TreasureViewFragment--LoadData", Toast.LENGTH_SHORT).show();
            Log.e("aa", "TreasureViewFragment-----isShow");
        } else {
            Log.e("aa", "TreasureViewFragment-----isHide");
        }
    }
}