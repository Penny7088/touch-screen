package com.jkframework.serialization;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jkframework.algorithm.JKAnalysis;
import com.jkframework.algorithm.JKConvert;
import com.jkframework.algorithm.JKFile;
import com.jkframework.debug.JKLog;

public class JKJson extends JKBaseSerialization{

	/**Json根对象*/
	private JSONObject jsoJson = new JSONObject();

	@Override
	public void LoadFile(String tPath, String tEncoding) {
		try {
			if (tPath.startsWith("/"))	//sdcard路径
				jsoJson = new JSONObject(JKFile.ReadFile(tPath,tEncoding));
			else
				jsoJson = new JSONObject(JKFile.ReadAssetsFile(tPath,tEncoding));
		} catch (JSONException e) {
			JKLog.ErrorLog("读取json失败.原因为" + e.getMessage());
		}
	}

	@Override
	public void LoadString(String tText) {
		try {
			jsoJson = new JSONObject(tText);
		} catch (JSONException e) {
			JKLog.ErrorLog("设置json失败.原因为" + e.getMessage());
		}
	}

	@Override
	public void SaveFile(String tPath, String tEncoding) {
		JKFile.WriteFile(tPath, GetString(), tEncoding);
	}

	@Override
	public String GetString() {
		return jsoJson.toString();
	}

	@Override
	public String GetNodeText(String tQuestion) {
		if (jsoJson != null)
		{		
			JSONObject jsoTmp = jsoJson;
			ArrayList<String> a_tRule = JKAnalysis.Split(tQuestion, "/");
			for (int i=0; i<a_tRule.size() - 1; ++i)
			{
				try {
					String tRule = a_tRule.get(i);
					String tIndex = JKAnalysis.GetMiddleString(tRule, "[", "]");
					if (tIndex.equals(""))
						jsoTmp = jsoTmp.getJSONObject(a_tRule.get(i));
					else 
					{
						JSONArray jsaArray = jsoTmp.getJSONArray(tRule.substring(0,tRule.indexOf("[")));						
						jsoTmp = jsaArray.getJSONObject(JKConvert.toInt(tIndex) - 1);
					}
				} catch (JSONException e) {
					JKLog.ErrorLog("获取json数据出现错误.原因为" + e.getMessage());
					return "";
				}
			}
			try {
				String tRule = a_tRule.get(a_tRule.size() - 1);
				return jsoTmp.getString(tRule);				
			} catch (JSONException e) {
				JKLog.ErrorLog("获取json数据出现错误.原因为" + e.getMessage());
				return "";
			}
		}
		return "";
	}

	@Override
	public int GetNodeCount(String tQuestion) {
		if (jsoJson != null)
		{			
			JSONObject jsoTmp = jsoJson;
			ArrayList<String> a_tRule = JKAnalysis.Split(tQuestion, "/");
			for (int i=0; i<a_tRule.size() - 1; ++i)
			{
				try {
					String tRule = a_tRule.get(i);
					String tIndex = JKAnalysis.GetMiddleString(tRule, "[", "]");
					if (tIndex.equals(""))
						jsoTmp = jsoTmp.getJSONObject(a_tRule.get(i));
					else 
					{
						JSONArray jsaArray = jsoTmp.getJSONArray(tRule.substring(0,tRule.indexOf("[")));						
						jsoTmp = jsaArray.getJSONObject(JKConvert.toInt(tIndex) - 1);
					}
				} catch (JSONException e) {
					JKLog.ErrorLog("获取json数据出现错误.原因为" + e.getMessage());
					return 0;
				}
			}
			try {
				String tRule = a_tRule.get(a_tRule.size() - 1);
				if (jsoTmp.isNull(tRule))
				{
					return 0;
				}
				else {
					JSONArray jsaArray = jsoTmp.getJSONArray(tRule);
					return jsaArray.length();
				}
			} catch (JSONException e) {
				JKLog.ErrorLog("获取json数据出现错误.原因为" + e.getMessage());
				return 1;
			}
		}
		return 0;
	}

	@Override
	public boolean IsExist(String tQuestion) {
		if (jsoJson != null)
		{
			JSONObject jsoTmp = jsoJson;
			ArrayList<String> a_tRule = JKAnalysis.Split(tQuestion, "/");
			for (int i=0; i<a_tRule.size() - 1; ++i)
			{
				try {
					String tRule = a_tRule.get(i);
					String tIndex = JKAnalysis.GetMiddleString(tRule, "[", "]");
					if (tIndex.equals(""))
						jsoTmp = jsoTmp.getJSONObject(a_tRule.get(i));
					else 
					{
						JSONArray jsaArray = jsoTmp.getJSONArray(tRule.substring(0,tRule.indexOf("[")));
						jsoTmp = jsaArray.getJSONObject(JKConvert.toInt(tIndex) - 1);
					}
				} catch (JSONException e) {
					return false;
				}
			}
			return !jsoTmp.isNull(a_tRule.get(a_tRule.size() - 1));
		}
		return false;
	}

	@Override
	public void CreateNode(String tQuestion, String tText) {
		if (jsoJson != null)
		{			
			JSONObject jsoTmp = jsoJson;
			ArrayList<String> a_tRule = JKAnalysis.Split(tQuestion, "/");
			for (int i=0; i<a_tRule.size() - 1; ++i)
			{
				try {
					String tRule = a_tRule.get(i);
					String tIndex = JKAnalysis.GetMiddleString(tRule, "[", "]");
					if (tIndex.equals(""))
					{
						if (jsoTmp.isNull(a_tRule.get(i)))
						{
							jsoTmp.put(tRule, new JSONObject());
						}
						jsoTmp = jsoTmp.getJSONObject(a_tRule.get(i));
					}
					else 
					{
                        if (jsoTmp.isNull(tRule.substring(0, tRule.indexOf("["))))
                        {
                            jsoTmp.put(tRule.substring(0,tRule.indexOf("[")), new JSONArray());
                        }
						JSONArray jsaArray = jsoTmp.getJSONArray(tRule.substring(0,tRule.indexOf("[")));
                        if (jsaArray.isNull(JKConvert.toInt(tIndex) - 1))
                        {
                            jsaArray.put(JKConvert.toInt(tIndex) - 1,new JSONObject());
                        }
						jsoTmp = jsaArray.getJSONObject(JKConvert.toInt(tIndex) - 1);
					}
				} catch (JSONException e) {
					JKLog.ErrorLog("创建json数据出现错误.原因为" + e.getMessage());
					return;
				}
			}
			try {
				String tRule = a_tRule.get(a_tRule.size() - 1);
				jsoTmp.put(tRule, tText);	
			} catch (JSONException e) {
				JKLog.ErrorLog("创建json数据出现错误.原因为" + e.getMessage());
            }
		}
	}

	@Override
	public void CreateNode(String tQuestion, int nText) {
		if (jsoJson != null)
		{
			JSONObject jsoTmp = jsoJson;
			ArrayList<String> a_tRule = JKAnalysis.Split(tQuestion, "/");
			for (int i=0; i<a_tRule.size() - 1; ++i)
			{
				try {
					String tRule = a_tRule.get(i);
					String tIndex = JKAnalysis.GetMiddleString(tRule, "[", "]");
					if (tIndex.equals(""))
					{
						if (jsoTmp.isNull(a_tRule.get(i)))
						{
							jsoTmp.put(tRule, new JSONObject());
						}
						jsoTmp = jsoTmp.getJSONObject(a_tRule.get(i));
					}
					else
					{
						if (jsoTmp.isNull(tRule.substring(0, tRule.indexOf("["))))
						{
							jsoTmp.put(tRule.substring(0,tRule.indexOf("[")), new JSONArray());
						}
						JSONArray jsaArray = jsoTmp.getJSONArray(tRule.substring(0,tRule.indexOf("[")));
						if (jsaArray.isNull(JKConvert.toInt(tIndex) - 1))
						{
							jsaArray.put(JKConvert.toInt(tIndex) - 1,new JSONObject());
						}
						jsoTmp = jsaArray.getJSONObject(JKConvert.toInt(tIndex) - 1);
					}
				} catch (JSONException e) {
					JKLog.ErrorLog("创建json数据出现错误.原因为" + e.getMessage());
					return;
				}
			}
			try {
				String tRule = a_tRule.get(a_tRule.size() - 1);
				jsoTmp.put(tRule, nText);
			} catch (JSONException e) {
				JKLog.ErrorLog("创建json数据出现错误.原因为" + e.getMessage());
			}
		}
	}

	@Override
	public void CreateNode(String tQuestion, boolean bBool) {
		if (jsoJson != null)
		{
			JSONObject jsoTmp = jsoJson;
			ArrayList<String> a_tRule = JKAnalysis.Split(tQuestion, "/");
			for (int i=0; i<a_tRule.size() - 1; ++i)
			{
				try {
					String tRule = a_tRule.get(i);
					String tIndex = JKAnalysis.GetMiddleString(tRule, "[", "]");
					if (tIndex.equals(""))
					{
						if (jsoTmp.isNull(a_tRule.get(i)))
						{
							jsoTmp.put(tRule, new JSONObject());
						}
						jsoTmp = jsoTmp.getJSONObject(a_tRule.get(i));
					}
					else
					{
						if (jsoTmp.isNull(tRule.substring(0, tRule.indexOf("["))))
						{
							jsoTmp.put(tRule.substring(0,tRule.indexOf("[")), new JSONArray());
						}
						JSONArray jsaArray = jsoTmp.getJSONArray(tRule.substring(0,tRule.indexOf("[")));
						if (jsaArray.isNull(JKConvert.toInt(tIndex) - 1))
						{
							jsaArray.put(JKConvert.toInt(tIndex) - 1,new JSONObject());
						}
						jsoTmp = jsaArray.getJSONObject(JKConvert.toInt(tIndex) - 1);
					}
				} catch (JSONException e) {
					JKLog.ErrorLog("创建json数据出现错误.原因为" + e.getMessage());
					return;
				}
			}
			try {
				String tRule = a_tRule.get(a_tRule.size() - 1);
				jsoTmp.put(tRule, bBool);
			} catch (JSONException e) {
				JKLog.ErrorLog("创建json数据出现错误.原因为" + e.getMessage());
			}
		}
	}
}
