package cn.bravedawn.latte.ec.sign;

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

/**
 * Created by 冯晓 on 2017/9/21.
 */

public class SignUpDelegate extends LatteDelegate {

    @BindView(R2.id.edit_sign_up_name)
    TextInputEditText mName = null;

    @BindView(R2.id.edit_sign_up_email)
    TextInputEditText mEmail = null;

    @BindView(R2.id.edit_sign_up_phone)
    TextInputEditText mPhone = null;

    @BindView(R2.id.edit_sign_up_pwd)
    TextInputEditText mPassword = null;

    @BindView(R2.id.edit_sign_up_verifyPwd)
    TextInputEditText mVerPassword = null;

    @OnClick(R2.id.btn_sign_up)
    void onClickSignUp() {
        if (checkForm()) {
//            RestClient.builder()
//                    .url("sign_up")
//                    .params("", "")
//                    .success(new ISuccess() {
//                        @Override
//                        public void onSuccess(String response) {
//
//                        }
//                    })
//                    .build()
//                    .post();
            Toast.makeText(getContext(), "验证通过", Toast.LENGTH_LONG).show();
        }
    }


    private boolean checkForm() {
        final String name = mName.getText().toString();
        final String email = mEmail.getText().toString();
        final String phone = mPhone.getText().toString();
        final String password = mPassword.getText().toString();
        final String verPassword = mVerPassword.getText().toString();

        boolean isPass = true;

        if (name.isEmpty()) {
            mName.setError("请输入姓名");
            isPass = false;
        } else {
            mName.setError(null);
        }

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mEmail.setError("错误的邮箱格式");
            isPass = false;
        } else {
            mEmail.setError(null);
        }

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

    }
}
