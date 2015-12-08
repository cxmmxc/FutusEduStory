package com.terry.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * 作者：Terry.Chen on 2015/12/81026.
 * 邮箱：herewinner@163.com
 * 描述：Person类，同步到Bmob服务器
 */
public class Person extends BmobObject {
    private String name;
    private String pass;
    private BmobFile headPic;
    private String email;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public BmobFile getHeadPic() {
        return headPic;
    }

    public void setHeadPic(BmobFile headPic) {
        this.headPic = headPic;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
