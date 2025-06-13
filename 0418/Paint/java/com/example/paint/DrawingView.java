package com.example.drawingapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

public class DrawingView extends View {

    private ArrayList<CustomPath> paths = new ArrayList<>();
    private ArrayList<CustomPath> undonePaths = new ArrayList<>();
    private Paint paint = new Paint();
    private CustomPath mPath = new CustomPath(Color.BLACK, 5f);

    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupPaint();
    }

    private void setupPaint() {
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (CustomPath path : paths) {
            paint.setColor(path.color);
            paint.setStrokeWidth(path.strokeWidth);
            canvas.drawPath(path, paint);
        }

        paint.setColor(mPath.color);
        paint.setStrokeWidth(mPath.strokeWidth);
        canvas.drawPath(mPath, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPath.reset();
                mPath.moveTo(x, y);
                return true;
            case MotionEvent.ACTION_MOVE:
                mPath.lineTo(x, y);
                break;
            case MotionEvent.ACTION_UP:
                paths.add(mPath.copy());
                mPath.reset();
                break;
            default:
                return false;
        }

        invalidate();
        return true;
    }

    public void setPenColor(int color) {
        mPath.color = color;
    }

    public void setPenSize(float size) {
        mPath.strokeWidth = size;
    }

    public void clearCanvas() {
        paths.clear();
        mPath.reset();
        invalidate();
    }

    public class CustomPath extends Path {
        public int color;
        public float strokeWidth;

        public CustomPath(int color, float strokeWidth) {
            this.color = color;
            this.strokeWidth = strokeWidth;
        }

        public CustomPath copy() {
            CustomPath copied = new CustomPath(color, strokeWidth);
            copied.addPath(this);
            return copied;
        }
    }
}