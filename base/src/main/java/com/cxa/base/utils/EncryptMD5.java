package com.cxa.base.utils;

import java.security.MessageDigest;

/**
 * MD5
 * Created by DeftrosChen on 2017-02-13.
 */

public class EncryptMD5 {
    /**
     * 加密字符串
     * @param input String
     * @return String
     * @throws Exception
     */

    public static String Encrypt(String input){
        try {
            return encrypt(input);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }





    /**
     * 加密字符串
     * @param str String 要加密的字符串
     * @return String 已加密字符串
     * @throws Exception
     */
    private static String encrypt(String str) throws Exception {
        char hexDigits[] = {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd',
                'e', 'f'};
        byte[] charTemp = str.getBytes("UTF-8");
        MessageDigest digest = MessageDigest.getInstance("MD5");
        digest.update(charTemp);
        byte[] md = digest.digest();
        int j = md.length;
        char charOut[] = new char[j * 2];
        int k = 0;
        for (int i = 0; i < j; i++) {
            byte byte0 = md[i];
            charOut[k++] = hexDigits[byte0 >>> 4 & 0xf];
            charOut[k++] = hexDigits[byte0 & 0xf];
        }
        return new String(charOut);
    }
}

