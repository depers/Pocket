package cn.bravedawn.latte.util.dimen;

import android.content.res.Resources;
import android.util.DisplayMetrics;

import cn.bravedawn.latte.app.Latte;

/**
 * Created by 冯晓 on 2017/9/18.
 */

public class DimenUtil {

    public static int getScreenWidth(){
        final Resources resources = Latte.getApplication().getResources();
        final DisplayMetrics dm = resources.getDisplayMetrics();
        return dm.widthPixels;
    }

    public static int getScreenHeight(){
        final Resources resources = Latte.getApplication().getResources();
        final DisplayMetrics dm = resources.getDisplayMetrics();
        return dm.heightPixels;
    }

}
