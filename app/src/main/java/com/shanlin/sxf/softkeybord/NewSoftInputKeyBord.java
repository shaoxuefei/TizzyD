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
import android.view.ViewTreeObserver;
import android.view.Window;
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
    private long softAnimationTime = 60;
    int rootViewVisibleHeight = 0;
    private boolean softWindowIsShow = false;

    public NewSoftInputKeyBord(Context context, AttributeSet attrs) {
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
                    //固定View的高度
                    lockViewHeight();
                    //隐藏表情
                    //显示键盘
                    showSoftKeyBord();
                    unLockViewHeight();
                    tvEmoji.setText(emojiStr);
                    editText.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            hideEmojiView();
                        }
                    }, softAnimationTime);
                    appCompatActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                }
                return false;
            }
        });
        relativeInput.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Log.e("aa", "addOnGlobalLayoutListener");
                //获取当前根视图在屏幕上显示的大小
                Rect r = new Rect();
                appCompatActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
                int visibleHeight = r.height();
                //根视图显示高度没有变化，可以看作软键盘显示／隐藏状态没有改变
                if (rootViewVisibleHeight == 0) {
                    rootViewVisibleHeight = visibleHeight;
                }
                if (rootViewVisibleHeight == visibleHeight) {
                    return;
                }
                //根视图显示高度变小超过200，可以看作软键盘显示了
                if (rootViewVisibleHeight - visibleHeight > 200) {
                    softWindowIsShow = true;
                    rootViewVisibleHeight = visibleHeight;
                    return;
                }

                //根视图显示高度变大超过200，可以看作软键盘隐藏了
                if (visibleHeight - rootViewVisibleHeight > 200) {
                    if (tvEmoji.getText().toString().equals(emojiStr)) {
                        linear_translate.callOnClick();
                    }
                    InputWxHeight.softKeyBordHeight = visibleHeight - rootViewVisibleHeight;
                    rootViewVisibleHeight = visibleHeight;
                    softWindowIsShow = false;
                }
            }
        });


        relativeInput.addOnLayoutChangeListener(new OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {

            }
        });
    }

    @OnClick({R.id.tv_emoji, R.id.tv_send, R.id.linear_translate})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_emoji:
                if (!DoubleClickUtil.isCommonClick()) {
                    return;
                }
                String tvStr = tvEmoji.getText().toString();
                if (emojiStr.equals(tvStr)) {
                    if (softWindowIsShow) {
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
                    appCompatActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                    tvEmoji.setText(textStr);
                } else if (textStr.equals(tvStr)) {
                    appCompatActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
                    //固定View的高度
                    lockViewHeight();
                    //隐藏表情
                    //显示键盘
                    showSoftKeyBord();
                    unLockViewHeight();
                    tvEmoji.setText(emojiStr);
                    editText.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            hideEmojiView();
                        }
                    }, softAnimationTime);
                    appCompatActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

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
                hideSoftKeyBord();
                softWindowIsShow = false;
                hideEmojiView();
                unLockViewHeight();
                if (tvEmoji.getText().equals(textStr)) {
                    setVisibility(GONE);
                } else {
                    editText.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //估计这里会影响到 RelativeLayout.addOnLayoutChangeListener()的监听,因为setVisibility为gone后--监听就不走了，oldBottom和bottom没置换
                            setVisibility(GONE);
                        }
                    }, softAnimationTime);
                }
                tvEmoji.setText(emojiStr);
                appCompatActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                break;
        }
    }


    public void showSoftKeyBord() {
        Log.e("aa", "showSoftKeyBord");
        appCompatActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setVisibility(VISIBLE);
        tvEmoji.setText(emojiStr);
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);//需要加上这个才会起作用--自动弹出
        editText.requestFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.showSoftInput(editText, 0);
        }
    }

    private void hideSoftKeyBord() {
        InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        }
        Log.e("aa", "hideSoftKeyBord");
    }

    private void lockViewHeight() {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) linear_translate.getLayoutParams();
        layoutParams.height = linear_translate.getHeight();
        layoutParams.weight = 0f;
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

    public int getKeyBoardHeight() {
        return InputWxHeight.softKeyBordHeight;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        //监听物理返回键
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0 && getVisibility() == VISIBLE) {
            linear_translate.callOnClick();
            return true;
        }
        Toast.makeText(appCompatActivity, "dispatchKeyEvent", Toast.LENGTH_SHORT).show();
        return super.dispatchKeyEvent(event);
    }


}
