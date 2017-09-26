package cn.bravedawn.fastec.example.event;

import android.webkit.WebView;
import android.widget.Toast;

import cn.bravedawn.latte.delegates.web.event.Event;

/**
 * Created by 冯晓 on 2017/9/26.
 */

public class TestEvent extends Event {

    @Override
    public String execute(String params) {
        Toast.makeText(getContext(), params, Toast.LENGTH_LONG).show();
        if (getAction().equals("test")){
            final WebView webView = getWebView();
            webView.post(new Runnable() {
                @Override
                public void run() {
                    webView.evaluateJavascript("nativeCall();", null);
                }
            });
        }
        return null;
    }
}
