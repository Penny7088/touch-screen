package com.jkframework.control;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.PtrUIHandler;
import in.srain.cube.views.ptr.indicator.PtrIndicator;

public class JKRefresh extends PtrFrameLayout implements PtrUIHandler {

    /**下拉刷新接口*/
    private JKRefreshListener jkrlListener;
    /**界面变更回调*/
    private JKRefreshUIHandler jkrhHandler;

	public JKRefresh(Context context) {
		super(context);
        Init();
	}

	public JKRefresh(Context context, AttributeSet attrs) {
		super(context, attrs);
        Init();
	}

	public JKRefresh(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
        Init();
	}

    private void Init()
    {
        setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                if (jkrlListener != null)
                    jkrlListener.onRefreshBegin(JKRefresh.this);
            }
        });
    }

    /**
     * 设置加载更多监听
     * @param l 加载更多事件
     */
    public void SetRefreshListener(JKRefreshListener l)
    {
        this.jkrlListener = l;
    }

    public void SetView(View vView)
    {
        setHeaderView(vView);
    }

	public void AddRefreshUIHandler(JKRefreshUIHandler jkrhHandler)
	{
        this.jkrhHandler = jkrhHandler;
        addPtrUIHandler(this);
	}

    @Override
    public void onUIReset(PtrFrameLayout frame) {
        if (jkrhHandler != null)
            jkrhHandler.onRefreshReset(this);
    }

    @Override
    public void onUIRefreshPrepare(PtrFrameLayout frame) {
        if (jkrhHandler != null)
            jkrhHandler.onRefreshReset(this);
    }

    @Override
    public void onUIRefreshBegin(PtrFrameLayout frame) {
        if (jkrhHandler != null)
            jkrhHandler.onRefreshBegin(this);
    }

    @Override
    public void onUIRefreshComplete(PtrFrameLayout frame) {
        if (jkrhHandler != null)
            jkrhHandler.onRefreshComplate(this);
    }

    @Override
    public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator) {
        final int mOffsetToRefresh = frame.getOffsetToRefresh(); //刷新需要滑动的偏移量
        int currentPosY = ptrIndicator.getCurrentPosY();//当前系统偏移值
        float fProgress = (float)currentPosY/mOffsetToRefresh;
        if (isUnderTouch && status == PtrFrameLayout.PTR_STATUS_PREPARE) {
            int nStatus = 0;
            if (ptrIndicator.getCurrentPosY() < mOffsetToRefresh && ptrIndicator.getLastPosY() >= mOffsetToRefresh) {
                nStatus = -1;
            }
            else if (ptrIndicator.getCurrentPosY() > mOffsetToRefresh && ptrIndicator.getLastPosY() <= mOffsetToRefresh) {
                nStatus = 1;
            }
            if (jkrhHandler != null)
                jkrhHandler.onRefreshChange(this, fProgress, nStatus);
        }
    }

    public interface JKRefreshUIHandler {

        // 重置下拉
        void onRefreshReset(JKRefresh container);

        /**
         * 下拉中
         * @param container 下拉刷新控件
         * @param fProgress 下拉进度
         * @param nStatus 触发刷新状态(1为满足刷新条件,0为不处理,-1为失去刷新条件)
         */
        void onRefreshChange(JKRefresh container,float fProgress,int nStatus);

        // 下拉加载
        void onRefreshBegin(JKRefresh container);

        // 加载完成
        void onRefreshComplate(JKRefresh container);
    }

    public interface JKRefreshListener {

        void onRefreshBegin(JKRefresh container);
    }
}