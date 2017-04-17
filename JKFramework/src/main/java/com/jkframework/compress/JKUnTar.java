package com.jkframework.compress;

import com.jkframework.algorithm.JKFile;
import com.jkframework.bean.JKCompressData;
import com.jkframework.callback.JKBaseUncompressListener;
import com.jkframework.debug.JKLog;
import com.jkframework.thread.JKThread;
import com.jkframework.thread.JKThread.JKThreadListener;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;


public class JKUnTar extends JKBaseUncompress{
	
	/**Buff空间*/
	private final int BUFFER = 1024 * 16;
	/**GZIP文件流*/
	private TarArchiveInputStream  taisStream;
	/**文件路径*/
	private String tFilePath;
	/**字符编码*/
	private String tEncoding = "UTF-8";
	/**当前解压数*/
	private int nCount = 0;
	/**当前解压总数*/
	private int nTotal = 0;
	
	
	@Override
	public int OpenFile(String tFilepath,String tEncoding)
	{		
		this.tFilePath = tFilepath;
		this.tEncoding = tEncoding;
		try {
			taisStream = new TarArchiveInputStream(new FileInputStream(tFilepath),tEncoding);
			int nFileCount = 0;
			TarArchiveEntry taeEntry;
			while((taeEntry = taisStream.getNextTarEntry()) != null){
				//如果条目是文件目录，则继续执行
				if(!taeEntry.isDirectory()){					
					nFileCount++;
				}
			}
			Close();
			nTotal = nFileCount;
		} catch (IOException e) {
			e.printStackTrace();
			JKLog.ErrorLog("打开SD卡上tar文件失败.原因为:" + e.getMessage());
			return -1;
		}
		return nTotal;
	}
	
	@Override
	public int OpenAssetsFile(String tAssetsName,String tEncoding)
	{		
		this.tFilePath = tAssetsName;
		this.tEncoding = tEncoding;
		try {
			taisStream = new TarArchiveInputStream(JKFile.ReadAssets(tAssetsName),tEncoding);
			int nFileCount = 0;
			TarArchiveEntry taeEntry;
			while((taeEntry = taisStream.getNextTarEntry()) != null){
				//如果条目是文件目录，则继续执行
				if(!taeEntry.isDirectory()){					
					nFileCount++;
				}
			}
			Close();
			nTotal = nFileCount;
		} catch (IOException e) {
			e.printStackTrace();
			JKLog.ErrorLog("打开Asset上的tar文件失败.原因为:" + e.getMessage());
			return -1;
		}
		return nTotal;
	}
	
	@Override
	public void UncompressAsync(final JKBaseUncompressListener l,final String tUnzippath,final Boolean jkvbExit){
		m_Listener = l;
		PostUnzipStatus(UNZIP_START);
		
		new JKThread().Start(new JKThreadListener() {
			
			@Override
			public void OnThread() {
				nCount = 0;
				try {
					if (IsClosed())
						Reset();
					
					TarArchiveEntry taeEntry;
					while((taeEntry = taisStream.getNextTarEntry()) != null){
						if (jkvbExit != null && jkvbExit)
							break;
						//如果条目是文件目录，则继续执行
						if(!taeEntry.isDirectory()){
							BufferedOutputStream bos;
							int count ;
							byte date[] = new byte[BUFFER];
							String tPath = tUnzippath + taeEntry.getName();
							JKFile.CreateDir(tPath);
							bos = new BufferedOutputStream(new FileOutputStream(new File(tPath)));
							while((count = taisStream.read(date))!=-1){
								bos.write(date,0,count);
							}
							bos.flush();
							bos.close();
							
							nCount++;	//记数+1
							JKCompressData jkcdTmp = new JKCompressData();
							jkcdTmp.setCurrentNum(nCount);
							jkcdTmp.setTotalNum(nTotal);
							jkcdTmp.setCompressPath(taeEntry.getName());
							jkcdTmp.setFilePath(tPath);
							PostUnzipProgress(jkcdTmp);
						}	
					}
					Close();
				}
				catch (IOException e1) {
					JKLog.ErrorLog("解压缩文件失败.原因为:" + e1.getMessage());
					PostUnzipStatus(UNZIP_FAILD);
					return;
				}
				
				PostUnzipStatus(UNZIP_OK);
			}
			
			@Override
			public void OnMain() {
			}
		});
	}
	
	@Override
	public boolean Uncompress(String tUnzippath,Boolean jkvbExit){
		nCount = 0;
		//循环读取文件条目，只要不为空，就进行处理
		try {
			if (IsClosed())
				Reset();
			TarArchiveEntry taeEntry;
			while((taeEntry = taisStream.getNextTarEntry()) != null){
				if (jkvbExit != null && jkvbExit)
					break;
				//如果条目是文件目录，则继续执行
				if(taeEntry.isDirectory()){
					BufferedOutputStream bos;
					int count ;
					byte date[] = new byte[BUFFER];
					String tPath = tUnzippath + taeEntry.getName();
					JKFile.CreateDir(tPath);
					bos = new BufferedOutputStream(new FileOutputStream(new File(tPath)));
					while((count = taisStream.read(date))!=-1){
						bos.write(date,0,count);
					}
					bos.flush();
					bos.close();
					
					nCount++;	//记数+1
				}	
			}
			Close();
		}catch (IOException e1) {
			JKLog.ErrorLog("解压缩文件失败.原因为:" + e1.getMessage());
			return false;
		}
		return true;
	}
	
	@Override
	public void Reset()
	{
		try {
			if (tFilePath.charAt(0) == '/')
				taisStream = new TarArchiveInputStream(new FileInputStream(tFilePath),tEncoding);
			else
				taisStream = new TarArchiveInputStream(JKFile.ReadAssets(tFilePath),tEncoding);
		} catch (IOException e) {
			e.printStackTrace();
			JKLog.ErrorLog("打开tar文件失败.原因为:" + e.getMessage());
		}
	}
	
	@Override
	public boolean IsClosed()
	{
        return taisStream == null || taisStream.IsClosed();
	}
	
	@Override
	public void Close()
	{
		try {			
			taisStream.close();
		} catch (IOException e) {
			e.printStackTrace();
			JKLog.ErrorLog("关闭tar流失败.原因为:" + e.getMessage());
		}
		
	}
	
	@Override
	public byte[] UncompressOne(String tSubPath) {
		return null;
	}
}
