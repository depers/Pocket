package cn.bravedawn.latte.ec.main.cart;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.joanzapata.iconify.widget.IconTextView;

import java.util.List;

import cn.bravedawn.latte.app.Latte;
import cn.bravedawn.latte.ec.R;
import cn.bravedawn.latte.ui.recycler.MultipleFields;
import cn.bravedawn.latte.ui.recycler.MultipleItemEntity;
import cn.bravedawn.latte.ui.recycler.MultipleRecycleAdapter;
import cn.bravedawn.latte.ui.recycler.MultipleViewHolder;

/**
 * Created by 冯晓 on 2017/9/27.
 */

public class ShopCartAdapter extends MultipleRecycleAdapter {


    protected ShopCartAdapter(List<MultipleItemEntity> data) {
        super(data);
        // 添加购物车item布局
        addItemType(ShopCartItemType.SHOP_CART_ITEM, R.layout.item_shop_cart);
    }

    @Override
    protected void convert(MultipleViewHolder holder, final MultipleItemEntity item) {
        super.convert(holder, item);
        switch (holder.getItemViewType()){
            case ShopCartItemType.SHOP_CART_ITEM:
                // 取出所有值
                final int id = item.getField(MultipleFields.ID);
                final String thumb = item.getField(MultipleFields.IMAGE_URL);
                final String title = item.getField(ShopCartMultipleFields.TITLE);
                final String desc = item.getField(ShopCartMultipleFields.DESC);
                final int count = item.getField(ShopCartMultipleFields.COUNT);
                final double price = item.getField(ShopCartMultipleFields.PRICE);
                final boolean isSelected = item.getField(ShopCartMultipleFields.IS_SELECTED);
                // 取出所有控件
                final AppCompatImageView imageThumb = holder.getView(R.id.image_item_shop_cart);
                final AppCompatTextView tvTitle = holder.getView(R.id.tv_item_shop_cart_title);
                final AppCompatTextView tvDesc = holder.getView(R.id.tv_item_shop_cart_desc);
                final AppCompatTextView tvPrice = holder.getView(R.id.tv_item_shop_cart_price);
                final AppCompatTextView tvCount = holder.getView(R.id.tv_item_shop_cart_count);
                final IconTextView iconMinus = holder.getView(R.id.icon_item_minus);
                final IconTextView iconPlus = holder.getView(R.id.icon_item_plus);
                final IconTextView iconIsSelected = holder.getView(R.id.icon_item_shop_cart);
                //赋值
                tvTitle.setText(title);
                tvCount.setText(String.valueOf(count));
                tvDesc.setText(desc);
                tvPrice.setText(String.valueOf(price));
                Glide.with(mContext)
                        .load(thumb)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerCrop()
                        .dontAnimate()
                        .into(imageThumb);
                // 根据数据状态显示左侧勾选
                if (isSelected){
                    iconIsSelected.setTextColor(ContextCompat
                            .getColor(Latte.getApplicationContext(), R.color.app_main));
                } else{
                    iconIsSelected.setTextColor(Color.GRAY);
                }
                // 添加左侧点击事件
                iconIsSelected.setOnClickListener(new View.OnClickListener() {
                    final boolean currentSelected = item.getField(ShopCartMultipleFields.IS_SELECTED);
                    @Override
                    public void onClick(View v) {
                        if (currentSelected){
                            iconIsSelected.setTextColor(Color.GRAY);
                            item.setField(ShopCartMultipleFields.IS_SELECTED, false);
                        } else{
                            iconIsSelected.setTextColor(ContextCompat
                                    .getColor(Latte.getApplicationContext(), R.color.app_main));
                            item.setField(ShopCartMultipleFields.IS_SELECTED, true);
                        }
                    }
                });
                break;
            default:
                break;
        }
    }
}
