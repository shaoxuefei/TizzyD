package com.shanlin.sxf.diyview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 * @author : SXF
 * @ date   : 2018/5/2
 * Description :
 */

public class SwipeMenuListView extends RecyclerView {

    public SwipeMenuLayout swipeMenuLayout;

    public LinearLayoutManager linearLayoutManager;

    public SwipeMenuListView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
    }

    @Override
    public void setLayoutManager(LayoutManager layout) {
        super.setLayoutManager(layout);
        if (layout instanceof LinearLayoutManager) {
            linearLayoutManager = (LinearLayoutManager) layout;
        }
    }

    public void setSwipeMenuLayout(SwipeMenuLayout swipeMenuLayout) {
        this.swipeMenuLayout = swipeMenuLayout;
    }

    float startX = 0, startY = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = event.getX();
                startY = event.getY();
                Log.e("aa", "dexListView---Down");
                break;
            case MotionEvent.ACTION_MOVE:
                float dexX = event.getX() - startX;
                float dexY = event.getY() - startY;
                //TODO:根据点击位置回去position的位置
                

                if (Math.abs(dexX) > 0) {
                    if (swipeMenuLayout != null) {
                        swipeMenuLayout.swif((int) dexX);
                    }
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                Log.e("aa", "dexListView---UP");
                break;
        }
        return super.onTouchEvent(event);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {

        return super.onInterceptTouchEvent(e);
    }

}
