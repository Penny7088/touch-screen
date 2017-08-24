package com.yysp.ecandroid.util;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created on 2017/8/19 0019.
 * by penny
 */

public class FileUtils {
    private static final String DIR = "Android";
    private static final String CHILD_DIR = "data";
    private static final String TAG = "FileUtils";
    public static final String TENCENT_PACKAGE = "com.tencent.mm";

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
                                    if (k.getName().equals(TENCENT_PACKAGE)) {
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

}
