package com.jkframework.callback;


public interface JKVideoListener {
	
	/**
	 * 视频播放完毕
	 */
	void FinishVideo();
	
	/**
	 * 视频即将开始播放时的回调
	 */
	void ReadyStart();
	
	/**
	 * 视频即将暂停时的回调
	 */
	void ReadyPause();
}
