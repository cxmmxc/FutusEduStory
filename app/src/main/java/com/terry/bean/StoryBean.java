package com.terry.bean;

import java.io.Serializable;

/**
 * Created by lmz_cxm on 2015/11/28.
 */
public class StoryBean implements Serializable {
    private String picUrl;
    private String title;
    private String mContentUrl;

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

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
}
