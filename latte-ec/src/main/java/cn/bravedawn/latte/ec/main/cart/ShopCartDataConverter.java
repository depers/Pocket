package cn.bravedawn.latte.ec.main.cart;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;

import cn.bravedawn.latte.ui.recycler.DataConverter;
import cn.bravedawn.latte.ui.recycler.MultipleFields;
import cn.bravedawn.latte.ui.recycler.MultipleItemEntity;

/**
 * Created by 冯晓 on 2017/9/27.
 */

public class ShopCartDataConverter extends DataConverter{


    @Override
    public ArrayList<MultipleItemEntity> convert() {
        final ArrayList<MultipleItemEntity> dataList = new ArrayList<>();
        JSONArray dataArray = JSON.parseObject(getJsonData()).getJSONArray("data");
        final int size = dataArray.size();
        for (int i = 0; i < size; i++){
            final JSONObject data = dataArray.getJSONObject(i);
            final String thumb = data.getString("thumb");
            final String desc = data.getString("desc");
            final String title = data.getString("title");
            final int id = data.getInteger("id");
            final int count = data.getInteger("count");
            final double price = data.getDouble("price");
            final MultipleItemEntity entity = MultipleItemEntity.builder()
                    .setItemField(MultipleFields.ITEM_TYPE, ShopCartItemType.SHOP_CART_ITEM)
                    .setItemField(MultipleFields.ID, id)
                    .setItemField(MultipleFields.IMAGE_URL, thumb)
                    .setItemField(ShopCartMultipleFields.TITLE, title)
                    .setItemField(ShopCartMultipleFields.DESC, desc)
                    .setItemField(ShopCartMultipleFields.COUNT, count)
                    .setItemField(ShopCartMultipleFields.PRICE, price)
                    .setItemField(ShopCartMultipleFields.IS_SELECTED, false)
                    .build();
            dataList.add(entity);
        }

        return dataList;
    }
}
