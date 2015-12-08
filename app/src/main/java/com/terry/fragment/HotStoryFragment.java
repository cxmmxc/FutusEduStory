package com.terry.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.terry.BaseFragment;
import com.terry.R;
import com.terry.activity.StoryDetailActivity;
import com.terry.adapter.StoryContentAdapter;
import com.terry.bean.StoryBean;
import com.terry.util.NetUtil;
import com.terry.util.ToastAlone;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xutils.common.util.LogUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by lmz_cxm on 2015/12/2.
 */
public class HotStoryFragment extends BaseFragment {
    private View mRootView;

    private PullToRefreshListView story_pull_list;
    private boolean isLastPage;
    private int mHotStart = 1;
    private StoryContentAdapter mStoryAdapter;
    private ListView mStoryList;
    private String mHotBaseUrl = "http://www.qbaobei.com/hot/jiaoyu/tj/tjgs/List_" + mHotStart
            + ".html";
    private ProgressBar progressbar;
    private final static int STORY_REQUEST_CODE = 11;
    private int mClickItemPosition = -1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.same_story_layout, null);
        }
        return mRootView;
    }

    @Override
    protected void initView() {

        story_pull_list = (PullToRefreshListView) mRootView.findViewById(R.id.story_pull_list);
        mStoryList = story_pull_list.getRefreshableView();
        progressbar = (ProgressBar) mRootView.findViewById(R.id.progressbar);
    }

    @Override
    protected void initData() {

        story_pull_list.setMode(PullToRefreshBase.Mode.BOTH);
        mStoryAdapter = new StoryContentAdapter(mActivity);
        mStoryList.setAdapter(mStoryAdapter);
        //判断是否有网
        if (!NetUtil.isNetworkAvailable(mActivity)) {
            progressbar.setVisibility(View.GONE);
            return;
        }
        getStoryData();
    }


    private void getStoryData() {
        new AsyTask().execute(mHotBaseUrl);
    }

    @Override
    protected void setListener() {
        story_pull_list.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                mHotStart = 1;
                new AsyTask().execute(getBaseUrl());
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (isLastPage) {
                    return;
                }
                mHotStart++;
                new AsyTask().execute(getBaseUrl());
            }
        });

        mStoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mClickItemPosition = position;
                StoryBean storyBean = mStoryAdapter.getItem(position - 1);
                Intent intent = new Intent(mActivity, StoryDetailActivity.class);
                intent.putExtra("storyBean", storyBean);
                startActivityForResult(intent, STORY_REQUEST_CODE);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK && requestCode == STORY_REQUEST_CODE) {
            //如果是已读的话，更新整个数据源
            if (mClickItemPosition != -1) {
                View view = mStoryList.getChildAt(mClickItemPosition - mStoryList.getFirstVisiblePosition());
                TextView txtView = (TextView) view.findViewById(R.id.title_story);
                txtView.setTextColor(Color.GRAY);
            }
        }
    }

    //ishot，如果是true，表示最热；否则表示最新
    private String getBaseUrl() {
        return "http://www.qbaobei.com/hot/jiaoyu/tj/tjgs/List_" + mHotStart
                + ".html";
    }

    class AsyTask extends AsyncTask<String, Void, Document> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Document document) {
            super.onPostExecute(document);
            story_pull_list.onRefreshComplete();

//            File file = new File(Environment.getExternalStorageDirectory() + "/hot.txt");
//
//            try {
//                if(!file.exists()) {
//                    file.createNewFile();
//                }
//                FileWriter writer = new FileWriter(file.getAbsolutePath());
//                BufferedWriter bufferedWriter = new BufferedWriter(writer);
//                bufferedWriter.write(document.toString());
//                bufferedWriter.close();
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }


            Element page = document.select("div.page").first();
            Element next = page.select("a.next").first();
            if (next == null) {
                isLastPage = true;
                ToastAlone.show(R.string.load_all_data);
                Log.i("cxm", "the last one");
                return;
            }
            progressbar.setVisibility(View.GONE);
            Element rulElement = document.select("ul.list-conBox-ul").first();
            Elements docuElements = rulElement.children();
            ArrayList<StoryBean> storyBeans = new ArrayList<StoryBean>();
            for (Element element : docuElements) {
                StoryBean bean = new StoryBean();
                if (element == null) {
                    Log.v("cxm", "null");
                } else {
                    Element href_element = element.select("[href]").first();

                    LogUtil.w(href_element.attr("href"));

                    Element img_element = href_element.select("img[src]").first();
                    LogUtil.i(img_element.attr("src") + "/n"
                                    + img_element.attr("alt")
                    );
                    bean.setmContentUrl(href_element.attr("href"));
                    bean.setTitle(img_element.attr("alt"));
                    String picUrl = img_element.attr("src");
                    if (picUrl.startsWith("http")) {
                        //证明是有图片的，完整图片
                        bean.setPicUrl(picUrl);
                    } else {
                        bean.setPicUrl("http://www.qbaobei.com" + picUrl);
                    }
                    storyBeans.add(bean);
                }
            }
            if (mHotStart == 1) {
                mStoryAdapter.setData(storyBeans);
            } else {
                mStoryAdapter.addData(storyBeans);
            }
        }

        @Override
        protected Document doInBackground(String... params) {
            Document document = null;
//            LogUtil.i(params[0]);
            try {
                document = Jsoup.connect(params[0]).timeout(15000)
                        .get();
//                Elements elements = document.getElementsByClass("ulTextlist_2 clear");


            } catch (IOException e) {
                e.printStackTrace();
            }
            if (document != null) {
                return document;
            }
            return null;
        }
    }


}
