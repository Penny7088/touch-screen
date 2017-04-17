package com.jkframework.algorithm;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;

import com.jkframework.config.JKSystem;
import com.jkframework.debug.JKDebug;
import com.jkframework.debug.JKLog;
import com.jkframework.thread.JKThread;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;



public class JKPicture {
	
    static class MyHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            Object[] a_oList = (Object[]) msg.obj;
			JKPictureListener m_Listener = (JKPictureListener) a_oList[0];
			if (m_Listener != null)
				m_Listener.FinishSet((Bitmap) a_oList[1]);
        }
    }

    /**
     * 同步获取图片
     * @param tPath 图片路径地址
     * @param dioOptions 图片加载配置
     * @return 图片Bitmap
     */
    public static Bitmap LoadBitmap(String tPath, DisplayImageOptions dioOptions)
    {
        ImageLoader ilBitmap = ImageLoader.getInstance();
        return ilBitmap.loadImageSync("file://" + (tPath.indexOf("/") == 0 ? tPath.substring(1) : tPath), dioOptions);
    }


    /**
	 * 同步获取图片
	 * @param tPath 图片路径地址
	 * @return 图片Bitmap 
	 */
	public static Bitmap LoadBitmap(String tPath)
	{  		
		ImageLoader ilBitmap = ImageLoader.getInstance(); 

	    DisplayImageOptions dioOptions =  new DisplayImageOptions.Builder()
			.cacheInMemory(true)
			.cacheOnDisk(true)
			.considerExifParams(true)
			.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
			.build();
		
	    return ilBitmap.loadImageSync("file://" + (tPath.indexOf("/") == 0 ? tPath.substring(1) : tPath), dioOptions);
	}

	/**
	 * 同步获取图片
	 * @param tPath 图片路径地址
	 * @param bCache 是否进行缓存
	 * @return 图片Bitmap
	 */
	public static Bitmap LoadBitmap(String tPath,boolean bCache)
	{
		ImageLoader ilBitmap = ImageLoader.getInstance();

		DisplayImageOptions dioOptions =  new DisplayImageOptions.Builder()
				.cacheInMemory(bCache)
				.cacheOnDisk(bCache)
				.considerExifParams(true)
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
				.build();

		return ilBitmap.loadImageSync("file://" + (tPath.indexOf("/") == 0 ? tPath.substring(1) : tPath), dioOptions);
	}

	/**
	 * 同步获取图片
	 * @param a_byData 图片字节
	 * @return 图片Bitmap 
	 */
	@SuppressWarnings("deprecation")
	public static Bitmap LoadBitmap(byte[] a_byData)
	{  	
		BitmapFactory.Options opt = new BitmapFactory.Options();  		
		if (VERSION.SDK_INT < 21)
        {
	        opt.inPurgeable = true;  
	        opt.inInputShareable = true;  
        }
        try {
			BitmapFactory.Options.class.getField("inNativeAlloc").setBoolean(opt,true);
		} catch (Exception ignored) {
		}        
	    return BitmapFactory.decodeByteArray(a_byData, 0, a_byData.length, opt);
	}	
	
	/**
	 * 异步获取图片
	 * @param m_Listener 回调响应
	 * @param tPath 图片路径地址
	 */
	public static void LoadBitmapAsync(final JKPictureListener m_Listener,final String tPath)
	{
        final MyHandler Handler = new MyHandler();
        new JKThread().Start(new JKThread.JKThreadListener() {
            @Override
            public void OnThread() {
                Message meMessage = new Message();
				meMessage.obj = new Object[]{m_Listener,LoadBitmap(tPath)};
                Handler.sendMessage(meMessage);
            }

            @Override
            public void OnMain() {

            }
        });
	}
	
	/**
	 * 异步获取图片
	 * @param m_Listener 回调响应
	 * @param a_byList 图片字节数组
	 */
	public static void LoadBitmapAsync(final JKPictureListener m_Listener,final byte[] a_byList)
	{
        final MyHandler Handler = new MyHandler();
        new JKThread().Start(new JKThread.JKThreadListener() {
            @Override
            public void OnThread() {
                Message meMessage = new Message();
                meMessage.obj = new Object[]{m_Listener,LoadBitmap(a_byList)};
                Handler.sendMessage(meMessage);
            }

            @Override
            public void OnMain() {

            }
        });
	}
	
	
	/**
	 * 优化方式同步获取图片
	 * @param a_byData 图片字节
	 * @param fWidth X轴缩放参数
	 * @param fHeight Y轴缩放参数
	 * @param bType true为百分比缩放,false为实际像素缩放
	 * @return 优化后的Bitmap
	 */
	@SuppressWarnings("deprecation")
	public static Bitmap LoadBitmap(byte[] a_byData,float fWidth,float fHeight,boolean bType)
	{  		
		if (fWidth <= 0 || fHeight <= 0)
		{
			return LoadBitmap(a_byData);
		}
		BitmapFactory.Options opt = new BitmapFactory.Options();  		 
		if (VERSION.SDK_INT < 21)
        {
	        opt.inPurgeable = true;  
	        opt.inInputShareable = true;  
        }
        try {
			BitmapFactory.Options.class.getField("inNativeAlloc").setBoolean(opt,true);
		} catch (Exception ignored) {
		}                 
        opt.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(a_byData, 0, a_byData.length, opt);
        if (fWidth > 0 && fHeight > 0)
        {       
        	float fWidthScale;
        	float fHeightScale;
        	if (bType)
        	{
        		fWidthScale = 1f / fWidth;
        		fHeightScale = 1f /fHeight;
        	}
        	else {
        		fWidthScale = (float)opt.outWidth / fWidth;
        		fHeightScale = (float)opt.outHeight / fHeight;
        	}
	        
	        float fScale;
	        if (fWidthScale > fHeightScale)
	        	fScale = fHeightScale;
	        else
	        	fScale = fWidthScale;
	        if (fScale < 1)
	        	fScale = 1;
	        opt.inSampleSize = (int) fScale;
        }        
        
        opt.inJustDecodeBounds = false;  	  
	    return Scale(BitmapFactory.decodeByteArray(a_byData, 0, a_byData.length, opt),fWidth,fHeight,bType);
	}
	
	/**
	 * 优化方式同步获取图片
	 * @param tPath 图片路径地址
	 * @param fWidth X轴缩放参数
	 * @param fHeight Y轴缩放参数
	 * @param bType true为百分比缩放,false为实际像素缩放
	 * @return 优化后的Bitmap
	 */
	@SuppressWarnings("deprecation")
	public static Bitmap LoadBitmap(String tPath,float fWidth,float fHeight,boolean bType)
	{  		
		if (fWidth <= 0 || fHeight <= 0)
		{
			return LoadBitmap(tPath);
		}
		BitmapFactory.Options opt = new BitmapFactory.Options();  		 
		if (VERSION.SDK_INT < 21)
        {
	        opt.inPurgeable = true;  
	        opt.inInputShareable = true;  
        }
        try {
			BitmapFactory.Options.class.getField("inNativeAlloc").setBoolean(opt,true);
		} catch (Exception ignored) {
		}                 
        opt.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(tPath, opt);
        if (fWidth > 0 && fHeight > 0)
        {       
        	float fWidthScale;
        	float fHeightScale;
        	if (bType)
        	{
        		fWidthScale = 1f / fWidth;
        		fHeightScale = 1f /fHeight;
        	}
        	else {
        		fWidthScale = (float)opt.outWidth / fWidth;
        		fHeightScale = (float)opt.outHeight / fHeight;
        	}
	        
	        float fScale;
	        if (fWidthScale > fHeightScale)
	        	fScale = fHeightScale;
	        else
	        	fScale = fWidthScale;
	        if (fScale < 1)
	        	fScale = 1;
	        opt.inSampleSize = (int) fScale;
        }        
        
        opt.inJustDecodeBounds = false;  
	    InputStream is;
	    if (!JKFile.IsExists(tPath) || JKFile.IsDirectory(tPath))
	    	return null;	
		try {
			is = new FileInputStream(tPath);
		} catch (FileNotFoundException e) { 
			JKLog.ErrorLog("获取图片失败.原因为" + e.getMessage());
			e.printStackTrace();
			return null;
		}
	    return Scale(BitmapFactory.decodeStream(is, null, opt),fWidth,fHeight,bType);
	}
	
	/**
	 * 优化方式异步获取图片
	 * @param m_Listener 回调响应
	 * @param tPath 图片路径地址
	 * @param fWidth X轴缩放参数
	 * @param fHeight Y轴缩放参数
	 * @param bType true为百分比缩放,false为实际像素缩放
	 */
	public static void LoadBitmapAsync(final JKPictureListener m_Listener,final String tPath,final float fWidth,final float fHeight,final boolean bType)
	{
        final MyHandler Handler = new MyHandler();
        new JKThread().Start(new JKThread.JKThreadListener() {
            @Override
            public void OnThread() {
                Message meMessage = new Message();
                if (fWidth <= 0 || fHeight <= 0)
                    meMessage.obj = new Object[]{m_Listener, LoadBitmap(tPath)};
                else
                    meMessage.obj = new Object[]{m_Listener, LoadBitmap(tPath, fWidth, fHeight, bType)};
                Handler.sendMessage(meMessage);
            }

            @Override
            public void OnMain() {

            }
        });
	}
	
	/**
	 * 优化方式异步获取图片
	 * @param m_Listener 回调响应
	 * @param a_byList 图片字节数组
	 * @param fWidth X轴缩放参数
	 * @param fHeight Y轴缩放参数
	 * @param bType true为百分比缩放,false为实际像素缩放
	 */
	public static void LoadBitmapAsync(final JKPictureListener m_Listener,final byte[] a_byList,final float fWidth,final float fHeight,final boolean bType)
	{
        final MyHandler Handler = new MyHandler();
        new JKThread().Start(new JKThread.JKThreadListener() {
            @Override
            public void OnThread() {
                Message meMessage = new Message();
                if (fWidth <= 0 || fHeight <= 0)
                    meMessage.obj = new Object[]{m_Listener, LoadBitmap(a_byList)};
                else
                    meMessage.obj = new Object[]{m_Listener, LoadBitmap(a_byList, fWidth, fHeight, bType)};
                Handler.sendMessage(meMessage);
            }

            @Override
            public void OnMain() {

            }
        });
	}

	/**
	 * 同步获取资源图片
	 * @param nResID 图片路径地址
	 * @return 图片Bitmap
	 */
	public static Bitmap LoadResourceBitmap(int nResID)
	{
		ImageLoader ilBitmap = ImageLoader.getInstance();

		DisplayImageOptions dioOptions =  new DisplayImageOptions.Builder()
				.cacheInMemory(true)
				.cacheOnDisk(true)
				.considerExifParams(true)
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
				.build();
		return ilBitmap.loadImageSync("drawable://" + nResID, dioOptions);
	}

	/**
	 * 同步获取Assets图片
	 * @param tPath 图片路径地址
     * @param dioOptions 加载配置
	 * @return 图片Bitmap
	 */
	public static Bitmap LoadAssetsBitmap(String tPath,DisplayImageOptions dioOptions)
	{
		ImageLoader ilBitmap = ImageLoader.getInstance();
		return ilBitmap.loadImageSync("assets://" + tPath, dioOptions);
	}

	/**
	 * 同步获取Assets图片
	 * @param tPath 图片路径地址
	 * @return 图片Bitmap
	 */
	public static Bitmap LoadAssetsBitmap(String tPath)
	{  		
		ImageLoader ilBitmap = ImageLoader.getInstance(); 

	    DisplayImageOptions dioOptions =  new DisplayImageOptions.Builder()
			.cacheInMemory(true)
			.cacheOnDisk(true)
			.considerExifParams(true)
			.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
			.build();
	    return ilBitmap.loadImageSync("assets://" + tPath, dioOptions);
	}

	/**
	 * 同步获取Assets图片
	 * @param tPath 图片路径地址
	 * @param bCache 是否使用缓存
	 * @return 图片Bitmap
	 */
	public static Bitmap LoadAssetsBitmap(String tPath,boolean bCache)
	{
		ImageLoader ilBitmap = ImageLoader.getInstance();

		DisplayImageOptions dioOptions =  new DisplayImageOptions.Builder()
				.cacheInMemory(bCache)
				.cacheOnDisk(bCache)
				.considerExifParams(true)
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
				.build();
		return ilBitmap.loadImageSync("assets://" + tPath, dioOptions);
	}

	/**
	 * 异步获取Assets图片
	 * @param m_Listener 回调响应
	 * @param tPath 图片路径地址
	 */
	public static void LoadAssetsBitmapAsync(final JKPictureListener m_Listener,final String tPath)
	{
        final MyHandler Handler = new MyHandler();
        new JKThread().Start(new JKThread.JKThreadListener() {
            @Override
            public void OnThread() {
                Message meMessage = new Message();
                meMessage.obj = new Object[]{m_Listener, LoadAssetsBitmap(tPath)};
                Handler.sendMessage(meMessage);
            }

            @Override
            public void OnMain() {

            }
        });
	}
	
	
	/**
	 * 优化方式同步获取Assets图片
	 * @param fWidth X轴缩放参数
	 * @param fHeight Y轴缩放参数
	 * @param bType true为百分比缩放,false为实际像素缩放
	 * @return 优化后的Bitmap
	 */
	@SuppressWarnings("deprecation")
	public static Bitmap LoadAssetsBitmap(String tPath,float fWidth,float fHeight,boolean bType)
	{  		
		BitmapFactory.Options opt = new BitmapFactory.Options();  		 
		if (VERSION.SDK_INT < 21)
        {
	        opt.inPurgeable = true;  
	        opt.inInputShareable = true;  
        }
		try {
			BitmapFactory.Options.class.getField("inNativeAlloc").setBoolean(opt,true);
		} catch (Exception ignored) {
		}                 
		opt.inJustDecodeBounds = true;
				
		InputStream is;
		try {
			is = JKDebug.hContext.getResources().getAssets().open(tPath);
		} catch (IOException e) { 
			JKLog.ErrorLog("获取Assets里的图片失败.原因为" + e.getMessage());
			e.printStackTrace();
			return null;
		}
		BitmapFactory.decodeStream(is, null, opt);
		if (fWidth > 0 && fHeight > 0)
        {       
        	float fWidthScale;
        	float fHeightScale;
        	if (bType)
        	{
        		fWidthScale = 1f / fWidth;
        		fHeightScale = 1f /fHeight;
        	}
        	else {
        		fWidthScale = (float)opt.outWidth / fWidth;
        		fHeightScale = (float)opt.outHeight / fHeight;
        	}
	        
	        float fScale;
	        if (fWidthScale > fHeightScale)
	        	fScale = fHeightScale;
	        else
	        	fScale = fWidthScale;
	        if (fScale < 1)
	        	fScale = 1;
	        opt.inSampleSize = (int) fScale;
        }        
		opt.inJustDecodeBounds = false;
        try {
			is = JKDebug.hContext.getResources().getAssets().open(tPath);
		} catch (IOException e) { 
			JKLog.ErrorLog("获取Assets里的图片失败.原因为" + e.getMessage());
			e.printStackTrace();
			return null;
		}
		return Scale(BitmapFactory.decodeStream(is, null, opt), fWidth, fHeight, bType);
	}
	
	/**
	 * 优化方式异步获取Assets图片
	 * @param m_Listener 回调响应
	 * @param tPath 图片路径地址
	 * @param fWidth X轴缩放参数
	 * @param fHeight Y轴缩放参数
	 * @param bType true为百分比缩放,false为实际像素缩放
	 */
	public static void LoadAssetsBitmapAsync(final JKPictureListener m_Listener,final String tPath,final float fWidth,final float fHeight,final boolean bType)
	{
        final MyHandler Handler = new MyHandler();
        new JKThread().Start(new JKThread.JKThreadListener() {
            @Override
            public void OnThread() {
                Message meMessage = new Message();
                meMessage.obj = new Object[]{m_Listener,LoadAssetsBitmap(tPath,fWidth,fHeight,bType)};
                Handler.sendMessage(meMessage);
            }

            @Override
            public void OnMain() {

            }
        });
	}
	
	/**
	 * 加载资源图片
	 * @param nResouceID 资源ID
	 * @return bitmap格式图片
	 */
	public static Bitmap LoadBitmap(int nResouceID)
	{  		
		return BitmapFactory.decodeResource(JKDebug.hContext.getResources(), nResouceID);
	}
	
	/**
	 * 缩放图片
	 * @param bmBitmap 需要缩放的图片
	 * @param fScaleX 缩放的X轴参数
	 * @param fScaleY 缩放的Y轴参数
	 * @param bType true为百分比缩放,false为实际像素缩放
	 * @return 返回缩放后的图片
	 */
	public static Bitmap Scale(Bitmap bmBitmap, float fScaleX, float fScaleY, boolean bType)
    {
		if (bmBitmap == null)
			return null;
		Matrix matrix = new Matrix();
		if (bType)
			matrix.postScale(fScaleX, fScaleY);
		else 
		{
			matrix.postScale(fScaleX / bmBitmap.getWidth(), fScaleY / bmBitmap.getHeight());
		}
		return Bitmap.createBitmap(bmBitmap, 0, 0, bmBitmap.getWidth(), bmBitmap.getHeight(), matrix, true);
    }
	
	/**
	 * 裁剪图片
	 * @param bmBitmap 需要裁剪的图片
	 * @param rtSize 裁剪大小(4个参数分别为x,y,width,height)
	 * @return 返回裁剪后的图片
	 */
	public static Bitmap Cut(Bitmap bmBitmap, Rect rtSize)
    {
		if (bmBitmap == null)
			return null;
		return Bitmap.createBitmap(bmBitmap, rtSize.left, rtSize.top, rtSize.right, rtSize.bottom);
    }
	
	/**
	 * 获取布局屏幕截图
	 * @param vView 要获取的布局
	 * @return 返回Bitmap
	 */
	public static Bitmap GetScreenBitmap(View vView)
	{
		vView.setDrawingCacheEnabled(false);
		vView.setDrawingCacheEnabled(true);  
		return vView.getDrawingCache();
	}
	
	/**
	 * 获取当前屏幕截图
	 * @param rtSize 当前屏幕指定区域(null为全屏,4个参数分别为x,y,width,height)
	 * @return 当前执行的截图
	 */
	public static Bitmap GetScreenBitmap(Activity MainActivity,Rect rtSize)
	{
		View vView = MainActivity.getWindow().getDecorView(); 
		vView.setDrawingCacheEnabled(false);
		vView.setDrawingCacheEnabled(true);
		vView.layout(0, 0, vView.getMeasuredWidth(), vView.getMeasuredHeight());
		Bitmap bitmap = vView.getDrawingCache();
		rtSize.top += JKSystem.GetCurrentStatusBarHeight();
        bitmap = Cut(bitmap,rtSize);
		vView.buildDrawingCache();            
        return bitmap;	
	}
	
	/**
	 * 设置图片圆角
	 * @param bitmap 图片文件
	 * @param round  圆角系数
	 * @return 处理圆角后的图片
	 */
	public static Bitmap SetRoundBitmap(Bitmap bitmap, int round) {
		Bitmap outBitmap = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(outBitmap);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, (float) round, (float) round, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return outBitmap;
	}
	
	/**
	 * 图片合成
	 * @param src  原始图片
	 * @param watermark 合成的水印图片
	 * @param nDirection(0为左上,1为右上,2为左下,3为右下)
	 * @return 合成后的图片
	 */
	public static Bitmap Compose(Bitmap src, Bitmap watermark,int nDirection) {
        if (src == null) {
            return null;
        }
        int w = src.getWidth();
        int h = src.getHeight();
        // create the new blank bitmap
        Bitmap newb = Bitmap.createBitmap(w, h, Config.ARGB_8888);// 创建一个新的和SRC长度宽度一样的位图
        Canvas cv = new Canvas(newb);
        // draw src into
        cv.drawBitmap(src, 0, 0, null);
        // draw watermark into
        switch (nDirection) {
		case 0:
	        cv.drawBitmap(watermark, 0, 0, null);
			break;
		case 1:
			cv.drawBitmap(watermark, w - watermark.getWidth(), 0, null);
			break;
		case 2:
			cv.drawBitmap(watermark, 0, h - watermark.getHeight(), null);
			break;
		case 3:
			cv.drawBitmap(watermark, w - watermark.getWidth(), h - watermark.getHeight(), null);
			break;
		}
        // save all clip
        cv.save(Canvas.ALL_SAVE_FLAG);
        // store
        cv.restore();// 存储
        return newb;
    }

	/**
	 * 图片灰化
	 * @param bmpOriginal  原始图片
	 * @return 灰化后的图片
	 */
	public static Bitmap ToGray(Bitmap bmpOriginal) {
		int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();
        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height,Config.ARGB_8888);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0, 0, paint);        
        return bmpGrayscale;
	}

	/**
	 * 图片增加倒影
	 * @param mBitmap 原始图片
	 * @return 倒影图片
	 */
	public static Bitmap ReverseShadow(Bitmap mBitmap) {
       
        int width = mBitmap.getWidth();
        int height = mBitmap.getHeight();
         
        Matrix matrix = new Matrix();
        // 图片缩放，x轴变为原来的1倍，y轴为-1倍,实现图片的反转
        matrix.preScale(1, -1);
         
        //创建反转后的图片Bitmap对象，图片高是原图的一半。
        //Bitmap mInverseBitmap = Bitmap.createBitmap(mBitmap, 0, height/2, width, height/2, matrix, false);
        //创建标准的Bitmap对象，宽和原图一致，高是原图的1.5倍。
        //注意两种createBitmap的不同
        //Bitmap mReflectedBitmap = Bitmap.createBitmap(width, height*3/2, Config.ARGB_8888);
         
        Bitmap mInverseBitmap = Bitmap.createBitmap(mBitmap, 0, 0, width, height, matrix, false);
        Bitmap mReflectedBitmap = Bitmap.createBitmap(width, height*2, Config.ARGB_8888);
         
        // 把新建的位图作为画板
        Canvas mCanvas = new Canvas(mReflectedBitmap);
        //绘制图片
        mCanvas.drawBitmap(mBitmap, 0, 0, null);
        mCanvas.drawBitmap(mInverseBitmap, 0, height, null);
         
        //添加倒影的渐变效果
        Paint mPaint = new Paint();
        Shader mShader = new LinearGradient(0, height, 0, mReflectedBitmap.getHeight(), 0x70ffffff, 0x00ffffff, TileMode.MIRROR);
        mPaint.setShader(mShader);
        //设置叠加模式
        mPaint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
        //绘制遮罩效果
        mCanvas.drawRect(0, height, width, mReflectedBitmap.getHeight(), mPaint);
         
        return mReflectedBitmap;
	}
	
	/**
	 * 不加载图片,仅获得图片宽高
	 * @param tPath 图片SD卡路径
	 * @return 返回图片宽高,x为宽,y为高
	 */
	@SuppressWarnings("deprecation")
	public static Point GetBitmapSizeOnly(String tPath)
	{
		BitmapFactory.Options opt = new BitmapFactory.Options();  		 	
		if (VERSION.SDK_INT < 21)
        {
	        opt.inPurgeable = true;  
	        opt.inInputShareable = true;  
        }
        try {
			BitmapFactory.Options.class.getField("inNativeAlloc").setBoolean(opt,true);
		} catch (Exception ignored) {
		}                 
        opt.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(tPath, opt);
        
        Point ptSize = new Point();
        ptSize.x = opt.outWidth;
        ptSize.y = opt.outHeight;
        return ptSize;
	}

	/**
	 * 将图片地址添加到相机相册里去
	 * @param tPath 图片路径
     * @return 返回在相册里的路径地址
	 */
	public static String AddToAlbum(String tPath)
	{
        try {
			if (tPath == null || tPath.length() == 0)
				return tPath;
            Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(JKDebug.hContext.getContentResolver(),
                    tPath, JKFile.GetBaseName(tPath), null));
			return JKConvert.UriToPath(uri);
        } catch (Exception e) {
            return tPath;
        }
	}
	
	/**
	 * 获取bitmap字节大小
	 * @param btMap 图像bitmap
	 * @return 字节大小
	 */
	@TargetApi(19)
	public static long GetBitmapSize(Bitmap btMap)
	{
		if (VERSION.SDK_INT >= 19){    //API 19
	        return btMap.getAllocationByteCount();
	    }
		return btMap.getByteCount();
	}
	
	public interface JKPictureListener {
		
		/**
		 * 图片设置完成后的回调函数 
		 * @param btMap 图片加载完毕后的BitMap
		 */
		void FinishSet(Bitmap btMap);
	}
}