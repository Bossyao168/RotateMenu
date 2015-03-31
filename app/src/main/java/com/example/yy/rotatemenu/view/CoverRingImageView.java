package com.example.yy.rotatemenu.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;

@SuppressLint("DrawAllocation")
/**
 *
 * 类描述 选装菜单遮盖绘图类
 *
 * @author yaoy
 * @since 1.0 2013-9-26 下午2:18:02
 * @version 1.0 2013-9-26 下午2:18:02
 */
public class CoverRingImageView extends ImageView {

	private final Paint paint;

	private Canvas mCanvas = null;

	// 绘制圆环的内圆宽，包括外环和里环
	private int mOutsideInnerCircleWidth;

	private int mInsideInnerCircleWidth;

	// 绘制圆环的圆环宽，包括外环和里环（圆环宽*2+1 == 真正圆环宽）
	private int mOutsideRingWidth;

	private int mInsideRingWidth;

	// 外、内环的消除绘制终点角度
	private int mRemoveOutsideCircleDynamicAngle = 0;

	private int mRemoveInsideCircleDynamicAngle = 0;

	// 外、内环的填充绘制终点角度
	private int mDrawOutsideCircleDynamicAngle = 0;

	private int mDrawInsideCircleDynamicAngle = 0;

	// 外、内环的填充是否完成
	private Boolean mDrawOutsideCircleIsDone = null;

	private Boolean mDrawInsideCircleIsDone = null;

	// 外、内环的消除是否完成
	private Boolean mRemoveOutsideCircleIsDone = null;

	private Boolean mRemoveInsideCircleIsDone = null;

	// 外、内菜单的出现是否完成
	private Boolean mShowOutsideCircleBoolean = false;

	private Boolean mShowInsideCircleBoolean = false;

	// 外、内菜单的隐藏是否完成
	private Boolean mHideOutsideCircleBoolean = false;

	private Boolean mHideInsideCircleBoolean = false;

	// 外、内菜单的出现是否正在进行
	private Boolean mShowOutsideCircleIsRunning = false;

	private Boolean mShowInsideCircleIsRunning = false;

	// 外、内菜单的隐藏是否正在进行
	private Boolean mHideOutsideCircleIsRunning = false;

	private Boolean mHideInsideCircleIsRunning = false;

	// 外、内环的颜色
	private static final int CIRCLE_COLOR = Color.WHITE;

	// 外、内环的内圆宽适应值
	private static final int OUTSIDE_INNER_CIRCLE_WIDTH_FITER = 3;

	private static final int INSIDE_INNER_CIRCLE_WIDTH_FITER = 3;

	// 外、内环的圆环宽适应值
	private static final int OUTSIDE_RING_WIDTH_FITER = 6;

	private static final int INTSIDE_RING_WIDTH_FITER = 4;

	/**
	 * 方法描述 构造方法，初始化一些变量，用于使自定义空间类能再xml运用
	 *
	 * @param context
	 *            ：上下文 AttributeSet：属性集
	 * @return 无
	 * @throws
	 * @since 1.0
	 * @author yaoy
	 * @date 2013-9-26
	 */
	public CoverRingImageView(Context context, AttributeSet attrs) {
		super(context, attrs);

		// TODO Auto-generated constructor stub
		this.paint = new Paint();

		// 消除锯齿
		this.paint.setAntiAlias(true);

		// 绘制空心圆或 空心矩形
		this.paint.setStyle(Style.STROKE);

		mRemoveOutsideCircleDynamicAngle = 0;
		mDrawOutsideCircleDynamicAngle = 0;

		// 绘制判断的条件，用于决定一开始先画的逻辑
		mDrawOutsideCircleIsDone = true;
		mRemoveOutsideCircleIsDone = false;
		mRemoveInsideCircleIsDone = false;
		mDrawInsideCircleIsDone = true;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// 内圆宽设置（所要覆盖菜单圆宽的一半减去适应值）
		mOutsideInnerCircleWidth = RingOperationLayout.mLevel2Width / 2 - OUTSIDE_INNER_CIRCLE_WIDTH_FITER;
		mInsideInnerCircleWidth = RingOperationLayout.mLevel1Width / 2 - INSIDE_INNER_CIRCLE_WIDTH_FITER;

		// 圆环宽设置（外圆减内圆的差的一半+适应值）
		mOutsideRingWidth = (RingOperationLayout.mLevel3Width - RingOperationLayout.mLevel2Width) / 2 + OUTSIDE_RING_WIDTH_FITER;
		mInsideRingWidth = (RingOperationLayout.mLevel2Width - RingOperationLayout.mLevel1Width) / 2 + INTSIDE_RING_WIDTH_FITER;

		mCanvas = canvas;
		super.onDraw(mCanvas);

		// 控件宽的一半
		int imageViewWidth = getWidth() / 2;

		// 填充参照坐标的矩形，即内切圆，据此计算出坐标点， 参数为左 上 右 下四点
		RectF outsideCircleRect = new RectF(imageViewWidth - (mOutsideInnerCircleWidth + 1 + mOutsideRingWidth / 2), imageViewWidth - (mOutsideInnerCircleWidth + 1 + mOutsideRingWidth / 2), imageViewWidth + (mOutsideInnerCircleWidth + 1 + mOutsideRingWidth / 2), imageViewWidth + (mOutsideInnerCircleWidth + 1 + mOutsideRingWidth / 2));

		RectF insideCircleRect = new RectF(imageViewWidth - (mInsideInnerCircleWidth + 1 + mOutsideRingWidth / 2), imageViewWidth - (mInsideInnerCircleWidth + 1 + mInsideRingWidth / 2), imageViewWidth + (mInsideInnerCircleWidth + 1 + mInsideRingWidth / 2), imageViewWidth + (mInsideInnerCircleWidth + 1 + mInsideRingWidth / 2));

		// 绘制逻辑
		if (mRemoveInsideCircleIsDone == true) {
			removeInsideCircle(canvas, insideCircleRect);
		}
		if (mDrawInsideCircleIsDone == true) {
			drawInsideCircle(canvas, insideCircleRect);
		}
		if (mRemoveOutsideCircleIsDone == true) {
			removeOutsideCircle(canvas, outsideCircleRect);
		}
		if (mDrawOutsideCircleIsDone == true) {
			drawOutsideCircle(canvas, outsideCircleRect);
		}

		// 用于重复刷新onDraw
		invalidate();
	}

	/**
	 * 方法描述 给其他类调用的接口函数，用于显示里菜单
	 *
	 * @param 无
	 * @return 无
	 * @throws
	 * @since 1.0
	 * @author yaoy
	 * @date 2013-9-26
	 */
	public void showInsideCircle() {
		if (mRemoveInsideCircleIsDone == false && mShowInsideCircleIsRunning == false && mHideInsideCircleIsRunning == false) {
			mShowInsideCircleBoolean = true;
			mShowInsideCircleIsRunning = true;
		}
	}

	/**
	 * 方法描述 给其他类调用的接口函数，用于隐藏里菜单
	 *
	 * @param 无
	 * @return 无
	 * @throws
	 * @since 1.0
	 * @author yaoy
	 * @date 2013-9-26
	 */
	public void hideInsideCircle() {
		if (mDrawInsideCircleIsDone == false && mShowInsideCircleIsRunning == false && mHideInsideCircleIsRunning == false) {
			mHideInsideCircleBoolean = true;
			mHideInsideCircleIsRunning = true;
		}
	}

	/**
	 * 方法描述 给其他类调用的接口函数，用于显示外菜单
	 *
	 * @param 无
	 * @return 无
	 * @throws
	 * @since 1.0
	 * @author yaoy
	 * @date 2013-9-26
	 */
	public void showOutsideCircle() {
		if (mRemoveOutsideCircleIsDone == false && mShowOutsideCircleIsRunning == false && mHideOutsideCircleIsRunning == false) {
			mShowOutsideCircleBoolean = true;
			mShowOutsideCircleIsRunning = true;
		}
	}

	/**
	 * 方法描述 给其他类调用的接口函数，用于隐藏外菜单
	 *
	 * @param 无
	 * @return 无
	 * @throws
	 * @since 1.0
	 * @author yaoy
	 * @date 2013-9-26
	 */
	public void hideOutsideCircle() {
		if (mDrawOutsideCircleIsDone == false && mShowOutsideCircleIsRunning == false && mHideOutsideCircleIsRunning == false) {
			mHideOutsideCircleBoolean = true;
			mHideOutsideCircleIsRunning = true;
		}
	}

	/**
	 * 方法描述 里环绘制具体实现函数
	 *
	 * @param canvas
	 *            :画布 rect：参照矩形
	 * @return
	 * @throws
	 * @since 1.0
	 * @author yaoy
	 * @date 2013-9-26
	 */
	private void drawInsideCircle(Canvas canvas, RectF rect) {
		// TODO Auto-generated method stub
		// 画完以后变更逻辑
		if (mDrawInsideCircleDynamicAngle == 360) {
			mDrawInsideCircleIsDone = false;
			mRemoveInsideCircleIsDone = true;
			mShowInsideCircleBoolean = false;
			mRemoveInsideCircleDynamicAngle = 0;
			mShowInsideCircleIsRunning = false;
			mHideInsideCircleIsRunning = false;
			RingOperationLayout.mLevel2ISRuning = false;
		}
		this.paint.setColor(CIRCLE_COLOR);
		// 设置圆环的挖取大小
		this.paint.setStrokeWidth(mInsideRingWidth);
		// 绘制圆环
		canvas.drawArc(rect, 360, mDrawInsideCircleDynamicAngle - 360, false, paint);

		if (mShowInsideCircleBoolean == true) {
			// 设置圆环的画图速度
			mDrawInsideCircleDynamicAngle += 4;
		}

	}

	/**
	 * 方法描述 里环消除具体实现函数
	 *
	 * @param canvas
	 *            :画布 rect：参照矩形
	 * @return
	 * @throws
	 * @since 1.0
	 * @author yaoy
	 * @date 2013-9-26
	 */
	private void removeInsideCircle(Canvas canvas, RectF rect) {
		// TODO Auto-generated method stub
		// 画完以后变更逻辑
		if (mRemoveInsideCircleDynamicAngle == 360) {
			mDrawInsideCircleIsDone = true;
			mRemoveInsideCircleIsDone = false;
			mHideInsideCircleBoolean = false;
			mDrawInsideCircleDynamicAngle = 0;
			mHideInsideCircleIsRunning = false;
			mShowInsideCircleIsRunning = false;
			RingOperationLayout.mLevel2ISRuning = false;

		}
		this.paint.setColor(CIRCLE_COLOR);
		// 设置圆环的挖取大小
		this.paint.setStrokeWidth(mInsideRingWidth);
		// 绘制圆环
		canvas.drawArc(rect, -180, mRemoveInsideCircleDynamicAngle, false, paint);

		if (mHideInsideCircleBoolean == true) {
			// 设置圆环的画图速度
			mRemoveInsideCircleDynamicAngle += 4;
		}

	}

	/**
	 * 方法描述 外环绘制具体实现函数
	 *
	 * @param canvas
	 *            :画布 rect：参照矩形
	 * @return
	 * @throws
	 * @since 1.0
	 * @author yaoy
	 * @date 2013-9-26
	 */
	private void drawOutsideCircle(Canvas canvas, RectF rect) {
		// TODO Auto-generated method stub
		// 画完以后变更逻辑
		if (mDrawOutsideCircleDynamicAngle == 360) {
			mDrawOutsideCircleIsDone = false;
			mRemoveOutsideCircleIsDone = true;
			mShowOutsideCircleBoolean = false;
			mRemoveOutsideCircleDynamicAngle = 0;
			mShowOutsideCircleIsRunning = false;
			mHideOutsideCircleIsRunning = false;
			RingOperationLayout.mLevel3ISRuning = false;

		}
		this.paint.setColor(CIRCLE_COLOR);
		// 设置圆环的挖取大小
		this.paint.setStrokeWidth(mOutsideRingWidth);
		// 绘制圆环
		canvas.drawArc(rect, 360, mDrawOutsideCircleDynamicAngle - 360, false, paint);

		if (mShowOutsideCircleBoolean == true) {
			// 设置圆环的画图速度
			mDrawOutsideCircleDynamicAngle += 2;
		}

	}

	/**
	 * 方法描述 外环消除具体实现函数
	 *
	 * @param canvas
	 *            :画布 rect：参照矩形
	 * @return
	 * @throws
	 * @since 1.0
	 * @author yaoy
	 * @date 2013-9-26
	 */
	private void removeOutsideCircle(Canvas canvas, RectF rect) {
		// TODO Auto-generated method stub
		// 画完以后变更逻辑
		if (mRemoveOutsideCircleDynamicAngle == 360) {
			mDrawOutsideCircleIsDone = true;
			mRemoveOutsideCircleIsDone = false;
			mHideOutsideCircleBoolean = false;
			mDrawOutsideCircleDynamicAngle = 0;
			mHideOutsideCircleIsRunning = false;
			mShowOutsideCircleIsRunning = false;
			RingOperationLayout.mLevel3ISRuning = false;

		}
		this.paint.setColor(CIRCLE_COLOR);
		// 设置圆环的挖取大小
		this.paint.setStrokeWidth(mOutsideRingWidth);
		// 绘制圆环
		canvas.drawArc(rect, -180, mRemoveOutsideCircleDynamicAngle, false, paint);

		if (mHideOutsideCircleBoolean == true) {
			// 设置圆环的画图速度
			mRemoveOutsideCircleDynamicAngle += 2;
		}

	}

	/**
	 * 方法描述 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 *
	 * @param context
	 *            :上下文
	 *            rect:需要转换的dip值
	 * @return
	 * @throws
	 * @since 1.0
	 * @author yaoy
	 * @date 2013-9-26
	 */
	public static int dip2px(Context context, float dpValue) {
		// 获取设备比率值
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 方法描述 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 *
	 * @param context
	 *            :上下文 rect：需要转换的px值
	 * @return
	 * @throws
	 * @since 1.0
	 * @author yaoy
	 * @date 2013-9-26
	 */
	public static int px2dip(Context context, float pxValue) {
		// 获取设备比率值
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

}
