package com.shanlin.sxf.dialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.shanlin.sxf.R;
import com.shanlin.sxf.softkeybord.NewSoftInputKeyBord;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SoftInputDialog extends DialogFragment {

    NewSoftInputKeyBord softInputKeybord;

    public static SoftInputDialog newInstance() {

        Bundle args = new Bundle();

        SoftInputDialog fragment = new SoftInputDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.soft_input_dialog, container, true);
        softInputKeybord=inflate.findViewById(R.id.softInputKeybord);
        softInputKeybord.setBaseActivity((AppCompatActivity) getActivity());
        softInputKeybord.showSoftKeyBord();
        return inflate;
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        if (!isAdded() && !isVisible() && !isRemoving()) {
            FragmentTransaction ft = manager.beginTransaction();
            ft.add(this, tag);
            ft.commitAllowingStateLoss();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        initLocation();
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
}
