package com.jkframework.algorithm;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.database.Cursor;
import android.os.Build.VERSION;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore;

import com.jkframework.activity.JKBaseActivity;
import com.jkframework.activity.JKFileActivity;
import com.jkframework.activity.JKPictureActivity;
import com.jkframework.bean.JKCutPictureData;
import com.jkframework.bean.JKFolderData;
import com.jkframework.bean.JKImageData;
import com.jkframework.config.JKPreferences;
import com.jkframework.config.JKSystem;
import com.jkframework.debug.JKDebug;
import com.jkframework.debug.JKLog;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;


public class JKFile {

    /**
     * 选择文件监听回调
     */
    public static JKChoiceListener mChoiceListener = null;

    /**
     * 判断文件(夹)是否存在
     *
     * @param tFileName 文件路径地址
     * @return 存在返回true, 否则返回false
     */
    public static boolean IsExists(String tFileName) {
        if (tFileName.indexOf("/") != 0)
            tFileName = "/" + tFileName;

        File flFile = new File(tFileName);
        return flFile.exists();
    }

    /**
     * 判断是否为文件
     *
     * @param tFileName 文件路径地址
     * @return 是文件返回true, 否则返回false
     */
    public static boolean IsFile(String tFileName) {
        if (tFileName.indexOf("/") != 0)
            tFileName = "/" + tFileName;

        File flFile = new File(tFileName);
        return flFile.exists() && flFile.isFile();
    }

    /**
     * 判断文件是否为文件夹
     *
     * @param tFileName 文件路径地址
     * @return 存在返回true, 否则返回false
     */
    public static boolean IsDirectory(String tFileName) {
        if (tFileName.indexOf("/") != 0)
            tFileName = "/" + tFileName;

        File flFile = new File(tFileName);
        return flFile.exists() && flFile.isDirectory();
    }

    /**
     * 在指定路径文件上创建所有不存在的根目录   (文件夹末尾加"/")
     *
     * @param tDirName 文件路径(匹配所有向上的文件夹)
     */
    public static void CreateDir(String tDirName) {
        if (tDirName.indexOf("/") != 0)
            tDirName = "/" + tDirName;

        String tTmpPath;
        try {
            tTmpPath = tDirName.substring(0, tDirName.lastIndexOf("/"));
            if (!IsExists(tTmpPath))    //文件不存在
                CreateDir(tTmpPath);
            FileUtils.forceMkdir(new File(tTmpPath));
            String tRun = "chmod 777 " + tTmpPath;
            Runtime tiRun = Runtime.getRuntime();
            try {
                tiRun.exec(tRun);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (StringIndexOutOfBoundsException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断SD卡是否可用
     *
     * @return true表示可用
     */
    public static boolean IsSDCardAvailable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 以覆盖的方式写入文件
     *
     * @param tPath  文本的路径地址
     * @param buffer 写入的数据数组
     */
    public static void WriteFile(String tPath, byte[] buffer) {
        try {
            FileUtils.writeByteArrayToFile(new File(tPath), buffer);
        } catch (Exception e) {
            JKLog.ErrorLog("写入SD卡数据数组失败.原因为" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 写入SD卡数据流
     *
     * @param tPath 写入的地址
     * @param is    数据流
     */
    public static void WriteFile(String tPath, InputStream is) {
        try {
            FileUtils.copyInputStreamToFile(is, new File(tPath));
        } catch (Exception e) {
            JKLog.ErrorLog("写入SD卡数据流失败.原因为" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 以覆盖的方式写入文件(默认UTF-8)
     *
     * @param tPath 文本的路径地址
     * @param tText 写入的字符串
     */
    public static void WriteFile(String tPath, String tText) {
        try {
            FileUtils.writeStringToFile(new File(tPath), tText, "UTF-8");
        } catch (Exception e) {
            JKLog.ErrorLog("写入SD卡字符串失败.原因为" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 指定编码格式覆盖的方式写入文件
     *
     * @param tPath     文本的路径地址
     * @param tText     写入的字符串
     * @param tEncoding 编码格式(GBK,UTF-8)
     */
    public static void WriteFile(String tPath, String tText, String tEncoding) {
        try {
            FileUtils.writeStringToFile(new File(tPath), tText, tEncoding);
        } catch (Exception e) {
            JKLog.ErrorLog("写入SD卡字符串失败.原因为" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 以末尾追加的方式写入文件(默认UTF-8)
     *
     * @param tPath  文本的路径地址
     * @param buffer 写入的字符串
     */
    public static void AppendFile(String tPath, byte[] buffer) {
        try {
            FileUtils.writeByteArrayToFile(new File(tPath), buffer, true);
        } catch (Exception e) {
            JKLog.ErrorLog("附加写入SD卡数据数组失败.原因为" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 以末尾追加的方式写入文件(默认UTF-8)
     *
     * @param tPath 文本的路径地址
     * @param tText 写入的字符串
     */
    public static void AppendFile(String tPath, String tText) {
        try {
            FileUtils.writeStringToFile(new File(tPath), tText, true);
        } catch (Exception e) {
            JKLog.ErrorLog("附加写入SD卡字符串失败.原因为" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 读取文件
     *
     * @param tPath 文件路径地址
     * @return 返回文件字节数组
     */
    public static byte[] ReadBytes(String tPath) {
        try {
            return FileUtils.readFileToByteArray(new File(tPath));
        } catch (Exception e) {
            JKLog.ErrorLog("读取SD卡数据数组失败.原因为" + e.getMessage());
            e.printStackTrace();
        }
        return new byte[0];
    }

    /**
     * 读取文件(默认UTF-8)
     *
     * @param tPath 文件路径地址
     * @return 返回文件内容字符串
     */
    public static String ReadFile(String tPath) {
        try {
            return FileUtils.readFileToString(new File(tPath), "UTF-8");
        } catch (Exception e) {
            JKLog.ErrorLog("读取SD卡字符串失败.原因为" + e.getMessage());
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 读取文件
     *
     * @param tPath     文件路径地址
     * @param tEncoding 字符编码(UTF-8,GBK,ISO-8859-1)
     * @return 返回文件内容字符串
     */
    public static String ReadFile(String tPath, String tEncoding) {
        try {
            return FileUtils.readFileToString(new File(tPath), tEncoding);
        } catch (Exception e) {
            JKLog.ErrorLog("读取SD卡字符串失败.原因为" + e.getMessage());
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 读取Assets文件(默认UTF-8)
     *
     * @param tPath 文件路径地址
     * @return 返回文件字符串
     */
    public static String ReadAssetsFile(String tPath) {
        InputStream isStream = ReadAssets(tPath);
        return JKConvert.toString(isStream);
    }

    /**
     * 读取Assets文件
     *
     * @param tPath     文件路径地址
     * @param tEncoding 字符编码(UTF-8,GBK,ISO-8859-1)
     * @return 返回文件内容字符串
     */
    public static String ReadAssetsFile(String tPath, String tEncoding) {
        InputStream isStream = ReadAssets(tPath);
        return JKConvert.toString(isStream, tEncoding);
    }

    /**
     * 获取文件(夹)大小
     *
     * @param tPath SD卡路径
     * @return 文件大小以字节为单位
     */
    public static long GetFileSize(String tPath) {
        if (IsExists(tPath)) {
            if (IsFile(tPath)) {
                File fInput = new File(tPath);
                return fInput.length();
            } else {
                return FileUtils.sizeOfDirectory(new File(tPath));
            }
        }
        return 0;
    }

    /**
     * 获取SD卡总容量
     *
     * @param tPath SD卡根目录
     * @return SD卡字节大小
     */
    @SuppressWarnings("deprecation")
    @TargetApi(18)
    public static long GetSDCardSize(String tPath) {
        StatFs stat = new StatFs(tPath);
        if (VERSION.SDK_INT >= 18) {
            long blockSize = stat.getBlockSizeLong();
            long totalBlocks = stat.getBlockCountLong();
            return blockSize * totalBlocks;
        } else {
            long blockSize = stat.getBlockSize();
            long totalBlocks = stat.getBlockCount();
            return blockSize * totalBlocks;
        }
    }

    /**
     * 获取SD卡可用容量
     *
     * @param tPath SD卡根目录
     * @return SD卡字节大小
     */
    @SuppressWarnings("deprecation")
    @TargetApi(18)
    public static long GetSDCardAvailableSize(String tPath) {
        StatFs stat = new StatFs(tPath);
        if (VERSION.SDK_INT >= 18) {
            long blockSize = stat.getBlockSizeLong();
            long totalBlocks = stat.getAvailableBlocksLong();
            return blockSize * totalBlocks;
        } else {
            long blockSize = stat.getBlockSize();
            long totalBlocks = stat.getAvailableBlocks();
            return blockSize * totalBlocks;
        }
    }

    /**
     * 遍历文件夹里所有的文件
     *
     * @param tPath        遍历文件夹路径
     * @param bIsIterative 是否进入子文件夹
     * @return 文件列表
     */
    public static ArrayList<String> GetFiles(String tPath, boolean bIsIterative) {
        ArrayList<String> a_tBack = new ArrayList<>();
         /*判断目录是否存在以及是否是目录*/
        if (!IsExists(tPath))
            return a_tBack;
        if (IsFile(tPath)) {
            a_tBack.add(tPath);
            return a_tBack;
        }

        LinkedList<File> a_flBack = (LinkedList<File>) FileUtils.listFiles(new File(tPath), null, bIsIterative);
        for (int i = 0; i < a_flBack.size(); ++i) {
            a_tBack.add(a_flBack.get(i).getAbsolutePath());
        }
        return a_tBack;
    }

    /**
     * 遍历文件夹里所有的文件夹
     *
     * @param tPath        遍历文件夹路径
     * @param bIsIterative 是否进入子文件夹
     * @return 文件夹列表
     */
    public static ArrayList<String> GetFolders(String tPath, boolean bIsIterative) {
        ArrayList<String> a_tBack = new ArrayList<>();
         /*判断目录是否存在以及是否是目录*/
        if (!IsExists(tPath))
            return a_tBack;
        if (IsFile(tPath))
            return a_tBack;

        if (bIsIterative) {
            /*创建队列*/
            Queue<String> q_tMatchList = new ConcurrentLinkedQueue<>();
            q_tMatchList.offer(tPath);
            while (!q_tMatchList.isEmpty()) {
                File fiFile = new File(q_tMatchList.poll());
                File[] a_flFiles = fiFile.listFiles();
                if (a_flFiles != null) {
                    for (File a_flFile : a_flFiles) {
                        if (JKFile.IsDirectory(a_flFile.getPath())) {
                            a_tBack.add(a_flFile.getPath());
                            q_tMatchList.offer(a_flFile.getPath());
                        }
                    }
                }
            }
        } else {        //当前目录下的文件
            File fiFile = new File(tPath);
            File[] a_flFiles = fiFile.listFiles();
            if (a_flFiles != null) {
                for (File a_flFile : a_flFiles) {
                    if (JKFile.IsDirectory(a_flFile.getPath()))
                        a_tBack.add(a_flFile.getPath());
                }
            }
        }
        return a_tBack;
    }

    /**
     * 获取文件名
     *
     * @param tPath 文件路径地址
     * @return 文件名
     */
    public static String GetFileName(String tPath) {
        return FilenameUtils.getName(tPath);
    }

    /**
     * 获取网络文件名
     *
     * @param tUrl 文件路径地址
     * @return 文件名
     */
    public static String GetUrlFileName(String tUrl) {
        return tUrl.substring(tUrl.lastIndexOf("/") + 1);
    }

    /**
     * Java文件操作 获取文件扩展名
     *
     * @param tFilename 文件路径
     * @return 返回文件扩展名
     */
    public static String GetFileExtension(String tFilename) {
        return FilenameUtils.getExtension(tFilename);
    }

    /**
     * Java文件操作 获取不带扩展名的文件名
     *
     * @param tFilename 文件名
     * @return 不带扩展名的文件名
     */
    public static String GetBaseName(String tFilename) {
        return FilenameUtils.getBaseName(tFilename);
    }

    /**
     * 获取SD卡的根目录
     *
     * @return SD卡根目录路径地址
     */
    public static String GetCurrentPath() {
        String tSdcardPath = Environment.getExternalStorageDirectory().getPath();
        if (tSdcardPath.indexOf("/") != 0)
            tSdcardPath = "/" + tSdcardPath;
        return tSdcardPath;
    }

//	/**
//     * 获取所有SD卡的根目录
//     * @return SD卡根目录集合
//     */
//    public static ArrayList<String> GetSDCardDir(){
//    	ArrayList<String> a_tBack = new ArrayList<>();
//    	try
//        {
//	    	StorageManager sm = (StorageManager) JKDebug.hContext.getSystemService(Context.STORAGE_SERVICE);
//	    	String[] paths = (String[]) sm.getClass().getMethod("getVolumePaths", null).invoke(sm, null);
//            for (String path : paths) {
//                String status = (String) sm.getClass().getMethod("getVolumeState", String.class).invoke(sm, path);
//                if (status.equals(Environment.MEDIA_MOUNTED)) {
//                    String tPath = path;
//                    if (tPath.indexOf("/") != 0)
//                        tPath = "/" + tPath;
//                    a_tBack.add(tPath);
//                }
//            }
//        }
//    	catch (Exception e) {
//    		JKLog.ErrorLog("获取多SD卡根目录出现错误.原因为" + e.getMessage());
//		}
//    	return a_tBack;
//    }

    /**
     * 获取SD卡的根目录(自动卸载)
     *
     * @return SD卡根目录路径地址
     */
    public static String GetPublicPath() {
        if (IsSDCardAvailable()) {
            String tSdcardPath = null;
            try {
                File flFile = JKDebug.hContext.getExternalFilesDir(null);
                if (flFile != null) {
                    tSdcardPath = flFile.getAbsolutePath();
                    if (tSdcardPath.indexOf("/") != 0)
                        tSdcardPath = "/" + tSdcardPath;
                }
            } catch (NullPointerException e) {
                return GetPrivatePath();
            }
            return tSdcardPath;
        } else {
            return GetPrivatePath();
        }
    }

    /**
     * 获取SD卡缓存的根目录(自动卸载)
     *
     * @return SD卡缓存根目录路径地址
     */
    public static String GetPublicCachePath() {
        if (IsSDCardAvailable()) {
            String tSdcardPath = null;
            try {
                File flFile = JKDebug.hContext.getExternalCacheDir();
                if (flFile != null) {
                    tSdcardPath = flFile.getAbsolutePath();
                    if (tSdcardPath.indexOf("/") != 0)
                        tSdcardPath = "/" + tSdcardPath;
                }
            } catch (NullPointerException e) {
                return GetPrivateCachePath();
            }
            return tSdcardPath;
        } else {
            return GetPrivateCachePath();
        }
    }

    /**
     * 获取内存缓存的根目录
     *
     * @return 内存缓存根目录路径地址
     */
    public static String GetPrivateCachePath() {
        String tSdcardPath = null;
        try {
            tSdcardPath = JKDebug.hContext.getCacheDir().getAbsolutePath();
            if (tSdcardPath.indexOf("/") != 0)
                tSdcardPath = "/" + tSdcardPath;
        } catch (NullPointerException e) {
            JKLog.ErrorLog("无法获取内存缓存卡目录.原因为" + e.getMessage());
            e.printStackTrace();
        }
        return tSdcardPath;
    }

    /**
     * 获取内存卡的根目录
     *
     * @return 内存卡根目录路径地址
     */
    public static String GetPrivatePath() {
        String tSdcardPath = null;
        try {
            tSdcardPath = JKDebug.hContext.getFilesDir().getAbsolutePath();
            if (tSdcardPath.indexOf("/") != 0)
                tSdcardPath = "/" + tSdcardPath;
        } catch (NullPointerException e) {
            JKLog.ErrorLog("无法获取内存卡目录.原因为" + e.getMessage());
            e.printStackTrace();
        }
        return tSdcardPath;
    }

    /**
     * 删除文件(夹)
     *
     * @param tFile 文件(夹)目录地址
     */
    public static void DeleteFile(String tFile) {
        try {
            FileUtils.forceDelete(new File(tFile));
        } catch (IOException e) {
            JKLog.ErrorLog("删除文件(夹)失败.原因为" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 将assets文件复制到指定目录
     *
     * @param tAssets   assets文件名
     * @param tFilePath 目标文件目录(末尾不带"/")
     */
    public static void AssetsToSDCard(String tAssets, String tFilePath) {
        try {
            InputStream isStrem = JKFile.ReadAssets(tAssets);
            if (isStrem != null)
                FileUtils.copyInputStreamToFile(isStrem, new File(tFilePath, tAssets));
        } catch (IOException e) {
            JKLog.ErrorLog("无法将Assets路径的文件拷贝到SD卡上.原因为" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 将raw文件复制到指定目录
     *
     * @param tRaw      raw文件名
     * @param tFilePath 目标文件目录
     */
    public static void RawToSDCard(String tRaw, String tFilePath) {
        try {
            int nId = JKDebug.hContext.getResources().getIdentifier(tRaw.substring(0, tRaw.indexOf(".")), "raw", JKDebug.hContext.getPackageName());
            FileUtils.copyInputStreamToFile(JKDebug.hContext.getResources().openRawResource(nId), new File(tFilePath, tRaw));
        } catch (IOException e) {
            JKLog.ErrorLog("无法将raw路径的文件拷贝到SD卡上.原因为" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 重命名文件(夹)
     *
     * @param tSourceFile 源文件(夹)地址
     * @param tNewName    更改后的新名字
     */
    public static void RenameFile(String tSourceFile, String tNewName) {
        if (!IsExists(tSourceFile))
            return;
        try {
            File flSourceFile = new File(tSourceFile);
            File flTargetFolder = new File(flSourceFile.getParentFile(), tNewName);
            if (flSourceFile.isDirectory()) {
                FileUtils.moveDirectory(flSourceFile, flTargetFolder);
            } else if (flSourceFile.isFile()) {
                FileUtils.moveFile(flSourceFile, flTargetFolder);
            }
        } catch (IOException e) {
            JKLog.ErrorLog("重命名文件(夹)失败.原因为" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 剪切文件(夹)
     *
     * @param tSourceFile   源文件(夹)地址
     * @param tTargetFolder 目标文件夹地址下
     */
    public static void CutFileToDirectory(String tSourceFile, String tTargetFolder) {
        if (!IsExists(tSourceFile))
            return;

        try {
            File flSourceFile = new File(tSourceFile);
            File flTargetFolder = new File(tTargetFolder);
            if (flSourceFile.isDirectory()) {
                FileUtils.moveDirectoryToDirectory(flSourceFile, flTargetFolder, true);
            } else if (flSourceFile.isFile()) {
                FileUtils.moveFileToDirectory(flSourceFile, flTargetFolder, true);
            }
        } catch (IOException e) {
            JKLog.ErrorLog("剪切文件(夹)失败.原因为" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 剪切文件(夹)
     *
     * @param tSourceFile 源文件(夹)地址
     * @param tTargetFile 目标文件(夹)地址下
     */
    public static void CutFile(String tSourceFile, String tTargetFile) {
        if (!IsExists(tSourceFile))
            return;

        try {
            File flSourceFile = new File(tSourceFile);
            File flTargetFolder = new File(tTargetFile);
            if (flSourceFile.isDirectory()) {
                FileUtils.moveDirectory(flSourceFile, flTargetFolder);
            } else if (flSourceFile.isFile()) {
                FileUtils.moveFile(flSourceFile, flTargetFolder);
            }
        } catch (IOException e) {
            JKLog.ErrorLog("剪切文件(夹)失败.原因为" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 复制文件
     *
     * @param tSourceFile   源文件
     * @param tTargetFolder 目标文件夹地址下
     */
    public static void CopyFileToDirectory(String tSourceFile, String tTargetFolder) {
        if (!IsExists(tSourceFile))
            return;

        try {
            File flSourceFile = new File(tSourceFile);
            File flTargetFolder = new File(tTargetFolder);
            if (flSourceFile.isDirectory()) {
                FileUtils.copyDirectoryToDirectory(flSourceFile, flTargetFolder);
            } else if (flSourceFile.isFile()) {
                FileUtils.copyFileToDirectory(flSourceFile, flTargetFolder);
            }
        } catch (IOException e) {
            JKLog.ErrorLog("复制文件(夹)失败.原因为" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 复制文件
     *
     * @param tSourceFile 源文件
     * @param tTargetFile 目标文件
     */
    public static void CopyFile(String tSourceFile, String tTargetFile) {
        if (!IsExists(tSourceFile))
            return;

        try {
            File flSourceFile = new File(tSourceFile);
            File flTargetFolder = new File(tTargetFile);
            if (flSourceFile.isDirectory()) {
                FileUtils.copyDirectory(flSourceFile, flTargetFolder);
            } else if (flSourceFile.isFile()) {
                FileUtils.copyFile(flSourceFile, flTargetFolder);
            }
        } catch (IOException e) {
            JKLog.ErrorLog("复制文件(夹)失败.原因为" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 读取assets文件夹里的文件
     *
     * @param tPath 文件路径
     * @return 文件流
     */
    public static InputStream ReadAssets(String tPath) {
        try {
            return JKDebug.hContext.getResources().getAssets().open(tPath);
        } catch (IOException e) {
            JKLog.ErrorLog("读取Assets文件失败.原因为" + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 读取pfx文件私钥
     *
     * @param tPath     assets路径
     * @param tPassword 私钥密码
     * @return 返回私钥对象
     */
    public static PrivateKey ReadP12PrivateAssets(String tPath, String tPassword) {
        try {
            KeyStore ks = KeyStore.getInstance("PKCS12");
            InputStream fis = ReadAssets(tPath);
            //FileInputStream fis = new FileInputStream(tPath);  

            // If the keystore password is empty(""), then we have to set  
            // to null, otherwise it won't work!!!  

            char[] nPassword;
            if ((tPassword == null) || tPassword.trim().equals("")) {
                nPassword = null;
            } else {
                nPassword = tPassword.toCharArray();
            }

            ks.load(fis, nPassword);
            if (fis != null) {
                fis.close();
            }


            // Now we loop all the aliases, we need the alias to get keys.
            // It seems that this value is the "Friendly name" field in the  
            // detals tab <-- Certificate window <-- view <-- Certificate  
            // Button <-- Content tab <-- Internet Options <-- Tools menu  
            // In MS IE 6.  

            Enumeration<String> enum1 = ks.aliases();
            String keyAlias = null;
            if (enum1.hasMoreElements()) // we are readin just one certificate.  
            {
                keyAlias = enum1.nextElement();
            }

            // Now once we know the alias, we could get the keys.  
            //            Certificate cert = ks.getCertificate(keyAlias);
//            PublicKey pubkey = cert.getPublicKey();                         

            return (PrivateKey) ks.getKey(keyAlias, nPassword);
        } catch (Exception e) {
            JKLog.ErrorLog("读取pfx文件私钥失败.原因为" + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 读取pfx文件公钥
     *
     * @param tPath     assets路径
     * @param tPassword 公钥密码
     * @return 返回公钥对象
     */
    public static PublicKey ReadP12PublicAssets(String tPath, String tPassword) {
        try {
            KeyStore ks = KeyStore.getInstance("PKCS12");
            InputStream fis = ReadAssets(tPath);
            //FileInputStream fis = new FileInputStream(tPath);  

            // If the keystore password is empty(""), then we have to set  
            // to null, otherwise it won't work!!!  

            char[] nPassword;
            if ((tPassword == null) || tPassword.trim().equals("")) {
                nPassword = null;
            } else {
                nPassword = tPassword.toCharArray();
            }

            ks.load(fis, nPassword);
            if (fis != null) {
                fis.close();
            }


            // Now we loop all the aliases, we need the alias to get keys.
            // It seems that this value is the "Friendly name" field in the  
            // detals tab <-- Certificate window <-- view <-- Certificate  
            // Button <-- Content tab <-- Internet Options <-- Tools menu  
            // In MS IE 6.  

            Enumeration<String> enum1 = ks.aliases();
            String keyAlias = null;
            if (enum1.hasMoreElements()) // we are readin just one certificate.  
            {
                keyAlias = enum1.nextElement();
            }

            // Now once we know the alias, we could get the keys.  
            Certificate cert = ks.getCertificate(keyAlias);

            return cert.getPublicKey();
        } catch (Exception e) {
            JKLog.ErrorLog("读取pfx文件公钥失败.原因为" + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 读取pfx证书
     *
     * @param tPath     assets路径
     * @param tPassword 公钥密码
     * @return 返回证书对象
     */
    public static Certificate ReadP12CertAssets(String tPath, String tPassword) {
        try {
            KeyStore ks = KeyStore.getInstance("PKCS12");
            InputStream fis = ReadAssets(tPath);
            //FileInputStream fis = new FileInputStream(tPath);  

            // If the keystore password is empty(""), then we have to set  
            // to null, otherwise it won't work!!!  

            char[] nPassword;
            if ((tPassword == null) || tPassword.trim().equals("")) {
                nPassword = null;
            } else {
                nPassword = tPassword.toCharArray();
            }

            ks.load(fis, nPassword);
            if (fis != null) {
                fis.close();
            }


            // Now we loop all the aliases, we need the alias to get keys.
            // It seems that this value is the "Friendly name" field in the  
            // detals tab <-- Certificate window <-- view <-- Certificate  
            // Button <-- Content tab <-- Internet Options <-- Tools menu  
            // In MS IE 6.  

            Enumeration<String> enum1 = ks.aliases();
            String keyAlias = null;
            if (enum1.hasMoreElements()) // we are readin just one certificate.  
            {
                keyAlias = enum1.nextElement();
            }

            // Now once we know the alias, we could get the keys.

            return ks.getCertificate(keyAlias);
        } catch (Exception e) {
            JKLog.ErrorLog("读取pfx证书失败.原因为" + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 获取文件32位大写MD5码
     *
     * @param tFilePath 文件路径
     * @return 文件MD5码
     */
    public static String toMD5(String tFilePath) {
        if (!IsExists(tFilePath))
            return "";

        InputStream fis;
        byte[] buffer = new byte[1024 * 16];
        int numRead;
        MessageDigest md5;
        try {
            fis = new FileInputStream(tFilePath);
            md5 = MessageDigest.getInstance("MD5");
            while ((numRead = fis.read(buffer)) > 0) {
                md5.update(buffer, 0, numRead);
            }
            fis.close();
            StringBuilder hexString = new StringBuilder();
            for (byte b : md5.digest()) {
                hexString.append(Integer.toHexString(0xFF & b));
            }
            return hexString.toString().toUpperCase(Locale.getDefault());
        } catch (Exception e) {
            JKLog.ErrorLog("获取文件MD5失败.原因为" + e.getMessage());
            return "";
        }
    }

    /**
     * 获取Assets文件32位大写MD5码
     *
     * @param tAssetsPath 文件路径
     * @return 文件MD5码
     */
    public static String toAssetsMD5(String tAssetsPath) {
        InputStream fis = ReadAssets(tAssetsPath);
        byte[] buffer = new byte[1024 * 16];
        int numRead;
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
            if (fis != null) {
                while ((numRead = fis.read(buffer)) > 0) {
                    md5.update(buffer, 0, numRead);
                }
                fis.close();
            }
            StringBuilder hexString = new StringBuilder();
            for (byte b : md5.digest()) {
                hexString.append(Integer.toHexString(0xFF & b));
            }
            return hexString.toString().toUpperCase(Locale.getDefault());
        } catch (Exception e) {
            JKLog.ErrorLog("获取文件MD5失败.原因为" + e.getMessage());
            return "";
        }
    }

    /**
     * 获取安装包位置
     *
     * @return 返回安装包位置
     */
    public static String GetApkPath() {
        final ApplicationInfo applicationInfo = JKDebug.hContext.getApplicationInfo();
        return applicationInfo.sourceDir;
    }

    /**
     * 选择SD卡上的文件
     *
     * @param jkclChoiceListener 选择文件的回调监听
     */
    public static void ChoiceFilePath(JKChoiceListener jkclChoiceListener) {
        mChoiceListener = jkclChoiceListener;
        Intent itIntent = new Intent();
        itIntent.setClass(JKSystem.GetCurrentActivity(), JKFileActivity.class);
        JKBaseActivity CurrentActivity = JKSystem.GetCurrentActivity();
        if (CurrentActivity != null)
            CurrentActivity.startActivity(itIntent);
    }

    /**
     * 选择图片路径
     *
     * @param nType              0为从相册选择,1为手机拍照
     * @param jkclChoiceListener 选择图片的回调监听
     */
    public static void ChoicePicture(int nType, JKChoiceListener jkclChoiceListener) {
        mChoiceListener = jkclChoiceListener;
        Intent itIntent = new Intent();
        itIntent.setClass(JKSystem.GetCurrentActivity(), JKPictureActivity.class);
        itIntent.putExtra("Type", nType);
        JKBaseActivity CurrentActivity = JKSystem.GetCurrentActivity();
        if (CurrentActivity != null)
            CurrentActivity.startActivity(itIntent);
    }

    /**
     * 选择图片路径(进行裁剪)
     *
     * @param nType              0为从相册选择,1为手机拍照
     * @param jkcpdCutData       裁剪参数
     * @param jkclChoiceListener 选择图片的回调监听
     */
    public static void ChoicePicture(int nType, JKCutPictureData jkcpdCutData, JKChoiceListener jkclChoiceListener) {
        mChoiceListener = jkclChoiceListener;
        Intent itIntent = new Intent();
        itIntent.setClass(JKSystem.GetCurrentActivity(), JKPictureActivity.class);
        itIntent.putExtra("Type", nType);
        itIntent.putExtra("CutData", jkcpdCutData);
        JKBaseActivity CurrentActivity = JKSystem.GetCurrentActivity();
        if (CurrentActivity != null)
            CurrentActivity.startActivity(itIntent);
    }

    /**
     * 获取最近一次文件选择路径
     *
     * @return 文件选择的路径, 没有选择返回null
     */
    public static String GetLastChoice() {
        if (JKPreferences.GetSharePersistentString("JKFILE_CHOICEFILE").equals(""))
            return null;
        else {
            String tPath = JKPreferences.GetSharePersistentString("JKFILE_CHOICEFILE");
            JKPreferences.RemoveSharePersistent("JKFILE_CHOICEFILE");
            return tPath;
        }
    }

    /**
     * 获取相册接口列表
     *
     * @return 相册结构列表
     */
    public static ArrayList<JKFolderData> GetPhotoAlbumList() {
        ArrayList<JKFolderData> mResultFolder = new ArrayList<>();
        String[] IMAGE_PROJECTION = {
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATE_ADDED,
                MediaStore.Images.Media._ID};

        ContentResolver contentResolver = JKDebug.hContext.getContentResolver();
        Cursor data = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                null, null, IMAGE_PROJECTION[2] + " DESC");
        if (data != null) {
            int count = data.getCount();
            if (count > 0) {
                data.moveToFirst();
                do {
                    String path = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
                    if (path == null || path.length() == 0)
                        continue;
                    String name = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]));
                    long dateTime = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]));
                    JKImageData image = new JKImageData(path, name, dateTime);

                    File imageFile = new File(path);
                    File folderFile = imageFile.getParentFile();
                    JKFolderData folder = new JKFolderData();
                    folder.name = folderFile.getName();
                    folder.path = folderFile.getAbsolutePath();
                    folder.cover = image;
                    if (!mResultFolder.contains(folder)) {
                        List<JKImageData> imageList = new ArrayList<>();
                        imageList.add(image);
                        folder.images = imageList;
                        mResultFolder.add(folder);
                    } else {
                        // 更新
                        JKFolderData f = mResultFolder.get(mResultFolder.indexOf(folder));
                        f.images.add(image);
                    }
                } while (data.moveToNext());
            }
            data.close();
        }
        return mResultFolder;
    }

    /**
     * 创建任务文本文件
     */

    public static void creatFileTxt(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public interface JKChoiceListener {

        /**
         * 选择完毕时回调
         *
         * @param tPath 图片选择完后的地址,没有选择返回null
         */
        void FinishChoice(String tPath);
    }
}