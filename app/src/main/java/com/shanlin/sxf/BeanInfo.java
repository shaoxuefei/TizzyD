package com.shanlin.sxf;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Sxf on 2017/5/12.
 *
 * @project: DeviceIdSample.
 * @detail:
 */

public class BeanInfo {

    public String mercode;
    public String proname;
    public String busicode;
    public String devicecode;
    public String reqtime;
    public String sign;
    public String orgcode;


    public String getJsonString(){
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("mercode",mercode);
            jsonObject.put("proname",proname);
            jsonObject.put("busicode",busicode);
            jsonObject.put("devicecode",devicecode);
            jsonObject.put("reqtime",reqtime);
            jsonObject.put("sign",sign);
            jsonObject.put("orgcode",orgcode);
            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
