package com.cxa.base.utils;

import android.util.Base64;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;


public class EncyptDES {
    private final static String Algorithm = "DES";
    private final static String BYTE_ENCODE = "UTF-8";
    // 加密密匙,必须是8位的倍数
    private final static String ENCODE = "ab7_eric";

    public static String encrypt(String input){
        try {
            SecureRandom random = new SecureRandom();
            DESKeySpec desKey = new DESKeySpec(ENCODE.getBytes(BYTE_ENCODE));
            // 创建一个密匙工厂，然后用它把DESKeySpec转换成
            SecretKeyFactory keyFactory = SecretKeyFactory
                    .getInstance(Algorithm);
            SecretKey securekey = keyFactory.generateSecret(desKey);
            // Cipher对象实际完成加密操作
            Cipher cipher = Cipher.getInstance(Algorithm);
            // 用密匙初始化Cipher对象
            cipher.init(Cipher.ENCRYPT_MODE, securekey, random);
            // 现在，获取数据并加密
            // 正式执行加密操作
            byte[] str = cipher.doFinal(input.getBytes(BYTE_ENCODE));
            return new String(Base64.encode(str, Base64.DEFAULT));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("des加密出错！");
        }
        return null;
    }

    public static String decrypt(String input)  {
        try{
            // DES算法要求有一个可信任的随机数源
            SecureRandom random = new SecureRandom();
            // 创建一个DESKeySpec对象
            DESKeySpec desKey = new DESKeySpec(ENCODE.getBytes(BYTE_ENCODE));
            // 创建一个密匙工厂
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(Algorithm);
            // 将DESKeySpec对象转换成SecretKey对象
            SecretKey securekey = keyFactory.generateSecret(desKey);
            // Cipher对象实际完成解密操作
            Cipher cipher = Cipher.getInstance(Algorithm);
            // 用密匙初始化Cipher对象
            cipher.init(Cipher.DECRYPT_MODE, securekey, random);
            // 真正开始解密操作
            byte[] dec = Base64.decode(input, Base64.DEFAULT);
            return new String(cipher.doFinal(dec), BYTE_ENCODE);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("des解密出错！");
            LogUtil.write("des.log",e);
        }
        return null;
    }

    /**
     * 加密文件
     * @param in
     * @param savePath 加密后保存的位置
     * @throws
     * @throws Exception
     */
    public static void encryptFile(InputStream in, String savePath) throws Exception {
        if(in==null) {
            throw new FileNotFoundException("未找到文件！");
        }
        SecureRandom random = new SecureRandom();
        DESKeySpec desKey = new DESKeySpec(ENCODE.getBytes(BYTE_ENCODE));
        // 创建一个密匙工厂，然后用它把DESKeySpec转换成
        SecretKeyFactory keyFactory = SecretKeyFactory
                .getInstance(Algorithm);
        SecretKey securekey = keyFactory.generateSecret(desKey);
        Cipher cipher = Cipher.getInstance(Algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, securekey,random);
        CipherInputStream cin = new CipherInputStream(in, cipher);
        OutputStream os = new FileOutputStream(savePath);
        byte[] bytes = new byte[1024];
        int len = -1;
        while((len=cin.read(bytes))>0) {
            os.write(bytes, 0, len);
            os.flush();
        }
        os.close();
        cin.close();
        in.close();
        //System.out.println("文件加密成功");
    }

    /**
     * 加密文件
     * @param filePath 需要加密的文件路径
     * @param savePath 加密后保存的位置
     * @throws FileNotFoundException
     */
    public static void encryptFile(String filePath, String savePath) throws Exception {
        encryptFile(new FileInputStream(filePath), savePath);
    }

    /**
     * 解密文件
     * @param in
     */
    public void decryptFile(InputStream in, String savePath)  throws Exception {
        if(in==null) {
            throw new FileNotFoundException("未找到文件！");
        }
        // DES算法要求有一个可信任的随机数源
        SecureRandom random = new SecureRandom();
        // 创建一个DESKeySpec对象
        DESKeySpec desKey = new DESKeySpec(ENCODE.getBytes(BYTE_ENCODE));
        // 创建一个密匙工厂
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(Algorithm);
        // 将DESKeySpec对象转换成SecretKey对象
        SecretKey securekey = keyFactory.generateSecret(desKey);
        Cipher cipher = Cipher.getInstance(this.Algorithm);
        cipher.init(Cipher.DECRYPT_MODE, securekey,random);
        CipherInputStream cin = new CipherInputStream(in, cipher);
        OutputStream os = new FileOutputStream(savePath);
        byte[] bytes = new byte[1024];
        int len = -1;
        while((len=cin.read(bytes))>0) {
            os.write(bytes, 0, len);
            os.flush();
        }
        os.close();
        cin.close();
        in.close();
        //System.out.println("文件解密成功");
    }
    /**
     * 解密文件
     * @param filePath  文件路径
     * @throws Exception
     */
    public void decryptFile(String filePath, String savePath) throws Exception {
        decryptFile(new FileInputStream(filePath),savePath);
    }

}
