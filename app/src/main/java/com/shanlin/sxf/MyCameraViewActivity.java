package com.shanlin.sxf;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.cameraview.CameraView;

/**
 * 针对google的封装好的CameraView--来展示摄像头
 */

public class MyCameraViewActivity extends AppCompatActivity {

    private CameraView cameraView;
    private ImageView imgPicture;
    private ImageView imagePic;
    private Button extraBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_camera_view);

        initView();
    }

    private void initView() {
        extraBtn = findViewById(R.id.extraBtn);
        imagePic = (ImageView) findViewById(R.id.imagePic);
        imgPicture = (ImageView) findViewById(R.id.imgPicture);
        imgPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraView.takePicture();
            }
        });
        cameraView = (CameraView) findViewById(R.id.cameraView);
        cameraView.addCallback(new CameraView.Callback() {
            @Override
            public void onCameraOpened(CameraView cameraView) {
                super.onCameraOpened(cameraView);

            }

            @Override
            public void onCameraClosed(CameraView cameraView) {
                super.onCameraClosed(cameraView);
            }

            @Override
            public void onPictureTaken(CameraView cameraView, byte[] data) {
                super.onPictureTaken(cameraView, data);
                //拍照回调
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                imagePic.setImageBitmap(bitmap);
            }
        });
        cameraView.setAutoFocus(true);
        try {
            cameraView.start();
        } catch (Exception e) {

        }
        extraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyCameraViewActivity.this, MyCamera2Activity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraView.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cameraView.stop();
    }
}
