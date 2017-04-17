package com.jkframework.manager;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;

import com.jkframework.algorithm.JKFile;
import com.jkframework.callback.JKDownloadManagerLinstener;
import com.jkframework.debug.JKDebug;

import java.io.File;
import java.util.ArrayList;


public class JKDownloadManager
{

    /**下载id数组*/
    public static ArrayList<Long> a_lIDList = new ArrayList<>();
    /**下载监听数组*/
    public static ArrayList<JKDownloadManagerLinstener> a_jkdmlLinstener= new ArrayList<>();
    /**是否开启了接收*/
    private static boolean bReceive = false;

    /**
     * android系统下载
     * @param tPath 下载文件路径
     * @param tUrl 下载路径
     * @param jkdmlLinstener 下载完成回调监听
     */
	static public long Download(String tPath,String tUrl,JKDownloadManagerLinstener jkdmlLinstener) {
        OpenReceive();
        JKFile.CreateDir(tPath);
        try {
            DownloadManager downloadManager = (DownloadManager) JKDebug.hContext.getSystemService(Context.DOWNLOAD_SERVICE);
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(tUrl));
            request.setVisibleInDownloadsUi(true);
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setDestinationUri(Uri.fromFile(new File(tPath)));//)tPath.substring(0, tPath.lastIndexOf("/")), JKFile.GetFileName(tPath));
            final long lID = downloadManager.enqueue(request);
            a_lIDList.add(lID);
            a_jkdmlLinstener.add(jkdmlLinstener);
        }
        catch (IllegalArgumentException e)
        {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * android系统下载
     * @param tTitle 正在下载+tTtile显示在通知栏标题上
     * @param tPath 下载文件路径
     * @param tUrl 下载路径
     * @param jkdmlLinstener 下载完成回调监听
     * @return 返回的下载id
     */
    static public long Download(String tTitle,String tPath,String tUrl, final JKDownloadManagerLinstener jkdmlLinstener)
    {
        OpenReceive();
        JKFile.CreateDir(tPath);
        try {
            DownloadManager downloadManager = (DownloadManager) JKDebug.hContext.getSystemService(Context.DOWNLOAD_SERVICE);
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(tUrl));
            request.setTitle(tTitle);
            request.setVisibleInDownloadsUi(true);
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setDestinationUri(Uri.fromFile(new File(tPath)));//)tPath.substring(0, tPath.lastIndexOf("/")), JKFile.GetFileName(tPath));
            final long lID = downloadManager.enqueue(request);
            a_lIDList.add(lID);
            a_jkdmlLinstener.add(jkdmlLinstener);
            return lID;
        }
        catch (IllegalArgumentException e)
        {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 停止下载
     * @param lID 停止下载ID
     */
    static public void StopDownload(long lID)
    {
        DownloadManager downloadManager = (DownloadManager) JKDebug.hContext.getSystemService(Context.DOWNLOAD_SERVICE);
        downloadManager.remove(lID);
    }

    /**
     * 打开广播监听
     */
    private static  void OpenReceive()
    {
        if (!bReceive)
        {
            final IntentFilter filter =  new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
            JKDebug.hContext.registerReceiver(receiver, filter);
            bReceive = true;
        }
    }

    static BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            long reference = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            for (int i=0; i<a_lIDList.size(); ++i)
            {
                if (reference == a_lIDList.get(i))
                {
                    if (a_jkdmlLinstener.get(i) != null)
                    {
                        a_jkdmlLinstener.get(i).FinishDownload();
                    }
                    a_jkdmlLinstener.remove(i);
                    a_lIDList.remove(i);
                    if (a_lIDList.size() == 0)
                    {
                        JKDebug.hContext.unregisterReceiver(receiver);
                        bReceive = false;
                    }
                    break;
                }
            }
        }
    };

}