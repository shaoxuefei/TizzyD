package com.shanlin.sxf.paint;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Sxf on 2017/5/10.
 *
 * @project: Demo.
 * @detail:
 */

public class PaintView extends View {
    private int screenWidth, screenHeight;
    private Bitmap backgroudBitmap;
    private Path path;
    private Paint paint;
    private float currentX, currentY;
    private Canvas mCanvas;

    public PaintView(Context context, int screenWidth, int screenHeight) {
        super(context);
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        initPaint();
    }

    private void initPaint() {
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);
        path = new Path();
        backgroudBitmap = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_8888);

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mCanvas == null) {
            //该画布是重新new的一个Bitmap画布;new Canvas()的构造方法---》无参、有参:Bitmap bitmap
            mCanvas = new Canvas(backgroudBitmap);//需要从新new一个画布进行承载Bitmap--以至于你在外部获取bitmap时并不是之前的大白板
        }
        //该canvas是自定义View自己的画布
        canvas.drawPath(path, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                currentX = x;
                currentY = y;
                path.moveTo(currentX, currentY);
                break;
            case MotionEvent.ACTION_MOVE:
                currentX = x;
                currentY = y;
                path.quadTo(currentX, currentY, x, y);

                break;
            case MotionEvent.ACTION_UP:
                mCanvas.drawPath(path, paint);
                break;
        }
        invalidate();
        return true;
    }

    public Bitmap getPathBitmap(float width, float heigth) {
        int paintWidth = backgroudBitmap.getWidth();
        int paintHeight = backgroudBitmap.getHeight();
        float dextaW = (float) width / paintWidth;
        float dextaH = (float) heigth / paintHeight;
        Matrix matrix = new Matrix();
        matrix.postScale(dextaW, dextaH);
        Bitmap bitmap = Bitmap.createBitmap(backgroudBitmap, 0, 0, paintWidth, paintHeight, matrix, true);
        return bitmap;
    }

    public void cleanCavans() {
        if (mCanvas != null) {
            path.reset();
            mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            invalidate();
        }
    }

}
