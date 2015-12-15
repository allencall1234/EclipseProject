package com.zlt.icode.common;

import com.zlt.icode.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.shapes.OvalShape;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class MyShape extends OvalShape implements OnTouchListener{
	private boolean check;
	private Paint mPaint;
	private int color1,color2;
	private int innerRadius;
	private int outerRadius;
	
	private int centerX,centerY;
	public MyShape(Context context) {
		// TODO Auto-generated constructor stub
		color1 = context.getResources().getColor(R.color.dark_grey);
		color2 = context.getResources().getColor(R.color.dark_blue);
		innerRadius = (int) context.getResources().getDimension(R.dimen.inner_radius);
		outerRadius = (int) context.getResources().getDimension(R.dimen.outer_radius);
	
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
		mPaint.setColor(color2);
	}
	
	@Override
	protected void onResize(float width, float height) {
		// TODO Auto-generated method stub
		super.onResize(width, height);
		centerX = (int) width/2;
		centerY = (int) height/2;
	}
	
	public void setCheck(boolean check) {
		// TODO Auto-generated method stub
		this.check = check;
		mPaint.setColor(check?color2:color1);
	}
	
	public boolean getCheck() {
		// TODO Auto-generated method stub
		return check;
	}
	@Override
	public void draw(Canvas canvas, Paint paint) {
		// TODO Auto-generated method stub
		super.draw(canvas, paint);
		mPaint.setStyle(Style.FILL);
		canvas.drawCircle(centerX, centerY, innerRadius, mPaint);
		mPaint.setStyle(Style.STROKE);
		canvas.drawCircle(centerX, centerY, outerRadius, mPaint);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		
		return true;
	}
	
}
