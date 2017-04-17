package com.jkframework.control;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.cundong.recyclerview.EndlessRecyclerOnScrollListener;
import com.cundong.recyclerview.HeaderAndFooterRecyclerViewAdapter;
import com.cundong.recyclerview.RecyclerViewUtils;
import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;

public class JKRecyclerView extends ObservableRecyclerView {

	/**加载更多UI*/
	private JKLoadMoreUIHandler jklmuihLoadmore;
    /**加载更多接口*/
    private JKLoadMoreListener jklmlListener;
    /**带有Footer的适配器对象*/
    private HeaderAndFooterRecyclerViewAdapter adapter = new HeaderAndFooterRecyclerViewAdapter();
    /**加载状态*/
    private State state = State.Normal;
    /**分页布局*/
    private View vFooterView;

	public JKRecyclerView(Context context) {
		super(context);
        Init();
	}

	public JKRecyclerView(Context context, AttributeSet attrs) {
		super(context, attrs);
        Init();
	}

	public JKRecyclerView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
        Init();
	}

    @Override
    @SuppressWarnings("unchecked")
    public void setAdapter(Adapter adapter)
    {
        this.adapter.setAdapter(adapter);
        super.setAdapter(this.adapter);
    }

    /**
     * 设置表头
     * @param vView 表头
     */
    public void SetHeaderView(View vView)
    {
        RecyclerViewUtils.removeHeaderView(this);
        adapter.addHeaderView(vView);
        state = State.Normal;
    }

    /**
     * 设置分页布局
     * @param vView 分页布局view
     */
    public void SetLoadMoreView(View vView)
    {
        RecyclerViewUtils.removeFooterView(this);
        this.vFooterView = vView;
        vView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    /**
     * 添加加载更多UI
     * @param jklmuihLoadmore 加载更多UI接口
     */
	public void AddLoadMoreUIHandler(JKLoadMoreUIHandler jklmuihLoadmore)
	{
		this.jklmuihLoadmore = jklmuihLoadmore;
        if (jklmuihLoadmore != null)
            jklmuihLoadmore.onLoadMoreReset(JKRecyclerView.this);
	}

    /**
     * 设置加载更多监听
     * @param l 加载更多事件
     */
    public void SetLoadMoreListener(JKLoadMoreListener l)
    {
        this.jklmlListener = l;
    }

    private void Init()
    {
		EndlessRecyclerOnScrollListener mOnScrollListener = new EndlessRecyclerOnScrollListener() {

			@Override
			public void onLoadNextPage(View view) {
				super.onLoadNextPage(view);
                if (adapter != null && adapter.getFooterView() != null) {
                    if (state == State.Loading || state == State.TheEnd)
                        return;
                    if (state == State.Normal || state == State.Error) {
                        adapter.getFooterView().setOnClickListener(null);
                        state = State.Loading;
                        if (jklmuihLoadmore != null)
                            jklmuihLoadmore.onLoadMoreBegin(JKRecyclerView.this);
                        if (jklmlListener != null)
                            jklmlListener.onLoadMoreBegin(JKRecyclerView.this);
                    }
                }
			}
		};
		addOnScrollListener(mOnScrollListener);
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
            RecyclerViewUtils.removeFooterView(this);
        }
        else {
            RecyclerViewUtils.setFooterView(JKRecyclerView.this,vFooterView);
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
        adapter.getFooterView().setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.getFooterView().setOnClickListener(null);
                state = State.Loading;
                if (jklmuihLoadmore != null)
                    jklmuihLoadmore.onLoadMoreReset(JKRecyclerView.this);
                if (jklmlListener != null)
                    jklmlListener.onLoadMoreBegin(JKRecyclerView.this);
            }
        });
    }

    public enum State {
        Normal/*正常*/, TheEnd/*加载到最底了*/, Loading/*加载中..*/, Error/*网络异常*/
    }

    public interface JKLoadMoreUIHandler {

        // 等待加载更多
        void onLoadMoreReset(JKRecyclerView container);

        // 加载中
        void onLoadMoreBegin(JKRecyclerView container);

        // 加载完成: 没有更多
        void onLoadMoreComplate(JKRecyclerView container,boolean hasMore);

        // 加载失败
        void onLoadMoreError(JKRecyclerView container, String errorMessage);
    }

    public interface JKLoadMoreListener {

        void onLoadMoreBegin(JKRecyclerView container);
    }
}