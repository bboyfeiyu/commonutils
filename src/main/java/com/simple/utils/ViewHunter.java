package com.simple.utils;

import android.view.View;

/**
 * 避免每次findViewById都进行类型转换的工具类
 *
 * Created by mrsimple on 15/3/16.
 */
public class ViewHunter {
    public View mRootView;

    public ViewHunter(View rootView) {
        mRootView = rootView;
    }

    public <T> T findViewById(int viewId) {
        if (mRootView == null) {
            throw new NullPointerException("在 " + this.getClass().getName() + " 实例中 mRootView 为空!!!! ");
        }
        return (T) mRootView.findViewById(viewId);
    }

}
