package com.shanlin.sxf.diyview;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * @author : SXF
 * @ date   : 2018/8/28
 * Description :
 */

public class NoScrollViewPager extends ViewPager {

    public NoScrollViewPager(Context context) {
        super(context);
    }

    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //不进行事件拦截,将事件不拦截向下传递、super.onIterceptTouchEvent()并不是真的直接拦截、而是根据内部的操作；手势来动态返回的
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        //返回false,当底部View没有响应onTouchEvent的时候，往上传，会执行该ViewPager的onTouchEvent方法，直接返回false不响应(不滑动)，再往上传
        return false;
    }
}
