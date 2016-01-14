package com.terry.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.File;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * 作者：Terry.Chen on 2015/12/151445.
 * 邮箱：herewinner@163.com
 * 描述：MpThree
 */
@Table(name="MpThree")
public class MpThree extends BmobObject {
    @Column(name = "id", isId = true)
    private int id;
    @Column(name="mp3_name")
    private String mp3_name;
    @Column(name="mp3_url")
    private String mp3_url;
    @Column(name="mp3_file_url")
    private String mp3_file_url;
    @Column(name="isCollect")
    private int isCollect;
    @Column(name="isReaded")
    private int isReaded;
    @Column(name="personObjId")
    private String personObjId;

    public String getPersonObjId() {
        return personObjId;
    }

    public void setPersonObjId(String personObjId) {
        this.personObjId = personObjId;
    }

    public int getIsCollect() {
        return isCollect;
    }

    public void setIsCollect(int isCollect) {
        this.isCollect = isCollect;
    }

    public int getIsReaded() {
        return isReaded;
    }

    public void setIsReaded(int isReaded) {
        this.isReaded = isReaded;
    }

    public String getMp3_file_url() {
        return mp3_file_url;
    }

    public void setMp3_file_url(String mp3_file_url) {
        this.mp3_file_url = mp3_file_url;
    }

    public String getMp3_name() {
        return mp3_name;
    }

    public void setMp3_name(String mp3_name) {
        this.mp3_name = mp3_name;
    }

    public String getMp3_url() {
        return mp3_url;
    }

    public void setMp3_url(String mp3_url) {
        this.mp3_url = mp3_url;
    }
}
