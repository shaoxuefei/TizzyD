package com.shanlin.sxf;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.shanlin.sxf.bean.StepBean;
import com.shanlin.sxf.diyview.ScrollVelocityView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewScrollVelocityTrackerActivity extends AppCompatActivity implements ScrollVelocityView.OnStepCallBack {
    @BindView(R.id.scroll_velocity)
    ScrollVelocityView scrollVelocity;
    @BindView(R.id.tv_count)
    TextView tv_count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_scroll_velocity_tracker);
        ButterKnife.bind(this);
        List<StepBean> stepBeanList = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            StepBean stepBean = new StepBean();
            stepBean.stepCount = (float) (Math.random() * 100);
            stepBeanList.add(stepBean);
            if (i == 0) {
                tv_count.setText(String.format("value:  %s", String.valueOf((int) stepBean.stepCount)));
            }
        }
        scrollVelocity.setOnStepCallBack(this);
        scrollVelocity.setStepBeanList(stepBeanList);
    }

    @Override
    public void onStepValue(float value) {
        tv_count.setText(String.format("value:  %s", String.valueOf((int) value)));
    }
}
