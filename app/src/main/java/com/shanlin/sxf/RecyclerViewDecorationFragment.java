package com.shanlin.sxf;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.shanlin.sxf.diyview.RecyclerViewDivider;
import com.shanlin.sxf.diyview.SwipeMenuLayout;
import com.shanlin.sxf.diyview.SwipeMenuListView;
import com.shanlin.sxf.itemtouch.MyDragItemTouchHelperCallBack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecyclerViewDecorationFragment extends BaseFragment {

    @BindView(R.id.menuListView)
    SwipeMenuListView menuListView;
    @BindView(R.id.smartLayout)
    SmartRefreshLayout relayout;
    @BindView(R.id.nestScrollView)
    NestedScrollView nestScrollView;


    ScrollMyAdapter scrollMyAdapter;

    MyDragItemTouchHelperCallBack itemTouchHelperCallBack;

    List<String> dataList = new ArrayList<>();

    public String[] strings = new String[]{"张三", "李四", "王五", "赵六", "曾七"};

    @Override
    public int getLayoutId() {
        Log.e("aa", "RecyclerViewDecorationFragment----onCreateView");
        return R.layout.activity_scroll_compat;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e("aa", "RecyclerViewDecorationFragment-----onViewCreated");
    }

    @Override
    public void initView(View inflate) {
        initView();
    }

    public static RecyclerViewDecorationFragment newInstance() {

        Bundle args = new Bundle();

        RecyclerViewDecorationFragment fragment = new RecyclerViewDecorationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private void initView() {
        menuListView.setLayoutManager(new LinearLayoutManager(getContext()));
        menuListView.addItemDecoration(new RecyclerViewDivider());
        scrollMyAdapter = new ScrollMyAdapter();
        menuListView.setAdapter(scrollMyAdapter);
        itemTouchHelperCallBack = new MyDragItemTouchHelperCallBack(scrollMyAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchHelperCallBack);
        itemTouchHelper.attachToRecyclerView(menuListView);
        for (int i = 0; i < 20; i++) {
            dataList.add(i + strings[i % strings.length]);
        }

        relayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                //refresh
                relayout.finishRefresh(2000);
            }
        });
        relayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                //load
                relayout.finishLoadmore(2000);
            }
        });

        DisplayMetrics displayMetrics = new DisplayMetrics();
        Display defaultDisplay = getActivity().getWindowManager().getDefaultDisplay();
        defaultDisplay.getMetrics(displayMetrics);
        final float density = displayMetrics.density;
        nestScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                Log.e("aa", "scrollY" + scrollY);
                if (scrollY < density * 130) {
                    nestScrollView.setNestedScrollingEnabled(false);
                } else {
                    nestScrollView.setNestedScrollingEnabled(true);
                }
            }
        });

    }

    public class ScrollMyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MyViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.scroll_item_layout, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            final MyViewHolder myViewHolder = (MyViewHolder) holder;
//            myViewHolder.swipeLayout.setContentView(myViewHolder.tvTitle);
//            myViewHolder.swipeLayout.setMenuView(myViewHolder.menuView);
            myViewHolder.tvTitle.setText(dataList.get(position));
            myViewHolder.tvTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), "" + position + "--" + myViewHolder.tvTitle.getText(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getContext(), VolumeActivity.class);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }

        //数据交换
        public void onItemMove(int fromPosition, int toPosition) {
            Collections.swap(dataList, fromPosition, toPosition);
            //step 1
            notifyItemMoved(fromPosition, toPosition);
            //step  2
            notifyItemRangeChanged(fromPosition, 1);
            notifyItemRangeChanged(toPosition, 1);
        }

        //数据删除
        public void onItemDissmiss(int position) {
            dataList.remove(position);
            //该移出只是为了更新RecyclerView的外部的列表集合、也可以理解为只是显示了移出的效果、、此时对应的RecyclerView的Item的position没变、也就是RecyclerView的显示、内部的data还没有更新
            notifyItemRemoved(position);
            //该操作是为了更新RecyclerView的内部数据源、也就是更新当前界面remove后的position
            notifyItemRangeChanged(position, dataList.size());
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tvTitle, menuView;
            LinearLayout swipeLayout;

            public MyViewHolder(View itemView) {
                super(itemView);
                tvTitle = itemView.findViewById(R.id.tv_title);
                menuView = itemView.findViewById(R.id.menuView);
                swipeLayout = itemView.findViewById(R.id.swipeLayout);
            }
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e("aa", "RecyclerViewDecorationFragment-----onDestroyView");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("aa", "RecyclerViewDecorationFragment-----onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("aa", "RecyclerViewDecorationFragment-----onPause");
    }


    @Override
    public void fragmentIsVisible(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            Toast.makeText(getContext(), "RecyclerViewDecorationFragment--LoadData", Toast.LENGTH_SHORT).show();
            Log.e("aa", "RecyclerViewDecorationFragment-----isShow");
        } else {
            Log.e("aa", "RecyclerViewDecorationFragment-----isHide");
        }
    }


}
