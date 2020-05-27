package com.cxa.base.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;
import android.os.StatFs;
import android.os.SystemClock;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import android.view.inputmethod.InputMethodManager;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * 基础工具类
 *
 * @author DeftrosChen
 */
public class BaseUtils {

    /**
     * 获取程序安装路径
     *
     * @return 程序安装路径
     */
    public static String getApplicationPath() {

//        String path = MyApplication.getInstance().getExternalFilesDir(null).toString();
//
//        return path;
        return Environment.getExternalStorageDirectory().getAbsolutePath();

    }

    /**
     * 判断存储卡是否存在
     * @return
     */
    public static boolean existSDCard() {
        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {
            return true;
        } else {
            return false;
        }
    }

    public static String getSdcardDir() {
        if (Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
            return Environment.getExternalStorageDirectory().toString();
        }
        return null;
    }
    /**
     * 获得SD卡总大小
     *
     * @return
     */
    public static String getSDTotalSize(Context context) {
        File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return Formatter.formatFileSize(context, blockSize * totalBlocks);
    }

    /**
     * 获得sd卡剩余容量，即可用大小
     *
     * @return
     */
    public static long getSDAvailableSize(Context context) {
        File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        //System.out.println("blockSize: "+blockSize +"  availableBlocks: "+availableBlocks);
        //System.out.println("blockSize:  availableBlocks: "+(blockSize * availableBlocks)/ 1024 / 1024);
        //return Formatter.formatFileSize(context, blockSize * availableBlocks);
        //System.out.println("path.getPath() == "+path.getPath() + " sd free : "+path.getFreeSpace()/ 1024 / 1024);
        //File root = Environment.getRootDirectory();
        //System.out.println("root path: "+root.getPath() + "   root free space: "+(root.getFreeSpace() / 1024 / 1024));
        return blockSize * availableBlocks;
    }

    /**
     * 获得机身内存总大小
     *
     * @return
     */
    public static String getRomTotalSize(Context context) {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return Formatter.formatFileSize(context, blockSize * totalBlocks);
    }

    /**
     * 获得机身可用内存
     *
     * @return
     */
    public static String getRomAvailableSize(Context context) {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return Formatter.formatFileSize(context, blockSize * availableBlocks);
    }

    /**
     * 获取PDA唯一标识码
     *
     * @return PDA唯一标识码
     */
    @SuppressLint("MissingPermission")
    public static String getIMEI_NUMBER(Context context) {
        return ((TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
    }

    /**
     * 获取手机号码
     * 注意，手机号码不是所有的都能获取。只是有一部分可以拿到。
     * 这个是由于移动运营商没有把手机号码的数据写入到sim卡中。
     * 这个就像是一个变量，当移动运营商为它赋值了，它自然就会有值。不赋值自然为空
     * @param context
     * @return
     */
    @SuppressLint("MissingPermission")
    public static String getTelephoneNumber(Context context) {
        return ((TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE)).getLine1Number();
    }


    /**
     * 得到版本号
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pi.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return "未知版本";
        }
    }

    /**
     * 修改系统时间
     *
     * @param pcDate 时间
     */
    public static void setSystemClock(Date pcDate) {
        SystemClock.setCurrentTimeMillis(pcDate.getTime());
    }

    /**
     * 判断是否登录
     * @param context
     * @return true-已经登录过 false-未登录
     */
    public static boolean isLogin(Context context) {
        //取sharedpreferences中数据的代码
        String userJson = null;
        SharedPreferences sharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        userJson = sharedPreferences.getString("userJson", "");
        if (userJson != null && !userJson.trim().equals("")) {
            return true;
        }
        return false;
    }

    /**
     * 保存登录信息
     *
     * @param context
     * @param userJson
     */
    public static void saveLogin(Context context, String userJson) {
        //SharedPreferences 保存数据的实现代码
        SharedPreferences sharedPreferences =
                context.getSharedPreferences("user", Context.MODE_PRIVATE);
        Editor editor = sharedPreferences.edit();
        //如果不能找到Editor接口。尝试使用 SharedPreferences.Editor
        editor.putString("userJson", userJson);

        editor.commit();
    }

    /**
     * 得到用户信息
     *
     * @param context
     * @return
     */
    public static String getLoginUser(Context context) {
        //取sharedpreferences中数据的代码
        SharedPreferences sharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        String userJson = sharedPreferences.getString("userJson", "");
        System.out.println("userJson+++:"+userJson);
        return userJson;
    }

    /**
     * 注销用户
     * @param context
     */
    public static void removeLogin(Context context) {
        SharedPreferences sharedPreferences =
                context.getSharedPreferences("user", Context.MODE_PRIVATE);
        Editor editor = sharedPreferences.edit();
        editor.putString("userJson", "");
        editor.commit();
    }

    /**
     * 得到配置的ip地址
     * @param context
     * @return
     */
    public static String getIP(Context context) {
        //取sharedpreferences中数据的代码
        SharedPreferences sharedPreferences = context.getSharedPreferences("remote_address", Context.MODE_PRIVATE);
        String ip = sharedPreferences.getString("ip", "");
        return ip;
    }

    /**
     * 保存配置的ip地址
     * @param context
     * @param ip
     */
    public static void saveIP(Context context, String ip) {
        //SharedPreferences 保存数据的实现代码
        SharedPreferences sharedPreferences =
                context.getSharedPreferences("remote_address", Context.MODE_PRIVATE);
        Editor editor = sharedPreferences.edit();
        //如果不能找到Editor接口。尝试使用 SharedPreferences.Editor
        editor.putString("ip", ip);

        editor.commit();
    }
    /**
     * 发送短信
     *
     * @param mobile  接收方的手机号码
     * @param message 信息内容
     */
    public static void sendSMS(String mobile, String message) {
        if (mobile == null || mobile.trim().equals(""))
            return;
        // 移动运营商允许每次发送的字节数据有限，使用Android提供的短信工具
        SmsManager smsManager = SmsManager.getDefault();
        // 如果短信没有超过限制长度，则返还一个长度的List
        List<String> texts = smsManager.divideMessage(message);
        for (String text : texts) {
            // 第一个参数：接收方的手机号码
            // 第二个参数：发送方的手机号码
            // 第三个参数：信息内容
            // 第四个参数：发送是否成功的回执
            // 第五个参数：结束是否成功的回执
            smsManager.sendTextMessage(mobile, null, text, null, null);
        }
    }

    public static String getRandom(int strLength){
        Random rm = new Random();

        // 获得随机数
        double pross = (1 + rm.nextDouble()) * Math.pow(10, strLength);

        // 将获得的获得随机数转化为字符串
        String fixLenthString = String.valueOf(pross);

        // 返回固定的长度的随机数
        return fixLenthString.substring(1, strLength + 1);
    }



    /**
     * 隐藏键盘的方法
     *
     * @param context
     */
    public static void hideKeyboard(Activity context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        // 隐藏软键盘
        imm.hideSoftInputFromWindow(context.getWindow().getDecorView().getWindowToken(), 0);


    }


}
