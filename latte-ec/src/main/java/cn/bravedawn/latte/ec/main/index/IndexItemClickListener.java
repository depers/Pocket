package cn.bravedawn.latte.ec.main.index;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.chad.library.adapter.base.listener.SimpleClickListener;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

import cn.bravedawn.latte.app.Latte;
import cn.bravedawn.latte.delegates.LatteDelegate;
import cn.bravedawn.latte.delegates.web.WebDelegate;
import cn.bravedawn.latte.delegates.web.WebDelegateImpl;
import cn.bravedawn.latte.ec.R;
import cn.bravedawn.latte.ec.detail.RecordDetailDelegate;
import cn.bravedawn.latte.net.RestClient;
import cn.bravedawn.latte.net.callback.ISuccess;
import cn.bravedawn.latte.ui.recycler.MultipleFields;
import cn.bravedawn.latte.ui.recycler.MultipleItemEntity;
import cn.bravedawn.latte.util.log.LatteLogger;
import cn.bravedawn.latte.util.storage.LattePreference;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import me.yokeyword.fragmentation.ISupportFragment;

/**
 * Created by 冯晓 on 2017/9/25.
 */

public class IndexItemClickListener extends SimpleClickListener {

    private final LatteDelegate DELEGATE;

    private Integer mIndex;

    private Integer mPosition;

    private BaseQuickAdapter mAdapter;

    private final List<String> channelList = new ArrayList<>();
    private final List<Integer> channelIdList = new ArrayList<>();

    private IndexItemClickListener(LatteDelegate delegate) {
        this.DELEGATE = delegate;
        getChannel();
    }


    public static SimpleClickListener create(LatteDelegate delegate) {
        return new IndexItemClickListener(delegate);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

        final MultipleItemEntity entity = (MultipleItemEntity) adapter.getData().get(position);
        final String url = entity.getField(MultipleFields.URL);
        final boolean isStar = entity.getField(MultipleFields.BOOL);
        final Integer id = entity.getField(MultipleFields.ID);
        LatteLogger.d(entity.getField(MultipleFields.TITLE));
        final RecordDetailDelegate delegate = RecordDetailDelegate.create(url, isStar, id);
        DELEGATE.getSupportDelegate().start(delegate, ISupportFragment.SINGLETASK);
    }

    @Override
    public void onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
        mAdapter = adapter;
        mPosition = position;
        final MultipleItemEntity entity = (MultipleItemEntity) adapter.getData().get(position);
        final String channel = entity.getField(MultipleFields.NAME);
        final Integer id = entity.getField(MultipleFields.ID);
        mIndex = channelList.indexOf(channel);
        float dx = adapter.getViewByPosition(position, R.id.layoutRecordItemMain).getX();
        if (!(dx > 0) || view.isLongClickable()){
            Latte.getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    new MaterialDialog.Builder(DELEGATE.getContext())
                            .title(R.string.group_dialog)
                            .items(channelList)
                            .itemsCallbackSingleChoice(mIndex, new MaterialDialog.ListCallbackSingleChoice() {
                                @Override
                                public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                                    modifyChannel(which, id);
                                    return true;
                                }
                            })
                            .positiveText("确定")
                            .show();
                }
            }, 200);
        }
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
    }

    @Override
    public void onItemChildLongClick(BaseQuickAdapter adapter, View view, int position) {

    }

    private void modifyChannel(int pos, Integer recordId) {
        final Integer channelId = channelIdList.get(pos);
        Observable.just("record_channel/"+recordId)
                .map(new Function<String, Boolean>() {
                    @Override
                    public Boolean apply(@NonNull String s) throws Exception {
                        RestClient.builder()
                                .url(s)
                                .params("channelId", channelId)
                                .success(new ISuccess() {
                                    @Override
                                    public void onSuccess(String response) {
                                        LatteLogger.d("modify_channel", response);
                                        JSONObject data = JSON.parseObject(response);
                                        MultipleItemEntity entity =  (MultipleItemEntity) mAdapter.getData().get(mPosition);
                                        entity.setField(MultipleFields.NAME, data.getString("data"));
                                        mAdapter.notifyItemChanged(mPosition);
                                    }
                                })
                                .build()
                                .put();
                        return true;

                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(@NonNull Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            Toast.makeText(Latte.getApplicationContext(), "修改成功", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(Latte.getApplicationContext(), "修改失败", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void getChannel(){
        RestClient.builder()
                .url("channel/" + LattePreference.getCustomAppProfile("userId"))
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        LatteLogger.d("getchannel", response);
                        final JSONObject result = JSON.parseObject(response);
                        final JSONArray array = result.getJSONArray("data");
                        final int size = array.size();
                        if (!channelList.isEmpty()){
                            channelList.clear();
                            channelIdList.clear();
                        }
                        for(int i = 0; i < size; i++){
                            final JSONObject obj = array.getJSONObject(i);
                            channelIdList.add(obj.getInteger("id"));
                            channelList.add(obj.getString("channel"));
                        }
                    }
                })
                .build()
                .get();

    }


    @Override
    public boolean equals(Object obj) {
        return true;
    }
}
