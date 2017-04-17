package com.jkframework.net;


import android.os.Handler;
import android.os.Message;

import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.jkframework.algorithm.JKConvert;
import com.jkframework.algorithm.JKFile;
import com.jkframework.callback.JKUploadLinstener;
import com.jkframework.debug.JKDebug;

import java.io.File;
import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class JKHttpUpload
{
	/** Http请求类 */
    private static final OkHttpClient.Builder okhcClient = new OkHttpClient().newBuilder();
	static{
        ClearableCookieJar cookieJar =
                new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(JKDebug.hContext));
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        File httpCacheDirectory = new File(JKFile.GetPublicCachePath() + "/JKCache/JKHttpSocket");

        okhcClient
                .connectTimeout(30,TimeUnit.SECONDS)
                .writeTimeout(30,TimeUnit.SECONDS)
                .readTimeout(30,TimeUnit.SECONDS)
                .addInterceptor(logging)
                .cache(new Cache(httpCacheDirectory,100 * 1024 * 1024))
                .cookieJar(cookieJar);
	}
	/** Http发送构造对象 */
	private Request.Builder rbSend;
	/** Http接受对象 */
	private Response rRequest;
	/** 表单构造对象 */
	private MultipartBody.Builder mbForm = new MultipartBody.Builder().setType(MultipartBody.FORM);
	/**Http回调监听*/
	private JKUploadLinstener m_socket;
    /**handler对象*/
    final MyHandler Handler = new MyHandler();
    /** 上传总大小 */
    private long lTotalSize = 0;


	/**请求成功消息*/
	final static private int RECEIVE_OK = 0;
    /**请求进度消息*/
    final static private int RECEIVE_PROGRESS = 1;
    /**请求状态消息*/
    final static private int RECEIVE_STATUS = 2;


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
                    JKUploadLinstener m_Tmp = (JKUploadLinstener) a_oObjects[2];
                    if (m_Tmp != null)
                        m_Tmp.ReceiveOK(h_tHead,(String) a_oObjects[1], (byte[]) a_oObjects[0]);
					break;
				}
                case RECEIVE_PROGRESS:
                {
                    Object[] a_oObjects = (Object[]) msg.obj;
                    JKUploadLinstener m_Tmp = (JKUploadLinstener) a_oObjects[0];
                    if (m_Tmp != null)
                        m_Tmp.ReceiveProgress(msg.arg1, msg.arg2);
                    break;
                }
                case RECEIVE_STATUS:
                {
                    int nBack = msg.arg1;
                    Object[] a_oObjects = (Object[]) msg.obj;
                    JKUploadLinstener m_Tmp = (JKUploadLinstener) a_oObjects[0];
                    if (m_Tmp != null)
                        m_Tmp.ReceiveStatus(nBack);
                    break;
                }
			}
		}
	}

	/**
	 * 设置类型
	 * @param tUrl	请求的网络地址
	 * @return 返回0表示成功,1表示URL不正确,2表示网络错误
	 */
	public int InitType(String tUrl)
	{
		rbSend = new Request.Builder().url(tUrl);
		return 0;
	}

    /**
     * 设置超时时间
     * @param nTimeout 超时时间(毫秒数)
     */
    public void SetTimeOut(int nTimeout)
    {
        okhcClient.connectTimeout(nTimeout, TimeUnit.MILLISECONDS);
        okhcClient.writeTimeout(nTimeout, TimeUnit.MILLISECONDS);
        okhcClient.readTimeout(nTimeout, TimeUnit.MILLISECONDS);
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
	public void SetParameter(String tKey,String tValue) {
        mbForm.addFormDataPart(tKey,tValue);
	}

	/**
	 * 发送Http协议
	 * @param m_socketTmp http回调
	 * @param tUploadHeader 上传文件的参数的表头
	 * @param tUploadPath 上传文件的路径地址
	 */
	public void UpLoadAsync(JKUploadLinstener m_socketTmp,final String tUploadHeader, final String tUploadPath)
	{
		m_socket = m_socketTmp;
        mbForm.addFormDataPart(tUploadHeader,JKFile.GetFileName(tUploadPath) , RequestBody.create(MediaType.parse("application/octet-stream"),new File(tUploadPath)));
        RequestBody rbBody = mbForm.build();
        rbSend.post(new JKRequestBody(rbBody, new JKRequestBody.ProgressListener() {
            @Override
            public void transferred(long transferedBytes) {
                Message meMessage = new Message();
                meMessage.what = RECEIVE_PROGRESS;
                meMessage.arg1 = (int) transferedBytes;
                meMessage.arg2 = (int) lTotalSize;
                Object[] a_oObject = new Object[1];
                a_oObject[0] = m_socket;
                meMessage.obj = a_oObject;
                Handler.sendMessage(meMessage);
            }
        }));
        try {
            lTotalSize = rbBody.contentLength();
        } catch (IOException e) {
            e.printStackTrace();
        }


        rbSend.tag(rbSend).build();
		okhcClient.build().newCall(rbSend.build()).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException arg1) {
                Message meMessage = new Message();
                meMessage.what = RECEIVE_STATUS;
                if (arg1 instanceof UnknownHostException) {
                    meMessage.arg1 = -1;
                } else if (arg1 instanceof SocketTimeoutException) {
                    meMessage.arg1 = -3;
                } else if (arg1 instanceof SocketException && arg1.getMessage().equals("Socket closed")) {
                    meMessage.arg1 = -4;
                } else
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
                    meMessage.what = RECEIVE_STATUS;
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
     * 批量上传文件
     *
     * @param m_socketTmp
     *            上传监听
     * @param a_tUploadHeader
     *            上传文件的参数的表头数组
     * @param a_tUploadPath
     *            上传文件的路径地址数组
     */
    public void UpLoadAsync(JKUploadLinstener m_socketTmp,
                            final ArrayList<String> a_tUploadHeader, final ArrayList<String> a_tUploadPath) {
        m_socket = m_socketTmp;

        int nMin = Math.min(a_tUploadHeader.size(), a_tUploadPath.size());
        for (int i=0; i<nMin; ++i)
        {
            mbForm.addFormDataPart(a_tUploadHeader.get(i), JKFile.GetFileName(a_tUploadPath.get(i)), RequestBody.create(MediaType.parse("application/octet-stream"), new File(a_tUploadPath.get(i))));
        }
        RequestBody rbBody = mbForm.build();
        rbSend.post(new JKRequestBody(rbBody, new JKRequestBody.ProgressListener() {
            @Override
            public void transferred(long transferedBytes) {
                Message meMessage = new Message();
                meMessage.what = RECEIVE_PROGRESS;
                meMessage.arg1 = (int) transferedBytes;
                meMessage.arg2 = (int) lTotalSize;
                Object[] a_oObject = new Object[1];
                a_oObject[0] = m_socket;
                meMessage.obj = a_oObject;
                Handler.sendMessage(meMessage);
            }
        }));
        try {
            lTotalSize = rbBody.contentLength();
        } catch (IOException e) {
            e.printStackTrace();
        }


        rbSend.tag(rbSend).build();
        okhcClient.build().newCall(rbSend.build()).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException arg1) {
                Message meMessage = new Message();
                meMessage.what = RECEIVE_STATUS;
                if (arg1 instanceof UnknownHostException) {
                    meMessage.arg1 = -1;
                } else if (arg1 instanceof SocketTimeoutException) {
                    meMessage.arg1 = -3;
                } else if (arg1 instanceof SocketException && arg1.getMessage().equals("Socket closed")) {
                    meMessage.arg1 = -4;
                } else
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
                    meMessage.what = RECEIVE_STATUS;
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