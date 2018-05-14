package com.example.pgg.qboxdemo.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Message;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AbsoluteLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.pgg.qboxdemo.R;
import com.example.pgg.qboxdemo.utils.MxxNetworkUtil;

/**
 * Created by pgg on 2018/5/14.
 */

public class Html5WebView extends WebView {

    private ProgressBar mProgressBar;

    private Context mContext;
    private WebsiteChangeListener mWebsiteChangeListener;

    public Html5WebView(Context context) {
        this(context,null);
    }

    public Html5WebView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public Html5WebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext=context;
        initView();
    }

    private void initView() {
        //===========顶部进度条初始化================
        mProgressBar=new ProgressBar(mContext,null,android.R.attr.progressBarStyleHorizontal);
        LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                8);
        mProgressBar.setLayoutParams(layoutParams);

        Drawable drawable=mContext.getResources().getDrawable(R.drawable.web_progress_bar_states);
        mProgressBar.setProgressDrawable(drawable);
        addView(mProgressBar);
        //===========顶部进度条初始化================

        WebSettings mWebSettings=getSettings();
        mWebSettings.setSupportZoom(true);
        mWebSettings.setLoadWithOverviewMode(true);
        mWebSettings.setUseWideViewPort(true);
        mWebSettings.setDefaultTextEncodingName("utf-8");
        mWebSettings.setLoadsImagesAutomatically(true);

        //调用JS方法，android版本大于17，加上注解@JavaScriptInterface
        mWebSettings.setJavaScriptEnabled(true);
        mWebSettings.setJavaScriptEnabled(true);
        mWebSettings.setSupportMultipleWindows(true);
        //缓存数据
        saveData(mWebSettings);
        newWin(mWebSettings);
        setWebChromeClient(webChromeClient);
        setWebViewClient(webViewClient);//添加去除广告的功能
    }


    /**
     * 多窗口问题
     * @param mWebSettings
     */
    private void newWin(WebSettings mWebSettings) {
        //html中的_bank标签就是新建窗口打开，有时会打不开，需要加以下
        //然后 复写 WebChromeClient的onCreateWindow方法
        mWebSettings.setSupportMultipleWindows(true);
        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(true);
    }

    /**
     * HTML5数据存储
     * @param mWebSettings
     */
    private void saveData(WebSettings mWebSettings) {
        //有时网页需要自己保存一些关键数据，Android webView需要自己设置
        if (MxxNetworkUtil.isNetworkAvailable(mContext)){
            //根据cache-control决定是否从网络上取数据
            mWebSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        }else {
            //没网，即从本地获取，离线加载
            mWebSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }

        mWebSettings.setDomStorageEnabled(true);
        mWebSettings.setDatabaseEnabled(true);
        mWebSettings.setAppCacheEnabled(true);
        String appCachePath=mContext.getCacheDir().getAbsolutePath();
        mWebSettings.setAppCachePath(appCachePath);
    }

    WebViewClient webViewClient=new WebViewClient(){
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
            int lastLen=url.length()>40?40:url.length();
            String adUrl=url.substring(0,lastLen).toLowerCase();
            if (!adUrl.contains("eastday.com/toutiaoh5")){
                return super.shouldInterceptRequest(view, url);
            }else {
                return new WebResourceResponse(null,null,null);
            }

        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
                String url=request.getUrl().toString().toLowerCase();
                if (!url.contains("eastday.com/toutiaoh5")){
                    return super.shouldInterceptRequest(view,request);
                }else{
                    return new WebResourceResponse(null,null,null);
                }
            }else {
                return super.shouldInterceptRequest(view, request);
            }
        }

        /**
         * 多页面在同一个WebView中打开，就是不新建activity或者调用系统浏览器打开
         */
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            if (mWebsiteChangeListener!=null){
                mWebsiteChangeListener.onUrlChange(url);
            }
            return true;
        }
    };

    WebChromeClient webChromeClient=new WebChromeClient(){
        @Override
        public void onReceivedIcon(WebView view, Bitmap icon) {
            super.onReceivedIcon(view, icon);
        }

        @Override
        public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
            callback.invoke(origin,true,false);//注意这个函数，第二个参数就是是否同意定位权限，第三个是是否希望内核记住
            super.onGeolocationPermissionsShowPrompt(origin, callback);
        }

        @Override
        public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
            HitTestResult result=view.getHitTestResult();
            String data=result.getExtra();
            view.loadUrl(data);
            return true;
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress==100){
                mProgressBar.setVisibility(GONE);
            }else {
                if (mProgressBar.getVisibility()==GONE){
                    mProgressBar.setVisibility(VISIBLE);
                    mProgressBar.setProgress(newProgress);
                }
            }
            super.onProgressChanged(view, newProgress);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            if (mWebsiteChangeListener!=null){
                mWebsiteChangeListener.onWebsiteChange(title);
            }
        }
    };

    public interface WebsiteChangeListener {
        void onWebsiteChange(String title);

        void onUrlChange(String url);
    }

    public void setmWebsiteChangeListener(WebsiteChangeListener listener){
        this.mWebsiteChangeListener=listener;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        AbsoluteLayout.LayoutParams lp= (LayoutParams) mProgressBar.getLayoutParams();
        lp.x=l;
        lp.y=t;
        mProgressBar.setLayoutParams(lp);
        super.onScrollChanged(l, t, oldl, oldt);
    }
}
