package cn.bravedawn.latte.ec.main.sort;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import cn.bravedawn.latte.delegates.LatteDelegate;
import cn.bravedawn.latte.delegates.bottom.BottomItemDelegate;
import cn.bravedawn.latte.ec.R;
import cn.bravedawn.latte.ec.main.sort.content.ContentDelegate;
import cn.bravedawn.latte.ec.main.sort.list.VerticalListDelegate;

/**
 * Created by 冯晓 on 2017/9/25.
 */

public class SortDelegate extends BottomItemDelegate {
    @Override
    public Object setLayout() {
        return R.layout.delegate_sort;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        final VerticalListDelegate verticalListDelegate = new VerticalListDelegate();
        loadRootFragment(R.id.vertical_list_container, verticalListDelegate);
        // 设置右侧第一个分类显示，默认显示分类一
        replaceLoadRootFragment(R.id.sort_content_container, ContentDelegate.newInstance(1), false);
    }
}
