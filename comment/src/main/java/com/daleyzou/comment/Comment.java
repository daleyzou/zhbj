package com.daleyzou.comment;

/**
 * Created by wgyscsf on 2016/5/26.
 * 邮箱：wgyscsf@163.com
 * 博客：http://blog.csdn.net/wgyscsf
 */
public class Comment {
    private String id;
    private String nickName;
    private String imgUrl;
    private int sex;//1:男；2:女；0：未知
    private String local;
    private String content;
    private String time;
    private String  replyed;//回复了谁

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public Comment() {
    }

    public Comment(String id, String nickName, String imgUrl, int sex, String local, String content, String time, String replyed) {
        this.id = id;
        this.nickName = nickName;
        this.imgUrl = imgUrl;
        this.sex = sex;
        this.local = local;
        this.content = content;
        this.time = time;
        this.replyed = replyed;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getReplyed() {
        return replyed;
    }

    public void setReplyed(String replyed) {
        this.replyed = replyed;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id='" + id + '\'' +
                ", nickName='" + nickName + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", local='" + local + '\'' +
                ", content='" + content + '\'' +
                ", time='" + time + '\'' +
                ", replyed='" + replyed + '\'' +
                '}';
    }
}
