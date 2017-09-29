package cn.bravedawn.latte.ui.launcher;

import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;

/**
 * Created by 冯晓 on 2017/9/20.
 */

public class LauncherHolderCreator implements CBViewHolderCreator{

    @Override
    public Object createHolder() {
        return new LauncherHolder();
    }
}
