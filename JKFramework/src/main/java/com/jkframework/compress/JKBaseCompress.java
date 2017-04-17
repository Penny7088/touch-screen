package com.jkframework.compress;

import java.util.ArrayList;

import android.os.Handler;
import android.os.Message;

import com.jkframework.bean.JKCompressData;
import com.jkframework.callback.JKBaseCompressListener;


public abstract class JKBaseCompress{
	
	/**压缩监听事件*/
	protected JKBaseCompressListener m_Listener;

	/**压缩开始消息*/
	final protected int ZIP_START = 0;
	/**压缩成功消息*/
	final protected int ZIP_OK = 1;
	/**压缩失败消息*/
	final protected int ZIP_FAILD = 2;
	/**压缩进度消息*/
	final protected int ZIP_PROGRESS = 3;
	
	/**
	 * 回调压缩状态
	 * @param nStatus 0为压缩开始,1为压缩成功,2为压缩失败
	 */
	protected final void PostZipStatus(int nStatus)
	{
		Message mgTip = hlHandler.obtainMessage(nStatus);
		hlHandler.sendMessage(mgTip); // 发送消息
	}
	
	/**
	 * 回调压缩状态
	 * @param jkcdProgress 压缩文件进度状态实例
	 */
	protected final void PostZipProgress(JKCompressData jkcdProgress)
	{
		Message mgTip = hlHandler.obtainMessage(ZIP_PROGRESS);
		mgTip.obj = jkcdProgress;
		hlHandler.sendMessage(mgTip); // 发送消息
	}
	
	/**
	 * 初始化压缩路径
	 * @param  tZipPath  压缩文件路径
	 */
	public abstract void OpenZip(String tZipPath);
	
	/**
	 * 压缩文件
	 * @param tZippath 添加压缩的路径地址(文件夹末尾加"/")
	 * @param tFilePath 添加压缩文件的路径地址(空文件夹传null)
	 * @return 是否压缩成功
	 */		
	public abstract boolean Compress(String tZippath,String tFilePath);
	
	/**
	 * 压缩文件组
	 * @param a_tZippath 添加压缩的路径地址组(文件夹末尾加"/")
	 * @param a_tFilePath 添加压缩文件的路径地址组(空文件夹传null)
	 * @return 是否压缩成功
	 */		
	public abstract boolean Compress(ArrayList<String> a_tZippath,ArrayList<String> a_tFilePath);
	
	/**
	 * 异步压缩文件组
	 * @param l 压缩监听事件
	 * @param a_tZippath 添加压缩的路径地址组(文件夹末尾加"/")
	 * @param a_tFilePath 添加压缩文件的路径地址组(空文件夹传null)
	 */		
	public abstract void CompressAsync(final JKBaseCompressListener l,final ArrayList<String> a_tZippath,final ArrayList<String> a_tFilePath);
	
	
	Handler hlHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (m_Listener != null)
			{
				switch (msg.what)
				{			
					case ZIP_START:
					{
						m_Listener.ZipStatus(0);
						break;
					}	
					case ZIP_OK:
					{
						m_Listener.ZipStatus(1);
						break;
					}
					case ZIP_FAILD:
					{
						m_Listener.ZipStatus(2);
						break;
					}	
					case ZIP_PROGRESS:
					{
						JKCompressData jkcdProgress = (JKCompressData) msg.obj;
						m_Listener.ZipProgress(jkcdProgress.getCurrentNum(),jkcdProgress.getTotalNum(),jkcdProgress.getCompressPath(),jkcdProgress.getFilePath());
						break;
					}	
				}
			}
		}
	};
	
}
