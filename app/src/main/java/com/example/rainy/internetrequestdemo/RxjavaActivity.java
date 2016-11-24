package com.example.rainy.internetrequestdemo;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class RxjavaActivity extends AppCompatActivity {
    private TextView tv;
    private File file;
    private File[] folders;
    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rxjava);
        tv = (TextView) findViewById(R.id.tv);
        imageView = (ImageView) findViewById(R.id.iv);
        file = new File("F:\\myphotos");
        folders = file.listFiles();
        Log.d("mtag","111111111111111======================"+Thread.currentThread().getName());
    }

    public  void hello(String... names){
        Observable.from(names).subscribe(new Action1<String>() {

            @Override
            public void call(String s) {
                System.out.println("Hello " + s + "!");
                tv.setText("Hello " + s + "!");
            }

        });
    }

    public void click(View v){
//        hello("lili","mama","yoyo");
//        showImg();
//        showBitmapRxJava();
//        basicReality();
//        simpleDemo1();
//        simpleDemo2();
        rxjavaScheduler();
        Log.d("mtag","回到按钮点击事件中==========="+Thread.currentThread().getName());
    }

    public void showImg(){
        new Thread(){
            @Override
            public void run() {
                super.run();
//                for(File folder : folders){
//                    if(folder.getName().endsWith(".png") || folder.getName().endsWith(".jpg")){
//                        final Bitmap bitmap = getBitmapFromFile(folder);
                        Log.d("mtag","222222222222222222======================"+Thread.currentThread().getName());
                        RxjavaActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                imageCollectorView.addImage(bitmap);
                                Log.d("mtag","333333333333333333333======================"+Thread.currentThread().getName());
                            }
                        });
//                    }
//                }
            }
        }.start();
    }

    public Bitmap getBitmapFromFile(File file){
        return null;
    }

    public void showBitmapRxJava(){
        Observable.from(folders)
                .flatMap(new Func1<File, Observable<File>>() {
                    @Override
                    public Observable<File> call(File file) {
                        Log.d("mtag","aaaaaaaaaaa======================"+Thread.currentThread().getName());
                        return Observable.from(file.listFiles());
                    }
                })
                .filter(new Func1<File, Boolean>() {
                    @Override
                    public Boolean call(File file) {
                        Log.d("mtag","bbbbbbbbbbbb======================"+Thread.currentThread().getName());
                        return file.getName().endsWith(".png");
                    }
                })
                .map(new Func1<File, Bitmap>() {
                    @Override
                    public Bitmap call(File file) {
                        Log.d("mtag","cccccccccccccccccc======================"+Thread.currentThread().getName());
                        return getBitmapFromFile(file);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Bitmap>() {
                    @Override
                    public void call(Bitmap bitmap) {
                        Log.d("mtag","ddddddddddddddddd======================"+Thread.currentThread().getName());
//                        imageCollectorView.addImage(bitmap);
                    }
                });
    }

    /**
     * rxJava
     * 基本实现
     * 1）创建observer
     * 2) 创建observable
     * 3) Subscribe (订阅)
     */
    public void basicReality(){
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onCompleted() {
                Log.d("mtag", "Completed！");
            }

            @Override
            public void onError(Throwable e) {
                Log.d("mtag", "Error！");
            }

            @Override
            public void onNext(String s) {
                Log.d("mtag", "Item: " + s);
            }
        };

        Subscriber<String> subscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {
                Log.d("mtag", "Completed！");
            }

            @Override
            public void onError(Throwable e) {
                Log.d("mtag", "Error！");
            }

            @Override
            public void onNext(String s) {
                Log.d("mtag", "Item: " + s);
            }
        };


        /**
         * 创建Observable
         *
         * Observable 即被观察者，它决定什么时候触发事件以及触发怎样的事件。
         * RxJava 使用 create() 方法来创建一个 Observable ，并为它定义事件触发规则
         */
        Observable observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("hello");
                subscriber.onNext("Hi");
                subscriber.onNext("Aloha");
                subscriber.onCompleted();

            }
        });

        //just(T...): 将传入的参数依次发送出来。
        Observable observable1 = Observable.just("hello","hi","aloha");
        //将会依次调用
        // onNext("Hello");
        // onNext("Hi");
        // onNext("Aloha");
        // onCompleted();

        //from(T[]) / from(Iterable<? extends T>) : 将传入的数组或 Iterable 拆分成具体对象后，依次发送出来。
        String[] words = {"hello","hi","aloha"};
        Observable observable2 = Observable.from(words);
        //将会依次调用：
        // onNext("Hello");
        // onNext("Hi");
        // onNext("Aloha");
        // onCompleted();

        /**
         * 订阅 Subscribe
         *
         * subscribe() 方法用来链接Observable 和 Observer
         */
//        observable.subscribe(observer)
//         或者：
//        observable.subscribe(subscriber);
    }

    /**
     * 场景示例一
     *
     * 打印字符串数组
     */
    public void simpleDemo1(){
        String[] names ={"aaa","bbb","ccc"};
        Observable.from(names)
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.d("mtag",Thread.currentThread().getName()+"--------------"+s);
                    }
                });
    }

    /**
     * 场景示例二
     *
     * 由 id 取得图片并显示
     * 加上线程调度
     * 意味着：加载图片将会发生在 IO 线程，而设置图片则被设定在了主线程
     */
    public void simpleDemo2(){
        final int drawableRes = R.drawable.images;
        Observable.create(new Observable.OnSubscribe<Drawable>() {

            @Override
            public void call(Subscriber<? super Drawable> subscriber) {
                Drawable drawable = getTheme().getDrawable(drawableRes);
                subscriber.onNext(drawable);
                subscriber.onCompleted();

            }
        })
        .subscribeOn(Schedulers.io())// 指定 subscribe() 发生在 IO 线程
        .observeOn(AndroidSchedulers.mainThread())// 指定 Subscriber 的回调发生在主线程
        .subscribe(new Observer<Drawable>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(RxjavaActivity.this, "Error!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(Drawable drawable) {
                imageView.setImageDrawable(drawable);
            }
        });
    }

    /**
     * 线程控制 —— Scheduler (一)
     *
     */
    public void rxjavaScheduler(){
        Observable.just(1,2,3,4)
                .subscribeOn(Schedulers.io())//指定 subscribe() 发生在 IO 线程 本例中既是被创建的事件的内容 1、2、3、4 将会在 IO 线程发出 后台取数据
                .observeOn(AndroidSchedulers.mainThread())// 指定 Subscriber 的回调发生在主线程    数字打印发生在主线程   主线程显示
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        Log.d("mtag", "number:" + integer);
                    }
                });
    }


}
