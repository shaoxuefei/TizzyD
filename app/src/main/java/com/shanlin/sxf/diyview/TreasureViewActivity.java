package com.shanlin.sxf.diyview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.shanlin.sxf.R;

/**
 * Created by Sxf on 2017/5/16.
 *
 * @project: Demo.
 * @detail:
 */
public class TreasureViewActivity extends AppCompatActivity {
    private TreasureLineView treasureView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.treasure_view_activity);
        treasureView= (TreasureLineView) findViewById(R.id.treasureView);
        String []strings=new String[]{"黄带的骄傲","打啊但大家带带马达","但大家带带马达","达到","大大大大大"};
        for (int i=0;i<5;i++){
            TreasureChildView childView=new TreasureChildView(this,strings[i]);
            treasureView.addView(childView);
        }
    }
}