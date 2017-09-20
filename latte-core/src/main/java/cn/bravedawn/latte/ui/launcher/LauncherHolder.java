package cn.bravedawn.latte.ui.launcher;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.holder.Holder;

/**
 * Created by 冯晓 on 2017/9/20.
 */

public class LauncherHolder implements Holder<Integer>{

    private AppCompatImageView mImageView = null;


    @Override
    public View createView(Context context) {
        mImageView = new AppCompatImageView(context);
        mImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        return mImageView;
    }

    @Override
    public void UpdateUI(Context context, int i, Integer data) {
        mImageView.setBackgroundResource(data);
    }
}
