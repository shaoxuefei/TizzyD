package com.shanlin.sxf.gson;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.shanlin.sxf.BeanInfo;
import com.shanlin.sxf.R;
import com.shanlin.sxf.api.ApiModule;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.TreeMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Sxf on 2017/5/18.
 *
 * @project: Demo.
 * @detail:
 */
public class GsonActivity extends AppCompatActivity implements View.OnClickListener {
    private String json = "{\"busicode\":\"00001\",\"devicecode\":\"ad7/6RD+UMueHVS5Q2TbimAFbaSSDG76YW7EWAE\\u003dbdfDFDFDJL5DDF4\",\"mercode\":\"201703280000010\",\"orgcode\":\"000000020170508\",\"proname\":\"善林\",\"reqtime\":\"20170516144831\",\"sign\":\"5A6C10CA3719882E064342DD119DFAE3\"}";
    private TextView button, buton1, button2, text, text1, text2, jsonString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gson_activity);
        initView();
        initGson();
    }

    String strUtf;

    private void initGson() {
        Gson gson = new Gson();
        BeanInfo beanInfo = gson.fromJson(json, BeanInfo.class);
        /**
         * 会自动将特殊字符转换成Unicode编码-->>“=”-》\u003d
         */
        String toJson = gson.toJson(beanInfo);

        byte[] bytes = new byte[0];
        try {
            bytes = toJson.getBytes("UTF-8");
            strUtf = new String(bytes, "UTF-8");
            Log.e("aa", strUtf);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        TreeMap treeMap = gson.fromJson(toJson, TreeMap.class);
        /**
         * setPrettyPrinting()是将JsonString格式化输出，会在每一个key:value 后边都有和换行符\n--正常赋值对象不会有问题，但如果对其JsonString进行操作“签名”因为其
         * 多了几个空格可能会报错
         *
         * disableHtmlEscaping--是使将对象转换成JsonString时里边的参数的特殊字符不被转译成UNICODE--因为Gson内部转换是和浏览器相关的
         */
//        Gson gson1=new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        Gson gson1 = new GsonBuilder().disableHtmlEscaping().create();
        /**
         * 会自动在每一个属性的组合对中每一组合对加\n的换行符
         */
        String disableJson = gson1.toJson(beanInfo);
        /**
         * 又从新转换过来即将\n去除--也对毕竟是给对象复制不会带特殊字符的
         */
        TreeMap treeMap1 = gson.fromJson(disableJson, TreeMap.class);
        BeanInfo beanInfo1 = gson.fromJson(disableJson, BeanInfo.class);


        /**
         * fastJson包---JSONObject 转换JsonString---Object--并不会对其中的特殊字符转译
         */
        //这种的话-参数中的/-转译为-->\/
        String jsonString = beanInfo.getJsonString();
        Log.e("aa", "jsonString--" + jsonString);
        //---会直接把你对象中的方法生成的String也当场key：value转成JsonString一部分--贼JB奇怪
        String fastJson = JSONObject.toJSONString(beanInfo);

        BeanInfo beanInfo2 = JSONObject.parseObject(jsonString, BeanInfo.class);


        TreeMap treeMap2 = JSONObject.parseObject(fastJson, TreeMap.class);


        ApiModule.getInstance().createOkHttp();
        OkHttpClient httpClient = ApiModule.getInstance().httpClient;

        Request request = new Request.Builder().url("http://fanyi.youdao.com/")
                .build();
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                String error = e.toString();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String strResponse = response.toString();
                //OkHttp直接调用enqueue()方法请求时，返回的回调也是在子线程的、、Retrofit内部的请求Call对象回调是已经做了线程调度的，也就是返回回调时是主线程,,Observable请求时需要先设置请求和放回线程的subscribeOn,observableOn，内部不会处理
                //runOnUiThread方法就是主线程Handler发送一个Message到主线程的Looper回调,是Activity内部方法
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(GsonActivity.this, strResponse, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void initView() {
        buton1 = (TextView) findViewById(R.id.button1);
        button = (TextView) findViewById(R.id.button);
        button2 = (TextView) findViewById(R.id.button2);
        text = (TextView) findViewById(R.id.text);
        text1 = (TextView) findViewById(R.id.text1);
        text2 = (TextView) findViewById(R.id.text2);
        jsonString = (TextView) findViewById(R.id.jsonString);
        jsonString.setText(json);
        button.setOnClickListener(this);
        buton1.setOnClickListener(this);
        button2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Gson gson = new Gson();
        BeanInfo beanInfo = gson.fromJson(json, BeanInfo.class);
        if (id == R.id.button) {
            String s = gson.toJson(beanInfo);
            text.setText(s);
        } else if (id == R.id.button1) {
            Gson gson1 = new GsonBuilder().disableHtmlEscaping().create();
            String s = gson1.toJson(beanInfo);
            text1.setText(s);
        } else if (id == R.id.button2) {
            String string = JSONObject.toJSONString(beanInfo);
            text2.setText(string);
        }
    }
}