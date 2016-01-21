package com.terry.futus.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

/**
 * 作者：Terry.Chen on 2015/12/81026.
 * 邮箱：herewinner@163.com
 * 描述：Person类，同步到Bmob服务器
 */
public class Person extends BmobUser {
    private String headPicUrl;


    public String getHeadPic() {
        return headPicUrl;
    }

    public void setHeadPic(String headPicUrl) {
        this.headPicUrl = headPicUrl;
    }
}
