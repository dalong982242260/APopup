package com.zwl.popup;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.View;

/**
 * @author zwl
 * @describe Popup 三角形
 * @date on 2021/5/12
 * @email 982242260@qq.com
 */
public class APopupTriangleView extends View {

    //倒三角
    public static final int INVERTED = 0;
    //正三角
    public static final int REGULAR = 1;
    //三角形模式
    private int mode;
    //三角形底长  默认50
    private int width = 50;
    //三角形的高  默认30
    private int height = 30;
    private Paint paint;
    private Path path;
    //三角形颜色
    private int color;


    public APopupTriangleView(Context context,
                              int color,
                              int gravity,
                              int width,
                              int height) {
        super(context, null);
        this.width = width;
        this.color = color;
        this.height = height;
        init(gravity);
    }


    private void init(int gravity) {
        if (gravity == APopupGravity.TOP_LEFT
                || gravity == APopupGravity.TOP_RIGHT
                || gravity == APopupGravity.TOP_CENTER) {
            mode = INVERTED;
        } else if (gravity == APopupGravity.BOTTOM_LEFT
                || gravity == APopupGravity.BOTTOM_CENTER
                || gravity == APopupGravity.BOTTOM_RIGHT) {
            mode = REGULAR;
        }
        paint = new Paint();
        paint.setColor(color);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        path = new Path();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(width, height);
    }


    public void setColor(int color) {
        this.color = color;
        paint.setColor(color);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawTriangle(canvas);
    }

    private void drawTriangle(Canvas canvas) {
        if (mode == INVERTED) {
            path.moveTo(0f, 0f);
            path.lineTo(width, 0f);
            path.lineTo(width / 2.0f, height);
        } else {
            path.moveTo(width / 2.0f, 0f);
            path.lineTo(0, height);
            path.lineTo(width, height);
        }
        path.close();
        canvas.drawPath(path, paint);
    }
}