package com.simple.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by mrsimple on 23/3/16.
 */
public final class UIUtils {
    private UIUtils() {
    }

    /**
     * get the screen size
     *
     * @return the screen size
     */
    public static int[] getScreenSize(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(
                Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return new int[]{outMetrics.widthPixels, outMetrics.heightPixels};
    }


    /**
     * 获得屏幕的英寸的英寸, 计算方法 : http://blog.csdn.net/moruite/article/details/7281428
     *
     * @return
     */
    public static double getScreenInch(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(
                Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);

        double diagonalPixels = Math.sqrt(Math.pow(outMetrics.widthPixels, 2)
                + Math.pow(outMetrics.heightPixels, 2));
        return diagonalPixels / (160 * outMetrics.density);
    }

    /**
     * dip 转为 px
     */
    public static int dp2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * px 转为 dip
     */
    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 获取视图在屏幕中的位置
     *
     * @param view
     * @return 返回一个含有2个元素的数组, 第一个元素是x坐标、第二个为y坐标
     */
    public static int[] getViewLocationInScreen(View view) {
        int[] loc = new int[2];
        view.getLocationOnScreen(loc);
        return loc;
    }

    /**
     * 获取视图在一个Window中的位置
     *
     * @param view
     * @return 返回一个含有2个元素的数组, 第一个元素是x坐标、第二个为y坐标
     */
    public static int[] getViewLocationInWindow(View view) {
        int[] loc = new int[2];
        view.getLocationInWindow(loc);
        return loc;
    }

    /**
     * 获取状态栏的高度
     *
     * @param context Context
     * @return 状态栏高度
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
