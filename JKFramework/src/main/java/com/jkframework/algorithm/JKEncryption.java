package com.jkframework.algorithm;

import android.util.Base64;

import com.jkframework.debug.JKLog;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class JKEncryption {

    /**
     * 获取32位大写MD5码
     *
     * @param tString 需要加密的字符串
     * @return 返回加密后的字符串
     */
    public static String MD5_32(String tString) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] a_byByte = JKConvert.toByteArray(tString);
            if (a_byByte != null) {
                digest.update(a_byByte);
            } else
                return "";

            StringBuilder builder = new StringBuilder();

            for (byte b : digest.digest()) {
                builder.append(Integer.toHexString((b >> 4) & 0xf));
                builder.append(Integer.toHexString(b & 0xf));
            }
            return builder.toString().toUpperCase(Locale.getDefault());

        } catch (Exception e) {
            JKLog.ErrorLog("获取MD5失败.原因为" + e.getMessage());
            return "";
        }
    }

    /**
     * 获取16位大写MD5码
     *
     * @param tString 需要加密的字符串
     * @return 返回加密后的字符串
     */
    public static String MD5_16(String tString) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] a_byByte = JKConvert.toByteArray(tString);
            if (a_byByte != null) {
                digest.update(a_byByte);
            } else
                return "";

            StringBuilder builder = new StringBuilder();

            for (byte b : digest.digest()) {
                builder.append(Integer.toHexString((b >> 4) & 0xf));
                builder.append(Integer.toHexString(b & 0xf));
            }
            return builder.toString().toUpperCase(Locale.getDefault()).substring(8, 24);

        } catch (Exception e) {
            JKLog.ErrorLog("获取MD5失败.原因为" + e.getMessage());
            return "";
        }
    }

    /**
     * 获取16位MD5码二进制
     *
     * @param tString 需要加密的字符串
     * @return 返回加密后的字节数组
     */
    public static byte[] MD5_byte(String tString) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] a_byByte = JKConvert.toByteArray(tString);
            if (a_byByte != null) {
                digest.update(a_byByte);
            } else
                return null;

            byte[] a_byBack = new byte[16];
            int nIndex = 0;
            for (byte b : digest.digest()) {
                a_byBack[nIndex] = b;
                nIndex++;
            }

            return a_byBack;

        } catch (Exception e) {
            JKLog.ErrorLog("获取MD5失败.原因为" + e.getMessage());
            return null;
        }
    }

    /**
     * 获取40位大写SHA1码
     *
     * @param tString 需要加密的字符串
     * @return 返回加密后的字符串
     */
    public static String SHA1_40(String tString) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update(tString.getBytes());
            byte messageDigest[] = digest.digest();
            if (messageDigest == null) {
                return "";
            }
            int expectedStringLen = messageDigest.length * 2;
            StringBuilder sb = new StringBuilder(expectedStringLen);
            for (byte aMessageDigest : messageDigest) {
                String hexStr = Integer.toString(aMessageDigest & 0x00FF, 16);
                if (hexStr.length() == 1) {
                    hexStr = "0" + hexStr;
                }
                sb.append(hexStr);
            }
            return sb.toString().toUpperCase(Locale.getDefault());
        } catch (NoSuchAlgorithmException e) {
            JKLog.ErrorLog("获取SHA1失败.原因为" + e.getMessage());
        }
        return "";
    }

    /**
     * Aes 128位解密
     *
     * @param a_byList  加密文件字节数组
     * @param tPassword 加密钥匙
     * @return 解密后的字节数组
     */
    public static byte[] AesDecryptor(byte[] a_byList, String tPassword) {
        byte[] a_byByte = JKConvert.toByteArray(MD5_16(tPassword));
        if (a_byByte == null)
            return null;
        IvParameterSpec dps = new IvParameterSpec(a_byByte);
        SecretKeySpec secretKey = new SecretKeySpec(a_byByte, "AES");

        try {
            Cipher cpjm = Cipher.getInstance("AES/CBC/NoPadding");
            cpjm.init(Cipher.DECRYPT_MODE, secretKey, dps);
            byte[] a_byBack = cpjm.doFinal(a_byList);
            int nRealSize = 0;
            for (int i = a_byBack.length - 1; i >= 0; i--) {
                if (a_byBack[i] != '\0') {
                    nRealSize = i + 1;
                    break;
                }
            }
            byte[] a_byRealBack = new byte[nRealSize];
            System.arraycopy(a_byBack, 0, a_byRealBack, 0, nRealSize);
            return a_byRealBack;
        } catch (Exception e) {
            JKLog.ErrorLog("Aes解密失败.原因为" + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Aes 128位加密
     *
     * @param a_byList  加密文件字节数组
     * @param tPassword 加密钥匙
     * @return 加密后的字节数组
     */
    public static byte[] AesEncryptor(byte[] a_byList, String tPassword) {
        byte[] a_byByte = JKConvert.toByteArray(MD5_16(tPassword));
        if (a_byByte == null)
            return null;
        IvParameterSpec dps = new IvParameterSpec(a_byByte);
        SecretKeySpec secretKey = new SecretKeySpec(a_byByte, "AES");

        try {
            Cipher cpjm = Cipher.getInstance("AES/CBC/NoPadding");
            int nBlockSize = cpjm.getBlockSize();
            int nFilLength = a_byList.length;
            if (nFilLength % nBlockSize != 0) {
                nFilLength = nFilLength + (nBlockSize - (nFilLength % nBlockSize));
            }
            byte[] a_byFillList = new byte[nFilLength];
            System.arraycopy(a_byList, 0, a_byFillList, 0, a_byList.length);

            cpjm.init(Cipher.ENCRYPT_MODE, secretKey, dps);
            return cpjm.doFinal(a_byFillList);
        } catch (Exception e) {
            JKLog.ErrorLog("Aes加密失败.原因为" + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将字符串Base64解密
     *
     * @param a_byList 加密前的字节数组
     * @return Base64 解密后的字节数组
     */
    public static byte[] Base64Decryptor(byte[] a_byList) {
        return Base64.decode(a_byList, Base64.NO_WRAP);
    }

    /**
     * 将字符串Base64加密
     *
     * @param a_byList 解密前的字节数组
     * @return Base64 加密后的字节数组
     */
    public static byte[] Base64Encryptor(byte[] a_byList) {
        return Base64.encode(a_byList, Base64.NO_WRAP);
    }
}
