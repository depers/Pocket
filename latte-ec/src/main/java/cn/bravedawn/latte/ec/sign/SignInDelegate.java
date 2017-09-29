package cn.bravedawn.latte.ec.sign;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bravedawn.latte.delegates.LatteDelegate;
import cn.bravedawn.latte.ec.R;
import cn.bravedawn.latte.ec.R2;
import cn.bravedawn.latte.net.RestClient;
import cn.bravedawn.latte.net.callback.ISuccess;
import cn.bravedawn.latte.util.log.LatteLogger;
import cn.bravedawn.latte.wechat.LatteWeChat;
import cn.bravedawn.latte.wechat.callbacks.IWeChatSignInCallback;

/**
 * Created by 冯晓 on 2017/9/21.
 */

public class SignInDelegate extends LatteDelegate {

    @BindView(R2.id.edit_sign_in_phone)
    TextInputEditText mPhone = null;

    @BindView(R2.id.edit_sign_in_pwd)
    TextInputEditText mPwd = null;

    private ISignListener mISignListener = null;

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

    @OnClick(R2.id.btn_sign_in)
    void onClickSignIn() {
        if (checkForm()) {
            RestClient.builder()
                    .url("sign_up")
                    .params("phone", mPhone.getText().toString())
                    .params("password", mPwd.getText().toString())
                    .success(new ISuccess() {
                        @Override
                        public void onSuccess(String response) {
                            LatteLogger.json("USER_PROFILE", response);
                            SignHandler.onSignIn(response, mISignListener);
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

    }
}
