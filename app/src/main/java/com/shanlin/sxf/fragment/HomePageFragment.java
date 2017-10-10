package com.shanlin.sxf.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shanlin.sxf.R;

/**
 * @Description: Fragment生命周期:11--Activity ：6
 * @Auther: Sxf
 */

public class HomePageFragment extends Fragment {
    public TextView tvHomePage;
    Fragment homeFragment;
    //onCreate-Fragment和Activity发生关联
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }
    //onCreate-
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    //onCreate-Fragment 开始创建视图
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.mhome_page_fragment, container,false);
        tvHomePage= (TextView) inflate.findViewById(R.id.tvHomePage);
        final TalkFragment talkFragment=new TalkFragment();

        tvHomePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                //通过Tag来获取对应的Fragment
                homeFragment = fragmentManager.findFragmentByTag("Home");
                FragmentTransaction fragmentTransaction =fragmentManager.beginTransaction();
                fragmentTransaction.hide(homeFragment);
                fragmentTransaction.add(R.id.mFrameLayout,talkFragment,"Talk");//(不用主动添加addToBackStack)这种需要在Fragment中加背景，，因为没有hide和show，不加背景会把backStack中的页面显示出
//                fragmentTransaction.replace(R.id.mFrameLayout,talkFragment,"Talk");//这个是直接remove掉，不会保留原来的页面数据
                fragmentTransaction.addToBackStack(null);//加入后退栈--对于add()---其不移除对应的页面消息，对于replace其移除对面的页面信息---但两种方式都是将开启过得Fragment保存到BackStack中，只不过add()的方式是跟Activity的后退栈差不多，而replace的后退栈是重新渲染对应的Fragment
                fragmentTransaction.commit();
            }
        });
        return inflate;
    }
    //onCreate-Activity开始创建
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        //在OnResume中--对hide()的进行show()???--加入后退栈的不需要show，他会自动帮你show()--对于非回退栈中的hide()需要show();
//        getFragmentManager().beginTransaction().show(homeFragment);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
