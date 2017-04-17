package com.jkframework.net;


import android.os.Handler;
import android.os.Message;

import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.jkframework.algorithm.JKAnalysis;
import com.jkframework.algorithm.JKConvert;
import com.jkframework.algorithm.JKFile;
import com.jkframework.callback.JKDownloadLinstener;
import com.jkframework.debug.JKDebug;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class JKHttpDownload
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
                .connectTimeout(10,TimeUnit.SECONDS)
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
	/**Http回调监听*/
	private JKDownloadLinstener m_socket;
    /**handler对象*/
    final MyHandler Handler = new MyHandler();

    /**请求进度消息*/
    final static private int RECEIVE_PROGRESS = 0;
    /**请求状态消息*/
    final static private int RECEIVE_STATUS = 1;

	static class MyHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what)
			{
                case RECEIVE_PROGRESS:
                {
                    Object[] a_oObjects = (Object[]) msg.obj;
                    JKDownloadLinstener m_Tmp = (JKDownloadLinstener) a_oObjects[0];
                    if (m_Tmp != null)
                        m_Tmp.ReceiveProgress(msg.arg1, msg.arg2);
                    break;
                }
                case RECEIVE_STATUS:
                {
                    int nBack = msg.arg1;
                    Object[] a_oObjects = (Object[]) msg.obj;
                    JKDownloadLinstener m_Tmp = (JKDownloadLinstener) a_oObjects[0];
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
	 * 发送Http协议
	 * @param m_socketTmp http回调
	 */
	public void DownLoad(JKDownloadLinstener m_socketTmp,final String tDownloadPath,final boolean bGoOn)
	{
		m_socket = m_socketTmp;
        JKFile.CreateDir(tDownloadPath);
        final int[] nCurrentSize = {0};
		if (bGoOn)
		{
			long lSize = JKFile.GetFileSize(tDownloadPath);
			rbSend.header("Range", "bytes=" + lSize + "-");
		}
		else
			JKFile.DeleteFile(tDownloadPath);

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
                if (rRequest.code() == 200 || rRequest.code() == 206) {       //200ok
                    BufferedOutputStream bosOutput = new BufferedOutputStream(new FileOutputStream(tDownloadPath,true));
                    int nSize = (int) rRequest.body().contentLength();
                    InputStream isStream = rRequest.body().byteStream();
                    if (m_socket != null)
                    {
                        Message meMessage = new Message();
                        meMessage.what = RECEIVE_STATUS;
                        meMessage.arg1 = 0;
                        Object[] a_oObject = new Object[1];
                        a_oObject[0] = m_socket;
                        meMessage.obj = a_oObject;
                        Handler.sendMessage(meMessage);
                    }

                    if (bGoOn)
                    {
                        Headers hdHead = rRequest.headers();
                        Map<String, List<String>> h_Head =  hdHead.toMultimap();
                        List<String> a_tRangeList = h_Head.get("Content-Range");
                        if (a_tRangeList != null && a_tRangeList.size() > 0)
                        {
                            String tSize = JKAnalysis.GetMiddleString(a_tRangeList.get(0), " ", "-");
                            nCurrentSize[0] = JKConvert.toInt(tSize);
                            nSize = JKConvert.toInt(a_tRangeList.get(0).substring(a_tRangeList.get(0).indexOf("/") + 1));
                        }
                    }

                    byte[] buffer = new byte[1024 * 16];
                    int nOffset;
                    while (true) {
                        nOffset = isStream.read(buffer, 0, 1024 * 16);
                        if (nOffset == -1)
                            break;
                        bosOutput.write(buffer, 0, nOffset);

                        nCurrentSize[0] += nOffset;
                        if (m_socket != null)
                        {
                            Message meMessage = new Message();
                            meMessage.what = RECEIVE_PROGRESS;
                            meMessage.arg1 = nCurrentSize[0];
                            meMessage.arg2 = nSize;
                            Object[] a_oObject = new Object[1];
                            a_oObject[0] = m_socket;
                            meMessage.obj = a_oObject;
                            Handler.sendMessage(meMessage);
                        }
                    }
                    //下载完毕，关闭输入流
                    bosOutput.close();
                    if (m_socket != null)
                    {
                        Message meMessage = new Message();
                        meMessage.what = RECEIVE_STATUS;
                        if (nSize == JKFile.GetFileSize(tDownloadPath)) //文件校验zn
                        {
                            meMessage.arg1 = 1;
                            Object[] a_oObject = new Object[1];
                            a_oObject[0] = m_socket;
                            meMessage.obj = a_oObject;
                            Handler.sendMessage(meMessage);
                        }
                        else
                        {
                            meMessage.arg1 = 2;
                            Object[] a_oObject = new Object[1];
                            a_oObject[0] = m_socket;
                            meMessage.obj = a_oObject;
                            Handler.sendMessage(meMessage);
                        }
                    }
                }
                else if (rRequest.code() == 416)
                {
                    Message meMessage = new Message();
                    meMessage.what = RECEIVE_STATUS;
                    meMessage.arg1 = 1;
                    Object[] a_oObject = new Object[1];
                    a_oObject[0] = m_socket;
                    meMessage.obj = a_oObject;
                    Handler.sendMessage(meMessage);
                }
                else {
                    Message meMessage = new Message();
                    meMessage.what = RECEIVE_STATUS;
                    meMessage.arg1 = -2;
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
}