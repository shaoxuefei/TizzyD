package com.shanlin.sxf.diytouch;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * @author : SXF
 * @ date   : 2018/3/23
 * e-mail  : shaoxf@go-goal.com
 * desc    :
 */

public class MyExtraLinearLayout extends RelativeLayout {

    public MyExtraLinearLayout(Context context) {
        super(context);
    }

    public MyExtraLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 先执行
     *
     * @param ev
     * @return MyLinearLayout---onInterceptTouchEvent-----false
     * MyLinearLayout---onTouchEvent-----false
     * MyLinearLayout---dispatchTouchEvent-----false----可以看出其dispatchTouchEvent方法也是根据onInterceptTouchEvent和onTouchEvent来判断；有一个返回true-
     * <p>
     * <p>
     * MyLinearLayout---onInterceptTouchEvent-----false
     * MyLinearLayout---onTouchEvent-----false
     */


    //ViewGroup 中的dispatchTouchEvent  其实感觉没啥用，内部还是调用的onInterceptTouchEvent
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.e("aa", ">>>>>>>>>>>>>>MyExtraLinearLayout----dispatchTouchEvent");
        return false;
    }

    /**
     * 后执行
     *
     * @param ev
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.e("aa", ">>>>>>>>>>>>>>MyExtraLinearLayout----onInterceptTouchEvent");
        return false;
    }

    /**
     * 最后执行--入果拦截事件--那么就在最后执行；
     * 入果没有拦截事件---就在子View的onTouchEvent中执行--入果返回true，，那么就不会执行该OnTOuchEvent；入果返回false--会向上执行该TouchEvent
     * 而dispatchEvent()是从上往下执行顺序----------所以---onTouchEvent是从下往上执行顺序
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e("aa", ">>>>>>>>>>>>>>MyExtraLinearLayout----onTouchEvent");
        return true;
    }
}
