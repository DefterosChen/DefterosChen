
package com.cxa.base.utils;

import androidx.annotation.StringRes;


public class ResUtil {

    public static String getStr(@StringRes int resId) {
        return GlobalConfig.getAppContext().getString(resId);
    }
    public static String getStr(@StringRes int resId, Object... formatArgs){
        return GlobalConfig.getAppContext().getString(resId, formatArgs);
    }

}
