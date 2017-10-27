package com.shanlin.sxf.diyview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * @Description: 自定义View
 * @Auther: Sxf
 * @Date: 2017/10/12
 */

public class MyView extends View {
    Paint circlePaint=new Paint();
    public MyView(Context context) {
        super(context,null);
    }

    public MyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        circlePaint.setColor(Color.RED);
        circlePaint.setAntiAlias(true);//设置抗锯齿-减少圆形边缘锯齿状
        //圆心坐标(距离x,y轴的距离)、圆半径
        canvas.drawCircle(100,100,100,circlePaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }
}
