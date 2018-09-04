package com.shanlin.sxf;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.camera2.CameraCharacteristics;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.shanlin.sxf.diyview.MyDiyImageView;
import com.shanlin.sxf.picture.FileUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import static android.hardware.camera2.CameraCharacteristics.*;

public class MyCameraActivity extends AppCompatActivity implements TextureView.SurfaceTextureListener {
    private MyDiyImageView myDiyImageView;
    private TextureView surfaceView;
    private Camera camera;
    private ImageView btnPicture;
    private ImageView picImage, image;
    private Button extraBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_camera);
        checkPermission();
    }

    private void initView() {
        camera = Camera.open();

        image = findViewById(R.id.image);
        extraBtn = (Button) findViewById(R.id.extraBtn);
        extraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyCameraActivity.this, MyCameraViewActivity.class);
                startActivity(intent);
            }
        });

        picImage = findViewById(R.id.picImage);
        surfaceView = findViewById(R.id.surfaceView);
        btnPicture = findViewById(R.id.btnPicture);
        //TextureView--的在清单中设置硬件加速后"        android:hardwareAccelerated="false""  承载的Camera不会显示，，用SurfaceView就可以承載
        surfaceView.setSurfaceTextureListener(this);
        /**
         * SurfaceTexture  就类似于画布不同於   SurfaceView(基本已经废弃)------TextureView
         * TextureView必须工作在开启硬件加速的环境中
         * 但假设写成false，能够看到onSurfaceTextureAvailable()这个回调就进不来了。TextureView没有了SurfaceTexture还玩个屁啊。
         */
        btnPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (camera == null) {
                    return;
                }
                camera.takePicture(null, null, new Camera.PictureCallback() {
                    @Override
                    public void onPictureTaken(byte[] data, Camera camera) {
                        if (data != null && data.length > 0) {
                            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                            File file = new File(Environment.getExternalStorageDirectory(), "diyName.jpeg");
                            try {
                                if (!file.exists()) {
                                    file.createNewFile();
                                }
                                FileOutputStream fileOutputStream = new FileOutputStream(file);
                                ByteArrayOutputStream outputFile = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputFile);
                                int length1 = outputFile.toByteArray().length;
                                Log.e("aa", "picBeforeSize" + length1);

                                //获取 图片旋转角度
                                int bitmapDegress = FileUtil.getBitmapDegress(file.getAbsolutePath());
                                Bitmap bitmap1 = FileUtil.roateBitmapByDgree(bitmap, bitmapDegress);
                                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                                bitmap1.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                                int length = outputStream.toByteArray().length;
                                Log.e("aa", "picSize" + length);
                                picImage.setImageBitmap(bitmap1);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        camera.startPreview();//拍完照后--重新开启预览模式
                    }
                });
            }
        });
        surfaceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (camera != null) {
                    //设置点击相机--自动对焦
                    camera.autoFocus(new Camera.AutoFocusCallback() {
                        @Override
                        public void onAutoFocus(boolean success, Camera camera) {

                        }
                    });
                }
            }
        });

        myDiyImageView = (MyDiyImageView) findViewById(R.id.myDiyImageView);

        float density = getResources().getDisplayMetrics().density;
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int screenHeight = getResources().getDisplayMetrics().heightPixels;
        int left = (int) (density * 30);
        int top = (int) (density * 100);
        int right = screenWidth - (int) (density * 30);
        int bottom = screenHeight - (int) (density * 100) - top;
        Rect rect = new Rect(left, top, right, bottom);
        myDiyImageView.setRecetCenter(rect);
    }

    private void setLayout() {
        try {
            int widthPixels = 0;
            int heightPixels = 0;
            //设置图片的分辨率
            Camera.Parameters parameters = camera.getParameters();
            //获取camera可支持的预览大小--其实这些都是像素大小
            List<Camera.Size> supportedPreviewSizes = parameters.getSupportedPreviewSizes();
            //获取支持最大的宽高
            for (int i = 0; i < supportedPreviewSizes.size(); i++) {
                Camera.Size size = supportedPreviewSizes.get(i);
                if (size.width > widthPixels) {
                    widthPixels = size.width;
                }
                if (size.height > heightPixels) {
                    heightPixels = size.height;
                }
            }

//            float density = getResources().getDisplayMetrics().density;
//            widthPixels = (int) (widthPixels - (density * 100));
            parameters.setPreviewSize(widthPixels, heightPixels);//设置Preview  预览  支持的分辨率大小的设置--这个需要设置在其能支持的分辨率内部范围内
//            parameters.setPictureSize(widthPixels, heightPixels);//该大小是图片的分辨率，最大分辨率就是可支持预览的·大小

            int cameraAngle = getCameraAngle();
            camera.setDisplayOrientation(cameraAngle);

            camera.setParameters(parameters);//设置在旋转角度的下方

            float density = getResources().getDisplayMetrics().density;

            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) surfaceView.getLayoutParams();
            layoutParams.width = heightPixels;
            layoutParams.height = (int) (widthPixels - (density * 100));
            surfaceView.setLayoutParams(layoutParams);
            //设置camera的数据界面的回调
            camera.setPreviewCallback(new Camera.PreviewCallback() {
                @Override
                public void onPreviewFrame(byte[] data, Camera camera) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startsPreview() {
        camera = Camera.open();
        try {
            camera.setPreviewTexture(surfaceView.getSurfaceTexture());
            setLayout();
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            initView();
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {

        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                    0x11);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0x11) {
            if (grantResults != null && grantResults.length > 0) {
                int grantResult = grantResults[0];
                if (grantResult == PackageManager.PERMISSION_GRANTED) {
                    initView();
                } else {
                }
            } else {
            }
        }
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        startsPreview();
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }


    /**
     * 获取照相机旋转角度
     */
    public int getCameraAngle() {
        int rotateAngle = 90;
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(0, info);
        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            rotateAngle = (info.orientation + degrees) % 360;
            rotateAngle = (360 - rotateAngle) % 360; // compensate the mirror
        } else { // back-facing
            rotateAngle = (info.orientation - degrees + 360) % 360;
        }
        return rotateAngle;
    }
}
