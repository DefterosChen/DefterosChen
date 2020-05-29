package com.cxa.base.widget.waterfall.collector.config;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import com.horizon.collector.setting.model.UserSetting;
import com.horizon.task.base.LogProxy;
import com.kky.healthcaregardens.common.base.utils.LogUtil;
import com.kky.healthcaregardens.common.widget.waterfall.base.config.GlobalConfig;
import com.kky.healthcaregardens.common.widget.waterfall.collector.util.UncaughtExceptionInterceptor;

import java.util.List;

public class AppInitManager {
    public static void initApplication(Application context) {
        if (AppConfig.APPLICATION_ID.equals(getProcessName(context))) {
            // 先初始化AppContext
            GlobalConfig.setAppContext(context);

            UncaughtExceptionInterceptor.getInstance().init();

            UserSetting.INSTANCE.getData();

            LogProxy.INSTANCE.init(GlobalLogger.getInstance());

//            Doodle.config()
//                    .setUserAgent(HttpClient.USER_AGENT)
//                    .setGifDecoder(new GifDecoder() {
//                        @NotNull
//                        @Override
//                        public Drawable decode(@NotNull byte[] bytes) throws Exception {
//                            return new GifDrawable(bytes);
//                        }
//                    });
        }
    }

    private static String getProcessName(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (am != null) {
            List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
            if (runningApps != null) {
                final int curPid = android.os.Process.myPid();
                for (ActivityManager.RunningAppProcessInfo runningApp : runningApps) {
                    if (runningApp.pid == curPid) {
                        return runningApp.processName;
                    }
                }
            }
        } else {
            LogUtil.e("AppInitManager", "Get ActivityManager service failed.");
        }
        return AppConfig.APPLICATION_ID;
    }

}
