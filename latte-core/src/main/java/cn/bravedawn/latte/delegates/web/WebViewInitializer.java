package cn.bravedawn.latte.delegates.web;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;

import cn.bravedawn.latte.app.ConfigKeys;
import cn.bravedawn.latte.app.Latte;
import cn.bravedawn.latte.util.net.NetWorkUtils;

/**
 * Created by 冯晓 on 2017/9/26.
 */

public class WebViewInitializer {

    private static final String APP_CACHE_DIRNAME = "APP_CACHE_DIR";

    @SuppressLint("SetJavaScriptEnabled")
    public WebView createWebView(WebView webView){

        webView.setWebContentsDebuggingEnabled(true);
        // cookies
        final CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.setAcceptThirdPartyCookies(webView, true);
        }
        CookieManager.setAcceptFileSchemeCookies(true);

        // 不能横向滚动
        webView.setHorizontalScrollBarEnabled(false);
        // 不能纵向滚动
        webView.setVerticalScrollBarEnabled(false);
        // 允许截图
        webView.setDrawingCacheEnabled(true);
        // 屏蔽长按事件
        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return true;
            }
        });
        // 初始化webSettings
        final WebSettings settings = webView.getSettings();
        // 开放js通道
        settings.setJavaScriptEnabled(true);
        final String ua = settings.getUserAgentString();
        settings.setUserAgentString(ua+"latte");
        // 隐藏缩放控件
        settings.setBuiltInZoomControls(false);
        settings.setDisplayZoomControls(false);
        // 文件权限
        settings.setAllowFileAccess(true);
        settings.setAllowFileAccessFromFileURLs(true);
        settings.setAllowUniversalAccessFromFileURLs(true);
        settings.setAllowContentAccess(true);
        // 缓存相关
        settings.setAppCacheEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        if (NetWorkUtils.isNetworkConnected(webView.getContext())){
            settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        } else{
            settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }

        String cacheDirPath = ((Activity)Latte.getConfiguration(ConfigKeys.ACTIVITY)).getFilesDir()
                .getAbsolutePath() + APP_CACHE_DIRNAME;
        settings.setAppCachePath(cacheDirPath);

        return webView;
    }
}
