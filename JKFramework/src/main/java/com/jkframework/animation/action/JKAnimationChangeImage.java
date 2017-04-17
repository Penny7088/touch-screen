package com.jkframework.animation.action;

import android.graphics.Bitmap;

import com.jkframework.algorithm.JKPicture;
import com.jkframework.algorithm.JKPicture.JKPictureListener;
import com.jkframework.animation.JKAnimationOne;
import com.jkframework.control.JKImageView;

import java.util.HashMap;

public class JKAnimationChangeImage extends JKAnimationOne{

	/**图片元素*/
	private JKImageView jkivImage;
	/**图片路径更换地址*/
	private String tPath;
	/**图片ID更换地址*/
	private int nID;
	/**图片模式(0为路径设置,1为ID设置)*/
	private int nMode;
	/**图片加载类型(0为SD卡,1为Assets)*/
	private int nType;
	/**是否异步加载*/
	private boolean bAsyncLoad;
	/**图片元素优化*/
	private boolean bOptimize;
	
	
	/**回调函数默认执行*/
	public void FinishAnimation() {
		
	}

	/**
	 * 构造函数
	 * @param jkivTmp 图片元素
	 * @param tString 更换图片的路径地址
	 * @param nTmp 图片加载类型(0为SD卡,1为Assets)
	 * @param bAsync 是否异步加载
	 * @param bTmp 是否优化加载
	 */
	public JKAnimationChangeImage(JKImageView jkivTmp,String tString,int nTmp,boolean bAsync,boolean bTmp) {
		jkivImage = jkivTmp;
		tPath = tString;
		nType = nTmp;
		bAsyncLoad = bAsync;
		bOptimize = bTmp;
		nAnimationTime = 0;
		nMode = 0;
		/*固定步骤*/
		InitFilter();
	}
	
	/**
	 * 构造函数
	 * @param jkivTmp 图片元素
	 * @param nRID 内部ID
	 */
	public JKAnimationChangeImage(JKImageView jkivTmp,int nRID) {
		jkivImage = jkivTmp;
		nID = nRID;
		nAnimationTime = 0;
		nMode = 1;
		/*固定步骤*/
		InitFilter();
	}
	
	@Override
	public void InitFilter() {
		a_tFilterList.add("ChangeImage");
	}

	@Override
	public void AddFilter() {
		a_tTypeList.add("ChangeImage");
	}

	@Override
	public int InitAnimation(HashMap<String, Boolean> hmList) {
		if (!bAutoCancel || hmList.get("ChangeImage"))	//取消动画
		{
			AddFilter();
			return 1;
		}			
		else
			return 2;
	}
	
	@Override
	public void StartAnimation(final HashMap<String, Boolean> hmList, Boolean jkvbTmp) {
		if (!bAutoCancel || hmList.get("ChangeImage"))
		{		
			switch (nMode)
			{
				case 0:
				{					
					if (bAsyncLoad)
					{
						if (bOptimize)
						{
							if (nType == 0)		//异步/优化/SD卡
							{
								JKPicture.LoadBitmapAsync(new JKPictureListener() {
									
									@Override
									public void FinishSet(Bitmap btMap) {
										jkivImage.setImageBitmap(btMap);
										a_tTypeList.remove("ChangeImage");
										CheckStatus();
									}									
								},tPath,jkivImage.getWidth(),jkivImage.getHeight(),false);
							}
							else {	//异步/优化/Assets
								JKPicture.LoadAssetsBitmapAsync(new JKPictureListener() {
									
									@Override
									public void FinishSet(Bitmap btMap) {
										jkivImage.setImageBitmap(btMap);
										a_tTypeList.remove("ChangeImage");
										CheckStatus();
									}									
								},tPath,jkivImage.getWidth(),jkivImage.getHeight(),false);
							}
						}
						else {
							if (nType == 0)		//异步/不优化/SD卡
							{
								JKPicture.LoadBitmapAsync(new JKPictureListener() {
									
									@Override
									public void FinishSet(Bitmap btMap) {
										jkivImage.setImageBitmap(btMap);
										a_tTypeList.remove("ChangeImage");
										CheckStatus();
									}									
								},tPath);
							}
							else {	//异步/不优化/Assets
								JKPicture.LoadAssetsBitmapAsync(new JKPictureListener() {
									
									@Override
									public void FinishSet(Bitmap btMap) {
										jkivImage.setImageBitmap(btMap);
										a_tTypeList.remove("ChangeImage");
										CheckStatus();
									}									
								},tPath);
							}
						}
					}
					else {	//同步加载
						if (nType == 0)
							jkivImage.SetImagePath(tPath);
						else 
							jkivImage.SetImagePath(tPath);
						a_tTypeList.remove("ChangeImage");
						CheckStatus();
					}
					break;
				}
				case 1:
				{
					jkivImage.setImageResource(nID);
					a_tTypeList.remove("ChangeImage");
					CheckStatus();		
					break;
				}
			}											
		}
		else
		{
			a_tTypeList.remove("ChangeImage");
			CheckStatus();
		}
	}
	
	@Override
	public void UpdateAnimation(HashMap<String, Boolean> hmList) {
		if (bAutoCancel && !hmList.get("ChangeImage") && CheckStatus("ChangeImage"))	//取消动画
		{
			a_tTypeList.remove("ChangeImage");
		}
	}

	@Override
	public void StopAnimation() {
		
	}

	@Override
	public void PauseAnimation() {		
		
	}

	@Override
	public void RestartAnimation(HashMap<String, Boolean> hmList) {
	}
}