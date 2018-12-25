package com.shanlin.sxf.guestor;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.RelativeLayout;


/**
 * @author : SXF
 * @ date   : 2018/11/28
 * Description :
 */
public class MoveEvnentReleative extends RelativeLayout {
    int touchSlop;
    GestureDetector gestureDetector;
    static final int MOVE_DEX = 150;

    public MoveEvnentReleative(Context context) {
        super(context);
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public MoveEvnentReleative(Context context, AttributeSet attrs) {
        super(context, attrs);
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        gestureDetector = new GestureDetector(context, new OnGestureDetectorListener());
    }


    float moveAbsX, moveAbsY;
    float startX;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        gestureDetector.onTouchEvent(ev);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                moveAbsX = ev.getX();
                moveAbsY = ev.getY();
                startX = ev.getX();
                return false;
            case MotionEvent.ACTION_MOVE:
                //防止内部事件多指触碰时，(当起一个手指时就会先走)ACTION_POINTER_UP执行会先于ACTION_UP方法这时进行手势归还到该层View、当在两个事件之间时由于
                // 内部K线View不进行拦截\当迅速起手指时会有Move件走到ACTION_MOVE事件
                if (TOUCH_MODE == SCROLL_MOVE && ev.getPointerCount() == 1) {
                    float abs = Math.abs(ev.getX() - moveAbsX);
                    float absY = Math.abs(ev.getY() - moveAbsY);
                    Log.e("aa", "dexX---->" + abs + "------>dexY" + absY);
                    if (abs > MOVE_DEX && abs > absY) {
                        return true;
                    } else {
                        return false;
                    }
                }
                return false;
            default:
                return false;
        }
    }

    boolean isStart = false;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_POINTER_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                float dexStartX = event.getX() - startX;
                Log.e("aa", "startX---->" + startX + "eventX---->" + event.getX());
                if (Math.abs(dexStartX) > MOVE_DEX && !isStart) {
                    //TODO 启动Activity
                    if (dexStartX > 0) {
//                        ((Activity) getContext()).overridePendingTransition(R.anim.left_in, R.anim.animation_no);
                        Log.e("aa", "右边滑动,左边启动");
                    } else {
//                        ((Activity) getContext()).overridePendingTransition(R.anim.right_in, R.anim.animation_no);
                        Log.e("aa", "左边滑动,右边启动");
                    }
                    ((Activity) getContext()).finish();
                }
                break;
        }
        return true;
    }

    private int TOUCH_MODE;
    private static final int FLYING = 0x11;
    private static final int LONG_PRESS = 0x21;
    private static final int SCROLL_MOVE = 0x31;

    class OnGestureDetectorListener implements GestureDetector.OnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            TOUCH_MODE = SCROLL_MOVE;
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            TOUCH_MODE = LONG_PRESS;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            TOUCH_MODE = FLYING;
            return false;
        }
    }
}
