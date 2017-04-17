package com.jkframework.control;


import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.jkframework.R;


public class JKViewSwitcher extends JKFrameLayout
{
	/**不显示的界面是否保留Layout*/
	private boolean bShow = false;
	/**当前显示页面*/
	private int nDisplay = -1;
	
	public JKViewSwitcher(Context context) {
		super(context);
	}	

	public JKViewSwitcher(Context context, AttributeSet attrs) {
		super(context, attrs);

		TypedArray taArray = context.obtainStyledAttributes(attrs, R.styleable.JKViewSwitcher);
		nDisplay = taArray.getInt(R.styleable.JKViewSwitcher_displayedChild, 0);
		taArray.recycle();
	}

	public JKViewSwitcher(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		TypedArray taArray = context.obtainStyledAttributes(attrs, R.styleable.JKViewSwitcher);
		nDisplay = taArray.getInt(R.styleable.JKViewSwitcher_displayedChild, 0);
		taArray.recycle();
	}

	/**
	 * 设置不显示的页面是否计算宽高
	 * @param bShow true表示计算
	 */
	public void SetAlwaysLayout(boolean bShow)
	{
		this.bShow = bShow;
		int nCount = getChildCount();
		for (int i=0; i<nCount; ++i)
		{
			if (i == nDisplay)
				continue;
			if (bShow)
				getChildAt(i).setVisibility(View.INVISIBLE);
			else
				getChildAt(i).setVisibility(View.GONE);
		}
	}
	
	@Override
	public void addView(@NonNull View child, int index, ViewGroup.LayoutParams params) {
		super.addView(child, index, params);
		if (getChildCount()-1 == nDisplay)
			child.setVisibility(View.VISIBLE);
		else {
			if (bShow)
				child.setVisibility(View.INVISIBLE);
			else
				child.setVisibility(View.GONE);
		}
	}
	 
	/**
	 * 切换页面
	 * @param nShow 显示第几个页面(从0开始)
	 */
	public void SetDisplayedChild(int nShow)
	{
		nDisplay = nShow;				
		int nCount = getChildCount();
		for (int i=0; i<nCount; ++i)
		{
			if (i==nShow)
				getChildAt(i).setVisibility(View.VISIBLE);
			else {
				if (bShow)
					getChildAt(i).setVisibility(View.INVISIBLE);
				else
					getChildAt(i).setVisibility(View.GONE);
			}
		}
	}

	/**
	 * 获取当前显示的页面索引
	 * @return 索引号(0开始)
	 */
	public int GetDisplayIndex()
	{
		return nDisplay;
	}
}