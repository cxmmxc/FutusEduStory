package com.terry;

import android.app.Application;
import android.os.Environment;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.terry.util.ToastAlone;

import org.xutils.DbManager;
import org.xutils.common.util.LogUtil;
import org.xutils.x;

import java.io.File;

import cn.bmob.v3.Bmob;

/**
 * Created by lmz_cxm on 2015/11/28.
 */
public class StoryApp extends Application {
    public static String FUTUS_DIR;
    private final static int DB_VERSION = 1;//默认数据库版本号是1
    public static DbManager mDbManager;
    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG);
        initData();
    }

    private void initData() {
        //初始化Bob
        Bmob.initialize(this, Constans.BmobAppId);
        ToastAlone.init(this);
        LogUtil.customTagPrefix = "cxm";
        FUTUS_DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Futus";
        File futus_file = new File(FUTUS_DIR);
        if (!futus_file.exists()) {
            futus_file.mkdir();
        }
        DbManager.DaoConfig daoConfig = new DbManager.DaoConfig()
                .setDbName("futus.db")
                .setDbDir(futus_file)
                .setDbVersion(DB_VERSION)
                .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                    @Override
                    public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
                        // TODO: ...
                        // db.addColumn(...);
                        // db.dropTable(...);
                        // ...
                    }
                });
        mDbManager = x.getDb(daoConfig);
    }
}
