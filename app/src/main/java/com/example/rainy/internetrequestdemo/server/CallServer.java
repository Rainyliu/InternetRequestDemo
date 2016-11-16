package com.example.rainy.internetrequestdemo.server;

import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;

/**
 * Author: Rainy <br>
 * Description: InternetRequestDemo 封装队列<br>
 * 懒汉式的单例模式
 * Since: 2016/11/16 0016 上午 10:06 <br>
 */

public class CallServer {
    private static CallServer instance;

    /**
     * 请求队列
     */
    private RequestQueue requestQueue;

    /**
     * 私有化构造方法
     */
    private CallServer(){
        requestQueue = NoHttp.newRequestQueue();
    }

    public synchronized static CallServer getInstance(){
        if(instance == null){
            synchronized (CallServer.class){
                if (instance == null){
                    instance = new CallServer();
                }
            }
        }
        return instance;
    }

    /**
     * 添加一个请求到请求队列。
     *
     * @param what      用来标志请求, 当多个请求使用同一个Listener时, 在回调方法中会返回这个what。
     * @param request   请求对象。
     * @param listener  结果回调对象。
     * @param <T>
     */
    public <T> void add(int what, Request<T> request, OnResponseListener listener){
        requestQueue.add(what,request,listener);
    }


    /**
     * 取消这个sign标记的所有请求。
     * @param sign 请求的取消标志。
     */
    public void cancelBySign(Object sign) {
        requestQueue.cancelBySign(sign);
    }

    /**
     * 取消队列中所有请求。
     */
    public void cancelAll(){
        requestQueue.cancelAll();
    }

}
