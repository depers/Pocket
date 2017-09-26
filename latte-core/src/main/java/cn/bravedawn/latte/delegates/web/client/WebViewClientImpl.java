package cn.bravedawn.latte.delegates.web.client;

import android.graphics.Bitmap;
import android.os.Handler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import cn.bravedawn.latte.app.Latte;
import cn.bravedawn.latte.delegates.web.IPageLoadListener;
import cn.bravedawn.latte.delegates.web.WebDelegate;
import cn.bravedawn.latte.delegates.web.route.Router;
import cn.bravedawn.latte.ui.loader.LatteLoader;
import cn.bravedawn.latte.util.log.LatteLogger;

/**
 * Created by 冯晓 on 2017/9/26.
 */

public class WebViewClientImpl extends WebViewClient {

    private final WebDelegate DELEGATE;
    private IPageLoadListener mPageLoadListener = null;
    private final Handler mHandler = Latte.getHandler();

    public void setPageLoadListener(IPageLoadListener listener){
        this.mPageLoadListener = listener;
    }

    public WebViewClientImpl(WebDelegate delegate) {
        this.DELEGATE = delegate;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        LatteLogger.d("shouldOverrideUrlLoading", url);
        return Router.getInstance().handleWebUrl(DELEGATE, url);
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        if (mPageLoadListener != null){
            mPageLoadListener.onLoadStart();
        }
        LatteLoader.showLoading(view.getContext());
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        if (mPageLoadListener != null){
            mPageLoadListener.onLoadEnd();
        }
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                LatteLoader.stopLoading();
            }
        }, 1000);
    }
}
