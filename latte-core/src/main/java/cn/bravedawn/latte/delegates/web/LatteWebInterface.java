package cn.bravedawn.latte.delegates.web;

import com.alibaba.fastjson.JSON;

/**
 * Created by 冯晓 on 2017/9/26.
 */

public class LatteWebInterface {

    private final WebDelegate DELEGATE;

    private LatteWebInterface(WebDelegate delegate) {
        this.DELEGATE = delegate;
    }

    public static LatteWebInterface create(WebDelegate delegate){
        return new LatteWebInterface(delegate);
    }

    public String evnet(String params){
        final String action = JSON.parseObject(params).getString("action");
        return null;
    }
}
