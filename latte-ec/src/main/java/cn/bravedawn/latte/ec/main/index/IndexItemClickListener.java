package cn.bravedawn.latte.ec.main.index;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.view.View;
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

/**
 * Created by 冯晓 on 2017/9/25.
 */

public class IndexItemClickListener extends SimpleClickListener {

    private final LatteDelegate DELEGATE;

    private final String URL;

    private Integer RESPONSE_CODE = null;

    private final List<String> channelList = new ArrayList<>();

    private IndexItemClickListener(LatteDelegate delegate, String url) {
        this.DELEGATE = delegate;
        this.URL = url;
        getChannel();
    }


    public static SimpleClickListener create(LatteDelegate delegate, String url) {
        return new IndexItemClickListener(delegate, url);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

        final MultipleItemEntity entity = (MultipleItemEntity) adapter.getData().get(position);
        final String url = entity.getField(MultipleFields.URL);
        final boolean isStar = entity.getField(MultipleFields.BOOL);
        final Integer id = entity.getField(MultipleFields.ID);
        LatteLogger.d(entity.getField(MultipleFields.TITLE));
        final RecordDetailDelegate delegate = RecordDetailDelegate.create(url, isStar, id);
        DELEGATE.getSupportDelegate().start(delegate);
    }

    @Override
    public void onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
        float dx = adapter.getViewByPosition(position, R.id.layoutRecordItemMain).getX();
        if (!(dx > 0)){
            Latte.getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    new MaterialDialog.Builder(DELEGATE.getContext())
                            .title(R.string.group_dialog)
                            .items(channelList)
                            .itemsCallbackSingleChoice(0, new MaterialDialog.ListCallbackSingleChoice() {
                                @Override
                                public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                                    modifyChannel(which);
                                    return true;
                                }
                            })
                            .positiveText("确定")
                            .show();
                }
            }, 500);
        }
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
    }

    @Override
    public void onItemChildLongClick(BaseQuickAdapter adapter, View view, int position) {

    }

    // TODO: 2017/10/10 与服务器配合添加上传修改分类数据
    private void modifyChannel(final int pos) {
        Observable.just(URL)
                .map(new Function<String, Boolean>() {
                    @Override
                    public Boolean apply(@NonNull String s) throws Exception {
                        RestClient.builder()
                                .url(URL)
                                .success(new ISuccess() {
                                    @Override
                                    public void onSuccess(String response) {
                                        LatteLogger.d("modify_channel", response);
                                        JSONObject result = JSON.parseObject(response);
                                        RESPONSE_CODE = result.getInteger("code");
                                        LatteLogger.d("RESPONSE_CODE2", RESPONSE_CODE);
                                    }
                                })
                                .build()
                                .get();
                        return true;

                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(@NonNull Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            Toast.makeText(Latte.getApplicationContext(), "修改成功, "+pos, Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(Latte.getApplicationContext(), "修改失败", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    // TODO: 2017/10/10 查询用户栏目
    private void getChannel(){
        RestClient.builder()
                .url("channel/" + LattePreference.getCustomAppProfile("userId"))
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        LatteLogger.d("getchannel", response);
                        final JSONObject result = JSON.parseObject(response);
                        final JSONArray array = result.getJSONArray("data");
                        final int size = array.size();
                        for(int i = 0; i < size; i++){
                            final JSONObject obj = array.getJSONObject(i);
                            LatteLogger.d("JSONObject", obj);
                            channelList.add(obj.getString("channel"));
                        }
                    }
                })
                .build()
                .get();

    }
}
