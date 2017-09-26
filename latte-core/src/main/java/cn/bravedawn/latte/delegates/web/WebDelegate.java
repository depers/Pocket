package cn.bravedawn.latte.delegates.web;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebView;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

import cn.bravedawn.latte.app.ConfigKeys;
import cn.bravedawn.latte.app.Latte;
import cn.bravedawn.latte.delegates.LatteDelegate;
import cn.bravedawn.latte.delegates.web.route.RouteKeys;

/**
 * Created by 冯晓 on 2017/9/26.
 */

public abstract class WebDelegate extends LatteDelegate implements IWebViewInitializer{

    private WebView mWebView = null;
    private final ReferenceQueue<WebView> WEB_VIEW_QUEUE = new ReferenceQueue<>();
    private String mUrl = null;
    private boolean mIsWebViewAvailable = false;
    private LatteDelegate mTopDelegate = null;

    public WebDelegate() {
    }

    public abstract IWebViewInitializer setInitializer();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle args = getArguments();
        mUrl = args.getString(RouteKeys.URL.name());
        initWebView();
    }

    @SuppressLint("JavascriptInterface")
    private void initWebView(){
        if (mWebView != null){
            mWebView.removeAllViews();
            mWebView.destroy();
        } else{
            final IWebViewInitializer initializer = setInitializer();
            if (initializer != null){
                final WeakReference<WebView> webViewWeakReference = new
                        WeakReference<WebView>(new WebView(getContext()), WEB_VIEW_QUEUE);
                mWebView = webViewWeakReference.get();
                mWebView = initializer.initWebView(mWebView);
                mWebView.setWebViewClient(initializer.initWebViewClient());
                mWebView.setWebChromeClient(initializer.initWebChromeClient());
                final String name = Latte.getConfiguration(ConfigKeys.JAVASCRIPT_INTERFACE);
                mWebView.addJavascriptInterface(LatteWebInterface.create(this), name);
                mIsWebViewAvailable = true;
            } else{
                throw new NullPointerException("Initializer is null");
            }
        }
    }

    public void setTopDelegate(LatteDelegate delegate){
        mTopDelegate = delegate;
    }

    public LatteDelegate getTopDelegate(){
        if (mTopDelegate == null){
            mTopDelegate = this;
        }
        return mTopDelegate;
    }
    public WebView getWebView(){
        if (mWebView == null){
            throw new NullPointerException("webView is null");
        }
        return mIsWebViewAvailable ? mWebView : null;
    }

    public String getUrl(){
        if (mUrl == null){
            throw new NullPointerException("url is null");
        }
        return mUrl;
    }



    @Override
    public void onPause() {
        super.onPause();
        if (mWebView != null){
            mWebView.onPause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mWebView != null){
            mWebView.onResume();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mIsWebViewAvailable = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mWebView.removeAllViews();
        mWebView.destroy();
        mWebView = null;
    }
}
