package com.jkframework.control;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.LinearInterpolator;

import com.jkframework.algorithm.JKPicture;
import com.jkframework.callback.JKImageViewListener;
import com.jkframework.debug.JKDebug;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;



public class JKImageView extends AppCompatImageView {

	/**图片适配模块*/
	protected DisplayImageOptions.Builder dioOptions;
	/**监听成员*/
	private JKImageViewListener m_UpdateLisnter;
	/**加载图片模块*/
	private ImageLoader ilBitmap = ImageLoader.getInstance();
    /**渐显时间*/
    private int nFadeTime = 0;
	/**目标图片宽度*/
	private int nTargetWidth = 0;
	/**目标图片高度*/
	private int nTargetHeight = 0;
    /**图片是否开始加载*/
    private boolean bStart = false;


	public JKImageView(Context context) {
		super(context);
		InitBitmapConfig();
	}

	public JKImageView(Context context,AttributeSet attrs) {
		super(context,attrs);
		InitBitmapConfig();
	}

	public JKImageView(Context context,AttributeSet attrs,int defStyle) {
		super(context,attrs,defStyle);
		InitBitmapConfig();
	}

	/**
	 * 设置回调函数
	 * @param l	初始化回调变量
	 */
	public void SetUpdateListener(JKImageViewListener l)
	{
		m_UpdateLisnter = l;
	}

	public void SetImageResource(int resId){
		ilBitmap.cancelDisplayTask(this);
		super.setImageResource(resId);
        nTargetWidth = getWidth();
        nTargetHeight = getHeight();
        if (m_UpdateLisnter != null)
            m_UpdateLisnter.LoadingProgress(100);
		if (m_UpdateLisnter != null)
			m_UpdateLisnter.FinishLoad(true);
        if (nFadeTime > 0)
        {
            AlphaAnimation raAnimation = new AlphaAnimation(0,1);
            raAnimation.setDuration(nFadeTime);
            raAnimation.setInterpolator(new LinearInterpolator());
            JKImageView.this.startAnimation(raAnimation);
        }
	}

	public void SetImageURI(Uri uri){
		ilBitmap.cancelDisplayTask(this);
		super.setImageURI(uri);
        if (uri == null)
        {
            nTargetWidth = 0;
            nTargetHeight = 0;
        }
        else {
            nTargetWidth = getWidth();
            nTargetHeight = getHeight();
        }
        if (m_UpdateLisnter != null && uri != null)
            m_UpdateLisnter.LoadingProgress(100);
		if (m_UpdateLisnter != null)
			m_UpdateLisnter.FinishLoad(uri != null);
        if (nFadeTime > 0)
        {
            AlphaAnimation raAnimation = new AlphaAnimation(0,1);
            raAnimation.setDuration(nFadeTime);
            raAnimation.setInterpolator(new LinearInterpolator());
            JKImageView.this.startAnimation(raAnimation);
        }
	}

	/**
	 * 设置图片
	 * @param bmBitmap 图片bitmap
	 */
	public void SetBitmap(Bitmap bmBitmap){
		ilBitmap.cancelDisplayTask(this);
		super.setImageBitmap(bmBitmap);
        if (bmBitmap == null)
        {
            nTargetWidth = 0;
            nTargetHeight = 0;
        }
        else {
            nTargetWidth = bmBitmap.getWidth();
            nTargetHeight = bmBitmap.getHeight();
        }
        if (m_UpdateLisnter != null && bmBitmap != null)
            m_UpdateLisnter.LoadingProgress(100);
		if (m_UpdateLisnter != null)
			m_UpdateLisnter.FinishLoad(bmBitmap != null);
        if (nFadeTime > 0)
        {
            AlphaAnimation raAnimation = new AlphaAnimation(0,1);
            raAnimation.setDuration(nFadeTime);
            raAnimation.setInterpolator(new LinearInterpolator());
            JKImageView.this.startAnimation(raAnimation);
        }
	}

	/**
	 * 设置图片路径
	 * @param tPath 图片路径
	 */
	public void SetImagePath(String tPath)
	{
		ilBitmap.cancelDisplayTask(this);
		Bitmap btTmp = JKPicture.LoadBitmap(tPath,dioOptions.build());
        super.setImageBitmap(btTmp);
        if (btTmp == null)
        {
            nTargetWidth = 0;
            nTargetHeight = 0;
        }
        else {
            nTargetWidth = btTmp.getWidth();
            nTargetHeight = btTmp.getHeight();
        }
        if (m_UpdateLisnter != null && btTmp != null)
            m_UpdateLisnter.LoadingProgress(100);
        if (m_UpdateLisnter != null)
            m_UpdateLisnter.FinishLoad(btTmp != null);
        if (nFadeTime > 0)
        {
            AlphaAnimation raAnimation = new AlphaAnimation(0,1);
            raAnimation.setDuration(nFadeTime);
            raAnimation.setInterpolator(new LinearInterpolator());
            JKImageView.this.startAnimation(raAnimation);
        }
	}

	/**
	 * 设置图片路径
	 * @param tPath 图片路径
	 */
	public void SetAssetsImagePath(String tPath)
	{
		ilBitmap.cancelDisplayTask(this);
		Bitmap btTmp = JKPicture.LoadAssetsBitmap(tPath,dioOptions.build());
        super.setImageBitmap(btTmp);
        if (btTmp == null)
        {
            nTargetWidth = 0;
            nTargetHeight = 0;
        }
        else {
            nTargetWidth = btTmp.getWidth();
            nTargetHeight = btTmp.getHeight();
        }
        if (m_UpdateLisnter != null && btTmp != null)
            m_UpdateLisnter.LoadingProgress(100);
        if (m_UpdateLisnter != null)
            m_UpdateLisnter.FinishLoad(btTmp != null);
        if (nFadeTime > 0)
        {
            AlphaAnimation raAnimation = new AlphaAnimation(0,1);
            raAnimation.setDuration(nFadeTime);
            raAnimation.setInterpolator(new LinearInterpolator());
            JKImageView.this.startAnimation(raAnimation);
        }
	}

	/**
	 * 异步设置图片http
	 * @param tUrl  图片url地址
	 */
	public void SetImageHttp(String tUrl)
	{
        LoaderBitmapAsync(tUrl);
	}

	/**
	 * 异步设置SD卡图片
	 * @param tPath  图片路径地址
	 */
	public void SetImagePathAsync(String tPath)
	{
		final String tFullPath = "file://" + (tPath.indexOf("/") == 0 ? tPath.substring(1) : tPath);
        LoaderBitmapAsync(tFullPath);
	}

	/**
	 * 异步设置assets图片
	 * @param tPath  图片路径地址
	 */
	public void SetAssetsImagePathAsync(String tPath)
	{
		final String tFullPath = "assets://" + tPath;
        LoaderBitmapAsync(tFullPath);
	}

	/**
	 * 异步设置图片资源
	 * @param resId  图片资源ID
	 */
	public void SetImageResourceAsync(int resId){
		final String tFullPath = "drawable://" + resId;
	    LoaderBitmapAsync(tFullPath);
	}

	/**
	 * 智能识别设置图片
	 * @param tPath 支持SD卡图片,http图片,assets图片
	 */
	public void SetImageAsync(String tPath)
	{
		if (tPath == null)
			tPath = "null";
		if (tPath.startsWith("http"))
			SetImageHttp(tPath);
		else if (tPath.startsWith("/"))
			SetImagePathAsync(tPath);
		else
			SetAssetsImagePathAsync(tPath);
	}

	/**
	 * 回收处理图片
	 */
	public void Destroy()
	{
		ilBitmap.cancelDisplayTask(this);
		setImageBitmap(null);
	}

	/**
	 * 设置控件锚点X轴
	 * @param fPivotX [0,1]范围
	 */
	public void SetPivotX(float fPivotX)
	{
		setPivotX(fPivotX * getWidth());
	}

	/**
	 * 设置控件锚点Y轴
	 * @param fPivotY [0,1]范围
	 */
	public void SetPivotY(float fPivotY)
	{
		setPivotY(fPivotY * getHeight());
	}

	/**
	 * 获取目标宽度
	 */
	public int GetTargetWidth()
	{
		return nTargetWidth;
	}

	/**
	 * 获取目标高度
	 */
	public int GetTargetHeight()
	{
		return nTargetHeight;
	}

    /**
     * 加载图片渐显时间
     * @param nFadeTime 渐显时间毫秒数
     */
    public void SetShowFade(int nFadeTime)
    {
		this.nFadeTime = nFadeTime;
//        if (nFadeTime>=0)
//            dioOptions.displayer(new FadeInBitmapDisplayer(nFadeTime));
    }

    /**
     * 设置加载中时显示的图片
     * @param nResourceID 图片资源ID
     */
    public void SetLoadingImage(int nResourceID)
    {
        dioOptions.showImageOnLoading(nResourceID);
    }

    /**
     * 设置加载中时显示的图片
     * @param bmp 图片
     */
    public void SetLoadingImage(Bitmap bmp)
    {
        dioOptions.showImageOnLoading(new BitmapDrawable(JKDebug.hContext.getResources(),bmp));
    }

    /**
     * 设置失败时显示的图片
     * @param nResourceID 图片资源ID
     */
    public void SetFailImage(int nResourceID)
    {
        dioOptions.showImageOnFail(nResourceID);
        dioOptions.showImageForEmptyUri(nResourceID);

    }

    /**
     * 设置失败时显示的图片
     * @param bmp 图片
     */
    public void SetFailImage(Bitmap bmp)
    {
        dioOptions.showImageOnFail(new BitmapDrawable(JKDebug.hContext.getResources(),bmp));
        dioOptions.showImageForEmptyUri(new BitmapDrawable(JKDebug.hContext.getResources(),bmp));
    }

    /**
     * 加载图片
     * @param tUrl 图片image-loader地址
     */
    private void LoaderBitmapAsync(String tUrl)
    {
        ilBitmap.displayImage(tUrl, this, dioOptions.build(), new ImageLoadingListener() {

            @Override
            public void onLoadingStarted(String imageUri, View view) {
                bStart = true;
            }

            @Override
            public void onLoadingFailed(String imageUri, View view,
                                        FailReason failReason) {
                nTargetWidth = 0;
                nTargetHeight = 0;
                if (m_UpdateLisnter != null)
                    m_UpdateLisnter.FinishLoad(false);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                if (loadedImage != null) {
                    nTargetWidth = loadedImage.getWidth();
                    nTargetHeight = loadedImage.getHeight();
                }
                if (bStart)
                {
                    if (m_UpdateLisnter != null) {
                        m_UpdateLisnter.LoadingProgress(100);
                    }
                }
                if (m_UpdateLisnter != null)
                    m_UpdateLisnter.FinishLoad(true);
                if (nFadeTime > 0)
                {
                    AlphaAnimation raAnimation = new AlphaAnimation(0,1);
                    raAnimation.setDuration(nFadeTime);
                    raAnimation.setInterpolator(new LinearInterpolator());
                    JKImageView.this.startAnimation(raAnimation);
                }
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
            }
        }, new ImageLoadingProgressListener() {

            @Override
            public void onProgressUpdate(String arg0, View arg1, int arg2, int arg3) {
                bStart = false;
                if (m_UpdateLisnter != null) {
                    int nPercent = (int) ((double) arg2 / arg3 * 100);
                    m_UpdateLisnter.LoadingProgress(nPercent);
                }
            }
        });
    }

	/**
	 * 初始化图片加载配置
	 */
	private void InitBitmapConfig()
	{
		dioOptions =  new DisplayImageOptions.Builder()
			.cacheInMemory(true)
			.cacheOnDisk(true)
			.considerExifParams(true)
			.imageScaleType(ImageScaleType.IN_SAMPLE_INT);

		final Drawable dwSrc = getDrawable();
		getViewTreeObserver().addOnPreDrawListener(new OnPreDrawListener() {

			@Override
			public boolean onPreDraw() {
				if (dwSrc instanceof AnimationDrawable)
				{
					((AnimationDrawable) dwSrc).start();
				}
				getViewTreeObserver().removeOnPreDrawListener(this);
				return true;
			}
		});
	}
}