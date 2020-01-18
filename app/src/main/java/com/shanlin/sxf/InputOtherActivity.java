package com.shanlin.sxf;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.shanlin.sxf.utils.AndroidBug5497Workaround;

public class InputOtherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_other);

        AndroidBug5497Workaround.assistActivity(this);

    }
}
