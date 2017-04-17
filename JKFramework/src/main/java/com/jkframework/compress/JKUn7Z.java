package com.jkframework.compress;

import com.jkframework.algorithm.JKFile;
import com.jkframework.bean.JKCompressData;
import com.jkframework.callback.JKBaseUncompressListener;
import com.jkframework.debug.JKLog;
import com.jkframework.thread.JKThread;
import com.jkframework.thread.JKThread.JKThreadListener;

import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class JKUn7Z extends JKBaseUncompress{
	
	/**7z文件对象*/
	private SevenZFile szFile;
	/**文件路径*/
	private String tFilePath;
	/**当前解压数*/
	private int nCount = 0;
	/**当前解压总数*/
	private int nTotal = 0;

	
	@Override
	public int OpenFile(String tFilepath, String tEncoding) {
		this.tFilePath = tFilepath;
		try {
			szFile = new SevenZFile(new File(tFilepath));		
			nTotal = szFile.GetSize();
		} catch (IOException e) {
			e.printStackTrace();
			JKLog.ErrorLog("打开SD卡上7z文件失败.原因为:" + e.getMessage());
			return -1;
		}
		return nTotal;

	}
	
	@Override
	public int OpenAssetsFile(String tAssetsName,String tEncoding)
	{		
		this.tFilePath = JKFile.GetPublicCachePath() + "/JKCache/JKUn7Z/" + tAssetsName;
		if (!JKFile.toAssetsMD5(tAssetsName).equals(JKFile.toMD5(JKFile.GetPublicCachePath() + "/JKCache/JKUn7Z/" + tAssetsName)))
			JKFile.AssetsToSDCard(tAssetsName, JKFile.GetPublicCachePath() + "/JKCache/JKUn7Z/" + tAssetsName);
		try {
			szFile = new SevenZFile(new File(JKFile.GetPublicCachePath() + "/JKCache/JKUn7Z/" + tAssetsName));
			nTotal = szFile.GetSize();
		} catch (IOException e) {
			e.printStackTrace();
			JKLog.ErrorLog("打开asset上7z文件失败.原因为:" + e.getMessage());
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
					SevenZArchiveEntry szaeEntry = szFile.getNextEntry();
					while(szaeEntry != null){
						if (jkvbExit != null && jkvbExit)
							break;
						//如果条目是文件目录，则继续执行
						if(szaeEntry.isDirectory()){
							szaeEntry = szFile.getNextEntry();
							continue;
						}else{		
							String tPath = tUnzippath + szaeEntry.getName();
							JKFile.CreateDir(tPath);
							FileOutputStream out = new FileOutputStream(tPath);
					        byte[] content = new byte[(int) szaeEntry.getSize()];
					        szFile.read(content, 0, content.length);
					        out.write(content);
					        out.close();
					        
							nCount++;	//记数+1
							JKCompressData jkcdTmp = new JKCompressData();
							jkcdTmp.setCurrentNum(nCount);
							jkcdTmp.setTotalNum(nTotal);
							jkcdTmp.setCompressPath(szaeEntry.getName());
							jkcdTmp.setFilePath(tPath);
							PostUnzipProgress(jkcdTmp);
						}	
						szaeEntry = szFile.getNextEntry();						
					}
					Close();
				}catch (IOException e1) {
					JKLog.ErrorLog("解压缩文件失败.原因为:" + e1.getMessage());
					PostUnzipStatus(UNZIP_FAILD);
					return;
				}
				Close();
				PostUnzipStatus(UNZIP_OK);
			}
			
			@Override
			public void OnMain() {
			}
		});
	}
	
	@Override
	public boolean Uncompress(String tUnzippath,Boolean jkvbExit){			
		//循环读取文件条目，只要不为空，就进行处理
		try {
			if (IsClosed())
				Reset();
			nCount = 0;
			SevenZArchiveEntry szaeEntry = szFile.getNextEntry();
			while(szaeEntry != null){
				if (jkvbExit != null && jkvbExit)
					break;
				//如果条目是文件目录，则继续执行
				if(szaeEntry.isDirectory()){
					szaeEntry = szFile.getNextEntry();
					continue;
				}else{		
					String tPath = tUnzippath + szaeEntry.getName();
					JKFile.CreateDir(tPath);
					FileOutputStream out = new FileOutputStream(tPath);
			        byte[] content = new byte[(int) szaeEntry.getSize()];
			        szFile.read(content, 0, content.length);
			        out.write(content);
			        out.close();
			        
					nCount++;	//记数+1
				}	
				szaeEntry = szFile.getNextEntry();
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
				szFile = new SevenZFile(new File(tFilePath));
			else
				szFile = new SevenZFile(new File(JKFile.GetPublicCachePath() + "/JKCache/JKUn7Z/" + tFilePath));
		} catch (IOException e) {
			e.printStackTrace();
			JKLog.ErrorLog("复位7z流失败.原因为:" + e.getMessage());
		}
	}
	
	@Override
	public boolean IsClosed()
	{
        return szFile == null || szFile.IsClosed();
	}
	
	@Override
	public void Close()
	{
		try {
			szFile.close();
		} catch (IOException e) {
			e.printStackTrace();
			JKLog.ErrorLog("关闭7z流失败.原因为:" + e.getMessage());
		}
	}
	
	@Override
	public byte[] UncompressOne(String tSubPath) {
		return null;
	}
}
