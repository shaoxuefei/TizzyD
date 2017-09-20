package com.shanlin.sxf;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.shanlin.sxf.api.ApiModule;
import com.shanlin.sxf.diyview.TreasureViewActivity;
import com.shanlin.sxf.gson.GsonActivity;
import com.shanlin.sxf.paint.PaintActivity;
import com.shanlin.sxf.picture.PictureActivity;
import com.slfinance.facesdk.ui.IDCardScanActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private Button button,button2,button3,button4,button5,treasureView,gsonButton,request,picture;
    private LinearLayout linearRoot;
    private ScrollView scrollView;
    private String string;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState!=null) {
            String saveId = savedInstanceState.getString("saveId");
            if (!TextUtils.isEmpty(saveId)) {
                Toast.makeText(this, saveId, Toast.LENGTH_SHORT).show();
            }
        }
        setContentView(R.layout.activity_main);
        linearRoot= (LinearLayout) findViewById(R.id.linearRoot);
        button= (Button) findViewById(R.id.button);
        button2= (Button) findViewById(R.id.button2);
        button3= (Button) findViewById(R.id.button3);
        button4= (Button) findViewById(R.id.seekBar);
        button5= (Button) findViewById(R.id.popubWindow);
        request= (Button) findViewById(R.id.request);
        gsonButton= (Button) findViewById(R.id.gsonButton);
        scrollView= (ScrollView) findViewById(R.id.scrollView);
        treasureView= (Button) findViewById(R.id.treasureView);
        picture= (Button) findViewById(R.id.picture);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, IDCardScanActivity.class);
                startActivity(intent);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,Main2Activity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.enter_in_anim,R.anim.insert_stay);
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,SingerActivity.class);
                startActivity(intent);
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,SeekBarActivity.class);
                startActivity(intent);
            }
        });
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,PopubWindowActivity.class);
                startActivity(intent);
            }
        });
        treasureView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, TreasureViewActivity.class);
                startActivity(intent);
            }
        });
        gsonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, GsonActivity.class);
                startActivity(intent);
            }
        });
        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestUrl();
            }
        });

        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                turnToPicture();
            }
        });
        initContentView();

    }

    private ImageView image;
    private Button btnSign;
    private final int SIGN_REQUEST_CODE=0x31;
    private void initContentView(){
        ContentView contentView=new ContentView(this);
        SignNameView signNameView=new SignNameView(this);
        image= (ImageView) signNameView.findViewById(R.id.image);
        btnSign= (Button) signNameView.findViewById(R.id.btnSign);
        btnSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, PaintActivity.class);
                startActivityForResult(intent,SIGN_REQUEST_CODE);
            }
        });
        linearRoot.addView(contentView);
        linearRoot.addView(signNameView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK&&requestCode==SIGN_REQUEST_CODE){
            if(data!=null) {
                byte[] imageBytes = data.getByteArrayExtra("imageByte");
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                image.setImageBitmap(bitmap);
            }
        }
    }


    public void requestUrl(){
        String absolutePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/Screenshots";
        File file = new File(absolutePath);
        File fileName = new File(file, "timg.jpg");
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpeg"), fileName);
        MultipartBody.Part part = MultipartBody.Part.createFormData("multipartFile", fileName.getName(), requestBody);
        Map<String, RequestBody> hashMap = new HashMap<>();
        RequestBody requestBody1=RequestBody.create(MediaType.parse("application/json"),"");
        hashMap.put("request",requestBody1);
        ApiModule.getInstance()
                .getApiUrl()
                .postPic(part,hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject jsonObject) {

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });

        new Thread(new Runnable() {
            @Override
            public void run() {
                //子线程startActivity()需要开启个新栈，防止跟主线程activity冲突
                startService(new Intent());//service默认是运行在mainThread 中
            }
        });
    }

    public void turnToPicture(){
        //调用系统相册
        Intent intent=new Intent(this,PictureActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Bundle bundle=new Bundle();
        bundle.putString("saveId","HelloWorld");
        onSaveInstanceState(bundle);
    }
    //Activity-不会主动调用--需要手动调用--一般在onPause()之前调用
    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        Log.e("aa","onSaveInstance-Activity");
    }
    //AppCompatActivity
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.e("aa","onSaveInstance_AppCompatActivity");
    }
}
