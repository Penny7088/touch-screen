package com.jkframework.compress;

import android.os.Handler;
import android.os.Message;

import com.jkframework.bean.JKCompressData;
import com.jkframework.callback.JKBaseUncompressListener;


public abstract class JKBaseUncompress{
	
	/**解压缩监听事件*/
	protected JKBaseUncompressListener m_Listener;
	
	/**解压缩成功消息*/
	final protected int UNZIP_START = 0;
	/**解压缩成功消息*/
	final protected int UNZIP_OK = 1;
	/**解压缩失败消息*/
	final protected int UNZIP_FAILD = 2;
	/**解压缩进度消息*/
	final protected int UNZIP_PROGRESS = 3;
	
	/**
	 * 打开SD卡上的压缩文件
	 * @param  tFilepath  压缩文件路径
	 * @param tEncoding 字符编码(GBK,UTF-8)
	 * @return 压缩文件个数
	 */
	public abstract int OpenFile(String tFilepath,String tEncoding);
	
	/**
	 * 打开Assets压缩文件
	 * @param tAssetsName  Assets文件名
	 * @param tEncoding 字符编码(GBK,UTF-8)
	 * @return 压缩文件个数
	 */
	public abstract int OpenAssetsFile(String tAssetsName,String tEncoding);
	
	/**
	 * 回调解压状态
	 * @param nStatus 0为解压开始,1为解压成功,2为解压失败
	 */
	protected final void PostUnzipStatus(int nStatus)
	{
		Message mgTip = hlHandler.obtainMessage(nStatus);
		hlHandler.sendMessage(mgTip); // 发送消息
	}
	
	/**
	 * 回调解压状态
	 * @param jkcdProgress 解压文件进度状态实例
	 */
	protected final void PostUnzipProgress(JKCompressData jkcdProgress)
	{
		Message mgTip = hlHandler.obtainMessage(UNZIP_PROGRESS);
		mgTip.obj = jkcdProgress;
		hlHandler.sendMessage(mgTip); // 发送消息
	}
	
	/**
	 * 解压Zip文件
	 * @param tUnzippath 解压的路径地址(末尾加"/")
	 * @param  jkvbExit 主程序是否退出
	 */		
	public abstract void UncompressAsync(final JKBaseUncompressListener l,final String tUnzippath,final Boolean jkvbExit);
	
	/**
	 * 解压Zip文件
	 * @param tUnzippath 解压的路径地址(末尾加"/")
	 * @param  jkvbExit 主程序是否退出
	 * @return 是否解压成功
	 */		
	public abstract boolean Uncompress(String tUnzippath,Boolean jkvbExit);
	
	/**
	 * 获取Zip内某一文件
	 * @param tSubPath Zip内路径("/"拼接)
	 * @return 该文件的byte字节数组
	 */
	public abstract byte[] UncompressOne(String tSubPath);
	
	/**
	 * 复位压缩流
	 */
	public abstract void Reset();
	
	/**
	 * 文件流是否关闭
     * @return true为关闭
	 */
	public abstract boolean IsClosed();
	
	/**
	 * 关闭压缩流
	 */
	public abstract void Close();
	
	
	Handler hlHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);				
			if (m_Listener != null)
			{
				switch (msg.what)
				{
					case UNZIP_START:
					{
						m_Listener.UnzipStatus(0);
						break;
					}
					case UNZIP_OK:
					{
						m_Listener.UnzipStatus(1);
						break;
					}
					case UNZIP_FAILD:
					{
						m_Listener.UnzipStatus(2);
						break;
					}
					case UNZIP_PROGRESS:
					{
						JKCompressData jkcdProgress = (JKCompressData) msg.obj;
						m_Listener.UnzipProgress(jkcdProgress.getCurrentNum(),jkcdProgress.getTotalNum(),jkcdProgress.getCompressPath(),jkcdProgress.getFilePath());
						break;
					}	
				}
			}
		}
	};
}
