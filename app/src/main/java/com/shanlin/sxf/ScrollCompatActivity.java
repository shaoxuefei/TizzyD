package com.shanlin.sxf;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shanlin.sxf.diyview.RecyclerViewDivider;
import com.shanlin.sxf.diyview.SwipeMenuLayout;
import com.shanlin.sxf.diyview.SwipeMenuListView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ScrollCompatActivity extends AppCompatActivity {

    @BindView(R.id.menuListView)
    SwipeMenuListView menuListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll_compat);
        ButterKnife.bind(this);
        menuListView.setLayoutManager(new LinearLayoutManager(this));
        menuListView.addItemDecoration(new RecyclerViewDivider());
        menuListView.setAdapter(new ScrollMyAdapter());


    }

    class ScrollMyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MyViewHolder(LayoutInflater.from(ScrollCompatActivity.this).inflate(R.layout.scroll_item_layout, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            MyViewHolder myViewHolder = (MyViewHolder) holder;
            myViewHolder.swipeLayout.setContentView(myViewHolder.tvTitle);
            myViewHolder.swipeLayout.setMenuView(myViewHolder.menuView);
            myViewHolder.tvTitle.setText("sayHei" + position);
        }

        @Override
        public int getItemCount() {
            return 20;
        }
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, menuView;
        SwipeMenuLayout swipeLayout;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            menuView = itemView.findViewById(R.id.menuView);
            swipeLayout = itemView.findViewById(R.id.swipeLayout);
        }
    }

    /**
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();

    }

    /**
     * if (mWindow.shouldCloseOnTouch(this, event)) {
     * finish();
     * return true;
     * }
     * return false;
     *
     * @param event
     * @return 默认是在点击非页面外部区域--都是false
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        return super.onTouchEvent(event);
    }
}
