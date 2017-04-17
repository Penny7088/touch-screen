package com.jkframework.animation;

import android.os.Handler;
import android.os.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;



public class JKAnimationGroup{
	
	/**动作顺序组*/
	private ArrayList<JKAnimationOne> a_jkaoList = new ArrayList<>();
	/**执行顺序组*/
	private ArrayList<JKAnimationDoing> a_jkadList = new ArrayList<>();
	/**正在执行动作组的编号*/
	private int nOrder = 0;
	/**正在执行动作组的位置*/
	private int nPos = 0;
	/**正在执行动作组的次数*/
	private int nTimes = 1;
	/**该动画组在管理器中的ID*/
	private int nID = 0;	
	/**程序起始时间*/	
	private long lStartTime = -1;	
	/**动画是播放状态:0为未开始,1为检测中,2为播放中,3为结束,4为暂停,5为等待恢复*/
	private int nDoingStatus = 0;
	/**列表是否在执行*/
	private boolean bLoading = false;	
	/**动画完成监听成员*/
	private JKAnimationGroupListener m_AnimationLisnter;
	/**单一动画完成监听成员*/
	private JKAnimationGroupOneListener m_AnimationOneLisnter;
	/**程序是否挂起状态*/
	private Boolean jkvbBool;
	/**当前动作组的标识*/
	private HashMap<String, Boolean> hmList = new HashMap<>();
	/**消息队列*/
	private Queue<Integer> q_iList = new LinkedList<>();
	
	
	/**动画未开始状态*/
	private final int NONE_STATUS = 0;
	/**动画检查中状态*/
	private final int READY_STATUS = 1;
	/**动画执行中状态*/
	private final int PLAYING_STATUS = 2;
	/**动画结束状态*/
	private final int FINISH_STATUS = 3;
	/**动画暂停状态*/
	private final int PAUSE_STATUS = 4;
	/**动画等待恢复状态*/
	private final int WAIT_STATUS = 5;
	
	
	/**初始化命令*/
	private final int INIT_ANIMATION = 0;
	/**播放动画命令*/
	private final int PLAY_ANIMATION = 1;
	/**暂停动画命令*/
	private final int PAUSE_ANIMATION = 2;
	/**恢复动画命令*/
	private final int RESUME_ANIMATION = 3;
	/**停止动画命令*/
	private final int STOP_ANIMATION = 4;
	/**跳下一个动画命令*/
	private final int NEXT_ANIMATION = 5;
	/**重置动画命令*/
	private final int RESET_ANIMATION = 6;
	/**更新冲突动作命令*/
	private final int UPDATE_ANIMATION = 7;
	
	
	
	/**
	 * 播放动画
	 * @param jkvTmp  程序挂起状态
	 */
	public void StartAnimation(Boolean jkvTmp)
	{	
		jkvbBool = jkvTmp;
		/*没有动作行为*/
		if (a_jkaoList.size() == 0)	
		{			
			CallBack();
			return;
		}
		
		/*设置默认播放*/
		if (a_jkadList.size() == 0)
		{
			AddDoing(1, a_jkaoList.size(), 1);
		}
		
		/*设置回调与时间点*/		
		for (int i=0; i<a_jkaoList.size(); ++i)
		{
			JKAnimationOne jkaoTmp = a_jkaoList.get(i);
			jkaoTmp.SetAnimationLisnter(new JKAnimationOneListener() {			
				public void FinishAnimation() {	//捕捉单一动画完毕,通知播放下一个动画		
					if (m_AnimationOneLisnter != null)
						m_AnimationOneLisnter.FinishOneAnimation(GetCurrentPos());
										
					/*执行下一个动画*/
					AddAction(RESET_ANIMATION);
					AddAction(NEXT_ANIMATION);					
				}

				@Override
				public void InitAnimation(boolean bValid) {
					if (bValid)		//允许执行
						nDoingStatus = READY_STATUS;
					else	
						AddAction(NEXT_ANIMATION);  //不允许执行 
					
					boolean bInit = false;
					while (!q_iList.isEmpty())
					{
						bInit = DoAction();
						if (bInit)
							break;					
					}
					if (!bInit)
						bLoading = false;					
				}			
			});	
			
			
		}
		lStartTime = System.currentTimeMillis();
		AddAction(INIT_ANIMATION);
		AddAction(PLAY_ANIMATION);		
		if (jkvbBool)
			AddAction(PAUSE_ANIMATION);
	}
	
	/**
	 * 顺序添加动作
	 * @param jkaoTmp 子动作
	 */
	public void AddAnimation(JKAnimationOne jkaoTmp)
	{
		/*更新过滤列表*/		
		for (int i=0; i<jkaoTmp.a_tFilterList.size(); ++i)
		{
			hmList.put(jkaoTmp.a_tFilterList.get(i), true);
		}
		a_jkaoList.add(jkaoTmp);
	}
	
	/**
	 * 更新过滤列表
	 * @param hmTmp 需过滤的列表
	 * @return 返回true表示动作已结束
	 */
	boolean UpdateFilter(HashMap<String, Boolean> hmTmp)
	{
		/*检查当前冲突*/
		if (IsFinish())
			return true;
			
		/*更新自身过滤列表*/
		for (String tKey : hmTmp.keySet()) {
			hmList.put(tKey, false);
		}
		Handler hlHandler = new Handler() {
			public void dispatchMessage(Message msg) {
				AddAction(UPDATE_ANIMATION);							
			}
		};
		hlHandler.sendEmptyMessageDelayed(0, 1);
		
		return false;
	}
	
	/**
	 * 返回过滤器表
	 * @return 过滤器Hash表
	 */
	HashMap<String, Boolean> GetFilter()
	{
		return hmList;
	}
	
	/**
	 * 停止当前动画
	 */
	public void StopAnimation()
	{
		AddAction(STOP_ANIMATION);
	}
	
	/**
	 * 暂停当前动画
	 */
	public void PauseAnimation()
	{
		AddAction(PAUSE_ANIMATION);		
	}
	
	/**
	 * 恢复当前动画
	 * @param nSuspendedTime 暂停所花费的时间
	 */
	public void RestartAnimation(int nSuspendedTime)
	{		
		lStartTime += nSuspendedTime;
		AddAction(RESUME_ANIMATION);	
	}
	
	/**
	 * 设置回调函数
	 * @param l	初始化回调变量
	 */
	public void SetAnimationLisnter(JKAnimationGroupListener l)
	{
		m_AnimationLisnter = l;
	}
	
	/**
	 * 设置回调函数
	 * @param l	初始化回调变量
	 */
	public void SetAnimationOneLisnter(JKAnimationGroupOneListener l)
	{
		m_AnimationOneLisnter = l;
	}
	
	/**
	 * 恢复音效
	 */
	void EnableSound()
	{		
		if (!IsFinish())
		{
			a_jkaoList.get(GetCurrentPos()).EnableSound();
		}
	}
	
	/**
	 * 禁用音效
	 */
	void DisableSound()
	{
		if (!IsFinish())
		{
			a_jkaoList.get(GetCurrentPos()).DisableSound();
		}
	}
	
	/**
	 * 动作执行完成,进行回调
	 */
	void CallBack()
	{
		nDoingStatus = FINISH_STATUS;
		bLoading = true;
		if (m_AnimationLisnter != null)
			m_AnimationLisnter.FinishAnimation();
	}
	
	
	/**
	 * 添加执行规则
	 * @param nStart  执行起始动作编号(从1开始)
	 * @param nEnd   执行结束动作编号(从1开始) 
	 * @param nCount 执行次数
	 */
	private void AddDoing(int nStart, int nEnd, int nCount)
	{
		JKAnimationDoing jkadTmp = new JKAnimationDoing();
		if (nStart <= nEnd)
		{
			jkadTmp.nStart = nStart-1;
			jkadTmp.nEnd = nEnd-1;
		}
		else {
			jkadTmp.nEnd = nStart-1;
			jkadTmp.nStart = nEnd-1;
		}
		jkadTmp.nCount = nCount;
		if (jkadTmp.nCount<0)
			jkadTmp.nCount = 0;
		
		a_jkadList.add(jkadTmp);
	}
	
	/**
	 * 自动设置动画ID,请勿使用
	 * @param nSetID ID号
	 */
	void SetID(int nSetID)
	{
		nID = nSetID;
	}
	
	/**
	 * 获取该动作组的ID号
	 * @return 该动作组的ID号
	 */
	int GetID()
	{
		return nID;
	}
	
	/**
	 * 删除所有动作顺序
	 */
	public void RemoveDoing()
	{
		a_jkadList.clear();
	}
	
	/**
	 * 获得动作个数
	 * @return 返回动作个数
	 */
	int GetActionCount()
	{
		return a_jkaoList.size();
	}
	
	/**
	 * 判断动画是否结束
	 * @return 动画结束返回true,或反之
	 */
	private boolean IsFinish()
	{
		return nDoingStatus == FINISH_STATUS;
	}
	
	/**
	 * 获得当前动画位置
	 */
	private int GetCurrentPos()
	{
		if (nTimes == 1 && nOrder == 0)
			return nPos;
		else
			return a_jkadList.get(nOrder).nStart + nPos;
	}
	
	/**
	 * 添加行为到执行列表
	 * @param nAction 行为编号
	 */
	private void AddAction(int nAction)
	{
		q_iList.offer(nAction);
		if (!bLoading)
		{
			bLoading = true;
			boolean bInit = false;
			while (!q_iList.isEmpty())
			{
				bInit = DoAction();
				if (bInit)
					break;					
			}
			if (!bInit)
				bLoading = false;
		}
	}
	
	/**
	 * 执行动作行为
	 * @return 返回true表示该动作异步初始化
	 */
	private boolean DoAction()
	{
		int nAction = q_iList.poll();
		boolean bAysncInit = false;
		
		if (!IsFinish())	
		{
			switch (nAction)
			{
				case INIT_ANIMATION:
				{
					if (nDoingStatus == NONE_STATUS)	//默认状态下才允许初始化
					{
						int nDo = a_jkaoList.get(GetCurrentPos()).InitAnimation(hmList);
						if (0 == nDo)	
						{
							bAysncInit = true;
						}
						else		
						{
							if (1 == nDo)		//允许执行
								nDoingStatus = READY_STATUS;
							else	//nDo == 2
								AddAction(NEXT_ANIMATION);  //不允许执行 
						}
					}
					break;
				}
				case PLAY_ANIMATION:
				{
					if (nDoingStatus == READY_STATUS)
					{
						nDoingStatus = PLAYING_STATUS;
						int nNext = GetCurrentPos();
						a_jkaoList.get(nNext).lFinishTime = lStartTime + a_jkaoList.get(nNext).GetAnimationTime();
						lStartTime = a_jkaoList.get(nNext).lFinishTime;
						a_jkaoList.get(nNext).StartAnimation(hmList,jkvbBool);
					}
					break;
				}
				case PAUSE_ANIMATION:
				{
					if (nDoingStatus == PLAYING_STATUS)		//动画播放状态时更改为默认状态
					{
						nDoingStatus = PAUSE_STATUS;
						a_jkaoList.get(GetCurrentPos()).PauseAnimation();
					}
					break;
				}
				case RESUME_ANIMATION:
				{
					if (nDoingStatus == PAUSE_STATUS)		//动画暂停时恢复
					{
						nDoingStatus = PLAYING_STATUS;
						a_jkaoList.get(GetCurrentPos()).RestartAnimation(hmList);
					}
					if (nDoingStatus == WAIT_STATUS)		//动画已结束时的恢复
					{
						nDoingStatus = PLAYING_STATUS;
						/*执行下一个动画*/
						AddAction(RESET_ANIMATION);
						AddAction(NEXT_ANIMATION);
					}
					break;
				}
				case STOP_ANIMATION:
				{
					if (nDoingStatus == PLAYING_STATUS)
					{
						a_jkaoList.get(GetCurrentPos()).StopAnimation();
					}			
					nDoingStatus = FINISH_STATUS;
					break;
				}
				case NEXT_ANIMATION:
				{
					if (nDoingStatus == NONE_STATUS)
					{
						++nPos;
						int nReallyPos = GetCurrentPos(); 
						if (nReallyPos > a_jkadList.get(nOrder).nEnd)		//换下一次动作
						{
							++nTimes;
							if (nTimes > a_jkadList.get(nOrder).nCount)		//换下一组动作
							{
								++nOrder;
								if (nOrder >= a_jkadList.size())  //所有动作执行完毕
								{
									CallBack();
									return false;
								}
								nTimes = 1;								
							}
							nPos = 0;
						}
						AddAction(INIT_ANIMATION);
						AddAction(PLAY_ANIMATION);
					}
					break;
				}
				case RESET_ANIMATION:
				{
					if (nDoingStatus == PLAYING_STATUS)		//动画播放状态时更改为默认状态
						nDoingStatus = NONE_STATUS;
					if (nDoingStatus == PAUSE_STATUS)		//动画在暂停时没有挂住
						nDoingStatus = WAIT_STATUS;
					break;
				}
				case UPDATE_ANIMATION:
				{		
					if (nDoingStatus == PLAYING_STATUS)
					{
						a_jkaoList.get(GetCurrentPos()).UpdateAnimation(hmList);
						lStartTime = System.currentTimeMillis();
					}
					break;
				}
			}
		}
		
		return bAysncInit;
	}
}