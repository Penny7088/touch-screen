package com.jkframework.compress;

import com.jkframework.algorithm.JKConvert;
import com.jkframework.algorithm.JKFile;
import com.jkframework.bean.JKCompressData;
import com.jkframework.callback.JKBaseUncompressListener;
import com.jkframework.debug.JKLog;
import com.jkframework.thread.JKThread;
import com.jkframework.thread.JKThread.JKThreadListener;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.io.FileUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;


public class JKUnZip extends JKBaseUncompress{
	
	/**Buff空间*/
	private final int BUFFER = 1024 * 16;
	/**zip文件对象*/
	private ZipFile zFile;
	/**zip文件流*/
	private ZipArchiveInputStream zaisStream;
	/**文件路径*/
	private String tFilePath;
	/**字符编码*/
	private String tEncoding = "UTF-8";
	/**当前解压数*/
	private int nCount = 0;
	/**当前解压总数*/
	private int nTotal = 0;
	/**zip解压格式(0为文件对象,1为流对象)*/
	private int nMode = 0;

	@Override
	public int OpenFile(String tFilepath,String tEncoding)
	{		
		this.tFilePath = tFilepath;
		this.tEncoding = tEncoding;
		nMode = 0;
		try {
			zFile = new ZipFile(tFilepath,tEncoding);		
			nTotal = zFile.GetSize();			
		} catch (IOException e) {
			e.printStackTrace();
			JKLog.ErrorLog("打开SD卡上zip文件失败.原因为:" + e.getMessage());
			return -1;
		}
		return nTotal;
	}
	
	@Override
	public int OpenAssetsFile(String tAssetsName,String tEncoding)
	{		
		this.tFilePath = tAssetsName;
		this.tEncoding = tEncoding;
		nMode = 1;
		try {
			zaisStream = new ZipArchiveInputStream(JKFile.ReadAssets(tAssetsName),tEncoding);
			int nFileCount = 0;
			ZipArchiveEntry zaeEntry;
			while((zaeEntry = zaisStream.getNextZipEntry()) != null){
				//如果条目是文件目录，则继续执行
				if(!zaeEntry.isDirectory()){					
					nFileCount++;
				}
			}
			Close();
			nTotal = nFileCount;
		} catch (IOException e) {
			e.printStackTrace();
			JKLog.ErrorLog("打开Asset上zip文件失败.原因为:" + e.getMessage());
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
				if (IsClosed())
					Reset();
				nCount = 0;
				
				//循环读取文件条目，只要不为空，就进行处理
				try {
					if (nMode == 0)
					{
						Enumeration<ZipArchiveEntry> e = zFile.getEntries();
						while(e.hasMoreElements()){
							ZipArchiveEntry entry = e.nextElement();
							if (jkvbExit != null && jkvbExit)
								break;
							//如果条目是文件目录，则继续执行
							if(!entry.isDirectory()){
								BufferedInputStream zis = new BufferedInputStream(zFile.getInputStream(entry));
								String tPath = tUnzippath + entry.getName();
								FileUtils.copyInputStreamToFile(zis,  new File(tPath));
								
								nCount++;	//记数+1
								JKCompressData jkcdTmp = new JKCompressData();
								jkcdTmp.setCurrentNum(nCount);
								jkcdTmp.setTotalNum(nTotal);
								jkcdTmp.setCompressPath(entry.getName());
								jkcdTmp.setFilePath(tPath);
								PostUnzipProgress(jkcdTmp);
								zis.close();
							}	
						}
					}
					else {
						ZipArchiveEntry zaeEntry;
						while((zaeEntry = zaisStream.getNextZipEntry()) != null){
							if (jkvbExit != null && jkvbExit)
								break;
							//如果条目是文件目录，则继续执行
							if(!zaeEntry.isDirectory()){
								BufferedOutputStream bos;
								int count ;
								byte date[] = new byte[BUFFER];
								String tPath = tUnzippath + zaeEntry.getName();
								JKFile.CreateDir(tPath);
								bos = new BufferedOutputStream(new FileOutputStream(new File(tPath)));
								while((count = zaisStream.read(date))!=-1){
									bos.write(date,0,count);
								}
								bos.flush();
								bos.close();
								
								nCount++;	//记数+1
								JKCompressData jkcdTmp = new JKCompressData();
								jkcdTmp.setCurrentNum(nCount);
								jkcdTmp.setTotalNum(nTotal);
								jkcdTmp.setCompressPath(zaeEntry.getName());
								jkcdTmp.setFilePath(tPath);
								PostUnzipProgress(jkcdTmp);
							}	
						}
					}
					Close();
				}catch (IOException e1) {
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
		if (IsClosed())
			Reset();
		nCount = 0;
		try {
			if (nMode == 0)
			{
				Enumeration<ZipArchiveEntry> e = zFile.getEntries();
				while(e.hasMoreElements()){
					ZipArchiveEntry entry = e.nextElement();
					if (jkvbExit != null && jkvbExit)
						break;
					//如果条目是文件目录，则继续执行
					if(!entry.isDirectory()){
						BufferedInputStream zis = new BufferedInputStream(zFile.getInputStream(entry));
						BufferedOutputStream bos;
						int count ;
						byte date[] = new byte[BUFFER];
						String tPath = tUnzippath + entry.getName();
						JKFile.CreateDir(tPath);
						bos = new BufferedOutputStream(new FileOutputStream(new File(tPath)));
						while((count=zis.read(date))!=-1){
							bos.write(date,0,count);
						}
						bos.flush();
						bos.close();
						
						nCount++;	//记数+1
						zis.close();
					}	
				}
			}
			else {
				ZipArchiveEntry zaeEntry;
				while((zaeEntry = zaisStream.getNextZipEntry()) != null){
					if (jkvbExit != null && jkvbExit)
						break;
					//如果条目是文件目录，则继续执行
					if(!zaeEntry.isDirectory()){
						BufferedOutputStream bos;
						int count ;
						byte date[] = new byte[BUFFER];
						String tPath = tUnzippath + zaeEntry.getName();
						JKFile.CreateDir(tPath);
						bos = new BufferedOutputStream(new FileOutputStream(new File(tPath)));
						while((count = zaisStream.read(date))!=-1){
							bos.write(date,0,count);
						}
						bos.flush();
						bos.close();
						
						nCount++;	//记数+1
					}	
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
	public byte[] UncompressOne(String tSubPath){		
		if (nMode == 0)
		{
			ZipArchiveEntry zeEntry = zFile.getEntry(tSubPath);
			try {
				InputStream is = zFile.getInputStream(zeEntry);
				return JKConvert.toByteArray(is);
			} catch (Exception e) {
				e.printStackTrace();
				JKLog.ErrorLog("获取指定压缩文件失败.原因为:" + e.getMessage());
			}
		}
		return null;
	}
	
	@Override
	public void Reset()
	{
		try {
			if (nMode == 0)
				zFile = new ZipFile(tFilePath,tEncoding);
			else
				zaisStream = new ZipArchiveInputStream(JKFile.ReadAssets(tFilePath),tEncoding);
		} catch (IOException e) {
			e.printStackTrace();
			JKLog.ErrorLog("打开zip文件失败.原因为:" + e.getMessage());
		}
	}
	
	@Override
	public boolean IsClosed()
	{
		if (nMode == 0)
		{
            return zFile == null || zFile.IsClosed();
		}
		else {
            return zaisStream == null || zaisStream.IsClosed();
		}
	}
	
	@Override
	public void Close()
	{
		try {
			if (nMode == 0)
				zFile.close();
			else
				zaisStream.close();
		} catch (IOException e) {
			e.printStackTrace();
			JKLog.ErrorLog("关闭zip流失败.原因为:" + e.getMessage());
		}
	}
}
