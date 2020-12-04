package com.shanlin.sxf;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;

import com.shanlin.sxf.diyview.ItemClickCallBack;
import com.shanlin.sxf.diyview.MyGroupItemView;
import com.shanlin.sxf.diyview.MyViewGroup;
import com.shanlin.sxf.enums.EnumContast;

public class LuckyYearActivity extends AppCompatActivity {
    private MyViewGroup myViewGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lucky_year);
        myViewGroup = findViewById(R.id.my_view_group);
        initViewGroup();
    }

    private void initViewGroup() {
        myViewGroup.addView(new MyGroupItemView(this, EnumContast.valueOf("ONE").getValueName()));
        myViewGroup.setCallBack(new ItemClickCallBack() {
            @Override
            public void onItemClick(int position) {
                if (position == 0) {
                    Intent intent = new Intent(LuckyYearActivity.this, ThreadFactoryActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}