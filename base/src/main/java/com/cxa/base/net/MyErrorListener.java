package com.cxa.base.net;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

/**
 * 自定义Volley异常处理
 * Created by DeftrosChen on 2016-06-15.
 */
public class MyErrorListener implements Response.ErrorListener {

    private Context context;
    Handler handler = null;

    public MyErrorListener(Context context){
        this.context = context;
    }
    public MyErrorListener(Context context, Handler handler){
        this.context = context;
        this.handler = handler;
    }
    @Override
    public void onErrorResponse(final VolleyError error) {
        if(handler!=null){
            Message msg = new Message();
            msg.what = 2;
            handler.sendMessage(msg);
        }
       /* if(error.getMessage() != null && error.getMessage().equals("session_timeout")){
           // ServiceCall.loginAgain(context);//会话超时，重新登录
        }else {*/
            new Thread() {
                @Override
                public void run() {
                    String str = new VolleyErrorHelper().getMessage(error);
                    Looper.prepare();
                    Toast.makeText(context, str , Toast.LENGTH_LONG).show();
                    Looper.loop();
                }
            }.start();
        //}
    }
    
    /*
    * 以下是Volley的异常列表：
    * AuthFailureError：如果在做一个HTTP的身份验证，可能会发生这个错误。
    * NetworkError：Socket关闭，服务器宕机，DNS错误都会产生这个错误。
    * NoConnectionError：和NetworkError类似，这个是客户端没有网络连接。
    * ParseError：在使用JsonObjectRequest或JsonArrayRequest时，如果接收到的JSON是畸形，会产生异常。
    * SERVERERROR：服务器的响应的一个错误，最有可能的4xx或5xx HTTP状态代码。
    * TimeoutError：Socket超时，服务器太忙或网络延迟会产生这个异常。默认情况下，Volley的超时时间为2.5秒。如果得到这个错误可以使用RetryPolicy。
    */
    private class VolleyErrorHelper {
        String generic_server_down = "连接服务器失败！";
        String no_internet = "无网络连接！";
        String generic_error = "网络异常,请稍后再试！";
        String server_error = "服务器端异常！";

        /**
         * Returns appropriate message which is to be displayed to the user against
         * the specified error object.
         *
         * @param error
         * @return
         */
        public String getMessage(Object error) {
            if (error instanceof TimeoutError) {
                return generic_server_down;
            } else if (isServerProblem(error)) {
                return handleServerError(error);
            } else if (isNetworkProblem(error)) {
                return no_internet;
            }
            return generic_error;
        }

        /**
         * Determines whether the error is related to network
         *
         * @param error
         * @return
         */
        private  boolean isNetworkProblem(Object error) {
            return (error instanceof NetworkError)
                    || (error instanceof NoConnectionError);
        }

        /**
         * Determines whether the error is related to server
         *
         * @param error
         * @return
         */
        private  boolean isServerProblem(Object error) {
            return (error instanceof ServerError)
                    || (error instanceof AuthFailureError);
        }

        /**
         * Handles the server error, tries to determine whether to show a stock
         * message or to show a message retrieved from the server.
         *
         * @param err
         * @return
         */
        private String handleServerError(Object err) {
            VolleyError error = (VolleyError) err;

            NetworkResponse response = error.networkResponse;

            if (response != null) {
                //System.out.println("服务端返回状态码："+response.statusCode);
                switch (response.statusCode) {
                    case 404:
                    case 422:
                    case 401:
                        /*try {
                            // server might return error like this { "error":
                            // "Some error occured" }
                            // Use "Gson" to parse the result
                            HashMap<String, String> result = new Gson().fromJson(
                                    new String(response.data),
                                    new TypeToken<Map<String, String>>() {}.getType());

                            if (result != null && result.containsKey("error")) {
                                return result.get("error");
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }*/
                        // invalid request
                        return generic_server_down + error.getMessage();
                    case 999:
                        return VolleyUtil.ERROR_NOT_REGISTER;
                    default:
                        return server_error;
                }
            }
            return generic_error;
        }
    }

}
