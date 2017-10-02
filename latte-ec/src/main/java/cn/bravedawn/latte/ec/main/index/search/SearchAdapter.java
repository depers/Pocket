package cn.bravedawn.latte.ec.main.index.search;

import android.support.v7.widget.AppCompatTextView;

import java.util.List;

import cn.bravedawn.latte.ec.R;
import cn.bravedawn.latte.ui.recycler.MultipleFields;
import cn.bravedawn.latte.ui.recycler.MultipleItemEntity;
import cn.bravedawn.latte.ui.recycler.MultipleRecycleAdapter;
import cn.bravedawn.latte.ui.recycler.MultipleViewHolder;

/**
 * Created by 冯晓 on 2017/10/2.
 */

public class SearchAdapter extends MultipleRecycleAdapter{


    protected SearchAdapter(List<MultipleItemEntity> data) {
        super(data);
        addItemType(SearchItemType.ITEM_SEARCH, R.layout.item_search);
    }

    @Override
    protected void convert(MultipleViewHolder holder, MultipleItemEntity item) {
        super.convert(holder, item);
        switch (item.getItemType()){
            case SearchItemType.ITEM_SEARCH:
                final AppCompatTextView textView = holder.getView(R.id.tv_search_item);
                final String history = item.getField(MultipleFields.TEXT);
                textView.setText(history);
                break;
            default:
                break;
        }
    }
}
