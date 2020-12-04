package com.shanlin.sxf;

import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * @Author: SXF
 * @CreateDate: 2020/12/3 18:14
 */
public class ThreadFactoryActivity extends AppCompatActivity {
    int executorServiceStart = 1;
    int scheduledService = 1;
    int scheduleDelayService = 1;
    int scheduleStartCount = 1;
    ExecutorService executorService;
    ScheduledExecutorService scheduledExecutorService;
    ScheduledExecutorService scheduledExecutorDelayService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread_factory);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkFactory();
            }
        });

        findViewById(R.id.buttonSchedule).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkScheduleExecutor();
            }
        });

        findViewById(R.id.buttonScheduleDelay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scheduleServiceDelay();
            }
        });
    }

    private void checkFactory() {
        if (executorService == null) {
            executorService = Executors.newFixedThreadPool(10);
        }
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                Log.e("aa", "ExecutorService--run" + executorServiceStart + "currentThread:" + Thread.currentThread() + "isMainThread:" + (Thread.currentThread() == Looper.getMainLooper().getThread()));
                executorServiceStart++;
                //点击多次请求
                //12-04 10:00:08.029 2998-3140/com.shanlin.sxf E/aa: ExecutorService--run1currentThread:Thread[pool-5-thread-1,5,main]isMainThread:false
                //12-04 10:00:14.686 2998-3147/com.shanlin.sxf E/aa: ExecutorService--run2currentThread:Thread[pool-5-thread-2,5,main]isMainThread:false
                //12-04 10:00:20.095 2998-3153/com.shanlin.sxf E/aa: ExecutorService--run3currentThread:Thread[pool-5-thread-3,5,main]isMainThread:false
                //12-04 10:00:22.761 2998-3154/com.shanlin.sxf E/aa: ExecutorService--run4currentThread:Thread[pool-5-thread-4,5,main]isMainThread:false
                //12-04 10:00:25.019 2998-3156/com.shanlin.sxf E/aa: ExecutorService--run5currentThread:Thread[pool-5-thread-5,5,main]isMainThread:false
                //12-04 10:00:27.865 2998-3157/com.shanlin.sxf E/aa: ExecutorService--run6currentThread:Thread[pool-5-thread-6,5,main]isMainThread:false
            }
        });

    }

    private void checkScheduleExecutor() {
        if (scheduledExecutorService == null) {
            scheduledExecutorService = Executors.newScheduledThreadPool(10, new ThreadFactory() {
                @Override
                public Thread newThread(Runnable r) {
                    Log.e("aa", "scheduledExecutorService---start" + scheduleStartCount + "currentThread:" + Thread.currentThread() + "isMainThread:" + (Thread.currentThread() == Looper.getMainLooper().getThread()));
                    scheduleStartCount++;
                    //12-04 10:01:24.950 2998-2998/com.shanlin.sxf E/aa: scheduledExecutorService---start1currentThread:Thread[main,5,main]isMainThread:true
                    return new Thread(r);
                }
            });
        }
        scheduledExecutorService.schedule(new Runnable() {
            @Override
            public void run() {
                Log.e("aa", "scheduledExecutorService--run" + scheduledService + "currentThread:" + Thread.currentThread() + "isMainThread:" + (Thread.currentThread() == Looper.getMainLooper().getThread()));
                scheduledService++;
                //runAble方法是延迟5s之后执行
                //12-04 10:01:29.950 2998-3174/com.shanlin.sxf E/aa: scheduledExecutorService--run1currentThread:Thread[Thread-2550,5,main]isMainThread:false
            }
        }, 5, TimeUnit.SECONDS);
    }

    private void scheduleServiceDelay() {
        if (scheduledExecutorDelayService == null) {
            scheduledExecutorDelayService = Executors.newScheduledThreadPool(10);
        }
        scheduledExecutorDelayService.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                Log.e("aa", "scheduledExecutorDelayService--run" + scheduleDelayService + "currentThread:" + Thread.currentThread() + "isMainThread:" + (Thread.currentThread() == Looper.getMainLooper().getThread()));
                scheduleDelayService++;
                if(scheduleDelayService==10){
                    scheduledExecutorDelayService.shutdown();
                }
            }
        }, 0, 10, TimeUnit.SECONDS);
    }
}
