package cn.bravedawn.latte.wechat.templates;

import cn.bravedawn.latte.activities.ProxyActivity;
import cn.bravedawn.latte.app.Latte;
import cn.bravedawn.latte.delegates.LatteDelegate;
import cn.bravedawn.latte.wechat.BaseEntryActivity;
import cn.bravedawn.latte.wechat.LatteWeChat;

/**
 * Created by 冯晓 on 2017/9/23.
 */

public class WXEntryTemplate extends BaseEntryActivity{

    @Override
    protected void onResume() {
        super.onResume();
        finish();
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onSignInSuccess(String userInfo) {
        LatteWeChat.getInstance().getSignInCallback().onSignSuccess(userInfo);
    }
}
