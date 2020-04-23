package com.shanlin.sxf;

import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by shaoxuefei on 2020/4/15.
 */
public class TestUtils {

    private ArrayList<PageItemView> mArrayList;

    public void setListArray(ArrayList<PageItemView> arrayList){
        mArrayList=arrayList;
    }

    public void toastMsg(){
        if(mArrayList!=null){
            Log.e("aa","mArrayList的Size:"+mArrayList.size());
        }
    }
}
