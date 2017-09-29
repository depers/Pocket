package cn.bravedawn.latte.wechat.templates;

import android.widget.Toast;

import com.tencent.mm.opensdk.modelbase.BaseReq;

import cn.bravedawn.latte.activities.ProxyActivity;
import cn.bravedawn.latte.delegates.LatteDelegate;
import cn.bravedawn.latte.wechat.BaseWXPayEntryActivity;

/**
 * Created by 冯晓 on 2017/9/23.
 */

public class WXPayEntryTemplate extends BaseWXPayEntryActivity{


    @Override
    protected void onPaySuccess() {
        Toast.makeText(this, "支付成功", Toast.LENGTH_LONG).show();
        finish();
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onPayFail() {
        Toast.makeText(this, "支付失败", Toast.LENGTH_LONG).show();
        finish();
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onPayCancel() {
        Toast.makeText(this, "支付取消", Toast.LENGTH_LONG).show();
        finish();
        overridePendingTransition(0, 0);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }
}
