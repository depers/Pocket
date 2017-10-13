package cn.bravedawn.latte.ec.main.channel;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bravedawn.latte.app.ConfigKeys;
import cn.bravedawn.latte.app.Latte;
import cn.bravedawn.latte.delegates.LatteDelegate;
import cn.bravedawn.latte.delegates.bottom.BottomItemDelegate;
import cn.bravedawn.latte.ec.R;
import cn.bravedawn.latte.ec.R2;
import cn.bravedawn.latte.ec.main.channel.addchannel.AddChannelDelegate;
import cn.bravedawn.latte.net.RestClient;
import cn.bravedawn.latte.net.callback.ISuccess;
import cn.bravedawn.latte.ui.loader.LoaderStyle;
import cn.bravedawn.latte.ui.recycler.MultipleItemEntity;
import cn.bravedawn.latte.util.log.LatteLogger;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        LatteLogger.d("onCreateView", "****************************");
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
        final int count = channls.getInteger("size");
        mTextViewTitle.setText("分类("+count+")");
        final ArrayList<MultipleItemEntity> data =
                new ChannelConverter()
                        .setJsonData(response)
                        .convert();
        mAdapter = new ChannelAdapter(data);
        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initViewByData(){
        RestClient.builder()
                .url("user_channel")
                .loader(getContext(),  LoaderStyle.LineScaleIndicator)
                .success(this)
                .build()
                .get();
    }


    @Override
    public void onResume() {
        super.onResume();
        LatteLogger.d("onResume()", "*****************************");
    }

    @Override
    public void onPause() {
        super.onPause();
        LatteLogger.d("onPause()", "*****************************");
    }

}
