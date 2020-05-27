package com.cxa.base.utils;

import android.util.Base64;

import java.nio.charset.StandardCharsets;

import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.AES;

/**
 * describe: AES加密算法
 */

public class EncryptAES {

    public static String Encrypt(String data, String pass) {

        AES aes = new AES(Mode.CBC, Padding.ZeroPadding,
                new SecretKeySpec(pass.getBytes(), "AES"),
                pass.getBytes());
        byte[] result = aes.encrypt(data);
        return Base64.encodeToString(result,Base64.DEFAULT);
    }

    public static String desEncrypt(String data, String pass) {

        AES aes = new AES(Mode.CBC, Padding.NoPadding,
                new SecretKeySpec(pass.getBytes(), "AES"),
                new IvParameterSpec(pass.getBytes()));
        byte[] result = aes.decrypt(Base64.decode(data.getBytes(StandardCharsets.UTF_8),Base64.DEFAULT));
        return new String(result, StandardCharsets.UTF_8);
    }



}
