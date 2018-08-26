package com.daleyzou.zhbj.base;

import android.app.Activity;
import android.media.Image;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.daleyzou.zhbj.MainActivity;
import com.daleyzou.zhbj.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

/**
 * 5个标签页的基类
 */
public class BasePager {
    public Activity mActivity;
    public TextView tvTitle;
    public ImageButton btnMenu;
    public FrameLayout flContent; //空的帧布局对象
    public View mRootView; //当前页面的布局文件对象

    public ImageButton btnPhoto;// 组图切换按钮

    public BasePager(Activity activity){
        mActivity = activity;
        System.out.println("BasePager被创建！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！");
        mRootView = initView();
    }

    /**
     * 初始化布局
     * @return
     */
    public View initView(){
        View view = View.inflate(mActivity, R.layout.base_pager, null);
        tvTitle = (TextView)view.findViewById(R.id.tv_title);
        btnMenu = (ImageButton)view.findViewById(R.id.btn_menu);
        btnPhoto = (ImageButton)view.findViewById(R.id.btn_photo);
        flContent = (FrameLayout)view.findViewById(R.id.fl_content);

        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle();
            }
        });
        return view;
    }

    /**
     * 打开或者关闭侧边栏
     */
    private void toggle() {
        MainActivity mainUI = (MainActivity) mActivity;
        SlidingMenu slidingMenu = mainUI.getSlidingMenu();
        slidingMenu.toggle();//如果当前状态是开，调用后就关；反之亦然
    }

    /**
     * 初始化数据
     */
    public void initData(){

    }
}
