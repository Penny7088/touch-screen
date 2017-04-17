package com.jkframework.serialization;

import com.jkframework.algorithm.JKAnalysis;
import com.jkframework.algorithm.JKConvert;
import com.jkframework.algorithm.JKFile;
import com.jkframework.debug.JKLog;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.WeakHashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class JKXml extends JKBaseSerialization {

	/**xml Dom对象*/
	private Document dmDoc = null;
	/**HashMap缓存*/
	private WeakHashMap<String, ArrayList<Element>> h_Elments = new WeakHashMap<>();
	
	
	@Override
	public void LoadFile(String tPath, String tEncoding) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();
			if (tPath.startsWith("/"))	//sdcard路径
				dmDoc = builder.parse(new File(tPath));
			else
			{
				InputStream isStream = JKFile.ReadAssets(tPath);
				if (isStream != null) {
					dmDoc = builder.parse(isStream);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			JKLog.ErrorLog("读取xml文件失败.原因为" + e.getMessage());
		} 
	}
	

	@Override
	public void LoadString(String tText) {
		StringReader stRead = new StringReader(tText);
	    InputSource iscSource = new InputSource(stRead);
	    
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();
			dmDoc = builder.parse(iscSource);
		} catch (Exception e) {
			e.printStackTrace();
			JKLog.ErrorLog("读取xml字符串失败.原因为" + e.getMessage());
		} 
	}

	@Override
	public void SaveFile(String tPath, String tEncoding) {
		Source source = new DOMSource(dmDoc);
		Result result = new StreamResult(new File(tPath));
		
		Transformer xformer;
		try {
			xformer = TransformerFactory.newInstance()
					.newTransformer();
			xformer.setOutputProperty(OutputKeys.ENCODING, tEncoding);
			xformer.transform(source, result);
		} catch (Exception e) {
			e.printStackTrace();
			JKLog.ErrorLog("保存xml文件失败.原因为" + e.getMessage());
		} 
	}

	@Override
	public String GetString() {
		Source source = new DOMSource(dmDoc);
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		OutputStreamWriter write = new OutputStreamWriter(outStream);
		Result result = new StreamResult(write);
		
		Transformer xformer;
		try {
			xformer = TransformerFactory.newInstance()
					.newTransformer();
			xformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			xformer.transform(source, result);
		} catch (Exception e) {
			e.printStackTrace();
			JKLog.ErrorLog("获取xml字符串失败.原因为" + e.getMessage());
		} 
		
		return outStream.toString();
	}
	
	@Override
	public boolean IsExist(String tQuestion) {
		if (dmDoc != null)
		{	
			StringBuilder tFullPath = new StringBuilder();
			Element emRoot = dmDoc.getDocumentElement(); 
			ArrayList<String> a_tRule = JKAnalysis.Split(tQuestion, "/");
			for (int i=1; i<a_tRule.size() - 1; ++i)
			{
				String tRule = a_tRule.get(i);
				String tIndex = JKAnalysis.GetMiddleString(tRule, "[", "]");
				if (tIndex.equals(""))
				{
					tFullPath.append("/").append(a_tRule.get(i));
					ArrayList<Element> a_eElements = h_Elments.get(tFullPath.toString());
					if (a_eElements == null)
					{
						NodeList nlItems = emRoot.getElementsByTagName(a_tRule.get(i));
						a_eElements = new ArrayList<>();
						for (int j=0; j<nlItems.getLength(); ++j)
						{
							Element emChild = (Element) nlItems.item(j);
							if (emChild.getParentNode().equals(emRoot))
							{
								a_eElements.add(emChild);
							}
						}
						h_Elments.put(tFullPath.toString(), a_eElements);
					}
					emRoot = a_eElements.get(0);
				}
				else 
				{
					int nIndex = JKConvert.toInt(tIndex);
					tFullPath.append("/").append(tRule.substring(0, tRule.indexOf("[")));
					 
					ArrayList<Element> a_eElements = h_Elments.get(tFullPath.toString());
					if (a_eElements == null)
					{
						NodeList nlItems = emRoot.getElementsByTagName(tRule.substring(0,tRule.indexOf("["))); 
						a_eElements = new ArrayList<>();
						for (int j=0; j<nlItems.getLength(); ++j)
						{
							Element emChild = (Element) nlItems.item(j);
							if (emChild.getParentNode().equals(emRoot))
							{
								a_eElements.add(emChild);
							}
						}
						h_Elments.put(tFullPath.toString(), a_eElements);
					}
					tFullPath.append("[").append(nIndex).append("]");
					emRoot = a_eElements.get(nIndex - 1);
				}
			}
			
			String tRule = a_tRule.get(a_tRule.size() - 1);
			if (tRule.indexOf("@") == 0)	//取属性
			{
				Attr aAttr = emRoot.getAttributeNode(tRule.substring(1));
                return aAttr != null;
			}
			else {
				tFullPath.append("/").append(a_tRule);
				ArrayList<Element> a_eElements = h_Elments.get(tFullPath.toString());
				if (a_eElements == null)
				{
					NodeList nlItems = emRoot.getElementsByTagName(tRule);
					a_eElements = new ArrayList<>();
					for (int j=0; j<nlItems.getLength(); ++j)
					{
						Element emChild = (Element) nlItems.item(j);
						if (emChild.getParentNode().equals(emRoot))
						{
							a_eElements.add(emChild);
						}
					}
					h_Elments.put(tFullPath.toString(), a_eElements);
				}
				if (a_eElements.size() > 0)
					return true;
			}
		}
		return false;
	}
	
	@Override
	public String GetNodeText(String tQuestion) {
		if (dmDoc != null)
		{	
			StringBuilder tFullPath = new StringBuilder();
			Element emRoot = dmDoc.getDocumentElement();
			ArrayList<String> a_tRule = JKAnalysis.Split(tQuestion, "/");
			for (int i=1; i<a_tRule.size() - 1; ++i)
			{
				String tRule = a_tRule.get(i);
				String tIndex = JKAnalysis.GetMiddleString(tRule, "[", "]");
				if (tIndex.equals(""))
				{
					tFullPath.append("/").append(a_tRule.get(i));
					ArrayList<Element> a_eElements = h_Elments.get(tFullPath.toString());
					if (a_eElements == null)
					{
						NodeList nlItems = emRoot.getElementsByTagName(a_tRule.get(i));
						a_eElements = new ArrayList<>();
						for (int j=0; j<nlItems.getLength(); ++j)
						{
							Element emChild = (Element) nlItems.item(j);
							if (emChild.getParentNode().equals(emRoot))
							{
								a_eElements.add(emChild);
							}
						}
						h_Elments.put(tFullPath.toString(), a_eElements);
					}
					emRoot = a_eElements.get(0);
				}
				else 
				{
					int nIndex = JKConvert.toInt(tIndex);
					tFullPath.append("/").append(tRule.substring(0, tRule.indexOf("[")));
					 
					ArrayList<Element> a_eElements = h_Elments.get(tFullPath.toString());
					if (a_eElements == null)
					{
						NodeList nlItems = emRoot.getElementsByTagName(tRule.substring(0,tRule.indexOf("["))); 
						a_eElements = new ArrayList<>();
						for (int j=0; j<nlItems.getLength(); ++j)
						{
							Element emChild = (Element) nlItems.item(j);
							if (emChild.getParentNode().equals(emRoot))
							{
								a_eElements.add(emChild);
							}
						}
						h_Elments.put(tFullPath.toString(), a_eElements);
					}
					tFullPath.append("[").append(nIndex).append("]");
					emRoot = a_eElements.get(nIndex - 1);
				}
			}
			
			String tRule = a_tRule.get(a_tRule.size() - 1);
			if (tRule.indexOf("@") == 0)	//取属性
			{
				return emRoot.getAttribute(tRule.substring(1));
			}
			else {
				tFullPath.append("/").append(a_tRule);
				ArrayList<Element> a_eElements = h_Elments.get(tFullPath.toString());
				if (a_eElements == null)
				{
					NodeList nlItems = emRoot.getElementsByTagName(tRule);
					a_eElements = new ArrayList<>();
					for (int j=0; j<nlItems.getLength(); ++j)
					{
						Element emChild = (Element) nlItems.item(j);
						if (emChild.getParentNode().equals(emRoot))
						{
							a_eElements.add(emChild);
						}
					}
					h_Elments.put(tFullPath.toString(), a_eElements);
				}
				if (a_eElements.size() == 0)
				{
					JKLog.ErrorLog("Xml节点不存在");	
					return "";
				}
				Element emChild = a_eElements.get(0);
				return emChild.getTextContent();			
			}
		}
		
		return "";
	}
	
	@Override
	public int GetNodeCount(String tQuestion) {
		if (dmDoc != null)
		{	
			StringBuilder tFullPath = new StringBuilder();
			Element emRoot = dmDoc.getDocumentElement(); 
			ArrayList<String> a_tRule = JKAnalysis.Split(tQuestion, "/");
			for (int i=1; i<a_tRule.size() - 1; ++i)
			{
				String tRule = a_tRule.get(i);
				String tIndex = JKAnalysis.GetMiddleString(tRule, "[", "]");
				if (tIndex.equals(""))
				{
					tFullPath.append("/").append(a_tRule.get(i));
					ArrayList<Element> a_eElements = h_Elments.get(tFullPath.toString());
					if (a_eElements == null)
					{
						NodeList nlItems = emRoot.getElementsByTagName(a_tRule.get(i));
						a_eElements = new ArrayList<>();
						for (int j=0; j<nlItems.getLength(); ++j)
						{
							Element emChild = (Element) nlItems.item(j);
							if (emChild.getParentNode().equals(emRoot))
							{
								a_eElements.add(emChild);
							}
						}
						h_Elments.put(tFullPath.toString(), a_eElements);
					}
					emRoot = a_eElements.get(0);
				}
				else 
				{
					int nIndex = JKConvert.toInt(tIndex);
					tFullPath.append("/").append(tRule.substring(0, tRule.indexOf("[")));
					 
					ArrayList<Element> a_eElements = h_Elments.get(tFullPath.toString());
					if (a_eElements == null)
					{
						NodeList nlItems = emRoot.getElementsByTagName(tRule.substring(0,tRule.indexOf("["))); 
						a_eElements = new ArrayList<>();
						for (int j=0; j<nlItems.getLength(); ++j)
						{
							Element emChild = (Element) nlItems.item(j);
							if (emChild.getParentNode().equals(emRoot))
							{
								a_eElements.add(emChild);
							}
						}
						h_Elments.put(tFullPath.toString(), a_eElements);
					}
					tFullPath.append("[").append(nIndex).append("]");
					emRoot = a_eElements.get(nIndex - 1);
				}
			}
			
			tFullPath.append("/").append(a_tRule);
			ArrayList<Element> a_eElements = h_Elments.get(tFullPath.toString());
			if (a_eElements == null)
			{
				NodeList nlItems = emRoot.getElementsByTagName(a_tRule.get(a_tRule.size() - 1));
				a_eElements = new ArrayList<>();
				for (int j=0; j<nlItems.getLength(); ++j)
				{
					Element emChild = (Element) nlItems.item(j);
					if (emChild.getParentNode().equals(emRoot))
					{
						a_eElements.add(emChild);
					}
				}
				h_Elments.put(tFullPath.toString(), a_eElements);
			}
			return a_eElements.size();
		}
		
		return 0;
	}
	
	@Override
	public void CreateNode(String tQuestion, String tText) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		if (tQuestion.contains("/"))	//非根节点
		{
			if (dmDoc != null)
			{
				Element emRoot = dmDoc.getDocumentElement(); 
				ArrayList<String> a_tRule = JKAnalysis.Split(tQuestion, "/");
				for (int i=1; i<a_tRule.size() - 1; ++i)
				{
					NodeList nlItems = emRoot.getElementsByTagName(a_tRule.get(i)); 
					for (int j=0; j<nlItems.getLength(); ++j)
					{
						Element emChild = (Element) nlItems.item(j);
						if (emChild.getParentNode().equals(emRoot))
						{
							emRoot = emChild;
							break;
						}
					}
				}
				
				String tRule = a_tRule.get(a_tRule.size() - 1);
				if (tRule.contains("@"))	//属性值
				{		
					emRoot.setAttribute(tRule.substring(1),tText);
				}
				else {
					Element emChild = dmDoc.createElement(a_tRule.get(a_tRule.size() - 1));
					emChild.setTextContent(tText);
					emRoot.appendChild(emChild);
				}
			}
		}
		else {
			try {
				DocumentBuilder builder = factory.newDocumentBuilder();
				dmDoc = builder.newDocument();
				Element emRoot = dmDoc.createElement(tQuestion);
				dmDoc.appendChild(emRoot);	
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
				JKLog.ErrorLog("创建xml根节点失败.原因为" + e.getMessage());	
			}	
		}
	}

	@Override
	public void CreateNode(String tQuestion, int nText) {
		CreateNode(tQuestion,JKConvert.toString(nText));
	}

	@Override
	public void CreateNode(String tQuestion, boolean bBool) {
		CreateNode(tQuestion,bBool ? "1" : "0");
	}
}
