package cn.bravedawn.latte.delegates.web;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import cn.bravedawn.latte.delegates.web.chromeClient.WebChromeClientImpl;
import cn.bravedawn.latte.delegates.web.client.WebViewClientImpl;
import cn.bravedawn.latte.delegates.web.route.RouteKeys;
import cn.bravedawn.latte.delegates.web.route.Router;

/**
 * Created by 冯晓 on 2017/9/26.
 */

public class WebDelegateImpl extends WebDelegate{

    private IPageLoadListener mPageLoadListener = null;

    public static WebDelegateImpl create(String url){
        final Bundle args = new Bundle();
        args.putString(RouteKeys.URL.name(), url);
        final WebDelegateImpl delegate = new WebDelegateImpl();
        delegate.setArguments(args);
        return delegate;
    }


    public void setPageLoadListener(IPageLoadListener listener){
        this.mPageLoadListener = listener;
    }

    @Override
    public Object setLayout() {
        return getWebView();
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
        if (getUrl() != null){
            // 用原生的方式模拟web跳转
            Router.getInstance().loadPage(this, getUrl());
        }
    }

    @Override
    public IWebViewInitializer setInitializer() {
        return this;
    }

    @Override
    public WebView initWebView(WebView webView) {
        return new WebViewInitializer().createWebView(webView);
    }

    @Override
    public WebViewClient initWebViewClient() {
        final WebViewClientImpl webViewClient = new WebViewClientImpl(this);
        webViewClient.setPageLoadListener(mPageLoadListener);
        return webViewClient;
    }

    @Override
    public WebChromeClient initWebChromeClient() {
        return new WebChromeClientImpl();
    }
}
