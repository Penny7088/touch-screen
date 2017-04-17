package com.jkframework.debug;

import android.content.Context;
import android.content.Intent;

import com.jkframework.activity.JKBaseActivity;
import com.jkframework.algorithm.JKFile;
import com.jkframework.config.JKPreferences;
import com.jkframework.manager.JKActivityManager;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

import java.io.File;

public class JKDebug {

	/**Debug版本号(0为发布版)*/
	public static int nDebug = 1;

	/**全局上下文引用*/
	public static Context hContext = null;
	/**反射类*/
	public static String tReflect = "";
	
	/**运行状态标志符*/
	private static String tState = null;
	
	
	/**
	 * 检查是否已经被回收
	 * @param activity activity对象
	 * @return 已经回收返回false并自启动,否则返回true
	 */
	public static boolean CheckStatus(JKBaseActivity activity)
	{
		if (tState == null)
		{
			tState = "Reset";
			final Intent itIntent = activity.getPackageManager().getLaunchIntentForPackage(activity.getPackageName());
			if (itIntent != null)
			{
				itIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                activity.startActivity(itIntent);
                activity.finish();
                JKException.ExitProgram();
                return false;
            }
			else {
				tState = null;
				activity.finish();
				JKException.ExitProgram();
			}
		}		
		return true;
	}
	
	/**
	 * 检查是否已经被回收
	 * @return 已经回收返回false,否则返回true
	 */
	public static boolean OnlyCheckStatus()
	{
        return tState != null;
    }
	
	/**
	 * 起始初始化执行
	 * @param nDebugStatus 初始化debug状态
	 * @param context 全局上下文引用
	 * @param tReflect 崩溃反射类
	 */
	public static void Init(int nDebugStatus,Context context,String tReflect)
	{
		nDebug = nDebugStatus;
		//全局Context记录
		hContext = context;		
		//崩溃处理框初始化
		JKDebug.tReflect = tReflect;
		
		JKFile.CreateDir(JKFile.GetPublicCachePath() + "/JKCache/JKImage/");
		ImageLoaderConfiguration config = new ImageLoaderConfiguration  
			    .Builder(JKDebug.hContext)  	
			    .denyCacheImageMultipleSizesInMemory()  
			    .memoryCache(new WeakMemoryCache())
			    .memoryCacheSizePercentage(13)
			    .tasksProcessingOrder(QueueProcessingType.LIFO)
			    .defaultDisplayImageOptions(DisplayImageOptions.createSimple())  
			    .imageDownloader(new BaseImageDownloader(JKDebug.hContext, 10 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)超时时间
			    .diskCache(new UnlimitedDiskCache(new File(JKFile.GetPublicCachePath() + "/JKCache/JKImage")))
			    .diskCacheSize(300 * 1024 * 1024)
			    .diskCacheFileCount(3000)
			    .build();//开始构建  
		
		ImageLoader.getInstance().init(config);//全局初始化此配置

//		try
//		{
//			System.loadLibrary("JKFramework");
//            Init(nDebugStatus);
//		}
//        catch (UnsatisfiedLinkError ignored)
//        {
//
//        }
	}

	/**
	 * 激活回收状态
	 */
	public static void Activation()
	{
		tState = "Running";
		JKBaseActivity.Lock();
		if (!JKActivityManager.isNull())
		{
			JKActivityManager.Abandon();
		}
		JKActivityManager.Reset();
		JKBaseActivity.Unlock();
        JKPreferences.RemoveSharePersistent("JKFILE_CHOICEFILE");
	}

//    /**
//     * 初始化NDK
//     * @param nDebug 0为正式版,1为测试版
//     */
//    public static native void Init(int nDebug);
}
