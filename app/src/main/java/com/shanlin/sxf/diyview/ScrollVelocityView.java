package com.shanlin.sxf.diyview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.OverScroller;

import com.shanlin.sxf.R;
import com.shanlin.sxf.bean.StepBean;
import com.shanlin.sxf.utils.DensityUtil;

import java.util.List;

/**
 * ViewScroll&VelocityView
 * Prefect
 */
public class ScrollVelocityView extends View {

    private List<StepBean> stepBeanList;
    private int spaceX;
    private int marginRight;
    private int marginBottom;
    private Paint paint;
    private Canvas canvas;
    private float yRadio;
    float maxY = 0;
    float rightX, bottomX;
    OverScroller overScroll;
    VelocityTracker velocityTracker;
    int scaledTouchSlop;
    int scaledMaximumFlingVelocity;
    int scaledMinimumFlingVelocity;
    private int circleRadius = DensityUtil.dp2px(getContext(), 2);
    float startCircleX;
    int overScrollCallBackX;
    private OnStepCallBack onStepCallBack;

    public ScrollVelocityView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public void setOnStepCallBack(OnStepCallBack onStepCallBack) {
        this.onStepCallBack = onStepCallBack;
    }

    public void setStepBeanList(List<StepBean> stepBeanList) {
        this.stepBeanList = stepBeanList;
        maxY = 0;
        for (int i = 0; i < stepBeanList.size(); i++) {
            StepBean stepBean = stepBeanList.get(i);
            maxY = maxY > stepBean.stepCount ? maxY : stepBean.stepCount;
        }
        postInvalidate();
    }

    private void initView() {
        setOverScrollMode(OVER_SCROLL_ALWAYS);
        paint = new Paint();
        paint.setAntiAlias(true);
        spaceX = DensityUtil.dp2px(getContext(), 35);
        marginBottom = DensityUtil.dp2px(getContext(), 10);
        overScroll = new OverScroller(getContext());
        velocityTracker = VelocityTracker.obtain();
        ViewConfiguration viewConfiguration = ViewConfiguration.get(getContext());
        scaledTouchSlop = viewConfiguration.getScaledTouchSlop();
        scaledMaximumFlingVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
        scaledMinimumFlingVelocity = viewConfiguration.getScaledMinimumFlingVelocity();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (stepBeanList == null || stepBeanList.size() == 0) {
            return;
        }
        overScrollCallBackX = getWidth() / 3;
        marginRight = getWidth() / 2;
        yRadio = maxY != 0 ? ((getHeight() - marginBottom) * 1f) / maxY : 0;
        this.canvas = canvas;
        drawLineY();
        drawPath();
        drawCircle();
        Log.e("aa", "-------------onDraw---------------------");
    }

    private void drawLineY() {
        paint.setStrokeWidth(DensityUtil.dp2px(getContext(), 1));
        paint.setColor(getResources().getColor(R.color.gray_9));
        paint.setTextSize(DensityUtil.dp2px(getContext(), 12));
        DashPathEffect pathEffect = new DashPathEffect(new float[]{10, 5}, 0);
        paint.setPathEffect(pathEffect);
        rightX = getWidth() - marginRight;
        bottomX = getHeight() - marginBottom;
        for (int i = 0; i < stepBeanList.size(); i++) {
            canvas.drawLine(rightX - i * spaceX, bottomX, rightX - i * spaceX, 0, paint);
            paint.setColor(getResources().getColor(R.color.black));
            canvas.drawText(String.valueOf(i), rightX - i * spaceX - 5, bottomX - 200, paint);
        }
    }

    private void drawPath() {
        paint.setColor(getResources().getColor(R.color.black));
        paint.setPathEffect(null);
        Path pathLine = new Path();
        Path pathShadow = new Path();
        for (int i = 0; i < stepBeanList.size(); i++) {
            StepBean stepBean = stepBeanList.get(i);
            if (i == 0) {
                pathLine.moveTo(rightX, bottomX - stepBean.stepCount * yRadio);
                pathShadow.moveTo(rightX, bottomX - stepBean.stepCount * yRadio);
            } else {
                pathLine.lineTo(rightX - i * spaceX, bottomX - stepBean.stepCount * yRadio);
                pathShadow.lineTo(rightX - i * spaceX, bottomX - stepBean.stepCount * yRadio);
            }
            if (i == stepBeanList.size() - 1) {
                pathShadow.lineTo(rightX - i * spaceX, bottomX);
                pathShadow.lineTo(rightX, bottomX);
                pathShadow.close();
            }
        }
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(getResources().getColor(R.color.colorcc23C993));
        canvas.drawPath(pathLine, paint);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(getResources().getColor(R.color.color8055ffc5));
        canvas.drawPath(pathShadow, paint);
    }

    private void drawCircle() {
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(getResources().getColor(R.color.red));
        startCircleX = rightX;
        StepBean stepBean = stepBeanList.get(currentTurePosition);
        int scrollX = getScrollX();
        startCircleX = startCircleX + scrollX;
        if (startCircleX < getScrollMaxRange()) {
            startCircleX = rightX - (currentTurePosition) * spaceX;
        }
        if (isChangeY) {
            stepBean = stepBeanList.get(currentTurePosition);
            if (onStepCallBack != null) {
                onStepCallBack.onStepValue(stepBean.stepCount);
            }
        }
        float centerY = bottomX - stepBean.stepCount * yRadio;
        canvas.drawCircle(startCircleX, centerY, circleRadius, paint);
    }


    float startDownX;
    boolean isDragged = false;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (velocityTracker == null) {
            velocityTracker = VelocityTracker.obtain();
        }
        velocityTracker.addMovement(event);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (!overScroll.isFinished()) {
                    overScroll.abortAnimation();
                }
                startDownX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                float dexIndex = event.getX() - startDownX;
                isChangeY = false;
                //如果不接触手势类的话，可以用变量表示第一次Move的时候执行判断
                if (!isDragged && Math.abs(dexIndex) > scaledTouchSlop) {
                    //第一次滑动
                    if (dexIndex > 0) {
                        dexIndex -= scaledTouchSlop;
                    } else {
                        dexIndex += scaledTouchSlop;
                    }
                    isDragged = true;
                }
                if (isDragged) {
                    //很奇怪dexIndex>0向左滑，<0向右滑---需要重写onOverScrolled()方法--内部其实调用setScrollX()也是调用的scrollTo()方法，所以这里可以直接
                    //TODO  用Scroll类来执行滑动不用OverScroll也行，至于两者的区别还需研究
                    //这种是移动画布的距离，也就是最左边不是永远是0，那么画布的宽度到底是多少这是个问题
                    boolean overScrollBy = overScrollBy(-(int) dexIndex, 0, getScrollX(), getScrollY(), getScrollMaxRange(), 0, getMaxOverScrollX(), 0, true);
//                    scrollTo(getScrollX() - (int) dexIndex, 0);
                    if (overScrollBy) {
                        velocityTracker.clear();
                    }
                    startDownX = event.getX();
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (isDragged) {
                    velocityTracker.computeCurrentVelocity(1000, scaledMaximumFlingVelocity);
                    int xVelocity = (int) velocityTracker.getXVelocity();
                    if (Math.abs(xVelocity) > scaledMinimumFlingVelocity) {
                        //flying也是如此，内部传入的坐标是准确的坐标，overScrollCallBackX是View滑动的惯性距离
                        overScroll.fling(getScrollX(), getScrollY(), -xVelocity, 0, getMinFlyingScrollX(), 0, 0, 0, overScrollCallBackX, 0);
                        invalidateView();
                    } else {
                        //这个是真实的移动距离，也就是最后的临界点坐标值
                        overScroll.springBack(getScrollX(), getScrollY(), getMinFlyingScrollX(), 0, 0, 0);
                        invalidateView();
                    }
                }
                isDragged = false;
                recycleVelocityTracker();
                break;
        }
        return true;
    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        int oldScrollX = getScrollX();
        int oldScrollY = getScrollY();
        setScrollX(scrollX);
        onScrollChanged(scrollX, scrollY, oldScrollX, oldScrollY);
        Log.e("aa", "----------------onOverScrolled-----------------");
    }

    private int getScrollMaxRange() {
        if (stepBeanList != null) {
            return -getMaxOverScrollX() + overScrollCallBackX;
        }
        return (getWidth() * 2 + overScrollCallBackX);
    }

    private int getMaxOverScrollX() {
        if (stepBeanList != null) {
            return (stepBeanList.size() - 1) * spaceX + overScrollCallBackX;
        }
        return getWidth() * 2;
    }

    private int getMinFlyingScrollX() {
        if (stepBeanList != null) {
            return -(stepBeanList.size() - 1) * spaceX;
        }
        return getWidth() * 2;
    }

    /**
     * 该方法是为了处理startScroll的时候用的，因为startScroll（）View不会执行滑动
     * fling方法也是不会走回调的，也是有回调距离，然后我们在执行对应View的运动
     */
    @Override
    public void computeScroll() {
        if (overScroll.computeScrollOffset()) {
            int oldX = getScrollX();
            int x = this.overScroll.getCurrX();
            overScrollBy(x - oldX, 0, oldX, 0, getScrollMaxRange(), 0, getMaxOverScrollX(), 0, false);
            invalidateView();//invalidate()内部会执行computeScroll()方法
            Log.e("aa", "-----------------------computeScrollOffset-----------------");
        } else {
            if (!isDragged) {
                checkCircleToScroll();
            }
            Log.e("aa", "----------------computeScroll-----------------");
        }
    }

    //根据小红点的位置交转画布的移动距离使其能够滑动到准确的位置
    private int currentTurePosition = 0;

    private void checkCircleToScroll() {
        float dexScrollX = 0;
        if (getScrollX() < 0) {
            int scrollX = Math.abs(getScrollX());
            int indexPosition = scrollX / spaceX;
            int dexLast = scrollX % spaceX;
            if (dexLast > spaceX / 2) {
                currentTurePosition = indexPosition + 1;
                dexScrollX = -(spaceX - dexLast);
            } else {
                if (dexLast == 0) {
                    currentTurePosition = indexPosition;
                } else {
                    currentTurePosition = indexPosition > 0 ? indexPosition - 1 : 0;
                }
                dexScrollX = dexLast;
            }
        } else if (getScrollX() > 0) {
            dexScrollX = -(getScrollX());
            currentTurePosition = 0;
        } else {
            currentTurePosition = 0;
        }
        if (currentTurePosition < 0) {
            currentTurePosition = 0;
        } else if (currentTurePosition > stepBeanList.size() - 1) {
            currentTurePosition = stepBeanList.size() - 1;
        }
        scrollToCurrentCircle(dexScrollX);
    }


    //移动画布使其到对应的坐标
    private boolean isChangeY;

    private void scrollToCurrentCircle(float dexIndex) {
        if (dexIndex != 0) {
            isChangeY = false;
            overScroll.startScroll(getScrollX(), 0, (int) dexIndex, 0);
            invalidateView();//重绘走Computer
        } else if (!isChangeY) {
            isChangeY = true;
            invalidateView();
        }
    }

    private void recycleVelocityTracker() {
        if (velocityTracker != null) {
            velocityTracker.recycle();
            velocityTracker = null;
        }
    }

    public void invalidateView() {
        if (Build.VERSION.SDK_INT >= 16) {
            postInvalidateOnAnimation();
        } else {
            invalidate();
        }
    }


    public interface OnStepCallBack {
        void onStepValue(float value);
    }

}
