package cn.bravedawn.latte.ec.main.star;

import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.ViewStubCompat;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import cn.bravedawn.latte.app.Latte;
import cn.bravedawn.latte.delegates.bottom.BottomItemDelegate;
import cn.bravedawn.latte.ec.R;
import cn.bravedawn.latte.ec.R2;
import cn.bravedawn.latte.ec.main.index.IndexDataAdapter;
import cn.bravedawn.latte.ec.main.index.IndexDataConverter;
import cn.bravedawn.latte.ec.main.index.IndexItemClickListener;
import cn.bravedawn.latte.net.RestClient;
import cn.bravedawn.latte.net.callback.IFailure;
import cn.bravedawn.latte.net.callback.ISuccess;
import cn.bravedawn.latte.ui.loader.LoaderStyle;
import cn.bravedawn.latte.ui.recycler.BaseDecoration;
import cn.bravedawn.latte.ui.recycler.MultipleItemEntity;
import cn.bravedawn.latte.util.log.LatteLogger;
import cn.bravedawn.latte.util.net.NetWorkUtils;
import cn.bravedawn.latte.util.storage.LattePreference;

/**
 * Created by 冯晓 on 2017/10/15.
 */

public class StarDelegate extends BottomItemDelegate implements ISuccess{

    @BindView(R2.id.rv_star)
    RecyclerView mRecyclerView = null;

    @BindView(R2.id.tb_star)
    Toolbar mToolbar = null;

    @BindView(R2.id.star_stun_no_item)
    ViewStubCompat mStubCompat = null;

    private IndexDataAdapter mAdapter = null;

    private String MODIFY_CHANNEL_URL = Latte.getApplicationContext().getString(R.string.modify_channel);

    private View studView = null;

    private Integer mCount = -1;

    private boolean IS_FIRST_LOAD = false;

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
        return R.layout.deleage_star;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {

    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        initViewByData();
        initSwipeView();
    }

    public void initViewByData(){
        RestClient.builder()
                .url("record/star/" + LattePreference.getCustomAppProfile("userId"))
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
    public void onSuccess(String response) {
        LatteLogger.d("ChannelDetailDelegate", response);
        final JSONObject data = JSON.parseObject(response);
        mCount = data.getInteger("total");
        final ArrayList<MultipleItemEntity> dataList = new IndexDataConverter()
                .setJsonData(response).convert();
        mAdapter = new IndexDataAdapter(dataList);
        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mAdapter);
        if (!IS_FIRST_LOAD){
            mRecyclerView.addItemDecoration(BaseDecoration.create(
                    ContextCompat.getColor(getContext(), R.color.app_background), 15));
        }
        if (IS_FIRST_LOAD){
            mRecyclerView.removeOnItemTouchListener(StarItemClickListener.create(this));
        }
        mRecyclerView.addOnItemTouchListener(StarItemClickListener.create(this));
        mAdapter.bindToRecyclerView(mRecyclerView);
        checkNetConnect();
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

    private void checkNetConnect(){
        if (!IS_FIRST_LOAD){
            studView = mStubCompat.inflate();
        }
        if (!NetWorkUtils.isNetworkConnected(getContext()) || mCount == 0){
            final RelativeLayout tvShow = (RelativeLayout) studView.findViewById(R.id.stud_connect);
            final AppCompatTextView tvStudText = (AppCompatTextView)studView.findViewById(R.id.stud_connect_text);
            if (mCount != 0){
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
            } else{
                tvStudText.setText("快将你喜欢的项目加入集锦菜单吧");
            }

            mStubCompat.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        } else{
            mRecyclerView.setVisibility(View.VISIBLE);
            mStubCompat.setVisibility(View.GONE);
        }
        IS_FIRST_LOAD = true;
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        if (IS_FIRST_LOAD){
            initViewByData();
        }
    }
}
