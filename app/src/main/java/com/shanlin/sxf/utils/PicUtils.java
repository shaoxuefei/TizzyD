package com.shanlin.sxf.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author : SXF
 * @ date   : 2018/9/4
 * Description :
 */

public class PicUtils {

    private Context mContext;

    public PicUtils(Context mContext) {
        this.mContext = mContext;
    }

    //对图片的大小进行压缩、、、decode 解码
    public Bitmap compressionSizePicture(Uri uri) {
        try {
            InputStream inputStream = mContext.getContentResolver().openInputStream(uri);
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
            //当宽大于高，宽大于480f\\宽按照480比例;;;;;当高大于宽，，高大于800f、、、高按照800f进行缩放
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
            Log.e("aa", "----compareSize---" + compareSize);

            //等比缩放--就是保持原图片比例不变--但是上边的情况会有个问题、(int)类型基本保持不了有效值
            options.inJustDecodeBounds = false;
            BitmapFactory.Options options1 = new BitmapFactory.Options();
            options1.inSampleSize = Math.round(compareSize + 0.5f);
            options1.inPreferredConfig = Bitmap.Config.ARGB_8888;
            InputStream inputStream1 = mContext.getContentResolver().openInputStream(uri);
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
        int length = outputStream.toByteArray().length;
        Log.e("aa", "压缩前的byte长度" + length);
        int options = 100;
        while (outputStream.toByteArray().length * 1f / 1024 >= 10) {
            outputStream.reset();
            options -= 10;
            if (options <= 0) {
                options = 1;
                bitmap.compress(Bitmap.CompressFormat.JPEG, options, outputStream);
                break;
            }
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, outputStream);
        }
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Bitmap bitmap1 = BitmapFactory.decodeStream(inputStream, null, null);


        Log.e("aa", "压缩后的byte长度" + outputStream.toByteArray().length);
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
