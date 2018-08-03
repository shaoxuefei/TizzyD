package com.shanlin.sxf.presenter;

import android.app.Activity;

import com.shanlin.sxf.bean.MethodBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : SXF
 * @ date   : 2018/7/12
 * Description :
 */

public class MethodRangePresenter {
    private Activity activity;
    OnMethodTestCallBack onMethodTestCallBack;

    public MethodRangePresenter(Activity activity, OnMethodTestCallBack onMethodTestCallBack) {
        this.activity = activity;
        this.onMethodTestCallBack = onMethodTestCallBack;
    }

    public void requestMethodData(List<MethodBean> dataList) {
        //即使前后调用两个方法，，数据源不一样---也是单独的运行分开---类似于区间分开---前提是创建了新对象--就类似于新区间--如果不是新对象就会有问题
        final List<MethodBean> resourceList = new ArrayList<>();
        resourceList.addAll(dataList);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (onMethodTestCallBack != null) {
                                //即使前后调用两个方法，，数据源不一样---也是单独的运行分开---类似于区间分开
                                onMethodTestCallBack.onCallBack(resourceList);
                            }
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();


//        Handler handler = new Handler(new Handler.Callback() {
//            @Override
//            public boolean handleMessage(Message msg) {
//                if (onMethodTestCallBack != null) {
//                    onMethodTestCallBack.onCallBack(resourceList);
//                }
//                return false;
//            }
//        });
//
//
//        handler.sendEmptyMessageDelayed(1, 5000);
    }


    public interface OnMethodTestCallBack {
        void onCallBack(List<MethodBean> dataList);
    }

}
