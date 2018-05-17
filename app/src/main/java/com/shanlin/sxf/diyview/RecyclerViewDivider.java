package com.shanlin.sxf.diyview;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.shanlin.sxf.R;

/**
 * @author : SXF
 * @ date   : 2018/5/2
 * Description :
 */

public class RecyclerViewDivider extends RecyclerView.ItemDecoration {

    private int dividerHeight = 3;

    private Paint paint;

    /**
     * @param outRect 该item
     * @param view
     * @param parent
     * @param state
     */
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        //设置该ItemView和下一个Item之间的间距---也就是divider的填充空间
        outRect.bottom = 3;//px

        paint = new Paint();
        paint.setColor(0xFFFF0000);
        paint.setStrokeWidth(2);
        paint.setAntiAlias(true);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        int childCount = parent.getChildCount();

        for (int i = 0; i < childCount; i++) {
            View childAt = parent.getChildAt(i);
            c.drawLine(childAt.getLeft(), childAt.getBottom() + dividerHeight, childAt.getRight(), childAt.getBottom() + dividerHeight, paint);
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
    }
}
