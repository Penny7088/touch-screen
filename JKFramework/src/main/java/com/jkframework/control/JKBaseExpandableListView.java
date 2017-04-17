package com.jkframework.control;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;

import com.jkframework.callback.JKExpandableListViewListener;
import com.jkframework.callback.JKPullToRefreshListener;

public abstract class JKBaseExpandableListView extends ExpandableListView{

    /**适配器成员*/
    private BaseExpandableListAdapter baAdapter;
    /**下拉信息界面*/
    private View vHeadView;
    /**上拉信息界面*/
    private View vBottomView;
    /**头部高度*/
    private int nHeadHeight;
    /**尾部高度*/
    private int nBottomHeight;
    /**下拉状态*/
    private int nState;
    /**数据个数*/
    private int nTotal;
    /**显示个数*/
    private int nVisableNum;
    /**下拉框位置*/
    private int nScrollY;
    /**上拉类型(0为点击加载,1为上拉加载)*/
    private int nType = 0;
    /**下拉起始Y坐标*/
    private float fDownStartY;
    /**上拉起始Y坐标*/
    private float fDownStartY2;
    /**是否开启下拉刷新功能*/
    private boolean bRefreshable = false;
    /**是否开启上拉更多功能*/
    private boolean bGetMore = false;
    /**是否开始下拉刷新*/
    private boolean bStartRefresh = false;
    /**是否从释放刷新回到下拉刷新*/
    private boolean bIsBack = false;
    /**是否开始上拉加载*/
    private boolean bStartMore = false;
    /**是否上拉加载中的手势*/
    private boolean bLoadingMore = false;
    /**刷新监听成员*/
    private JKPullToRefreshListener m_Listener;

    /**释放刷新*/
    private final static int RELEASE_To_REFRESH = 0;
    /**下拉刷新*/
    private final static int PULL_To_REFRESH = 1;
    /**正在下拉刷新*/
    private final static int REFRESHING = 2;
    /**刷新完成*/
    private final static int DONE = 3;
    /**上拉加载*/
    private final static int PULL_To_MORE = 4;
    /**正在上拉加载*/
    private final static int GETMORE = 5;
    /**上拉加载数据完毕*/
    private final static int MORE_FINISH = 6;
    /**下拉加载数据完毕*/
    private final static int LOADING_FINISH = 6;

    /**ListView比例系数值*/
    private final static int RATIO = 1;


	public JKBaseExpandableListView(Context context) {
		super(context);
        InitHeadView(context);
        InitBottomView(context);
        SetListener();
	}

	public JKBaseExpandableListView(Context context, AttributeSet attrs) {
		super(context, attrs);
        InitHeadView(context);
        InitBottomView(context);
        SetListener();
	}

	public JKBaseExpandableListView(Context context, AttributeSet attrs, int defStyle) {
		super(context,attrs,defStyle);
        InitHeadView(context);
        InitBottomView(context);
        SetListener();
	}

    /**
     * 设置下拉刷新响应
     * @param l 响应回调
     */
    public void SetRefreshListener(JKPullToRefreshListener l) {
        m_Listener = l;
    }

    /**
     * 设置是否启用下拉刷新功能(请在InitHead开头使用)
     * @param bUse true为使用
     */
    public void SetRefresh(boolean bUse)
    {
        bRefreshable = bUse;
    }

    /**
     * 设置是否启用上拉加载更多功能(请在InitBottom开头使用)
     * @param bUse true为使用
     * @param nTypeTmp 为0时则点击加载,为1时则为上拉加载
     */
    public void SetMore(boolean bUse,int nTypeTmp)
    {
        bGetMore = bUse;
        nType = nTypeTmp;
    }

    @Override
    public void setAdapter(ExpandableListAdapter adapter) {
        super.setAdapter(adapter);
        baAdapter = (BaseExpandableListAdapter) adapter;
    }

//	@Override
//	protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent){
//		int newDeltaY = deltaY;
//		int delta = (int) (deltaY * SCROLL_RATIO);
//		if (delta != 0)
//			newDeltaY = delta;
//		return super.overScrollBy(deltaX, newDeltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, MAX_SCROLL, isTouchEvent);
//	}

    /**
     * 初始化下拉界面眉页
     * @param inflater 界面索引对象
     * @return 下拉界面眉页
     */
    abstract protected LinearLayout InitHead(LayoutInflater inflater);

    /**
     * 初始化上拉界面眉页
     * @param inflater 界面索引对象
     * @return 上拉界面眉页
     */
    abstract protected LinearLayout InitBottom(LayoutInflater inflater);

    /**
     * 下拉状态执行功能
     */
    abstract protected void Pull();

    /**
     * 从释放状态返回下拉状态执行功能
     */
    abstract protected void ReturnPull();

    /**
     * 下拉到底部时的执行功能
     */
    abstract protected void Relase();

    /**
     * 刷新中执行功能
     */
    abstract protected void Refresh();

    /**
     * 刷新完成时执行功能
     */
    abstract protected void DoneRefresh();

    /**
     * 上拉状态执行功能
     */
    abstract protected void Push();

    /**
     * 加载更多中执行功能
     */
    abstract protected void LoadingMore();

    /**
     * 加载更多中执行功能
     */
    abstract protected void DoneMore();

    /**
     * 执行刷新功能
     */
    public void DoRefresh()
    {
        nState = REFRESHING;
        ChangeHeaderViewByState(true);
    }

    /**
     * 通知界面刷新已完成
     * @param bFinish true为数据完结不在上拉,false为允许上拉
     * @param bAnimation true为动画移动,false为无动画移动
     */
    public void RefreshComplete(boolean bFinish,boolean bAnimation) {
        if (bFinish)
        {
            bLoadingMore = false;
            nState = LOADING_FINISH;
            ChangeHeaderViewByState(bAnimation);
            baAdapter.notifyDataSetChanged();
        }
        else {
            bLoadingMore = true;
            nState = DONE;
            ChangeHeaderViewByState(bAnimation);
            baAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 通知界面刷新已完成
     * @param bFinish true为数据完结不在上拉,false为允许上拉
     * @param bAnimation true为动画移动,false为无动画移动
     */
    public void MoreComplete(boolean bFinish,boolean bAnimation) {
        if (bFinish)
        {
            bGetMore = false;
            nState = MORE_FINISH;
            ChangeBottomViewByState(bAnimation);
            baAdapter.notifyDataSetChanged();
        }
        else {
            bGetMore = true;
            nState = DONE;
            ChangeBottomViewByState(bAnimation);
            baAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull MotionEvent event) {
        return nState == REFRESHING || nState == GETMORE || super.onInterceptTouchEvent(event);
    }

    /**
     * 判断是否滑动到底部
     * @return
     */
    public boolean IsBottom()
    {
        return Math.abs(getChildAt(nVisableNum - 1).getBottom() - (getBottom() - getTop())) <= 1;
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        if (nState == REFRESHING || nState == GETMORE)
            return true;
        //下拉刷新
        if (bRefreshable) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (nScrollY == 0 && !bStartRefresh) {
                        bStartRefresh = true;
                        fDownStartY = event.getRawY();
                    }
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    if (nState == PULL_To_REFRESH) {
                        nState = DONE;
                        ChangeHeaderViewByState(true);
                    }
                    else if (nState == RELEASE_To_REFRESH) {
                        nState = REFRESHING;
                        ChangeHeaderViewByState(true);
                    }
                    bStartRefresh = false;
                    break;
                case MotionEvent.ACTION_MOVE:
                    float fTempY = event.getRawY();
                    if (nScrollY == 0 && !bStartRefresh) {
                        bStartRefresh = true;
                        fDownStartY = event.getRawY();
                    }
                    if (bStartRefresh)
                    {
                        if (nState == DONE)
                        {
                            if (fTempY - fDownStartY > 0) {
                                nState = PULL_To_REFRESH;
                                bIsBack = false;
                                ChangeHeaderViewByState(true);
                            }
                        }
                        else if (nState == PULL_To_REFRESH)
                        {
                            setSelection(0);
                            if ((fTempY - fDownStartY ) / RATIO  >= nHeadHeight) {
                                nState = RELEASE_To_REFRESH;
                                ChangeHeaderViewByState(true);
                            }
                            else if (fTempY - fDownStartY  <= 0) {
                                nState = DONE;
                                ChangeHeaderViewByState(true);
                            }
                        }
                        else if (nState == RELEASE_To_REFRESH)
                        {
                            setSelection(0);
                            if ((fTempY - fDownStartY ) / RATIO < nHeadHeight) {
                                nState = PULL_To_REFRESH;
                                bIsBack = true;
                                ChangeHeaderViewByState(true);
                            }
                            else if (fTempY - fDownStartY <= 0) {
                                nState = DONE;
                                ChangeHeaderViewByState(true);
                            }

                        }

                        //更新状态
                        if (nState == PULL_To_REFRESH)
                            vHeadView.setPadding(0, (int) (-1 * nHeadHeight + (fTempY - fDownStartY) / RATIO), 0, 0);
                        if (nState == RELEASE_To_REFRESH)
                            vHeadView.setPadding(0, (int) ((fTempY - fDownStartY) / RATIO - nHeadHeight) , 0, 0);
                    }
                    break;
            }
        }

        //上拉更多
        if (bGetMore && nType == 1) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if ((nScrollY + nVisableNum >= nTotal) && !bStartMore) {
                        bStartMore = true;
                        fDownStartY2 = event.getRawY();
                    }
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    if (nState == PULL_To_MORE) {
                        nState = DONE;
                        ChangeBottomViewByState(false);
                    }
                    bLoadingMore = false;
                    bStartMore = false;
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (bLoadingMore)
                        break;
                    float fTempY = event.getRawY();
                    if ((nScrollY + nVisableNum >= nTotal) && !bStartMore) {
                        bStartMore = true;
                        fDownStartY2 = event.getRawY();
                    }
                    if (bStartMore)
                    {
                        if (nState == DONE)
                        {
                            if (fTempY - fDownStartY2 < 0) {
                                nState = PULL_To_MORE;
                                ChangeBottomViewByState(false);
                            }
                        }
                        else if (nState == PULL_To_MORE)
                        {
                            if (IsBottom())
                            {
                                nState = GETMORE;
                                bLoadingMore = true;
                                ChangeBottomViewByState(false);
                                onGetMore();
                            }
                        }
                    }
                    break;
            }
        }
        return nState == REFRESHING || nState == GETMORE || super.onTouchEvent(event);
    }

    /**
     * 初始化页眉界面
     */
    private void InitHeadView(Context context)
    {
        if (isInEditMode())
            return;
        vHeadView = InitHead(LayoutInflater.from(context));
        if (vHeadView == null)	//没有Listview头
            return;
        MeasureView(vHeadView);
        nHeadHeight = vHeadView.getMeasuredHeight();
        vHeadView.setPadding(0, -1 * nHeadHeight, 0, 0);
        vHeadView.invalidate();
        addHeaderView(vHeadView, null, false);
    }

    /**
     * 初始化页底界面
     */
    private void InitBottomView(Context context)
    {
        if (isInEditMode())
            return;
        vBottomView = InitBottom(LayoutInflater.from(context));
        if (vBottomView == null)	//没有Listview尾
            return;
        MeasureView(vBottomView);
        nBottomHeight = vBottomView.getMeasuredHeight();
        if (bGetMore)
        {
            vBottomView.setPadding(0, 0, 0, 0);
            Push();
        }
        else
            vBottomView.setPadding(0, 0, 0, -1 * nBottomHeight);

        vBottomView.invalidate();
        addFooterView(vBottomView, null, false);

        vBottomView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (nType == 0) {
                    nState = GETMORE;
                    ChangeBottomViewByState(false);
                    onGetMore();
                }
            }
        });
    }

    /**
     * 设置滑动监听
     */
    private void SetListener()
    {
        //设置Scroll条监听
        setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (bGetMore) {
                    if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
                            && nState == DONE) {
                        if (IsBottom())
                        {
                            nState = GETMORE;
                            bLoadingMore = true;
                            ChangeBottomViewByState(false);
                            onGetMore();
                        }
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                nScrollY = firstVisibleItem;
                nTotal = totalItemCount;
                nVisableNum = visibleItemCount;
            }
        });
        setGroupIndicator(null);
        //setOverScrollMode(View.OVER_SCROLL_NEVER);
        nState = DONE;
    }

	/**
	 * 设置长按监听事件
	 * @param l 监听事件
	 */
	public void SetOnLongClickListener(final JKExpandableListViewListener l)
	{
		if (l != null)
		{
			setOnItemLongClickListener(new OnItemLongClickListener() {

				@Override
				public boolean onItemLongClick(AdapterView<?> parent, View view,
						int nPos, long id) {	
					
					int nGroupPos = 0;
					int nChildPos = -1;
					int nIndex = 0;
					
					while (nIndex < nPos)
					{
						if (isGroupExpanded(nGroupPos))
						{							
							int nChildCount = getExpandableListAdapter().getChildrenCount(nGroupPos);
							if (nIndex + nChildCount >= nPos)	//说明在里面
							{
								nChildPos = nPos - nIndex - 1;
								break;
							}
							else {
								nIndex += nChildCount + 1;
								nGroupPos++;
							}
						}
						else {
							nIndex ++;
							nGroupPos ++;
						}
					}
					
					return l.OnItemLongClick(parent, view, nGroupPos, nChildPos, id);
				}
			});
		}
		else {
			setOnItemLongClickListener(null);
		}
	}

    /**
     * 切换眉页状态
     */
    private void ChangeHeaderViewByState(boolean bAnimation) {
        switch (nState) {
            case RELEASE_To_REFRESH:
                Relase();
                break;
            case PULL_To_REFRESH:
                if (bIsBack)
                    ReturnPull();
                else
                    Pull();
                break;
            case REFRESHING:
                ValueAnimator animationTop = ValueAnimator.ofInt(vHeadView.getPaddingTop(), 0);
                animationTop.setDuration(200);
                animationTop.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        vHeadView.setPadding(0, ((Integer) animation.getAnimatedValue()), 0, 0);
                    }
                });
                animationTop.setInterpolator(new AccelerateDecelerateInterpolator());
                animationTop.addListener(new Animator.AnimatorListener() {

                    @Override
                    public void onAnimationStart(Animator arg0) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator arg0) {

                    }

                    @Override
                    public void onAnimationEnd(Animator arg0) {
                        onRefresh();
                        Refresh();
                    }

                    @Override
                    public void onAnimationCancel(Animator arg0) {

                    }
                });

                animationTop.start();
                break;
            case DONE:
            {
                DoneRefresh();
                if (bAnimation)
                {
                    ValueAnimator animation = ValueAnimator.ofInt(vHeadView.getPaddingTop(), -1 * nHeadHeight);
                    animation.setDuration(200);
                    animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            vHeadView.setPadding(0, ((Integer) animation.getAnimatedValue()), 0, 0);
                        }
                    });
                    animation.setInterpolator(new AccelerateDecelerateInterpolator());
                    animation.start();
                }
                else {
                    vHeadView.setPadding(0,-1 * nHeadHeight, 0, 0);
                }
                break;
            }
            case LOADING_FINISH:
                DoneRefresh();
                if (bAnimation)
                {
                    ValueAnimator animation = ValueAnimator.ofInt(vHeadView.getPaddingTop(), -1 * nHeadHeight);
                    animation.setDuration(200);
                    animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            vHeadView.setPadding(0, ((Integer) animation.getAnimatedValue()), 0, 0);
                        }
                    });
                    animation.setInterpolator(new AccelerateDecelerateInterpolator());
                    animation.start();
                }
                else {
                    vHeadView.setPadding(0,-1 * nHeadHeight, 0, 0);
                }
                nState = DONE;
                break;
        }
    }

    /**
     * 切换眉脚状态
     * @param bAnimation 是否带动画效果
     */
    private void ChangeBottomViewByState(boolean bAnimation) {
        switch (nState) {
            case PULL_To_MORE:
                Push();
                break;
            case GETMORE:
                LoadingMore();
                break;
            case DONE:
                Push();
                vBottomView.setPadding(0, 0, 0, 0);
                break;
            case MORE_FINISH:
                DoneMore();
                if (bAnimation)
                {
                    ValueAnimator animation = ValueAnimator.ofInt(0, -1 * nBottomHeight);
                    animation.setDuration(200);
                    animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            vBottomView.setPadding(0, ((Integer)animation.getAnimatedValue()), 0, 0);
                        }
                    });
                    animation.setInterpolator(new AccelerateDecelerateInterpolator());
                    animation.start();
                }
                else {
                    vBottomView.setPadding(0, -1 * nBottomHeight, 0, 0);
                }
                nState = DONE;
                break;
        }
    }

    /**
     * 测量View宽高
     * @param child 测量的View
     */
    private void MeasureView(View child) {
        ViewGroup.LayoutParams p = child.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0, p.width);
        int lpHeight = p.height;
        int childHeightSpec;
        if (lpHeight > 0) {
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = MeasureSpec.makeMeasureSpec(0,MeasureSpec.UNSPECIFIED);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }

    /**
     * 开始下拉刷新
     */
    private void onRefresh() {
        if (m_Listener != null) {
            m_Listener.BeginRefresh();
        }
    }

    private void onGetMore() {
        if (m_Listener != null) {
            m_Listener.BeginLoadingMore();
        }
    }
}