

package com.cxa.base.utils;


import android.widget.Toast;

import androidx.annotation.StringRes;

import com.cxa.base.config.GlobalConfig;

public class ToastUtil {
    public static void showTips(String tips){
        Toast.makeText(GlobalConfig.getAppContext(), tips, Toast.LENGTH_SHORT).show();
    }



    public static void showTips(@StringRes int resID){
        Toast.makeText(GlobalConfig.getAppContext(), ResUtil.getStr(resID), Toast.LENGTH_SHORT).show();
    }
}
