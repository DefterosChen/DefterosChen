package com.cxa.base;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.cxa.base.system.CrashHandler;
import com.cxa.base.widget.waterfall.collector.config.AppInitManager;
import com.hjq.toast.ToastInterceptor;
import com.hjq.toast.ToastUtils;
import com.horizon.doodle.Doodle;
import com.kky.healthcaregardens.common.activity.WelcomeActivity;
import com.kky.healthcaregardens.common.base.system.CrashHandler;
import com.kky.healthcaregardens.common.widget.waterfall.collector.config.AppInitManager;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.squareup.leakcanary.LeakCanary;
import com.xuexiang.xpage.AppPageConfig;
import com.xuexiang.xpage.PageConfig;
import com.xuexiang.xpage.PageConfiguration;
import com.xuexiang.xpage.base.XPageActivity;
import com.xuexiang.xpage.base.XPageSimpleListFragment;
import com.xuexiang.xpage.model.PageInfo;
import com.xuexiang.xrouter.launcher.XRouter;
import com.xuexiang.xui.XUI;
import com.xuexiang.xutil.XUtil;

import java.util.List;

import cat.ereza.customactivityoncrash.CustomActivityOnCrash;
import cat.ereza.customactivityoncrash.config.CaocConfig;

//import com.kky.baselibrary.waterfall.collector.config.AppInitManager;

//import com.hjq.demo.ui.activity.CrashActivity;

/**
 * 项目中的 Application 基类
 */
public final class MyApplication extends Application {

    private static Context context;


    //方便在任意位置得到Context
    private static MyApplication myApplication;

    public static MyApplication getInstance() {
        return myApplication;
    }


    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    private double longitude = 0.0; //经度
    private double latitude = 0.0;  //纬度

//    public LocationClient mLocationClient;//定位客户端
//    public MyLocationListener mMyLocationListener;//监听器
//    public TextView mLocationResult,logMsg;//这是返回的结果，当前你也可以用String 来存储了

    @SuppressLint("RestrictedApi")
    @Override
    public void onCreate() {
        super.onCreate();

        // 进行一些必要的初始化
        AppInitManager.initApplication(this);

        CustomActivityOnCrash.install(this);
        //获取Context
        context = getApplicationContext();
        myApplication = this;

        // 开启系统运行异常处理
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext(),this);


        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            System.out.println("没有读取SD卡数据");
            return;
        }
        System.out.println("读取到了SD卡数据");

//        //创建程序初始化目录
//        SystemUtils.createInitDir();
//
//        //初始化数据库
//        SystemUtils.createDatabase(this);

        initUI();

        initSDK(this);


//        SDKInitializer.initialize(getApplicationContext());
//        mLocationClient = new LocationClient(this.getApplicationContext());
//        mMyLocationListener = new MyLocationListener();
//        mLocationClient.registerLocationListener(mMyLocationListener);
    }


    /**
     * 初始化XUI 框架
     */
    private void initUI() {

        XUI.init(this);
        XUI.debug(MyApplication.isDebug());

        XUtil.init(this);
        XUtil.debug(true);
//        initXRouter();
//
//        //设置默认字体为华文行楷
        XUI.getInstance().initFontStyle("fonts/hwxk.ttf");
        PictureFileUtils.setAppName("xui");

        PageConfig.getInstance()
                .setPageConfiguration(new PageConfiguration() { //页面注册
                    @Override
                    public List<PageInfo> registerPages(Context context) {
                        //自动注册页面,是编译时自动生成的，build一下就出来了
                        return AppPageConfig.getInstance().getPages();
                    }
                })
                .debug("PageLog")       //开启调试
                .enableWatcher(false)   //设置是否开启内存泄露监测
                .setContainActivityClazz(XPageActivity.class) //设置默认的容器Activity
                .init(this);   //初始化页面配置

//        initRouter();

        //字体图标库
//        Iconics.init(this);
//        //这是自己定义的图标库
//        Iconics.registerFont(new XUIIconFont());

//        CameraView.setICameraStrategy(new AutoCameraStrategy(1920 * 1080));
    }

    public static boolean isDebug() {
        return BuildConfig.DEBUG;
    }

    /**
     * 初始化一些第三方框架
     */
    public static void initSDK(Application application) {
        // 这个过程专门用于堆分析的 leak 金丝雀
        // 你不应该在这个过程中初始化你的应用程序
        if (LeakCanary.isInAnalyzerProcess(application)) {
            return;
        }

        // 内存泄漏检测
        LeakCanary.install(application);

//        // 友盟统计、登录、分享 SDK
//        UmengClient.init(application);

        // 设置 Toast 拦截器
        ToastUtils.setToastInterceptor(new ToastInterceptor() {
            @Override
            public boolean intercept(Toast toast, CharSequence text) {
                boolean intercept = super.intercept(toast, text);
                if (intercept) {
                    Log.e("Toast", "空 Toast");
                } else {
                    Log.i("Toast", text.toString());
                }
                return intercept;
            }
        });
        // 吐司工具类
        ToastUtils.init(application);

        // 图片加载器
//        ImageLoader.init(application);


        // Bugly 异常捕捉 cxa
//        CrashReport.initCrashReport(application, BuildConfig.BUGLY_ID, false);

        // Crash 捕捉界面
        CaocConfig.Builder.create()
                .backgroundMode(CaocConfig.BACKGROUND_MODE_SHOW_CUSTOM)
                .enabled(true)
                .trackActivities(true)
                .minTimeBetweenCrashesMs(2000)
                // 重启的 Activity
                .restartActivity(MainActivity.class)
                // 错误的 Activity
                .errorActivity(WelcomeActivity.class)
                // 设置监听器
                //.eventListener(new YourCustomEventListener())
                .apply();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        // 使用 Dex分包
//        MultiDex.install(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        System.exit(0); //释放全局资源
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);

//        if (GlobalConfig.DEBUG) {
//            Log.i(TAG, "onTrimMemory ... level:" + level);
//        }

        Doodle.trimMemory(level);
    }



    //static 代码段可以防止内存泄露
    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white);//全局设置主题颜色
                return new ClassicsHeader(context);
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context).setDrawableSize(20);
            }
        });
    }


    private void initXRouter() {
        if (isDebug()) {           // 这两行必须写在init之前，否则这些配置在init过程中将无效
            XRouter.openLog();     // 打印日志
            XRouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        XRouter.init(this);
    }

    /**
     * 增加组件信息和子演示页信息
     *
     * @param clazz 【继承了ListSimpleFragment的类】
     */
    private void addPageInfoAndSubPages(List<PageInfo> pageInfos, Class<? extends XPageSimpleListFragment> clazz) {
        pageInfos.add(PageConfig.getPageInfo(clazz));
        try {
            registerPageInfos(pageInfos, clazz.newInstance().getSimplePageClasses());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 注册多个页面信息
     *
     * @param clazz
     * @return
     */
    private void registerPageInfos(List<PageInfo> pageInfos, Class... clazz) {
        for (Class aClazz : clazz) {
            pageInfos.add(PageConfig.getPageInfo(aClazz));
        }
    }

    private void initRouter() {
        // 这两行必须写在init之前，否则这些配置在init过程中将无效
        if (BuildConfig.DEBUG) {
            XRouter.openLog();     // 打印日志
            XRouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        XRouter.init(this);
    }






//    /**
//     * 实现实时位置回调监听
//     */
//    public class MyLocationListener implements BDLocationListener {
//
//
//        @Override
//        public void onReceiveLocation(BDLocation location) {
//            //Receive Location
//            StringBuffer sb = new StringBuffer(256);
//            sb.append("time : ");
//            sb.append(location.getTime());
//            sb.append("\nerror code : ");
//            sb.append(location.getLocType());
//            sb.append("\nlatitude : ");
//            sb.append(location.getLatitude());
//            sb.append("\nlontitude : ");
//            sb.append(location.getLongitude());
//            sb.append("\nradius : ");
//            sb.append(location.getRadius());
//            if (location.getLocType() == BDLocation.TypeGpsLocation){// GPS定位结果
//                sb.append("\nspeed : ");
//                sb.append(location.getSpeed());// 单位：公里每小时
//                sb.append("\nsatellite : ");
//                sb.append(location.getSatelliteNumber());
//                sb.append("\nheight : ");
//                sb.append(location.getAltitude());// 单位：米
//                sb.append("\ndirection : ");
//                sb.append(location.getDirection());
//                sb.append("\naddr : ");
//                sb.append(location.getAddrStr());
//                sb.append("\ndescribe : ");
//                sb.append("gps定位成功");
//
//
//            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation){// 网络定位结果
//                sb.append("\naddr : ");
//                sb.append(location.getCity());
//                //运营商信息
//                sb.append("\noperationers : ");
//                sb.append(location.getOperators());
//                sb.append("\ndescribe : ");
//                sb.append("网络定位成功");
//            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
//                sb.append("\ndescribe : ");
//                sb.append("离线定位成功，离线定位结果也是有效的");
//            } else if (location.getLocType() == BDLocation.TypeServerError) {
//                sb.append("\ndescribe : ");
//                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
//            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
//                sb.append("\ndescribe : ");
//                sb.append("网络不同导致定位失败，请检查网络是否通畅");
//            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
//                sb.append("\ndescribe : ");
//                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
//            }
//
////            XToastUtils(sb.toString());
//            Log.i("BaiduLocationApiDem", sb.toString());
//            // mLocationClient.setEnableGpsRealTimeTransfer(true);
//        }
//    }


//    /**
//     * 显示请求字符串
//     * @param str
//     */
//    public void logMsg(String str) {
//        try {
//            if (mLocationResult != null)
//                mLocationResult.setText(str);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }








    //返回
    public static Context getContextObject() {
        return context;
    }



}