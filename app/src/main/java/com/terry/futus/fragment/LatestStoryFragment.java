package com.terry.futus.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.terry.futus.BaseFragment;
import com.terry.futus.Constans;
import com.terry.futus.R;
import com.terry.futus.activity.StoryDetailActivity;
import com.terry.futus.adapter.StoryContentAdapter;
import com.terry.futus.bean.StoryBean;
import com.terry.futus.util.NetUtil;
import com.terry.futus.util.ToastAlone;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lmz_cxm on 2015/12/2.
 */
public class LatestStoryFragment extends BaseFragment {

    private View mRootView;

    private PullToRefreshListView story_pull_list;
    private boolean isLastPage;
    private int mLatestStart = 1;
    private StoryContentAdapter mStoryAdapter;
    private ListView mStoryList;
    private int mStartNum = 1;
    private String mLatestBaseUrl = "http://www.qbaobei.com/jiaoyu/tj/tjgs/List_" + mLatestStart
            + ".html";
    private String mTestJsonUrl = "http://dynamic.qbaobei.com/dynamic.php?s=/qbaobeimobile/cate_list/cate/207/star/" + mStartNum
            + "&callback=jQuery11110999141864092687_1453174495543&_=1453174495548";
    private ProgressBar progressbar;
    private final static int STORY_REQUEST_CODE = 10;

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
//        getStoryData();
        getTestJson();
    }

    private String getJsonUrl(){
        return "http://dynamic.qbaobei.com/dynamic.php?s=/qbaobeimobile/cate_list/cate/207/star/" + mStartNum
                + "&callback=jQuery11110999141864092687_1453174495543&_=1453174495548";
    };

    private void getTestJson() {

        RequestParams params = new RequestParams(getJsonUrl());
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String s) {
                analyzeJson(s);
            }

            @Override
            public void onError(Throwable throwable, boolean b) {

            }

            @Override
            public void onCancelled(CancelledException e) {

            }

            @Override
            public void onFinished() {
                story_pull_list.onRefreshComplete();
                progressbar.setVisibility(View.GONE);
            }
        });
    }

    //进行json解析
    private void analyzeJson(String json) {
        if (!TextUtils.isEmpty(json)) {
            String[] cir_arr_one = json.split("\\(");
            String cir_one = cir_arr_one[1];
            String[] cir_arr_two = cir_one.split("\\)");
            String cir_two = cir_arr_two[0];
            if ("null".equals(cir_two)) {
                //标明 已经全部加载完
                //证明已经到了最后一页
                isLastPage = true;
                ToastAlone.show(R.string.load_all_data);
                Log.i("cxm", "the last one");
                return;
            }else {
                Gson gson = new Gson();
                List<StoryBean> storyBeans = gson.fromJson(cir_two, new TypeToken<List<StoryBean>>() {
                }.getType());
                if (storyBeans != null) {
                    if (mStartNum == 20) {
                        mStoryAdapter.setData(storyBeans);
                    } else {
                        mStoryAdapter.addData(storyBeans);
                    }
                }
            }
        }
    }


    private void getStoryData() {
        new AsyTask().execute(mLatestBaseUrl);
    }


    @Override
    protected void setListener() {
        story_pull_list.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                mStartNum = 20;
//                new AsyTask().execute(getBaseUrl());
                getTestJson();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (isLastPage) {
                    return;
                }
                mStartNum += 10;
//                new AsyTask().execute(getBaseUrl());
                getTestJson();
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
        if (resultCode == Activity.RESULT_OK && requestCode == STORY_REQUEST_CODE) {
            LogUtil.w("Latest---onActivityResult");
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
        return "http://www.qbaobei.com/jiaoyu/tj/tjgs/List_" + mLatestStart
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
            File file = new File(Environment.getExternalStorageDirectory() + "/Latest_qbaobei.txt");
            try {
                if (!file.exists()) {
                    file.createNewFile();
                }
                String str = document.toString();
                FileWriter writer = new FileWriter(file.getAbsolutePath());
                BufferedWriter bufferedWriter = new BufferedWriter(writer);
                bufferedWriter.write(str);
                bufferedWriter.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (document == null) {
                ToastAlone.show(R.string.load_fail_hint);
                return;
            }
            Element page = document.select("div.page").first();
            if (page != null) {
                Elements children = page.children();
                if (children.size() == 1 && "prev".equals(children.first().attr("class"))) {
                    //证明已经到了最后一页
                    isLastPage = true;
                    ToastAlone.show(R.string.load_all_data);
                    Log.i("cxm", "the last one");
                    return;
                }
            }
            progressbar.setVisibility(View.GONE);
            //使用新的爬虫规则
            Elements div_elements = document.select("div.news-list-ul");
            if (div_elements != null) {
                Element div_fir = div_elements.first();
                if (div_fir != null) {
                    Elements div_children = div_fir.children();
                    if (div_children != null) {
                        ArrayList<StoryBean> storyBeans = new ArrayList<StoryBean>();
                        for (Element element : div_children) {
                            StoryBean bean = new StoryBean();
                            Element href_elem = element.select("a[href]").first();
                            String href_str = href_elem.attr("href");
                            Element img_elem = element.select("img[src]").first();
                            String img_str = Constans.defualt_pic;
                            if (img_elem != null) {
                                img_str = img_elem.attr("src");
                            }
                            Element tit_element = element.select("p.tit").first();
                            String tit_str = tit_element.text();
                            LogUtil.v(tit_str + "---" + href_str + "---" + img_str);
                            bean.setTitle(tit_str);
                            bean.setImg(img_str);
                            bean.setUrl(href_str);
                            storyBeans.add(bean);
                        }
                        if (mLatestStart == 1) {
                            mStoryAdapter.setData(storyBeans);
                        } else {
                            mStoryAdapter.addData(storyBeans);
                        }
                    }
                }
            } else {
                //立马启动第二种解析方式
                Elements ul_elements = document.select("ul.index-ul");
                if (ul_elements != null) {
                    Element ul_fir = ul_elements.first();
                    Elements ul_children = ul_fir.children();
                    if (ul_children != null) {
                        ArrayList<StoryBean> storyBeans = new ArrayList<StoryBean>();
                        for (Element child : ul_children) {
                            StoryBean bean = new StoryBean();
                            Element href_elem = child.select("a[href]").first();
                            Element img_elem = child.select("img[src]").first();
                            String title = href_elem.text();
                            String content_url = href_elem.attr("href");
                            String img_url = Constans.defualt_pic;
                            if (img_elem != null) {
                                img_url = img_elem.attr("src");
                            }
                            bean.setTitle(title);
                            bean.setUrl(content_url);
                            bean.setImg(img_url);
                            storyBeans.add(bean);
                        }
                        if (mLatestStart == 1) {
                            mStoryAdapter.setData(storyBeans);
                        } else {
                            mStoryAdapter.addData(storyBeans);
                        }
                    }
                }
            }


            /*Elements elements = document.select("[class]");
            for (Element element : elements) {
                if (element == null) {
                    Log.v("cxm", "null");
                } else {
                    String className = element.className();
                    if ("index-ul".equals(className)) {

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
                            } else {
                                bean.setPicUrl(img.attr("src"));
                            }
                            storyBeans.add(bean);
                        }
                        if (mLatestStart == 1) {
                            mStoryAdapter.setData(storyBeans);
                        } else {
                            mStoryAdapter.addData(storyBeans);
                        }
                    }
                }
            }*/
        }

        @Override
        protected Document doInBackground(String... params) {
            Document document = null;
            try {
                LogUtil.e(params[0]);
                document = Jsoup.connect(params[0]).timeout(9000)
                        .post();
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
