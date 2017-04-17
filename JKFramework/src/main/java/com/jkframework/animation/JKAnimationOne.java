package com.jkframework.animation;

import java.util.ArrayList;
import java.util.HashMap;




public abstract class JKAnimationOne{
	
	/**动作预计完成时间*/
	public long lFinishTime;
	
	
	/**动作是否被复写(等待动作默认不复写)*/
	protected boolean bAutoCancel = true;
	/**即时列表*/
	protected ArrayList<String> a_tTypeList = new ArrayList<>();
	/**过滤列表*/
	protected ArrayList<String> a_tFilterList = new ArrayList<>();
	/**监听成员*/
	protected JKAnimationOneListener m_AnimationLisnter;	
	/**动作持续时间*/
	protected int nAnimationTime;	
	/**程序是否退出标识*/
	protected Boolean jkvbExit;
	/**该动作是否废弃*/
	protected boolean bAbandon = false;
	
	
	/**
	 * 初始化过滤列表
	 */
	public abstract void InitFilter();
	
	/**
	 * 添加过滤列表
	 */
	public abstract void AddFilter();
	
	/**
	 * 初始化动作,必须保证nAnimation计算完毕
	 * @return 返回0表示该动作为异步初始化,返回1表示该动作可以执行,返回2表示该动作不需要执行
	 */
	public abstract int InitAnimation(HashMap<String, Boolean> hmList);
	
	/**
	 * 开始执行动作
	 * @param hmList 过滤器列表
	 * @param jkvbTmp 程序是否退出标识
	 */
	public abstract void StartAnimation(HashMap<String, Boolean> hmList,Boolean jkvbTmp);
	
	/**
	 * 更新过滤列表至Group级
	 * @param hmList 传入过滤器
	 */
	public abstract void UpdateAnimation(HashMap<String, Boolean> hmList);
	
	/**
	 * 设置回调函数
	 * @param l	初始化回调变量
	 */
	void SetAnimationLisnter(JKAnimationOneListener l)
	{
		m_AnimationLisnter = l;
	}
	
	/**
	 * 检查动画播放状态是否执行回调
	 */
	public void CheckStatus()
	{
		if (a_tTypeList.size() == 0)
		{
			if (m_AnimationLisnter != null)
				m_AnimationLisnter.FinishAnimation();
		}
	}
	
	/**
	 * 检查过滤状态
	 * @param tName 标识符
	 * @return 若存在标识符则返回true,否则返回false
	 */
	public boolean CheckStatus(String tName)
	{
		return a_tTypeList.indexOf(tName) >= 0;
	}
	
	/**
	 * 设置动作是否被覆盖
	 * @param bCover 覆盖传true
	 */
	public void SetCover(boolean bCover)
	{
		bAutoCancel = bCover;		
	}
	
	/**
	 * 停止当前动画
	 */
	public abstract void StopAnimation();
	
	/**
	 * 暂停当前动画
	 */
	public abstract void PauseAnimation();
	
	/**
	 * 恢复当前动画
	 * @param hmList 传入过滤器
	 */
	public abstract void RestartAnimation(HashMap<String, Boolean> hmList);
	
	/**
	 * 恢复声音
	 */
	public void EnableSound()
	{
		
	}
		
	/**
	 * 禁用声音
	 */
	public void DisableSound()
	{
		
	}
	
	/**
	 * 获取动作持续时间
	 * @return 动作持续毫秒
	 */
	int GetAnimationTime()
	{
		return nAnimationTime;
	}
}