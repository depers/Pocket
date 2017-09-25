package cn.bravedawn.latte.ec.main.sort.content;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import cn.bravedawn.latte.delegates.LatteDelegate;
import cn.bravedawn.latte.ec.R;

/**
 * Created by 冯晓 on 2017/9/25.
 */

public class ContentDelegate extends LatteDelegate{

    private static final String ARG_CONTENT_ID = "CONTENT_ID";
    private int mContentId = -1;

    public static ContentDelegate newInstance(int contentId){
        final Bundle args = new Bundle();
        args.putInt(ARG_CONTENT_ID, contentId);
        final ContentDelegate contentDelegate = new ContentDelegate();
        contentDelegate.setArguments(args);
        return contentDelegate;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle args = getArguments();
        if (args != null){
            mContentId = args.getInt(ARG_CONTENT_ID);
        }
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_content_list;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {

    }
}
