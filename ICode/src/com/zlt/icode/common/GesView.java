package com.zlt.icode.common;

import java.util.ArrayList;
import java.util.List;

import com.zlt.icode.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * 手势绘制view
 * @author Administrator
 *
 */
public class GesView extends View {

	private Paint mPaint;

	private int width, height;

	private int innerRadius, outerRadius;

	private int color1, color2, color3;

	private int color;

	private List<Integer> posList = new ArrayList<Integer>();

	private Path mPath;
	private Paint pathPaint;
	
	
	private String password = "";
	public GesView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
		color1 = getResources().getColor(R.color.dark_grey);
		color2 = getResources().getColor(R.color.dark_blue);
		color3 = getResources().getColor(R.color.color_white);

		innerRadius = (int) getResources().getDimension(R.dimen.inner_radius);
		outerRadius = (int) getResources().getDimension(R.dimen.outer_radius);

		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
		color = color1;
		mPaint.setStyle(Style.FILL);

		mPath = new Path();
		
		pathPaint = new Paint(Paint.DITHER_FLAG|Paint.ANTI_ALIAS_FLAG);
		pathPaint.setColor(color2);
		pathPaint.setStyle(Style.STROKE);
		pathPaint.setStrokeWidth(2);
	}

	public GesView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}

	public GesView(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);
		width = w;
		height = width;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		
		if (posList.size() > 1) {
			canvas.drawPath(mPath, pathPaint);
		}

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				canvas.save();
				canvas.translate((i + 1) % 4 * width / 4, (j + 1) % 4 * width
						/ 4);
				if (posList.contains(i + 3 * j)) {
					color = color2;
				} else {
					color = color1;
				}
				mPaint.setColor(color);
				canvas.drawCircle(0, 0, outerRadius, mPaint);
				mPaint.setColor(color3);
				canvas.drawCircle(0, 0, outerRadius - 3, mPaint);
				mPaint.setColor(color);
				canvas.drawCircle(0, 0, innerRadius, mPaint);
				canvas.restore();
			}
		}
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		int x = (int) event.getX();
		int y = (int) event.getY();

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			return isAviliablePos(x, y) >= 0;
		case MotionEvent.ACTION_MOVE:
			isAviliablePos(x, y);
			break;
		case MotionEvent.ACTION_UP:
			posList.clear();
			mPath.reset();
			if (listener != null) {
				listener.onEndTouch(password);
			}
			invalidate();
			password = "";
			return true;
		default:
			break;
		}
		return super.onTouchEvent(event);	
	}

	private int isAviliablePos(int x, int y) {
		// TODO Auto-generated method stub
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (Math.sqrt((x - (i + 1) % 4 * width / 4)
						* (x - (i + 1) % 4 * width / 4)
						+ (y - (j + 1) % 4 * width / 4)
						* (y - (j + 1) % 4 * width / 4)) < outerRadius) {
					int pos = i + 3 * j;
					if (posList.size() == 0 || posList.get(posList.size()-1) != pos) {
						posList.add(pos);
						password += pos + "";
						if (posList.size() == 1) {
							mPath.moveTo((i + 1) % 4 * width / 4, (j + 1) % 4 * width / 4);
						}else {
							mPath.lineTo((i + 1) % 4 * width / 4, (j + 1) % 4 * width / 4);
						}
						invalidate();
					}
					return i + 3 * j;
				}
			}
		}
		return -1;
	}
	
	public interface OnEndTouchListener{
		void onEndTouch(String password);
	}
	
	private OnEndTouchListener listener;
	
	public void SetOnEndTouchListener(OnEndTouchListener listener){
		this.listener = listener;
	}
}
