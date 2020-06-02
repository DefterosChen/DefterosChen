package com.cxa.base.server;

import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

/**
 * 基础服务
 * Created by Eric on 2016-07-08.
 */
public class BaseService {

    private static BaseService baseService;
    private Context context = null;
    private int count = 0; //计数器

    private BaseService(){}

    public static BaseService getInstance(){
        if(baseService==null){
            baseService = new BaseService();
        }
        return baseService;
    }
    /**
     * 初始化基础数据到本地数据库
     */
    public void initBaseData(Context context) {
        this.context = context;
        count = 0; //计数器

        showMsg("开始同步...");
    }

    //消息计数器回调
    private void endMsg(int i) {
        System.out.println("消息计数器回调: "+i);
        if(i==4){
            showMsg("同步已完成！");
        }
    }


    private void showMsg(final String msg){
        if(context!=null){
            new Thread() {
                @Override
                public void run() {
                    Looper.prepare();
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
            }.start();
        }
    }



}
