package com.jkframework.serialization;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.jkframework.algorithm.JKAnalysis;
import com.jkframework.algorithm.JKConvert;
import com.jkframework.algorithm.JKFile;
import com.jkframework.debug.JKLog;

public class JKHtml extends JKBaseSerialization {

	/**html Dom对象*/
	private Document dmDoc = null;

	@Override
	public void LoadFile(String tPath, String tEncoding) {
		try {
			if (tPath.startsWith("/"))	//sdcard路径
				dmDoc = Jsoup.parse(new File(tPath), tEncoding);
			else
				dmDoc = Jsoup.parse(JKFile.ReadAssetsFile(tPath, tEncoding));
		} catch (IOException e) {
			JKLog.ErrorLog("加载Html文件失败.原因为" + e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void LoadString(String tText) {
		dmDoc = Jsoup.parse(tText);
	}

	@Override
	public void SaveFile(String tPath, String tEncoding) {
		JKFile.WriteFile(tPath, GetString(), tEncoding);
	}

	@Override
	public String GetString() {
		return dmDoc.toString();
	}

	@Override
	public String GetNodeText(String tQuestion) {
		if (dmDoc != null)
		{	
			Element emRoot = dmDoc.child(0);			
			ArrayList<String> a_tRule = JKAnalysis.Split(tQuestion, "/");
			for (int i=1; i<a_tRule.size() - 1; ++i)
			{
				String tRule = a_tRule.get(i);
				String tIndex = JKAnalysis.GetMiddleString(tRule, "[", "]");
				if (tIndex.equals(""))
				{
					Elements nlItems = emRoot.getElementsByTag(a_tRule.get(i)); 
					for (int j=0; j<nlItems.size(); ++j)
					{
						Element emChild = nlItems.get(j);
						if (emChild.parent().equals(emRoot))
						{
							emRoot = emChild;
							break;
						}
					}
				}
				else 
				{
					int nIndex = JKConvert.toInt(tIndex);
					Elements nlItems = emRoot.getElementsByTag(tRule.substring(0,tRule.indexOf("["))); 
					for (int j=0; j<nlItems.size(); ++j)
					{
						Element emChild = nlItems.get(j);
						if (emChild.parent().equals(emRoot))
						{
							if (nIndex == 1)
							{
								emRoot = emChild;
								break;
							}
							else
								nIndex--;
						}
					}
				}
			}
			
			String tRule = a_tRule.get(a_tRule.size() - 1);
			if (tRule.indexOf("@") == 0)	//取属性
			{				
				return emRoot.attr(tRule.substring(1));
			}
			else {
				Elements nlItems = emRoot.getElementsByTag(tRule); 
				for (int j=0; j<nlItems.size(); ++j)
				{
					Element emChild = nlItems.get(j);
					if (emChild.parent().equals(emRoot))
					{
						return emChild.text();
					}
				}
			}
		}
		return "";
	}

	@Override
	public int GetNodeCount(String tQuestion) {
		if (dmDoc != null)
		{	
			Element emRoot = dmDoc.child(0);			
			ArrayList<String> a_tRule = JKAnalysis.Split(tQuestion, "/");
			for (int i=1; i<a_tRule.size() - 1; ++i)
			{
				String tRule = a_tRule.get(i);
				String tIndex = JKAnalysis.GetMiddleString(tRule, "[", "]");
				if (tIndex.equals(""))
				{
					Elements nlItems = emRoot.getElementsByTag(a_tRule.get(i)); 
					for (int j=0; j<nlItems.size(); ++j)
					{
						Element emChild = nlItems.get(j);
						if (emChild.parent().equals(emRoot))
						{
							emRoot = emChild;
							break;
						}
					}
				}
				else 
				{
					int nIndex = JKConvert.toInt(tIndex);
					Elements nlItems = emRoot.getElementsByTag(tRule.substring(0,tRule.indexOf("["))); 
					for (int j=0; j<nlItems.size(); ++j)
					{
						Element emChild = nlItems.get(j);
						if (emChild.parent().equals(emRoot))
						{
							if (nIndex == 1)
							{
								emRoot = emChild;
								break;
							}
							else
								nIndex--;
						}
					}
				}
			}
			
			String tRule = a_tRule.get(a_tRule.size() - 1);
			int nNum = 0;
			Elements nlItems = emRoot.getElementsByTag(tRule); 
			for (int j=0; j<nlItems.size(); ++j)
			{
				Element emChild = nlItems.get(j);
				if (emChild.parent().equals(emRoot))
				{
					nNum++;
				}
			}
			return nNum;
		}
		return 0;
	}

	@Override
	public void CreateNode(String tQuestion, String tText) {
		// TODO Auto-generated method stub
	}

	@Override
	public void CreateNode(String tQuestion, int nText) {
		// TODO Auto-generated method stub
	}

	@Override
	public void CreateNode(String tQuestion, boolean bBool) {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean IsExist(String tQuestion) {
		if (dmDoc != null)
		{	
			Element emRoot = dmDoc.child(0);			
			ArrayList<String> a_tRule = JKAnalysis.Split(tQuestion, "/");
			for (int i=1; i<a_tRule.size() - 1; ++i)
			{
				String tRule = a_tRule.get(i);
				String tIndex = JKAnalysis.GetMiddleString(tRule, "[", "]");
				if (tIndex.equals(""))
				{
					Elements nlItems = emRoot.getElementsByTag(a_tRule.get(i)); 
					for (int j=0; j<nlItems.size(); ++j)
					{
						Element emChild = nlItems.get(j);
						if (emChild.parent().equals(emRoot))
						{
							emRoot = emChild;
							break;
						}
					}
				}
				else 
				{
					int nIndex = JKConvert.toInt(tIndex);
					Elements nlItems = emRoot.getElementsByTag(tRule.substring(0,tRule.indexOf("["))); 
					for (int j=0; j<nlItems.size(); ++j)
					{
						Element emChild = nlItems.get(j);
						if (emChild.parent().equals(emRoot))
						{
							if (nIndex == 1)
							{
								emRoot = emChild;
								break;
							}
							else
								nIndex--;
						}
					}
				}
			}
			
			String tRule = a_tRule.get(a_tRule.size() - 1);
			if (tRule.indexOf("@") == 0)	//取属性
			{				
				Attributes aAttrs = emRoot.attributes();
				return aAttrs.hasKey(tRule.substring(1));
			}
			else {
				Elements nlItems = emRoot.getElementsByTag(tRule); 
				for (int j=0; j<nlItems.size(); ++j)
				{
					Element emChild = nlItems.get(j);
					if (emChild.parent().equals(emRoot))
					{
						return true;
					}
				}
			}
		}
		
		return false;
	}
}
