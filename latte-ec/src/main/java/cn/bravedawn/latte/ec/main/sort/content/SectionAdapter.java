package cn.bravedawn.latte.ec.main.sort.content;

import android.support.v7.widget.AppCompatImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.bravedawn.latte.ec.R;

/**
 * Created by 冯晓 on 2017/9/25.
 */

public class SectionAdapter extends BaseSectionQuickAdapter<SectionBean, BaseViewHolder>{


    public SectionAdapter(int layoutResId, int sectionHeadResId, List<SectionBean> data) {
        super(layoutResId, sectionHeadResId, data);
    }

    @Override
    protected void convertHead(BaseViewHolder helper, SectionBean item) {
        helper.setText(R.id.header, item.header);
        helper.setVisible(R.id.more, item.isIsMore());
        helper.addOnClickListener(R.id.more);
    }

    @Override
    protected void convert(BaseViewHolder helper, SectionBean item) {
        // item.t 返回泛型SectionContentItemEntity
        final String thumb = item.t.getGoodsThumb();
        final String name = item.t.getGoodsName();
        final int id = item.t.getGoodsId();
        final SectionContentItemEntity entity = item.t;
        helper.setText(R.id.item_content_title, name);
        final AppCompatImageView imageView = helper.getView(R.id.item_content_img);
        Glide.with(mContext)
                .load(thumb)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(imageView);

    }
}
