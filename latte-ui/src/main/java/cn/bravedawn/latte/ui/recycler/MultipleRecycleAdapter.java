package cn.bravedawn.latte.ui.recycler;

import android.graphics.Color;

import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;

import java.util.List;


import cn.bravedawn.latte.ui.R;
import cn.bravedawn.latte.util.log.LatteLogger;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by 冯晓 on 2017/9/24.
 */

public class MultipleRecycleAdapter extends BaseMultiItemQuickAdapter<MultipleItemEntity, MultipleViewHolder> {

    protected MultipleRecycleAdapter(List<MultipleItemEntity> data) {
        super(data);
        openLoadAnimation();
    }

    @Override
    protected void convert(MultipleViewHolder helper, MultipleItemEntity item) {

    }

    public static MultipleRecycleAdapter create(List<MultipleItemEntity> data) {
        return new MultipleRecycleAdapter(data);
    }

    public static MultipleRecycleAdapter create(DataConverter converter) {
        return new MultipleRecycleAdapter(converter.convert());
    }


}
