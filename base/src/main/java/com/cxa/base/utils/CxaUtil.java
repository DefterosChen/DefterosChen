package com.cxa.base.utils;
/*
 * Copyright (C) 2018 xuexiangjys(xuexiangjys@163.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.cxa.base.app.ActivityLifecycleHelper;
import com.cxa.base.logger.Logger;


/**
 * <pre>
 *     desc   : 全局工具管理
 *     author : xuexiang
 *     time   : 2018/4/27 下午8:38
 * </pre>
 */
public final class CxaUtil{
    private static Context sContext;
    private static CxaUtil sInstance;

    private ActivityLifecycleHelper mActivityLifecycleHelper;
    /**
     * 主线程Handler
     */
    private static final Handler sMainHandler = new Handler(Looper.getMainLooper());

    private CxaUtil() {
        mActivityLifecycleHelper = new ActivityLifecycleHelper();
    }

    /**
     * 初始化工具
     * @param application
     */
    public static void init(Application application) {
        sContext = application.getApplicationContext();
        application.registerActivityLifecycleCallbacks(CxaUtil.get().getActivityLifecycleHelper());
    }

    /**
     * 初始化工具
     * @param context
     */
    public static void init(Context context) {
        sContext = context.getApplicationContext();
    }

    /**
     * 注册activity的生命回调
     * @param application
     * @param lifecycleHelper
     * @return
     */
    public CxaUtil registerLifecycleCallbacks(Application application, ActivityLifecycleHelper lifecycleHelper) {
        mActivityLifecycleHelper = lifecycleHelper;
        application.registerActivityLifecycleCallbacks(mActivityLifecycleHelper);
        return this;
    }

    /**
     * 获取全局上下文
     * @return
     */
    public static Context getContext() {
        testInitialize();
        return sContext;
    }

    public ActivityLifecycleHelper getActivityLifecycleHelper() {
        return mActivityLifecycleHelper;
    }

    private static void testInitialize() {
        if (sContext == null) {
            throw new ExceptionInInitializerError("请先在全局Application中调用 CxaUtil.init() 初始化！");
        }
    }

    /**
     * 设置日志记录
     */
    public static void debug(boolean isDebug) {
        if (isDebug) {
            debug(Logger.DEFAULT_LOG_TAG);
        } else {
            debug("");
        }
    }

    /**
     * 设置日志记录
     * @param tag
     */
    public static void debug(String tag) {
        Logger.debug(tag);
    }

    /**
     * 获取主线程的Handler
     * @return
     */
    public static Handler getMainHandler() {
        return sMainHandler;
    }

    /**
     * 在主线程中执行
     * @param runnable
     * @return
     */
    public static boolean runOnUiThread(Runnable runnable) {
        return getMainHandler().post(runnable);
    }


    /**
     * 获取实例
     * @return
     */
    public static CxaUtil get() {
        if (sInstance == null) {
            synchronized (CxaUtil.class) {
                if (sInstance == null) {
                    sInstance = new CxaUtil();
                }
            }
        }
        return sInstance;
    }

    /**
     * 退出程序
     */
    public void exitApp() {
        if (mActivityLifecycleHelper != null) {
            mActivityLifecycleHelper.exit();
        }
        ServiceUtils.stopAllRunningService(getContext());
        ProcessUtils.killBackgroundProcesses(getContext().getPackageName());
        System.exit(0);
    }


}
