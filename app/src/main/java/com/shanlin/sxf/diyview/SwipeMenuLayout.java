package com.shanlin.sxf.diyview;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.support.v4.widget.ScrollerCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.shanlin.sxf.R;

/**
 * @author : SXF
 * @ date   : 2018/5/2
 * Description :
 */

public class SwipeMenuLayout extends LinearLayout {
    View contentView, menuView;
//    ScrollerCompat scrollerCompat;


    public View getContentView() {
        return contentView;
    }

    public void setContentView(View contentView) {
        this.contentView = contentView;
    }

    public View getMenuView() {
        return menuView;
    }

    public void setMenuView(View menuView) {
        this.menuView = menuView;
    }

    public SwipeMenuLayout(Context context) {
        super(context);
        initView();
    }

    public SwipeMenuLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

    }

    //本身View是不支持  滑动的  不会走move事件
    float startX = 0, startY = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = event.getX();
                startY = event.getY();
                Log.e("aa", "dexItem---down");
                break;
            case MotionEvent.ACTION_MOVE:
                float dexX = event.getX() - startX;
                float dexY = event.getY() - startY;
                Log.e("aa", "dexItem---Move" + dexX);
                if (Math.abs(dexX) > 0) {
                    swif((int) dexX);
                }
                startX = event.getX();
                startY = event.getY();
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                Log.e("aa", "dexItem---UP");
                break;
        }
        return super.onTouchEvent(event);
    }

    public void swif(int dis) {
        if (dis > 0) {
            //向右滑
            if (contentView != null) {
                if (contentView.getLeft() < 0) {
                    contentView.layout(contentView.getLeft() + dis, contentView.getTop(), contentView.getRight() + dis, contentView.getBottom());
                }
            }
        } else {
            //向左滑
            if (contentView != null) {
                int left = contentView.getLeft();
                Log.e("aa", "contentViewLeft" + left);
                if (contentView.getLeft() < 0 || contentView.getLeft() == 0) {
                    contentView.layout(contentView.getLeft() + dis, contentView.getTop(), contentView.getRight() + dis, contentView.getBottom());
                }
            }
        }
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        return super.dispatchTouchEvent(ev);
    }
}
