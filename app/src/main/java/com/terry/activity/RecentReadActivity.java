package com.terry.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.terry.BaseActivity;
import com.terry.R;
import com.terry.StoryApp;
import com.terry.adapter.StoryContentAdapter;
import com.terry.bean.StoryBean;

import org.xutils.ex.DbException;

import java.util.List;

/**
 * 作者：Terry.Chen on 2015/12/251516.
 * 邮箱：herewinner@163.com
 * 描述：最近阅读页面
 */
public class RecentReadActivity extends BaseActivity {
    private ImageView back_img;
    private ListView recent_list;

    private StoryContentAdapter mScAdapter;
    @Override
    protected void initView() {
        setContentView(R.layout.recent_read_layout);

        back_img = (ImageView) findViewById(R.id.back_img);
        recent_list = (ListView) findViewById(R.id.recent_list);

    }

    @Override
    protected void initData() {
        mScAdapter = new StoryContentAdapter(mContext);
        recent_list.setAdapter(mScAdapter);

        try {
            List<StoryBean> storyBeans = StoryApp.mDbManager.selector(StoryBean.class).findAll();
            if(storyBeans != null){
                mScAdapter.setData(storyBeans);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void setListener() {
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recent_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                StoryBean storyBean = mScAdapter.getItem(position);
                Intent intent = new Intent(mContext, StoryDetailActivity.class);
                intent.putExtra("storyBean", storyBean);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void initToolbar() {

    }
}
