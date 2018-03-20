package com.jht.doctor.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author mayakun
 * @introduce:MD5计算工具 （被计算的文件不可别其他引用，否则MD5值不一样）
 */
public class MD5Util {
    public static final String MD5_SIGN = "cbc10e419d5075e8bb1795abe9f96b28";

    /**
     * Used to build output as Hex
     */
    private static final char[] DIGITS_LOWER =
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * Used to build output as Hex
     */
    private static final char[] DIGITS_UPPER =
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    /**
     * md5加密
     *
     * @param source 加密原文 默认转小写
     * @return
     */
    public static String md5(String source) {
        return md5(source, false);
    }

    /**
     * md5加密
     *
     * @param source    加密原文
     * @param toLowCase 是否转换成大写
     * @return
     */
    public static String md5(String source, boolean toLowCase) {
        try {
            byte[] hash = MessageDigest.getInstance("MD5").digest(source.getBytes("UTF-8"));
            return new String(encodeHex(hash, toLowCase ? DIGITS_UPPER : DIGITS_LOWER));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 计算文件的MD5值，默认 返回小写的16进制
     *
     * @param file
     * @return
     */
    public static String md5(File file) {
        return md5(file, DIGITS_LOWER);
    }

    public static String md5(File file, boolean toLowCase) {
        return md5(file, toLowCase ? DIGITS_LOWER : DIGITS_UPPER);
    }

    /**
     * 计算文件的MD5值，
     *
     * @param file
     * @param toDigits 是否对字母进行小写转换
     * @return
     */
    public static String md5(File file, char[] toDigits) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = fis.read(buffer)) != -1) {
                md.update(buffer, 0, len);
            }
            return new String(encodeHex(md.digest(), toDigits));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static char[] encodeHex(byte[] data, char[] toDigits) {
        final int l = data.length;
        final char[] out = new char[l << 1];
        // two characters form the hex value.
        for (int i = 0, j = 0; i < l; i++) {
            out[j++] = toDigits[(0xF0 & data[i]) >>> 4];
            out[j++] = toDigits[0x0F & data[i]];
        }
        return out;
    }

}
