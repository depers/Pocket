package cn.bravedawn.latte.ec.main.personal.settings;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import cn.bravedawn.latte.delegates.LatteDelegate;
import cn.bravedawn.latte.ec.R;

/**
 * Created by 冯晓 on 2017/9/29.
 */

public class NameDelegate extends LatteDelegate {

    @Override
    public Object setLayout() {
        return R.layout.delegate_name;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {

    }
}