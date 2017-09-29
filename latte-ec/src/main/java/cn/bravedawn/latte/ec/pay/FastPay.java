package cn.bravedawn.latte.ec.pay;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.alibaba.fastjson.JSON;

import cn.bravedawn.latte.delegates.LatteDelegate;
import cn.bravedawn.latte.ec.R;
import cn.bravedawn.latte.net.RestClient;
import cn.bravedawn.latte.net.callback.ISuccess;
import cn.bravedawn.latte.util.log.LatteLogger;

/**
 * Created by 冯晓 on 2017/9/29.
 */

public class FastPay implements View.OnClickListener {

    // 设置支付回调监听
    private IAliPayResultListener mAliPayResultListener = null;
    private Activity mActivity = null;
    private AlertDialog mDialog = null;
    private int mOrderId = -1;

    private FastPay(LatteDelegate delegate) {
        this.mActivity = delegate.getProxyActivity();
        this.mDialog = new AlertDialog.Builder(delegate.getContext()).create();
    }

    public static FastPay create(LatteDelegate delegate) {
        return new FastPay(delegate);
    }

    public void beginPayDialog() {
        mDialog.show();
        final Window window = mDialog.getWindow();
        if (window != null) {
            window.setContentView(R.layout.dialog_pay_panel);
            window.setGravity(Gravity.BOTTOM);
            window.setWindowAnimations(R.style.anim_panel_up_from_bottom);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            // 设置属性
            final WindowManager.LayoutParams params = window.getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            window.setAttributes(params);

            window.findViewById(R.id.btn_dialog_pay_alipay).setOnClickListener(this);
            window.findViewById(R.id.btn_dialog_pay_wechat).setOnClickListener(this);
            window.findViewById(R.id.btn_dialog_pay_cancel).setOnClickListener(this);
        }
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        // 因为是library不能使用switch,library没有常量
        if (id == R.id.btn_dialog_pay_alipay) {
            aliPay(mOrderId);
            mDialog.cancel();
        } else if (id == R.id.btn_dialog_pay_wechat) {
            mDialog.cancel();
        } else if (id == R.id.btn_dialog_pay_cancel) {
            mDialog.cancel();
        }
    }


    public final void aliPay(int orderId) {
        final String singUrl = "";
        // 获取签名字符串
        RestClient.builder()
                .url(singUrl)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        final String paySign = JSON.parseObject(response).getString("result");
                        LatteLogger.d("PAY_SIGN", paySign);
                        // 必须是异步调用客户端支付接口
                        final PayAsyncTask payAsyncTask = new PayAsyncTask(mActivity, mAliPayResultListener);
                        payAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, paySign);
                    }
                })
                .build()
                .post();
    }

    public FastPay setPayResultListener(IAliPayResultListener listener) {
        this.mAliPayResultListener = listener;
        return this;
    }

    public FastPay setOrderId(int orderId) {
        this.mOrderId = orderId;
        return this;
    }
}
