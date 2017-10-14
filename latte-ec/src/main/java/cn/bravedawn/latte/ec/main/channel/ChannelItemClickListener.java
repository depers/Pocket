package cn.bravedawn.latte.ec.main.channel;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.SimpleClickListener;

import cn.bravedawn.latte.delegates.LatteDelegate;
import cn.bravedawn.latte.ec.main.channel.detail.ChannelDetailDelegate;
import cn.bravedawn.latte.ui.recycler.MultipleFields;
import cn.bravedawn.latte.ui.recycler.MultipleItemEntity;
import cn.bravedawn.latte.util.log.LatteLogger;
import me.yokeyword.fragmentation.ISupportFragment;

/**
 * Created by 冯晓 on 2017/10/14.
 */

public class ChannelItemClickListener extends SimpleClickListener {

    private final LatteDelegate DELEGATE;

    private ChannelItemClickListener(LatteDelegate delegate) {
        this.DELEGATE = delegate;
    }

    public static SimpleClickListener create(LatteDelegate delegate) {
        return new ChannelItemClickListener(delegate);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        LatteLogger.d("onItemClick");
        final MultipleItemEntity entity = (MultipleItemEntity) baseQuickAdapter.getData().get(position);
        final Integer channelId = entity.getField(MultipleFields.ID);
        final ChannelDetailDelegate delegate = ChannelDetailDelegate.create(channelId);
        DELEGATE.getSupportDelegate().start(delegate, ISupportFragment.SINGLETASK);
    }

    @Override
    public void onItemLongClick(BaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemChildLongClick(BaseQuickAdapter adapter, View view, int position) {

    }
}
