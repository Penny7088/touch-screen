package com.jkframework.net;


import android.os.Handler;
import android.os.Message;

import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.jkframework.algorithm.JKConvert;
import com.jkframework.algorithm.JKFile;
import com.jkframework.callback.JKSocketLinstener;
import com.jkframework.debug.JKDebug;
import com.jkframework.debug.JKLog;

import java.io.File;
import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class JKHttpClient
{
	/** Http请求类 */
	private static final OkHttpClient.Builder okhcClient = new OkHttpClient().newBuilder();
	static{
		ClearableCookieJar cookieJar =
				new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(JKDebug.hContext));
		HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
		logging.setLevel(HttpLoggingInterceptor.Level.BODY);
		File httpCacheDirectory = new File(JKFile.GetPublicCachePath() + "/JKCache/JKHttpClient");

		okhcClient
				.connectTimeout(10,TimeUnit.SECONDS)
				.writeTimeout(10,TimeUnit.SECONDS)
                .readTimeout(10,TimeUnit.SECONDS)
				.addInterceptor(logging)
				.cache(new Cache(httpCacheDirectory,100 * 1024 * 1024))
				.cookieJar(cookieJar);
	}
	/** Http发送构造对象 */
	private Request.Builder rbSend;
	/** Http接受对象 */
	private Response rRequest;
	/** 表单构造对象 */
	private FormBody.Builder febForm = new FormBody.Builder();
	/** 参数对象 */
	private RequestBody rbBody;
    /**contenttype类型*/
    private String tContentType = null;
	/**Http回调监听*/
	private JKSocketLinstener m_socket;
	/**发送模式(0为Get类型,1为Post类型)*/
	private int nMode = 0;
	/**Post参数类型(-1为无参数,0为流类型,1为键值对类型)*/
	private int nPostParam = -1;
	/**内容下载地址*/
    private String tDownPath = "";
    /**handler对象*/
    final MyHandler Handler = new MyHandler();

	/**请求成功消息*/
	final static private int RECEIVE_OK = 0;
	/**请求失败消息*/
	final static private int RECEIVE_FAILD = 1;

	static class MyHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what)
			{
				case RECEIVE_OK:
				{
					Object[] a_oObjects = (Object[]) msg.obj;
					HashMap<String,String> h_tHead = new HashMap<>();
					Headers hdHead = ((Response)a_oObjects[3]).headers();
					Map<String, List<String>> h_hHead =  hdHead.toMultimap();
					for(Map.Entry<String, List<String>> me:h_hHead.entrySet()) {
						List<String> a_tValue = me.getValue();
						for (int i=0; i<a_tValue.size(); ++i)
						{
							h_tHead.put(me.getKey(),a_tValue.get(i));
						}
					}
                    JKSocketLinstener m_Tmp = (JKSocketLinstener) a_oObjects[2];
                    if (m_Tmp != null)
                        m_Tmp.ReceiveOK(h_tHead,(String) a_oObjects[1], (byte[]) a_oObjects[0]);
					break;
				}
				case RECEIVE_FAILD:
				{
					int nBack = msg.arg1;
                    Object[] a_oObjects = (Object[]) msg.obj;
                    JKSocketLinstener m_Tmp = (JKSocketLinstener) a_oObjects[0];
                    if (m_Tmp != null)
                        m_Tmp.ReceiveFaild(nBack);
					break;
				}
			}
		}
	}

	/**
	 * 设置类型
	 * @param tType	设置类型为"post"或"get"(无视大小写)
	 * @param tUrl	请求的网络地址
	 * @return 返回0表示成功,1表示URL不正确,2表示网络错误
	 */
	public int InitType(String tType,String tUrl)
	{
		if (tType.equalsIgnoreCase("get"))
			nMode = 0;
		else
			nMode =1;
		try {
			rbSend = new Request.Builder().url(tUrl);
		}
		catch (IllegalArgumentException ignored)
		{
			return 1;
		}
		return 0;
	}

	/**
	 * 设置表头
	 * @param tKey  表头键名
	 * @param tValue 表头值
	 */
	public void SetHead(String tKey,String tValue)
	{
		if (rbSend != null) {
            rbSend.header(tKey, tValue);
            if (tKey.equalsIgnoreCase("Content-Type"))
            {
                tContentType = tValue;
            }
        }
	}

	/**
	 * 设置Cookie
	 * @param tCookie cookie对象
	 */
    public void SetCookie(String tCookie)
    {
    	if (rbSend != null)
    		rbSend.header("Cookie", tCookie);
    }

	/**
	 * 设置Post参数
	 * @param tKey 设置Post参数名
	 * @param tValue 字符串类型参数
	 */
	public void SetParameterUrl(String tKey,String tValue) {
		nPostParam = 1;
        febForm.add(tKey, tValue);
	}

	/**
	 * 设置Post参数
	 * @param tValue json字符串
	 */
	public void SetParameterJson(String tValue) {
		nPostParam = 2;
		MediaType tText = MediaType.parse(tContentType == null ? "application/json; charset=utf-8" : tContentType);
		rbBody = RequestBody.create(tText, tValue);
	}

	/**
	 * 设置Post参数
	 * @param tValue 字符串类型参数
	 */
	public void SetParameterSteam(String tValue) {
		nPostParam = 0;
		MediaType tText = MediaType.parse(tContentType == null ? "text/html; charset=utf-8" : tContentType);
		rbBody = RequestBody.create(tText, tValue);
	}

	/**
	 * 设置Post参数
	 * @param byValue 16进制类型参数
	 */
	public void SetParameterSteam(byte[] byValue) {
		nPostParam = 0;
		MediaType tText = MediaType.parse(tContentType == null ? "text/html; charset=utf-8" : tContentType);
		rbBody = RequestBody.create(tText, byValue);
	}

	/**
	 * 设置内容下载路径
	 * @param tPath 下载地址
	 */
    public void SetDownPath(String tPath)
    {
        tDownPath = tPath;
    }

	/**
	 * 发送Http协议
	 * @param m_socketTmp http回调
	 */
	public void SendAsync(JKSocketLinstener m_socketTmp)
	{
		m_socket = m_socketTmp;
		if (1 == nMode)
		{
			if (nPostParam == 0)
				rbSend.post(rbBody);
			else if (nPostParam == 1)
				rbSend.post(febForm.build());
			else if (nPostParam == 2)
				rbSend.post(rbBody);
		}

		rbSend.tag(rbSend).build();
		okhcClient.build().newCall(rbSend.build()).enqueue(new Callback() {

			@Override
			public void onFailure(Call call, IOException arg1) {
				Message meMessage = new Message();
				meMessage.what = RECEIVE_FAILD;
				if (arg1 instanceof UnknownHostException)
				{
					meMessage.arg1 = -1;
				}
				else if (arg1 instanceof SocketTimeoutException)
				{
					meMessage.arg1 = -3;
				}
				else if (arg1 instanceof SocketException && arg1.getMessage().equals("Socket closed")){
					meMessage.arg1 = -4;
				}
				else
					meMessage.arg1 = -2;
				Object[] a_oObject = new Object[1];
				a_oObject[0] = m_socket;
				meMessage.obj = a_oObject;
				Handler.sendMessage(meMessage);
			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				rRequest = response;
				if (rRequest.code() == 200) {       //200ok
					byte[] a_byList = rRequest.body().bytes();
					if (!tDownPath.equals("")) {
						JKFile.WriteFile(tDownPath, a_byList);
					}
					String tBack = JKConvert.toString(a_byList);

					Message meMessage = new Message();
					meMessage.what = RECEIVE_OK;
					Object[] a_oObject = new Object[4];
					a_oObject[0] = a_byList;
					a_oObject[1] = tBack;
					a_oObject[2] = m_socket;
					a_oObject[3] = rRequest;
					meMessage.obj = a_oObject;
					Handler.sendMessage(meMessage);
				} else {
					Message meMessage = new Message();
					meMessage.what = RECEIVE_FAILD;
					meMessage.arg1 = GetCode();
					Object[] a_oObject = new Object[1];
					a_oObject[0] = m_socket;
					meMessage.obj = a_oObject;
					Handler.sendMessage(meMessage);
				}
			}
		});
	}

	/**
	 * 发送Http协议
	 * @return Http返回结果
	 */
	public JKHttpSocketResult Send()
	{
		if (1 == nMode)
		{
			if (nPostParam == 0)
				rbSend.post(rbBody);
			else if (nPostParam == 1)
				rbSend.post(febForm.build());
		}

		try {
			rRequest = okhcClient.build().newCall(rbSend.build()).execute();

			if (GetCode() == 200) {
				byte[] a_byList = rRequest.body().bytes();
				if (!tDownPath.equals(""))
                {
                    JKFile.WriteFile(tDownPath, a_byList);
                }
				String tBack = JKConvert.toString(a_byList);

				JKHttpSocketResult jhrResult = new JKHttpSocketResult(0);
                HashMap<String,String> h_tHead = new HashMap<>();
                Headers hdHead = rRequest.headers();
                Map<String, List<String>> h_hHead =  hdHead.toMultimap();
                for(Map.Entry<String, List<String>> me:h_hHead.entrySet()) {
                    List<String> a_tValue = me.getValue();
                    for (int i=0; i<a_tValue.size(); ++i)
                    {
                        h_tHead.put(me.getKey(),a_tValue.get(i));
                    }
                }
				jhrResult.SetResult(h_tHead, tBack,a_byList);
				return jhrResult;
			} else {
				return new JKHttpSocketResult(GetCode());
			}
		} catch (IOException e) {
            e.printStackTrace();
            if (e instanceof UnknownHostException)
            {
                return new JKHttpSocketResult(-1);
            }
            else if (e instanceof SocketTimeoutException)
            {
                return new JKHttpSocketResult(-3);
            }
            else if (e instanceof SocketException && e.getMessage().equals("Socket closed")){
                return new JKHttpSocketResult(-4);
            }
            else
            {
                JKLog.ErrorLog("发送http协议失败.原因为" + e.getMessage());
                return new JKHttpSocketResult(-1);
            }
        }
	}

	/**
	 * 停止发送http
	 */
	public void StopSend()
	{
		for (Call call : okhcClient.build().dispatcher().queuedCalls()) {
			if (rbSend.equals(call.request().tag()))
				call.cancel();
		}
	}

    /**
     * 获取返回码
     * @return 返回http失败码
     */
    private int GetCode()
    {
        return rRequest.code();
    }
}