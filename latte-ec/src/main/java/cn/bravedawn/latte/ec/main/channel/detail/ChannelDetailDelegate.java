package cn.bravedawn.latte.ec.main.channel.detail;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.ViewStubCompat;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.joanzapata.iconify.widget.IconTextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bravedawn.latte.app.Latte;
import cn.bravedawn.latte.delegates.LatteDelegate;
import cn.bravedawn.latte.delegates.bottom.BottomItemDelegate;
import cn.bravedawn.latte.ec.R;
import cn.bravedawn.latte.ec.R2;
import cn.bravedawn.latte.ec.main.EcBottomDelegate;
import cn.bravedawn.latte.ec.main.channel.add.AddChannelDelegate;
import cn.bravedawn.latte.ec.main.index.IndexDataAdapter;
import cn.bravedawn.latte.ec.main.index.IndexDataConverter;
import cn.bravedawn.latte.ec.main.index.IndexDelegate;
import cn.bravedawn.latte.ec.main.index.IndexItemClickListener;
import cn.bravedawn.latte.net.RestClient;
import cn.bravedawn.latte.net.callback.ISuccess;
import cn.bravedawn.latte.ui.loader.LoaderStyle;
import cn.bravedawn.latte.ui.recycler.BaseDecoration;
import cn.bravedawn.latte.ui.recycler.MultipleItemEntity;
import cn.bravedawn.latte.util.log.LatteLogger;
import cn.bravedawn.latte.util.net.NetWorkUtils;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

/**
 * Created by 冯晓 on 2017/10/14.
 */

public class ChannelDetailDelegate extends LatteDelegate implements ISuccess,
        AppBarLayout.OnOffsetChangedListener {

    @BindView(R2.id.rv_channel_detail)
    RecyclerView mRecyclerView = null;

    @BindView(R2.id.channel_detail_toolbar)
    Toolbar mToolbar = null;

    @OnClick(R2.id.icon_channel_back)
    void onClickBack(){
        getSupportDelegate().pop();
    }

    @BindView(R2.id.channel_detail_bar_image)
    AppCompatImageView mImageView = null;

    @BindView(R2.id.channel_appbar)
    AppBarLayout mAppBarLayout = null;

    @BindView(R2.id.channel_collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingToolbar = null;

    @BindView(R2.id.tv_channel_title_text)
    AppCompatTextView mTitleTextView = null;

    @BindView(R2.id.detail_stun_no_item)
    ViewStubCompat mViewStubCompat = null;

    @BindView(R2.id.channel_detail_bar_count)
    AppCompatTextView mChannelCount = null;

    @BindView(R2.id.channel_detail_bar_desc)
    AppCompatTextView mChannelDesc = null;

    private IndexDataAdapter mAdapter = null;

    private Integer mChannelId = null;

    private Integer mCount = null;

    private boolean IS_FIRST_LOAD = false;

    private View studView = null;

    public static ChannelDetailDelegate create(Integer id){
        Bundle bundle = new Bundle();
        bundle.putInt("channelId", id);
        ChannelDetailDelegate detailDelegate = new ChannelDetailDelegate();
        detailDelegate.setArguments(bundle);
        return detailDelegate;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle args = getArguments();
        mChannelId = args.getInt("channelId");
        LatteLogger.d("Channel_id", mChannelId);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mToolbar.setContentInsetsAbsolute(0, 0);
        mToolbar.inflateMenu(R.menu.channel_detail_menu);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.channel_detail_action_refresh){
                    initData();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_channel_detail;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        mAppBarLayout.addOnOffsetChangedListener(this);
        mCollapsingToolbar.setContentScrimColor(ContextCompat.getColor(getContext(), R.color.app_main));
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        initData();
        initSwipeView();
        mRecyclerView.addItemDecoration(BaseDecoration.create(
                ContextCompat.getColor(getContext(), R.color.app_background), 10));
    }


    private void initData(){
        RestClient.builder()
                .url("channel/detail/"+mChannelId)
                .loader(getContext(), LoaderStyle.LineScaleIndicator)
                .success(this)
                .build()
                .get();
    }

    @Override
    public void onSuccess(String response) {
        LatteLogger.d("ChannelDetailDelegate", response);
        final JSONObject data = JSON.parseObject(response);
        final String title = data.getString("title");
        final String desc = data.getString("desc");
        final String image = data.getString("imageUrl");
        mCount = data.getInteger("total");
        mTitleTextView.setText(title);
        mChannelCount.setText(mCount.toString());
        mChannelDesc.setText(desc);
        Glide.with(getContext())
                .load(image)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .dontAnimate()
                .into(mImageView);
        final ArrayList<MultipleItemEntity> dataList = new IndexDataConverter()
                .setJsonData(response).convert();
        mAdapter = new IndexDataAdapter(dataList);
        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnItemTouchListener(IndexItemClickListener
                .create(this));
        mAdapter.bindToRecyclerView(mRecyclerView);
        checkCount();
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
    }

    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        return new DefaultHorizontalAnimator();
    }


    private void initSwipeView(){
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                mAdapter.remove(viewHolder.getLayoutPosition());
                Toast.makeText(Latte.getApplicationContext(), "删除成功", Toast.LENGTH_LONG).show();
            }


            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                getDefaultUIUtil().onDraw(c, mRecyclerView,
                        viewHolder.itemView.findViewById(R.id.layoutRecordItemMain), dX, dY, actionState, isCurrentlyActive);
                if (dX > 0){
                    viewHolder.itemView.findViewById(R.id.layoutRecordItem)
                            .setBackgroundColor(ContextCompat.getColor(mRecyclerView.getContext(), R.color.deep_orange));
                    viewHolder.itemView.findViewById(R.id.imageViewRemove).setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                getDefaultUIUtil().clearView(viewHolder.itemView.findViewById(R.id.layoutRecordItemMain));
            }
        });
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    private void checkCount(){
        if (!IS_FIRST_LOAD){
            studView = mViewStubCompat.inflate();
        }
        if (mCount == 0){
            final AppCompatTextView tvStudText = (AppCompatTextView)studView.findViewById(R.id.stud_connect_text);
            tvStudText.setText("快将你喜欢的项目加入该分组吧");
            mViewStubCompat.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        } else{
            mRecyclerView.setVisibility(View.VISIBLE);
            mViewStubCompat.setVisibility(View.GONE);
        }
        IS_FIRST_LOAD = true;
    }

}
