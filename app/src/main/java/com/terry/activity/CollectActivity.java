package com.terry.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.terry.BaseActivity;
import com.terry.Constans;
import com.terry.R;
import com.terry.adapter.StoryContentAdapter;
import com.terry.bean.StoryBean;
import com.terry.util.ToastAlone;

import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.HttpTask;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;

/**
 * 作者：Terry.Chen on 2015/12/141032.
 * 邮箱：herewinner@163.com
 * 描述：收藏页面
 */
public class CollectActivity extends BaseActivity {
    private ImageView back_img;
    private ProgressBar progressbar;
    private PullToRefreshListView collect_pull_list;
    private int mTotoalCount;
    private StoryContentAdapter mScAdapter;
    private ListView mCollectListview;

    @Override
    protected void initView() {
        setContentView(R.layout.collection_layout);
        back_img = (ImageView) findViewById(R.id.back_img);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
        collect_pull_list = (PullToRefreshListView) findViewById(R.id.collect_pull_list);
        mCollectListview = collect_pull_list.getRefreshableView();
    }

    @Override
    protected void initData() {
        collect_pull_list.setMode(PullToRefreshBase.Mode.BOTH);
        mScAdapter = new StoryContentAdapter(mContext);
        mCollectListview.setAdapter(mScAdapter);

//        BmobQuery<StoryBean> query = new BmobQuery<StoryBean>();
//        query.count(mContext, StoryBean.class, new CountListener() {
//            @Override
//            public void onSuccess(int i) {
//                mTotoalCount = i;
//                LogUtil.i(i+"");
//                getMobData(0);
//            }
//
//            @Override
//            public void onFailure(int i, String s) {
//                ToastAlone.show(R.string.fail_count_data);
//                progressbar.setVisibility(View.GONE);
//            }
//        });
        getCloudData();
    }

    private void getCloudData() {
        RequestParams params = new RequestParams(Constans.BaseUrl+"getCollectData");
        params.addBodyParameter("personObjId", spUtil.getPersonObjid());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String s) {
                LogUtil.v(s);
            }

            @Override
            public void onError(Throwable throwable, boolean b) {
                LogUtil.v(throwable.toString());
            }

            @Override
            public void onCancelled(CancelledException e) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void getMobData(final int skip) {
        //从服务器获取数据
        BmobQuery<StoryBean> query = new BmobQuery<StoryBean>();
        query.addWhereEqualTo("personObjId", spUtil.getPersonObjid());
        query.setLimit(50);//一次查询限制10条
//        query.setSkip(skip);
        query.findObjects(mContext, new FindListener<StoryBean>() {
            @Override
            public void onSuccess(List<StoryBean> list) {
                progressbar.setVisibility(View.GONE);
                collect_pull_list.onRefreshComplete();
                LogUtil.v(list.toString());
                if (skip == 0) {
                    mScAdapter.setData(list);
                } else {
                    mScAdapter.addData(list);
                }
            }

            @Override
            public void onError(int i, String s) {
                collect_pull_list.onRefreshComplete();
                ToastAlone.show(s);
                progressbar.setVisibility(View.GONE);
            }
        });
    }

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    collect_pull_list.onRefreshComplete();
                    ToastAlone.show(R.string.load_all);
                    break;
            }
        }
    };

    @Override
    protected void setListener() {
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        collect_pull_list.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                getMobData(0);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (mScAdapter.getCount() >= mTotoalCount) {
                    mHandler.sendEmptyMessageDelayed(1, 1000);
                    return;
                }
                getMobData(mScAdapter.getCount());
            }
        });

        mCollectListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                StoryBean storyBean = mScAdapter.getItem(position - 1);
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
