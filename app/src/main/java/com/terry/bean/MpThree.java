package com.terry.bean;

import java.io.File;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * 作者：Terry.Chen on 2015/12/151445.
 * 邮箱：herewinner@163.com
 * 描述：MpThree
 */
public class MpThree extends BmobObject {
    private String mp3_name;
    private String mp3_url;
    private String mp3_file_url;

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
