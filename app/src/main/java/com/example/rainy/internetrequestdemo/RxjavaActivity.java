package com.example.rainy.internetrequestdemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rainy.internetrequestdemo.bean.Course;
import com.example.rainy.internetrequestdemo.bean.Messages;
import com.example.rainy.internetrequestdemo.bean.Student;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
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
//        rxjavaScheduler();
//        mapDemo();
//        flaMapDemo();
//        flaMapDemo1();
//        flaMapDemo2();
        liftDemo();
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

    /**
     * 变换
     * 所谓变换，就是将事件序列中的对象或整个序列进行加工处理，转换成不同的事件或事件序列
     * Integer 变换为 Bitmap
     *  map() 的例子
     */
    public void mapDemo(){
        int log = R.raw.log;
        Observable.just(log)
                .map(new Func1<Integer, Bitmap>() {//输入类型String
                    @Override
                    public Bitmap call(Integer id) {//参数类型String
                        Log.d("mtag","id----------"+id);
                        return getBitmapFromPath(id);//返回类型Bitmap
                    }
                })
                .subscribe(new Action1<Bitmap>() {
                    @Override
                    public void call(Bitmap bitmap) {
                        Log.d("mtag","bitmap----------"+bitmap);
                        showBitmap(bitmap);
                    }
                });
    }

    /**
     * 展示图片到imageView中
     * @param bitmap
     */
    public void showBitmap(Bitmap bitmap){
        if(bitmap != null){
            imageView.setImageBitmap(bitmap);
        }
    }

    /**
     * 从工程项目中raw(在res文件夹下)中读取图片
     * @param id 图片资源id
     * @return bitmap格式图片
     */
    public Bitmap getBitmapFromPath(int id){
        InputStream inputStream = getResources().openRawResource(id);
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
        return bitmap;
    }

    /**
     * 变换
     *
     * flatMap()
     * 由Object转换成String
     * 学生——>学生名称
     * 一对一的关系
     */
    public void flaMapDemo(){
        Student[] student = {new Student("lili"),new Student("mama"),new Student("hihi")};
        Subscriber<String> subscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String name) {
                Log.d("mtag","student-----------name--------"+name);
            }
        };

        Observable.from(student)
                .map(new Func1<Student, String>() {

                    @Override
                    public String call(Student student) {
                        return student.getName();
                    }
                })
                .subscribe(subscriber);
    }

    /**
     * 变换
     *
     * flatMap() (没有用到，用for循环的方式)
     * 由Object转换成Object
     * 学生——>学生所修的课程
     * 一对多的关系
     */
    public void flaMapDemo1(){
        Student[] students = setStudentData();
        Subscriber<Student> subscriber = new Subscriber<Student>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Student student) {
                List<Course> courses = student.getCourses();
                for (int i = 0; i < courses.size(); i++) {
                    Course course = courses.get(i);
                    Log.d("mtag", "courseName===================="+course.getName());
                }
            }
        };

        Observable.from(students)//分发学生数据
                .subscribe(subscriber);//产生订阅关系
    }

    /**
     * 变换
     *
     * flatMap()
     * 由Object转换成Object
     * 学生——>学生所修的课程
     * 一对多的关系
     */
    public void flaMapDemo2(){
        Student[] students = setStudentData();
        Subscriber<Course> subscriber = new Subscriber<Course>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Course course) {
                Log.d("mtag", "courseName===================="+course.getName());
            }
        };
        Observable.from(students)
                .flatMap(new Func1<Student, Observable<Course>>() {
                    @Override
                    public Observable<Course> call(Student student) {
                        return Observable.from(student.getCourses());
                    }
                })
                .subscribe(subscriber);
    }

    /**
     * 使用flatMap（）
     * 嵌套的网络请求
     * （Retrofit + RxJava）：demo
     */
    public void flaMapDemo3(){
//        networkClient.token() // 返回 Observable<String>，在订阅时请求 token，并在响应后发送 token
//                .flatMap(new Func1<String, Observable<Messages>>() {
//                    @Override
//                    public Observable<Messages> call(String token) {
//                        // 返回 Observable<Messages>，在订阅时请求消息列表，并在响应后发送请求到的消息列表
//                        return networkClient.messages();
//                    }
//                })
//                .subscribe(new Action1<Messages>() {
//                    @Override
//                    public void call(Messages messages) {
//                        // 处理显示消息列表
//                        showMessages(messages);
//                    }
//                });
    }

    /**
     * throttleFirst(): 在每次事件触发后的一定时间间隔内丢弃新的事件。常用作去抖动过滤，例如按钮的点击监听器： RxView.clickEvents(button)
     * RxBinding 代码，后面的文章有解释 .throttleFirst(500, TimeUnit.MILLISECONDS)
     * 设置防抖间隔为 500ms .subscribe(subscriber);
     * 妈妈再也不怕我的用户手抖点开两个重复的界面啦。
     */


    /**
     * 变换的原理 lift()
     *
     * 实质：针对事件序列的处理和再发送
     *有点像一种代理机制，通过事件拦截和处理实现事件序列的变换。
     * demo：Integer转换成String
     */
    public void liftDemo(){
        Observable<Integer> observable = Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                Log.d("mtag","创建了一个观察者");
            }
        });
        observable.lift(new Observable.Operator<String,Integer>() {
            @Override
            public Subscriber<? super Integer> call(final Subscriber<? super String> subscriber) {
                // 将事件序列中的 Integer 对象转换为 String 对象
                return new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        subscriber.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        subscriber.onError(e);
                    }

                    @Override
                    public void onNext(Integer integer) {
                        subscriber.onNext("" + integer);
                    }
                };
            }
        });
    }

    /**
     * compose: 对 Observable 整体的变换
     * compose(Transformer)
     * lift() 是针对事件项和事件序列的，而 compose() 是针对 Observable 自身进行变换
     *
     */
    public class LiftAllTransformer implements Observable.Transformer<Integer,String>{

        @Override
        public Observable<String> call(Observable<Integer> observable) {
            return null;
//            observable
//                    .lift1()
//                    .lift2()
//                    .lift3()
//                    .lift4();
        }

    }

//    Transformer liftAll = new LiftAllTransformer();
//    observable1.compose(liftAll).subscribe(subscriber1);
//    observable2.compose(liftAll).subscribe(subscriber2);
//    observable3.compose(liftAll).subscribe(subscriber3);
//    observable4.compose(liftAll).subscribe(subscriber4);


    /**
     * 线程控制：Scheduler (二)
     * observeOn()指定的是它之后的操作所在的线程
     * observeOn() 可以多次调用，程序实现了线程的多次切换。
     * subscribeOn()制定的是它之前的操作所在的线程
     * subscribeOn() 的位置放在哪里都可以，但它是只能调用一次的。(subscribeOn可以多个混合使用)
     *因此如果有多次切换线程的需求，只要在每个想要切换线程的位置调用一次 observeOn() 即可
     */
    public void rxJavaScheduler2(){
        Observable.just(1,2,3,4)// IO 线程，由 subscribeOn() 指定
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
//                .map(mapOperator)// 新线程，由 observeOn() 指定
                .observeOn(Schedulers.io())
//                .map(mapOperator2)// IO 线程，由 observeOn() 指定
                .observeOn(AndroidSchedulers.mainThread());
//                .subscribe(subscriber);// Android 主线程，由 observeOn() 指定
    }

    /**
     * doOnSubscribe()
     * Subscriber 的 onStart() 可以用作流程开始前的初始化，
     * 但是在subscribe() 发生时就被调用了，因此不能指定线程而是只能执行在 subscribe() 被调用时的线程
     *
     * Observable.doOnSubscribe()
     * 它和 Subscriber.onStart() 同样是在 subscribe() 调用后而且在事件发送前执行，
     * 但区别在于它可以指定线程。默认情况下， doOnSubscribe() 执行在 subscribe() 发生的线程；
     * 而如果在 doOnSubscribe() 之后有 subscribeOn() 的话，它将执行在离它最近的 subscribeOn() 所指定的线程。
     */
    public void doOnSubscribeDemo(){
//        Observable.create(onSubscribe)
//                .subscribeOn(Schedulers.io())
//                .doOnSubscribe(new Action0() {
//                    @Override
//                    public void call() {
//                        progressBar.setVisibility(View.VISIBLE); // 需要在主线程执行
//                    }
//                })
//                .subscribeOn(AndroidSchedulers.mainThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(subscriber);
    }


    /**
     * 展示请求回来的消息
     */
    public void showMessages(Messages messages){
        tv.setText(messages.getContent());
    }

    /**
     * 初始化学生数据
     */
    public Student[] setStudentData(){
        Course course1 = new Course("math");
        Course course2 = new Course("java");
        Course course3 = new Course("android");
        Course course4 = new Course("h5");
        List<Course> list1 = new ArrayList<>();
        list1.add(course1);
        list1.add(course2);
        List<Course> list2 = new ArrayList<>();
        list2.add(course1);
        list2.add(course2);
        list2.add(course3);
        List<Course> list3 = new ArrayList<>();
        list3.add(course1);
        list3.add(course2);
        list3.add(course3);
        list3.add(course4);

        Student student1 = new Student("aa");
        student1.setCourses(list1);
        Student student2 = new Student("bb");
        student2.setCourses(list2);
        Student student3 = new Student("cc");
        student3.setCourses(list3);
        Student[] students = {student1,student2,student3};
        return students;
    }





}
