package com.example.yy.rotatemenu.view;

import java.util.Timer;
import java.util.TimerTask;




import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.yy.rotatemenu.R;
import com.example.yy.rotatemenu.RotateAnimations;

/**
 *
 * 类描述 圆环逻辑实现类
 *
 * @author yaoy
 * @since 1.0 2013-9-26 下午3:51:44
 * @version 1.0 2013-9-26 下午3:51:44
 */
public class RingOperationLayout extends LinearLayout {

	// 可点击的总菜单按钮
	private ImageButton mHomeButton;

	// 可点击的次菜单按钮
	private ImageButton mMenuButton;

	// 一级菜单
	private RelativeLayout mLevel1;

	// 二级菜单
	private RelativeLayout mLevel2;

	// 三级菜单
	private RelativeLayout mLevel3;

	// 判断二、三级菜单状态
	private boolean mIsLevel2Show = false;

	private boolean mIsLevel3Show = false;

	public static boolean mLevel2ISRuning = false;

	public static boolean mLevel3ISRuning = false;

	private View mConvertView;

	private LayoutInflater mLayoutInflater = null;

	private CoverRingImageView mDrawImageView;

	private Timer mTimer;

	// 菜单的宽
	public static int mLevel1Width;

	public static int mLevel2Width;

	public static int mLevel3Width;

	//是否得到了菜单的宽
	private Boolean mHasGotWidth = false;

	private static final int WANT_TO_GET_MUNU_WIDTH_MESSAGE = 1;

	public RingOperationLayout(Context context, AttributeSet attrs) {
		super(context);
		//初始化
		initialize(context);

		mMenuButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (mLevel3ISRuning == false) {
					mLevel3ISRuning = true;

					if (mIsLevel3Show) {
						// mMenuButton.setClickable(false);
						// 隐藏3级导航菜单
						mDrawImageView.hideOutsideCircle();
						RotateAnimations.startAnimation(mLevel3, 3500, 0);
					} else {
						// 显示3级导航菜单
						// showCircleLevelOne();
						mDrawImageView.showOutsideCircle();
						RotateAnimations.startAnimation(mLevel3, 3500, 0);
					}

					mIsLevel3Show = !mIsLevel3Show;
				}
			}
		});

		mHomeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (mLevel3ISRuning == false && mLevel2ISRuning == false) {
					mLevel2ISRuning = true;

					if (!mIsLevel2Show) {
						// 显示2级导航菜单
						mDrawImageView.showInsideCircle();
						RotateAnimations.startAnimation(mLevel2, 2000, 0);
					} else {
						if (mIsLevel3Show) {
							// 隐藏3级导航菜单
							mDrawImageView.hideOutsideCircle();
							RotateAnimations.startAnimation(mLevel3, 3500, 0);
							// 隐藏2级导航菜单
							mDrawImageView.hideInsideCircle();
							RotateAnimations.startAnimation(mLevel2, 2000, 500);
							mIsLevel3Show = !mIsLevel3Show;
						} else {
							// 隐藏2级导航菜单
							mDrawImageView.hideInsideCircle();
							RotateAnimations.startAnimation(mLevel2, 2000, 0);
						}
					}
					mIsLevel2Show = !mIsLevel2Show;
				}
			}
		});

		addView(mConvertView);

		final Handler myHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				if (msg.what == WANT_TO_GET_MUNU_WIDTH_MESSAGE) {
					if (mLevel3.getWidth() != 0 && mLevel2.getWidth() != 0 && mLevel1.getWidth() != 0) {
						mLevel1Width = mLevel1.getWidth();
						mLevel2Width = mLevel2.getWidth();
						mLevel3Width = mLevel3.getWidth();
						System.out.println(mLevel3Width + "");
						// 取消定时器
						mTimer.cancel();
						mHasGotWidth = true;
					}
				}
			}
		};

		if (mHasGotWidth == false) {
			mTimer = new Timer();
			//在未得到菜单宽时，不断线程请求获取
			TimerTask task = new TimerTask() {
				public void run() {
					Message message = new Message();
					message.what = WANT_TO_GET_MUNU_WIDTH_MESSAGE;
					myHandler.sendMessage(message);
				}
			};
			// 延迟每次延迟10 毫秒 隔1秒执行一次
			mTimer.schedule(task, 10, 1000);
		}
	}

	/**
	 *
	 * 方法描述 初始化控件
	 *
	 * @param
	 * @return
	 * @throws
	 * @since 1.0
	 * @author yaoy
	 * @date 2013-9-26
	 */
	private void initialize(Context context) {
		// TODO Auto-generated method stub
		mLayoutInflater = LayoutInflater.from(context);
		mConvertView = mLayoutInflater.inflate(R.layout.custom_ring_operation_layout, null);
		mDrawImageView = (CoverRingImageView) mConvertView.findViewById(R.id.drawImageView);

		mHomeButton = (ImageButton) mConvertView.findViewById(R.id.home);
		mMenuButton = (ImageButton) mConvertView.findViewById(R.id.menu);

		mLevel1 = (RelativeLayout) mConvertView.findViewById(R.id.level1);
		mLevel2 = (RelativeLayout) mConvertView.findViewById(R.id.level2);
		mLevel3 = (RelativeLayout) mConvertView.findViewById(R.id.level3);

	}

}
