package cn.bravedawn.latte.ec.main.discover;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import cn.bravedawn.latte.delegates.bottom.BottomItemDelegate;
import cn.bravedawn.latte.delegates.web.IPageLoadListener;
import cn.bravedawn.latte.delegates.web.WebDelegateImpl;
import cn.bravedawn.latte.ec.R;
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

/**
 * Created by 冯晓 on 2017/9/26.
 */

public class DiscoverDelegate extends BottomItemDelegate {

    @Override
    public Object setLayout() {
        return R.layout.delegate_discover;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {

    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        final WebDelegateImpl delegate = WebDelegateImpl.create("index.html");
        delegate.setPageLoadListener(new IPageLoadListener() {
            @Override
            public void onLoadStart() {
                //Toast.makeText(delegate.getContext(), "start loading", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onLoadEnd() {
                //Toast.makeText(delegate.getContext(), "end loading", Toast.LENGTH_LONG).show();
            }
        });
        delegate.setTopDelegate(this.getParentDelegate());
        loadRootFragment(R.id.web_discover_container, delegate);
    }

    /**
     * 给一个横向动画
     * @return
     */
    @Override
    protected FragmentAnimator onCreateFragmentAnimator() {
        return new DefaultHorizontalAnimator();
    }
}
