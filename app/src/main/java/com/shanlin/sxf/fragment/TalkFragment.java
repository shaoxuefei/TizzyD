package com.shanlin.sxf.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shanlin.sxf.R;

/**
 * @Description:
 * @Auther: Sxf
 * @Date: 2017/10/10
 */

public class TalkFragment extends Fragment {

    public TextView tvTalk,tvPopStack;
    Fragment talkFragment;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.talk_fragment, container,false);
        tvTalk= (TextView) inflate.findViewById(R.id.tvTalk);
        tvPopStack= (TextView) inflate.findViewById(R.id.tvPopStack);
        tvPopStack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.popBackStack();//自动回退，类似于Back-返回键-操作

            }
        });
        final PersonFragment personFragment=new PersonFragment();
        tvTalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                talkFragment = fragmentManager.findFragmentByTag("Talk");
                FragmentTransaction fragmentTransaction =fragmentManager.beginTransaction();
                fragmentTransaction.hide(talkFragment);
                fragmentTransaction.add(R.id.mFrameLayout,personFragment,"Person");
//                fragmentTransaction.replace(R.id.mFrameLayout,personFragment,"Person");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        return inflate;
    }

    @Override
    public void onResume() {
        super.onResume();
//        getFragmentManager().beginTransaction().show(talkFragment);
    }
}
