package com.shanlin.sxf.softkeybord;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shanlin.sxf.R;
import com.shanlin.sxf.utils.DensityUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author : SXF
 * @ date   : 2020/2/12 0012
 * Description :
 */
public class NewSoftInputKeyBord extends LinearLayout {
    @BindView(R.id.editText)
    EditText editText;
    @BindView(R.id.tv_emoji)
    TextView tvEmoji;
    @BindView(R.id.tv_send)
    TextView tvSend;
    @BindView(R.id.relative_input)
    RelativeLayout relativeInput;
    @BindView(R.id.recycleView_emoji)
    RecyclerView recycleViewEmoji;
    @BindView(R.id.linear_translate)
    LinearLayout linear_translate;

    EmojiAdapter emojiAdapter;
    View inflate;
    private String emojiStr = "emoji";
    private String textStr = "text";
    AppCompatActivity appCompatActivity;
    public NewSoftInputKeyBord(Context context,AttributeSet attrs) {
        super(context, attrs);

        inflate = LayoutInflater.from(context).inflate(R.layout.layout_new_soft, this, true);
        ButterKnife.bind(this, inflate);
        initView();
    }

    public void setBaseActivity(AppCompatActivity appCompatActivity) {
        this.appCompatActivity = appCompatActivity;
    }

    private void initView() {
        ArrayList emojiList = new ArrayList<>();
        for (int i = 0; i < 56; i++) {
            emojiList.add(String.valueOf(i));
        }
        emojiAdapter = new EmojiAdapter(getContext(), emojiList);
        recycleViewEmoji.setLayoutManager(new GridLayoutManager(getContext(), 8));
        recycleViewEmoji.setAdapter(emojiAdapter);
        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP && editText.isFocusable()) {
                    if (recycleViewEmoji.isShown()) {
                        //固定View的高度
                        lockViewHeight();
                        //隐藏表情
                        hideEmojiView();
                        //显示键盘
                        showSoftKeyBord();
                        //释放高度
                        unLockViewHeight();
                    } else {
                        showSoftKeyBord();
                    }
                    tvEmoji.setText("emoji");
                }
                return false;
            }
        });
        relativeInput.addOnLayoutChangeListener(new OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (oldBottom == 0) {
                    bottomY = bottom;
                }
                if (oldBottom > bottom) {
                    //表示软键盘抬出来了
                    int dexBottom = oldBottom - bottom;
                } else if (oldBottom < bottom) {
                    int dexBottom = oldBottom - bottom;
                }
                if (oldBottom != 0 && bottom - oldBottom >=screenHeight/4) {
                    dismissSoftView();
                    hideEmojiView();
                    tvEmoji.setText(emojiStr);
                }
                Log.e("aa", "bottomY: " + bottom + "oldBottom Y " + oldBottom);
            }
        });
    }

    public void dismissSoftView() {
        hideSoftKeyBord();
    }

    @OnClick({R.id.tv_emoji, R.id.tv_send,R.id.linear_translate})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_emoji:
                String tvStr = tvEmoji.getText().toString();
                if (emojiStr.equals(tvStr)) {
                    if (softInputIsShow()) {
                        //固定高度
                        lockViewHeight();
                        //隐藏键盘
                        hideSoftKeyBord();
                        showEmojiView();
                        //还原高度
                        unLockViewHeight();
                    } else {
                        showEmojiView();
                    }
                    //延迟获取软键盘的高度
                    editText.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            appCompatActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                        }
                    }, 200);
                    tvEmoji.setText(textStr);
                } else if (textStr.equals(tvStr)) {
                    //TODO
                    appCompatActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
                    //固定View的高度
                    lockViewHeight();
                    //隐藏表情
                    //显示键盘
                    showSoftKeyBord();
                    unLockViewHeight();
                    tvEmoji.setText(emojiStr);
                    //延迟获取软键盘的高度
                    editText.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            hideEmojiView();
                            appCompatActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                        }
                    }, 200);
                }
                break;
            case R.id.tv_send:
                String editStr = editText.getText().toString();
                if (!TextUtils.isEmpty(editStr)) {
                    Toast.makeText(getContext(), editStr, Toast.LENGTH_SHORT).show();
                }
                editText.setText("");
                break;
            case R.id.linear_translate:
//                if(softInputIsShow()||recycleViewEmoji.getVisibility()==VISIBLE){
//                    hideSoftKeyBord();
//                    hideEmojiView();
//                    tvEmoji.setText(textStr);
//                }else {
//                    setVisibility(GONE);
//                }
                hideSoftKeyBord();
                hideEmojiView();

                editText.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //TODO 需要延迟设置windowSoftInputMode||setVisibility(GONE);
                        setVisibility(GONE);
                        appCompatActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                    }
                },50);
                break;
        }
    }


    public void showSoftKeyBord() {
//        appCompatActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setVisibility(VISIBLE);
        tvEmoji.setText(emojiStr);
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);//需要加上这个才会起作用--自动弹出
        editText.requestFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.showSoftInput(editText, 0);
        }
        //延迟获取软键盘的高度
        editText.postDelayed(new Runnable() {
            @Override
            public void run() {
                getSupportSoftInputHeight();
            }
        }, 200);
    }

    private void hideSoftKeyBord() {
        InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        }
    }

    private void lockViewHeight() {
        int dexHeight = 0;
        if (recycleViewEmoji.getLayoutParams().height > 0 && recycleViewEmoji.getLayoutParams().height != getKeyBoardHeight() && softInputIsShow()) {
            dexHeight = recycleViewEmoji.getLayoutParams().height - getKeyBoardHeight();
        }
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) linear_translate.getLayoutParams();
        layoutParams.height = linear_translate.getHeight() + dexHeight;
        layoutParams.weight = 0f;
    }

    private boolean softInputIsShow() {
        Log.e("aa", "softHeight:  " + getSupportSoftInputHeight());
        return getSupportSoftInputHeight() != 0;
    }

    private void hideEmojiView() {
        recycleViewEmoji.setVisibility(GONE);
    }

    private void unLockViewHeight() {
        editText.postDelayed(new Runnable() {
            @Override
            public void run() {
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) linear_translate.getLayoutParams();
                layoutParams.weight = 1;
            }
        }, 200);
    }

    private void showEmojiView() {
        LinearLayout.LayoutParams layoutParams = (LayoutParams) recycleViewEmoji.getLayoutParams();
        layoutParams.height = getKeyBoardHeight();
        recycleViewEmoji.setVisibility(VISIBLE);
    }

    /**
     * 获取软键盘的高度
     *
     * @return
     */
    private float bottomY;

    class EmojiAdapter extends RecyclerView.Adapter {

        private Context mContext;
        private List<String> imageIconList;

        public EmojiAdapter(Context mContext, List<String> imageIconList) {
            this.mContext = mContext;
            this.imageIconList = imageIconList;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new EmojiAdapter.EmojiViewHolder(LayoutInflater.from(mContext).inflate(R.layout.emoji_item_adapter, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return imageIconList.size();
        }

        class EmojiViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.image_icon)
            ImageView image_icon;

            public EmojiViewHolder(View itemView) {
                super(itemView);
            }
        }
    }


    /**
     * 获取软件盘的高度
     *
     * @return
     */
    int screenHeight;
    private int getSupportSoftInputHeight() {
        Rect r = new Rect();
        /**
         * decorView是window中的最顶层view，可以从window中通过getDecorView获取到decorView。
         * 通过decorView获取到程序显示的区域，包括标题栏，但不包括状态栏。
         */
        appCompatActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
        //获取屏幕的高度
        screenHeight = appCompatActivity.getWindow().getDecorView().getRootView().getHeight();
        //计算软件盘的高度
        int softInputHeight = screenHeight - r.bottom;

        /**
         * 某些Android版本下，没有显示软键盘时减出来的高度总是144，而不是零，
         * 这是因为高度是包括了虚拟按键栏的(例如华为系列)，所以在API Level高于20时，
         * 我们需要减去底部虚拟按键栏的高度（如果有的话）
         */
        if (Build.VERSION.SDK_INT >= 20) {
            // When SDK Level >= 20 (Android L), the softInputHeight will contain the height of softButtonsBar (if has)
            softInputHeight = softInputHeight - getSoftButtonsBarHeight();
            Log.e("aa", "虚拟键Height:  " + getSoftButtonsBarHeight());
        }

        if (softInputHeight < 0) {
            Log.e("aa", "EmotionKeyboard--Warning: value of softInputHeight is below zero!");
        }
        //存一份到本地
        if (softInputHeight > 0 && softInputHeight > screenHeight / 4) {
            InputWxHeight.softKeyBordHeight = softInputHeight;
        }else {
            softInputHeight=0;
        }
        return softInputHeight;
    }

    /**
     * 底部虚拟按键栏的高度
     *
     * @return
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private int getSoftButtonsBarHeight() {
        DisplayMetrics metrics = new DisplayMetrics();
        //这个方法获取可能不是真实屏幕的高度
        appCompatActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int usableHeight = metrics.heightPixels;
        //获取当前屏幕的真实高度
        appCompatActivity.getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        int realHeight = metrics.heightPixels;
        if (realHeight > usableHeight) {
            return realHeight - usableHeight;
        } else {
            return 0;
        }
    }

    /**
     * 获取软键盘高度，由于第一次直接弹出表情时会出现小问题，787是一个均值，作为临时解决方案
     *
     * @return
     */
    public int getKeyBoardHeight() {
        return InputWxHeight.softKeyBordHeight;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if(event.getKeyCode()==KeyEvent.KEYCODE_BACK&&event.getRepeatCount()==0&&getVisibility()==VISIBLE){
            linear_translate.callOnClick();
            return false;
        }
        Toast.makeText(appCompatActivity,"dispatchKeyEvent",Toast.LENGTH_SHORT).show();
        return super.dispatchKeyEvent(event);
    }


}
