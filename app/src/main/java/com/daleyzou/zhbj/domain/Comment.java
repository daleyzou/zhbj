package com.daleyzou.zhbj.domain;

public class Comment {
    public String newsId;
    public String commentDate;
    public String content;

    public Comment(String newsId, String commentDate, String content) {
        this.newsId = newsId;
        this.commentDate = commentDate;
        this.content = content;
    }

    public String getNewsId() {
        return newsId;
    }

    public void setNewsId(String newsId) {
        this.newsId = newsId;
    }

    public String getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(String commentDate) {
        this.commentDate = commentDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
