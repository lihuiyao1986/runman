package com.clt.runman.widget.ring;

import com.clt.runman.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * 自定义圆视图
 * 
 * @author yanshengli
 * @since 2015-2-5
 */
public class RingView extends View 
{
	/** 画笔 **/
	private Paint paint;
	/** 上下文 **/
	private Context context;
	/** 半径 **/
	private float radius;
	/** 背景颜色 **/
	private int bgColor;
	
	/**
	 * 构造器－1
	 * @param context
	 */
	public RingView(Context context) 
	{
		this(context, null);
	}
	
	/**
	 * 构造器－2
	 * @param context
	 * @param attrs
	 */
	public RingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		this.paint = new Paint();
		this.paint.setAntiAlias(true); // 消除锯齿
		this.paint.setStyle(Paint.Style.FILL); // 绘制实心圆
		TypedArray typeArray = context.obtainStyledAttributes(attrs, R.styleable.RingView);
		radius = typeArray.getDimension(R.styleable.RingView_radius, 5);
		bgColor = typeArray.getColor(R.styleable.RingView_bgColor, Color.GRAY);
		typeArray.recycle();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		int center = getWidth() / 2;
		int innerCircle = dip2px(context, radius); // 设置内圆半径
		this.paint.setColor(bgColor);
		this.paint.setStrokeWidth(2);
		canvas.drawCircle(center, center, innerCircle, this.paint);
		super.onDraw(canvas);
	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	private static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}
}
