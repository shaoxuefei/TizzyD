package com.shanlin.sxf;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * @author : SXF
 * @ date   : 2018/8/28
 * Description :
 */

public abstract class BaseFragment extends Fragment {

    boolean isViewCreated = false;
    boolean isViewVisible = false;
    boolean isFirstCreate = true;

    //预加载会调用执行
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(getLayoutId(), container, false);
        ButterKnife.bind(this, inflate);
        initView(inflate);
        return inflate;
    }

    //
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isViewCreated = true;
        isFirstCreate = true;
        doLazyLoad();
    }

    /**
     * 返回True  表示当前View是否加载到窗口、就是是否是当前窗口展示
     *
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            //该方法有可能会调用多次、包括有可能View还没创建就进行调用、、、防止重复加载、防止View还没创建就进行View的调用操作
            isViewVisible = true;
            doLazyLoad();
        } else {
            isViewVisible = false;
        }
    }

    //需要区分是否是第一次加载、防止每次切换都进行数据的调用  更新
    private void doLazyLoad() {
        if (isViewCreated && isViewVisible) {
            if (isFirstCreate) {
                fragmentIsVisible(true);
                isFirstCreate = false;
            }
        } else {
            fragmentIsVisible(false);
        }
    }

    public abstract int getLayoutId();

    public abstract void initView(View inflate);

    public abstract void fragmentIsVisible(boolean isVisibleToUser);
}


