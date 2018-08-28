package com.shanlin.sxf;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author : SXF
 * @ date   : 2018/8/28
 * Description :
 */

public class ViewPagerFlipperActivity extends AppCompatActivity {

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpager_flipper);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {

    }
}
