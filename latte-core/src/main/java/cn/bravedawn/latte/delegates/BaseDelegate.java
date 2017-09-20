package cn.bravedawn.latte.delegates;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.bravedawn.latte.activities.ProxyActivity;
import me.yokeyword.fragmentation_swipeback.SwipeBackFragment;

/**
 * Created by 冯晓 on 2017/9/15.
 */

public abstract class BaseDelegate extends SwipeBackFragment{

    @SuppressWarnings("SpellCheckingInspection")
    private Unbinder mUnbinder = null;

    public abstract Object setLayout();

    public abstract void onBindView(@Nullable Bundle savedInstanceState, View rootView);

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView;
        if (setLayout() instanceof Integer){
            rootView = inflater.inflate((Integer) setLayout(), container, false);
        } else if (setLayout() instanceof View){
            rootView = (View) setLayout();
        } else{
            throw new ClassCastException("setLayout() type must be int or View!");
        }
        mUnbinder = ButterKnife.bind(this, rootView);
        onBindView(savedInstanceState, rootView);

        return rootView;
    }

    public final ProxyActivity getProsyActivity(){
        return (ProxyActivity) _mActivity;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // 解除绑定
        if (mUnbinder != null){
            mUnbinder.unbind();
        }
    }
}
