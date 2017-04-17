/*
 * Copyright (C) 2007-2014 Geometer Plus <contact@geometerplus.com>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301, USA.
 */

package com.jkframework.manager;

import java.util.ArrayList;

import com.jkframework.activity.JKBaseActivity;

public class JKActivityManager {
	
	/**Activity跟踪结构*/
	private static ArrayList<JKBaseActivity> AllActivity = new ArrayList<>();

	public static ArrayList<JKBaseActivity> getAllActivity() {
		return AllActivity;
	}

	/**
	 * 是否Activity全部被回收
	 * @return
	 */
	public static boolean isNull()
	{
		return AllActivity == null;
	}

	/**
	 * 添加Activity
	 * @param CurrentActivity 当前Activity
	 */
	public static void AddActivity(JKBaseActivity CurrentActivity)
	{
		AllActivity.add(CurrentActivity);
	}
	
	/**
	 * 重置Activity信息
	 */
	public static void Reset()
	{
		AllActivity = new ArrayList<>();
	}
	
	/**
	 * 废弃所有Activity信息
	 */
	public static void Abandon()
	{
		for (int i=0; i<AllActivity.size(); ++i)
		{
			AllActivity.get(i).setAbandon(true);
		}
	}
	
	/**
	 * 退出所有Activity
	 */
	public static void Exit()
	{
		for (int i=0; i<AllActivity.size(); ++i)
		{
			AllActivity.get(i).finish();
		}
	}
}
