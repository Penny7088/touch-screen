package com.jkframework.serialization;

import com.google.gson.Gson;
import com.jkframework.algorithm.JKFile;

public class JKJsonSerialization {

	private static final Gson gson = new Gson();
	static {
//		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

	public static  <T> T LoadFile(String tPath, String tEncoding,Class<T> cClass) {
		String tJson;
		if (tPath.startsWith("/"))	//sdcard路径
			tJson = JKFile.ReadFile(tPath,tEncoding);
		else
			tJson = JKFile.ReadAssetsFile(tPath,tEncoding);
        return gson.fromJson(tJson,cClass);
	}

	public static <T> T LoadString(String tText,Class<T> cClass) {
        return gson.fromJson(tText,cClass);
	}

	public static void SaveFile(String tPath, String tEncoding,Object oObject) {
		try {
			JKFile.WriteFile(tPath, gson.toJson(oObject), tEncoding);
		} catch (Exception ignored) {
		}
	}

	public static String GetString(Object oObject) {
        return gson.toJson(oObject);
	}

}
