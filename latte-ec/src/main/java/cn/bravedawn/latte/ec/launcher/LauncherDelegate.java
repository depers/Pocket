package cn.bravedawn.latte.ec.launcher;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import java.text.MessageFormat;
import java.util.Timer;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bravedawn.latte.app.AccountManager;
import cn.bravedawn.latte.app.ConfigKeys;
import cn.bravedawn.latte.app.IUserChecker;
import cn.bravedawn.latte.app.Latte;
import cn.bravedawn.latte.delegates.LatteDelegate;
import cn.bravedawn.latte.ec.R;
import cn.bravedawn.latte.ec.R2;
import cn.bravedawn.latte.ui.launcher.ILauncherListener;
import cn.bravedawn.latte.ui.launcher.OnLauncherFinishTag;
import cn.bravedawn.latte.ui.launcher.ScrollLauncherTag;
import cn.bravedawn.latte.util.storage.LattePreference;
import cn.bravedawn.latte.util.timer.BaseTimerTask;
import cn.bravedawn.latte.util.timer.ITimeListener;
import qiu.niorgai.StatusBarCompat;

/**
 * Created by 冯晓 on 2017/9/20.
 */

public class LauncherDelegate extends LatteDelegate implements ITimeListener {

    @BindView(R2.id.tv_launcher_timer)
    AppCompatTextView mTvTimer = null;

    private Timer mTimer = null;
    private int mCount = 5;
    private ILauncherListener mILauncherListener = null;

    @OnClick(R2.id.tv_launcher_timer)
    void onClickTimerView() {
        // 这里一定要将mTimer置为null
        mTimer = null;
        checkIsShowScroll();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof ILauncherListener){
            mILauncherListener = (ILauncherListener) activity;

        }
    }

    private void initTimer() {
        mTimer = new Timer();
        final BaseTimerTask task = new BaseTimerTask(this);
        //第一个参数是任务，第二个是延迟时间，第三个是间隔时间
        mTimer.schedule(task, 0, 1000);
    }

    // 判断是否显示滑动启动页
    private void checkIsShowScroll() {
        if (!LattePreference.getAppFlag(ScrollLauncherTag.HAS_FIRST_LAUNCHER_APP.name())) {
            getSupportDelegate().start(new LauncherScrollDelegate(), SINGLETASK);
        } else {
            // todo 检查用户是否登陆了App，这里的代码需要再加工抽象
            AccountManager.checkAccount(new IUserChecker() {
                @Override
                public void onSignIn() {
                    if (mILauncherListener != null){
                        mILauncherListener.onLauncherFinish(OnLauncherFinishTag.SIGNED);
                    }
                }

                @Override
                public void NotSignIn() {
                    if (mILauncherListener != null){
                        mILauncherListener.onLauncherFinish(OnLauncherFinishTag.NOT_SIGNED);
                    }
                }
            });
        }
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_launcher;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
        StatusBarCompat.translucentStatusBar((Activity) Latte.getConfiguration(ConfigKeys.ACTIVITY), true);
        initTimer();
    }

    @Override
    public void onTimer() {
        getProxyActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mTvTimer != null) {
                    mTvTimer.setText(MessageFormat.format("跳过 {0}s", mCount));
                    mCount--;
                    if (mCount <= 0) {
                        if (mTimer != null) {
                            mTimer.cancel();
                            mTimer = null;
                            checkIsShowScroll();
                        }
                    }
                }
            }
        });
    }
}
