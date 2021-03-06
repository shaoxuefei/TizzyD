package com.shanlin.sxf;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Sxf on 2017/5/9.
 *
 * @project: Demo.
 * @detail:
 */

public class PageItemView extends LinearLayout implements ItemClickListener {
    public RecyclerView recyclerView;
    private RecycleBaseAdapter adapter;
    private ArrayList<String> arrayList = new ArrayList<>();
    private Context context;
    private SwipeRefreshLayout swipeLayout;
    Handler handler;
    private boolean canClick = true;

    public PageItemView(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public PageItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    public void setClick(boolean canClick) {
        this.canClick = canClick;
    }

    private void initView() {
        View inflate = LayoutInflater.from(context).inflate(R.layout.pageitemview, this, true);
        swipeLayout = findViewById(R.id.swipeLayout);
        recyclerView = inflate.findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new SmoothScrollLayoutManager(context));
        adapter = new RecycleBaseAdapter(arrayList, context);
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);
        for (int i = 0; i < 20; i++) {
            arrayList.add(i, i + "");
        }
        adapter.notifyDataSetChanged();
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                handler.sendEmptyMessageDelayed(1, 2000);
            }
        });
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                swipeLayout.setRefreshing(false);
                return false;
            }
        });
    }

    /**
     * adapter.notifyDataSetChanged()---当更新的数据源比源数据源多或者是相等的话--其更新不会进行列表滑动--但是如果更新后数据源比之前的数据源少的话--会进行重新渲染也就是会滑动到顶部
     * 但是如果数据源添加到头部也就是 position=0的位置的话、、、其不会滚动，，也就是位置不会变，，但是数据源会变，，也就是说之前滑上去的数据源会是新的数据源、、之前老的数据源会展示在下边，类似于把它顶下来了，
     */
    public void updateData() {
        ArrayList<String> uploadData = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            uploadData.add(i, i + "");
        }
//        arrayList.clear();
        arrayList.addAll(0, uploadData);
        adapter.notifyDataSetChanged();
        Toast.makeText(getContext(), "Update", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(int position) {
        if (canClick) {
            Toast.makeText(context, "" + position, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(context, Main4Activity.class);
            context.startActivity(intent
            );
        }
    }

    public class SmoothScrollLayoutManager extends LinearLayoutManager {

        public SmoothScrollLayoutManager(Context context) {
            super(context);
        }

        @Override
        public void smoothScrollToPosition(RecyclerView recyclerView,
                                           RecyclerView.State state, final int position) {

            LinearSmoothScroller smoothScroller = new LinearSmoothScroller(recyclerView.getContext()) {
                // 返回：滑过1px时经历的时间(ms)。
                @Override
                protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                    return 500f / displayMetrics.densityDpi;
                }
            };

            smoothScroller.setTargetPosition(position);
            startSmoothScroll(smoothScroller);
        }
    }

}
