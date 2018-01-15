package com.shanlin.sxf;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.impl.RefreshFooterWrapper;
import com.scwang.smartrefresh.layout.impl.RefreshHeaderWrapper;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.shanlin.sxf.crash.DIYCrashHandler;
import com.shanlin.sxf.diyview.MyViewItemActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyViewActivity extends AppCompatActivity {

    RecycleBaseAdapter baseAdapter;
    @BindView(R.id.listView)
    RecyclerView listView;
    @BindView(R.id.smartLayout)
    SmartRefreshLayout relayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_view);
        ButterKnife.bind(this);
        initView();
        initDialog();
    }

    private void initView() {
        ArrayList<String> dataList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            dataList.add(i + "");
        }
        baseAdapter = new RecycleBaseAdapter(dataList, this);
        listView.setLayoutManager(new LinearLayoutManager(this));
        listView.setAdapter(baseAdapter);
        relayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                //refresh
                Toast.makeText(MyViewActivity.this, "刷新", Toast.LENGTH_SHORT).show();
                relayout.finishRefresh(2000);
            }
        });
        relayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                //load
                Toast.makeText(MyViewActivity.this, "加载更多", Toast.LENGTH_SHORT).show();
                relayout.finishLoadmore(2000);
            }
        });
//        View headView = LayoutInflater.from(this).inflate(R.layout.viewrefreshhead, null);
//        relayout.setRefreshHeader(new MyRefreshHead(headView));
//        View footView = LayoutInflater.from(this).inflate(R.layout.viewrefreshhead, null);
//        TextView viewById = (TextView) footView.findViewById(R.id.tv_content);
//        viewById.setText("加载更多");
//        relayout.setRefreshFooter(new MyRefreshFoot(footView));


        baseAdapter.setOnItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(int position) {
                alertDialog.show();
            }
        });


    }

    class MyRefreshHead extends RefreshHeaderWrapper {

        public MyRefreshHead(View wrapper) {
            super(wrapper);
        }
    }

    class MyRefreshFoot extends RefreshFooterWrapper {

        public MyRefreshFoot(View wrapper) {
            super(wrapper);
        }
    }


    AlertDialog alertDialog;

    private void initDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("AlertDialog");
        builder.setMessage("是否跳转Activity到MyViewItemActivity");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(MyViewActivity.this, MyViewItemActivity.class);
                startActivity(intent);
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });
        builder.setCancelable(true);
        alertDialog = builder.create();
    }
}
