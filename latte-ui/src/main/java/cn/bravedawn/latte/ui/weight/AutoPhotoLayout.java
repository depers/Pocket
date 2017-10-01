package cn.bravedawn.latte.ui.weight;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.view.View;

import com.joanzapata.iconify.widget.IconTextView;

import java.util.ArrayList;
import java.util.List;

import cn.bravedawn.latte.delegates.LatteDelegate;

/**
 * Created by 冯晓 on 2017/10/1.
 */

public class AutoPhotoLayout extends LinearLayoutCompat{

    private int mCurrentNum = 0;
    private int mMaxNum = 0;
    private int mMaxLineNum = 0;
    private IconTextView mIconAdd = null;
    private LayoutParams mParams;
    // 要删除的图片id
    private int mDeleteId = 0;
    private AppCompatImageView mTargetImageView = null;
    private int mImageMargin = 0;
    private LatteDelegate mDelegate = null;
    private List<View> mLineViews = null;
    private AlertDialog mTargetDialog = null;
    private static final String ICON_TEXT = "{fa-plus}";
    private float mIconSize = 0;

    private static final List<List<View>> ALL_VIEWS = new ArrayList<>();
    private static final List<Integer> LINE_HEIGHTS = new ArrayList<>();

    public AutoPhotoLayout(Context context) {
        super(context, null);
    }

    public AutoPhotoLayout(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public AutoPhotoLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
