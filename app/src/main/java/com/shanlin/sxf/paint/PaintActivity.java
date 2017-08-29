package com.shanlin.sxf.paint;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.shanlin.sxf.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Sxf on 2017/5/10.
 *
 * @project: Demo.
 * @detail:
 */
public class PaintActivity extends AppCompatActivity implements View.OnClickListener {
    private FrameLayout frameLayout;
    private PaintView paintView;
    private TextView sure,clean,cancle;
    DisplayMetrics metris;
    private ImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paintactivity);
        initView();
    }

    private void initView(){
         metris=new DisplayMetrics();
        Display defaultDisplay = getWindowManager().getDefaultDisplay();
        defaultDisplay.getMetrics(metris);
        sure= (TextView) findViewById(R.id.sure);
        clean= (TextView) findViewById(R.id.clean);
        cancle= (TextView) findViewById(R.id.cancel);
        image= (ImageView) findViewById(R.id.image);
        sure.setOnClickListener(this);
        clean.setOnClickListener(this);
        cancle.setOnClickListener(this);
        paintView=new PaintView(this,defaultDisplay.getWidth(), (int) (300*metris.density));
        frameLayout= (FrameLayout) findViewById(R.id.frameLayout);
        frameLayout.addView(paintView);
    }
    private  Subscription subscription;
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.sure:
                Bitmap pathBitmap = paintView.getPathBitmap(200 * metris.density, 100 * metris.density);
                if(pathBitmap!=null) {
                    image.setImageBitmap(pathBitmap);
                    try {
                        saveFile(pathBitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                subscription = Observable.just(pathBitmap)
                        .map(new Func1<Bitmap, byte[]>() {
                            @Override
                            public byte[] call(Bitmap bitmap) {
                                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                                byte[] bytes = outputStream.toByteArray();
                                return bytes;
                            }
                        }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<byte[]>() {
                            @Override
                            public void call(byte[] bytes) {
                                Intent intent = new Intent();
                                intent.putExtra("imageByte", bytes);
                                setResult(RESULT_OK, intent);
                                finish();
                            }
                        });
                break;
            case R.id.clean:
                paintView.cleanCavans();
                break;
            case R.id.cancel:
                finish();
                break;
        }
    }
    FileOutputStream outputStream;
    private void saveFile(Bitmap bitmap) throws IOException {
        File file=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath());
        File filePath=new File(file,getPackageName());
        if(!filePath.exists()){
            filePath.mkdir();
        }
        File fileName=new File(filePath,System.currentTimeMillis()+".png");
        try {
            outputStream=new FileOutputStream(fileName);
            bitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream);
            outputStream.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if(outputStream!=null){
                outputStream.close();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(subscription!=null&&subscription.isUnsubscribed()){
            subscription.unsubscribe();
        }
    }
}