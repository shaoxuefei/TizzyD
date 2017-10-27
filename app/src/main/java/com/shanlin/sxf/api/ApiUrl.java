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
import retrofit2.http.Path;
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
    //{userpath}--Url的占位符--在请求方法中将Url传递进去，其会替换上边对应的占位符
    @GET("{userpath}")
    Observable<JsonObject> changePath(@Path("userpath") String path);


    //这种方式是--Get请求时，没有Body，请求参数需要拼接到Url后边，，所以提供了@Query/@QueryMap进行参数的添加--会自动拼接到Url后端

    @GET("user/password/")
    Observable<String> getUserPassword(@Query("password") String password);

    @GET
    Observable<JsonObject> getUserInfo(@QueryMap Map<String, String> map);

    //表单形式提交：from_data形式 需要添加 @FormUrlEncode
    @FormUrlEncoded
    @POST("user/post")
    Observable<JsonObject> postParams(@FieldMap Map<String, RequestBody> mapParams, @Field("name") RequestBody nameBody, @Field("password") RequestBody passwordBody);



    //格式：Json格式上传  ---@Body

    @POST("mayijinfu/yidundigitalidentity/")
    Observable<BeanResponseInfo> postInfoBean(@Body BeanInfo beanInfo);

    //文件上传-需要添加@Multipart---@Part /@PartMap (Value--RequestBody或者是MultipartBody/MultipartBody.Part)
    @Multipart
    @POST("usre/postFile")
    Observable<JsonObject> postPic(@Part MultipartBody.Part fileParams, @PartMap Map<String, RequestBody> mapParams);
    //其实文件上传就是以  from_data表单的形式上传的
    @Multipart
    @POST
    Observable<JsonObject> postMuchFile(@Part MultipartBody partList);

}
