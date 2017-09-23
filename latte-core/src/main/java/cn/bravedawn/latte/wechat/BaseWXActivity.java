package cn.bravedawn.latte.wechat;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

import cn.bravedawn.latte.wechat.LatteWeChat;

/**
 * Created by 冯晓 on 2017/9/23.
 */

public abstract class BaseWXActivity extends AppCompatActivity implements IWXAPIEventHandler{

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 这个必须写在onCreate中
        LatteWeChat.getInstance().getWXAPI().handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        LatteWeChat.getInstance().getWXAPI().handleIntent(getIntent(), this);
    }
}
