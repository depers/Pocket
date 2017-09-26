package cn.bravedawn.latte.delegates.web.event;

import android.content.Context;
import android.webkit.WebView;

import cn.bravedawn.latte.delegates.LatteDelegate;
import cn.bravedawn.latte.delegates.web.WebDelegate;

/**
 * Created by 冯晓 on 2017/9/26.
 */

public abstract class Event implements IEvent{

    private Context mContext = null;
    private String mAction = null;
    private WebDelegate mDelegate = null;
    private String mUrl = null;
    private WebView mWebView = null;

    public Context getContext() {
        return mContext;
    }

    public void setContext(Context context) {
        this.mContext = context;
    }

    public String getAction() {
        return mAction;
    }

    public void setAction(String action) {
        this.mAction = action;
    }

    public LatteDelegate getDelegate() {
        return mDelegate;
    }

    public void setDelegate(WebDelegate delegate) {
        this.mDelegate = delegate;
    }

    public String gtmUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        this.mUrl = url;
    }

    public WebView getWebView() {
        return mDelegate.getWebView();
    }

}
