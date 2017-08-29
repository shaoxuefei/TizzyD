package com.shanlin.sxf;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * Created by Sxf on 2017/5/15.
 *
 * @project: Demo.
 * @detail:
 */
public class SeekBarActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {
    private SeekBar seekBar;
    private TextView tv_Progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seekbarlayout);
        initView();
    }

    private void initView(){
        seekBar= (SeekBar) findViewById(R.id.seekBar);
        tv_Progress= (TextView) findViewById(R.id.progressTxt);
        seekBar.setOnSeekBarChangeListener(this);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        tv_Progress.setText(progress+"万元");
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}