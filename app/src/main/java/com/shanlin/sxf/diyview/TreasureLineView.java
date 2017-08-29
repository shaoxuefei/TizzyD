package com.shanlin.sxf.diyview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Sxf on 2017/5/16.
 *
 * @project: Demo.
 * @detail:
 */

public class TreasureLineView extends ViewGroup {

    private int groupWidth;
    final int MARGIN = 20;//左右上下间距

    public TreasureLineView(Context context) {
        super(context);
    }

    public TreasureLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int childCount = getChildCount();
        int widthTotal = 0;
        int line = 1;
        for (int i = 0; i < childCount; i++) {
            View childAtView = getChildAt(i);
            measureChild(childAtView, MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);//开始测量子View的大小--MeasureSpec.UNSPECIFIED不限制其大小，从而使child.getMeasureWidth()有对应的值
            int width = childAtView.getMeasuredWidth() + childAtView.getPaddingLeft() + childAtView.getPaddingRight() + MARGIN;
            groupWidth = MeasureSpec.getSize(widthMeasureSpec);//获取该Group的总宽度
            if (widthTotal + width > groupWidth) {
                line++;
                if (width > widthMeasureSpec) {
                    widthTotal = 0;
                } else {
                    widthTotal = width;
                }
            } else {
                widthTotal += width;
            }
        }
        View childAt = getChildAt(0);
        int viewHeight = childAt.getMeasuredHeight() + childAt.getPaddingTop() + getPaddingBottom() + MARGIN;
        setMeasuredDimension(groupWidth, line * viewHeight);//设置父View的宽高
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        int totalMeasureWidth = 0;
        int line = 0;
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            int measuredWidth = childAt.getMeasuredWidth() + MARGIN;
            if (totalMeasureWidth + measuredWidth > groupWidth) {
                line++;
                if (measuredWidth > getMeasuredWidth()) {
                    totalMeasureWidth = 0;
                } else {
                    totalMeasureWidth = measuredWidth;
                }
                int paddingLeft = childAt.getPaddingLeft();
                int measuredWi = childAt.getMeasuredWidth();
                int paddingRight = childAt.getPaddingRight();
                int top = line * (childAt.getMeasuredHeight() + childAt.getPaddingTop()) + MARGIN;
                //绘制子View的位置
                childAt.layout(paddingLeft, top, paddingLeft + measuredWi + paddingRight, top + childAt.getMeasuredHeight() + childAt.getPaddingBottom() + MARGIN);
            } else {
                int top = line * ((childAt.getMeasuredHeight() + childAt.getPaddingTop()) + MARGIN);
                childAt.layout(totalMeasureWidth, top, totalMeasureWidth + childAt.getMeasuredWidth(), top + childAt.getMeasuredHeight() + MARGIN);
                totalMeasureWidth += measuredWidth;
            }
        }

    }

}
