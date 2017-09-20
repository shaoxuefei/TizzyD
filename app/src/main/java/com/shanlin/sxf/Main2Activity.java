package com.shanlin.sxf;

import android.content.Intent;
import android.os.Handler;
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

import com.shanlin.sxf.service.MyIntentService;
import com.shanlin.sxf.service.MyService;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener {
    private TextView snackbar_text;
    private TextInputEditText editInputText;
    private EditText editText;
    private TextInputLayout textInputLayout;
    private FloatingActionButton floatButton;
    private Button collection,intentService,service,broadCast,broadOrderCast;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        initView();
    }

    private void initView(){
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
            MyIntentService.startIntentService(this,new MyHandler());
            intentService.setText("upload");
        }else if(id==R.id.service){
            MyService.startMyService(this,new MyHandler());
            service.setText("upload");
        }else if(id==R.id.broadCast){
            //广播一般都是通过  隐式意图来发的--这样可以 发送一个广播  导致 多个接受者接受到广播
            Intent intent=new Intent();
            intent.setAction("com.myBrocaCast");
            sendBroadcast(intent);
        }else if(id==R.id.orderBroadCast){
            Intent intent=new Intent();
            intent.setAction("com.myBrocaCast");
            sendOrderedBroadcast(intent,null);
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
            }
            Log.e("aa","handler回调");
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0,R.anim.enter_out_anim);
    }
}
