package com.shanlin.sxf.api;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Sxf on 2017/5/10.
 *
 * @project: Demo.
 * @detail:
 */

public class ApiModule {
    OkHttpClient httpClient;
    Retrofit retrofit;
    String baseUrl="http://121.43.163.141:8080/";
    static ApiModule apiModule;

    private void ApiModule(){}
    public static ApiModule getInstance(){
        if(apiModule==null){
            apiModule=new ApiModule();
        }
        return apiModule;
    }

    public void createOkHttp() {
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        HttpLoggingInterceptor interceptor=new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
       builder.connectTimeout(30000, TimeUnit.MILLISECONDS)
                .readTimeout(60000, TimeUnit.MILLISECONDS)
                .addInterceptor(interceptor)
                .build();
        httpClient = builder.build();
    }

    public void createRetrofit(){
        Retrofit.Builder builder=new Retrofit.Builder();
        retrofit = builder
                .baseUrl(baseUrl)
                .client(httpClient)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    }

    public   ApiUrl getApiUrl(){
        if(httpClient==null){
            createOkHttp();
        }

        if (retrofit==null){
            createRetrofit();
        }
        ApiUrl apiUrl = retrofit.create(ApiUrl.class);
        return apiUrl;
    }
}
