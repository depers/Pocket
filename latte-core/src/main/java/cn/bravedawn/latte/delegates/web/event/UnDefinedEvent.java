package cn.bravedawn.latte.delegates.web.event;

import cn.bravedawn.latte.util.log.LatteLogger;

/**
 * Created by 冯晓 on 2017/9/26.
 */

public class UnDefinedEvent extends Event{

    @Override
    public String execute(String params) {
        LatteLogger.e("UnDefinedEvent", params);
        return null;
    }
}
