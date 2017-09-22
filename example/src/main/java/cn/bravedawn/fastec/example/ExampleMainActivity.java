package cn.bravedawn.fastec.example;

import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import cn.bravedawn.latte.activities.ProxyActivity;
import cn.bravedawn.latte.app.Latte;
import cn.bravedawn.latte.delegates.LatteDelegate;
import cn.bravedawn.latte.ec.launcher.LauncherDelegate;
import cn.bravedawn.latte.ec.launcher.LauncherScrollDelegate;
import cn.bravedawn.latte.ec.sign.ISignListener;
import cn.bravedawn.latte.ec.sign.SignUpDelegate;
import cn.bravedawn.latte.ui.launcher.ILauncherListener;
import cn.bravedawn.latte.ui.launcher.OnLauncherFinishTag;

public class ExampleMainActivity extends ProxyActivity implements
        ISignListener,
        ILauncherListener{

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }

    @Override
    public LatteDelegate setRootDelegate() {
        return new SignUpDelegate();
    }

    @Override
    public void onSignInSuccess() {
        Toast.makeText(this, "登录成功", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSignUpSuccess() {
        Toast.makeText(this, "注册成功", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLauncherFinish(OnLauncherFinishTag tag) {
        switch (tag){
            case SIGNED:
                Toast.makeText(this, "启动结束，用户已登录", Toast.LENGTH_LONG).show();
                startWithPop(new ExampleDelegate());
                break;
            case NOT_SIGNED:
                Toast.makeText(this, "启动结束，用户未登录", Toast.LENGTH_LONG).show();
                startWithPop(new SignUpDelegate());
                break;
            default:
                break;
        }
    }
}
