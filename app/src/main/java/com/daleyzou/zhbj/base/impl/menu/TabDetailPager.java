package com.daleyzou.zhbj.base.impl.menu;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.daleyzou.zhbj.NewsDetailActivity;
import com.daleyzou.zhbj.R;
import com.daleyzou.zhbj.base.BaseMenuDetailPager;
import com.daleyzou.zhbj.domain.Comment;
import com.daleyzou.zhbj.domain.NewsMenu;
import com.daleyzou.zhbj.domain.NewsTabBean;
import com.daleyzou.zhbj.global.GlobalConstants;
import com.daleyzou.zhbj.utils.CacheUtils;
import com.daleyzou.zhbj.utils.PrefUtils;
import com.daleyzou.zhbj.view.PullRefreshListView;
import com.daleyzou.zhbj.view.TopNewsViewPager;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;

public class TabDetailPager extends BaseMenuDetailPager {

    private NewsMenu.NewsTabData mTabData;//单个页签的网络数据
//    private TextView view;

    private String mUrl;
    private ArrayList<NewsTabBean.NewsData> mNewsList;
    private NewsAdapter mNewsAdapter;

    @ViewInject(R.id.lv_tab_detail_list)
    private PullRefreshListView lvList;
    private String mMoreUrl; // 下一页数据链接


    public TabDetailPager(Activity activity) {
        super(activity);
    }

    public TabDetailPager(Activity mActivity, NewsMenu.NewsTabData newsTabData) {
        super(mActivity);
        mTabData = newsTabData;
        mUrl = newsTabData.url;
    }

    @Override
    public View initView() {
//        view = new TextView(mActivity);
//        //view.setText(mTabData.title);  此处空指针
//        view.setTextColor(Color.RED);
//        view.setTextSize(22);
//        view.setGravity(Gravity.CENTER);
        View view = View.inflate(mActivity, R.layout.pager_tab_detail, null);
        ViewUtils.inject(this,view);



       // 5.前端界面设置回调
        lvList.setOnRefreshListener(new PullRefreshListView.OnRefreshListener() {
            @Override
            public void onRefersh() {
                // 刷新数据
                getDataFromServer();
            }

            @Override
            public void onLoadMore() {
                // 判断是否有下一页数据
                if (mMoreUrl != null){
                    getMoreDataFromServer();
                }else {
                    Toast.makeText(mActivity,"没有更多多数据了", Toast.LENGTH_SHORT).show();
                    lvList.onRefreshComplete(true);// 没有数据时也要收起控件
                }
            }
        });

        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int headerViewsCount = lvList.getHeaderViewsCount();// 获取头布局数量
                position = position - headerViewsCount;// 需要减去头布局的占位
                System.out.println("第 "+ position+ "被点击了！");
                NewsTabBean.NewsData news = mNewsList.get(position);

                // read_ids: 记录已经被点击的新闻item的id
                String readIds = PrefUtils.getString(mActivity, "read_ids", "");
                if (!readIds.contains(news.newsId + "")){// 只有不包含当前id才追加
                    readIds = readIds + news.newsId + ",";
                    PrefUtils.setString(mActivity,"read_ids",readIds);
                }
                // 要将被点击的item的文字改为灰色
                TextView tvItemTitle = (TextView)view.findViewById(R.id.tv_item_title);
                tvItemTitle.setTextColor(Color.GRAY);

                // 跳到新闻详情页面
                Intent intent = new Intent(mActivity, NewsDetailActivity.class);
                intent.putExtra("url",news.link);
                intent.putExtra("newsId",news.newsId);
                mActivity.startActivity(intent);
            }
        });
        return view;
    }

    /**
     * 加载下一页数据
     */
    private void getMoreDataFromServer() {
        HttpUtils utils = new HttpUtils();
        System.out.println("mMoreUrl: " + mMoreUrl);
        utils.send(HttpRequest.HttpMethod.GET, mMoreUrl, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                processData(result, true);

                // 收起下拉刷新控件
                lvList.onRefreshComplete(true);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                //请求失败
                e.printStackTrace();
                Toast.makeText(mActivity,s,Toast.LENGTH_SHORT).show();

                // 收起下拉刷新控件
                lvList.onRefreshComplete(false);
            }
        });
    }

    @Override
    public void initData() {
//        view.setText(mTabData.title);
        String cache = CacheUtils.getCache(mUrl, mActivity);
        if (!TextUtils.isEmpty(cache)){
            processData(cache, false);
        }
        getDataFromServer();
    }

    private void getDataFromServer() {
        HttpUtils utils = new HttpUtils();
        utils.send(HttpRequest.HttpMethod.GET, mUrl, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                processData(result, false);

                CacheUtils.setCache(mUrl,result,mActivity);
                // 收起下拉刷新控件
                lvList.onRefreshComplete(true);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                //请求失败
                e.printStackTrace();
                Toast.makeText(mActivity,s,Toast.LENGTH_SHORT).show();

                // 收起下拉刷新控件
                lvList.onRefreshComplete(false);
            }
        });
    }

    private void processData(String result, boolean isMore) {
        Gson gson = new Gson();
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println(result);
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++");
        NewsTabBean newsTabBean = gson.fromJson(result, NewsTabBean.class);
        String moreUrl = null;//GlobalConstants.MORE_URL;
        if(!TextUtils.isEmpty(moreUrl)){
            mMoreUrl = GlobalConstants.MORE_URL;
        }else {
            mMoreUrl = null;
        }
        if (!isMore){
            // 列表新闻
            mNewsList = newsTabBean.data.feed;
            if (mNewsList != null){
                mNewsAdapter = new NewsAdapter();
                lvList.setAdapter(mNewsAdapter);
            }

        }else {
            // 加载更多数据
            ArrayList<NewsTabBean.NewsData> moreNews = newsTabBean.data.feed;
            mNewsList.addAll(moreNews);// 将数据追加到原来的集合中
            // 刷新listview
            mNewsAdapter.notifyDataSetChanged();
        }
    }



    class NewsAdapter extends BaseAdapter{
        private BitmapUtils mBitmapUtils;
        public NewsAdapter(){
            mBitmapUtils = new BitmapUtils(mActivity);
            mBitmapUtils.configDefaultLoadingImage(R.drawable.news_pic_default);
        }

        @Override
        public int getCount() {
            return mNewsList.size();
        }

        @Override
        public Object getItem(int position) {
            return mNewsList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null){
                convertView = View.inflate(mActivity,R.layout.list_item_news,null);
                holder = new ViewHolder();
                holder.ivIcon = (ImageView)convertView.findViewById(R.id.iv_icon);
                holder.tvTitle = (TextView)convertView.findViewById(R.id.tv_item_title);
                holder.tvDate = (TextView)convertView.findViewById(R.id.tv_item_date);
                convertView.setTag(holder);
            }else {
                holder = (ViewHolder) convertView.getTag();
            }
            NewsTabBean.NewsData  news = (NewsTabBean.NewsData) getItem(position);
            holder.tvTitle.setText(news.title);
            holder.tvDate.setText(news.pubDate);
            // 根据本地记录标记已读、未读
            String readIds = PrefUtils.getString(mActivity, "read_ids", "");
            if (readIds.contains(news.newsId + "")){
                holder.tvTitle.setTextColor(Color.GRAY);
            }else {
                holder.tvTitle.setTextColor(Color.BLACK);
            }

            mBitmapUtils.display(holder.ivIcon,news.kpic);
            return convertView;
        }
    }
    static class ViewHolder{
        public ImageView ivIcon;
        public TextView tvTitle;
        public TextView tvDate;
    }
}
