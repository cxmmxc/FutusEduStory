package com.terry.futus.bean;

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
    @Column(name="title")
    private String title;
    @Column(name="description")
    private String description;
    @Column(name="img")
    private String img;
    @Column(name="url")
    private String url;
    @Column(name="isCollect")
    private int isCollect;//默认0是未收藏，1是收藏
    @Column(name = "Content")
    private String Content;
    @Column(name = "isRead")//默认0是未读，1是已读
    private int isRead;
    @Column(name="smallimg")
    private String smallimg;

    private String personObjId;//对应的用户的personId，用于收藏用

    public String getPersonObjId() {
        return personObjId;
    }

    public void setPersonObjId(String personObjId) {
        this.personObjId = personObjId;
    }

    public String getSmallimg() {
        return smallimg;
    }

    public void setSmallimg(String smallimg) {
        this.smallimg = smallimg;
    }

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


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
