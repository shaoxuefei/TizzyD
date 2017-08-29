package com.shanlin.sxf;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Sxf on 2017/8/28.
 */

public class PictureActivity extends AppCompatActivity {
    private Button pictureAll;
    private ImageView picImage;
    private int PICTURE_REQUEST = 0x21;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.picture_activity);
        initView();
    }

    public void initView() {
        pictureAll = (Button) findViewById(R.id.pictureAllButton);
        picImage = (ImageView) findViewById(R.id.picImage);
        pictureAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                turnToPicture();
            }
        });
    }

    public void turnToPicture() {
        //调用系统相册
        Intent intent;
        if(Build.VERSION.SDK_INT<19) {
            //这种在大于19sdk中直接是打开的全部文件不只是图库
            intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
        }else {
            //Uri:MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        startActivityForResult(intent,PICTURE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICTURE_REQUEST && resultCode == RESULT_OK) {
            Uri fromFile = data.getData();
            Log.e("aa", "uri:" + fromFile);//content://media/external/images/media/59032
            File fileFromMediaUri = getFileFromMediaUri(fromFile);
            Log.e("aa", "filePath:" + fileFromMediaUri);///storage/emulated/0/sina/weibo/weibo/img-bafb49589507d91d66c813501806e9c2.jpg
            ContentResolver contentResolver = getContentResolver();
            try {
                InputStream inputStream = contentResolver.openInputStream(fromFile);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
                Log.e("aa","压缩前byte长度"+byteArrayOutputStream.toByteArray().length);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Bitmap bitmap = compressionSizePicture(fromFile);
            if (bitmap != null) {
                Bitmap bitmap1 = roateBitmapByDgree(bitmap, getBitmapDegress(fileFromMediaUri.getAbsolutePath()));
                picImage.setImageBitmap(bitmap1);
            }
        }
    }

    //根据Uri查找File文件是否存在
    public File getFileFromMediaUri(Uri uri) {
        if (uri.getScheme().toString().compareTo("content") == 0) {
            ContentResolver contentResolver = getContentResolver();
            Cursor cursor = contentResolver.query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int columnCount = cursor.getColumnCount();
                if (columnCount > 0) {
                    String filePath = null;
                    for (int i = 0; i < columnCount; i++) {
                        String columnName = cursor.getColumnName(i);
                        switch (columnName) {
                            case "_data":
                                filePath = cursor.getString(cursor.getColumnIndex("_data"));
                                break;
                        }
                    }
                    cursor.close();
                    if (filePath != null) {
                        return new File(filePath);
                    } else {
                        return new File(uri.toString().replace("file://", ""));
                    }
                }

            } else if (uri.getScheme().toString().compareTo("file") == 0) {
                return new File(uri.toString().replace("file://", ""));
            }
        }
        return null;
    }


    //对图片的大小进行压缩
    public Bitmap compressionSizePicture(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            BitmapFactory.Options options = new BitmapFactory.Options();
            //进入测试调节模式--只是为了获取图片的大小信息--因此减少了多一次的绘制
            options.inJustDecodeBounds = true;
            options.inDither = true;
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            BitmapFactory.decodeStream(inputStream, null, options);
            inputStream.close();
            int outWidth = options.outWidth;
            int outHeight = options.outHeight;
            if ((outWidth == 1) || (outHeight == 1)) {
                return null;
            }
            //按照480分辨率来压缩
            float height = 800f;
            float width = 480f;
            int compareSize = 1;
            if (outWidth > outHeight && outWidth > width) {
                compareSize = (int) (outWidth / width);
            }
            if (outHeight > outWidth && outHeight > height) {
                compareSize = (int) (outHeight / height);
            }
            if (compareSize <= 0) {
                compareSize = 1;
            }
            BitmapFactory.Options options1 = new BitmapFactory.Options();
            options1.inSampleSize = compareSize;
            options1.inPreferredConfig = Bitmap.Config.ARGB_8888;
            InputStream inputStream1 = getContentResolver().openInputStream(uri);
            Bitmap bitmap1 = BitmapFactory.decodeStream(inputStream1, null, options1);
            return compressQualityPicture(bitmap1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //进行智质量压缩
    public Bitmap compressQualityPicture(Bitmap bitmap) {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);//100表示不压缩,质量最好
        int options = 100;
        while (outputStream.toByteArray().length / 1024 > 100) {
            outputStream.reset();
            options -= 10;
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, outputStream);
        }
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Bitmap bitmap1 = BitmapFactory.decodeStream(inputStream, null, null);
        Log.e("aa","压缩后的byte长度"+outputStream.toByteArray().length);
        return bitmap1;
    }

    //获取图片的旋转角度

    public int getBitmapDegress(String filePath) {
        int degress = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(filePath);
            int attributeInt = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (attributeInt) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degress = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degress = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degress = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degress;
    }

    //旋转图片
    public Bitmap roateBitmapByDgree(Bitmap bitmap, int degress) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degress);
        Bitmap returnBm = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        if (returnBm == null) {
            returnBm = bitmap;
        }
        if (bitmap != returnBm) {
            bitmap.recycle();
        }
        return returnBm;
    }

}
