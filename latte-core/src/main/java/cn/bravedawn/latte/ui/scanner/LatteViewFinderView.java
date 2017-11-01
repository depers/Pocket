package cn.bravedawn.latte.ui.scanner;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import cn.bravedawn.latte.R;
import me.dm7.barcodescanner.core.ViewFinderView;

/**
 * Created by 冯晓 on 2017/10/2.
 */

public class LatteViewFinderView extends ViewFinderView{


    public LatteViewFinderView(Context context) {
        this(context, null);
    }

    public LatteViewFinderView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        mSquareViewFinder = true;
        mBorderPaint.setColor(ContextCompat.getColor(getContext(), R.color.app_main));
        mLaserPaint.setColor(ContextCompat.getColor(getContext(), R.color.app_main));
    }
}
