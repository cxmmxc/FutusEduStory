package com.terry.futus.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 作者：Terry.Chen on 2015/12/81056.
 * 邮箱：herewinner@163.com
 * 描述：数据存储
 */
public class SpUtil {
    private static SpUtil spUtil;
    private static SharedPreferences.Editor mEditor;
    private static SharedPreferences spInfo;
    private static String NAME = "futus";
    private static Context mContext;

    public static SpUtil getInstance(Context context) {
        mContext = context;
        if (spInfo == null && mContext != null) {
            spInfo = mContext.getSharedPreferences(NAME, Context.MODE_PRIVATE);
            mEditor = spInfo.edit();
            spUtil = new SpUtil();
        }
        return spUtil;
    }

    private SpUtil() {}

    private final static String PERSON_OBJID = "person_objid";
    private final static String PERSON_NAME = "person_name";
    private final static String PERSON_HEAD = "person_head";
    private final static String PERSON_EMAIL = "person_email";
    private final static String PERSON_PHONE = "person_phone";

    //存储获取Personid
    public void setPersonObjId(String objId) {
        mEditor.putString(PERSON_OBJID, objId);
        mEditor.commit();
    }

    public String getPersonObjid() {
        return spInfo.getString(PERSON_OBJID, "");
    }

    //存储获取PersonName
    public void setPersonName(String objName) {
        mEditor.putString(PERSON_NAME, objName);
        mEditor.commit();
    }

    public String getPersonName() {
        return spInfo.getString(PERSON_NAME, "");
    }

    //存储获取PersonHeadUrl
    public void setPersonHead(String headUrl) {
        mEditor.putString(PERSON_HEAD, headUrl);
        mEditor.commit();
    }

    public String getPersonHead() {
        return spInfo.getString(PERSON_HEAD, "");
    }

    //存储获取PersonEmail
    public void setPersonEmail(String email) {
        mEditor.putString(PERSON_EMAIL, email);
        mEditor.commit();
    }

    public String getPersonEmail() {
        return spInfo.getString(PERSON_EMAIL, "");
    }


    //存储获取PersonPhone
    public void setPersonPhone(String phone) {
        mEditor.putString(PERSON_PHONE, phone);
        mEditor.commit();
    }

    public String getPersonPhone() {
        return spInfo.getString(PERSON_PHONE, "");
    }



}
