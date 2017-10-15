package cn.bravedawn.latte.ec.detail;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import butterknife.BindView;
import cn.bravedawn.latte.delegates.LatteDelegate;
import cn.bravedawn.latte.delegates.web.WebDelegateImpl;
import cn.bravedawn.latte.ec.R;
import cn.bravedawn.latte.ec.R2;
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

/**
 * Created by 冯晓 on 2017/10/15.
 */

public class RecordDetailDelegate extends LatteDelegate{

    private String mUrl = null;

    @BindView(R2.id.record_detail_title)
    AppCompatTextView mTextView = null;

    public static RecordDetailDelegate create(String url){
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        RecordDetailDelegate delegate = new RecordDetailDelegate();
        delegate.setArguments(bundle);
        return delegate;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        mUrl = args.getString("url");
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_record_detail;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {

    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        final WebDelegateImpl delegate = WebDelegateImpl.create(mUrl, mTextView);
        delegate.setTopDelegate(this.getParentDelegate());
        getSupportDelegate().loadRootFragment(R.id.web_re_detail_container, delegate);
    }

    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        return new DefaultHorizontalAnimator();
    }
}
