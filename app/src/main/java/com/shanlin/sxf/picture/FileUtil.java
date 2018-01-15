package com.shanlin.sxf.picture;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Author: Troy
 * Date: 2017/8/30
 * Email: 810196673@qq.com
 * Des: FileUtil
 */

public class FileUtil {
    private static final String TAG = FileUtil.class.getSimpleName();
    private static final File parentPath = Environment.getExternalStorageDirectory();
    private static String storagePath = "";
    private static final String FOLDER_NAME = "CameraView";

    /**
     * 保存Bitmap到SD卡
     *
     * @param b
     * @param savePath
     */
    public static void saveBitmap(Bitmap b, String savePath) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(savePath);
            BufferedOutputStream bos = new BufferedOutputStream(fileOutputStream);
            b.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
            Log.i(TAG, "saveBitmap成功");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            Log.i(TAG, "saveBitmap:失败");
            e.printStackTrace();
        }
    }

    /**
     * 根据文件Uri获取文件路径
     *
     * @param context
     * @param uri
     * @return the file path or null
     */
    public static String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String path = null;
        if (scheme == null)
            path = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            path = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        path = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return path;
    }

    //对图片的质量进行压缩
    public static Bitmap compressionSizePicture(Uri uri, Context context) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
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
            InputStream inputStream1 = context.getContentResolver().openInputStream(uri);
            Bitmap bitmap1 = BitmapFactory.decodeStream(inputStream1, null, options1);
            return compressQualityPicture(bitmap1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //对图片的质量进行压缩
    public static Bitmap compressionSizePicture(Bitmap bitmap, Context context) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

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

            byte[] bytes = outputStream.toByteArray();
            Bitmap bitmap1 = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options1);
            outputStream.close();
            return compressQualityPicture(bitmap1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //进行图片的大小(存储大小)进行压缩
    public static Bitmap compressQualityPicture(Bitmap bitmap) {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);//100表示不压缩,质量最好
        Log.e("aa", "压缩前图片的大小" + outputStream.size());
        int options = 100;
        while (outputStream.toByteArray().length / 1024 > 100 && options > 10) {
            outputStream.reset();
            options -= 10;
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, outputStream);
        }
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Log.e("aa", "压缩后图片的大小" + outputStream.size());
        Bitmap bitmap1 = BitmapFactory.decodeStream(inputStream, null, null);
        return bitmap1;
    }

    //旋转图片
    public static Bitmap roateBitmapByDgree(Bitmap bitmap, int degress) {
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
    //获取图片的旋转角度

    public static int getBitmapDegress(String filePath) {
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
}
