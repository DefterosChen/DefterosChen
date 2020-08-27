package com.cxa.base.net;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * volley工具
 * @author DeftrosChen
 *
 */
public class VolleyUtil {

    /**
     * Log or request TAG
     */
    public static final String TAG = "VolleyPatterns";
    /**
     * 提交给服务器的标识，识别是从PDA发起的请求
     */
    public static final String FROM_PDA_FLAG = "from_pda";
    /**
     * session超时异常
     */
    //public static final String ERROR_SESSION_TIMEOUT = "session_timeout";
    /**
     * session超时异常
     */
    public static final String ERROR_NOT_REGISTER = "抱歉，手机未注册！";

    private static RequestQueue mQueue ;

   // private static DefaultHttpClient httpClient;

    public static void initialize(Context context){
        if (mQueue == null){
            synchronized (VolleyUtil.class){
                if (mQueue == null){
                    mQueue = Volley.newRequestQueue(context) ;
                }
            }
        }
        mQueue.start();
    }

    public static RequestQueue getRequestQueue(){
        if (mQueue == null)
            throw new RuntimeException("volley没有初始化！！") ;
        return mQueue ;
    }

    /**
     * 添加指定标记的请求到队列中
     * 带标记的请求可用于取消请求操作
     * @param req
     * @param tag
     */
    public static <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);

       // VolleyLog.d("Adding request to queue: %s", req.getUrl());
        req.setRetryPolicy(new DefaultRetryPolicy(40 * 1000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mQueue.add(req);
    }
    /**
     * 添加默认的请求到队列中
     *
     * @param req
     */
    public static <T> void addToRequestQueue(Request<T> req) {
        // set the default tag if tag is empty
        req.setTag(TAG);
        /**
         * Volley中没有指定的方法来设置请求超时时间，可以设置RetryPolicy 来变通实现。
         * 1.第一个参数，可以设置超时时间。
         // 2.第二个参数，重试次数，确保最大重试次数为1，以保证超时后不重新请求。
         //如果你想失败后重新请求（因超时），您可以指定使用上面的代码，增加重试次数。
         //3.最后一个参数DEFAULT_BACKOFF_MULT，它允许你指定一个退避乘数可以用来实现“指数退避”来从RESTful服务器请求数据。
         //对于请求失败之后的请求，并不会隔相同的时间去请求Server，不会以线性的时间增长去请求，
         // 而是一个曲线增长，一次比一次长，如果backoff因子是2，当前超时为3，即下次再请求隔6S​
         */
        //req.setRetryPolicy(new DefaultRetryPolicy(40 * 1000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        req.setRetryPolicy(new DefaultRetryPolicy(40 * 1000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mQueue.add(req);
    }

    /**
     * 取消未处理或正在处理的请求
     *
     * @param tag 指定标记
     */
    public static void cancelPendingRequests(Object tag) {
        if (mQueue != null) {
            mQueue.cancelAll(tag);
        }
    }

}