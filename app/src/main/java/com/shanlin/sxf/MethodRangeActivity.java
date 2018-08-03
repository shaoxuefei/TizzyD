package com.shanlin.sxf;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.shanlin.sxf.bean.MethodBean;
import com.shanlin.sxf.presenter.MethodRangePresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MethodRangeActivity extends AppCompatActivity implements MethodRangePresenter.OnMethodTestCallBack {

    @BindView(R.id.tv_btn)
    Button tvBtn;
    @BindView(R.id.tv_add)
    Button tvAdd;

    MethodRangePresenter rangePresenter;

    List<MethodBean> resourceList;
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_method_range);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        rangePresenter = new MethodRangePresenter(this, this);
        resourceList = new ArrayList<>();
    }


    @Override
    public void onCallBack(List<MethodBean> dataList) {
        int size = dataList.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                MethodBean methodBean = dataList.get(i);
                String textNum = methodBean.textNum;
                Log.e("aa", "size==" + size + "---textNum:" + textNum);
            }
        }
    }

    @OnClick({R.id.tv_btn, R.id.tv_add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_btn:
                rangePresenter.requestMethodData(resourceList);
                break;
            case R.id.tv_add:
                MethodBean methodBean = new MethodBean();
                i++;
                methodBean.textNum = i + "";
                resourceList.add(methodBean);
                break;
        }
    }
}
