package com.cxa.base.system;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.widget.Toast;

import com.cxa.base.utils.DateTimeUtil;
import com.cxa.base.utils.SystemUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * UncaughtException处理类，当程序发送Uncaught异常的时候，有该类来接管程序，并记录发送错误报告。
 */
public class CrashHandler implements UncaughtExceptionHandler {

    // 系统默认的UncaughtException处理类
    private UncaughtExceptionHandler mDefaultHandler;
    // CrashHandler实例
    private static CrashHandler INSTANCE = new CrashHandler();
    // 保存Context对象
    private Context mContext;
    private Application app;


    // 用来存储设备信息和异常信息
    private Map<String, String> infos = new HashMap<String, String>();

    /**
     * 保证只有一个CrashHandler实例
     */
    private CrashHandler() {

    }

    /**
     * 获取CrashHandler实例，单例模式
     *
     * @return
     */
    public static CrashHandler getInstance() {
        return INSTANCE;
    }

    /**
     * 初始化
     *
     * @param context
     */
    public void init(Context context, Application app) {
        mContext = context;
        this.app = app;
        // 获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        // 设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            // 如果用户没有处理，则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            try {
                Thread.sleep(3300);
            } catch (Exception e) {
                e.printStackTrace();
            }
//             退出程序
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
//             当app退出的时候，会执行onTerminate方法，但是有时候不会主动执行。
//             那么强制执行这个方法
            app.onTerminate();
        }
    }


    /**
     * 自定义错误处理，收集错误信息 发送错误报告等操作均再次完成
     *
     * @param ex
     * @return true：如果处理了该异常信息；否则返回false。
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        // 使用Toast来显示异常信息
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(mContext, "很抱歉，程序出现异常，即将退出。", Toast.LENGTH_LONG)
                        .show();
                Looper.loop();
            }
        }.start();
        // 收集设备参数信息
        collectDeviceInfo(mContext);
        // 保存日志文件
        saveCrashInfo2File(ex);
        return true;
    }

    /**
     * 收集设备参数信息
     *
     * @param context
     */
    public void collectDeviceInfo(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null"
                        : pi.versionName;
                String versionCode = pi.versionCode + "";
                infos.put("versionName", versionName);
                infos.put("versionCode", versionCode);
            }
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                infos.put(field.getName(), field.get(null).toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 保存错误信息到文件中
     *
     * @param ex
     * @return 返回文件名称，便于将文件传送到服务器
     */
    private void saveCrashInfo2File(Throwable ex) {
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : infos.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key + "=" + value + "\n");
        }

        Writer writer = new StringWriter();
        PrintWriter pw = new PrintWriter(writer);
        ex.printStackTrace(pw);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(pw);
            cause = cause.getCause();
        }
        pw.close();
        String result = writer.toString();
        sb.append(result);
        try {
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            String time = formatter.format(new Date());
            String fileName = "crash-" + time + ".log";
            if (Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                String path = SystemUtils.getLogPath();
                File dir = new File(path);
                if (!dir.exists()) {
                    dir.mkdir();
                }
                FileOutputStream fos = new FileOutputStream(path + "/" + fileName,true);
                String msg = DateTimeUtil.getCurrentFormatDateTime() + "\n\r" + sb.toString() + "\n\r\n\r";
                fos.write(msg.getBytes());
                fos.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
