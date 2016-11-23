package com.example.rainy.internetrequestdemo;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rxjava);
        tv = (TextView) findViewById(R.id.tv);
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
        basicReality();
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
    }


}
