package com.example.rainy.internetrequestdemo;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rainy.internetrequestdemo.utils.WaitDialog;
import com.yolanda.nohttp.Headers;
import com.yolanda.nohttp.Logger;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.error.NetworkError;
import com.yolanda.nohttp.error.NotFoundCacheError;
import com.yolanda.nohttp.error.ParseError;
import com.yolanda.nohttp.error.TimeoutError;
import com.yolanda.nohttp.error.URLError;
import com.yolanda.nohttp.error.UnKnownHostError;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;
import com.yolanda.nohttp.rest.Response;

import java.net.ProtocolException;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final String IMAGE_URL = "http://pics.sc.chinaz.com/files/pic/pic9/201508/apic14052.jpg";
    private static final int NOHTTP_WHAT_TEST = 0x001;
    private ImageView iv;
    /**
     * 请求队列
     */
    private RequestQueue requestQueue;
    /**
     * 请求的时候等待框。
     */
    private WaitDialog mWaitDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iv = (ImageView) findViewById(R.id.iv);

        mWaitDialog = new WaitDialog(this);

        // 创建请求队列, 默认并发3个请求,传入你想要的数字可以改变默认并发数, 例如NoHttp.newRequestQueue(1)。
        // 不过正式的项目中不要每次请求时都创建队列，应该把队列封装成单例模式，这样才能把队列的优点发挥出来。
        requestQueue = NoHttp.newRequestQueue();
    }

    public void click(View v){
        //创建请求对象
        Request<Bitmap> request = NoHttp.createImageRequest(IMAGE_URL, RequestMethod.GET);

        //添加请求参数
        request.add("userName", "rainy") // String型。
                .add("userPass", 1) // int型。
                .add("userAge", 1.25) // double型。
                .add("nooxxx", 1.2F) // flocat型。

                .setConnectTimeout(10*1000)// 设置连接超时。
                .setReadTimeout(20*1000)//设置读取超时时间，也就是服务器的响应超时。

                /**
                 * 上传文件；上传文件支持File、Bitmap、ByteArrayBinary、InputStream四种，这里推荐File、InputStream。
                 */
                // request.add("userHead", new FileBinary());
                // request.add("userHead", new BitmapBinary());
                // request.add("userHead", new ByteArrayBinary());
                // request.add("", new InputStreamBinary());

                // 请求头，是否要添加头，添加什么头，要看开发者服务器端的要求。
                .addHeader("Author", "sample")

                // 设置一个tag, 在请求完(失败/成功)时原封不动返回; 多数情况下不需要。
                .setTag(this)
                // 设置取消标志。
                .setCancelSign(this);

        /*
         * what: 当多个请求同时使用同一个OnResponseListener时用来区分请求, 类似handler的what一样。
		 * request: 请求对象。
		 * onResponseListener 回调对象，接受请求结果。
		 */
        requestQueue.add(NOHTTP_WHAT_TEST, request, onResponseListener);
    }

    private OnResponseListener<Bitmap> onResponseListener = new OnResponseListener<Bitmap>() {
        @Override
        public void onStart(int what) {
            // 请求开始，这里可以显示一个dialog
            if (mWaitDialog != null && !mWaitDialog.isShowing())
                mWaitDialog.show();
        }

        @Override
        public void onSucceed(int what, Response<Bitmap> response) {
            if(what == NOHTTP_WHAT_TEST){// 根据what判断是哪个请求的返回，这样就可以用一个OnResponseListener来接受多个请求的结果。
                int responseCode = response.getHeaders().getResponseCode();//服务器响应码

                if(responseCode == 200){// 如果是是用NoHttp的默认的请求或者自己没有对NoHttp做封装，这里最好判断下Http状态码。
                    Bitmap bitmap = response.get();//响应结果

                    iv.setImageBitmap(bitmap);

                    Object tag = response.getTag();//拿到请求时设置的tag

                    //响应头
                    Headers headers = response.getHeaders();
//                    String headResult = getString(R.string.request_original_result);
//                    headResult = String.format(Locale.getDefault(), headResult, headers.getResponseCode(), response.getNetworkMillis());
//                    ((TextView) findView(R.id.tv_head)).setText(headResult);

                }
            }
        }

        @Override
        public void onFailed(int what, Response<Bitmap> response) {
            //TODO 特别注意：这里可能有人会想到是不是每个地方都要这么判断，其实不用，请参考HttpResponseListener类的封装，你也可以这么封装。

            // 请求失败
            Exception exception = response.getException();
            if (exception instanceof NetworkError) {// 网络不好
                com.example.rainy.internetrequestdemo.utils.Toast.show(MainActivity.this, R.string.error_please_check_network);
            } else if (exception instanceof TimeoutError) {// 请求超时
                com.example.rainy.internetrequestdemo.utils.Toast.show(MainActivity.this, R.string.error_timeout);
            } else if (exception instanceof UnKnownHostError) {// 找不到服务器
                com.example.rainy.internetrequestdemo.utils.Toast.show(MainActivity.this, R.string.error_not_found_server);
            } else if (exception instanceof URLError) {// URL是错的
                com.example.rainy.internetrequestdemo.utils.Toast.show(MainActivity.this, R.string.error_url_error);
            } else if (exception instanceof NotFoundCacheError) {
                // 这个异常只会在仅仅查找缓存时没有找到缓存时返回
                com.example.rainy.internetrequestdemo.utils.Toast.show(MainActivity.this, R.string.error_not_found_cache);
            } else if (exception instanceof ProtocolException) {
                com.example.rainy.internetrequestdemo.utils.Toast.show(MainActivity.this, R.string.error_system_unsupport_method);
            } else if (exception instanceof ParseError) {
                com.example.rainy.internetrequestdemo.utils.Toast.show(MainActivity.this, R.string.error_parse_data_error);
            } else {
                com.example.rainy.internetrequestdemo.utils.Toast.show(MainActivity.this, R.string.error_unknow);
            }
            Logger.e("错误：" + exception.getMessage());

        }

        @Override
        public void onFinish(int what) {
            // 请求结束，这里关闭dialog
            if (mWaitDialog != null && mWaitDialog.isShowing())
                mWaitDialog.dismiss();
        }
    };

    @Override
    protected void onDestroy() {
        if(requestQueue != null){
            //根据取消标识取消队列中的请求
            requestQueue.cancelBySign(this);
        }
        super.onDestroy();
    }
}
