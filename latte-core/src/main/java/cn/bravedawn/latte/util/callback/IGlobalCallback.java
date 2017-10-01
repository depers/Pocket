package cn.bravedawn.latte.util.callback;

import android.support.annotation.Nullable;

/**
 * Created by 冯晓 on 2017/9/30.
 */

public interface IGlobalCallback<T> {

    void executeCallBack(@Nullable T args);
}
