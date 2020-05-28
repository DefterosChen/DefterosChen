package com.cxa.base.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;

/**
 * 业务系统工具
 * Created by DeftrosChen
 */
public class SystemUtils {
    /**
     * 业务系统名称
     */
    private final static String SYSTEM_NAME = "kyy";
    /**
     * 数据库名称
     */
    public final static String DATABASE_FILENAME = "kyy.s3db";

    public static Context context;



    /**
     * 创建程序初始化目录
     */
    public static void createInitDir() {

//        //根目录
//        String rootPath = Environment.getDataDirectory().getParentFile().getAbsolutePath()+ "" + SYSTEM_NAME;



        String rootPath = getRootPath();
        System.out.println("获取到的文件夹目录是在"+ rootPath);
        File cpmisDir = new File(rootPath);
        if (!cpmisDir.exists()) {
            System.out.println("创建该主目录");

            if (cpmisDir.mkdirs()) {// 创建文件夹
                System.out.println("主目录创建成功");
            } else {
                System.out.println("主目录创建失败 ");
            }


        }
        //数据库文件目录
        File databaseDir = new File(rootPath + "/database");
        if (!databaseDir.exists()) {
            System.out.println("创建该数据库目录");
//            databaseDir.mkdirs();
            if (databaseDir.mkdir()) {// 创建文件夹
                System.out.println("数据库创建成功");
            } else {
                System.out.println("数据库创建失败 ");
            }
        }
        //导入文件目录
        File importDir = new File(rootPath + "/import");
        if (!importDir.exists()) {
            System.out.println("创建该导入目录");
            importDir.mkdirs();
        }
        //导出文件目录
        File exportDir = new File(rootPath + "/export");
        if (!exportDir.exists()) {
            System.out.println("创建该导出目录");
            exportDir.mkdirs();
        }
        //日志文件目录
        File logDir = new File(rootPath + "/log");
        if (!logDir.exists()) {
            System.out.println("创建日志目录");
            logDir.mkdirs();
        }
    }

    public static String getRootPath() {
        return BaseUtils.getApplicationPath() + "/" + SYSTEM_NAME;
    }

    /**
     * 得到下载的APK文件的路径
     *
     * @return
     */
    public static String getDownloadAPKFliePath() {
        String filePath = BaseUtils.getApplicationPath() + "/" + SYSTEM_NAME + "/downloadAPK";
        File projectDir = new File(filePath);
        if (!projectDir.exists()) {
            projectDir.mkdirs();
        }
        return filePath;
    }


//    /**
//     * 写入数据库
//     *
//     * @param context
//     */
//    public static void createDatabase(Context context) {
//        // 数据库路径
//        String databaseFileName = getDatabasePath() + "/" + DATABASE_FILENAME;
//        // 写入数据库
//        File file = new File(databaseFileName);
//        if (!file.exists()) {
//            InputStream is = null;
//            FileOutputStream fos = null;
//            try {
//                is = context.getResources().openRawResource(R.raw.kky);
//                fos = new FileOutputStream(databaseFileName);
//                byte[] buffer = new byte[8192];
//                int count = 0;
//                while ((count = is.read(buffer)) > 0) {
//                    fos.write(buffer, 0, count);
//                }
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            } finally {
//                try {
//                    // 关闭IO流
//                    if (fos != null) {
//                        fos.flush();
//                        fos.close();
//                    }
//                    if (is != null) {
//                        is.close();
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//            //初始化基础数据
//            BaseService.getInstance().initBaseData(null);
//        }
//    }

    /**
     * 获取import文件夹路径
     *
     * @return 导入文件夹路径
     */
    public static String getImportPath() {
        return BaseUtils.getApplicationPath() + "/" + SYSTEM_NAME + "/import";
    }

    /**
     * 获取export文件夹路径
     *
     * @return 导出文件夹路径
     */
    public static String getExportPath() {
        return BaseUtils.getApplicationPath() + "/" + SYSTEM_NAME + "/export";
    }

    /**
     * 获取log文件夹路径
     *
     * @return log文件夹路径
     */
    public static String getLogPath() {
        return BaseUtils.getApplicationPath() + "/" + SYSTEM_NAME + "/log";
    }

    /**
     * 获取database文件夹路径
     *
     * @return 数据库路径
     */
    public static String getDatabasePath() {
        return BaseUtils.getApplicationPath() + "/" + SYSTEM_NAME + "/database";
    }

    /**
     * 获取database备份文件夹路径
     *
     * @return 备份数据库路径
     */
    public static String getDatabaseBackPath() {
        return BaseUtils.getApplicationPath() + "/" + SYSTEM_NAME + "/database_back";
    }

    /**
     * 调用系统照相机程序
     *
     * @param activity
     * @param filePath 相对路径，比如 a/b/
     * @param fileName 保存的照片名（不带路径）
     */
    public static void transferSysCamera(Activity activity, String filePath, String fileName) {
        // 设置照片输出路径
        filePath = getExportPath() + "/" + filePath;
        if (!new File(filePath).exists()) {
            new File(filePath).mkdirs(); //创建文件夹
        }
        String out = filePath + "/" + fileName;
        Uri uri = Uri.fromFile(new File(out));

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        activity.startActivityForResult(intent, 2);
    }





}
