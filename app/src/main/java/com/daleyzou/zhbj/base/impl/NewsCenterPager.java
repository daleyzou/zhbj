package com.daleyzou.zhbj.base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.daleyzou.zhbj.MainActivity;
import com.daleyzou.zhbj.base.BaseMenuDetailPager;
import com.daleyzou.zhbj.base.BasePager;
import com.daleyzou.zhbj.base.impl.menu.NewsMenuDetailPager;
import com.daleyzou.zhbj.domain.NewsMenu;
import com.daleyzou.zhbj.global.GlobalConstants;
import com.daleyzou.zhbj.utils.CacheUtils;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import java.util.ArrayList;

/**
 * 首页
 */
public class NewsCenterPager extends BasePager {
    private ArrayList<BaseMenuDetailPager> mMenuDetailPagers;//菜单详情页集合
    private NewsMenu mNewsData;
    public NewsCenterPager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
//        //要给帧布局填充布局对象
//        TextView view = new TextView(mActivity);
//        view.setText("新闻中心");
//        view.setTextColor(Color.RED);
//        view.setTextSize(22);
//        view.setGravity(Gravity.CENTER);
//
//        flContent.addView(view);
        //修改页面标题
        tvTitle.setText("新闻");
        btnMenu.setVisibility(View.VISIBLE);

        //先判断有没有缓存
        String cache = CacheUtils.getCache(GlobalConstants.CATEGORY_URL, mActivity);
        if(!TextUtils.isEmpty(cache)){
            System.out.println("发现缓存啦！");
            processData(cache);
        }else {
            //请求服务器获取数据
            getDataFromServer();
        }

    }

    /**
     * 从服务器获取数据
     */
    private void getDataFromServer() {
        HttpUtils utils = new HttpUtils();
        utils.send(HttpMethod.GET, GlobalConstants.CATEGORY_URL,new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                //请求成功
                String result = responseInfo.result;
                System.out.println("服务器返回结果："+result);
                
                processData(result);

                //写缓存
                CacheUtils.setCache(GlobalConstants.CATEGORY_URL,result,mActivity);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                //请求失败
                e.printStackTrace();
                Toast.makeText(mActivity,s,Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 解析数据
     * @param json
     */
    private void processData(String json) {
        Gson gson = new Gson();
        mNewsData = gson.fromJson(json, NewsMenu.class);
        System.out.print(mNewsData);


        //初始化菜单详情页
        mMenuDetailPagers = new ArrayList<BaseMenuDetailPager>();
        mMenuDetailPagers.add(new NewsMenuDetailPager(mActivity,mNewsData.data.get(0).children));


        // 将新闻菜单详情页设为默认界面
        setCurrentDetailPager(0);
    }

    /**
     * 设置菜单详情页
     * @param position
     */
    public void setCurrentDetailPager(int position){
        //重新改FarameLayout添加内容
        BaseMenuDetailPager pager = mMenuDetailPagers.get(position);
        View view = pager.mRootView;// 当前页面的布局

        // 清除以前的布局
        flContent.removeAllViews();

        flContent.addView(view);// 给帧布局添加布局

        // 初始化页面数据
        pager.initData();

        // 更新标题
        tvTitle.setText(mNewsData.data.get(position).title);

        // 如果是组图页面，需要显示切换按钮

            btnPhoto.setVisibility(View.INVISIBLE);
    }
}
