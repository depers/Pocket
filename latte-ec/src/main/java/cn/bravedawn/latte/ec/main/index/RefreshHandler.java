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
import cn.bravedawn.latte.net.RestClient;
import cn.bravedawn.latte.net.callback.ISuccess;
import cn.bravedawn.latte.ui.loader.LoaderStyle;
import cn.bravedawn.latte.ui.recycler.DataConverter;
import cn.bravedawn.latte.ui.refresh.PagingBean;
import cn.bravedawn.latte.util.log.LatteLogger;
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

    private void refresh() {
        REFRESH_LAYOUT.setRefreshing(true);
        Latte.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                    firstPage("user_record");
                REFRESH_LAYOUT.setRefreshing(false);
            }
        }, 2000);
    }

    // TODO: 2017/10/17 分页获取用户的记录 
    public void firstPage(String url) {
        BEAN.setDelayed(1000);
        RestClient.builder()
                .url(url)
                .loader(RECYCLERVIEW.getContext(), LoaderStyle.LineScaleIndicator)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        LatteLogger.d("response", response);
                        final JSONObject object = JSON.parseObject(response);
                        BEAN.setTotal(object.getInteger("total"))
                                .setPageSize(object.getInteger("page_size"));
                        // 设置Adapter
                        mAdapter = new IndexDataAdapter(CONVERTER.setJsonData(response).convert());
                        mAdapter.setOnLoadMoreListener(RefreshHandler.this, RECYCLERVIEW);
                        mAdapter.enableSwipeItem();
                        RECYCLERVIEW.setAdapter(mAdapter);
                        BEAN.addIndex();
                    }
                })
                .build()
                .get();
    }


    private void paging(final String url) {
        final int pageSize = BEAN.getPageSize();
        final int currentCount = BEAN.getCurrentCount();
        final int total = BEAN.getTotal();
        final int index = BEAN.getPageIndex();

        if (mAdapter.getData().size() < pageSize || currentCount >= total) {
            mAdapter.loadMoreEnd();
        } else {
            Latte.getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    RestClient.builder()
                            .url(url + index)
                            .loader(RECYCLERVIEW.getContext(), LoaderStyle.LineScaleIndicator)
                            .success(new ISuccess() {
                                @Override
                                public void onSuccess(String response) {
                                    LatteLogger.d("onLoadMoreRequested", response);
                                    CONVERTER.clearData();
                                    mAdapter.addData(CONVERTER.setJsonData(response).convert());
                                    // 累加数量
                                    BEAN.setCurrentCount(mAdapter.getData().size());
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
        paging("refresh");
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

    // TODO: 2017/10/10 这里是记录的新增请求
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
                        info.put("title", record.getTitle());
                        info.put("colorAvatar", record.getColorAvatar());
                        info.put("url", record.getUrl());
                        LatteLogger.d("spider_title", info.get("title"));
                        LatteLogger.d("spider_colorAvatar", info.get("colorAvatar"));
                        RestClient.builder()
                                .url("one_record")
                                .loader(RECYCLERVIEW.getContext(), LoaderStyle.LineScaleIndicator)
                                .success(new ISuccess() {
                                    @Override
                                    public void onSuccess(String response) {
                                        LatteLogger.d("add", response);
                                        CONVERTER.clearData();
                                        mAdapter.addData(0, CONVERTER.setJsonData(response).convert());
                                        // 累加数量
                                        BEAN.setCurrentCount(mAdapter.getData().size());
                                        LatteLogger.d("size", mAdapter.getData().size());
                                        RECYCLERVIEW.scrollToPosition(0);
                                    }
                                })
                                .build()
                                .get();
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

    // TODO: 2017/10/10 这里是记录的删除请求
    private void delete(final int pos){
        Observable.just("delete/")
                .map(new Function<String, Boolean>() {
                    @Override
                    public Boolean apply(@NonNull String s) throws Exception {
                        RestClient.builder()
                                .url("delete/")
                                .loader(RECYCLERVIEW.getContext(), LoaderStyle.LineScaleIndicator)
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
                        if (aBoolean) {
                            Toast.makeText(Latte.getApplicationContext(), "删除成功, "+pos, Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(Latte.getApplicationContext(), "删除失败", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

}
