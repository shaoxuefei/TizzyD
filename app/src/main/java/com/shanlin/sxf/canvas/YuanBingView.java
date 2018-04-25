package com.shanlin.sxf.canvas;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.shanlin.sxf.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author : SXF
 * @ date   : 2018/3/26
 * e-mail  : shaoxf@go-goal.com
 * desc    :
 */

public class YuanBingView extends View {

    public YuanBingView(Context context) {
        super(context);
    }

    public YuanBingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    private int arcWidth;
    private int arcHeight;
    private Paint paintStart, paintSecond, paintThird, paintFour, paintFive, paintSix;
    private int storkWidth = 2;
    private float[] total = new float[]{100, 59, 89, 32, 88, 169};
    private ArrayList<Paint> data = new ArrayList<>();
    private float ratio = 0.02f;
    float totalInt = 0;
    private boolean anima, isTable;

    private String[] tableNums = new String[]{"12", "1", "2", "3", "4", " 5", " 6", "7", "8", "9", "10", "11"};

    private HashMap<Integer, float[]> keyMap = new HashMap<>();
    private HashMap<Integer, float[]> minMap = new HashMap<>();
    int lineWidth = 10, space = 10;
    Paint keyPaint, textPaint;

    public boolean isAnima() {
        return anima;
    }

    public void setAnima(boolean anima) {
        this.anima = anima;
        if (!anima) {
            ratio = 1f;
            invalidate();
        }
    }

    public boolean isTable() {
        return isTable;
    }

    public void setTable(boolean table) {
        isTable = table;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        data.clear();
        totalInt = 0;
        arcWidth = getWidth();
        arcHeight = getHeight();
        checkPercentage();
        paintStart = new Paint();
        paintStart.setStrokeWidth(storkWidth);
        paintStart.setAntiAlias(true);
        paintStart.setColor(getResources().getColor(R.color.colorAccent));

        paintSecond = new Paint();
        paintSecond.setStrokeWidth(storkWidth);
        paintSecond.setAntiAlias(true);
        paintSecond.setColor(getResources().getColor(R.color.colorPrimary));

        paintThird = new Paint();
        paintThird.setAntiAlias(true);
        paintThird.setStrokeWidth(storkWidth);
        paintThird.setColor(getResources().getColor(R.color.black));

        paintFour = new Paint();
        paintFour.setStrokeWidth(storkWidth);
        paintFour.setAntiAlias(true);
        paintFour.setColor(getResources().getColor(R.color.backroud));
        paintFive = new Paint();
        paintFive.setStrokeWidth(storkWidth);
        paintFive.setAntiAlias(true);
        paintFive.setColor(getResources().getColor(R.color.red));
        paintSix = new Paint();
        paintSix.setStrokeWidth(storkWidth);
        paintSix.setAntiAlias(true);
        paintSix.setColor(getResources().getColor(R.color.gray_9));

        data.add(paintStart);
        data.add(paintSecond);
        data.add(paintThird);
        data.add(paintFour);
        data.add(paintFive);
        data.add(paintSix);


        //绘制扇形区域
        RectF rectF = new RectF(0, 0, arcWidth, arcHeight);
        float start = 0;

        //绘制分布版百分比
        if (textPaint == null) {
            textPaint = new Paint();
            textPaint.setTextSize(28);
            textPaint.setAntiAlias(true);
            textPaint.setColor(getResources().getColor(R.color.white));
            textPaint.setStrokeWidth(storkWidth);
        }
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float measureHeight = fontMetrics.descent - fontMetrics.ascent;
        //左上角是(0,0)点原点坐标
        /**
         * drawText(text,x,y,paint)
         * x:表示字符串左侧在屏幕的x坐标
         * y:表示字符串的baseline在屏幕上的纵坐标，也就是文字的底部
         */
        if (!isTable) {
            for (int i = 0; i < total.length; i++) {
                float measureWidth = textPaint.measureText(Math.round((total[i] / totalInt) * 100) + "%");//获取的是text的宽度，，而高度直接有textSize就可以知道其高度
                canvas.drawArc(rectF, start, (total[i] / totalInt) * 360 * ratio, true, data.get(i));
                canvas.drawText(Math.round((total[i] / totalInt) * 100) + "%", (int) initArc((int) start, (int) ((total[i] / totalInt) * 360), 0.5f)[0] - measureWidth / 2, (int) initArc((int) start, (int) ((total[i] / totalInt) * 360), 0.5f)[1] + measureHeight / 2, textPaint);
                start = start + (total[i] / totalInt) * 360;
            }
            if (ratio < 1f) {
                ratio = ratio + 0.02f;
                invalidate();
            }
        } else {
            /**
             * 绘制-表
             * 再次声明  canvas.drawText是Y轴的值文字的baseLine的坐标
             */
            if (keyPaint == null) {
                keyPaint = new Paint();
                keyPaint.setColor(getResources().getColor(R.color.white));
                keyPaint.setAntiAlias(true);
                keyPaint.setStrokeWidth(5);
            }

            canvas.drawArc(rectF, start, 360, true, paintStart);
            int tabStart = -90, tableSweep = 0;

            for (int j = 0; j < tableNums.length; j++) {
                double[] doubles = initArc(tabStart, tableSweep, 1f);
                int angleTurn = tabStart + tableSweep;
                tabStart = angleTurn;
                float textWidth = textPaint.measureText(tableNums[j]);
                Paint.FontMetrics fontMetrics1 = textPaint.getFontMetrics();
                float textHeight = fontMetrics1.descent - fontMetrics1.ascent;
                if (angleTurn > -90 && angleTurn < 0) {
                    drawLineAndText(canvas, tableNums[j], doubles, angleTurn, textWidth, textHeight,
                            (float) doubles[0] - lineWidth,
                            (float) doubles[1] + lineWidth,
                            (float) doubles[0] - lineWidth - space - textWidth,
                            (float) doubles[1] + lineWidth + textHeight);
                } else if (angleTurn > 0 && angleTurn < 90) {
                    drawLineAndText(canvas, tableNums[j], doubles, angleTurn, textWidth, textHeight,
                            (float) (doubles[0] - lineWidth),
                            (float) (doubles[1] - lineWidth),
                            (float) (doubles[0] - lineWidth - space - textWidth),
                            (float) (doubles[1] - lineWidth));
                } else if (angleTurn > 90 && angleTurn < 180) {
                    drawLineAndText(canvas, tableNums[j], doubles, angleTurn, textWidth, textHeight,
                            (float) doubles[0] + lineWidth,
                            (float) doubles[1] - lineWidth,
                            (float) doubles[0] + space + lineWidth,
                            (float) doubles[1] - lineWidth);
                } else if (angleTurn > 180 && angleTurn < 270) {
                    drawLineAndText(canvas, tableNums[j], doubles, angleTurn, textWidth, textHeight,
                            (float) doubles[0] + lineWidth,
                            (float) doubles[1] + lineWidth,
                            (float) doubles[0] + lineWidth + space,
                            (float) doubles[1] + lineWidth + textHeight);
                } else if (angleTurn == -90) {
                    drawLineAndText(canvas, tableNums[j], doubles, angleTurn, textWidth, textHeight,
                            (float) doubles[0],
                            (float) doubles[1] + lineWidth,
                            (float) doubles[0] - textWidth / 2,
                            (float) doubles[1] + lineWidth + space + textHeight);
                } else if (angleTurn == 0 || tabStart == 360) {
                    drawLineAndText(canvas, tableNums[j], doubles, angleTurn, textWidth, textHeight,
                            (float) doubles[0] - lineWidth,
                            (float) doubles[1],
                            (float) doubles[0] - lineWidth - space - textWidth,
                            (float) doubles[1] + textHeight / 2);
                } else if (angleTurn == 90) {
                    drawLineAndText(canvas, tableNums[j], doubles, angleTurn, textWidth, textHeight,
                            (float) doubles[0],
                            (float) doubles[1] - lineWidth,
                            (float) doubles[0] - textWidth / 2,
                            (float) doubles[1] - lineWidth - space);
                } else if (angleTurn == 180) {
                    drawLineAndText(canvas, tableNums[j], doubles, angleTurn, textWidth, textHeight,
                            (float) doubles[0] + lineWidth,
                            (float) doubles[1],
                            (float) doubles[0] + lineWidth + space,
                            (float) doubles[1] + textHeight / 2);
                }
                tableSweep = 30;
            }

            /**
             * 绘制表针
             */
            canvas.drawCircle(arcWidth / 2, arcWidth / 2, 10, paintThird);
            //获取8点钟坐标
            double[] doubles = initArc(90, 60, 1);
            //绘制时针
            float measureText = textPaint.measureText("8");
            canvas.drawLine(arcWidth / 2, arcWidth / 2, (float) doubles[0] + space + lineWidth + measureText + 50, (float) (doubles[1] - (space + lineWidth + measureText + 50) / 1.5), paintSecond);
            Paint.FontMetrics fontMetrics1 = textPaint.getFontMetrics();
            //绘制分针
            canvas.drawLine(arcWidth / 2, arcWidth / 2, arcWidth / 2, space + fontMetrics1.descent - fontMetrics1.ascent + lineWidth, paintSecond);

        }

    }

    private void drawLineAndText(Canvas canvas, String text, double[] doubles, int angleTurn, float textWidth, float textHeight, float minX, float minY, float hourX, float hourY) {
        //存储分钟刻度坐标
        float[] minkey = new float[2];
        minkey[0] = (float) doubles[0] - lineWidth;
        minkey[1] = (float) doubles[1] + lineWidth;
        minMap.put(angleTurn, minkey);
        if (angleTurn % 30 == 0) {
            //整点绘制刻度线(粗线)+表文字
            float[] key = new float[2];
            key[0] = (float) (doubles[0] - lineWidth - space - textWidth);
            key[1] = (float) doubles[1] + lineWidth + textHeight;
            keyMap.put(angleTurn, key);
            canvas.drawText(text, key[0], key[1], textPaint);
            canvas.drawLine(minkey[0], minkey[1], (float) doubles[0], (float) doubles[1], keyPaint);
        } else {
            //分钟刻度用细线
            canvas.drawLine(minkey[0], minkey[1], (float) doubles[0], (float) doubles[1], textPaint);
        }
    }


    /**
     * Math.round()--四舍五入
     * 其实对于Math.sin()、Math.cos()内部对应的是弧度的值，并不是角度的值；所以需要把角度转换成弧度
     * 3点钟方向是0度，顺时针是正数；逆时针是负数(负度数)
     * Math.sin()、Math.cos()计算是根据你的start和你的sweepAngle来判断的,start是之前的开始度数的和，所以180-(start+sweep/2）肯定能找到弧度中心的角度与坐标轴为参考物的角度值；
     * Y--0-180度算出来的值需要加半径来当坐标，180-360的值需要半径减去算出的值才是坐标
     * X--90-270度算出的值需要半径减去你的值才是坐标，270-90：需要半径加上算出的中心值才是坐标
     *
     * @param startAngle
     * @param sweepAngle
     * @return
     */
    private double[] initArc(int startAngle, int sweepAngle, float ratioDex) {
        double[] position = new double[2];
        //这个就是文字位置的角度--所以取剩余的角度一定可以是以坐标轴为参照物的，因为知道start的角度
        int partOfAngle = (int) (startAngle + (sweepAngle * ratioDex));
        int circleR = arcWidth / 2;
        if (partOfAngle < 0) {
            partOfAngle = 360 + partOfAngle;
        }
        double y = Math.sin(Math.toRadians(partOfAngle % 180)) * ratioDex * circleR;
        double x = Math.abs(Math.cos(Math.toRadians(partOfAngle % 180)) * ratioDex * circleR);
        if (partOfAngle <= 90 && partOfAngle >= 0) {
            //内部参数--是弧度，并不是角度
            position[0] = x + circleR;
            position[1] = y + circleR;
        } else if (partOfAngle > 90 && partOfAngle <= 180) {
            //内部参数--是弧度，并不是角度--这个时候cos(120)==-cos(60),所以其实是半径-计算的长度;sin不用因为sin(120)=sin(60)
            position[0] = circleR - x;
            position[1] = y + circleR;
        } else if (partOfAngle > 180 && partOfAngle <= 270) {
            //内部参数--是弧度，并不是角度
            position[0] = circleR - x;
            position[1] = circleR - y;
        } else if (partOfAngle > 270 && partOfAngle <= 360) {
            //内部参数--是弧度，并不是角度
            position[0] = x + circleR;
            position[1] = circleR - y;
        }
        return position;
    }

    /**
     * 所以这种写法  不提倡
     *
     * @param totalInt
     */

    private void checkPercentage(float totalInt) {
        for (int i = 0; i < total.length; i++) {
            /**
             *  局部变量只在方法内部赋值有用，所以需要加上this,,要不然onDraw方法中，相当于该变量没做处理还是0，不管这个是全局的还是局部变量，，全局的话需要加this，
             局部的就不能用方法了
             */
            this.totalInt = this.totalInt + total[i];
        }
    }

    private void checkPercentage() {
        for (int i = 0; i < total.length; i++) {
            /**
             *  局部变量只在方法内部赋值有用，所以需要加上this,,要不然onDraw方法中，相当于该变量没做处理还是0，不管这个是全局的还是局部变量，，全局的话需要加this，
             局部的就不能用方法了
             */
            totalInt = totalInt + total[i];
        }
    }
}
