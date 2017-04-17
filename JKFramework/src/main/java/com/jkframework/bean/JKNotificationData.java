package com.jkframework.bean;

import android.app.Notification;




public class JKNotificationData
{
	/**消息通知对象*/
	private Notification Notifi;
	/**当前消息通知对象所在ID(实时更新)*/
	private int ID;
	
	public Notification getNotifi() {
		return Notifi;
	}
	
	public void setNotifi(Notification nfNotifi) {
		this.Notifi = nfNotifi;
	}
	
	public int getID() {
		return ID;
	}
	
	public void setID(int nID) {
		this.ID = nID;
	}
}