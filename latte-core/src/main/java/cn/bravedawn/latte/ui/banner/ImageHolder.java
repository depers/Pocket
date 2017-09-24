package cn.bravedawn.latte.ui.banner;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.holder.Holder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import cn.bravedawn.latte.R;

/**
 * Created by 冯晓 on 2017/9/24.
 */

public class ImageHolder implements Holder<String>{

    private AppCompatImageView mImageView = null;

    @Override
    public View createView(Context context) {
        mImageView = new AppCompatImageView(context);
        return mImageView;
    }

    @Override
    public void UpdateUI(Context context, int i, String s) {
        Glide.with(context)
                .load(s)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .centerCrop()
                .into(mImageView);
    }
}
