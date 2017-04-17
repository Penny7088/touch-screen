package com.jkframework.callback;


public interface JKPullToRefreshListener {
	
	/**
	 * 开始下拉刷新
	 */
	void BeginRefresh();
	
	/**
	 * 开始上拉加载
	 */
	void BeginLoadingMore();
}
