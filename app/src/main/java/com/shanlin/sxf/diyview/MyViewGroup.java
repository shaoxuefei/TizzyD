package com.shanlin.sxf.diyview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.shanlin.sxf.R;

/**
 * @Description: AutoLabel:缺少可点击样式的修改
 * @Auther: Sxf
 * @Date: 2017/11/13
 */

public class MyViewGroup extends ViewGroup implements ItemClickCallBack {
    int honearSpace, verticalSpace;
    int lastPosition = 0;

    public MyViewGroup(Context context) {
        this(context, null);
    }

    public MyViewGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        float density = context.getResources().getDisplayMetrics().density;
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MyViewGroup, defStyleAttr, 0);
        honearSpace = typedArray.getDimensionPixelSize(R.styleable.MyViewGroup_horna, (int) (density * 10));
        verticalSpace = typedArray.getDimensionPixelSize(R.styleable.MyViewGroup_vertica, (int) (density * 10));
    }

    /**
     * 用于测量该ViewGroup的宽高(根据该ViewGroup的子View的宽高)
     *
     * @param widthMeasureSpec  表示的是父布局建议该ViewGroup的宽高
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width = 0, height = 0;
        //对所有的child进行测量---measureChild()是对单独的child进行测量
        measureChildren(widthMeasureSpec, heightMeasureSpec);//测量child的大小--其实跟自定义View/或者是上边的viewGroup差不多，内部也是做了对其模式和size的判断测量,只不过是ViewGroup对其进行了封装而已;
        switch (widthMode) {
            case MeasureSpec.EXACTLY:
                width = widthSize;
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                //根据child的数量进行设置ViewGroup的宽
                int childCount = getChildCount();
                int line = 1;
                int honWidth = 0;
                int measuredHeights = 0;
                for (int i = 0; i < childCount; i++) {
                    View childAt = getChildAt(i);//怎样获取子view的宽高：可以通过getMeasureHeight(),getMeasureHeight();要想获取子view的margin,padding,可以通过对应的LayoutParams,前提是需要根据规则测量过measureChildren()
                    int measuredHeight = childAt.getMeasuredHeight();
                    int measuredWidth = childAt.getMeasuredWidth();
                    measuredHeights = measuredHeight;
                    honWidth = honWidth + measuredWidth + honearSpace;
                    //设置ViewGroup的Padding--margin的话就不用加了，因为本身都不在ViewGroup的宽高测量中
                    if (honWidth + getPaddingLeft() + getPaddingRight() > widthSize) {
                        line++;
                        honWidth = childAt.getMeasuredWidth() + honearSpace;
                        height = measuredHeight * line + (line - 1) * verticalSpace + getPaddingTop() + getPaddingBottom();
                    }
                    if (i == lastPosition) {
                        childAt.findViewById(R.id.title).setSelected(true);
                    } else {
                        childAt.findViewById(R.id.title).setSelected(false);
                    }
//                    //设置Item的点击事件
                    final int finalI1 = i;
                    childAt.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onItemClick(finalI1);
                        }
                    });

                }
                if (line == 1) {
                    height = measuredHeights;
                    width = honWidth;
                } else {
                    width = widthSize;
                }
                break;
        }
        switch (heightMode) {
            case MeasureSpec.EXACTLY:
                height = heightSize;
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                //根据child的数量进行设置ViewGroup的宽
                int childCount = getChildCount();
                int line = 1;
                int honWidth = 0;
                int measuredHeights = 0;
                for (int i = 0; i < childCount; i++) {
                    View childAt = getChildAt(i);//怎样获取子view的宽高：可以通过getMeasureHeight(),getMeasureHeight();要想获取子view的margin,padding,可以通过对应的LayoutParams,前提是需要根据规则测量过measureChildren()
                    int measuredHeight = childAt.getMeasuredHeight();
                    int measuredWidth = childAt.getMeasuredWidth();
                    measuredHeights = measuredHeight;
                    honWidth = honWidth + measuredWidth + honearSpace;
                    //设置ViewGroup的Padding--margin的话就不用加了，因为本身都不在ViewGroup的宽高测量中
                    if (honWidth + getPaddingLeft() + getPaddingRight() > widthSize) {
                        line++;
                        honWidth = childAt.getMeasuredWidth() + honearSpace;
                        height = measuredHeight * line + (line - 1) * verticalSpace + getPaddingTop() + getPaddingBottom();
                    }
                    if (i == lastPosition) {
                        childAt.findViewById(R.id.title).setSelected(true);
                    } else {
                        childAt.findViewById(R.id.title).setSelected(false);
                    }
//                    //设置Item的点击事件
                    final int finalI1 = i;
                    childAt.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onItemClick(finalI1);
                        }
                    });

                }
                if (line == 1) {
                    height = measuredHeights;
                }
                break;
        }
        setMeasuredDimension(width, height);//是ViewGroup在onMeasure中进行该ViewGroup的测量
    }

    /**
     * onLayout()用于放置子Child的位置
     *
     * @param changed 改viewGroup是否发生了变化
     * @param l       表示的是viewgroup相对于其父布局的距离
     * @param t       其实感觉没啥用这些参数，跟子View的摆放没啥关系,除非你需要把View放在ViewGroup的固定位置,yeah,It's right!
     * @param r
     * @param b
     */

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {


        //存放childView的位置
        int childCount = getChildCount();
        int viewGroupWidth = getMeasuredWidth();
        int line = 0;
        int width = 0, bottom, top, left, right;
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            left = getPaddingLeft() + width;
            width = width + childAt.getMeasuredWidth() + honearSpace;
            //设置ViewGroup的Padding
            if (width + getPaddingLeft() + getPaddingRight() > viewGroupWidth) {
                line++;
                left = getPaddingLeft();
                width = childAt.getMeasuredWidth() + honearSpace;
            }
            top = getPaddingTop() + line * (childAt.getMeasuredHeight() + verticalSpace);
            bottom = top + childAt.getMeasuredHeight();
            right = left + childAt.getMeasuredWidth();
            childAt.layout(left, top, right, bottom);//是针对子Child在onLayout中进行的大小的位置设置
        }
    }

    @Override
    public void onItemClick(int position) {
        lastPosition = position;
        if (itemClickCallBack != null) {
            itemClickCallBack.onItemClick(position);
        }
//        postInvalidate();//这是自定义View中使用的，即重新走onDraw()回调,其可以在UI线程或者是其他线程
//        invalidate();//跟postInvalidate()方法的绘制相同，不过其只能在UI线程中执行
        requestLayout();//其只会重新走onMeasure()和onLayout()方法不会走onDraw()方法
    }


    //设置点击回调
    private ItemClickCallBack itemClickCallBack;

    public void setCallBack(ItemClickCallBack callBack) {
        itemClickCallBack = callBack;
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }
}
