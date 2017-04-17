package com.jkframework.bean;





public class JKCompressData
{
	/**当前文件进度*/
	private int CurrentNum;
	/**总文件进度*/
	private int TotalNum;
	/**压缩包内路径*/
	private String CompressPath;
	/**压缩文件路径*/
	private String FilePath;
	
	public int getCurrentNum() {
		return CurrentNum;
	}
	
	public void setCurrentNum(int currentNum) {
		CurrentNum = currentNum;
	}
	
	public int getTotalNum() {
		return TotalNum;
	}
	
	public void setTotalNum(int totalNum) {
		TotalNum = totalNum;
	}
	
	public String getCompressPath() {
		return CompressPath;
	}
	
	public void setCompressPath(String compressPath) {
		CompressPath = compressPath;
	}
	
	public String getFilePath() {
		return FilePath;
	}
	
	public void setFilePath(String filePath) {
		FilePath = filePath;
	}
	
}