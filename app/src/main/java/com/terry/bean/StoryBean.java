package com.terry.bean;

import java.io.Serializable;

/**
 * Created by lmz_cxm on 2015/11/28.
 */
public class StoryBean implements Serializable {
    private String url;
    private String title;
    private String mUrlContent;

    public String getmUrlContent() {
        return mUrlContent;
    }

    public void setmUrlContent(String mUrlContent) {
        this.mUrlContent = mUrlContent;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
