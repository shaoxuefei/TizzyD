package com.shanlin.sxf.softkeybord;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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

public class InputWxEmojActivity extends AppCompatActivity {
    @BindView(R.id.recycleView)
    RecyclerCanClickView recycleView;
    @BindView(R.id.editText)
    EditText editText;
    @BindView(R.id.tv_emoji)
    TextView tvEmoji;
    @BindView(R.id.tv_send)
    TextView tv_send;
    @BindView(R.id.relative_input)
    RelativeLayout relativeInput;
    @BindView(R.id.recycleView_emoji)
    RecyclerView recycleView_emoji;
    @BindView(R.id.softInputKey)
    KeySoftBordNormalView keySoftBordNormalView;
    @BindView(R.id.softInputKeybord)
    SoftInputKeyBordView softInputKeyBordView;


    List<String> txtList, emojiList;
    String[] strings = new String[]{"你好，新年快乐", "你好，万事大吉", "你好，身体健康", "你好，2020请多多指教", "你好，一夜暴富", "你好，祖国强盛，国泰民安", "你好，20200202"};
    InputEmojiAdapter inputEmojiAdapter;
    EmojiAdapter emojiAdapter;
    int emojiHeight;
    int isDefault;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_wx_emoj);
        ButterKnife.bind(this);
        initView();
    }


    private void initView() {
        isDefault = getIntent().getIntExtra("isDefault", 1);

        emojiHeight = DensityUtil.dp2px(this, 250);
        txtList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            int index = (int) (Math.random() * (strings.length - 1));
            txtList.add(strings[index]);
        }
        inputEmojiAdapter = new InputEmojiAdapter(this, txtList);
        recycleView.setLayoutManager(new LinearLayoutManager(this));
        recycleView.setAdapter(inputEmojiAdapter);
        recycleView.smoothScrollToPosition(txtList.size() - 1);
        emojiList = new ArrayList<>();
        for (int i = 0; i < 56; i++) {
            emojiList.add(String.valueOf(i));
        }
        emojiAdapter = new EmojiAdapter(this, emojiList);
        recycleView_emoji.setLayoutManager(new GridLayoutManager(this, 8));
        recycleView_emoji.setAdapter(emojiAdapter);

        if (1 == isDefault) {
            recycleView_emoji.setVisibility(View.VISIBLE);
            recycleView_emoji.getLayoutParams().height = 0;
        }
        recycleView.setOnRecyclerViewClickListener(new RecyclerCanClickView.OnRecyclerViewClickListener() {
            @Override
            public void onViewClick() {
                if (isDefault == 1) {
                    hideSoftInputKeyBord();
                    if (recycleView_emoji.getLayoutParams().height > 0) {
                        animationOpenEmoj(false);
                        tvEmoji.setText("emoji");
                    }
                } else if (isDefault == 2) {
                    if (recycleView_emoji.isShown()) {
                        hideEmotionLayout(false);
                    } else if (softInputIsShow()) {
                        hideSoftInputKeyBord();
                    }
                    tvEmoji.setText("emoji");
                } else if (isDefault == 3) {
                    keySoftBordNormalView.dismissSoftView();
//                    softInputKeyBordView.setVisibility(View.GONE);
                }
            }
        });

        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (recycleView_emoji.isShown()) {
                        if (isDefault == 1) {
                            showInputSoftKeyBord();
//                            animationOpenEmoj(false);
                            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) recycleView_emoji.getLayoutParams();
                            layoutParams.height = 0;
                            editText.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    recycleView.smoothScrollToPosition(txtList.size() - 1);
                                }
                            }, 200);
                        } else if (isDefault == 2) {
                            //固定内容View的高度
                            lockContentViewHeight();
                            //隐藏表情
                            hideEmotionLayout(true);
                            //延迟解禁
                            unLockContentViewHeight();
                        }
                    } else {
                        editText.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                recycleView.smoothScrollToPosition(txtList.size() - 1);
                            }
                        }, 200);
                    }
                    tvEmoji.setText("emoji");
                }
                return false;
            }
        });

        if (isDefault == 3) {
            relativeInput.setVisibility(View.GONE);
            recycleView_emoji.setVisibility(View.GONE);
            keySoftBordNormalView.setBaseActivity(this);
//            softInputKeyBordView.setVisibility(View.VISIBLE);
        } else {
            softInputKeyBordView.setVisibility(View.GONE);
        }

    }

    private boolean softInputIsShow() {
        Log.e("aa", "softHeight:  " + getSupportSoftInputHeight());
        return getSupportSoftInputHeight() != 0;
    }

    private void lockContentViewHeight() {
        int dexHeight = 0;
        if (recycleView_emoji.getLayoutParams().height > 0 && recycleView_emoji.getLayoutParams().height != getKeyBoardHeight() && softInputIsShow()) {
            dexHeight = recycleView_emoji.getLayoutParams().height - getKeyBoardHeight();
        }
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) recycleView.getLayoutParams();
        layoutParams.height = recycleView.getHeight() + dexHeight;
        layoutParams.weight = 0f;
        Log.e("aa", "lockContent:  " + layoutParams.height);
    }

    private void unLockContentViewHeight() {
        editText.postDelayed(new Runnable() {
            @Override
            public void run() {
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) recycleView.getLayoutParams();
                layoutParams.weight = 1;
                Log.e("aa", "unlockContent:  " + layoutParams.height);
            }
        }, 200);
    }

    /**
     * 隐藏表情布局
     *
     * @param showSoftInput 是否显示软件盘
     */
    private void hideEmotionLayout(boolean showSoftInput) {
        if (recycleView_emoji.isShown()) {
            recycleView_emoji.setVisibility(View.GONE);
            if (showSoftInput) {
                showInputSoftKeyBord();
                editText.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        recycleView.smoothScrollToPosition(txtList.size() - 1);
                    }
                }, 200);
            }
        }
    }

    private void showEmojiLayout() {
        editText.requestFocus();
        int supportSoftInputHeight = getSupportSoftInputHeight();
        if (supportSoftInputHeight == 0) {
            supportSoftInputHeight = getKeyBoardHeight();
        }
        hideSoftInputKeyBord();
        recycleView_emoji.getLayoutParams().height = supportSoftInputHeight;
        recycleView_emoji.setVisibility(View.VISIBLE);
    }


    @OnClick({R.id.tv_emoji, R.id.tv_send})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_emoji:
                String emojiBtn = tvEmoji.getText().toString();
                if ("emoji".equals(emojiBtn)) {
                    if (isDefault == 1) {
                        hideSoftInputKeyBord();
                        tvEmoji.setText("text");
                        tvEmoji.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                animationOpenEmoj(true);
                            }
                        }, 100);
                    } else if (isDefault == 2) {
                        //other--check
                        if (softInputIsShow()) {
                            //固定头部高度
                            lockContentViewHeight();
                            //显示表情弹窗
                            showEmojiLayout();
                            //延迟释放头部内容高度
                            unLockContentViewHeight();
                        } else {
                            showEmojiLayout();
                            editText.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    recycleView.smoothScrollToPosition(txtList.size() - 1);
                                }
                            }, 200);
                        }
                        tvEmoji.setText("text");
                        recycleView.smoothScrollToPosition(txtList.size() - 1);
                    }
                } else if ("text".equals(emojiBtn)) {
                    if (isDefault == 1) {
                        showInputSoftKeyBord();
//                        animationOpenEmoj(false);
                        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) recycleView_emoji.getLayoutParams();
                        layoutParams.height = 0;
                        editText.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                recycleView.smoothScrollToPosition(txtList.size() - 1);
                            }
                        }, 200);
                    } else if (isDefault == 2) {
                        editText.requestFocus();
                        //固定内容View的高度
                        lockContentViewHeight();
                        //隐藏表情
                        hideEmotionLayout(true);
                        //延迟解禁
                        unLockContentViewHeight();
                    }
                    tvEmoji.setText("emoji");
                }
                break;
            case R.id.tv_send:
                String s = editText.getText().toString();
                if (!TextUtils.isEmpty(s)) {
                    txtList.add(s);
                    inputEmojiAdapter.notifyItemInserted(txtList.size() - 1);
                    recycleView.smoothScrollToPosition(txtList.size() - 1);
                    editText.setText("");
                }
                tvEmoji.setText("emoji");
                break;
        }
    }

    class InputEmojiAdapter extends RecyclerView.Adapter {

        private Context mContext;
        private List<String> dataList;


        public InputEmojiAdapter(Context mContext, List<String> dataList) {
            this.mContext = mContext;
            this.dataList = dataList;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new BaseEmojiViewHolder(LayoutInflater.from(mContext).inflate(R.layout.input_emoji_item_layout, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            BaseEmojiViewHolder baseEmojiViewHolder = (BaseEmojiViewHolder) holder;
            final String s = dataList.get(position);
            baseEmojiViewHolder.tv_content.setText(s);
            baseEmojiViewHolder.tv_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isDefault == 3) {
                        keySoftBordNormalView.showSoftInputView(true, null);
//                        softInputKeyBordView.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(mContext, s, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }

        class BaseEmojiViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.tv_content)
            TextView tv_content;

            public BaseEmojiViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }

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
            return new EmojiViewHolder(LayoutInflater.from(mContext).inflate(R.layout.emoji_item_adapter, parent, false));
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

    private void showInputSoftKeyBord() {
        editText.requestFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.showSoftInput(editText, 0);
        }
    }

    private void hideSoftInputKeyBord() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        }
    }


    ValueAnimator valueAnimator;

    private void animationOpenEmoj(final boolean isOpen) {
//        TranslateAnimation translateAnimation = new TranslateAnimation
//                (Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0
//                        , RELATIVE_TO_SELF, isOpen ? 1 : 0, RELATIVE_TO_SELF, isOpen ? 0 : 1);
//        translateAnimation.setDuration(1000);
//        recycleView_emoji.setAnimation(translateAnimation);
//        translateAnimation.start();
//        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//                if (isOpen) {
//                    recycleView_emoji.setVisibility(View.VISIBLE);
//                }
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                if (!isOpen) {
//                    recycleView_emoji.setVisibility(View.GONE);
//                }
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });
        if (valueAnimator != null && valueAnimator.isRunning()) {
            valueAnimator.cancel();
            return;
        }
        valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.setDuration(300)
                .addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float animatedValue = (float) animation.getAnimatedValue();
                        float emojiCurrentHeight;
                        if (isOpen) {
                            emojiCurrentHeight = emojiHeight * animatedValue;
                        } else {
                            emojiCurrentHeight = emojiHeight * (1 - animatedValue);
                        }
                        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) recycleView_emoji.getLayoutParams();
                        layoutParams.height = (int) emojiCurrentHeight;
                        if (animatedValue == 1) {
                            layoutParams.height = isOpen ? emojiHeight : 0;
                            recycleView.smoothScrollToPosition(txtList.size() - 1);
                        }
                        recycleView_emoji.setLayoutParams(layoutParams);
                    }
                });
        valueAnimator.start();
    }

    /**
     * 获取软件盘的高度
     *
     * @return
     */
    private int getSupportSoftInputHeight() {
        Rect r = new Rect();
        /**
         * decorView是window中的最顶层view，可以从window中通过getDecorView获取到decorView。
         * 通过decorView获取到程序显示的区域，包括标题栏，但不包括状态栏。
         */
        getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
        //获取屏幕的高度
        int screenHeight = getWindow().getDecorView().getRootView().getHeight();
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
//        //存一份到本地
        if (softInputHeight > 0) {
            InputWxHeight.softKeyBordHeight = softInputHeight;
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
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int usableHeight = metrics.heightPixels;
        //获取当前屏幕的真实高度
        getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
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
}
