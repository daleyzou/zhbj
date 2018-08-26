package com.daleyzou.zhbj.domain;

import java.util.ArrayList;

/**
 * 页签详情对象
 */
public class NewsTabBean {

    public NewsTab data;

    public class NewsTab{
        public ArrayList<NewsData> feed;

    }

    /**
     * 新闻列表对象
     */
    public class NewsData{
        public String newsId;
        public String kpic;
        public String pubDate;
        public String title;
        public String category;
        public String link;
    }
}
