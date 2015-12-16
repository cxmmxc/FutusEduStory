package com.terry.activity;

import android.os.Environment;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bmob.BmobProFile;
import com.terry.BaseActivity;
import com.terry.R;
import com.terry.bean.MpThree;
import com.terry.util.ToastAlone;

import org.xutils.common.util.LogUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadBatchListener;

/**
 * 作者：Terry.Chen on 2015/12/151517.
 * 邮箱：herewinner@163.com
 * 描述：上传音乐文件
 */
public class MusicUploadActivity extends BaseActivity {
    private TextView read_text;
    private String[] filepaths;
    private String dir_path = Environment.getExternalStorageDirectory() + "/Futus_music/";
    private List<BmobObject> mpThrees;
    private EditText edit_num;
    private int one_total;
    @Override
    protected void initView() {
        setContentView(R.layout.upload_music_layout);
        read_text = (TextView) findViewById(R.id.read_text);
        edit_num = (EditText) findViewById(R.id.edit_num);
    }

    public void uploadClick(View view) {
        int currentIndex;
        /*BmobProFile.getInstance(mContext).uploadBatch(filepaths, new com.bmob.btp.callback.UploadBatchListener() {
            @Override
            public void onSuccess(boolean isFinish,String[] fileNames,String[] urls,BmobFile[] files) {
                LogUtil.v("上传成功");
                //上传成功，开始建表
                LogUtil.v("isFinish="+isFinish+"files.length="+files.length+",one_total="+one_total);
                if (isFinish) {
                    LogUtil.e(one_total+"到了");
                    int file_lenght = files.length;
                    for (int i = 0; i < file_lenght; i++) {
                        BmobFile bmobFile = files[i];
                        LogUtil.i(bmobFile.toString());
                        MpThree mpThree = new MpThree();
                        mpThree.setMp3_file_url(bmobFile.getFileUrl(mContext));
                        mpThree.setMp3_name(bmobFile.getFilename());
                        mpThree.setMp3_url(bmobFile.getUrl());
                        mpThrees.add(mpThree);
                    }
                    one_total = 0;
                    filepaths = null;
                }
            }

            @Override
            public void onProgress(int curIndex, int curPercent, int total, int totalPercent) {
                read_text.setText("当前为第" + curIndex + "个文件，上传进度=" + curPercent + "。\n总上传数为" + total + ",百分比为：" + totalPercent);
                LogUtil.i("当前为第" + curIndex + "个文件，上传进度=" + curPercent + "。\n总上传数为" + total + ",百分比为：" + totalPercent);
            }

            @Override
            public void onError(int statuscode, String errormsg) {
                one_total = 0;
                filepaths = null;
                LogUtil.v("上传失败"+errormsg+",statuscode="+statuscode);
            }
        });*/



        //旧版开始上传
        Bmob.uploadBatch(mContext, filepaths, new UploadBatchListener() {
            @Override
            public void onSuccess(List<BmobFile> list, List<String> list1) {
                LogUtil.v("上传成功");
                //上传成功，开始建表
                LogUtil.v("shangchuan_size="+list.size()+",one_total="+one_total);
                if (list.size() == one_total) {
                    LogUtil.e(one_total+"到了");
                    for (BmobFile bmobFile : list) {
                        LogUtil.i(bmobFile.toString());
                        MpThree mpThree = new MpThree();
                        mpThree.setMp3_file_url(bmobFile.getFileUrl(mContext));
                        mpThree.setMp3_name(bmobFile.getFilename());
                        mpThree.setMp3_url(bmobFile.getUrl());
                        mpThrees.add(mpThree);
                    }
                }
                //成功了，则开始上传建mp3表
//                createMp3Table();
            }

            @Override
            public void onProgress(int curIndex, int curPercent, int total, int totalPercent) {
                read_text.setText("当前为第" + curIndex + "个文件，上传进度=" + curPercent + "。\n总上传数为" + total + ",百分比为：" + totalPercent);
//                LogUtil.i("当前为第" + curIndex + "个文件，上传进度=" + curPercent + "。\n总上传数为" + total + ",百分比为：" + totalPercent);
            }

            @Override
            public void onError(int i, String s) {
                LogUtil.v("上传失败"+s+",err="+i);
            }
        });
    }

    private void createMp3Table() {

    }

    public void readFile(View view) {
        //读取文件
        String num_dir = edit_num.getText().toString();
        File file = new File(dir_path+num_dir);
        File[] listFiles = file.listFiles();
        int length = listFiles.length;
        one_total = length;
        filepaths = new String[length];
        for (int i = 0; i < length; i++) {
            filepaths[i] = listFiles[i].getAbsolutePath();
            read_text.setText("正在读取第" + (i+1) + "个文件");
            if ((i+1) == one_total) {
                ToastAlone.show("已全部读取完毕");
            }
        }
    }

    public void createTable(View view) {
        LogUtil.w(""+mpThrees.size());
        new BmobObject().insertBatch(mContext, mpThrees, new SaveListener() {
            @Override
            public void onSuccess() {
                LogUtil.v("建表成功");
                ToastAlone.show("建表成功");
            }

            @Override
            public void onFailure(int i, String s) {
                LogUtil.v("建表失败" + s+",err="+i);
                ToastAlone.show("建表失败" + s+",err="+i);
            }
        });
    }

    public void clearData(View view) {
        mpThrees.clear();
        one_total = 0;
        filepaths = null;
    }

    @Override
    protected void initData() {
        mpThrees = new ArrayList<BmobObject>();
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void initToolbar() {

    }
}
