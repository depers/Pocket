package cn.bravedawn.latte.ec.main.channel;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.ViewStubCompat;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bravedawn.latte.app.Latte;
import cn.bravedawn.latte.delegates.bottom.BottomItemDelegate;
import cn.bravedawn.latte.ec.R;
import cn.bravedawn.latte.ec.R2;
import cn.bravedawn.latte.ec.main.EcBottomDelegate;
import cn.bravedawn.latte.ec.main.channel.add.AddChannelDelegate;
import cn.bravedawn.latte.net.RestClient;
import cn.bravedawn.latte.net.callback.IFailure;
import cn.bravedawn.latte.net.callback.ISuccess;
import cn.bravedawn.latte.ui.loader.LoaderStyle;
import cn.bravedawn.latte.ui.recycler.MultipleItemEntity;
import cn.bravedawn.latte.util.log.LatteLogger;
import cn.bravedawn.latte.util.net.NetWorkUtils;
import cn.bravedawn.latte.util.storage.LattePreference;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by 冯晓 on 2017/10/10.
 */

public class ChannelDelegate extends BottomItemDelegate implements ISuccess{

    private ChannelAdapter mAdapter = null;

    @BindView(R2.id.rv_channel)
    RecyclerView mRecyclerView = null;

    @BindView(R2.id.tb_channel)
    Toolbar mToolbar;

    @BindView(R2.id.channel_bar_title)
    AppCompatTextView mTextViewTitle = null;

    @OnClick(R2.id.add_channel_fab)
    void onclickAddChannel(){
        getParentDelegate().getSupportDelegate().start(new AddChannelDelegate());
    }

    @BindView(R2.id.stun_no_item)
    ViewStubCompat mViewStubCompat = null;

    private View studView = null;
    private boolean IS_FIRST_LOAD = false;
    private Integer mCount = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mToolbar.inflateMenu(R.menu.channel_menu);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.channel_action_refresh){
                    initViewByData();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public Object setLayout() {
        return R.layout.deleage_channel;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {

    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        initViewByData();
    }

    @Override
    public void onSuccess(String response) {
        LatteLogger.d("channel_response", response);
        final JSONObject channls = JSON.parseObject(response);
        mCount = channls.getInteger("total");
        mTextViewTitle.setText("分类("+mCount+")");
        final ArrayList<MultipleItemEntity> data =
                new ChannelConverter()
                        .setJsonData(response)
                        .convert();
        mAdapter = new ChannelAdapter(data);
        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mAdapter);
        if (IS_FIRST_LOAD){
            mRecyclerView.removeOnItemTouchListener(ChannelItemClickListener.create(this));
        }
        mRecyclerView.addOnItemTouchListener(ChannelItemClickListener.create(this));
        checkNetConnect();
    }

    public void initViewByData(){
        RestClient.builder()
                .url("channel/" + LattePreference.getCustomAppProfile("userId"))
                .loader(getContext(), LoaderStyle.LineScaleIndicator)
                .success(this)
                .failure(new IFailure() {
                    @Override
                    public void onFailure() {
                        checkNetConnect();
                    }
                })
                .build()
                .get();
    }


    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        if (IS_FIRST_LOAD){
            initViewByData();
        }
    }

    private void checkNetConnect(){
        if (!IS_FIRST_LOAD){
            studView = mViewStubCompat.inflate();
        }
        if (!NetWorkUtils.isNetworkConnected(getContext()) || mCount == 0){
            mTextViewTitle.setText("分类");
            final RelativeLayout tvShow = (RelativeLayout) studView.findViewById(R.id.stud_connect);
            tvShow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!NetWorkUtils.isNetworkConnected(getContext())){
                        Toast.makeText(getContext(), "请稍后重试", Toast.LENGTH_LONG).show();
                    } else{
                        initViewByData();
                    }
                }
            });
            mViewStubCompat.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        } else{
            mRecyclerView.setVisibility(View.VISIBLE);
            mViewStubCompat.setVisibility(View.GONE);
        }
        IS_FIRST_LOAD = true;
    }

    public ChannelAdapter getAdapter(){
        return mAdapter;
    }
}
