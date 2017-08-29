package com.shanlin.sxf;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
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
    private RecyclerView recyclerView;
    private RecycleBaseAdapter adapter;
    private ArrayList<String> arrayList=new ArrayList<>();
    private Context context;
    private SwipeRefreshLayout swipeLayout;
    public PageItemView(Context context) {
        super(context);
        this.context=context;
        initView();
    }

    public PageItemView(Context context, @Nullable AttributeSet attrs) {
        super(context,attrs);
        this.context=context;
        initView();
    }
    private void initView(){
        View inflate = LayoutInflater.from(context).inflate(R.layout.pageitemview, this, true);
        swipeLayout= (SwipeRefreshLayout) findViewById(R.id.swipeLayout);
        recyclerView= (RecyclerView) inflate.findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter=new RecycleBaseAdapter(arrayList,context);
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);
        for (int i=0;i<20;i++){
            arrayList.add(i,i+"");
        }
        adapter.notifyDataSetChanged();
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    Thread.sleep(2000);
                    swipeLayout.setRefreshing(false);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    @Override
    public void onClick(int position) {
        Toast.makeText(context,""+position,Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(context,Main4Activity.class);
        context.startActivity(intent
        );
    }
}
