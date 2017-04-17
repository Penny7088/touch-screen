package com.jkframework.callback;

public interface JKBatteryLinstener {

	/**
	 * 接受电池电量更新
	 * @param nBattery 电池电量[0,100]
	 */
	void Receive(int nBattery);
}
