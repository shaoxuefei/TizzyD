package com.shanlin.sxf.softkeybord;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.shanlin.sxf.InputOtherActivity;
import com.shanlin.sxf.MyWebViewActivity;
import com.shanlin.sxf.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SoftInputChangeActivity extends AppCompatActivity {

    @BindView(R.id.btn_default)
    Button btnDefault;
    @BindView(R.id.btn_wx)
    Button btnWx;
    @BindView(R.id.btn_popub_window)
    Button btn_popub_window;
    @BindView(R.id.rootLinear)
    RelativeLayout rootLinear;
    @BindView(R.id.softInputKey)
    SoftInputKeyBordView softInputKeyBordView;
    @BindView(R.id.softInputNormalKey)
    KeySoftBordNormalView keySoftBordNormalView;
    @BindView(R.id.editText2)
    EditText editText2;
    @BindView(R.id.editText1)
    EditText editText1;
    @BindView(R.id.recyclerview_utils)
    Button recyclerview_utils;
    @BindView(R.id.btn_other_activity)
    Button btn_other_activity;

    KeySoftPopWindow softPopWindow;
    KeySoftShowDialog keySoftShowDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soft_input_change);
        ButterKnife.bind(this);
        keySoftBordNormalView.setBaseActivity(this);

        editText1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    keySoftBordNormalView.showSoftInputView(false, editText1);
                }
                return false;
            }
        });

        editText2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    keySoftBordNormalView.showSoftInputView(false, editText2);
                }
                return false;
            }
        });
    }

    @OnClick({R.id.btn_default, R.id.btn_wx, R.id.btn_popub_window, R.id.btn_utils, R.id.recyclerview_utils, R.id.btn_other_activity,R.id.btn_webView})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_default:
                Intent intent = new Intent(SoftInputChangeActivity.this, InputWxEmojActivity.class);
                intent.putExtra("isDefault", 1);
                startActivity(intent);
                break;
            case R.id.btn_wx:
                Intent intent1 = new Intent(SoftInputChangeActivity.this, InputWxEmojActivity.class);
                intent1.putExtra("isDefault", 2);
                startActivity(intent1);
                break;
            case R.id.btn_popub_window:
//                if (softPopWindow == null) {
//                    softPopWindow = new KeySoftPopWindow(this, null);
//                }
//                softPopWindow.showAtLocation(rootLinear, Gravity.BOTTOM, 0, 0);

//                if (keySoftShowDialog == null) {
//                    keySoftShowDialog = new KeySoftShowDialog();
//                }
//                keySoftShowDialog.show(getFragmentManager(), "KeySoftShowDialog");

                softInputKeyBordView.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_utils:
                keySoftBordNormalView.showSoftInputView(true, null);
                break;
            case R.id.recyclerview_utils:
                Intent intent2 = new Intent(SoftInputChangeActivity.this, InputWxEmojActivity.class);
                intent2.putExtra("isDefault", 3);
                startActivity(intent2);
                break;
            case R.id.btn_other_activity:
                Intent intent3 = new Intent(SoftInputChangeActivity.this, InputOtherActivity.class);
                startActivity(intent3);
                break;
            case R.id.btn_webView:
                Intent intent4 = new Intent(SoftInputChangeActivity.this, MyWebViewActivity.class);
                startActivity(intent4);
                break;
        }
    }
}
