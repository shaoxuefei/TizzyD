package com.shanlin.sxf;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.deviceid.DeviceTokenClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.shanlin.sxf.api.ApiModule;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Sxf on 2017/5/12.
 *
 * @project: Demo.
 * @detail:指纹签名
 */
public class SingerActivity extends AppCompatActivity implements View.OnClickListener {


    private TextView genApdidButton         = null;

    private TextView showApdidTokenTextView = null;

    private final String APPNAME                = "shanlinjinrong";

    private final String APPKEY_CLIENT          = "0qktLIYTPtCUlt0k";

    private String actionId="UopHOR/mMdTfKLBAx8RAbR0VewMR36NlYJlQGJpReYo9qEwVXAEAAA==";
    private String contentJson;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.singer_activity);
        genApdidButton = (TextView) findViewById(R.id.btn_gen_apdid);
        showApdidTokenTextView = (TextView) findViewById(R.id.txt_apdid_token);
        genApdidButton.setOnClickListener(this);
//        loadData(actionId);
        loadHttpUrlConnect(actionId);

    }

    @Override
    public void onClick(View view) {
        DeviceTokenClient.getInstance(getApplicationContext()).initToken(APPNAME, APPKEY_CLIENT,
                new DeviceTokenClient.InitResultListener() {
                    @Override
                    public void onResult(String apdidToken, final int errorCode) {
                        final String token = apdidToken;
                        final int    err   = errorCode;
                        SingerActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                handleApdidTokeResult(token, err);
                            }
                        });
                    }
                });
    }

    private void handleApdidTokeResult(final String apdidToken, final int errorCode) {
        switch (errorCode) {
            case 0:
                showApdidTokenTextView.setText(apdidToken);
                if(!TextUtils.isEmpty(apdidToken)) {
//                    loadData(apdidToken);
                }else {
                    Toast.makeText(SingerActivity.this,"值为："+apdidToken,Toast.LENGTH_SHORT).show();
                }
                break;
            case 1:
                Toast.makeText(SingerActivity.this, "非法的AppName / AppKeyClient组合!", Toast.LENGTH_LONG).show();
                break;
            case 2:
                Toast.makeText(SingerActivity.this, "AppName为空!", Toast.LENGTH_LONG).show();
                break;
            case 3:
                Toast.makeText(SingerActivity.this, "AppKeyClient为空!", Toast.LENGTH_LONG).show();
                break;
            case 4:
                Toast.makeText(SingerActivity.this, "网络错误!", Toast.LENGTH_LONG).show();
                break;
        }
    }



    private void loadHttpUrlConnect(String deviceCode){
        BeanInfo beanInfo=new BeanInfo();
        beanInfo.busicode="00001";
        beanInfo.devicecode=deviceCode;
        beanInfo.mercode="201703280000009";
        beanInfo.orgcode="000000020170508";
        beanInfo.proname="善林";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        beanInfo.reqtime = sdf.format(new Date());
        beanInfo.sign=createSign(beanInfo,"33333333");
        Gson gson=new GsonBuilder().disableHtmlEscaping().create();
        final String toJson = gson.toJson(beanInfo);
        String strUrl="http://121.43.163.141:8080/mayijinfu/yidundigitalidentity/";
        try {
            URL url=new URL(strUrl);
            final HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setReadTimeout(30000);
            urlConnection.setConnectTimeout(30000);
            //设置Post请求必须设置下边的两句话：outPut/inPut--进行读入读出
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setUseCaches(false);
            urlConnection.setRequestProperty("Content-type", "application/json");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        //开始建立连接
                        urlConnection.connect();
                        //开始写入参数--正式开始传输参数数据
                        OutputStream outputStream = urlConnection.getOutputStream();
                        OutputStreamWriter  outputStreamWriter=new OutputStreamWriter(outputStream);
                        outputStreamWriter.write(toJson);
                        outputStreamWriter.flush();//是将数据流刷新--输出--若该输出流有接受对象(例如File)那么在读取完后会自动写在该File上，其余手动转换输出对象的可以考虑不调用
                        //请求完正式开始回调数据
                        InputStream inputStream = urlConnection.getInputStream();
                        int responseCode = urlConnection.getResponseCode();
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        byte[] buffer = new byte[1024];
                        int len = 0;
                        while(-1 != (len = inputStream.read(buffer))){
                            baos.write(buffer,0,len);
                            baos.flush();
                        }
                        final String stringjson = baos.toString();
                        outputStream.close();
                        baos.close();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                               Toast.makeText(SingerActivity.this,"结果："+stringjson,Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private void loadData(String deviceCode){
        BeanInfo beanInfo=new BeanInfo();
        beanInfo.busicode="00001";
        beanInfo.devicecode=deviceCode;
        beanInfo.mercode="201703280000009";
        beanInfo.orgcode="000000020170508";
        beanInfo.proname="善林";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        beanInfo.reqtime = sdf.format(new Date());
        beanInfo.sign=createSign(beanInfo,"33333333");
        ApiModule.getInstance()
                .getApiUrl()
                .postInfoBean(beanInfo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<BeanResponseInfo>() {
                    @Override
                    public void call(BeanResponseInfo jsonObject) {
                        Toast.makeText(SingerActivity.this,jsonObject.resCode,Toast.LENGTH_SHORT).show();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Toast.makeText(SingerActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * 通过bean 生成签名
     *
     * @param obj
     *         java bean
     *
     * @return
     *
     * @throws Exception
     */
    @Nullable
    public static String createSign(BeanInfo obj, String key) {
        Gson gson = new Gson();
        String objStr = gson.toJson(obj);
        TreeMap<String,Object> map = gson.fromJson(objStr , TreeMap.class);
        if (map.containsKey("sign")) {
            map.remove("sign");
        }
        String sign;
        try {
            sign = getSign(map, key);
            return sign;
        } catch (Exception e) {
            System.out.println("**************签名生成错误************");
        }
        return null;
    }

    /**
     * @param data：加签名、值集合
     * @param signKey：
     *
     * @return
     *
     * @throws Exception
     * @sign rule：密钥需加密数据：密钥+全部非空名、值+密钥（例：keyparam1value1param2value2key）
     * 对需加密数据进行md5，然后转成大写16进制字符串
     */
    public static String getSign(Map<String, Object> data, String signKey) throws Exception {
        Gson gson = new Gson();
        List<String> keys = new ArrayList<>(data.size());
        StringBuilder sb = new StringBuilder();
        keys.addAll(data.keySet());
        sb.append(signKey);
        for (String key : keys) {
            sb.append(key);
            try {
                sb.append("\"" + data.get(key).toString() + "\"");
            } catch (Exception e) {
                sb.append(data.get(key).toString());
            }
        }
        sb.append(signKey);
        byte[] bytes = Md5.getMD5Digest(sb.toString());
        String sign = Md5.byte2hex(bytes);
        return sign;
    }
}