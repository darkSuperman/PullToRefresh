package com.example.pulltorefresh.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;

import com.example.pulltorefresh.R;

public class Needle extends View {

	private Bitmap needle;

	public Needle(Context context, AttributeSet attrs) {
		super(context, attrs);
		needle = BitmapFactory
				.decodeResource(getResources(), R.drawable.needle);
	}

	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int width = getW(widthMeasureSpec);
		int height = getH(heightMeasureSpec);
		setMeasuredDimension(width, height);
	}

	private int getH(int heightMeasureSpec) {
		int result = 0;
		int mode = MeasureSpec.getMode(heightMeasureSpec);
		int size = MeasureSpec.getSize(heightMeasureSpec);
		switch (mode) {
		case MeasureSpec.EXACTLY:
			result=size;
			break;
		case MeasureSpec.AT_MOST:
			result=needle.getHeight();
			break;
		case MeasureSpec.UNSPECIFIED:
			result=needle.getHeight();
			break;
		}
		
		return result;
	}

	private int getW(int widthMeasureSpec) {
		int result = 0;
		int mode = MeasureSpec.getMode(widthMeasureSpec);
		int size = MeasureSpec.getSize(widthMeasureSpec);
		switch (mode) {
		case MeasureSpec.EXACTLY:
			result = size;
			break;
		case MeasureSpec.AT_MOST:
			result = needle.getWidth();
			break;
		case MeasureSpec.UNSPECIFIED:
			result = needle.getWidth();
			break;
		}
		return result;
	}

	protected void onDraw(Canvas canvas) {
		canvas.drawBitmap(needle, 0, 0, null);
	}

	public void startScanning() {
		 RotateAnimation ra=new RotateAnimation(
				 180,
				 280,
				 RotateAnimation.RELATIVE_TO_SELF, 
				 0.5f, RotateAnimation.RELATIVE_TO_PARENT, 
				 0.5f);
		 ra.setDuration(800);
		 ra.setInterpolator(new DecelerateInterpolator());
		 ra.setRepeatCount(RotateAnimation.INFINITE);
		 ra.setRepeatMode(RotateAnimation.REVERSE);
		 startAnimation(ra);
	}

}
