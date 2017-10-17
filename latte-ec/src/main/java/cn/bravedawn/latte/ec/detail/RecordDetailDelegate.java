package cn.bravedawn.latte.ec.detail;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

import com.joanzapata.iconify.widget.IconTextView;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bravedawn.latte.delegates.LatteDelegate;
import cn.bravedawn.latte.delegates.web.WebDelegateImpl;
import cn.bravedawn.latte.delegates.web.chromeClient.WebChromeClientImpl;
import cn.bravedawn.latte.ec.R;
import cn.bravedawn.latte.ec.R2;
import cn.bravedawn.latte.util.log.LatteLogger;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

/**
 * Created by 冯晓 on 2017/10/15.
 */

public class RecordDetailDelegate extends LatteDelegate{

    private String mUrl = null;
    private boolean mIsStar = false;
    private WebDelegateImpl mDelegate = null;
    private String mIconUrl = null;
    private String mTitle = null;

    @BindView(R2.id.record_detail_title)
    AppCompatTextView mTextView = null;

    @BindView(R2.id.record_detail_toolbar)
    Toolbar mToolbar = null;

    @BindView(R2.id.icon_add_star)
    IconTextView mStarIconTextView = null;

    @OnClick(R2.id.icon_record_detail_back)
    void onClickBack(){
        getSupportDelegate().pop();
    }

    @OnClick(R2.id.icon_add_star)
    void onClickAddStar(){
        if ((boolean)mStarIconTextView.getTag()){
            mStarIconTextView.setTextColor(Color.WHITE);
            mStarIconTextView.setTag(false);
            Toast.makeText(getContext(), "已添加该项目到集锦菜单！", Toast.LENGTH_LONG).show();
        } else{
            mStarIconTextView.setTextColor(Color.parseColor("#f4ea2a"));
            mStarIconTextView.setTag(true);
            Toast.makeText(getContext(), "该项目已从集锦菜单中移除！", Toast.LENGTH_LONG).show();
        }
    }

    @OnClick(R2.id.icon_add_share)
    void onClickShare(){
        ShareSDK.initSDK(getContext());
        final OnekeyShare oks = new OnekeyShare();
        oks.disableSSOWhenAuthorize();
        LatteLogger.d("title", mTitle);
        LatteLogger.d("iconUrl", getShareIcon(mUrl));
        LatteLogger.d("url", mUrl);
        oks.setTitle(mTitle);
        oks.setImageUrl(getShareIcon(mUrl));
        oks.setUrl(mUrl);
        oks.show(getContext());
    }


    public static RecordDetailDelegate create(String url, boolean isStar){
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        bundle.putBoolean("isStar", isStar);
        RecordDetailDelegate delegate = new RecordDetailDelegate();
        delegate.setArguments(bundle);
        return delegate;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        mUrl = args.getString("url");
        mIsStar = args.getBoolean("isStar");
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_record_detail;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        mToolbar.inflateMenu(R.menu.record_detail_menu);
        mToolbar.setContentInsetsAbsolute(0, 0);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.record_detail_action_refresh){
                    mDelegate.getWebView().loadUrl(mUrl);
                }
                if (item.getItemId() == R.id.record_detail_action_delete){
                    // TODO: 2017/10/16 删除记录请求
                    Toast.makeText(getContext(), "删除成功", Toast.LENGTH_LONG).show();
                    getSupportDelegate().pop();
                }
                return false;
            }
        });
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        if (mIsStar){
            mStarIconTextView.setTextColor(Color.parseColor("#f4ea2a"));
            mStarIconTextView.setTag(true);
        } else{
            mStarIconTextView.setTextColor(Color.WHITE);
            mStarIconTextView.setTag(false);
        }
        mDelegate = WebDelegateImpl.create(mUrl);
        mDelegate.setWebChromeClient(initWebChromeClient());
        mDelegate.setTopDelegate(this.getParentDelegate());
        getSupportDelegate().loadRootFragment(R.id.web_re_detail_container, mDelegate);
    }

    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        return new DefaultHorizontalAnimator();
    }


    private WebChromeClientImpl initWebChromeClient(){
        WebChromeClientImpl webChromeClient = new WebChromeClientImpl(){
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                mTextView.setText(title);
                mTitle = title;
            }

            @Override
            public void onReceivedTouchIconUrl(WebView view, String url, boolean precomposed) {
                super.onReceivedTouchIconUrl(view, url, precomposed);
                LatteLogger.d("webViewIcon", url);
                mIconUrl = url;
            }
        };
        return webChromeClient;
    }

    private String getShareIcon(String url){
        if (url.contains("mp.weixin.qq.com")){
            return "http://oxut2e6if.bkt.clouddn.com/weixin.png";
        } else if(mIconUrl == null){
            return "http://oxut2e6if.bkt.clouddn.com/icecream-01.png";
        }
        else{
            return mIconUrl;
        }
    }

}
