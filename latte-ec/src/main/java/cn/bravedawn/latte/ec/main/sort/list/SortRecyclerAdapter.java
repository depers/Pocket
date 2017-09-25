package cn.bravedawn.latte.ec.main.sort.list;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import java.util.List;

import cn.bravedawn.latte.delegates.LatteDelegate;
import cn.bravedawn.latte.ec.R;
import cn.bravedawn.latte.ec.main.sort.SortDelegate;
import cn.bravedawn.latte.ec.main.sort.content.ContentDelegate;
import cn.bravedawn.latte.ui.recycler.ItemType;
import cn.bravedawn.latte.ui.recycler.MultipleFields;
import cn.bravedawn.latte.ui.recycler.MultipleItemEntity;
import cn.bravedawn.latte.ui.recycler.MultipleRecycleAdapter;
import cn.bravedawn.latte.ui.recycler.MultipleViewHolder;

/**
 * Created by 冯晓 on 2017/9/25.
 */

public class SortRecyclerAdapter extends MultipleRecycleAdapter {

    private final SortDelegate DELEGATE;
    private int mPrePosition = 0;


    protected SortRecyclerAdapter(List<MultipleItemEntity> data, SortDelegate delegate) {
        super(data);
        this.DELEGATE = delegate;
        // 添加垂直布局
        addItemType(ItemType.VERTICAL_MENU_LIST, R.layout.item_vertical_menu_list);
    }

    @Override
    protected void convert(final MultipleViewHolder holder, final MultipleItemEntity item) {
        super.convert(holder, item);
        switch (holder.getItemViewType()){
            case ItemType.VERTICAL_MENU_LIST:
                final String text = item.getField(MultipleFields.NAME);
                final boolean isClicked = item.getField(MultipleFields.TAG);
                final int contentId = item.getField(MultipleFields.ID);
                final AppCompatTextView name = holder.getView(R.id.tv_vertical_item_name);
                final View line = holder.getView(R.id.view_line);
                final View itemView = holder.itemView;

                //处理点击事件
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final int currentPosition = holder.getAdapterPosition();
                        if (mPrePosition != currentPosition){
                            // 还原上一个
                            getData().get(mPrePosition).setField(MultipleFields.TAG, false);
                            notifyItemChanged(mPrePosition);

                            // 更新选中的item
                            item.setField(MultipleFields.TAG, true);
                            notifyItemChanged(currentPosition);
                            mPrePosition = currentPosition;
                            showContent(contentId);
                        }
                    }
                });

                if (!isClicked){
                    line.setVisibility(View.INVISIBLE);
                    name.setTextColor(ContextCompat.getColor(mContext, R.color.we_chat_black));
                    itemView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.item_background));
                } else{
                    line.setVisibility(View.VISIBLE);
                    name.setTextColor(ContextCompat.getColor(mContext, R.color.app_main));
                    line.setBackgroundColor(ContextCompat.getColor(mContext, R.color.app_main));
                    itemView.setBackgroundColor(Color.WHITE);
                }
                holder.setText(R.id.tv_vertical_item_name, text);
                break;
            default:
                break;
        }
    }

    private void showContent(int contentId){
        final ContentDelegate contentDelegate = new ContentDelegate();
        switchContent(contentDelegate);

    }

    private void switchContent(ContentDelegate delegate){
        final LatteDelegate contentDelegate = DELEGATE.findChildFragment(ContentDelegate.class);
        if (contentDelegate != null){
            contentDelegate.replaceFragment(delegate, false);
        }
    }

}
