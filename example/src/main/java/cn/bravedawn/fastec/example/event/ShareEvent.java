package cn.bravedawn.fastec.example.event;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mob.MobSDK;

import cn.bravedawn.latte.delegates.web.event.Event;
import cn.bravedawn.latte.util.log.LatteLogger;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by 冯晓 on 2017/10/2.
 */

public class ShareEvent extends Event{


    @Override
    public String execute(String params) {

        LatteLogger.d("ShareEvent", params);

        final JSONObject object = JSON.parseObject(params).getJSONObject("params");
        final String title = object.getString("title");
        final String url = object.getString("url");
        final String imageUrl = object.getString("imageUrl");
        final String text = object.getString("text");

        final OnekeyShare oks = new OnekeyShare();
        oks.disableSSOWhenAuthorize();
        oks.setTitle(title);
        oks.setText(text);
        oks.setImageUrl(imageUrl);
        oks.setUrl(url);
        oks.show(getContext());

        return null;
    }
}
