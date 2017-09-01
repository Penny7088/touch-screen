package com.yysp.ecandroid.framework.util;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import com.yysp.ecandroid.config.PackageConst;
import com.yysp.ecandroid.framework.crashMonitor.ui.utils.DISPath;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2017/8/19 0019.
 * by penny
 */

public class FileUtils {
    private static final String DIR = "Android";
    private static final String CHILD_DIR = "data";
    private static final String TAG = "FileUtils";

    private static boolean isMount() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    static String findTencentDir() {
        String path;
        if (isMount()) {
            File externalStorageDirectory =
                    Environment.getExternalStorageDirectory();
            path = getTencentPath(externalStorageDirectory);
            return path;
        } else {
            return null;
        }
    }

    private static String getTencentPath(File pDirectory) {
        String tencentPath = null;
        File[] lFiles = pDirectory.listFiles();
        for (File i :
                lFiles) {
            if (i.getName().equals(FileUtils.DIR)) {
                if (i.isDirectory()) {
                    File[] lAndroidList = i.listFiles();
                    for (File j :
                            lAndroidList) {
                        if (j.getName().equals(FileUtils.CHILD_DIR)) {
                            if (j.isDirectory()) {
                                File[] lDataList = j.listFiles();
                                for (File k :
                                        lDataList) {
                                    if (k.getName().equals(PackageConst.APP)) {
                                        if (k.isDirectory()) {
                                            tencentPath = k.getPath();
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return tencentPath;
    }

    public static String findDataTencentDir(File pDirectory) {
        String tencentPath = null;
        if (pDirectory.exists()) {
            if (pDirectory.isDirectory()) {
                File[] lFiles = pDirectory.listFiles();
                for (File f :
                        lFiles) {
                    Log.d("==========", "==:" + f.getName());
                }
            }
        }
        return tencentPath;
    }

    public static boolean deleteDir(File dir) {
//        if(!dir.exists()) return false;
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    public static void copyDir(String oldPath, String newPath) {
        try {
            (new File(newPath)).mkdirs();
            File a = new File(oldPath);
            String[] file = a.list();
            File temp = null;
            for (int i = 0; i < file.length; i++) {
                if (oldPath.endsWith(File.separator)) {
                    temp = new File(oldPath + file[i]);
                } else {
                    temp = new File(oldPath + File.separator + file[i]);
                }
                if (temp.isFile()) {
                    FileInputStream input = new FileInputStream(temp);
                    FileOutputStream output = new FileOutputStream(newPath + "/" +
                            (temp.getName()).toString());
                    byte[] b = new byte[1024 * 5];
                    int len;
                    while ((len = input.read(b)) != -1) {
                        output.write(b, 0, len);
                    }
                    output.flush();
                    output.close();
                    input.close();
                }
                if (temp.isDirectory()) {
                    copyDir(oldPath + "/" + file[i], newPath + "/" + file[i]);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "复制整个文件夹内容操作出错");
            e.printStackTrace();
        }
    }


    private static boolean execCmdDeleteDir(String pStrings) {
        Process process = null;
        boolean isDelete = false;
        try {
            process = Runtime.getRuntime().exec("su");
            OutputStream os = process.getOutputStream();
            process.getErrorStream();
            os.write((pStrings + "\n").getBytes());
            os.write("exit\n".getBytes());
            os.flush();
            os.close();
            isDelete = true;
        } catch (IOException pE) {
            pE.printStackTrace();
            return isDelete;
        }
        return isDelete;
    }


    private static String execCmd_mkdir(String[] cmds) {
        String path = null;
        Process process = null;
        try {
            process = Runtime.getRuntime().exec("su");
            OutputStream os = process.getOutputStream();
            process.getErrorStream();
            InputStream is = process.getInputStream();
            int lLength = cmds.length;
            for (int i = 0; i < lLength; i++) {
                String str = cmds[i];
                os.write((str + "\n").getBytes());
            }
            os.write("exit\n".getBytes());
            os.flush();
            os.close();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            while (true) {
                String str = reader.readLine();
                if (str.trim().equals(PackageConst.APP)) {
                    path = str;
                    break;
                }
            }
            reader.close();
            process.waitFor();
            process.destroy();
        } catch (Exception localException) {
            return path;
        }
        return path;
    }

    private static String execCmdsforResult(String[] cmds) {
        String path = null;
        try {
            Process process = Runtime.getRuntime().exec("su");
            OutputStream os = process.getOutputStream();
            process.getErrorStream();
            InputStream is = process.getInputStream();
            int lLength = cmds.length;
            for (int i = 0; i < lLength; i++) {
                String str = cmds[i];
                os.write((str + "\n").getBytes());
            }
            os.write("exit\n".getBytes());
            os.flush();
            os.close();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            while (true) {
                String str = reader.readLine();
                if (str.trim().equals(PackageConst.APP)) {
                    path = str;
                    break;
                }
            }
            reader.close();
            process.waitFor();
            process.destroy();
        } catch (Exception localException) {
            return null;
        }
        return path;
    }

    /**
     * 查找文件夹
     * @return
     */
    public static String findDir() {
        String path = execCmdsforResult(new String[]{"cd /data/data/", "ls -R"});
        return path == null ? null : path;
    }

    /**
     * 删除文件夹
     * @param path
     * @return
     */
    public static boolean deleteDir(String path) {
        return execCmdDeleteDir("rm -rf data/data/" + path);
    }

    /**
     * 创建文件夹
     * @return
     */
    public static String mkdir() {
        return execCmd_mkdir(new String[]{"cd /data/data/", "mkdir com.tencent.mm", "ls -R"});
    }

    /**
     * 允许权限
     * @param path
     */
    public static void permissionAs(String path){
        execPermission("chmod 777 /data/data/" + path);
    }


    public static void execPermission(String pS) {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec("su");
            OutputStream os = process.getOutputStream();
            process.getErrorStream();
            os.write((pS + "\n").getBytes());
            os.write("exit\n".getBytes());
            os.flush();
            os.close();
        } catch (IOException pE) {
            pE.printStackTrace();
        }
    }

    /***
     * crash Logo
     *
     * @param context
     * @return
     */
    public static String getCrashLogPath(Context context) {
        File externalCacheDir = context.getExternalCacheDir();
        String path = DISPath.PATH_BASE + File.separator + "crashLogs";
        return path;
    }


    public static List<File> getFileList(File file) {
        List<File> mFileList = new ArrayList<>();
        File[] fileArray = file.listFiles();
        if (fileArray == null || fileArray.length <= 0) {
            return mFileList;
        }
        for (File f : fileArray) {
            if (f.isFile()) {
                mFileList.add(f);
            }
        }
        return mFileList;
    }

    public static void deleteAllFiles(File root) {
        File files[] = root.listFiles();
        if (files != null)
            for (File f : files) {
                if (f.isDirectory()) { // 判断是否为文件夹
                    deleteAllFiles(f);
                    try {
                        f.delete();
                    } catch (Exception e) {
                    }
                } else {
                    if (f.exists()) { // 判断是否存在
                        deleteAllFiles(f);
                        try {
                            f.delete();
                        } catch (Exception e) {
                        }
                    }
                }
            }
    }
}
