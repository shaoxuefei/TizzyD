package com.shanlin.sxf.diyview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.shanlin.sxf.R;
import com.shanlin.sxf.canvas.YuanBingView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Sxf on 2017/5/16.
 *
 * @project: Demo.
 * @detail:
 */
public class TreasureViewActivity extends AppCompatActivity {

    @BindView(R.id.yuanBing)
    YuanBingView yuanBing;
    @BindView(R.id.yuanBiao)
    YuanBingView yuanBiao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.treasure_view_activity);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        yuanBing.setAnima(true);
        yuanBiao.setAnima(false);
        yuanBiao.setTable(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}