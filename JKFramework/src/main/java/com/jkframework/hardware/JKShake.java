package com.jkframework.hardware;

import android.content.Context;
import android.hardware.SensorManager;

import com.jkframework.debug.JKDebug;
import com.squareup.seismic.ShakeDetector;
import com.squareup.seismic.ShakeDetector.Listener;

public class JKShake {
	
	/**摇一摇对象*/
	private ShakeDetector sdShake;
	/** 摇一摇监听接口 */
	private JKShakeListener m_ShakeListener;

    /**
	 * 注册摇一摇事件
	 * @param l 摇一摇监听接口
	 */
	public void Register(JKShakeListener l) {
		UnRegister();
		m_ShakeListener = l;
		/* 重力感应管理者对象 */
        SensorManager sensorManager = (SensorManager) JKDebug.hContext
                .getSystemService(Context.SENSOR_SERVICE);
		sdShake = new ShakeDetector(new Listener() {
			
			@Override
			public void hearShake() {
				if (m_ShakeListener != null)
					m_ShakeListener.Shake();
			}
		});
		sdShake.start(sensorManager);
	}

	/**
	 * 取消注册
	 */
	public void UnRegister() {
		if (m_ShakeListener != null) {
			m_ShakeListener = null;
			if (sdShake != null)
				sdShake.stop();
		}
	}

	public interface JKShakeListener {

		/**
		 * 触发了摇一摇回调
		 */
		void Shake();
	}
}