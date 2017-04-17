package com.jkframework.control;

import android.annotation.TargetApi;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.Scroller;

import com.jkframework.adapter.JKPagerAdapter;
import com.jkframework.callback.JKViewPagerListener;
import com.viewpagerindicator.PageIndicator;

import java.lang.reflect.Field;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;


public class JKViewPager extends AutoScrollViewPager{

    /**自定义滑动事件*/
    public JKViewPagerListener m_OnChanged;
	/**是否允许自动滑动*/
	private boolean bAutoScroll = false;
	/**当前屏幕方向*/
	private int nStartItem = -1;
	/**-1为左边尽头,1为右边尽头*/
	private int nEnd = 0;
	/**状态0为滑动结束,状态为1滑动中*/
	private int nState = 0;
    /**真实数据个数*/
    private int nRealCount = -1;
	/**指示器*/
	private PageIndicator piIndicator = null;
	
	public JKViewPager(Context context) {
		super(context);
		Init();
	}

	public JKViewPager(Context context,AttributeSet attrs) {
		super(context, attrs);
		Init();
	}
	
	/**
	 * 设置滑动监听
	 * @param l 监听对象
	 */
	public void SetOnChangeListener(JKViewPagerListener l)
	{
		m_OnChanged = l;
	}

    /**
     * 设置适配器
     * @param jkpaAdapter 适配器对象
     */
	public void SetAdapter(JKPagerAdapter jkpaAdapter)
	{
        jkpaAdapter.SetViewPager(this);
		super.setAdapter(jkpaAdapter);
        jkpaAdapter.notifyDataSetChanged();
	}

    /**
     * 设置指示器
     * @param piIndicator 指示器对象
     */
    public void SetPageIndicator(PageIndicator piIndicator)
    {
        this.piIndicator = piIndicator;
        this.piIndicator.setViewPager(this);
        this.piIndicator.setOnPageChangeListener(mPageChange);
    }

	/**
	 * 设置循环轮播模
	 * @param bRecycle true为循环轮播
	 */
	public void SetMode(boolean bRecycle)
	{
        if (bRecycle)
            setSlideBorderMode(SLIDE_BORDER_MODE_CYCLE);
        else
            setSlideBorderMode(SLIDE_BORDER_MODE_NONE);
	}

    /**
     * 设置真实个数
     * @param nCount
     */
    public void SetRealCount(int nCount)
    {
        nRealCount = nCount;
    }

    /**
     * 设置真实个数
     * @return 获取真实个数
     */
    public int GetRealCount() {
        return nRealCount;
    }
	
	/**
	 * 获取当前循环模式
	 */
	public boolean GetRecycleMode()
	{
        return getSlideBorderMode() == SLIDE_BORDER_MODE_CYCLE;
	}
	
    /**
     * 是否启用了自动滑动
     * @return true表示自动滑动
     */
    public boolean IsAutoScroll()
    {
        return bAutoScroll;
    }
	
	/**
	 * 开始自动滑动
	 * @param nInterval  滑动间隔(毫秒)
	 */
	public void StartAutoScroll(int nInterval)
	{
        bAutoScroll = true;
        setInterval(nInterval);
        startAutoScroll(nInterval);
	}
	
	/**
	 * 停止自动滑动
	 */
	public void StopAutoScroll()
	{
        bAutoScroll = false;
        stopAutoScroll();
	}

	/**
	 * 设置Viewpager索引
	 * @param item 索引号(0开始)
	 * @param smoothScroll 是否播放动画
	 */
	public void SetCurrentItem(int item, boolean smoothScroll)
	{
        if (GetRecycleMode())
        {
            if (nRealCount != 0)
            {
                super.setCurrentItem(item, smoothScroll);
            }
        }
        else {
            if (item >= 0 && item < nRealCount)
            {
                super.setCurrentItem(item, smoothScroll);
            }
        }
	}

    /**
     * 返回ViewPager索引
     * @return ViewPager索引
     */
    public int GetCurrentItem()
    {
        return super.getCurrentItem();
    }
	
	@Override
	protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
        return super.canScroll(v, checkV, dx, x, y);
    }
	
	/**
	 * 反向注册ViewPager滑动事件
	 * @param piIndicator 指示器对象
	 */
	public void SetViewPagerIndicator(PageIndicator piIndicator)
	{
		piIndicator.setViewPager(this);
		piIndicator.setOnPageChangeListener(mPageChange);
	}

    /**
     * 初始化Pagecontrol
     */
	private void Init()
	{
        //调整滑动速度
        try{
            Class<?> viewpager = ViewPager.class;
            Field scroller = viewpager.getDeclaredField("mScroller");
            scroller.setAccessible(true);
            Field interpolator = viewpager.getDeclaredField("sInterpolator");
            interpolator.setAccessible(true);

            ScrollerCustomDuration mScroller = new ScrollerCustomDuration(getContext(),
                    (Interpolator) interpolator.get(null));
            scroller.set(this, mScroller);
        }catch (Exception e){
            e.printStackTrace();
        }

        SetMode(false);
		addOnPageChangeListener(mPageChange);
	}
	
		
	private class ScrollerCustomDuration extends Scroller {

	    public ScrollerCustomDuration(Context context) {
	        super(context);
	    }

	    public ScrollerCustomDuration(Context context, Interpolator interpolator) {
	        super(context, interpolator);
	    }

	    @TargetApi(11)
	    public ScrollerCustomDuration(Context context, Interpolator interpolator, boolean flywheel) {
	        super(context, interpolator, flywheel);
	    }

	    @Override
	    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
	        super.startScroll(startX, startY, dx, dy, 800);
	    }

	}

    public OnPageChangeListener mPageChange = new OnPageChangeListener() {

        @Override
        public void onPageSelected(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            if (m_OnChanged != null)
            {
                if (arg0 == 0 && nState == 1)
                {
                    nEnd = -1;
                }
                else if (arg0 == getAdapter().getCount()-1 && nState == 1)
                {
                    nEnd = 1;
                }
            }
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
            nState = arg0;
            switch (arg0) {
                case ViewPager.SCROLL_STATE_IDLE:
                    if (m_OnChanged != null)
                    {
                        m_OnChanged.OnChanged(nStartItem% GetRealCount(), getCurrentItem() - nStartItem + nEnd, nStartItem != getCurrentItem());
                        nEnd = 0;
                    }
                    if (bAutoScroll)
                        startAutoScroll((int) getInterval());
                    break;
                case ViewPager.SCROLL_STATE_DRAGGING:
                    nStartItem = getCurrentItem();
                    if (bAutoScroll)
                        stopAutoScroll();
                    break;
            }
        }
    };
}