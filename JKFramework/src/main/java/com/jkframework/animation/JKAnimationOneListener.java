package com.jkframework.animation;

public interface JKAnimationOneListener {
	/**
	 * 动作执行完毕的回调函数
	 */
	void FinishAnimation();

	/**
	 * 动作初始化完毕的回调函数
	 */
	void InitAnimation(boolean bValid);
}
