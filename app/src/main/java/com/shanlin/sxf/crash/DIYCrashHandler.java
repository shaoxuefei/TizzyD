package com.shanlin.sxf.crash;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.Looper;
import android.widget.Toast;

import com.shanlin.sxf.BuildConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

/**
 * @Description: android 内嵌的Crash的捕捉工具
 * @Auther: Sxf
 * @Date: 2017/12/7
 */

public class DIYCrashHandler implements Thread.UncaughtExceptionHandler {

    private Thread.UncaughtExceptionHandler defaultCrashHandler;
    private static DIYCrashHandler diyCrashHandler;
    private Context context;
    private String strThrow;
    private Intent intent;

    private DIYCrashHandler(Context context) {
        this.context = context;
        //此处是获取系统的默认的处理Crash的对象
        defaultCrashHandler = Thread.getDefaultUncaughtExceptionHandler();
        //设置回调处理-在该类的uncaughtException中处理(上边的defaultCrashHandler需要先获取，
        // 不然设置默认的以后在获取就是该类本身的对象，就无意义了，
        // 之所以获取改默认的CrashHandler对象，是因为自己不处理时仍然交给系统的uncaughtException方法处理-即调用方法使用defaultCrashHandler.uncaughtException())
        Thread.setDefaultUncaughtExceptionHandler(this);

    }

    public static DIYCrashHandler init(Context context) {
        if (diyCrashHandler == null) {
            diyCrashHandler = new DIYCrashHandler(context);
        }
        return diyCrashHandler;
    }

    //对Crash日志进行本地保存或者上传服务器
    private boolean saveErrorInSdCard(Throwable throwable) {
        StringBuffer stringBuffer = new StringBuffer();
        String versionName = BuildConfig.VERSION_NAME;
        int versionCode = BuildConfig.VERSION_CODE;
        stringBuffer.append("versionCode: " + versionCode).append("versionName：" + versionName);

        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        throwable.printStackTrace(printWriter);
        Throwable cause = throwable.getCause();
        while (cause != null) {
            //循环遍历--throwable的错误信息
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        String throwResult = printWriter.toString();
        strThrow = throwResult;
        stringBuffer.append("CrashContent: " + throwResult);
        long l = System.currentTimeMillis();
        String fileName = "crash-" + l + ".txt";
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            try {
                String absolutePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/TizzCrash/";
                File errorFile = new File(absolutePath);
                if (!errorFile.exists()) {
                    errorFile.mkdirs();//创建文件夹(也就是指定的目录，如果父目录不存在的话·，其也会创建父目录缺失的目录文件)
                    // createNewFile是生成文件（文件一般没有会自动创建而文件夹不会）
                    //mkdir()是创建一级文件夹目录：即如果父目录不存在的话，他不会创建父目录
                }
                FileOutputStream fileOutputStream = new FileOutputStream(absolutePath + fileName);
                fileOutputStream.write(stringBuffer.toString().getBytes());
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    //异常处理的回调
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();//为一个线程开启一个循环
                Toast.makeText(context, "程序出现异常", Toast.LENGTH_SHORT).show();
                Looper.loop();//说的Looper.loop()后边的代码不会执行，其是开启Looper的循环
            }
        }).start();
        try {
            Thread.sleep(2000);//此处的sleep是为了让Toast显示
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        saveErrorInSdCard(e);
        defaultCrashHandler.uncaughtException(t, e);
    }
}
