package com.terry.activity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.terry.BaseActivity;
import com.terry.R;
import com.terry.bean.StoryBean;
import com.terry.util.MeasureTool;
import com.terry.util.NetUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

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

    @Override
    protected void initView() {
        setContentView(R.layout.story_detail_layout);
        mWebView = (WebView) findViewById(R.id.mWebView);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
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

        mStyle = "<head><style>img{width:"+screenWH[0]/3+"px !important;}</style></head>";

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
//判断是否有网
        if (!NetUtil.isNetworkAvailable(this)) {
            progressbar.setVisibility(View.GONE);
            return;
        }
//        mWebView.loadUrl(mUrl);
        toolbar.setTitle(title);
        new UrlTask().execute();
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
                Log.v("cxm", first.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (first != null) {
                Log.v("cxm", "doc != null");
                return first.html();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressbar.setVisibility(View.GONE);
            mStoryBean.setContent(s);
            //并且默认存入到本地数据库
//            progressDialog.dismiss();
//            text11.setText(s);
//            Log.w("cxm", s);
            mWebView.loadDataWithBaseURL("http://www.qbaobei.com", mStyle+s, "text/html", "utf-8", null);
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
                finish();
            }
        });
    }

    @Override
    protected void initToolbar() {
        super.initToolbar(toolbar);
    }
}
