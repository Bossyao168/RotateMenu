package com.example.yy.rotatemenu;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

/**
 * 类描述 旋转过渡效果类
 *
 * @author yaoy
 * @since 1.0 2013-9-26 下午3:52:41
 * @version 1.0 2013-9-26 下午3:52:41
 */
public class RotateAnimations {

	/**
	 * 方法描述 封装旋转效果实现的方法
	 *
	 * @param viewGroup
	 *            :需要旋转的viewGroup duration:旋转持续时间 startOffSet:旋转延迟时间
	 * @return
	 * @throws
	 * @since 1.0
	 * @author yaoy
	 * @date 2013-9-26
	 */
	public static void startAnimation(ViewGroup viewGroup, int duration, int startOffSet) {
		for (int i = 0; i < viewGroup.getChildCount(); i++) {
			viewGroup.getChildAt(i).setVisibility(View.VISIBLE);// 设置显示
			viewGroup.getChildAt(i).setFocusable(true);// 获得焦点
			viewGroup.getChildAt(i).setClickable(true);// 可以点击
		}

		Animation animation;

		/**
		 * 旋转动画
		 * RotateAnimation(fromDegrees, toDegrees, pivotXType, pivotXValue, pivotYType, pivotYValue)
		 * fromDegrees 开始旋转角度
		 * toDegrees 旋转到的角度
		 * pivotXType X轴 参照物
		 * pivotXValue x轴 旋转的参考点
		 * pivotYType Y轴 参照物
		 * pivotYValue Y轴 旋转的参考点
		 */
		animation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		animation.setFillAfter(true);// 停留在动画结束位置
		animation.setDuration(duration);
		animation.setStartOffset(startOffSet);
		viewGroup.startAnimation(animation);

	}

}
