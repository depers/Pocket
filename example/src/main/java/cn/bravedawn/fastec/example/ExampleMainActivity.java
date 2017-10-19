package cn.bravedawn.fastec.example;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.widget.Toast;

import cn.bravedawn.latte.activities.ProxyActivity;
import cn.bravedawn.latte.app.Latte;
import cn.bravedawn.latte.delegates.LatteDelegate;
import cn.bravedawn.latte.ec.launcher.LauncherDelegate;
import cn.bravedawn.latte.ec.main.EcBottomDelegate;
import cn.bravedawn.latte.ec.sign.ISignListener;
import cn.bravedawn.latte.ec.sign.SignInDelegate;
import cn.bravedawn.latte.ec.sign.SignUpDelegate;
import cn.bravedawn.latte.ui.launcher.ILauncherListener;
import cn.bravedawn.latte.ui.launcher.OnLauncherFinishTag;
import cn.jpush.android.api.JPushInterface;
import me.yokeyword.fragmentation.ISupportFragment;
import qiu.niorgai.StatusBarCompat;

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
        Latte.getConfigurator().withActivity(this);


        if (Build.VERSION.SDK_INT >= 23) {
            int REQUEST_CODE_CONTACT = 101;
            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            //验证是否许可权限
            for (String str : permissions) {
                if (this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                    //申请权限
                    this.requestPermissions(permissions, REQUEST_CODE_CONTACT);
                    return;
                }
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }

    @Override
    public LatteDelegate setRootDelegate() {
        return new LauncherDelegate();
    }

    @Override
    public void onSignInSuccess() {
        Toast.makeText(this, "登录成功", Toast.LENGTH_LONG).show();
        getSupportDelegate().start(new EcBottomDelegate());
    }

    @Override
    public void onSignUpSuccess() {
        Toast.makeText(this, "注册成功", Toast.LENGTH_LONG).show();
        getSupportDelegate().start(new EcBottomDelegate());
    }

    @Override
    public void onLauncherFinish(OnLauncherFinishTag tag) {
        switch (tag){
            case SIGNED:
                //Toast.makeText(this, "启动结束，用户已登录", Toast.LENGTH_LONG).show();
                getSupportDelegate().start(new EcBottomDelegate());
                break;
            case NOT_SIGNED:
                //Toast.makeText(this, "启动结束，用户未登录", Toast.LENGTH_LONG).show();
                getSupportDelegate().start(new SignInDelegate(), ISupportFragment.SINGLETOP);
                break;
            default:
                break;
        }
    }
}
