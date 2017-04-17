package com.jkframework.callback;

import android.view.View;


public interface JKOnItemClickListener {
	
	/**
	 * 子项监听事件
	 * @param view 布局对象
	 * @param nPos 子项事件
	 * @return true表示事件处理
	 */
	boolean OnItemClick(View view, int nPos);
	
}
