package com.terry.futus;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.terry.futus.util.ToastAlone;

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

    /**
     * exitAcivity
     */
    public static void exit(final Activity activity) {
        // 判断SDK版本
        int sdkVersion = Integer.valueOf(android.os.Build.VERSION.SDK);
        // 如果是2.2返回sdkVersion =8
        //清除文件夹内的缓存文件

        // 加判断
        ActivityManager manager = (ActivityManager) activity
                .getSystemService(ACTIVITY_SERVICE);

        if (sdkVersion < 8) {
            manager.restartPackage(activity.getPackageName());
            System.exit(0);
        } else {
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(startMain);
            System.exit(0);
        }

    }
}
