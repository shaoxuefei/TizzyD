package com.shanlin.sxf.picture;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.shanlin.sxf.R;
import com.zfdang.multiple_images_selector.ImagesSelectorActivity;
import com.zfdang.multiple_images_selector.SelectorSettings;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Sxf on 2017/8/28.
 */

public class PictureActivity extends AppCompatActivity {
    private Button pictureAll, muchSelectPicture, tackPicture;
    private ImageView picImage;
    private int PICTURE_REQUEST = 0x21;
    private int MUCH_PICTURE_CODE = 0x31;
    private int TAKE_CAMERA_REQUESTCODE = 0x41;
    private ArrayList<String> mResults = new ArrayList<>();
    private TextView resultTxt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.picture_activity);
        initView();

        String string = getResources().getString(R.string.type_txt);
        resultTxt.setText(String.format(string, "苹果本", 124.23, 20));
    }

    public void initView() {
        tackPicture = (Button) findViewById(R.id.tackPicture);
        pictureAll = (Button) findViewById(R.id.pictureAllButton);
        picImage = (ImageView) findViewById(R.id.picImage);
        muchSelectPicture = (Button) findViewById(R.id.muchSelectPicture);
        resultTxt = (TextView) findViewById(R.id.resultTxt);
        pictureAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                turnToPicture();
            }
        });
        muchSelectPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // start multiple photos selector
                Intent intent = new Intent(PictureActivity.this, ImagesSelectorActivity.class);
                // max number of images to be selected
                intent.putExtra(SelectorSettings.SELECTOR_MAX_IMAGE_NUMBER, 5);
                // min size of image which will be shown; to filter tiny images (mainly icons)
                intent.putExtra(SelectorSettings.SELECTOR_MIN_IMAGE_SIZE, 100000);
                // show camera or not
                intent.putExtra(SelectorSettings.SELECTOR_SHOW_CAMERA, true);
                // pass current selected images as the initial value
                intent.putStringArrayListExtra(SelectorSettings.SELECTOR_INITIAL_SELECTED_LIST, mResults);
                // start the selector
                startActivityForResult(intent, MUCH_PICTURE_CODE);
            }
        });

        tackPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermission(Manifest.permission.CAMERA)) {
                    tackCamera();
                }
            }
        });
    }

    //拍照
    public void tackCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, TAKE_CAMERA_REQUESTCODE);
    }

    //调用系统相册
    public void turnToPicture() {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            //这种在大于19sdk中直接是打开的全部文件不只是图库
            intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
        } else {
            //Uri:MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        startActivityForResult(intent, PICTURE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICTURE_REQUEST && resultCode == RESULT_OK) {
            Uri fromFile = data.getData();
            Log.e("aa", "uri:" + fromFile);//content://media/external/images/media/59032
            File fileFromMediaUri = getFileFromMediaUri(fromFile);
            Log.e("aa", "filePath:" + fileFromMediaUri);///storage/emulated/0/sina/weibo/weibo/img-bafb49589507d91d66c813501806e9c2.jpg
            try {
                //两张方式、可以直接Url通过内容提供者获取InputStream\\或者通过内容提供者讲Url提供给外部调用生产正式文件地址路径File,然后File自己转成InputStream的方式
//                FileInputStream fileInputStream=new FileInputStream(fileFromMediaUri);
//                Bitmap bitmap1 = BitmapFactory.decodeStream(fileInputStream);

                ContentResolver contentResolver = getContentResolver();
                InputStream inputStream = contentResolver.openInputStream(fromFile);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                Log.e("aa", "压缩前byte长度" + byteArrayOutputStream.toByteArray().length);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Bitmap bitmap = compressionSizePicture(fromFile);
            if (bitmap != null) {
                Bitmap bitmap1 = roateBitmapByDgree(bitmap, getBitmapDegress(fileFromMediaUri.getAbsolutePath()));
                picImage.setImageBitmap(bitmap1);
            }
        } else if (requestCode == MUCH_PICTURE_CODE && resultCode == RESULT_OK) {
            mResults = data.getStringArrayListExtra(SelectorSettings.SELECTOR_RESULTS);

            StringBuilder sb = new StringBuilder();
            sb.append(String.format("Totally %d images selected:", mResults.size())).append("\n");
            for (String result : mResults) {
                sb.append(result).append("\n");
            }
            resultTxt.setText(sb.toString());
        } else if (requestCode == TAKE_CAMERA_REQUESTCODE) {
            if (data != null) {
                Bundle extras = data.getExtras();
                if (extras != null && extras.get("data") != null) {
                    Bitmap cameraBitmap = (Bitmap) extras.get("data");
                    picImage.setImageBitmap(cameraBitmap);
                }
            }
        }
    }

    //根据Uri查找File文件是否存在
    public File getFileFromMediaUri(Uri uri) {
        if (uri.getScheme().toString().compareTo("content") == 0) {
            Log.e("aa","uri:"+uri);
            ContentResolver contentResolver = getContentResolver();
            Cursor cursor = contentResolver.query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int columnCount = cursor.getColumnCount();
                if (columnCount > 0) {
                    String filePath = null;
                    for (int i = 0; i < columnCount; i++) {
                        //获取的是提供的内容提供者的URL---》然后通过内容提供者解析Url，便利获取该文件的基本信息属性，获取到自己需要的属性_data
                        String columnName = cursor.getColumnName(i);
                        //2020-04-24 16:35:32.597 12157-12157/com.shanlin.sxf E/aa: columnName:instance_id
                        //2020-04-24 16:35:32.597 12157-12157/com.shanlin.sxf E/aa: columnName:duration
                        //2020-04-24 16:35:32.597 12157-12157/com.shanlin.sxf E/aa: columnName:description
                        //2020-04-24 16:35:32.597 12157-12157/com.shanlin.sxf E/aa: columnName:picasa_id
                        //2020-04-24 16:35:32.597 12157-12157/com.shanlin.sxf E/aa: columnName:latitude
                        //2020-04-24 16:35:32.597 12157-12157/com.shanlin.sxf E/aa: columnName:orientation
                        //2020-04-24 16:35:32.597 12157-12157/com.shanlin.sxf E/aa: columnName:height
                        //2020-04-24 16:35:32.597 12157-12157/com.shanlin.sxf E/aa: columnName:is_drm
                        //2020-04-24 16:35:32.597 12157-12157/com.shanlin.sxf E/aa: columnName:bucket_display_name
                        //2020-04-24 16:35:32.597 12157-12157/com.shanlin.sxf E/aa: columnName:owner_package_name
                        //2020-04-24 16:35:32.597 12157-12157/com.shanlin.sxf E/aa: columnName:volume_name
                        //2020-04-24 16:35:32.597 12157-12157/com.shanlin.sxf E/aa: columnName:date_modified
                        //2020-04-24 16:35:32.597 12157-12157/com.shanlin.sxf E/aa: columnName:date_expires
                        //2020-04-24 16:35:32.597 12157-12157/com.shanlin.sxf E/aa: columnName:_display_name
                        //2020-04-24 16:35:32.597 12157-12157/com.shanlin.sxf E/aa: columnName:datetaken
                        //2020-04-24 16:35:32.597 12157-12157/com.shanlin.sxf E/aa: columnName:mime_type
                        //2020-04-24 16:35:32.597 12157-12157/com.shanlin.sxf E/aa: columnName:_id
                        //2020-04-24 16:35:32.597 12157-12157/com.shanlin.sxf E/aa: columnName:_data
                        //2020-04-24 16:35:32.597 12157-12157/com.shanlin.sxf E/aa: columnName:_hash
                        //2020-04-24 16:35:32.597 12157-12157/com.shanlin.sxf E/aa: columnName:_size
                        //2020-04-24 16:35:32.597 12157-12157/com.shanlin.sxf E/aa: columnName:title
                        //2020-04-24 16:35:32.597 12157-12157/com.shanlin.sxf E/aa: columnName:width
                        //2020-04-24 16:35:32.597 12157-12157/com.shanlin.sxf E/aa: columnName:longitude
                        //2020-04-24 16:35:32.597 12157-12157/com.shanlin.sxf E/aa: columnName:is_trashed
                        //2020-04-24 16:35:32.597 12157-12157/com.shanlin.sxf E/aa: columnName:group_id
                        //2020-04-24 16:35:32.597 12157-12157/com.shanlin.sxf E/aa: columnName:document_id
                        //2020-04-24 16:35:32.597 12157-12157/com.shanlin.sxf E/aa: columnName:is_pending
                        //2020-04-24 16:35:32.597 12157-12157/com.shanlin.sxf E/aa: columnName:date_added
                        //2020-04-24 16:35:32.597 12157-12157/com.shanlin.sxf E/aa: columnName:mini_thumb_magic
                        //2020-04-24 16:35:32.597 12157-12157/com.shanlin.sxf E/aa: columnName:primary_directory
                        //2020-04-24 16:35:32.597 12157-12157/com.shanlin.sxf E/aa: columnName:secondary_directory
                        //2020-04-24 16:35:32.597 12157-12157/com.shanlin.sxf E/aa: columnName:isprivate
                        //2020-04-24 16:35:32.597 12157-12157/com.shanlin.sxf E/aa: columnName:original_document_id
                        //2020-04-24 16:35:32.597 12157-12157/com.shanlin.sxf E/aa: columnName:bucket_id
                        //2020-04-24 16:35:32.597 12157-12157/com.shanlin.sxf E/aa: columnName:relative_path
                        Log.e("aa","columnName:"+columnName);
                        switch (columnName) {
                            case "_data":
                                filePath = cursor.getString(cursor.getColumnIndex("_data"));
                                break;
                        }
                    }
                    //内容提供者就是根据提供的内部隐士Url来获取到对应的真正的系统可以提供给外部的文件的链接文件地址文件files
                    Log.e("aa","filePath："+filePath);
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


    //对图片的大小进行压缩、、、decode 解码
    public Bitmap compressionSizePicture(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            BitmapFactory.Options options = new BitmapFactory.Options();
            //进入测试调节模式--只是为了获取图片的大小信息--因此减少了多一次的绘制
            /**
             * If set to true, the decoder will return null (no bitmap), but
             * the <code>out...</code> fields will still be set, allowing the caller to
             * query the bitmap without having to allocate the memory for its pixels.
             */
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
//            //TODO 另一种情况、将图片缩放成给定的比例
//            if (outHeight > outWidth) {
//                //宽不变、变高
//                float v = outWidth / (width / (width + height));
//                float needHeight = v * (height / (width + height));
//                //高度的缩放比例
//                float heightDex = needHeight / height;
//            } else {
//                //高不变、变宽
//                float v = outHeight / (height / (width + height));
//                float needWidth = v * (height / (width + height));
//                //宽度的缩放比例
//                float widthDex = needWidth / width;
//            }
//           //这种等比缩放、对Bitmap的大小没影响、只是改变了宽高的尺寸，整体图片的比例还是原来的比例、、而且图片也不存在裁剪
//            //TODO 另一种情况、将图片缩放到给定的尺寸


            if (compareSize <= 0) {
                compareSize = 1;
            }
            //等比缩放--就是保持原图片比例不变--但是上边的情况会有个问题、(int)类型基本保持不了有效值
            options.inJustDecodeBounds = false;
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
        Log.e("aa", "亚索后的byte长度" + outputStream.toByteArray().length);
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


    private boolean checkPermission(String permission) {
        if (!(ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, new String[]{permission}, 10);
            return false;
        } else {
            return true;
        }
    }
}
