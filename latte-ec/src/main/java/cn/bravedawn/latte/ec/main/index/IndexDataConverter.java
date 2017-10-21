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
        final ArrayList<MultipleItemEntity> dataList = new ArrayList<>();
        final JSONArray dataArray =  JSON.parseObject(getJsonData()).getJSONArray("data");
        final int size = dataArray.size();
        for (int i = 0; i < size; i++){
            final JSONObject data = dataArray.getJSONObject(i);

            final int id = data.getInteger("id");
            final String url = data.getString("url");
            final String title = data.getString("title");
            final String resource = data.getString("resource");
            final String channel = data.getString("channel");
            final String colorAvatar = data.getString("colorAvatar");
            final boolean isStar = data.getBoolean("mstar");

            final MultipleItemEntity entity = MultipleItemEntity.builder()
                    .setItemField(MultipleFields.ITEM_TYPE, ItemType.TEXT_IMAGE)
                    .setItemField(MultipleFields.ID, id)
                    .setItemField(MultipleFields.TITLE, title)
                    .setItemField(MultipleFields.COLOR_AVATAR, colorAvatar)
                    .setItemField(MultipleFields.URL, url)
                    .setItemField(MultipleFields.TEXT, resource)
                    .setItemField(MultipleFields.BOOL, isStar)
                    .setItemField(MultipleFields.NAME, channel)
                    .build();
            dataList.add(entity);
        }
        return dataList;
    }
}
