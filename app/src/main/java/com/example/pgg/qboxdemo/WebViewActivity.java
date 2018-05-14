package com.example.pgg.qboxdemo;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.FrameLayout;

import com.example.pgg.qboxdemo.base.BaseCommonActivity;
import com.example.pgg.qboxdemo.widget.Html5WebView;
import com.example.pgg.qboxdemo.widget.slidinglayout.SlidingLayout;

import butterknife.BindView;

/**
 * Created by pgg on 2018/5/7.
 */

public class WebViewActivity extends BaseCommonActivity {

    @BindView(R.id.toolbar_webpage)
    Toolbar mToolbarWebpage;

    private String mUrl;
    private String mTitle;

    private FrameLayout mLayout;
    private WebView mWebView;

    @Override
    public void initContentView() {
        setContentView(R.layout.activity_web_page);
    }

    @Override
    public void initView() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mUrl = bundle.getString("url");
            mTitle = bundle.getString("title");
        }
        if (TextUtils.isEmpty(mUrl)) {
            finish();
        }
        initToolBar();

        mLayout = (SlidingLayout) findViewById(R.id.web_layout);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mWebView = new Html5WebView(getApplicationContext());
        mWebView.setLayoutParams(params);
        mLayout.addView(mWebView);

        mWebView.loadUrl(mUrl);
    }

    private void initToolBar() {
        mToolbarWebpage.setTitle(TextUtils.isEmpty(mTitle) ? "" : mTitle);
        setSupportActionBar(mToolbarWebpage);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (android.R.id.home==item.getItemId()){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private long mOldTime;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK){
            if (System.currentTimeMillis()-mOldTime<1500){
                mWebView.clearHistory();
                mWebView.loadUrl(mUrl);
            }else if (mWebView.canGoBack()){
                mWebView.goBack();
            }else {
                WebViewActivity.this.finish();
            }
            mOldTime=System.currentTimeMillis();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        if (mWebView!=null){
            mWebView.loadDataWithBaseURL(null,"","text/html","utf-8",null);
            mWebView.clearHistory();

            ((ViewGroup)mWebView.getParent()).removeView(mWebView);
            mWebView.destroy();
            mWebView=null;
        }
        super.onDestroy();
    }

    @Override
    public void initPresenter() {

    }
}
