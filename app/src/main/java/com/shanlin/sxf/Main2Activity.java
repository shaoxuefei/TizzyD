package com.shanlin.sxf;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener {
    private TextView snackbar_text;
    private TextInputEditText editInputText;
    private EditText editText;
    private TextInputLayout textInputLayout;
    private FloatingActionButton floatButton;
    private Button collection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        initView();
    }

    private void initView(){
        collection= (Button) findViewById(R.id.collection);
        textInputLayout= (TextInputLayout) findViewById(R.id.textInputLayout);
        snackbar_text= (TextView) findViewById(R.id.snackbar_text);
        editInputText= (TextInputEditText) findViewById(R.id.editInputText);
        editText= (EditText) findViewById(R.id.editText);
        floatButton= (FloatingActionButton) findViewById(R.id.floatButton);
        floatButton.setOnClickListener(this);
        snackbar_text.setOnClickListener(this);
        collection.setOnClickListener(this);
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
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0,R.anim.enter_out_anim);
    }
}
