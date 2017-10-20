package cn.bravedawn.latte.ec.main.index;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.View;
import android.view.animation.Animation;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.joanzapata.iconify.widget.IconTextView;

import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bravedawn.latte.app.ConfigKeys;
import cn.bravedawn.latte.app.Latte;
import cn.bravedawn.latte.delegates.bottom.BottomItemDelegate;
import cn.bravedawn.latte.ec.R;
import cn.bravedawn.latte.ec.R2;
import cn.bravedawn.latte.ec.main.EcBottomDelegate;
import cn.bravedawn.latte.ec.main.index.search.SearchDelegate;
import cn.bravedawn.latte.ui.recycler.BaseDecoration;
import cn.bravedawn.latte.ui.refresh.PagingBean;
import cn.bravedawn.latte.util.callback.CallBackManager;
import cn.bravedawn.latte.util.callback.CallBackType;
import cn.bravedawn.latte.util.callback.IGlobalCallback;
import cn.bravedawn.latte.util.clipboard.ClipboardUtil;
import cn.bravedawn.latte.util.log.LatteLogger;
import cn.bravedawn.latte.util.storage.LattePreference;
import qiu.niorgai.StatusBarCompat;

/**
 * Created by 冯晓 on 2017/9/24.
 */

public class IndexDelegate extends BottomItemDelegate implements
        View.OnFocusChangeListener, ClipboardUtil.OnPrimaryClipChangedListener {

    @BindView(R2.id.rv_index)
    RecyclerView mRecyclerView = null;

    @BindView(R2.id.srl_index)
    SwipeRefreshLayout mRefreshLayout = null;

    @BindView(R2.id.tb_index)
    Toolbar mToolbar = null;

    @BindView(R2.id.icon_index_scan)
    IconTextView mIconTextView = null;

    @BindView(R2.id.et_search_view)
    AppCompatEditText mSearchView = null;

    @OnClick(R2.id.icon_index_scan)
    void onClickSanQrCode() {
        startScanWithCheck(this.getParentDelegate());
    }

    @BindView(R2.id.design_bottom_sheet)
    RelativeLayout mRelativeLayout = null;

    @BindView(R2.id.bottomsheet_text)
    AppCompatTextView mBottomSheet_text = null;

    @OnClick(R2.id.add_to_list)
    void onClickAdd(){
        mRefreshHandler.spider(URL);
        mRelativeLayout.setVisibility(View.GONE);
        mClipboard.clearClip();
    }

    private RefreshHandler mRefreshHandler = null;

    private ClipboardUtil mClipboard;

    private String URL = null;

    private String MODIFY_CHANNEL_URL = Latte.getApplicationContext().getString(R.string.modify_channel);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompat.setStatusBarColor((Activity) Latte.getConfiguration(ConfigKeys.ACTIVITY),
                ContextCompat.getColor(getContext(), R.color.colorPrimary));
        //ClipboardUtil在Application的onCreate中调用init初始化
        // 剪切板
        ClipboardUtil.init(Latte.getApplicationContext());
        mClipboard = ClipboardUtil.getInstance();
        mClipboard.addOnPrimaryClipChangedListener(this);
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
        mRefreshHandler = RefreshHandler.create(mRefreshLayout, mRecyclerView,
                new IndexDataConverter(), new PagingBean());
        CallBackManager.getInstance()
                .addCallBack(CallBackType.ON_SCAN, new IGlobalCallback<String>() {
                    @Override
                    public void executeCallBack(@Nullable String args) {
                        Toast.makeText(getContext(), args, Toast.LENGTH_LONG).show();
                    }
                });
        mSearchView.setOnFocusChangeListener(this);

    }


    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        initRefreshLayout();
        initRecyclerView();
        mRefreshHandler.firstPage("record/"+ LattePreference.getCustomAppProfile("userId"));
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_index;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus){
            getParentDelegate().getSupportDelegate().start(new SearchDelegate());
        }
    }

    @Override
    public void onPrimaryClipChanged(ClipboardManager clipboardManager) {
        if (mClipboard.hasPrimaryClip()){
            if (Patterns.WEB_URL.matcher(mClipboard.getClipText()).matches()){
                URL = mClipboard.getClipText();
                if (URL != null){
                    mRelativeLayout.setVisibility(View.VISIBLE);
                    mBottomSheet_text.setText(URL);
                    LatteLogger.d("onPrimaryClipChanged", URL);
                }
            }
        }
    }
    private void initRefreshLayout() {
        mRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
        );
        mRefreshLayout.setProgressViewOffset(true, 20, 120);
    }

    private void initRecyclerView() {
        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.addItemDecoration(BaseDecoration.create(
                ContextCompat.getColor(getContext(), R.color.app_background), 3));
        mRecyclerView.addOnItemTouchListener(IndexItemClickListener
                .create(this, MODIFY_CHANNEL_URL));

        //是否显示添加菜单
        isAddRecord();
    }

    private void isAddRecord(){
        if (mClipboard.hasPrimaryClip()){
            if (Patterns.WEB_URL.matcher(mClipboard.getClipText()).matches()){
                URL = mClipboard.getClipText();
                if (URL != null){
                    mRelativeLayout.setVisibility(View.VISIBLE);
                    mBottomSheet_text.setText(URL);
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        isAddRecord();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mClipboard.removeOnPrimaryClipChangedListener(this);
    }

}
