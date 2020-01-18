package com.shanlin.sxf.softkeybord;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
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

/**
 * 软键盘封装View---只有底部一丁点
 */
public class SoftInputKeyBordView extends LinearLayout {

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
    EmojiAdapter emojiAdapter;
    View inflate;
    private String emojiStr = "emoji";
    private String textStr = "text";

    public SoftInputKeyBordView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        inflate = LayoutInflater.from(context).inflate(R.layout.layout_soft_input_keybord, this, true);
        ButterKnife.bind(this, inflate);
        initView();
    }

    private void initView() {
        ArrayList emojiList = new ArrayList<>();
        for (int i = 0; i < 56; i++) {
            emojiList.add(String.valueOf(i));
        }
        emojiAdapter = new EmojiAdapter(getContext(), emojiList);
        recycleViewEmoji.setLayoutManager(new GridLayoutManager(getContext(), 8));
        recycleViewEmoji.setAdapter(emojiAdapter);

        editText.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
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
                }
                return false;
            }
        });
    }


    @OnClick({R.id.tv_emoji, R.id.tv_send})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_emoji:
                String tvStr = tvEmoji.getText().toString();
                if (emojiStr.equals(tvStr)) {
                    //固定高度
                    lockViewHeight();
                    //隐藏键盘
                    hideSoftKeyBord();
                    editText.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //显示表情
                            showEmojiView();
                            unLockViewHeight();
                        }
                    }, 200);
//                    //还原高度
                    tvEmoji.setText(textStr);
                } else if (textStr.equals(tvStr)) {
                    //固定View的高度
//                    lockViewHeight();
//                    unLockViewHeight();
                    //隐藏表情
                    hideEmojiView();
                    //显示键盘
                    showSoftKeyBord();
//                    showSoftKeyBord();
//                    unLockViewHeight();
                    tvEmoji.setText(emojiStr);
                }
                break;
            case R.id.tv_send:
                String editStr = editText.getText().toString();
                if (!TextUtils.isEmpty(editStr)) {
                    Toast.makeText(getContext(), editStr, Toast.LENGTH_SHORT).show();
                }
                editText.setText("");
                break;
        }
    }


    private void showSoftKeyBord() {
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
    }

    private void lockViewHeight() {
        inflate.getLayoutParams().height = getKeyBordHeight() + DensityUtil.dp2px(getContext(), 60);
    }

    private void hideEmojiView() {
        recycleViewEmoji.setVisibility(GONE);
    }

    private void unLockViewHeight() {
        inflate.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
    }

    private void showEmojiView() {
        LinearLayout.LayoutParams layoutParams = (LayoutParams) recycleViewEmoji.getLayoutParams();
        layoutParams.height = getKeyBordHeight();
        recycleViewEmoji.setVisibility(VISIBLE);
    }

    /**
     * 获取软键盘的高度
     *
     * @return
     */
    private float bottomY;

    private int getKeyBordHeight() {
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
                    //软键盘缩下去了
                    int dexBottom = oldBottom - bottom;
                }

                Log.e("aa", "bottomY: " + bottom + "oldBottom Y " + oldBottom);
            }
        });

        return 835;
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

}
