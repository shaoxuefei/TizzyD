package com.shanlin.sxf;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * @author : SXF
 * @ date   : 2019/3/5
 * Description : 引导页
 */
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initView();
    }

    private void initView() {
        Intent intent = new Intent(this, MainUIActivity.class);
        startActivity(intent);
    }
}
