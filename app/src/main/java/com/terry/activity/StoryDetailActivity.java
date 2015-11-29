package com.terry.activity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.terry.BaseActivity;
import com.terry.R;

/**
 * Created by lmz_cxm on 2015/11/29.
 */
public class StoryDetailActivity extends BaseActivity {
    private WebView mWebView;
    private String mUrl;
    private String title;
    private Toolbar toolbar;
    private ProgressDialog progressDialog;
    @Override
    protected void initView() {
        setContentView(R.layout.story_detail_layout);
        mWebView = (WebView) findViewById(R.id.mWebView);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
    }

    @Override
    protected void initData() {
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("加载中");
        mUrl = getIntent().getStringExtra("url");
        title = getIntent().getStringExtra("title");
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl(mUrl);
        toolbar.setTitle(title);
    }

    @Override
    protected void setListener() {
        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                return super.shouldOverrideUrlLoading(view, url);
                return false;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressDialog.show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressDialog.dismiss();
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
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
