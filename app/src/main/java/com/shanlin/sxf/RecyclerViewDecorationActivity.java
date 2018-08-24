package com.shanlin.sxf;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shanlin.sxf.diyview.RecyclerViewDivider;
import com.shanlin.sxf.diyview.SwipeMenuLayout;
import com.shanlin.sxf.diyview.SwipeMenuListView;
import com.shanlin.sxf.itemtouch.MyDragItemTouchHelperCallBack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecyclerViewDecorationActivity extends AppCompatActivity {

    @BindView(R.id.menuListView)
    SwipeMenuListView menuListView;

    ScrollMyAdapter scrollMyAdapter;

    MyDragItemTouchHelperCallBack itemTouchHelperCallBack;

    List<String> dataList = new ArrayList<>();

    public String[] strings = new String[]{"张三", "李四", "王五", "赵六", "曾七"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll_compat);
        ButterKnife.bind(this);
        menuListView.setLayoutManager(new LinearLayoutManager(this));
        menuListView.addItemDecoration(new RecyclerViewDivider());
        scrollMyAdapter = new ScrollMyAdapter();
        menuListView.setAdapter(scrollMyAdapter);
        itemTouchHelperCallBack = new MyDragItemTouchHelperCallBack(scrollMyAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchHelperCallBack);
        itemTouchHelper.attachToRecyclerView(menuListView);

        for (int i = 0; i < 20; i++) {
            dataList.add(i + strings[i % strings.length]);
        }

    }

    public class ScrollMyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MyViewHolder(LayoutInflater.from(RecyclerViewDecorationActivity.this).inflate(R.layout.scroll_item_layout, parent, false));
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
                    Toast.makeText(RecyclerViewDecorationActivity.this, "" + position + "--" + myViewHolder.tvTitle.getText(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RecyclerViewDecorationActivity.this, VolumeActivity.class);
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
