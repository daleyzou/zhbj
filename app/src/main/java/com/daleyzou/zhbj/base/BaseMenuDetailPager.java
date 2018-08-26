package com.daleyzou.zhbj.base;

import android.app.Activity;
import android.view.View;

/**
 * 菜单详情页基类
 */
public abstract class BaseMenuDetailPager {
    public Activity mActivity;
    public View mRootView;

    public BaseMenuDetailPager(Activity activity){
        mActivity = activity;
        mRootView = initView();
    }

    //初始化布局
    public abstract View initView();
    //初始化数据
    public void initData(){}
}
