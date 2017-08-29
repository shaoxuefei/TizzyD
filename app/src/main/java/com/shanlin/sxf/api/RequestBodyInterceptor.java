package com.shanlin.sxf.api;

import java.io.IOException;
import java.util.Comparator;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author ZhaoYun
 * @desc 功能描述
 * @date 2016/7/26 14:43
 */
public final class RequestBodyInterceptor implements Interceptor {

    private static final Comparator<String> SORT_COMPARATOR = new Comparator<String>() {

        @Override
        public int compare(String s, String t1) {
            return s.compareTo(t1);
        }
    };

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request orgRequest = chain.request();

//        TreeMap<String, String> map = new TreeMap<String, String>(SORT_COMPARATOR);
//
//        Request.Builder orgRequestBuilder = orgRequest.newBuilder();
//        //请求定制：添加请求头
////                .header("APIKEY", Constant.API_KEY);
//        if (orgRequest.body() == null) {
//            return chain.proceed(orgRequest);
//        }
//        RequestBody orgRequestBody = orgRequest.body();
//        if (orgRequestBody instanceof FormBody) {
//            FormBody orgFormBody = (FormBody) orgRequestBody;
//            FormBody.Builder newFormBodyBuilder = new FormBody.Builder();
//            for (int i = 0; i < orgFormBody.size(); i++) {
//                map.put(orgFormBody.name(i), orgFormBody.value(i));
//                newFormBodyBuilder.addEncoded(orgFormBody.encodedName(i), orgFormBody.encodedValue(i));
//            }
//            //sort + md5
//            map.put(HttpBaseConfig.SECRET_KEY, HttpBaseConfig.SECRET_VALUE);
//            StringBuilder sb = new StringBuilder();
//            Iterator iter = map.keySet().iterator();
//            while (iter.hasNext()) {
//                String key = (String) iter.next();
//                sb.append(key == null ? "" : key);//key
//                sb.append(map.get(key) == null ? "" : map.get(key));//value
//            }
//            String requestSignValue = MD5Util.encode(sb.toString());
//            //append requestSign
//            newFormBodyBuilder.addEncoded(HttpBaseConfig.REQUESTSIGN_KEY, requestSignValue);
//            orgRequestBuilder.delete(orgFormBody);
//            orgRequestBuilder.method(orgRequest.method(), newFormBodyBuilder.build());
//            Request newRequest = orgRequestBuilder.build();
//            return chain.proceed(newRequest);
//        }

        return chain.proceed(orgRequest);
    }

}
