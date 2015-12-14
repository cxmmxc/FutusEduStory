package com.terry.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.terry.BaseActivity;
import com.terry.R;
import com.terry.StoryApp;
import com.terry.bean.StoryBean;
import com.terry.util.MeasureTool;
import com.terry.util.NetUtil;
import com.terry.util.ToastAlone;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.xutils.common.util.LogUtil;
import org.xutils.ex.DbException;

import java.io.IOException;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by lmz_cxm on 2015/11/29.
 */
public class StoryDetailActivity extends BaseActivity {
    private WebView mWebView;
    private String mUrl;
    private String title;
    private Toolbar toolbar;
    private StoryBean mStoryBean;
//    private ProgressDialog progressDialog;

    private ProgressBar progressbar;
    private TextView text11;
    private WebSettings mWebViewSettings;
    private String mStyle;
    private ImageView collect_img;
    private boolean isCollected;//是否已收藏
    private String mCollectObjId;

    @Override
    protected void initView() {
        setContentView(R.layout.story_detail_layout);
        mWebView = (WebView) findViewById(R.id.mWebView);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
        collect_img = (ImageView) findViewById(R.id.collect_img);
//        text11 = (TextView) findViewById(R.id.text11);
//        text11.setMovementMethod(ScrollingMovementMethod.getInstance());
    }

    @Override
    protected void initData() {
        mStoryBean = (StoryBean) getIntent().getSerializableExtra("storyBean");
//        progressDialog = new ProgressDialog(mContext);
//        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        progressDialog.setMessage("加载中");
        mUrl = mStoryBean.getmContentUrl();
        title = mStoryBean.getTitle();
        int[] screenWH = MeasureTool.getScreenWH(mContext);

        mStyle = "<head><style>img{width:" + screenWH[0] / 3 + "px !important;}</style></head>";

        mWebViewSettings = mWebView.getSettings();
        //让缩放显示的最小值为起始
//        mWebView.setInitialScale(5);
        mWebViewSettings.setJavaScriptEnabled(true);
        // 设置可以支持缩放
//        mWebViewSettings.setSupportZoom(true);
//// 设置出现缩放工具
//        mWebViewSettings.setBuiltInZoomControls(true);
        mWebViewSettings.setDefaultTextEncodingName("utf-8");
//        mWebViewSettings.setUseWideViewPort(true);
//        mWebViewSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//        mWebViewSettings.setLoadWithOverviewMode(true);

//        mWebView.loadUrl(mUrl);
        toolbar.setTitle(title);
        try {
            StoryBean bean = StoryApp.mDbManager.selector(StoryBean.class).where("title", "like", "%" + this.title + "%").findFirst();
            if (bean != null) {
//                LogUtil.v(bean.toString());
                mStoryBean = bean;
                progressbar.setVisibility(View.GONE);
                //并且默认存入到本地数据库
                mWebView.loadDataWithBaseURL("http://www.qbaobei.com", mStyle + bean.getContent(), "text/html", "utf-8", null);

            } else {
//判断是否有网
                if (!NetUtil.isNetworkAvailable(this)) {
                    progressbar.setVisibility(View.GONE);
                    return;
                }
                new UrlTask().execute();
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        if (!TextUtils.isEmpty(spUtil.getPersonObjid())) {
            //已经登录了，就去查询是否已收藏到Bmob
            getMobBeanId();
        }

    }

    private void getMobBeanId() {
        BmobQuery<StoryBean> storyBeanBmobQuery = new BmobQuery<StoryBean>();
        storyBeanBmobQuery.addWhereEqualTo("title", this.title);
        storyBeanBmobQuery.findObjects(this, new FindListener<StoryBean>() {
            @Override
            public void onSuccess(List<StoryBean> list) {
                mCollectObjId = list.get(0).getObjectId();
                //证明已收藏
                collect_img.setBackgroundResource(R.drawable.selector_collected);
                isCollected = true;
            }

            @Override
            public void onError(int i, String s) {
                isCollected = false;
            }
        });
    }

    class UrlTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            progressDialog.show();
        }


        @Override
        protected String doInBackground(String... params) {
            Document document = null;
            Element first = null;
            try {
                document = Jsoup.connect(mUrl).timeout(8000).get();
                first = document.select("div.news-artbody").first();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (first != null) {
                return first.html();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressbar.setVisibility(View.GONE);
            mStoryBean.setContent(s);
            mStoryBean.setIsRead(1);
            //并且默认存入到本地数据库
//            progressDialog.dismiss();
//            text11.setText(s);
//            Log.w("cxm", s);
            mWebView.loadDataWithBaseURL("http://www.qbaobei.com", mStyle + s, "text/html", "utf-8", null);
            try {
                StoryBean bean = StoryApp.mDbManager.selector(StoryBean.class).where("title", "like", "%" + StoryDetailActivity.this.title + "%").findFirst();
                if (bean == null) {
                    LogUtil.i(mStoryBean.getContent());
                    StoryApp.mDbManager.save(mStoryBean);
                }
            } catch (DbException e) {
                e.printStackTrace();
            }
//            mWebView.loadData(s, "text/html", "UTF-8");
        }
    }

    @Override
    protected void setListener() {
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                return super.shouldOverrideUrlLoading(view, url);
                return false;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request,
                                        WebResourceError error) {
                super.onReceivedError(view, request, error);
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(Activity.RESULT_OK);
                finish();
            }
        });

        collect_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCollected) {
                    //先查询此条收藏的id
                    getMobBeanId();
                    //取消收藏
                    mStoryBean.setObjectId(mCollectObjId);
                    mStoryBean.delete(mContext, new DeleteListener() {
                        @Override
                        public void onSuccess() {
                            isCollected = false;
                            ToastAlone.show(R.string.collect_cancel);
                            collect_img.setBackgroundResource(R.drawable.selector_collection);
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            isCollected = true;
                            LogUtil.v(s);
                            ToastAlone.show(R.string.collect_cancel_fail);
                        }
                    });
                } else {
                    //收藏
                    if (TextUtils.isEmpty(spUtil.getPersonObjid())) {
                        //去登录页面进行登录
                        Intent intent = new Intent(mContext, LoginActivity.class);
                        startActivity(intent);
                    } else {
                        //直接就收藏了
                        mStoryBean.setIsCollect(1);
                        mStoryBean.setPersonObjId(spUtil.getPersonObjid());
                        mStoryBean.save(mContext, new SaveListener() {
                            @Override
                            public void onSuccess() {
                                //收藏成功
                                isCollected = true;
                                ToastAlone.show(R.string.collect_success);
                                collect_img.setBackgroundResource(R.drawable.selector_collected);
                            }

                            @Override
                            public void onFailure(int i, String s) {
                                LogUtil.v(s);
                                //收藏成功
                                isCollected = false;
                                ToastAlone.show(R.string.collect_fail);
                            }
                        });
                    }


                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_OK);
        finish();
    }

    @Override
    protected void initToolbar() {
        super.initToolbar(toolbar);
    }
}
