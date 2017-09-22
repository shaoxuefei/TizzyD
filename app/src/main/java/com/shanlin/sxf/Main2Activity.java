package com.shanlin.sxf;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.shanlin.sxf.broadcast.MyBroadCastBReceive;
import com.shanlin.sxf.service.MyIntentService;
import com.shanlin.sxf.service.MyService;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener {
    private TextView snackbar_text;
    private TextInputEditText editInputText;
    private EditText editText;
    private TextInputLayout textInputLayout;
    private FloatingActionButton floatButton;
    private Button collection,intentService,service,broadCast,broadOrderCast,threadButton,handlerThread;
    private MyHandler myHandler;
    private Handler handler;
    private MyRunable myRunable;
    private Thread thread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        initView();
    }

    private void initView(){
        handler=new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                //在主线程中post(Runnable)--其runnable方法就是主线程，不会走这个handlerMessage回调
                Log.e("aa","MainThread_HandlerMessageRunnable");
                return false;
            }
        });
        myRunable=new MyRunable();
        myHandler=new MyHandler();
        handlerThread= (Button) findViewById(R.id.handlerThread);
        threadButton= (Button) findViewById(R.id.thread);
        broadCast= (Button) findViewById(R.id.broadCast);
        broadOrderCast= (Button) findViewById(R.id.orderBroadCast);
        service= (Button) findViewById(R.id.service);
        intentService= (Button) findViewById(R.id.intentService);
        collection= (Button) findViewById(R.id.collection);
        textInputLayout= (TextInputLayout) findViewById(R.id.textInputLayout);
        snackbar_text= (TextView) findViewById(R.id.snackbar_text);
        editInputText= (TextInputEditText) findViewById(R.id.editInputText);
        editText= (EditText) findViewById(R.id.editText);
        floatButton= (FloatingActionButton) findViewById(R.id.floatButton);
        floatButton.setOnClickListener(this);
        snackbar_text.setOnClickListener(this);
        collection.setOnClickListener(this);
        intentService.setOnClickListener(this);
        service.setOnClickListener(this);
        broadCast.setOnClickListener(this);
        broadOrderCast.setOnClickListener(this);
        threadButton.setOnClickListener(this);
        handlerThread.setOnClickListener(this);
        EditText editText = textInputLayout.getEditText();
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()>4){
                    textInputLayout.setErrorEnabled(true);
                    textInputLayout.setError("PasswordError");
                }else {
//                    textInputLayout.setEnabled(false);//设置是否可以输入
                    textInputLayout.setErrorEnabled(false);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id==R.id.snackbar_text){
            Snackbar.make(snackbar_text,"SnackBarToast",Snackbar.LENGTH_SHORT).setAction("Action", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Snackbar.make(snackbar_text,"Action",Snackbar.LENGTH_SHORT).show();
                }
            }).show();
        }else if(id==R.id.floatButton){
            Snackbar.make(floatButton,"FloatActionButton",Snackbar.LENGTH_SHORT).setAction("Intent", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(Main2Activity.this,Main3Activity.class);
                    startActivity(intent);
                }
            }).show();
        }else if(id==R.id.collection){
            new Miss().useCollection();
        }else if(id==R.id.intentService){
            MyIntentService.startIntentService(this,myHandler);
            intentService.setText("upload");
        }else if(id==R.id.service){
            MyService.startMyService(this,myHandler);
            service.setText("upload");
        }else if(id==R.id.broadCast){
            //广播一般都是通过  隐式意图来发的--这样可以 发送一个广播  导致 多个接受者接受到广播
            Intent intent=new Intent();
            intent.setAction("com.myBrocaCast");
            sendBroadcast(intent);
        }else if(id==R.id.orderBroadCast){
            //进行BroadCastReceive的动态注册-
            MyBroadCastBReceive bReceive=new MyBroadCastBReceive();
            IntentFilter intentFilter=new IntentFilter();
            intentFilter.addAction("com.myBrocaCast");//设置接受广播的Action
            intentFilter.setPriority(100);
            registerReceiver(bReceive,intentFilter);
            //发送广播
            Intent intent=new Intent();
            intent.setAction("com.myBrocaCast");
            sendOrderedBroadcast(intent,null);
        }else if(id==R.id.thread){
            //1.0
            thread=new Thread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(Main2Activity.this,"Thread",Toast.LENGTH_SHORT).show();
                }
            });
            thread.start();
            //2.0
            myHandler.sendEmptyMessage(3);
            //3.0
            //通过Handler2s后开始走这个回调
            handler.postDelayed(myRunable,2000);
            //这并不是开启线程，只是单纯的post(可以理解为发送)一个空的Message，不含Message对象，然后走一下回调，，这种有个handler.removeCallBack(runable);--可以清空消息队列，如果在子线程中多次重复发消息的话
            handler.post(myRunable);
        }else if(id==R.id.handlerThread){
            //HandlerThread的使用-
            HandlerThread handlerThr=new HandlerThread("HandlerThread");
            handlerThr.start();//开启线程
            Looper looper = handlerThr.getLooper();
            Handler handler=new Handler(looper, new Handler.Callback() {
                @Override
                public boolean handleMessage(Message msg) {
                    //没有发送Message  不会走这个回调--而且该Handler是在子线程中创建的--所以其handlerMessage()回调也是在子线程中的回调--handlerMessage()的回调线程是个创建Handler的线程在一起
                    //故 这个回调里也不能更新UI
                    Log.e("aa","HandlerThread_Runnable_HandlerMessage");
                    return false;
                }
            });//该Handler就是在子线程中生命的Handler---并且  你不需要Looper.prepare();Looper.loop();
            handlerThread.setText("upload");
            if(handler!=null) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        // 子Thread-不能更新UI
                        //异步请求
                      Toast.makeText(Main2Activity.this,"异步请求",Toast.LENGTH_SHORT).show();

                    }
                });
            }
        }
    }

    class MyRunable implements Runnable{

        @Override
        public void run() {
            //MainThread-更新UI--right
            threadButton.setText("Runnable");
            Log.e("aa","MainThread_Runnable");
        }

    }
    class MyHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    intentService.setText("Success");
                    break;
                case 2:
                    service.setText("Success");
                    break;
                case 3:
                    //即使Activity销毁--其也不会停止，会继续显示Toast
                    Toast.makeText(Main2Activity.this,"Handler",Toast.LENGTH_SHORT).show();
                    break;
            }
            Log.e("aa","handler回调");
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0,R.anim.enter_out_anim);
        myHandler.removeCallbacksAndMessages(null);//清楚Handler以HandlerMessage()回调方式创建的Handler--Message信息
        handler.removeCallbacks(myRunable);//清楚Handler 以Post(Runable)--方式放的post回调
        thread.stop();//一般会用HandlerThread来处理线程和其回调，，handler.removeCallbacks(myRunable)：
    }
}
