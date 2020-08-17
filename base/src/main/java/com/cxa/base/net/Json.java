package com.cxa.base.net;

/**
 * 服务器端返回固定的json格式
 * @author DefterosChen
 *
 */
public class Json {
    /**
     * 返回结果标识：0-失败， 1-成功
     */
    private int result = 0;
    /**
     * 返回结果描述，特殊描述或者异常描述等
     */
    private String info = "";
    /**
     * 返回的数据对象
     */
    private Object data;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Json{" +
                "data=" + data +
                ", result=" + result +
                ", info='" + info + '\'' +
                '}';
    }
}