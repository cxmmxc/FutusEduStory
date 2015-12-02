package com.terry.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * Created by lmz_cxm on 2015/11/28.
 */
@Table(name="StoryBean")
public class StoryBean implements Serializable {

    @Column(name = "id", isId = true)
    private int id;
    @Column(name="picUrl")
    private String picUrl;
    @Column(name="title")
    private String title;
    @Column(name="mContentUrl")
    private String mContentUrl;
    @Column(name="isCollect")
    private int isCollect;//默认0是未收藏，1是收藏

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIsCollect() {
        return isCollect;
    }

    public void setIsCollect(int isCollect) {
        this.isCollect = isCollect;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    @Column(name="Content")
    private String Content;

    public String getmContentUrl() {
        return mContentUrl;
    }

    public void setmContentUrl(String mContentUrl) {
        this.mContentUrl = mContentUrl;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "StoryBean{" +
                "id=" + id +
                ", picUrl='" + picUrl + '\'' +
                ", title='" + title + '\'' +
                ", mContentUrl='" + mContentUrl + '\'' +
                ", isCollect=" + isCollect +
                ", Content='" + Content + '\'' +
                '}';
    }
}
