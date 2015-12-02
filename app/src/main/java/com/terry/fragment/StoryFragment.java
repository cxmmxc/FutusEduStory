package com.terry.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.terry.BaseFragment;
import com.terry.R;
import com.terry.activity.StoryDetailActivity;
import com.terry.adapter.StoryContentAdapter;
import com.terry.bean.StoryBean;
import com.terry.util.NetUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

/**
 * 作者：Terry.Chen on 2015/11/271639.
 * 邮箱：herewinner@163.com
 * 描述：小故事的页面
 */
public class StoryFragment extends BaseFragment {

    private View mRootView;
    private PullToRefreshListView story_pull_list;
    private RadioGroup radio_group;
    private RadioButton latest_btn, hot_btn;
    private boolean isLastPage;
    private int mLatestStart = 1;
    private int mHotStart = 1;
    private StoryContentAdapter mStoryAdapter;
    private ListView mStoryList;
    private String mLatestBaseUrl = "http://www.qbaobei.com/jiaoyu/tj/tjgs/List_" + mLatestStart + ".html";
    private String mHotBaseUrl = "http://www.qbaobei.com/hot/jiaoyu/tj/tjgs/List_" + mHotStart + ".html";
    private ProgressBar progressbar;
    private boolean isHotPage;//是否是最热故事,默认是最新故事

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        if(mRootView == null) {
            mRootView = inflater.inflate(R.layout.story_frag_layout, null);
        }
        return mRootView;
    }


    @Override
    protected void initView() {
        story_pull_list = (PullToRefreshListView) mRootView.findViewById(R.id.story_pull_list);
        mStoryList = story_pull_list.getRefreshableView();
        progressbar = (ProgressBar) mRootView.findViewById(R.id.progressbar);
        radio_group = (RadioGroup) mRootView.findViewById(R.id.radio_group);
        latest_btn = (RadioButton) mRootView.findViewById(R.id.latest_btn);
        hot_btn = (RadioButton) mRootView.findViewById(R.id.hot_btn);
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
        getStoryData(false);
    }


    private void getStoryData(boolean isHot) {
        String url = isHot ? mHotBaseUrl : mLatestBaseUrl;
        new AsyTask().execute(url);
    }

    @Override
    protected void setListener() {
        story_pull_list.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (!isHotPage) {
                    mLatestStart = 1;
                } else {
                    mHotStart = 1;
                }
                new AsyTask().execute(getBaseUrl(isHotPage));
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (!isHotPage) {
                    mLatestStart++;
                } else {
                    mHotStart ++;
                }
                new AsyTask().execute(getBaseUrl(isHotPage));
            }
        });

        mStoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                StoryBean storyBean = mStoryAdapter.getItem(position - 1);
                Intent intent = new Intent(mActivity, StoryDetailActivity.class);
                intent.putExtra("storyBean", storyBean);
                startActivity(intent);
            }
        });

        radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.latest_btn:
                        mLatestStart = 1;
                        isHotPage = false;
                        getStoryData(isHotPage);
                        break;
                    case R.id.hot_btn:
                        mHotStart = 1;
                        isHotPage = true;
                        getStoryData(isHotPage);
                        break;
                }
            }
        });
    }

    //ishot，如果是true，表示最热；否则表示最新
    private String getBaseUrl(boolean isHot) {
        return isHot ? mHotBaseUrl : mLatestBaseUrl;
//        if(!isHot) {
//            return mLatestBaseUrl;
//        }else {
//            return mHotBaseUrl;
//        }
//        int page = isHot ? mHotStart : mLatestStart;
//        return "http://www.qbaobei.com/jiaoyu/tj/tjgs/List_" + page + ".html";
    }

    class AsyTask extends AsyncTask<String, Void, Document> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Document document) {
            super.onPostExecute(document);
            story_pull_list.onRefreshComplete();
            Element page = document.select("div.page").first();
            Elements children = page.children();
            if(children.size() == 1 && "prev".equals(children.first().attr("class"))) {
                Log.i("cxm", "the last one");
            }
            progressbar.setVisibility(View.GONE);
            Elements elements = document.select("[class]");
            for(Element element : elements){
                if(element == null){
                    Log.v("cxm", "null");
                }else {
                    String className = element.className();
                    if("index-ul".equals(className)) {

                        Elements elements1 = element.select("li");
                        Log.v("cxm", "size=" + elements1.size());
                        ArrayList<StoryBean> storyBeans = new ArrayList<StoryBean>();
                        for (Element child : elements1) {
                            StoryBean bean = new StoryBean();
                            Element href = child.select("[href]").first();
                            String name = href.text();
                            Element img = child.select("img[src]").first();
                            Log.w("cxm", "href=" + href.attr("href") + " ~~ name=" + name + "" +
                                    " ~~ img=");
                            bean.setTitle(name);
                            bean.setmContentUrl(href.attr("href"));
                            if (null == img) {
                                Log.e("cxm", "img = null");
                                bean.setPicUrl("");
                            }else {
                                bean.setPicUrl(img.attr("src"));
                            }
                            storyBeans.add(bean);
                        }
                        if (!isHotPage) {
                            if(mLatestStart == 1) {
                                mStoryAdapter.setData(storyBeans);
                            } else {
                                mStoryAdapter.addData(storyBeans);
                            }
                        }else {
                            if(mHotStart == 1) {
                                mStoryAdapter.setData(storyBeans);
                            } else {
                                mStoryAdapter.addData(storyBeans);
                            }
                        }
                    }
                }
            }
        }

        @Override
        protected Document doInBackground(String... params) {
            Document document = null;
            try {
                document = Jsoup.connect(params[0]).timeout(9000)
                        .post();
//                Elements elements = document.getElementsByClass("ulTextlist_2 clear");


            } catch (IOException e) {
                e.printStackTrace();
            }
            if(document != null) {
                return document;
            }
            return null;
        }
    }




}
