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
import cn.bravedawn.latte.app.Latte;
import cn.bravedawn.latte.delegates.LatteDelegate;
import cn.bravedawn.latte.delegates.web.WebDelegateImpl;
import cn.bravedawn.latte.delegates.web.chromeClient.WebChromeClientImpl;
import cn.bravedawn.latte.ec.R;
import cn.bravedawn.latte.ec.R2;
import cn.bravedawn.latte.net.RestClient;
import cn.bravedawn.latte.net.callback.ISuccess;
import cn.bravedawn.latte.ui.loader.LatteLoader;
import cn.bravedawn.latte.ui.loader.LoaderStyle;
import cn.bravedawn.latte.util.log.LatteLogger;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
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
    private Integer mId = null;
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
            deleteStar(mId);
        } else{
            mStarIconTextView.setTextColor(Color.parseColor("#f4ea2a"));
            mStarIconTextView.setTag(true);
            addStar(mId);
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


    public static RecordDetailDelegate create(String url, boolean isStar, Integer id){
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        bundle.putBoolean("isStar", isStar);
        bundle.putInt("id", id);
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
        mId = args.getInt("id");
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
                    delete(mId);
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

    private void addStar(final int id){
        LatteLoader.showLoading(getContext(), LoaderStyle.LineScaleIndicator);
        Observable.just("record/star/"+id)
                .map(new Function<String, Boolean>() {
                    @Override
                    public Boolean apply(@io.reactivex.annotations.NonNull String s) throws Exception {
                        RestClient.builder()
                                .url(s)
                                .success(new ISuccess() {
                                    @Override
                                    public void onSuccess(String response) {
                                        LatteLogger.d("addStar", response);
                                    }
                                })
                                .build()
                                .post();
                        return true;

                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Boolean aBoolean) throws Exception {
                        LatteLoader.stopLoading();
                        if (aBoolean) {
                            Toast.makeText(Latte.getApplicationContext(), "已添加该项目到集锦菜单！", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


    private void deleteStar(final int id){
        LatteLoader.showLoading(getContext(), LoaderStyle.LineScaleIndicator);
        Observable.just("record/star/"+id)
                .map(new Function<String, Boolean>() {
                    @Override
                    public Boolean apply(@io.reactivex.annotations.NonNull String s) throws Exception {
                        RestClient.builder()
                                .url(s)
                                .success(new ISuccess() {
                                    @Override
                                    public void onSuccess(String response) {
                                        LatteLogger.d("addStar", response);
                                    }
                                })
                                .build()
                                .delete();
                        return true;

                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Boolean aBoolean) throws Exception {
                        LatteLoader.stopLoading();
                        if (aBoolean) {
                            Toast.makeText(Latte.getApplicationContext(), "该项目已从集锦菜单中移除！", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void delete(final int id){
        LatteLoader.showLoading(getContext(), LoaderStyle.LineScaleIndicator);
        Observable.just("record/"+id)
                .map(new Function<String, Boolean>() {
                    @Override
                    public Boolean apply(@io.reactivex.annotations.NonNull String s) throws Exception {
                        final boolean flag;
                        RestClient.builder()
                                .url(s)
                                .success(new ISuccess() {
                                    @Override
                                    public void onSuccess(String response) {
                                        LatteLogger.d("delete", response);
                                    }
                                })
                                .build()
                                .delete();
                        return true;

                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Boolean aBoolean) throws Exception {
                        LatteLoader.stopLoading();
                        if (aBoolean) {
                            Toast.makeText(Latte.getApplicationContext(), "删除成功", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(Latte.getApplicationContext(), "删除失败", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
