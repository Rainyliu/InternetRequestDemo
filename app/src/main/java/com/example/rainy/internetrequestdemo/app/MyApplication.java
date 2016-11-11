package com.example.rainy.internetrequestdemo.app;

import android.app.Application;

import com.yolanda.nohttp.Logger;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.URLConnectionNetworkExecutor;
import com.yolanda.nohttp.cache.DBCacheStore;
import com.yolanda.nohttp.cache.DiskCacheStore;
import com.yolanda.nohttp.cookie.DBCookieStore;

/**
 * Author: Rainy <br>
 * Description: InternetRequestDemo <br>
 * Since: 2016/11/11 0011 上午 10:34 <br>
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //一般初始化，直接初始化后，一般采用默认设置
        NoHttp.initialize(this);

        //高级自定义初始化

        //超时配置，默认10s
        NoHttp.initialize(this,new NoHttp.Config()
                // 设置全局连接超时时间，单位毫秒
                .setConnectTimeout(30*1000)
                // 设置全局服务器响应超时时间，单位毫秒
                .setReadTimeout(30*1000));

        //配置缓存，默认保存在数据库
        NoHttp.initialize(this,new NoHttp.Config()
                // 保存到数据库
                .setCacheStore(new DBCacheStore(this).setEnable(true))// 如果不使用缓存，设置false禁用。
                // 或者保存到SD卡
                .setCacheStore(new DiskCacheStore(this))
        );

        //配置Cookie保存的位置，默认保存在数据库
        NoHttp.initialize(this,new NoHttp.Config()
                // 默认保存数据库DBCookieStore，开发者可以自己实现。
                .setCookieStore(new DBCookieStore(this).setEnable(false))// 如果不维护cookie，设置false禁用。
        );

        //配置网络层
        NoHttp.initialize(this,new NoHttp.Config()
                //使用HttpURLConnection
                .setNetworkExecutor(new URLConnectionNetworkExecutor())
                // 使用OkHttp
//                .setNetworkExecutor(new OkHttpNetworkExecutor())
        );

        //友好的调试模式
        Logger.setDebug(true);// 开启NoHttp的调试模式, 配置后可看到请求过程、日志和错误信息。
        Logger.setTag("InternetRequestdemo");// 设置NoHttp打印Log的tag。

    }
}
