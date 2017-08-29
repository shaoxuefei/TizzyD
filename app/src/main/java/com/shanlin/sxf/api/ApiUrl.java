package com.shanlin.sxf.api;


import com.google.gson.JsonObject;
import com.shanlin.sxf.BeanInfo;
import com.shanlin.sxf.BeanResponseInfo;
import com.shanlin.sxf.PersonInfo;


import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by Sxf on 2017/5/10.
 *
 * @project: Demo.
 * @detail:
 */

public interface ApiUrl {


    @GET("user/name/")
    Call<String> getUserName(@Query("name") String userName);

    @GET("user/password/")
    Observable<String> getUserPassword(@Query("password") String password);

    @GET
    Observable<JsonObject> getUserInfo(@QueryMap Map<String,String> map);


    @FormUrlEncoded
    @POST("user/post")
    Observable<JsonObject> postParams(@FieldMap Map<String,RequestBody> mapParams, @Field("name")RequestBody nameBody, @Field("password") RequestBody passwordBody);


    @Multipart
    @POST("usre/postFile")
    Observable<JsonObject> postFile(@Part MultipartBody.Part fileParams, @PartMap Map<String,RequestBody> mapParams);

    @Multipart
    @POST
    Observable<JsonObject> postMuchFile(@Part MultipartBody partList);

    @POST
    Observable<PersonInfo> postJson(@Body PersonInfo personInfo);

    @POST("mayijinfu/yidundigitalidentity/")
    Observable<BeanResponseInfo> postInfoBean(@Body BeanInfo beanInfo);

    @Multipart
    @POST("usre/postFile")
    Observable<JsonObject> postPic(@Part MultipartBody.Part fileParams, @PartMap Map<String,RequestBody> mapParams);
}
