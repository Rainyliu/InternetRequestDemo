package com.example.rainy.internetrequestdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

public class BackpressureActivity extends AppCompatActivity {
    private TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backpressure);
        tv = (TextView) findViewById(R.id.text);

    }

    public void click(View view){
//        collapseDemo();
//        filterDemo();
//        bufferDemo();
        onBackpressureDrop();
    }

    public void collapseDemo(){
        Observable.interval(1, TimeUnit.MICROSECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Log.w("TAG","---->"+aLong);
                    }
                });
    }

    public void backDemo(){
        //被观察者将产生100000个事件
        Observable<Integer> observable = Observable.range(1,100000);
        observable.observeOn(Schedulers.newThread())
                .subscribe(new MySubscriber());

    }

    class MySubscriber extends Subscriber<Integer>{

        @Override
        public void onStart() {
            //一定要在onStart中通知被观察者先发送一个事件
            request(1);
        }

        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(Integer t) {
            //处理完毕之后，在通知被观察者发送下一个事件
            request(1);
        }
    }

    public void filterDemo(){
        Observable.interval(1,TimeUnit.MILLISECONDS)
                .observeOn(Schedulers.newThread())
                //这个操作符简单理解就是每隔200ms发送里时间点最近那个事件，
                //其他的事件浪费掉
                .sample(200,TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Log.w("TAG","---->"+aLong);
                    }
                });
    }

    public void bufferDemo(){
        Observable.interval(1,TimeUnit.MILLISECONDS)
                .observeOn(Schedulers.newThread())
                //这个操作符简单理解就是把100毫秒内的事件打包成list发送
                .buffer(100,TimeUnit.MILLISECONDS)
                .subscribe(new Action1<List<Long>>() {
                    @Override
                    public void call(List<Long> longs) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Log.w("TAG","---->"+longs.size());
                    }
                });
    }

    /**
     * onBackpressurebuffer：把observable发送出来的事件做缓存，当request方法被调用的时候，
     * 给下层流发送一个item(如果给这个缓存区设置了大小，那么超过了这个大小就会抛出异常)。
     *
     onBackpressureDrop：将observable发送的事件抛弃掉，
     直到subscriber再次调用request（n）方法的时候，就发送给它这之后的n个事件。
     */
    public void onBackpressureDrop(){
        Observable.interval(1,TimeUnit.MILLISECONDS)
                .onBackpressureDrop()
                .observeOn(Schedulers.newThread())
                .subscribe(new Subscriber<Long>() {

                    @Override
                    public void onStart() {
                        Log.w("TAG","start");
//                        request(1);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("ERROR",e.toString());
                    }

                    @Override
                    public void onNext(Long aLong) {
                        Log.w("TAG","---->"+aLong);
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


}
