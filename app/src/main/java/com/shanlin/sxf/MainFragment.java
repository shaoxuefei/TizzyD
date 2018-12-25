package com.shanlin.sxf;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import com.shanlin.sxf.enums.EnumBean;
import com.shanlin.sxf.gson.GsonActivity;
import com.shanlin.sxf.paint.PaintActivity;
import com.shanlin.sxf.picture.PictureActivity;
import com.shanlin.sxf.service.MyVideoPlayService;
import com.shanlin.sxf.utils.FloatMessageWindow;
import com.shanlin.sxf.utils.MediaPlayUtils;
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

public class MainFragment extends BaseFragment {
    private LinearLayout linearRoot;
    private MyViewGroup myViewGroup;
    private StringBuilder stringBuilder;
    private StringBuffer stringBuffer;
    private String defaultFinalStr;

    @Override
    public int getLayoutId() {
        Log.e("aa", "MainFragment-----onCreateView");
        return R.layout.activity_main;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e("aa", "MainFragment-----onViewCreated");
    }

    @Override
    public void initView(View inflate) {
        initMyViewGroup(inflate);

        stringBuilder = new StringBuilder();
        stringBuilder.append("1");
        stringBuilder.append("2");


        stringBuffer = new StringBuffer();
        stringBuffer.append("1");
        stringBuffer.append("2");


    }

    BindServiceConnected serviceConnected;

    private void initMyViewGroup(View inflate) {
        linearRoot = (LinearLayout) inflate.findViewById(R.id.linearRoot);
        initContentView();
        checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        Toast.makeText(getContext(), "TENCENT_PATCH_HOT", Toast.LENGTH_SHORT).show();
        myViewGroup = (MyViewGroup) inflate.findViewById(R.id.myViewGroup);
        myViewGroup.addView(new MyGroupItemView(getContext(), EnumBean.valueOf("TAB01").getEnumName()));
        myViewGroup.addView(new MyGroupItemView(getContext(), EnumBean.valueOf("TAB02").getEnumName()));
        myViewGroup.addView(new MyGroupItemView(getContext(), EnumBean.valueOf("TAB03").getEnumName()));
        myViewGroup.addView(new MyGroupItemView(getContext(), EnumBean.valueOf("TAB04").getEnumName()));
        myViewGroup.addView(new MyGroupItemView(getContext(), EnumBean.valueOf("TAB05").getEnumName()));
        myViewGroup.addView(new MyGroupItemView(getContext(), EnumBean.valueOf("TAB06").getEnumName()));
        myViewGroup.addView(new MyGroupItemView(getContext(), EnumBean.valueOf("TAB07").getEnumName()));
        myViewGroup.addView(new MyGroupItemView(getContext(), EnumBean.valueOf("TAB08").getEnumName()));
        myViewGroup.addView(new MyGroupItemView(getContext(), EnumBean.valueOf("TAB09").getEnumName()));
        myViewGroup.addView(new MyGroupItemView(getContext(), EnumBean.valueOf("TAB10").getEnumName()));
        myViewGroup.addView(new MyGroupItemView(getContext(), EnumBean.valueOf("TAB11").getEnumName()));
        myViewGroup.addView(new MyGroupItemView(getContext(), EnumBean.valueOf("TAB12").getEnumName()));


        EnumBean tab01 = EnumBean.TAB01;
        EnumBean enumBean = EnumBean.valueOf("TAB01");
        EnumBean[] values = EnumBean.values();
        serviceConnected = new BindServiceConnected();
        myViewGroup.setCallBack(new ItemClickCallBack() {
            @Override
            public void onItemClick(int position) {
                switch (position) {
                    case 0:
                        Intent intent = new Intent(getContext(), Main2Activity.class);
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.enter_in_anim, R.anim.insert_stay);
                        break;
                    case 1:
                        Intent intent1 = new Intent(getContext(), SeekBarActivity.class);
                        startActivity(intent1);
                        break;
                    case 2:
                        Intent intent2 = new Intent(getContext(), PopubWindowActivity.class);
                        startActivity(intent2);
                        break;
                    case 3:
                        Intent intent4 = new Intent(getContext(), GsonActivity.class);
                        startActivity(intent4);
                        break;
                    case 4:
                        requestUrl();
                        break;
                    case 5:
                        //手动埋点+自定义事件信息(eventId、params)
                        turnToPicture();
                        Properties properties = new Properties();
                        properties.put("name", "选择图片");
                        StatService.trackCustomKVEvent(getContext(), "onClick", properties);
                        break;
                    case 6:
                        initDialogFragment();

                        Properties properties2 = new Properties();
                        properties2.put("name", "战士");
                        properties2.put("price", 250);
                        properties2.put("playerLevel", 5);
                        StatService.trackCustomKVEvent(getContext(), "buy", properties2);

                        break;
                    case 7:

                        break;
                    case 8:
                        Intent intent10 = new Intent(getContext(), MainTouchActivity.class);
                        startActivity(intent10);
                        break;
                    case 9:
                        Intent intent11 = new Intent(getContext(), MyWebViewActivity.class);
                        startActivity(intent11);
                        break;
                    case 10:
                        Intent intent13 = new Intent(getContext(), MethodRangeActivity.class);
                        startActivity(intent13);
                        break;
                    case 11:
//                        String voicePath = Environment.getExternalStorageDirectory() + "/msc/tts.wav";
//                        mediaPlayer.startPlay(voicePath);
//                        if (serviceConnected != null && isConnected) {
//                            getContext().unbindService(serviceConnected);
//                        }
//                        Intent intentService = new Intent("android.intent.action.VIDEO_PLAY_SERVICE");
//                        intentService.setPackage(BuildConfig.APPLICATION_ID);
//                        getContext().bindService(intentService, serviceConnected, Context.BIND_AUTO_CREATE);

                        FloatMessageWindow.getInstance(getContext()).showWindows();

                        break;
                    default:
                        break;
                }

            }
        });
    }

    MyVideoPlayService.MyBinder myBinder;
    boolean isConnected;

    class BindServiceConnected implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            myBinder = (MyVideoPlayService.MyBinder) service;
            isConnected = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isConnected = false;
        }
    }


    private void doInitVideo() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(getContext())) {
                //若未授权则请求权限
                Intent intent14 = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                intent14.setData(Uri.parse("package:" + getContext().getPackageName()));
                this.startActivity(intent14);
            } else {
                FloatMessageWindow.getInstance(getContext()).showWindows();
            }
        }
    }

    public ImageView image;
    private Button btnSign;

    private void initContentView() {
        ContentView contentView = new ContentView(getContext());
        SignNameView signNameView = new SignNameView(getContext());
        image = (ImageView) signNameView.findViewById(R.id.image);
        btnSign = (Button) signNameView.findViewById(R.id.btnSign);
        btnSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), PaintActivity.class);
                startActivityForResult(intent, 0x31);
            }
        });
        linearRoot.addView(contentView);
        linearRoot.addView(signNameView);
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
                getActivity().startService(new Intent());//service默认是运行在mainThread 中
            }
        });
    }

    public void turnToPicture() {
        Intent intent = new Intent(getContext(), PictureActivity.class);
        startActivity(intent);
    }

    public void initDialogFragment() {
        MyDialogFragment myDialogFragment = MyDialogFragment.newInstance();
        myDialogFragment.show(getFragmentManager(), "MyDialogFragment");

    }

    private boolean checkPermission(String permission) {
        if (!(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{permission}, 10);
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 10) {
            if (grantResults.length > 0) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 10);
                }
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 10);
            }
        }
    }

    //TODO 这种方法只有在手动调用Hide、show或者依赖的Activity的声明周期改变(onResume、onPause)的时候其才会执行、、、而这种ViewPager的来回切换时是不会执行的
    @Override
    public void onResume() {
        super.onResume();
        Log.e("aa", "MainFragment-----onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("aa", "MainFragment-----onPause");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e("aa", "MainFragment-----onDestroyView");
    }


    @Override
    public void fragmentIsVisible(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            Toast.makeText(getContext(), "MainFragment--LoadData", Toast.LENGTH_SHORT).show();
            Log.e("aa", "MainFragment-----isShow");
        } else {
            Log.e("aa", "MainFragment-----isHide");
        }
    }

}
