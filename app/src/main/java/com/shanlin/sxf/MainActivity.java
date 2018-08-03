package com.shanlin.sxf;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.shanlin.sxf.api.ApiModule;
import com.shanlin.sxf.dialog.MyDialogFragment;
import com.shanlin.sxf.diyview.ItemClickCallBack;
import com.shanlin.sxf.diyview.MyGroupItemView;
import com.shanlin.sxf.diyview.MyViewGroup;
import com.shanlin.sxf.diyview.TreasureViewActivity;
import com.shanlin.sxf.gson.GsonActivity;
import com.shanlin.sxf.paint.PaintActivity;
import com.shanlin.sxf.picture.PictureActivity;
import com.tencent.stat.StatService;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private LinearLayout linearRoot;
    private MyViewGroup myViewGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            String saveId = savedInstanceState.getString("saveId");
            if (!TextUtils.isEmpty(saveId)) {
                Toast.makeText(this, saveId, Toast.LENGTH_SHORT).show();
            }
        }
        setContentView(R.layout.activity_main);
        linearRoot = (LinearLayout) findViewById(R.id.linearRoot);
        initMyViewGroup();
        initContentView();
        checkPermission();

        Toast.makeText(this, "TENCENT_PATCH_HOT", Toast.LENGTH_SHORT).show();
    }

    private void initMyViewGroup() {
        myViewGroup = (MyViewGroup) findViewById(R.id.myViewGroup);
        myViewGroup.addView(new MyGroupItemView(this, "随性一笔"));
        myViewGroup.addView(new MyGroupItemView(this, "SeekBar"));
        myViewGroup.addView(new MyGroupItemView(this, "PopWindow-List"));
        myViewGroup.addView(new MyGroupItemView(this, "饼状图/表"));
        myViewGroup.addView(new MyGroupItemView(this, "gson/fastJson-特殊字符转译"));
        myViewGroup.addView(new MyGroupItemView(this, "Rxjava-请求示例"));
        myViewGroup.addView(new MyGroupItemView(this, "Pic选择"));
        myViewGroup.addView(new MyGroupItemView(this, "DialogFragment"));
        myViewGroup.addView(new MyGroupItemView(this, "My DIY View"));
        myViewGroup.addView(new MyGroupItemView(this, "跳转App"));
        myViewGroup.addView(new MyGroupItemView(this, "事件分析"));
        myViewGroup.addView(new MyGroupItemView(this, "重定项-URL2JS"));
        myViewGroup.addView(new MyGroupItemView(this, "ScrollCompat"));
        myViewGroup.addView(new MyGroupItemView(this, "方法作用域"));
        myViewGroup.setCallBack(new ItemClickCallBack() {
            @Override
            public void onItemClick(int position) {
                switch (position) {
                    case 0:
                        Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.enter_in_anim, R.anim.insert_stay);
                        break;
                    case 1:
                        Intent intent1 = new Intent(MainActivity.this, SeekBarActivity.class);
                        startActivity(intent1);
                        break;
                    case 2:
                        Intent intent2 = new Intent(MainActivity.this, PopubWindowActivity.class);
                        startActivity(intent2);
                        break;
                    case 3:
                        Intent intent3 = new Intent(MainActivity.this, TreasureViewActivity.class);
                        startActivity(intent3);
                        break;
                    case 4:
                        Intent intent4 = new Intent(MainActivity.this, GsonActivity.class);
                        startActivity(intent4);
                        break;
                    case 5:
                        requestUrl();
                        break;
                    case 6:
                        //手动埋点+自定义事件信息(eventId、params)
                        turnToPicture();
                        Properties properties = new Properties();
                        properties.put("name", "选择图片");
                        StatService.trackCustomKVEvent(MainActivity.this, "onClick", properties);
                        break;
                    case 7:
                        initDialogFragment();

                        Properties properties2 = new Properties();
                        properties2.put("name", "战士");
                        properties2.put("price", 250);
                        properties2.put("playerLevel", 5);
                        StatService.trackCustomKVEvent(MainActivity.this, "buy", properties2);

                        break;
                    case 8:
                        Intent intent8 = new Intent(MainActivity.this, MyViewActivity.class);
                        startActivity(intent8);

                        Properties properties1 = new Properties();
                        properties1.put("name", "自定义View");
                        StatService.trackCustomKVEvent(MainActivity.this, "onClick", properties1);

                        break;
                    case 9:
                        Intent intent9 = new Intent();
                        intent9.setAction("android.intent.action.MAIN");
                        intent9.addCategory("android.intent.category.LAUNCHER");
                        String packageName = "com.shanlin.nexu";
                        String activityName = "com.shanlin.nexu.MainActivity";
                        ComponentName componentName = new ComponentName(packageName, activityName);
                        intent9.setComponent(componentName);
//                        startService(intent9);
                        startActivity(intent9);
                        break;
                    case 10:
                        Intent intent10 = new Intent(MainActivity.this, MainTouchActivity.class);
                        startActivity(intent10);
                        break;
                    case 11:
                        Intent intent11 = new Intent(MainActivity.this, MyWebViewActivity.class);
                        startActivity(intent11);
                        break;
                    case 12:
                        Intent intent12 = new Intent(MainActivity.this, RecyclerViewDecorationActivity.class);
                        startActivity(intent12);
                        break;
                    case 13:
                        Intent intent13 = new Intent(MainActivity.this, MethodRangeActivity.class);
                        startActivity(intent13);
                        break;
                    default:
                        break;
                }

            }
        });
    }


    private ImageView image;
    private Button btnSign;
    private final int SIGN_REQUEST_CODE = 0x31;

    private void initContentView() {
        ContentView contentView = new ContentView(this);
        SignNameView signNameView = new SignNameView(this);
        image = (ImageView) signNameView.findViewById(R.id.image);
        btnSign = (Button) signNameView.findViewById(R.id.btnSign);
        btnSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PaintActivity.class);
                startActivityForResult(intent, SIGN_REQUEST_CODE);
            }
        });
        linearRoot.addView(contentView);
        linearRoot.addView(signNameView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == SIGN_REQUEST_CODE) {
            if (data != null) {
                byte[] imageBytes = data.getByteArrayExtra("imageByte");
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                image.setImageBitmap(bitmap);
            }
        }
    }


    public void requestUrl() {
        String absolutePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/Screenshots";
        File file = new File(absolutePath);
        File fileName = new File(file, "timg.jpg");
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpeg"), fileName);
        MultipartBody.Part part = MultipartBody.Part.createFormData("multipartFile", fileName.getName(), requestBody);
        Map<String, RequestBody> hashMap = new HashMap<>();
        RequestBody requestBody1 = RequestBody.create(MediaType.parse("application/json"), "");
        hashMap.put("request", requestBody1);
        ApiModule.getInstance()
                .getApiUrl()
                .postPic(part, hashMap)
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

    public void turnToPicture() {
        Intent intent = new Intent(this, PictureActivity.class);
        startActivity(intent);
    }

    public void initDialogFragment() {
        MyDialogFragment myDialogFragment = MyDialogFragment.newInstance();
        myDialogFragment.show(getSupportFragmentManager(), "MyDialogFragment");

    }

    @Override
    protected void onPause() {
        super.onPause();
        Bundle bundle = new Bundle();
        bundle.putString("saveId", "HelloWorld");
        onSaveInstanceState(bundle);
    }

    //Activity-不会主动调用--需要手动调用--一般在onPause()之前调用
    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        Log.e("aa", "onSaveInstance-Activity");
    }

    //AppCompatActivity
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.e("aa", "onSaveInstance_AppCompatActivity");
    }


    private void checkPermission() {
        if (!(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 10);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 10) {
            if (grantResults.length > 0) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 10);
                }
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 10);
            }
        }
    }
}
