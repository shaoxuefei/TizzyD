package com.shanlin.sxf.diyview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shanlin.sxf.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author : SXF
 * @ date   : 2018/12/19
 * Description :
 */
public class DiyFloatView extends LinearLayout {

    @BindView(R.id.tv_play)
    TextView tvPlay;
    @BindView(R.id.tv_pause)
    TextView tvPause;
    @BindView(R.id.tv_delete)
    TextView tvDelete;


    public DiyFloatView(Context context) {
        super(context);
        View inflate = LayoutInflater.from(context).inflate(R.layout.float_window_view, this, true);
        ButterKnife.bind(this, inflate);
    }


    @OnClick({R.id.tv_play, R.id.tv_pause, R.id.tv_delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_play:
                if (onItemClickListener != null) {
                    onItemClickListener.onPlay();
                }
                break;
            case R.id.tv_pause:
                if (onItemClickListener != null) {
                    onItemClickListener.onPause();
                }
                break;
            case R.id.tv_delete:
                if (onItemClickListener != null) {
                    onItemClickListener.onDelete();
                }
                break;
        }
    }

    OnItemClickListener onItemClickListener;

    public void setOnTypeClick(OnItemClickListener onTypeClick) {
        onItemClickListener = onTypeClick;
    }

    public interface OnItemClickListener {
        void onPlay();

        void onPause();

        void onDelete();
    }
}
