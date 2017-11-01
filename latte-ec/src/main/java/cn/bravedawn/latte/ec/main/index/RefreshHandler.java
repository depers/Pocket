package cn.bravedawn.latte.ec.main.index;

import android.graphics.Canvas;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.chad.library.adapter.base.listener.OnItemSwipeListener;

import java.util.WeakHashMap;

import cn.bravedawn.latte.app.Latte;
import cn.bravedawn.latte.ec.R;
import cn.bravedawn.latte.ec.Spider.SpiderUtil;
import cn.bravedawn.latte.ec.bean.Record;
import cn.bravedawn.latte.ec.database.DatabaseManager;
import cn.bravedawn.latte.ec.database.RecordProfile;
import cn.bravedawn.latte.net.RestClient;
import cn.bravedawn.latte.net.callback.ISuccess;
import cn.bravedawn.latte.ui.loader.LatteLoader;
import cn.bravedawn.latte.ui.loader.LoaderStyle;
import cn.bravedawn.latte.ui.recycler.DataConverter;
import cn.bravedawn.latte.ui.recycler.MultipleFields;
import cn.bravedawn.latte.ui.recycler.MultipleItemEntity;
import cn.bravedawn.latte.ui.refresh.PagingBean;
import cn.bravedawn.latte.util.log.LatteLogger;
import cn.bravedawn.latte.util.storage.LattePreference;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.http.DELETE;

/**
 * Created by 冯晓 on 2017/9/24.
 */

public class RefreshHandler implements
        SwipeRefreshLayout.OnRefreshListener,
        BaseQuickAdapter.RequestLoadMoreListener {

    private final SwipeRefreshLayout REFRESH_LAYOUT;
    private final PagingBean BEAN;
    private final RecyclerView RECYCLERVIEW;
    private IndexDataAdapter mAdapter = null;
    private final DataConverter CONVERTER;


    private RefreshHandler(SwipeRefreshLayout refreshLayout,
                           RecyclerView recyclerView,
                           DataConverter converter,
                           PagingBean pagingBean) {
        this.REFRESH_LAYOUT = refreshLayout;
        this.BEAN = pagingBean;
        this.RECYCLERVIEW = recyclerView;
        this.CONVERTER = converter;
        REFRESH_LAYOUT.setOnRefreshListener(this);
        initSwipeView();
    }

    public static RefreshHandler create(SwipeRefreshLayout refreshLayout,
                                        RecyclerView recyclerView,
                                        DataConverter converter,
                                        PagingBean pagingBean) {
        return new RefreshHandler(refreshLayout, recyclerView, converter, pagingBean);
    }

    public void refresh() {
        REFRESH_LAYOUT.setRefreshing(true);
        Latte.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                BEAN.setPageIndex(0);
                firstPage("record/"+ LattePreference.getCustomAppProfile("userId"));
                REFRESH_LAYOUT.setRefreshing(false);
                mAdapter.notifyDataSetChanged();
            }
        }, 2000);
    }

    public void firstPage(String url) {
        BEAN.setDelayed(1000);
        RestClient.builder()
                .url(url)
                .loader(RECYCLERVIEW.getContext(), LoaderStyle.LineScaleIndicator)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        //LatteLogger.d("response", response);
                        final JSONObject object = JSON.parseObject(response);
                        BEAN.setTotal(object.getInteger("total"))
                                .setPageSize(object.getInteger("page_size"))
                                .setTotalPage(object.getInteger("total_page"));
                        // 设置Adapter
                        CONVERTER.clearData();
                        mAdapter = new IndexDataAdapter(CONVERTER.setJsonData(response).convert());
                        mAdapter.setOnLoadMoreListener(RefreshHandler.this, RECYCLERVIEW);
                        RECYCLERVIEW.setAdapter(mAdapter);
                        BEAN.addIndex();
                    }
                })
                .build()
                .get();
    }


    private void paging(final String url) {
        final int pageSize = BEAN.getPageSize();
        final int totalPage = BEAN.getTotalPage();
        final int total = BEAN.getTotal();
        final int index = BEAN.getPageIndex();

        if (mAdapter.getData().size() >= total  || index > totalPage) {
            mAdapter.loadMoreEnd();
        } else {
            Latte.getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    RestClient.builder()
                            .url(url + "/?page=" + index)
                            .loader(RECYCLERVIEW.getContext(), LoaderStyle.LineScaleIndicator)
                            .success(new ISuccess() {
                                @Override
                                public void onSuccess(String response) {
                                    //LatteLogger.d("onLoadMoreRequested", response);
                                    CONVERTER.clearData();
                                    mAdapter.addData(CONVERTER.setJsonData(response).convert());
                                    // 累加数量
                                    mAdapter.loadMoreComplete();
                                    BEAN.addIndex();
                                }
                            })
                            .build()
                            .get();
                }
            }, 1000);
        }
    }


    @Override
    public void onRefresh() {
        refresh();
    }


    @Override
    public void onLoadMoreRequested() {
        paging("record/"+ LattePreference.getCustomAppProfile("userId"));
    }

    private void initSwipeView(){
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                LatteLogger.d("getLayoutPosition", viewHolder.getLayoutPosition());
                LatteLogger.d("getAdapterPosition", viewHolder.getAdapterPosition());
                MultipleItemEntity entity = mAdapter.getData().get(viewHolder.getAdapterPosition());
                mAdapter.remove(viewHolder.getLayoutPosition());
                int recordId = entity.getField(MultipleFields.ID);
                delete(recordId);
            }


            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                getDefaultUIUtil().onDraw(c, RECYCLERVIEW,
                        viewHolder.itemView.findViewById(R.id.layoutRecordItemMain), dX, dY, actionState, isCurrentlyActive);
                if (dX > 0){
                    viewHolder.itemView.findViewById(R.id.layoutRecordItem)
                            .setBackgroundColor(ContextCompat.getColor(RECYCLERVIEW.getContext(), R.color.deep_orange));
                    viewHolder.itemView.findViewById(R.id.imageViewRemove).setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                getDefaultUIUtil().clearView(viewHolder.itemView.findViewById(R.id.layoutRecordItemMain));
            }
        });
        itemTouchHelper.attachToRecyclerView(RECYCLERVIEW);
    }

    public void spider(final String url) {
        LatteLogger.d("URL", url);
        final WeakHashMap<String, Object> info = new WeakHashMap<>();
        Observable.just(url)
                .map(new Function<String, Record>() {
                    @Override
                    public Record apply(@NonNull String s) throws Exception {
                        return SpiderUtil.getRecordFromSpider(s);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Record>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull Record record) {
                        //remote
                        info.put("title", record.getTitle());
                        info.put("colorAvatar", record.getColorAvatar());
                        info.put("url", record.getUrl());
                        info.put("userId", Integer.parseInt(LattePreference.getCustomAppProfile("userId")));
                        RestClient.builder()
                                .url("record")
                                .params(info)
                                .loader(RECYCLERVIEW.getContext(), LoaderStyle.LineScaleIndicator)
                                .success(new ISuccess() {
                                    @Override
                                    public void onSuccess(String response) {
                                        LatteLogger.d("add", response);
                                        CONVERTER.clearData();
                                        if (mAdapter == null){
                                            mAdapter = new IndexDataAdapter(CONVERTER.setJsonData(response).convert());
                                            RECYCLERVIEW.setAdapter(mAdapter);
                                        } else{
                                            mAdapter.addData(0, CONVERTER.setJsonData(response).convert());
                                            JSONObject jsonObject = JSON.parseObject(response)
                                                    .getJSONArray("data").getJSONObject(0);
                                            final long id = jsonObject.getInteger("id");
                                            final String url = jsonObject.getString("url");
                                            final String title = jsonObject.getString("title");
                                            final String colorAvatar = jsonObject.getString("colorAvatar");
                                            final String resource = jsonObject.getString("resource");
                                            final boolean isStar = jsonObject.getBoolean("mstar");

                                            final RecordProfile recordProfile = new RecordProfile(id, url,
                                                    title, colorAvatar, resource, "", isStar);
                                           DatabaseManager.getInstance().getRecordDao().insert(recordProfile);
                                        }
                                        // 累加数量
                                        LatteLogger.d("size", mAdapter.getData().size());
                                        RECYCLERVIEW.scrollToPosition(0);
                                    }
                                })
                                .build()
                                .post();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        Toast.makeText(Latte.getApplicationContext(), "爬取完成", Toast.LENGTH_LONG).show();
                    }
                });

    }

    private void delete(final int id){
        LatteLoader.showLoading(RECYCLERVIEW.getContext(), LoaderStyle.LineScaleIndicator);
        Observable.just("record/"+id)
                .map(new Function<String, Boolean>() {
                    @Override
                    public Boolean apply(@NonNull String s) throws Exception {
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
                    public void accept(@NonNull Boolean aBoolean) throws Exception {
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
