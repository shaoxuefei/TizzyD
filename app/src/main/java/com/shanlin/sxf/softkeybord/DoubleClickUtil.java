package com.shanlin.sxf.softkeybord;



public class DoubleClickUtil {

    private static long lastClickTime;
    private static final int MIN_CLICK_DELAY_TIME = 300;

    public synchronized static boolean isCommonClick() {
        long time = System.currentTimeMillis();
        //time - lastClickTime < 0 是防止修改系统时间，防止修改的系统时间小于当前保存的时间，造成一直返回false，导致按钮一直无法点击
        //time - lastClickTime > MIN_CLICK_DELAY_TIME  正常情况
        if (time - lastClickTime < 0 || time - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = time;
            return true;
        } else {
            return false;
        }

    }
}
