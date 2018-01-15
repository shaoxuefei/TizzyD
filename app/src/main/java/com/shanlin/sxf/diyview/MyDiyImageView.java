package com.shanlin.sxf.diyview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.shanlin.sxf.R;

/**
 * @Description: 自定义控件--摄像头的空白区域
 * @Auther: Sxf
 * @Date: 2017/11/21
 */

public class MyDiyImageView extends View {
    int lineApha, areaBgApha, lineColor, areaColor;
    int widthSize, heightSize;

    public MyDiyImageView(Context context) {
        this(context, null);
    }

    public MyDiyImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyDiyImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyDiyImageView);
        lineColor = typedArray.getColor(R.styleable.MyDiyImageView_lineColor, Color.WHITE);
        areaColor = typedArray.getColor(R.styleable.MyDiyImageView_areaBgColor, Color.BLACK);
        lineApha = typedArray.getInt(R.styleable.MyDiyImageView_lineApha, 50);
        areaBgApha = typedArray.getInt(R.styleable.MyDiyImageView_areaBgApha, 50);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        widthSize = MeasureSpec.getSize(widthMeasureSpec);
        heightSize = MeasureSpec.getSize(heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint linePaint = new Paint();
        linePaint.setStyle(Paint.Style.STROKE);//只绘制描边
        linePaint.setColor(lineColor);
        linePaint.setAlpha(lineApha);
        if (recetCenter != null) {
            canvas.drawRect(recetCenter, linePaint);
            Paint areaPaint = new Paint();
            areaPaint.setStyle(Paint.Style.FILL);//只绘制内容，不绘制描边
            areaPaint.setColor(areaColor);//setColor 必须放在setAlpha的前面，要不然无效
            areaPaint.setAlpha(areaBgApha);
            canvas.drawRect(0, recetCenter.top - 1, widthSize / 2 - recetCenter.width() / 2 - 1, recetCenter.bottom + 1, areaPaint);
            canvas.drawRect(recetCenter.right + 1, recetCenter.top - 1, widthSize, recetCenter.bottom + 1, areaPaint);
            canvas.drawRect(0, 0, widthSize, recetCenter.top - 1, areaPaint);
            canvas.drawRect(0, recetCenter.bottom + 1, widthSize, heightSize, areaPaint);
        }
    }

    private Rect recetCenter;

    public void setRecetCenter(Rect recetCenter) {
        this.recetCenter = recetCenter;
        postInvalidate();//重新绘制--针对的是自定义View  即 只重新走onDraw()方法
    }
}
