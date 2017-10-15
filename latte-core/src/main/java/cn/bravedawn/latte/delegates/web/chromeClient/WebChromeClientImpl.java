package cn.bravedawn.latte.delegates.web.chromeClient;

import android.support.v7.widget.AppCompatTextView;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

/**
 * Created by 冯晓 on 2017/9/26.
 */

public class WebChromeClientImpl extends WebChromeClient{

    @Override
    public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
        return super.onJsAlert(view, url, message, result);
    }


}
