package com.shanlin.sxf.itemtouch;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.ViewGroup;

import com.shanlin.sxf.RecyclerViewDecorationActivity;

/**
 * @author : SXF
 * @ date   : 2018/6/28
 * Description : ItemTouchCallBack---ItemTouchHelper v7包工具类配合RecyclerView的Item滑动时候的Item默认动画处理---extends RecyclerView.ItemDecoration
 */

public class MyDragItemTouchHelperCallBack extends ItemTouchHelper.Callback {

    RecyclerViewDecorationActivity.ScrollMyAdapter scrollMyAdapter;

    public MyDragItemTouchHelperCallBack(RecyclerViewDecorationActivity.ScrollMyAdapter scrollMyAdapter) {
        this.scrollMyAdapter = scrollMyAdapter;
    }

    /**
     * 设置Item的滑动属性、左右或者是长按拖动
     *
     * @param recyclerView
     * @param viewHolder
     * @return
     */
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = 0;
        int swipeFlags = 0;
        dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        swipeFlags = ItemTouchHelper.LEFT;//只支持左滑-也就是只支持左滑删除
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    //---长按拖动后的移动回调
    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        scrollMyAdapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    //滑动删除后的回调
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        scrollMyAdapter.onItemDissmiss(viewHolder.getAdapterPosition());
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        ViewGroup itemView = (ViewGroup) viewHolder.itemView;
        int width = itemView.getChildAt(1).getWidth();
//        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
//            if (Math.abs(dX) < width) {
//                itemView.scrollTo((int) -dX, 0);
//            } else if (Math.abs(dX) <= recyclerView.getWidth() / 2) {
//
//            }
//        } else {
//            //父类方法是直接删除的所以侧滑删除不走父类方法
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
//        }
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
//        viewHolder.itemView.setScrollX(0);
    }
}
