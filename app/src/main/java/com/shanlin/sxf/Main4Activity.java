package com.shanlin.sxf;

import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;

import com.google.gson.JsonObject;
import com.shanlin.sxf.api.ApiModule;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class Main4Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
    }

    public void loadData() {
        Map<String, String> hashMap = new HashMap<>();
        //上传文件
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath());
        File fileName = new File(file, "idCard.jpg");
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpeg"), fileName);
        MultipartBody.Part part = MultipartBody.Part.createFormData("paramName", fileName.getName(), requestBody);

        //与这种格式上传File文件应该差不多... ...
        Map<String, RequestBody> params = new HashMap<>();
        String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(fileName.getName()));
        RequestBody partImage = RequestBody.create(MediaType.parse(mimeType), fileName);
        params.put("image\"; filename=\"" + fileName.getName(), partImage);

        //上传多个文件--MulipartBody和MulipartBody.Part的集合转换--也就是MulipartBody.Part的集合
        MultipartBody multipartBody = new MultipartBody.Builder().build();
        List<MultipartBody.Part> parts = multipartBody.parts();
        parts.add(part);


        ApiModule.getInstance()
                .getApiUrl()
                .getUserInfo(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject jsonObject) {


                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {


                    }
                });
    }

    private void initObserva() {
        //观察者--做出数据更新的回调--也就是被观察者Observable后的回调
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {

            }
        };
        //实现Observer的抽象类
        Subscriber<String> subscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {

            }
        };
        /**
         * Observable的创建方式--create（）内部手动调用订阅接受的回调(这里无任何请求操作)
         * .from()\.just()内部直接走订阅的回调
         * 以下是观察者的同步模式：也就是发出的和接受的在同一线程
         * 一般要在异步观察者运用：Scheduler（线程控制器）的使用--、Schedulers.io()---Android的AndroidSchedulers.mainThread()
         * subscribeOn()是IO也就是(OnSubscriber执行的线程内部Call方法耗时的线程，读文件，写文件，网络交互)顾--其也一般也用于读写文件操作上
         * observeOn()是Main也就是Subscriber执行的线程(事件消费的线程)
         *
         */
        //Observeable 数据请求被观察者
        Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onCompleted();
                subscriber.onNext("One");
                subscriber.onNext("Two");
                subscriber.onNext("three");
                subscriber.onError(new Throwable());
            }
        });
        observable.subscribe(observer);//当被订阅后Obervable的Call方法会自动执行--Observer订阅后会自动转换成Subscribe对象
        //Action是不完整的回调--其会自动创建Observer--然后将其内部的请求结果onComplete()和onNext(String).onError(Throwable)封装成不带参数额Action(),和带一个参数的Action，从而执行其的Call回调方法
        observable.subscribe(new Action1<String>() {
            @Override
            public void call(String s) {

            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {

            }
        });

        observable.subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {

            }
        });


        //相当于执行了subscriber.onNext（one\two\three）的方法--就是一个事件列表（OnSubscribe）
        //just--会一步一步执行回调(例如for循环)
        Observable<String> observable1 = Observable.just("0ne", "two", "three");
        observable1.subscribe(subscriber);
        //from--数组、Iterable、--会一步一步执行回调(例如for循环)
        String[] strings = new String[]{"0ne", "two", "three"};
        Observable<String> observable2 = Observable.from(strings);
        observable2.subscribe(subscriber);//将传入的subscribe返回回来不过是转换成Subscription的形式方便进行取消订阅
        //顾取消订阅是观察者取消--也就是Observer--Subscriber--Subscription
        Subscription subscription = subscriber;
        subscription.unsubscribe();
        

        /**
         * map--String
         */
        Observable.just("10").map(new Func1<String, Integer>() {
            @Override
            public Integer call(String s) {
                return Integer.parseInt(s);
            }
        }).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                //输出将String转换成Integer后的数据
            }
        });
        /**
         * map---ArrayList<Object></>
         */
        ArrayList<PersonInfo> arrayList = new ArrayList<>();

        Observable.from(arrayList)
                .map(new Func1<PersonInfo, String>() {
                    @Override
                    public String call(PersonInfo personInfo) {
                        return personInfo.number;
                    }
                }).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                //将整个对象数据转换--只取对应的Number
            }
        });
        /**
         * flatMap--ArrayList<ArrayList<String>> arrayLists
         */
        ArrayList<ArrayList<String>> arrayLists = new ArrayList<>();
        Observable.from(arrayLists)
                .flatMap(new Func1<ArrayList<String>, Observable<String>>() {
                    @Override
                    public Observable<String> call(ArrayList<String> strings) {
                        return Observable.from(strings);
                    }
                }).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                //输出arrayLists对应的内部Arraylist内部的对应的string的值
            }
        });

        /**
         * flatMap---多次网络请求----
         * map与flatMap中的请求参数Func<A,B>其实A是你本身应该拿到的对象，或者是第一次请求返回的对象，
         * B是你真正想要的对象的属性，也就是第二次请求时返回的数据类型
         */
        subscription1 = ApiModule.getInstance()
                .getApiUrl()
                .getUserPassword("userPassword")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<String, Observable<JsonObject>>() {
                    @Override
                    public Observable<JsonObject> call(String s) {
                        int params = Integer.parseInt(s);
                        HashMap<String, String> map = new HashMap<>();
                        return ApiModule.getInstance().getApiUrl().getUserInfo(map);
                    }
                }).subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject s) {

                    }
                });
    }

    private Subscription subscription1;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消订阅--不可直接写在.subscribe().后边，这样会直接取消订阅走不到回调方法
        if (subscription1.isUnsubscribed()) {
            subscription1.unsubscribe();
        }

    }
}
