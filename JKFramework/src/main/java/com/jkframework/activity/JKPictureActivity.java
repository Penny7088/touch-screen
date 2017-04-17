package com.jkframework.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;

import com.jkframework.algorithm.JKConvert;
import com.jkframework.algorithm.JKFile;
import com.jkframework.algorithm.JKPicture;
import com.jkframework.algorithm.JKRandom;
import com.jkframework.bean.JKCutPictureData;
import com.jkframework.config.JKPreferences;
import com.jkframework.control.JKToast;

import java.io.File;


public class JKPictureActivity extends JKBaseActivity
{
    /**是否进行裁剪*/
    private boolean bCrop = false;
    /**保存的外部地址*/
    private String tSavePath = "";
    /**图片裁剪资料*/
    private JKCutPictureData jkcpdCutData = null;

    /**相册选择图片*/
    private final int REQUEST_CODE_PICK_IMAGE = 0;
    /**手机拍照选择照片*/
    private final int REQUEST_CODE_CAPTURE_CAMEIA = 1;
    /**裁剪照片*/
    private final int REQUEST_CODE_CROP = 2;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null)
        {
            bCrop = savedInstanceState.getBoolean("Crop",false);
            tSavePath = savedInstanceState.getString("SavePath");
            jkcpdCutData = savedInstanceState.getParcelable("CutData");
            return;
        }

        int nType = getIntent().getIntExtra("Type",0);
        jkcpdCutData = getIntent().getParcelableExtra("CutData");
        bCrop = jkcpdCutData != null;
        if (nType == 0)
        {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
//			if (bCrop)
//			{
//				intent.putExtra("crop", "true");
//				intent.putExtra("aspectX", jkcpdCutData.nScaleX);
//		        intent.putExtra("aspectY", jkcpdCutData.nScaleY);
//		        if (jkcpdCutData.nTargetWidth != -1)
//		        	intent.putExtra("outputX",jkcpdCutData.nTargetWidth);
//		        if (jkcpdCutData.nTargetHeight != -1)
//		        	intent.putExtra("outputY",jkcpdCutData.nTargetHeight);

//		        tSavePath = JKFile.GetPublicCachePath() + "/JKCache/JKPictureActivity/cache/" + JKRandom.GetIntRandom(32);
//		        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(tSavePath)));
//			}
            intent.putExtra("return-data", false);
            //intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());
            intent.putExtra("noFaceDetection", true);
            startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
        }
        else {
            try {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                tSavePath = JKFile.GetPublicCachePath() + "/JKCache/JKPictureActivity/" + JKRandom.MakeGUID() + ".jpg";
                JKFile.CreateDir(tSavePath);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(tSavePath)));
                intent.putExtra("return-data", false);
                intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
                intent.putExtra("noFaceDetection", true);
                startActivityForResult(intent, REQUEST_CODE_CAPTURE_CAMEIA);
            }
            catch (Exception e)
            {
                JKToast.Show("拍照功能无法使用",1);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putBoolean("Crop", bCrop);
        outState.putString("SavePath", tSavePath);
        outState.putParcelable("CutData", jkcpdCutData);
    }

    @Override
    public void onRestoreInstanceState(@NonNull Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)  {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_IMAGE) {
            if (resultCode == RESULT_OK)
            {
                if (data != null)
                {
                    Uri uri = data.getData();
                    if (uri != null)
                    {
                        tSavePath = JKConvert.UriToPath(uri);
                        if (tSavePath == null)
                        {
                            JKPreferences.SaveSharePersistent("JKFILE_CHOICEFILE","");
                            if (JKFile.mChoiceListener != null)
                                JKFile.mChoiceListener.FinishChoice(null);
                        }
                        String tPathTmp = JKFile.GetPublicCachePath() + "/JKCache/JKPictureActivity/" + JKRandom.MakeGUID() + ".png";
                        if (FixPic(tSavePath,tPathTmp))
                            tSavePath = tPathTmp;
                        if (bCrop)
                        {
                            CropCameia();
                            return;
                        }
                        else {
                            JKPreferences.SaveSharePersistent("JKFILE_CHOICEFILE",tSavePath);
                            if (JKFile.mChoiceListener != null)
                                JKFile.mChoiceListener.FinishChoice(tSavePath);
                        }
                    }
                    else {
                        Bitmap btPic = data.getParcelableExtra("data");
                        if (btPic != null)
                        {
                            String tPathTmp = JKFile.GetPublicCachePath() + "/JKCache/JKPictureActivity/" + JKRandom.MakeGUID() + ".png";
                            JKFile.WriteFile(tPathTmp, JKConvert.toByteArray(btPic, -1));
                            FixPic(tPathTmp, tPathTmp);
                            if (bCrop)
                            {
                                tSavePath = tPathTmp;
                                CropCameia();
                                return;
                            }
                            else {
                                JKPreferences.SaveSharePersistent("JKFILE_CHOICEFILE",tSavePath);
                                if (JKFile.mChoiceListener != null)
                                    JKFile.mChoiceListener.FinishChoice(tPathTmp);
                            }
                        }
                        else {	//判断文件是否存在
                            String tFilePath = data.getStringExtra("filePath");
                            if (tFilePath != null)
                            {
                                FixPic(tFilePath,tFilePath);
                                if (bCrop)
                                {
                                    tSavePath = tFilePath;
                                    CropCameia();
                                    return;
                                }
                                else {
                                    JKPreferences.SaveSharePersistent("JKFILE_CHOICEFILE",tFilePath);
                                    if (JKFile.mChoiceListener != null)
                                        JKFile.mChoiceListener.FinishChoice(tFilePath);
                                }
                            }
                            else {
                                JKPreferences.SaveSharePersistent("JKFILE_CHOICEFILE","");
                                if (JKFile.mChoiceListener != null)
                                    JKFile.mChoiceListener.FinishChoice(null);
                            }
                        }
                    }
                }
                else {
                    JKPreferences.SaveSharePersistent("JKFILE_CHOICEFILE","");
                    if (JKFile.mChoiceListener != null)
                        JKFile.mChoiceListener.FinishChoice(null);
                }
            }
            else {
                JKPreferences.SaveSharePersistent("JKFILE_CHOICEFILE","");
                if (JKFile.mChoiceListener != null)
                    JKFile.mChoiceListener.FinishChoice(null);
            }
            finish();
        }
        else if (requestCode == REQUEST_CODE_CAPTURE_CAMEIA) {
            if (JKFile.IsExists(tSavePath))
            {
                if (bCrop)
                {
                    CropCameia();
                    return;
                }
                else {
                    FixPic(tSavePath,tSavePath);
                    JKPreferences.SaveSharePersistent("JKFILE_CHOICEFILE",tSavePath);
                    if (JKFile.mChoiceListener != null)
                        JKFile.mChoiceListener.FinishChoice(tSavePath);
                }
            }
            else {
                JKPreferences.SaveSharePersistent("JKFILE_CHOICEFILE","");
                if (JKFile.mChoiceListener != null)
                    JKFile.mChoiceListener.FinishChoice(null);
            }
            finish();
        }
        else if (requestCode == REQUEST_CODE_CROP) {
            if (resultCode == RESULT_OK)
            {
                if (data != null)
                {
                    Uri uri = data.getData();
                    if (uri != null)
                    {
                        String tPath = JKConvert.UriToPath(uri);
                        JKPreferences.SaveSharePersistent("JKFILE_CHOICEFILE",tPath);
                        if (JKFile.mChoiceListener != null)
                            JKFile.mChoiceListener.FinishChoice(tPath);
                    }
                    else {
                        Bitmap btPic = data.getParcelableExtra("data");
                        if (btPic != null)
                        {
                            String tSavePath = JKFile.GetPublicCachePath() + "/JKCache/JKPictureActivity/" + JKRandom.MakeGUID() + ".png";
                            JKFile.WriteFile(tSavePath, JKConvert.toByteArray(btPic,-1));
                            JKPreferences.SaveSharePersistent("JKFILE_CHOICEFILE",tSavePath);
                            if (JKFile.mChoiceListener != null)
                                JKFile.mChoiceListener.FinishChoice(tSavePath);
                        }
                        else {	//判断文件是否存在
                            String tFilePath = data.getStringExtra("filePath");
                            if (tFilePath != null)
                            {
                                JKPreferences.SaveSharePersistent("JKFILE_CHOICEFILE",tFilePath);
                                if (JKFile.mChoiceListener != null)
                                    JKFile.mChoiceListener.FinishChoice(tFilePath);
                            }
                            else if (tSavePath != null && !tSavePath.equals("")){
                                JKPreferences.SaveSharePersistent("JKFILE_CHOICEFILE",tSavePath);
                                if (JKFile.mChoiceListener != null)
                                    JKFile.mChoiceListener.FinishChoice(tSavePath);
                            }
                            else {
                                JKPreferences.SaveSharePersistent("JKFILE_CHOICEFILE","");
                                if (JKFile.mChoiceListener != null)
                                    JKFile.mChoiceListener.FinishChoice(null);
                            }
                        }
                    }
                }
                else {
                    JKPreferences.SaveSharePersistent("JKFILE_CHOICEFILE","");
                    if (JKFile.mChoiceListener != null)
                        JKFile.mChoiceListener.FinishChoice(null);
                }
            }
            else {
                JKPreferences.SaveSharePersistent("JKFILE_CHOICEFILE", "");
                if (JKFile.mChoiceListener != null)
                    JKFile.mChoiceListener.FinishChoice(null);
            }
            finish();
        }
    }

    /**
     * 裁剪相机拍摄图片
     */
    private void CropCameia()
    {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(Uri.fromFile(new File(tSavePath)),"image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", jkcpdCutData.nScaleX);
        intent.putExtra("aspectY", jkcpdCutData.nScaleY);
        if (jkcpdCutData.nTargetWidth != -1)
            intent.putExtra("outputX",jkcpdCutData.nTargetWidth);
        if (jkcpdCutData.nTargetHeight != -1)
            intent.putExtra("outputY",jkcpdCutData.nTargetHeight);
        tSavePath = JKFile.GetPublicCachePath() + "/JKCache/JKPictureActivity/" + JKRandom.MakeGUID() + ".png";
        JKFile.CreateDir(tSavePath);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(tSavePath)));
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, REQUEST_CODE_CROP);
    }

    private static boolean FixPic(String tLoadPath,String tSavePath)
    {
        try {
            ExifInterface exifInterface = new ExifInterface(tLoadPath);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_NORMAL:
                    return false;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    break;
            }
            Bitmap btMap = JKPicture.LoadBitmap(tLoadPath,false);
            if (JKFile.GetFileExtension(tLoadPath).equalsIgnoreCase("jpeg") || JKFile.GetFileExtension(tLoadPath).equalsIgnoreCase("jpg") ) {
				JKFile.WriteFile(tSavePath, JKConvert.toByteArray(Bitmap.createBitmap(btMap, 0, 0, btMap.getWidth(), btMap.getHeight()),80));                
            }
            else {
               JKFile.WriteFile(tSavePath, JKConvert.toByteArray(Bitmap.createBitmap(btMap, 0, 0, btMap.getWidth(), btMap.getHeight()),-1));
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}