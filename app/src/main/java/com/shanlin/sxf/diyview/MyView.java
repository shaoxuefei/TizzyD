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
 * @Description: 自定义View
 * @Auther: Sxf
 * @Date: 2017/10/12
 */

public class MyView extends View {
    Paint circlePaint;
    String textContent;
    int textColor;
    int textSize;
    Rect bounds;

    public MyView(Context context) {
        this(context, null);
    }

    public MyView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        float density = context.getResources().getDisplayMetrics().density;

        //解析自定义权限
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MyView, defStyleAttr, 0);
        textContent = typedArray.getString(R.styleable.MyView_myViewText);
        textColor = typedArray.getColor(R.styleable.MyView_myViewTextColor, Color.BLUE);
        //获取大小属性dimension:getDimensionPixeSize()
        textSize = typedArray.getDimensionPixelSize(R.styleable.MyView_myViewTextSize, (int) (density * 16));

        //获取界限--只是绘制前的text的内容长度测量得出的矩形的形状Rect
        circlePaint = new Paint();
        bounds = new Rect();
        circlePaint.setTextSize(textSize);//需要设置TextSize，再测量content的宽高
        circlePaint.getTextBounds(textContent, 0, textContent.length(), bounds);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        circlePaint.setColor(Color.YELLOW);
        circlePaint.setTextSize(textSize);
        circlePaint.setAntiAlias(true);//设置抗锯齿
        canvas.drawRect(0, 0, getWidth(), getHeight(), circlePaint);//此处：getWidth() 和 gewMeasureWidth()

        circlePaint.setColor(textColor);
        /**
         * float X:textContent的内容距离左上角X轴的距离,如果paint设置了GrivateCenter,那么是相对于中心点的X距离,可以理解为view在该位置后的距离位置
         * float Y:textContent的内容的底线距离左上角Y轴的距离
         */
        canvas.drawText(textContent, getWidth() / 2 - bounds.width() / 2, getHeight() / 2 + bounds.height() / 2, circlePaint);
        /**
         * postInvalidate();//这是自定义View中使用的，即重新走onDraw()回调,其可以在UI线程或者是其他线程
         * invalidate();//跟postInvalidate()方法的绘制相同，不过其只能在UI线程中执行
         */
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int wideMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width = 0, height = 0;
        switch (wideMode) {
            case MeasureSpec.EXACTLY:
                width = widthSize;
                break;
            case MeasureSpec.AT_MOST:
                //此处是设置了wrap_parent
                int textWidth = bounds.width();
                width = textWidth + getPaddingLeft() + getPaddingRight();
                break;
            case MeasureSpec.UNSPECIFIED:
                width = widthMeasureSpec;
                break;
        }

        switch (heightMode) {
            case MeasureSpec.EXACTLY:
                height = heightSize;
                break;
            case MeasureSpec.AT_MOST:
                //此处是设置了wrap_parent，默认自定义view会绘制成match_parent
                int textHeight = bounds.height();
                height = textHeight + getPaddingTop() + getPaddingBottom();
                break;
            case MeasureSpec.UNSPECIFIED:
                height = heightMeasureSpec;
                break;
        }
        setMeasuredDimension(width, height);
    }
}
