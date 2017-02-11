package com.example.pulltorefresh.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.pulltorefresh.R;

/**
 * Created by 小明 on 2017/2/10.
 */

public class ZDY extends View {
    private Bitmap db;
    private Bitmap needle;
    private int progress;
    private int h;
    private int w;

    public ZDY(Context context, AttributeSet attrs) {
        super(context, attrs);
        db = BitmapFactory
                .decodeResource(getResources(), R.drawable.dash_board);
        needle = BitmapFactory
                .decodeResource(getResources(), R.drawable.needle);
    }

    // widthMeasureSpec，heightMeasureSpec由两部分组成，
    // mode:MeasureSpec.AT_MOST;至多
    // MeasureSpec.EXACTLY;确切的(match_parent,100dp)
    // MeasureSpec.UNSPECIFIED;(未指定)
    // size是系统通过父控件和其子控件计算出来的参考值
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //setMeasuredDimension(db.getWidth(), db.getHeight());
        int measureWidth = measureWidth(widthMeasureSpec);
        int measureHeight = measureHeight(heightMeasureSpec);
        setMeasuredDimension(measureWidth, measureHeight);
    }

    private int measureHeight(int heightMeasureSpec) {
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int size = MeasureSpec.getSize(heightMeasureSpec);
        Log.i("测试", "size:"+size);
        int result = 0;
        switch (mode) {
            case MeasureSpec.EXACTLY:
                result=size;
                break;
            case MeasureSpec.AT_MOST:
                result=db.getHeight();
                break;
            case MeasureSpec.UNSPECIFIED:
                result=db.getHeight();
                break;
        }
        return result;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        // TODO Auto-generated method stub
        super.onSizeChanged(w, h, oldw, oldh);
        this.w=w;
        this.h=h;
        db= Bitmap.createScaledBitmap(db, w, h, true);
        needle=Bitmap.createScaledBitmap(needle, w, h, true);
    }

    private int measureWidth(int widthMeasureSpec) {
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int size = MeasureSpec.getSize(widthMeasureSpec);
        int result = 0;
        switch (mode) {
            case MeasureSpec.EXACTLY:
                result = size;
                break;
            case MeasureSpec.AT_MOST:
                result = db.getWidth();
                break;
            case MeasureSpec.UNSPECIFIED:
                result = db.getWidth();
                break;
        }
        return result;
    }

    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(db, 0, 0, null);
        canvas.rotate(progress,w/2,h/2);
        canvas.drawBitmap(needle, 0, 0, null);

    }

    public void setProgress(int progress) {
        if (progress>100) {
            progress=100;
        }

        this.progress=(int) (progress*2.8f);
        invalidate();
    }
}
