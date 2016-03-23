package com.simple.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Toast工具类
 */
public final class ToastUtils {

    private ToastUtils() {
    }

    public static void shortToast(Context context, int msg) {
        shortToast(context, context.getResources().getString(msg));
    }

    public static void shortToast(Context context, String msg) {
        showToast(context, msg, Toast.LENGTH_SHORT);
    }

    public static void longToast(Context context, int msg) {
        showToast(context, context.getResources().getString(msg), Toast.LENGTH_LONG);
    }

    public static void longToast(Context context, String msg) {
        showToast(context, msg, Toast.LENGTH_LONG);
    }

    private static void showToast(Context context, String msg, int duration) {
        Toast.makeText(context, msg, duration).show();
    }


}
