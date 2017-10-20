package cn.bravedawn.latte.ec.main.channel;

import android.support.v7.widget.PopupMenu;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.SimpleClickListener;

import cn.bravedawn.latte.app.Latte;
import cn.bravedawn.latte.delegates.LatteDelegate;
import cn.bravedawn.latte.ec.R;
import cn.bravedawn.latte.ec.main.channel.add.AddChannelDelegate;
import cn.bravedawn.latte.ec.main.channel.detail.ChannelDetailDelegate;
import cn.bravedawn.latte.net.RestClient;
import cn.bravedawn.latte.net.callback.ISuccess;
import cn.bravedawn.latte.ui.loader.LatteLoader;
import cn.bravedawn.latte.ui.loader.LoaderStyle;
import cn.bravedawn.latte.ui.recycler.MultipleFields;
import cn.bravedawn.latte.ui.recycler.MultipleItemEntity;
import cn.bravedawn.latte.util.log.LatteLogger;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import me.yokeyword.fragmentation.ISupportFragment;

/**
 * Created by 冯晓 on 2017/10/14.
 */

public class ChannelItemClickListener extends SimpleClickListener {

    private final ChannelDelegate DELEGATE;

    private PopupMenu popupMenu;

    private ChannelItemClickListener(ChannelDelegate delegate) {
        this.DELEGATE = delegate;
    }

    public static SimpleClickListener create(ChannelDelegate delegate) {
        return new ChannelItemClickListener(delegate);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        LatteLogger.d("onItemClick");
        final MultipleItemEntity entity = (MultipleItemEntity) baseQuickAdapter.getData().get(position);
        final Integer channelId = entity.getField(MultipleFields.ID);
        final ChannelDetailDelegate delegate = ChannelDetailDelegate.create(channelId);
        DELEGATE.getParentDelegate().getSupportDelegate().start(delegate, ISupportFragment.SINGLETASK);
    }

    @Override
    public void onItemLongClick(BaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemChildClick(final BaseQuickAdapter adapter, View view, final int position) {
        final Integer channelId = ((MultipleItemEntity) adapter.getData()
                .get(position)).getField(MultipleFields.ID);
        final Integer count = ((MultipleItemEntity) adapter.getData()
                .get(position)).getField(MultipleFields.COUNT);
        popupMenu = new PopupMenu(DELEGATE.getContext(), view, Gravity.BOTTOM);
        popupMenu.inflate(R.menu.channel_item_menu);
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int i = item.getItemId();
                if (i == R.id.channel_item_action_modify) {
                    popupMenu.dismiss();
                    DELEGATE.getParentDelegate().getSupportDelegate().start(AddChannelDelegate.create(channelId));
                    return true;
                }
                if (i == R.id.channel_item_action_delete){
                    popupMenu.dismiss();
                    if (count > 0){
                        Toast.makeText(DELEGATE.getContext(), "抱歉，该栏目下尚有子项", Toast.LENGTH_LONG).show();
                        return true;
                    }
                    popupMenu.dismiss();
                    adapter.remove(position);
                    delete(channelId);
                    return true;
                }
                popupMenu.dismiss();
                return true;
            }
        });
    }

    @Override
    public void onItemChildLongClick(BaseQuickAdapter adapter, View view, int position) {

    }

    private void delete(Integer channelId){
        LatteLoader.showLoading(DELEGATE.getContext(), LoaderStyle.LineScaleIndicator);
        Observable.just("channel/" + channelId)
                .map(new Function<String, Boolean>() {
                    @Override
                    public Boolean apply(@io.reactivex.annotations.NonNull String s) throws Exception {
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
                    public void accept(@io.reactivex.annotations.NonNull Boolean aBoolean) throws Exception {
                        LatteLoader.stopLoading();
                        if (aBoolean) {
                            DELEGATE.initViewByData();
                            Toast.makeText(DELEGATE.getContext(), "删除成功", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(DELEGATE.getContext(), "删除失败", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
