package com.jkframework.control;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;

import com.github.ksoichiro.android.observablescrollview.ObservableListView;

public class JKListView extends ObservableListView {

    /**表头界面*/
    private View vHeaderView = null;
    /**首个显示编号*/
    private int nFirstVisibleItem = 0;
    /**显示元素个数*/
    private int nVisibleItemCount = 0;
    /**总元素个数*/
    private int nTotalItemCount = 0;
    /**加载更多UI*/
    private JKLoadMoreUIHandler jklmuihLoadmore;
    /**加载更多接口*/
    private JKLoadMoreListener jklmlListener;
    /**加载状态*/
    private State state = State.Normal;
    /**分页布局*/
    private View vFooterView;


	public JKListView(Context context) {
		super(context);
        Init();
	}

	public JKListView(Context context, AttributeSet attrs) {
		super(context, attrs);
        Init();
	}

	public JKListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
        Init();
	}

    /**
     * 添加分割线
     * @param  nColor 分割线颜色
     * @param  nHeight 分割线高度(像素)
     */
    public void AddItemDecoration(int nColor,int nHeight)
    {
        setDivider(new ColorDrawable(nColor));
        setDividerHeight(nHeight);
    }

    /**
     * 添加表头
     * @param  vView 表头界面
     */
    public void SetHeaderView(View vView)
    {
        if (vHeaderView != null)
            removeHeaderView(vHeaderView);
        vHeaderView = vView;
        addHeaderView(vView);
    }

    /**
     * 设置分页布局
     * @param vView 分页布局view
     */
    public void SetLoadMoreView(View vView)
    {
        if (vFooterView != null)
            removeFooterView(vFooterView);
        this.vFooterView = vView;
    }

    /**
     * 添加加载更多UI
     * @param jklmuihLoadmore 加载更多UI接口
     */
    public void AddLoadMoreUIHandler(JKLoadMoreUIHandler jklmuihLoadmore)
    {
        this.jklmuihLoadmore = jklmuihLoadmore;
        if (jklmuihLoadmore != null)
            jklmuihLoadmore.onLoadMoreReset(this);
    }

    /**
     * 设置加载更多监听
     * @param l 加载更多事件
     */
    public void SetLoadMoreListener(JKLoadMoreListener l)
    {
        this.jklmlListener = l;
    }

    /**
     * 初始化
     */
	private void Init()
	{
		setDivider(null);
		setOnScrollListener(new AbsListView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
                if ((nVisibleItemCount > 0 && scrollState == RecyclerView.SCROLL_STATE_IDLE && (nFirstVisibleItem + nVisibleItemCount) >= nTotalItemCount - 1) && !ViewCompat.canScrollVertically(JKListView.this, 1)) {
                    if (getAdapter() != null && vFooterView != null) {
                        if (state == State.Loading || state == State.TheEnd)
                            return;
                        if (state == State.Normal || state == State.Error) {
                            vFooterView.setOnClickListener(null);
                            state = State.Loading;
                            if (jklmuihLoadmore != null)
                                jklmuihLoadmore.onLoadMoreBegin(JKListView.this);
                            if (jklmlListener != null)
                                jklmlListener.onLoadMoreBegin(JKListView.this);
                        }
                    }
                }
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                nFirstVisibleItem = firstVisibleItem;
                nVisibleItemCount = visibleItemCount;
                nTotalItemCount = totalItemCount;
			}
		});
	}

    /**
     * 加载完成
     * @param hasMore 是否还有更多数据
     */
    public void LoadMoreComplete(boolean hasMore)
    {
        if (jklmuihLoadmore != null)
            jklmuihLoadmore.onLoadMoreComplate(this,hasMore);
        if (!hasMore)
        {
            state = State.TheEnd;
            removeFooterView(vFooterView);
        }
        else {
            addFooterView(vFooterView);
            state = State.Normal;
            if (jklmuihLoadmore != null)
                jklmuihLoadmore.onLoadMoreReset(this);
        }
    }

    /**
     * 加载错误
     * @param errorMessage 是否还有更多数据
     */
    public void LoadError(String errorMessage)
    {
        state = State.Error;
        if (jklmuihLoadmore != null)
            jklmuihLoadmore.onLoadMoreError(this,errorMessage);
        vFooterView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                vFooterView.setOnClickListener(null);
                state = State.Loading;
                if (jklmuihLoadmore != null)
                    jklmuihLoadmore.onLoadMoreReset(JKListView.this);
                if (jklmlListener != null)
                    jklmlListener.onLoadMoreBegin(JKListView.this);
            }
        });
    }

    public enum State {
        Normal/*正常*/, TheEnd/*加载到最底了*/, Loading/*加载中..*/, Error/*网络异常*/
    }

    public interface JKLoadMoreUIHandler {

        // 等待加载更多
        void onLoadMoreReset(JKListView container);

        // 加载中
        void onLoadMoreBegin(JKListView container);

        // 加载完成: 没有更多
        void onLoadMoreComplate(JKListView container,boolean hasMore);

        // 加载失败
        void onLoadMoreError(JKListView container, String errorMessage);
    }

    public interface JKLoadMoreListener {

        void onLoadMoreBegin(JKListView container);
    }
}