package com.jkframework.adapter;

import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.jkframework.control.JKViewPager;
import com.viewpagerindicator.CirclePageIndicator;

public abstract class JKPagerAdapter extends PagerAdapter {

    /**ViewPager对象*/
	protected JKViewPager jkvpPager;
    /**ViewPager对象*/
    private SparseArray<View> h_vList = new SparseArray<>();

    /**循环节点倍数*/
    private final int RECYCLE_ITEMBIT = CirclePageIndicator.RECYCLE_ITEMBIT;

	/**
	 * 设置ViewPager对象
	 * @param jkvpPager ViewPager对象
     */
	public void SetViewPager(JKViewPager jkvpPager)
	{
		this.jkvpPager = jkvpPager;
	}

    /**
     * 获取适配器个数
     * @param nCount 真实个数
     * @return 适配器个数
     */
    public int GetAdapterCount(int nCount)
    {
        jkvpPager.SetRealCount(nCount);
//        self.upcPageControl?.numberOfPages = nRealCount;
        if (jkvpPager.GetRecycleMode())
        {
            return nCount * RECYCLE_ITEMBIT;
        }
        else {
            return nCount;
        }
    }

	@Override
    public abstract int getCount();

	public abstract Object InstantiateItem(ViewGroup vgView, int position);

    @Override
    public final Object instantiateItem(ViewGroup vgView, int position)
    {
        View vView = (View) InstantiateItem(vgView,position);
        h_vList.put(position,vView);
        return vView;
    }

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}
	
	@Override
	public void destroyItem(ViewGroup view, int position, Object object)
    {
        view.removeView(h_vList.get(position));
    }

	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}

    @Override
    public void notifyDataSetChanged()
    {
        super.notifyDataSetChanged();
        if (jkvpPager.GetRecycleMode())
        {
            jkvpPager.SetCurrentItem(jkvpPager.GetRealCount() * RECYCLE_ITEMBIT/2, false);
        }
    }
}
