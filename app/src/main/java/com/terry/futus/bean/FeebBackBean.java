package com.terry.futus.bean;

import cn.bmob.v3.BmobObject;

/**
 * 作者：Terry.Chen on 2016/1/261130.
 * 邮箱：herewinner@163.com
 * 描述：用户反馈bean
 */
public class FeebBackBean extends BmobObject {
    private String userName;
    private String mobilephone;
    private String email;
    private String qq;
    private String feedContent;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMobilephone() {
        return mobilephone;
    }

    public void setMobilephone(String mobilephone) {
        this.mobilephone = mobilephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getFeedContent() {
        return feedContent;
    }

    public void setFeedContent(String feedContent) {
        this.feedContent = feedContent;
    }
}
