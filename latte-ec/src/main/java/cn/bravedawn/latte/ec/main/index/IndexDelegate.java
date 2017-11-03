package cn.bravedawn.latte.ec.main.index;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.app.NotificationCompat;
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
import cn.bravedawn.latte.delegates.web.WebDelegateImpl;
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
import cn.bravedawn.latte.util.net.NetWorkUtils;
import cn.bravedawn.latte.util.storage.LattePreference;
import qiu.niorgai.StatusBarCompat;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by 冯晓 on 2017/9/24.
 */

public class IndexDelegate extends BottomItemDelegate implements ClipboardUtil.OnPrimaryClipChangedListener {

    @BindView(R2.id.rv_index)
    RecyclerView mRecyclerView = null;

    @BindView(R2.id.srl_index)
    SwipeRefreshLayout mRefreshLayout = null;

    @BindView(R2.id.tb_index)
    Toolbar mToolbar = null;

    @BindView(R2.id.icon_index_scan)
    IconTextView mIconTextView = null;

    @OnClick(R2.id.icon_index_scan)
    void onClickSanQrCode() {
        startScanWithCheck(this.getParentDelegate());
    }

    @BindView(R2.id.design_bottom_sheet)
    RelativeLayout mRelativeLayout = null;

    @BindView(R2.id.bottomsheet_text)
    AppCompatTextView mBottomSheet_text = null;

    @OnClick(R2.id.icon_index_search)
    void onClickSearch(){
        getParentDelegate().getSupportDelegate().start(new SearchDelegate());
    }

    @OnClick(R2.id.add_to_list)
    void onClickAdd(){
        mRefreshHandler.spider(URL);
        mRelativeLayout.setVisibility(View.GONE);
        mClipboard.clearClip();
    }

    private RefreshHandler mRefreshHandler = null;
    private LocalHandler mLocalHandler = null;

    private ClipboardUtil mClipboard;

    private String URL = null;

    private IndexItemClickListener mIndexItemClickListener = null;

    private boolean IS_FIRST_LOAD = false;

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
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(args));
                        startActivity(intent);
                    }
                });

    }


    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        if (NetWorkUtils.isNetworkConnected(getContext())){
            initRefreshLayout();
            initRecyclerView();
            final EcBottomDelegate ecBottomDelegate = getParentDelegate();
            mIndexItemClickListener = (IndexItemClickListener) IndexItemClickListener.create(ecBottomDelegate);
            mRecyclerView.addOnItemTouchListener(mIndexItemClickListener);
            //是否显示添加菜单
            isAddRecord();
            IS_FIRST_LOAD = true;
            mRefreshHandler.firstPage("record/"+ LattePreference.getCustomAppProfile("userId"));
        } else{
            mRefreshLayout.setEnabled(false);
            initRecyclerView();
            final EcBottomDelegate ecBottomDelegate = getParentDelegate();
            mLocalHandler = LocalHandler.create(mRecyclerView, new IndexDataConverter(), ecBottomDelegate);
            mLocalHandler.firstLocalLoad();
        }

    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_index;
    }

    @Override
    public void onPrimaryClipChanged(ClipboardManager clipboardManager) {
        if (mClipboard.hasPrimaryClip()){
            if (Patterns.WEB_URL.matcher(mClipboard.getClipText()).matches()){
                URL = mClipboard.getClipText();
                if (URL != null){
                    mRelativeLayout.setVisibility(View.VISIBLE);
                    mBottomSheet_text.setText(URL);
                    LatteLogger.d("URL", URL);
                    notificationSet(URL);
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

    private void notificationSet(String url){
        NotificationManager mNotificationManager = (NotificationManager) _mActivity
                .getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getContext());
        final Intent intent = new Intent(getContext(), Latte.getConfiguration(ConfigKeys.ACTIVITY).getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent contentIntent = PendingIntent.getActivity(getContext(), 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        mBuilder.setContentTitle("是否将链接添加至口袋？")
                .setContentText(url)
                .setTicker("通知来啦！")
                .setFullScreenIntent(contentIntent, true)
                .setContentIntent(contentIntent)
                .setWhen(System.currentTimeMillis())
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setDefaults(Notification.DEFAULT_ALL)
                .setSmallIcon(R.mipmap.logo)
                .setAutoCancel(true)
                .setVisibility(Notification.VISIBILITY_PUBLIC);


        LatteLogger.d("notification");
        mNotificationManager.notify(1, mBuilder.build());
    }


    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        if (NetWorkUtils.isNetworkConnected(getContext()) && IS_FIRST_LOAD){
            mIndexItemClickListener.getChannel();
        }
    }
}
