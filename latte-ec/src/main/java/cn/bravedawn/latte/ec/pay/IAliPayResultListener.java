package cn.bravedawn.latte.ec.pay;

/**
 * Created by 冯晓 on 2017/9/29.
 */

public interface IAliPayResultListener {

    void onPaySuccess();

    void onPaying();

    void onPayFail();

    void onPayCancel();

    void onPayConnectError();
}
