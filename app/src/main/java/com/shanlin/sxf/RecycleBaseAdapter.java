package com.shanlin.sxf;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Sxf on 2017/5/8.
 */

public class RecycleBaseAdapter extends RecyclerView.Adapter {
    private ArrayList<String> arrayList;
    private Context context;
    private ItemClickListener itemClickListener;
    public RecycleBaseAdapter(ArrayList<String> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_view, null);
        //ItemV
        //
        // iew设置宽度充满全屏----RecyclerView.LayoutParams
        RecyclerView.LayoutParams layoutParams=new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(layoutParams);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.textView.setText(arrayList.get(position));
        ((ViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onClick(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public void  setOnItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener=itemClickListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public TextView textView;
        public View itemView;
        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView=itemView;
            textView = (TextView) itemView.findViewById(R.id.textView);
        }


    }
}
