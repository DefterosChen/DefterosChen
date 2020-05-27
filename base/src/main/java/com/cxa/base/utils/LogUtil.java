package com.cxa.base.utils;

import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 日志记录
 * Created by DeftrosChen
 */
public class LogUtil {
    static String TAG = LogUtil.class.getSimpleName();
    /**
     * 写日志信息
     * @param fileName
     * @param msg
     */
    public static void write(String fileName, String msg){
        fileName = SystemUtils.getLogPath() + "/" + fileName;
        File logFile = new File(fileName);
        FileOutputStream output = null;
        try{
            if (!logFile.exists()){
                logFile.createNewFile();
            }
            output = new FileOutputStream(logFile, true);
            msg = DateTimeUtil.getCurrentFormatDateTime() + "\n\r" + msg + "\n\r\n\r";
            output.write(msg.getBytes());
        }catch(Exception e){
            Log.d(TAG,"写日志出错！！");
        }finally{
            try{
                if (output != null) output.close();
            }catch(Exception e){
                Log.d(TAG,"写日志出错！！");
            }
        }
    }

    /**
     * 记录异常日志信息
     * @param fileName
     * @param e 异常对象
     */
    public static void write(String fileName, Exception e){
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        pw.close();
        String msg = sw.toString();
        write(fileName, msg);
    }

    public static void e(String tag, String msg) {
        Log.e(tag, msg);
    }

    public static void e(String tag, Throwable e) {
        Log.e(tag, e.getMessage(), e);
    }
}
