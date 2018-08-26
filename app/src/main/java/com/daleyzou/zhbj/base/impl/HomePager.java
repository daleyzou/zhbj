package com.daleyzou.zhbj.base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.daleyzou.zhbj.base.BasePager;

/**
 * 首页
 */
public class HomePager extends BasePager {
    public HomePager(Activity activity) {
        super(activity);
        System.out.println("HomePager被创建！");
    }

    @Override
    public void initData() {
        //要给帧布局填充布局对象
        TextView view = new TextView(mActivity);
        view.setText("首页");
        view.setTextColor(Color.RED);
        view.setTextSize(22);
        view.setGravity(Gravity.CENTER);

        flContent.addView(view);

        //修改页面标题
        tvTitle.setText("首页");

        //隐藏菜单按钮
        btnMenu.setVisibility(View.GONE);
    }
}
