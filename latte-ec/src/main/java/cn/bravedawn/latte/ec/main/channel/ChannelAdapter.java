package cn.bravedawn.latte.ec.main.channel;

import android.support.v7.widget.AppCompatImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import cn.bravedawn.latte.ec.R;
import cn.bravedawn.latte.ui.recycler.ItemType;
import cn.bravedawn.latte.ui.recycler.MultipleFields;
import cn.bravedawn.latte.ui.recycler.MultipleItemEntity;
import cn.bravedawn.latte.ui.recycler.MultipleRecycleAdapter;
import cn.bravedawn.latte.ui.recycler.MultipleViewHolder;

/**
 * Created by 冯晓 on 2017/10/10.
 */

public class ChannelAdapter extends MultipleRecycleAdapter {


    protected ChannelAdapter(List<MultipleItemEntity> data) {
        super(data);
        addItemType(ItemType.CHANNEL_CARD_VIEW, R.layout.item_channel);
    }

    @Override
    protected void convert(MultipleViewHolder helper, MultipleItemEntity item) {
        super.convert(helper, item);
        switch (helper.getItemViewType()){
            case ItemType.CHANNEL_CARD_VIEW:
                // 取出所有值
                final String thumb = item.getField(MultipleFields.IMAGE_URL);
                final String title = item.getField(MultipleFields.TITLE);
                final String desc = item.getField(MultipleFields.TEXT);
                // 控件赋值
                helper.setText(R.id.tv_channel_title, title);
                helper.setText(R.id.tv_channel_desc, desc);
                final AppCompatImageView imageView = helper.getView(R.id.image_channel_thumb);
                Glide.with(mContext)
                        .load(thumb)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerCrop()
                        .dontAnimate()
                        .into(imageView);
                break;
            default:
                break;
        }
    }
}
