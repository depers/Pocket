package cn.bravedawn.latte.ec.main.personal.order.detail;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bravedawn.latte.delegates.LatteDelegate;
import cn.bravedawn.latte.ec.R;
import cn.bravedawn.latte.ec.R2;
import cn.bravedawn.latte.ui.weight.AutoPhotoLayout;
import cn.bravedawn.latte.ui.weight.StarLayout;
import cn.bravedawn.latte.util.callback.CallBackManager;
import cn.bravedawn.latte.util.callback.CallBackType;
import cn.bravedawn.latte.util.callback.IGlobalCallback;

/**
 * Created by 冯晓 on 2017/10/1.
 */

public class OrderCommentDelegate extends LatteDelegate{


    @BindView(R2.id.custom_star_layout)
    StarLayout mStartLayout = null;

    @BindView(R2.id.custom_auto_photo_layout)
    AutoPhotoLayout mAutoPhotoLayout = null;

    @OnClick(R2.id.top_tv_comment_commit)
    void onClickSubmit(){
        Toast.makeText(getContext(), "评分：" + mStartLayout.getStarCount(), Toast.LENGTH_LONG).show();
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_order_comment;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        mAutoPhotoLayout.setDelegate(this);
        CallBackManager.getInstance()
                .addCallBack(CallBackType.ON_CROP, new IGlobalCallback<Uri>() {
                    @Override
                    public void executeCallBack(@Nullable Uri args) {
                        mAutoPhotoLayout.onCropTarget(args);
                    }
                });
    }
}
