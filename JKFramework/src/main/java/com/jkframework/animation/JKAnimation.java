package com.jkframework.animation;

import java.util.ArrayList;



public class JKAnimation{
	
	/**动作组列表*/
	private ArrayList<JKAnimationGroup> a_jkagList = new ArrayList<>();
	/**程序状态*/
	private Boolean jkvbSuspend = false;
	/**播放动画*/
	private int nID = 0;
	
	
	/**挂起时间*/
	private long lSuspended;
	
	/**
	 * 构造函数
	 * @param jkvbTmp  程序挂起状态,可为Null则为默认永不挂起
	 */
	public JKAnimation(Boolean jkvbTmp)
	{
		if (jkvbTmp != null)
			jkvbSuspend = jkvbTmp;
	}
	
	/**
	 * 执行动画
	 * @param jkagGroup	需要执行的动画组
	 * @param jkagGroup  系统挂起状态
	 * @return 返回该组动画的ID序号
	 */
	public synchronized int StartAnimation(final JKAnimationGroup jkagGroup)
	{
		++nID;
		/*播放动画*/
		if (jkagGroup.GetActionCount() == 0)	//空动作
		{
			jkagGroup.CallBack();	
		}
		else {
			ArrayList<JKAnimationGroup> a_jkagDelList = new ArrayList<>();
			/*更新过滤器*/
			for (int i=0; i< a_jkagList.size(); ++i)
			{
				JKAnimationGroup jkagTmp = a_jkagList.get(i);
				boolean bDel = jkagTmp.UpdateFilter(jkagGroup.GetFilter());
				if (bDel)
					a_jkagDelList.add(jkagTmp);
			}
			/*去除已过期的动作*/
			for (int i=0; i< a_jkagDelList.size(); ++i)
			{
				a_jkagList.remove(a_jkagDelList.get(i));
			}
			
			jkagGroup.SetID(nID);
			a_jkagList.add(jkagGroup);
				
			jkagGroup.StartAnimation(jkvbSuspend);
		}
		return nID;
	}	
	
	/**
	 * 停止所有动作,动作会跳到终止点
	 */
	public void StopAnimation()
	{
		for (int i=0; i< a_jkagList.size(); ++i)
		{
			a_jkagList.get(i).StopAnimation();
		}
	}
	
	/**
	 * 停止该ID动作,动作会跳到终止点
	 * @param nIndexID 停止动作的ID号
	 */
	public void StopAnimation(int nIndexID)
	{
		for (int i=0; i< a_jkagList.size(); ++i)
		{
			if (nIndexID == a_jkagList.get(i).GetID())
			{
				a_jkagList.get(i).StopAnimation();
				return;
			}
		}
	}
	
	/**
	 * 暂停所有动作
	 */
	public void PauseAnimation()
	{		
		for (int i=0; i< a_jkagList.size(); ++i)
		{
			a_jkagList.get(i).PauseAnimation();
		}
		lSuspended = System.currentTimeMillis();
	}
	
	/**
	 * 恢复所有动作
	 */
	public void RestartAnimation()
	{
		int nSpaceTime = (int) (System.currentTimeMillis() - lSuspended);
		for (int i=0; i< a_jkagList.size(); ++i)
		{
			a_jkagList.get(i).RestartAnimation(nSpaceTime);
		}		
	}
	
	/**
	 * 恢复所有动作
	 * @param nSpaceTime 动画暂停的时间
	 */
	public void RestartAnimation(int nSpaceTime)
	{
		for (int i=0; i< a_jkagList.size(); ++i)
		{
			a_jkagList.get(i).RestartAnimation(nSpaceTime);
		}		
	}
	
	/**
	 * 恢复所有音效
	 */
	public void EnableSound()
	{	
		for (int i=0; i< a_jkagList.size(); ++i)
		{
			a_jkagList.get(i).EnableSound();
		}
	}
	
	/**
	 * 禁用所有音效
	 */
	public void DisableSound()
	{
		for (int i=0; i< a_jkagList.size(); ++i)
		{
			a_jkagList.get(i).DisableSound();
		}
	}
}