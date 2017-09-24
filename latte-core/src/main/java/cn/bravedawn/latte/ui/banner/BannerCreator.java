package cn.bravedawn.latte.ui.banner;

import com.ToxicBakery.viewpager.transforms.DefaultTransformer;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;

import java.util.ArrayList;

import cn.bravedawn.latte.R;

/**
 * Created by 冯晓 on 2017/9/24.
 */

public class BannerCreator {

    public static void setDefault(ConvenientBanner<String> convenientBanner
            , ArrayList<String> banners, OnItemClickListener listener){
        convenientBanner
                .setPages(new HolderCreator(), banners)
                .setPageIndicator(new int[]{R.drawable.dot_normal, R.drawable.dot_focus})
                .setOnItemClickListener(listener)
                .setPageTransformer(new DefaultTransformer())
                .startTurning(3000)
                .setCanLoop(true);

    }

}
