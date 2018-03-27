package com.shanlin.sxf.application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.shanlin.sxf.BuildConfig;
import com.shanlin.sxf.crash.DIYCrashHandler;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.mta.track.StatisticsDataAPI;
import com.tencent.stat.MtaSDkException;
import com.tencent.stat.StatConfig;
import com.tencent.stat.StatCrashReporter;
import com.tencent.stat.StatReportStrategy;
import com.tencent.stat.StatService;
import com.tencent.stat.common.StatConstants;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

/**
 * Created by Sxf on 2017/8/29.
 */

public class MyApplication extends Application {
    boolean isDebug = true;//debug模式会有相关的Log日志进行打印

    @Override
    public void onCreate() {
        super.onCreate();

        Fresco.initialize(getApplicationContext());

        MultiDex.install(this);


        //设置Activity的LifeCallBack
        registerActivityLifecycleCallbacks(activityLifecycleCallbacks);


        //设置CrashHandler工具
        DIYCrashHandler.init(this);


        //Bugly初始化配置--注册后生产的appid:43c9a7d274
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(this);
        strategy.setAppChannel(BuildConfig.FLAVOR);//设置渠道名统计，或者统一在AndroidManifest中统一设置
        strategy.setAppVersion(BuildConfig.VERSION_NAME);
        strategy.setCrashHandleCallback(new DiyCrashCallBack());
        CrashReport.initCrashReport(getApplicationContext(), "43c9a7d274", isDebug, strategy);
        CrashReport.setUserId("10010");//设置UserId


        String packageName = getPackageName();
        // 获取当前进程名
        String processName = getProcessName(android.os.Process.myPid());
        if (!TextUtils.isEmpty(packageName) && processName.equals(packageName)) {
            Log.e("aa", "processName" + processName);
        }

        //啓動腾讯的统计MTA
        try {
            StatConfig statConfig = new StatConfig();

            StatService.startStatService(this, "AH1YTN5V83SY", StatConstants.VERSION);

            //启动MTA的Crash的统计(事件埋点)
            StatConfig.setAppKey(this, "AH1YTN5V83SY");
            StatConfig.setStatSendStrategy(StatReportStrategy.APP_LAUNCH);//启动时发送
            StatCrashReporter statCrashReporter = StatCrashReporter.getStatCrashReporter(this);
            statCrashReporter.setJavaCrashHandlerStatus(true);

            //启动MTA的可视化埋点功能
            StatisticsDataAPI.instance(this);
        } catch (MtaSDkException e) {
            e.printStackTrace();
        }
    }

    private static MyApplication myApplication;

    public static MyApplication getInstance() {
        if (myApplication == null) {
            myApplication = new MyApplication();
        }
        return myApplication;
    }

    public Context getContext(){
        return this;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //注册：分包，防止方法超过65536--
        MultiDex.install(this);
    }

    //自定义Crash回调
    class DiyCrashCallBack extends CrashReport.CrashHandleCallback {

        /**
         * Crash处理.
         *
         * @param crashType    错误类型：CRASHTYPE_JAVA，CRASHTYPE_NATIVE，CRASHTYPE_U3D ,CRASHTYPE_ANR
         * @param errorType    错误的类型名
         * @param errorMessage 错误的消息
         * @param errorStack   错误的堆栈
         * @return 返回额外的自定义信息上报
         */
        @Override
        public synchronized Map<String, String> onCrashHandleStart(int crashType, String errorType, String errorMessage, String errorStack) {

            Toast.makeText(getApplicationContext(), errorType, Toast.LENGTH_SHORT).show();
            //此处可以进行Map的数值添加：key,value
            Map<String, String> stringStringMap = super.onCrashHandleStart(crashType, errorType, errorMessage, errorStack);
            stringStringMap.put("appType", "自定义CrashBack");
            return stringStringMap;
        }

        /**
         * Crash处理.
         *
         * @param crashType    错误类型：CRASHTYPE_JAVA，CRASHTYPE_NATIVE，CRASHTYPE_U3D ,CRASHTYPE_ANR
         * @param errorType    错误的类型名
         * @param errorMessage 错误的消息
         * @param errorStack   错误的堆栈
         * @return byte[] 额外的2进制内容进行上报
         */
        @Override
        public synchronized byte[] onCrashHandleStart2GetExtraDatas(int crashType, String errorType, String errorMessage, String errorStack) {

            return super.onCrashHandleStart2GetExtraDatas(crashType, errorType, errorMessage, errorStack);
        }
    }


    /**
     * Application---前后台切换统计
     */


    int activityCreateCount = 0;
    int activityStartCount = 0;
    ActivityLifecycleCallbacks activityLifecycleCallbacks = new ActivityLifecycleCallbacks() {

        //onCreate()----onDestroy()
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            //这种方式针对Crash赵成的Application重启onActivityCreated==0；
            Log.e("app", "onActivityCreated---" + activityCreateCount);
            if (activityCreateCount == 0) {
                Log.e("app", "App首次启动");
            }
            activityCreateCount++;
        }


        //onActivityStop
        @Override
        public void onActivityStarted(Activity activity) {
            if (activityStartCount == 0) {
                Log.e("app", "App Back to Front");
            }
            activityStartCount++;
        }

        //onActivityPause()
        @Override
        public void onActivityResumed(Activity activity) {

        }

        //onActivityResume()
        @Override
        public void onActivityPaused(Activity activity) {
        }


        //App进入后台后Activity会执行onStop的操作--并不是销毁而是会走onRestart--start
        @Override
        public void onActivityStopped(Activity activity) {
            activityStartCount--;
            if (activityStartCount == 0) {
                Log.e("app", "App Front to Back");
            }
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        //销毁--onDestroy()--会走onCreate()
        @Override
        public void onActivityDestroyed(Activity activity) {
            activityCreateCount--;
        }
    };

    private static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

}
