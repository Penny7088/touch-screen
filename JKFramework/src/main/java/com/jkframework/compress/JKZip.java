package com.jkframework.compress;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;

import org.apache.commons.compress.archivers.zip.Zip64Mode;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;

import com.jkframework.algorithm.JKFile;
import com.jkframework.bean.JKCompressData;
import com.jkframework.callback.JKBaseCompressListener;
import com.jkframework.debug.JKLog;
import com.jkframework.thread.JKThread;
import com.jkframework.thread.JKThread.JKThreadListener;


public class JKZip extends JKBaseCompress {

	/**zip文件对象*/
	private ZipArchiveOutputStream zaosOutput;
	
	
	
	@Override
	public void OpenZip(String tZipPath)
	{
		try {
			CheckedOutputStream cos = new CheckedOutputStream(new FileOutputStream(tZipPath), new CRC32());
			zaosOutput = new ZipArchiveOutputStream(cos);
			zaosOutput.setUseZip64(Zip64Mode.AsNeeded);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			JKLog.ErrorLog("指定压缩文件路径失败.原因为:" + e.getMessage());
		}  
	}
	
	@Override
	public boolean Compress(String tZippath,String tFilePath){			
		try {
			if (tFilePath == null)
			{
				ZipArchiveEntry zaeEntry = new ZipArchiveEntry(tZippath);
				zaosOutput.putArchiveEntry(zaeEntry);	
				zaosOutput.closeArchiveEntry(); 
				return true;
			}
			
			File fFile = new File(tFilePath);
			/*Buff空间*/
			int BUFFER = 1024 * 16;
			if (fFile.isDirectory())
			{
				ArrayList<String> a_tFolders = JKFile.GetFolders(tFilePath, true);
				for (int i=0; i<a_tFolders.size(); ++i)
				{
					String tTmp = a_tFolders.get(i);
					ZipArchiveEntry zaeEntry = new ZipArchiveEntry(tZippath + tTmp.substring(tFilePath.length()) + "/");
					zaosOutput.putArchiveEntry(zaeEntry);
					zaosOutput.closeArchiveEntry(); 
				}
				
				ArrayList<String> a_tList = JKFile.GetFiles(tFilePath, true);
				for (int i=0; i<a_tList.size(); ++i)
				{
					String tTmp = a_tList.get(i);
					ZipArchiveEntry zaeEntry = new ZipArchiveEntry(tZippath + tTmp.substring(tFilePath.length()));
					zaosOutput.putArchiveEntry(zaeEntry);		
					
					InputStream is = new FileInputStream(tTmp);
					byte[] buffer = new byte[BUFFER];
					int len;
					while ((len = is.read(buffer)) != -1) {
						//把缓冲区的字节写入到ZipArchiveEntry
						zaosOutput.write(buffer, 0, len);
					}
					is.close();
		            zaosOutput.closeArchiveEntry(); 
				}
			}
			else {
				ZipArchiveEntry zaeEntry = new ZipArchiveEntry(tZippath);
				zaosOutput.putArchiveEntry(zaeEntry);		
				
				InputStream is = new FileInputStream(fFile);
				byte[] buffer = new byte[BUFFER];
				int len;
				while ((len = is.read(buffer)) != -1) {
					//把缓冲区的字节写入到ZipArchiveEntry
					zaosOutput.write(buffer, 0, len);
				}
				is.close();
	            zaosOutput.closeArchiveEntry(); 
			}
		} catch (IOException e) {
			e.printStackTrace();
			JKLog.ErrorLog("添加压缩文件失败.原因为:" + e.getMessage());
			return false;
		}
		return true;
	}
	
	@Override
	public boolean Compress(ArrayList<String> a_tZippath,ArrayList<String> a_tFilePath){	
		int nSize = Math.min(a_tZippath.size(), a_tFilePath.size());
		for (int i=0; i<nSize; ++i)
		{
			boolean bResult = Compress(a_tZippath.get(i), a_tFilePath.get(i));
			if (!bResult)
				return false;
		}
		return true;
	}
	
	@Override
	public void CompressAsync(final JKBaseCompressListener l,final ArrayList<String> a_tZippath,final ArrayList<String> a_tFilePath){
		m_Listener = l;
		PostZipStatus(ZIP_START);
		
		new JKThread().Start(new JKThreadListener() {
			
			@Override
			public void OnThread() {
				int nSize = Math.min(a_tZippath.size(), a_tFilePath.size());
				for (int i=0; i<nSize; ++i)
				{
					JKCompressData jkcdTmp = new JKCompressData();
					jkcdTmp.setCurrentNum(i + 1);
					jkcdTmp.setTotalNum(nSize);
					jkcdTmp.setCompressPath(a_tZippath.get(i));
					jkcdTmp.setFilePath(a_tFilePath.get(i));
					PostZipProgress(jkcdTmp);
					boolean bResult = Compress(a_tZippath.get(i), a_tFilePath.get(i));
					if (!bResult)
					{
						PostZipStatus(ZIP_FAILD);
						return;
					}
				}
				
				PostZipStatus(ZIP_OK);
			}
			
			@Override
			public void OnMain() {
			}
		});
	}
	
	
}
