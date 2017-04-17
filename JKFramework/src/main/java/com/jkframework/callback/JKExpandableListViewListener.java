package com.jkframework.callback;

import android.view.View;
import android.widget.AdapterView;


public interface JKExpandableListViewListener {
	
	/**
	 * 长按监听事件
	 * @param parent 适配器对象
	 * @param view 长按的布局对象
	 * @param nGroupPos 长按的父节点
	 * @param nChildPos 长按的子节点
	 * @param id 长按对象的ID
	 * @return true表示事件处理
	 */
	boolean OnItemLongClick(AdapterView<?> parent, View view,int nGroupPos,int nChildPos, long id);
	
}
