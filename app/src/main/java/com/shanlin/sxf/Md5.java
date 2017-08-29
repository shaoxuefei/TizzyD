package com.shanlin.sxf;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by zhangting on 2017/4/18.
 */

public class Md5 {

    public static byte[] getMD5Digest(String data) {

        byte[] bytes = null ;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5") ;
            bytes = md.digest(data.getBytes("UTF-8")) ;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return bytes ;
    }

    public static String byte2hex(byte[] bytes) {

        StringBuilder stringBuilder = new StringBuilder() ;
        for (int i=0; i<bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF) ;
            if (hex.length() == 1) {
                stringBuilder.append("0") ;
            }
            stringBuilder.append(hex.toUpperCase()) ;
        }
        return stringBuilder.toString() ;
    }
}

