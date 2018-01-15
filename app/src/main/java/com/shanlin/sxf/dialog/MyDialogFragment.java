package com.shanlin.sxf.dialog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.shanlin.sxf.MainActivity;
import com.shanlin.sxf.R;
import com.tencent.stat.StatService;

import java.util.Properties;

/**
 * @Description:
 * @Auther: Sxf
 * @Date: 2017/11/29
 */

public class MyDialogFragment extends DialogFragment implements View.OnClickListener {
    private String key1, key2;
    private Context context;

    //该方法是Fragment的对应的内部方法--传递参数的方法
    public static MyDialogFragment newInstance() {

        Bundle args = new Bundle();

        MyDialogFragment fragment = new MyDialogFragment();
        args.putCharSequence("key1", "value1");
        args.putCharSequence("key2", "value2");
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        key1 = arguments.getString("key1");
        key2 = arguments.getString("key2");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.context = inflater.getContext();
        View view = inflater.inflate(R.layout.dialog_fragment_view, container, false);
        Toast.makeText(inflater.getContext(), key1 + key2, Toast.LENGTH_SHORT).show();
        //有时候这中LinearLayout的布局，，在自定View绘制时就不会显示全对其MatchParent的属性，而RelativeLayout的却可以
//        initLocation();
        initView(view);
        return view;
    }

    private void initLocation() {
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.gravity = Gravity.BOTTOM;
        attributes.width = WindowManager.LayoutParams.MATCH_PARENT;
        attributes.windowAnimations = R.style.dialogAnim;
        window.setAttributes(attributes);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        if (!isAdded() && !isVisible() && !isRemoving()) {
            FragmentTransaction ft = manager.beginTransaction();
            ft.add(this, tag);
            ft.commitAllowingStateLoss();
        }
    }

    //如果initLocation()方法在onCreate中声明的话，页面就如Dialog的样式一样，宽度不会充满全屏，而在onStart中声明的话，宽度会充满全屏
    @Override
    public void onStart() {
        super.onStart();
        initLocation();
    }

    private void initView(View view) {
        view.findViewById(R.id.tv_item1).setOnClickListener(this);
        view.findViewById(R.id.tv_item2).setOnClickListener(this);
        view.findViewById(R.id.tv_item3).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.tv_item1:
                Properties properties1 = new Properties();
                properties1.put("name", "法师");
                properties1.put("price", 200);
                properties1.put("playerLevel", 6);
                StatService.trackCustomKVEvent(context, "buy", properties1);
                dismiss();
                break;
            case R.id.tv_item2:
                Properties properties2 = new Properties();
                properties2.put("name", "战士");
                properties2.put("price", 300);
                properties2.put("playerLevel", 14);
                StatService.trackCustomKVEvent(context, "buy", properties2);
                dismiss();
                break;
            case R.id.tv_item3:
                Properties properties3 = new Properties();
                properties3.put("name", "射手");
                properties3.put("price", 100);
                properties3.put("playerLevel", 2);
                StatService.trackCustomKVEvent(context, "buy", properties3);
                dismiss();
                break;
            default:
                break;
        }
    }
}
