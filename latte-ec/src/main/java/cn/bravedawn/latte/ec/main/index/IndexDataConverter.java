package cn.bravedawn.latte.ec.main.index;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;

import cn.bravedawn.latte.ui.recycler.DataConverter;
import cn.bravedawn.latte.ui.recycler.ItemType;
import cn.bravedawn.latte.ui.recycler.MultipleFields;
import cn.bravedawn.latte.ui.recycler.MultipleItemEntity;

/**
 * Created by 冯晓 on 2017/9/24.
 */

public class IndexDataConverter extends DataConverter{

    @Override
    public ArrayList<MultipleItemEntity> convert() {
        final JSONArray dataArray =  JSON.parseObject(getJsonData()).getJSONArray("data");
        final int size = dataArray.size();
        for (int i = 0; i < size; i++){
            final JSONObject data = dataArray.getJSONObject(i);

            final String imageUrl = data.getString("imageUrl");
            final String text = data.getString("text");
            final int spanSize = data.getInteger("spanSize");
            final int id = data.getInteger("goodsId");
            final JSONArray banners = data.getJSONArray("banners");
            final ArrayList<String> bannerImages = new ArrayList<>();
            int type = 0;
            if (imageUrl == null && text != null){
                type = ItemType.TEXT;
            } else if (imageUrl != null && text == null){
                type = ItemType.IMAGE;
            } else if(imageUrl != null && text != null){
                type = ItemType.TEXT_IMAGE;
            } else if (banners != null){
                type = ItemType.BANNER;
                // banner初始化
                final int bannerSize = banners.size();
                for (int j = 0; j < bannerSize; j++){
                    final String banner = banners.getString(j);
                    bannerImages.add(banner);
                }
            }

            final MultipleItemEntity entity = MultipleItemEntity.builder()
                    .setItemField(MultipleFields.ITEM_TYPE, type)
                    .setItemField(MultipleFields.SPAN_SIZE, spanSize)
                    .setItemField(MultipleFields.ID, id)
                    .setItemField(MultipleFields.TEXT, text)
                    .setItemField(MultipleFields.IMAGE_URL, imageUrl)
                    .setItemField(MultipleFields.BANNERS, bannerImages)
                    .build();
            ENTITIES.add(entity);
        }
        return ENTITIES;
    }
}
