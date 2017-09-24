package cn.bravedawn.latte.ui.banner;

import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;

/**
 * Created by 冯晓 on 2017/9/24.
 */

public class HolderCreator implements CBViewHolderCreator<ImageHolder>{

    @Override
    public ImageHolder createHolder() {
        return new ImageHolder();
    }
}
