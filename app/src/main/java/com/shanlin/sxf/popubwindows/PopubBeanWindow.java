package com.shanlin.sxf.popubwindows;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.shanlin.sxf.R;

import java.util.ArrayList;

/**
 * Created by Sxf on 2017/5/15.
 *
 * @project: Demo.
 * @detail:
 */

public class PopubBeanWindow extends PopupWindow implements AdapterView.OnItemClickListener {
    private ArrayList<PopubBeanInfo> arrayList;
    private ListView listView;
    private Context context;
    private PopubViewAdapter adapter;
    private PopOnItemClickListener onItemClickListener;
    private int lastPosition=0;
    public PopubBeanWindow(Context context, ArrayList<PopubBeanInfo> arrayList) {
        super(context);
        this.arrayList = arrayList;
        this.context=context;
        initContentView();
    }

    private void initContentView(){
        View view = LayoutInflater.from(context).inflate(R.layout.popub_contentview, null);
        listView= (ListView) view.findViewById(R.id.listView);
        adapter=new PopubViewAdapter();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        setContentView(view);
        setTouchable(true);
        setOutsideTouchable(true);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x99000000);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
//        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的背景
        setBackgroundDrawable(dw);//设置背景使有些版本：点击外部或返回键无法消毁dimiss();
//        //设置点击外部背景部分使其消失
//        view.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                int bottom = listView.getBottom();
//                int y = (int) event.getY();
//                if(event.getAction()==MotionEvent.ACTION_UP){
//                    if(y>bottom){
//                        dismiss();
//                    }
//                }
//                return true;
//            }
//        });
    }

    public void setOnItemClickListener(PopOnItemClickListener onItemClickListener){
        this.onItemClickListener=onItemClickListener;
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        arrayList.get(lastPosition).isCheck=false;
        onItemClickListener.onItemClickListener(position);
        arrayList.get(position).isCheck=true;
        lastPosition=position;
        dismiss();
    }
    int []location=new int[2];
    public void show(View view){
//        int width = view.getWidth();
//        setWidth(width);
        view.getLocationInWindow(location);
        int x= location[0];
        int y= location[1];
        showAtLocation(view,Gravity.NO_GRAVITY,x,y+ view.getHeight());
        Log.e("aa","locationX:"+x+"locationY:"+y+"viewX"+view.getX()+"viewY"+view.getY()+"top"+view.getTop()+"viewLeft"+view.getLeft());
    }

    class PopubViewAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            if(arrayList!=null){
                return arrayList.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return arrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if(convertView==null) {
                viewHolder=new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.popub_itemview, null);
                viewHolder.itemContent= (TextView) convertView.findViewById(R.id.tv_item);
                convertView.setTag(viewHolder);
            }else {
                viewHolder= (ViewHolder) convertView.getTag();
            }
            PopubBeanInfo popubBeanInfo = arrayList.get(position);
            if(popubBeanInfo.isCheck){
                viewHolder.itemContent.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
            }else {
                viewHolder.itemContent.setBackgroundColor(Color.WHITE);
            }
            viewHolder.itemContent.setText(popubBeanInfo.contentTitle);
            return convertView;
        }
    }

    class ViewHolder {
        TextView itemContent;
    }
}
