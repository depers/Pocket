package cn.bravedawn.latte.ec.launcher;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;

import java.util.ArrayList;

import cn.bravedawn.latte.app.AccountManager;
import cn.bravedawn.latte.app.IUserChecker;
import cn.bravedawn.latte.delegates.LatteDelegate;
import cn.bravedawn.latte.ec.R;
import cn.bravedawn.latte.ui.launcher.ILauncherListener;
import cn.bravedawn.latte.ui.launcher.LauncherHolderCreator;
import cn.bravedawn.latte.ui.launcher.OnLauncherFinishTag;
import cn.bravedawn.latte.ui.launcher.ScrollLauncherTag;
import cn.bravedawn.latte.util.storage.LattePreference;

/**
 * Created by 冯晓 on 2017/9/20.
 */

public class LauncherScrollDelegate extends LatteDelegate implements OnItemClickListener{

    private ConvenientBanner<Integer> mConvenientBanner = null;
    private static final ArrayList<Integer> INTEGERS = new ArrayList<>();
    private ILauncherListener mILauncherListener = null;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof ILauncherListener){
            mILauncherListener = (ILauncherListener) activity;

        }
    }

    private void initBanner(){
        INTEGERS.add(R.mipmap.launcher_01);
        INTEGERS.add(R.mipmap.launcher_02);
        INTEGERS.add(R.mipmap.launcher_03);
        INTEGERS.add(R.mipmap.launcher_04);
        INTEGERS.add(R.mipmap.launcher_05);
        mConvenientBanner
                .setPages(new LauncherHolderCreator(), INTEGERS)
                .setPageIndicator(new int[]{R.drawable.dot_normal, R.drawable.dot_focus})
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL)
                .setOnItemClickListener(this)
                .setCanLoop(false);

    }


    @Override
    public Object setLayout() {
        mConvenientBanner = new ConvenientBanner<Integer>(getContext());
        return mConvenientBanner;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
        initBanner();
    }

    @Override
    public void onItemClick(int i) {
        // 如果点击的是最后一个
        if (i == INTEGERS.size()-1){
            LattePreference.setAppFlag(ScrollLauncherTag.HAS_FIRST_LAUNCHER_APP.name(), true);
            // todo 检查用户是否已经登录,这里的代码需要再加工抽象
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
}
