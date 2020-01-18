package com.shanlin.sxf.softkeybord;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.shanlin.sxf.R;
import com.shanlin.sxf.softkeybord.SoftInputKeyBordView;

public class KeySoftShowDialog extends DialogFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        SoftInputKeyBordView softInputKeyBordView = new SoftInputKeyBordView(inflater.getContext(), null);
        initView();
        return softInputKeyBordView;
    }

    private void initView() {
        setCancelable(false);

        Window window = getDialog().getWindow();
        window.setBackgroundDrawableResource(R.color.transparent);
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.gravity = Gravity.BOTTOM;
        window.setAttributes(attributes);
    }
}
