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
import qiu.niorgai.StatusBarCompat;

/**
 * Created by 冯晓 on 2017/9/21.
 */

public class SignUpDelegate extends LatteDelegate {

    @BindView(R2.id.edit_sign_up_phone)
    TextInputEditText mPhone = null;

    @BindView(R2.id.edit_sign_up_pwd)
    TextInputEditText mPassword = null;

    @BindView(R2.id.edit_sign_up_verifyPwd)
    TextInputEditText mVerPassword = null;

    private ISignListener mISignListener = null;

    // 再点一次退出程序时间设置
    private static final long WAIT_TIME = 2000L;
    private long TOUCH_TIME = 0;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof ISignListener){
            mISignListener = (ISignListener) activity;
        }
    }



    @OnClick(R2.id.btn_sign_up)
    void onClickSignUp() {
        if (checkForm()) {
            RestClient.builder()
                    .url("sign_up")
                    .params("phone", mPhone.getText().toString())
                    .params("password", mPassword.getText().toString())
                    .success(new ISuccess() {
                        @Override
                        public void onSuccess(String response) {
                            //LatteLogger.json("USER_PROFILE", response);
                            JSONObject data = JSON.parseObject(response);
                            if (data.getInteger("code") != 0){
                                Toast.makeText(getContext(), data.getString("msg"), Toast.LENGTH_LONG).show();
                            } else{
                                SignHandler.onSignUp(response, mISignListener);
                            }
                        }
                    })
                    .build()
                    .post();
        }
    }

    @OnClick(R2.id.tv_link_sign_in)
    void onClickLink() {
        getSupportDelegate().start(new SignInDelegate());
    }


    private boolean checkForm() {
        final String phone = mPhone.getText().toString();
        final String password = mPassword.getText().toString();
        final String verPassword = mVerPassword.getText().toString();

        boolean isPass = true;

        if (phone.isEmpty() || phone.length() != 11) {
            mPhone.setError("手机号码错误");
            isPass = false;
        } else {
            mPhone.setError(null);
        }

        if (password.isEmpty() || password.length() < 6) {
            mPassword.setError("密码不得少于6位");
            isPass = false;
        } else {
            mPassword.setError(null);
        }

        if (verPassword.isEmpty() || verPassword.length() < 6 || !verPassword.equals(password)) {
            mVerPassword.setError("密码验证错误");
            isPass = false;
        } else {
            mVerPassword.setError(null);
        }
        return isPass;
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_sign_up;
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
