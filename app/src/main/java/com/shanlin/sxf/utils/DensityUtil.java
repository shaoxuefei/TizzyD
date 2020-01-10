package com.shanlin.sxf.utils;

import android.content.Context;

import com.shanlin.sxf.application.MyApplication;

/**
 * Created by panjiaqiang on 2019/6/27.
 */
public class DensityUtil {

    private static float density;
    private static float scaledDensity;

    static {

    }

    /**
     * 将dip或dp值转换为px值
     *
     * @param dip
     * @return
     */
    public static int dp2px(Context context, int dip) {
        if (density == 0&&context!=null) {
            density = context.getResources().getDisplayMetrics().density;
            scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        }
        return (int) (dip * density + 0.5);
    }

    public static int sp2px(Context context,float spValue) {
        if (density == 0&&context!=null) {
            density = context.getResources().getDisplayMetrics().density;
            scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        }
        return (int) (spValue * scaledDensity + 0.5f);
    }
}
