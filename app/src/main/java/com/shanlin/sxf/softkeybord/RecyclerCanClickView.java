package com.shanlin.sxf.softkeybord;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class RecyclerCanClickView extends RecyclerView {
    private OnRecyclerViewClickListener onRecyclerViewClickListener;


    public void setOnRecyclerViewClickListener(OnRecyclerViewClickListener onRecyclerViewClickListener) {
        this.onRecyclerViewClickListener = onRecyclerViewClickListener;
    }

    public RecyclerCanClickView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {

        return super.onInterceptTouchEvent(e);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (e.getAction() == MotionEvent.ACTION_UP) {
            if (onRecyclerViewClickListener != null) {
                onRecyclerViewClickListener.onViewClick();
            }
        }
        int action = e.getAction();
        switch (action) {

            case MotionEvent.ACTION_DOWN:
                break;

            case MotionEvent.ACTION_UP:

                break;
        }

        return super.onTouchEvent(e);
    }

    public interface OnRecyclerViewClickListener {

        void onViewClick();
    }
}
