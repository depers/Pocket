package cn.bravedawn.latte.ec.sign;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.ContextCompat;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bravedawn.latte.app.ConfigKeys;
import cn.bravedawn.latte.app.Latte;
import cn.bravedawn.latte.delegates.LatteDelegate;
import cn.bravedawn.latte.ec.R;
import cn.bravedawn.latte.ec.R2;
import cn.bravedawn.latte.net.RestClient;
import cn.bravedawn.latte.net.callback.ISuccess;
import cn.bravedawn.latte.util.log.LatteLogger;
import cn.bravedawn.latte.wechat.LatteWeChat;
import cn.bravedawn.latte.wechat.callbacks.IWeChatSignInCallback;
import qiu.niorgai.StatusBarCompat;

/**
 * Created by 冯晓 on 2017/9/21.
 */

public class SignInDelegate extends LatteDelegate {

    @BindView(R2.id.edit_sign_in_phone)
    TextInputEditText mPhone = null;

    @BindView(R2.id.edit_sign_in_pwd)
    TextInputEditText mPwd = null;

    private ISignListener mISignListener = null;

    // 再点一次退出程序时间设置
    private static final long WAIT_TIME = 2000L;
    private long TOUCH_TIME = 0;

    @OnClick(R2.id.icon_sign_in_weChat)
    void onClickWeiXinSignIn() {
        LatteWeChat.getInstance().onSignSuccess(new IWeChatSignInCallback() {
            @Override
            public void onSignSuccess(String userInfo) {
                Toast.makeText(getContext(), userInfo, Toast.LENGTH_LONG).show();
            }
        }).signIn();
    }

    @OnClick(R2.id.tv_link_sign_up)
    void onClickLink() {
        getSupportDelegate().start(new SignUpDelegate());
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof ISignListener){
            mISignListener = (ISignListener) activity;
        }
    }

    // TODO: 2017/10/16 登录请求
    @OnClick(R2.id.btn_sign_in)
    void onClickSignIn() {
        if (checkForm()) {
            RestClient.builder()
                    .url("sign_in")
                    .params("phone", mPhone.getText().toString())
                    .params("password", mPwd.getText().toString())
                    .success(new ISuccess() {
                        @Override
                        public void onSuccess(String response) {
                            //LatteLogger.json("USER_PROFILE", response);
                            JSONObject data = JSON.parseObject(response);
                            if (data.getInteger("code") != 0){
                                Toast.makeText(getContext(), data.getString("msg"), Toast.LENGTH_LONG).show();
                            } else{
                                SignHandler.onSignIn(response, mISignListener);
                            }
                        }
                    })
                    .build()
                    .post();
        }
    }

    private boolean checkForm() {
        final String phone = mPhone.getText().toString();
        final String password = mPwd.getText().toString();

        boolean isPass = true;

        if (phone.isEmpty() || phone.length() != 11) {
            mPhone.setError("手机号码错误");
            isPass = false;
        } else {
            mPhone.setError(null);
        }

        if (password.isEmpty() || password.length() < 6) {
            mPwd.setError("密码不得少于6位");
            isPass = false;
        } else {
            mPwd.setError(null);
        }

        return isPass;
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_sign_in;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
        StatusBarCompat.setStatusBarColor((Activity) Latte.getConfiguration(ConfigKeys.ACTIVITY),
                ContextCompat.getColor(getContext(), R.color.colorPrimary));
    }


    @Override
    public boolean onBackPressedSupport() {
        if (System.currentTimeMillis() - TOUCH_TIME < WAIT_TIME) {
            _mActivity.finish();
        } else {
            TOUCH_TIME = System.currentTimeMillis();
            Toast.makeText(_mActivity, "双击退出" + Latte.getApplicationContext().getString(cn.bravedawn.latte.R.string.app_name), Toast.LENGTH_SHORT).show();
        }
        return true;
    }
}
