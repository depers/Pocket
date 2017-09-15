package cn.bravedawn.fastec.example;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import cn.bravedawn.latte.delegates.LatteDelegate;

/**
 * Created by 冯晓 on 2017/9/15.
 */

public class ExampleDelegate extends LatteDelegate{
    @Override
    public Object setLayout() {
        return R.layout.delegate_example;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {

    }
}
