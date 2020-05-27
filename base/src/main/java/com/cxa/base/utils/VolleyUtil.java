package com.cxa.base.utils;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

/**
 * 瀑布流图片刷新用
 *
 * 2020/05/07
 */
public class VolleyUtil {

    public VolleyUtil(Context context, String url, final VolleyCallBack volleyCallBack){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        volleyCallBack.onSuccess(jsonObject.toString());
                        Log.e("recycler", "onResponse: " + android.os.Process.myTid());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("recyclerveiw", "onErrorResponse:" + volleyError);
            }
        });
        requestQueue.add(jsonObjectRequest);
    }
    public interface VolleyCallBack{
        void onSuccess(String result);
    }

}
