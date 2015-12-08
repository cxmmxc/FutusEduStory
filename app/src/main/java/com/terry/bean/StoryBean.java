package com.terry.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * Created by lmz_cxm on 2015/11/28.
 */
@Table(name="StoryBean")
public class StoryBean extends BmobObject {

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
    @Column(name = "Content")
    private String Content;
    @Column(name = "isRead")//默认0是未读，1是已读
    private int isRead;

    public String getPersonObjId() {
        return personObjId;
    }

    public void setPersonObjId(String personObjId) {
        this.personObjId = personObjId;
    }

    private String personObjId;//对应的用户的personId，用于收藏用

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

    public int getIsRead() {
        return isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
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
                ", isRead=" + isRead +
                '}';
    }
}
