package com.cxa.base.net;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.cxa.base.server.ServiceCall;
import com.cxa.base.utils.EncryptMD5;
import com.cxa.base.utils.EncyptDES;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Volley扩展 返回 GSON对象
 * @author DeftrosChen
 *
 * @param <T>
 */
public class GsonRequest<T> extends Request<T> {

    private Map mMap; //提交 参数

    private final Listener<T> mListener;

    private Gson mGson;

    private Class<T> mClass;

    public GsonRequest(String url, Class<T> clazz, Listener<T> listener,
                       ErrorListener errorListener, Map map) {
        this(Method.POST, url, clazz, listener, errorListener,map);
    }

    public GsonRequest(int method, String url, Class<T> clazz, Listener<T> listener,
                       ErrorListener errorListener, Map map) {
        super(method, url, errorListener);
        mGson = new Gson();
        mClass = clazz;
        mListener = listener;
        mMap = map;
    }


    public GsonRequest(String url, Class<T> clazz, Listener<T> listener,
                       ErrorListener errorListener) {
        this(Method.GET, url, clazz, listener, errorListener,null);
    }

    @Override
    protected Map getParams() throws AuthFailureError {
        //对参数加密
        if(mMap!=null) {
            Set set = mMap.keySet();
            Iterator its = set.iterator();
            while (its.hasNext()) {
                Object key = its.next();
                mMap.put(key, EncyptDES.encrypt(mMap.get(key).toString()));
            }
        }
        return mMap;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> myHeaders = super.getHeaders();
        if(myHeaders==null || myHeaders.equals(Collections.emptyMap())){
            myHeaders = new HashMap<String, String>();
        }
        //提交手机序列号给服务器做认证
        myHeaders.put(VolleyUtil.FROM_PDA_FLAG, EncyptDES.encrypt(EncryptMD5.Encrypt(ServiceCall.getRegisterSerialNumber())));

        return myHeaders;
    }
    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers,""));
            //System.out.println("返回："+jsonString);
            jsonString = EncyptDES.decrypt(jsonString);
           //System.out.println("解密后："+jsonString);
            return Response.success(mGson.fromJson(jsonString, mClass),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(T response) {
        mListener.onResponse(response);
    }

}
