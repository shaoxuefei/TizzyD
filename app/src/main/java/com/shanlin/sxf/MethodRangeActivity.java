package com.shanlin.sxf;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.shanlin.sxf.bean.MethodBean;
import com.shanlin.sxf.presenter.MethodRangePresenter;
import com.shanlin.sxf.utils.PicUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MethodRangeActivity extends AppCompatActivity implements MethodRangePresenter.OnMethodTestCallBack {

    @BindView(R.id.tv_btn)
    Button tvBtn;
    @BindView(R.id.tv_add)
    Button tvAdd;
    @BindView(R.id.btn_log)
    Button btn_log;
    @BindView(R.id.btn_compress)
    Button btnCompress;
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.tv_des)
    TextView tvDes;
    @BindView(R.id.image_right)
    ImageView image_right;
    @BindView(R.id.image_bottom)
    ImageView imageBottom;
    @BindView(R.id.image_right_bottom)
    ImageView imageRightBottom;
    @BindView(R.id.image_divider_bottom)
    ImageView image_divider_bottom;

    MethodRangePresenter rangePresenter;
    List<MethodBean> resourceList;
    LinkedList<MethodBean> linkedList;
    PicUtils picUtils;

    int i = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_method_range);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        linkedList=new LinkedList<>();
        rangePresenter = new MethodRangePresenter(this, this);
        resourceList = new ArrayList<>();
        String url = "http://ww1.sinaimg.cn/large/c82e4122gy1fux9m28xkmj20ev083tgp.jpg";
        String url1 = "http://ww1.sinaimg.cn/large/c82e4122gy1fuxh3gpu3mj20jh0b0k0w.jpg";
        final RequestOptions options = new RequestOptions();
        options.placeholder(R.mipmap.ic_launcher)
                //圆角图片
//                .circleCrop()
                .error(R.mipmap.ic_launcher);

        Glide.with(this).load(url1).apply(options).into(image);
        picUtils = new PicUtils(this);
        Glide.with(this).asBitmap().load(url1).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                Bitmap bitmap = picUtils.compressQualityPicture(resource);
                image_right.setImageBitmap(bitmap);

                int width = bitmap.getWidth();
                int height = bitmap.getHeight();
                float maxWidth = 480f, maxHeight = 720f;
                float dex = 0;
                if (width > height && width > maxWidth) {
                    dex = width / maxWidth;
                }
                if (height > width && height > maxHeight) {
                    dex = height / maxHeight;
                }
                if (dex <= 0) {
                    dex = 1;
                }
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
                BitmapFactory.Options options1 = new BitmapFactory.Options();
                float v = dex + 0.5f;//之所以加0.5是为了让出现的1.24不到1.5的进行进制进取、实施压缩即使压缩的尺寸比例大一点，但没关系
                options1.inSampleSize = Math.round(v);
                Bitmap bitmap1 = BitmapFactory.decodeStream(inputStream, null, options1);
                imageRightBottom.setImageBitmap(bitmap1);
                imageBottom.setImageBitmap(resource);

                //图片--裁剪--比如要把图片裁剪成480*720的宽高
                int resourceWidth = resource.getWidth();
                int resourceHeight = resource.getHeight();
                if (resourceWidth > 480 && resourceHeight > 720) {
                    /**
                     * Bitmap source, int x, int y, int width, int height
                     * x,y是要裁剪的起始x,y值 width和height是要裁剪的宽高
                     * 切记裁剪的宽高不能大于原Bitmap的宽高
                     * Matrix 是裁剪后的图片再进行缩放的参数
                     * 其实内部调用:createBitmap(@NonNull Bitmap source, int x, int y, int width, int height,@Nullable Matrix m, boolean filter)
                     */
                    Bitmap bitmap2 = Bitmap.createBitmap(resource, 0, 0, 480, 720);
                    image_divider_bottom.setImageBitmap(bitmap2);
                } else {
                    //要么进行宽高比例等比缩放后进行、裁剪、切记裁剪的宽高不能大于原Bitmap的宽高
                    //但是一般裁剪的话一般不对图片进行比例扩大或者是缩小
                }


                String dexStr = "第二三排的是尺寸压缩,最后一张是裁剪成480*720：图片大小尺寸压缩宽>高按照宽为参照物（宽和高一起相同比例压缩），反之高>宽按照高度为参作物压缩,,,这样就是按照等比压缩处理";

                tvDes.setText("第一排的两张是质量压缩，16K压缩到6K左右\n" + dexStr + "\n" + "压缩之前的宽高:宽：" + resource.getWidth() + "高：" + resource.getHeight() + "\n压缩后的宽：" + bitmap1.getWidth() + "高：" + bitmap1.getHeight()
                        + "\nImageView.setImageBitmap()会将图片铺满该ImageView" + "\n尺寸压缩前的大小：" + resource.getByteCount() + "\n尺寸压缩后的大小" + bitmap1.getByteCount());

            }
        });
    }


    @Override
    public void onCallBack(List<MethodBean> dataList) {
        int size = dataList.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                MethodBean methodBean = dataList.get(i);
                String textNum = methodBean.textNum;
                Log.e("aa", "size==" + size + "---textNum:" + textNum);
            }
        }
    }

    @OnClick({R.id.tv_btn, R.id.tv_add, R.id.btn_log, R.id.btn_compress})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_btn:
                rangePresenter.requestMethodData(resourceList);
                break;
            case R.id.tv_add:
                MethodBean methodBean = new MethodBean();
                i++;
                methodBean.textNum = i + "";
                resourceList.add(methodBean);
                break;
            case R.id.btn_log:
                changeTextNumber();
                break;
            case R.id.btn_compress:

                break;
        }
    }
    int textNumber = 10;
    MethodBean methodBean=new MethodBean();
    private void changeTextNumber() {
        methodBean.textNum="10";
        changeMethodBean(methodBean);
        changeTextValue(textNumber);
//        Toast.makeText(this, textNumber + "", Toast.LENGTH_SHORT).show();
        Toast.makeText(this, methodBean.textNum + "", Toast.LENGTH_SHORT).show();
    }
    //输出不会受影响输出还是10
    private void changeTextValue(int textNumber) {
        textNumber = 20;
    }
    //对象改变还是之前的对象输出20
    private void changeMethodBean(MethodBean methodBean){
        methodBean.textNum="20";
    }
}
