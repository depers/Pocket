package cn.bravedawn.latte.ec.main.channel;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import cn.bravedawn.latte.ui.recycler.DataConverter;
import cn.bravedawn.latte.ui.recycler.ItemType;
import cn.bravedawn.latte.ui.recycler.MultipleFields;
import cn.bravedawn.latte.ui.recycler.MultipleItemEntity;


/**
 * Created by 冯晓 on 2017/10/10.
 */

public class ChannelConverter extends DataConverter {


    @Override
    public ArrayList<MultipleItemEntity> convert() {
        final ArrayList<MultipleItemEntity> dataList = new ArrayList<>();
        final JSONObject channls = JSON.parseObject(getJsonData());
        JSONArray dataArray = channls.getJSONArray("data");
        final int size = dataArray.size();
        for (int i = 0; i < size; i++){
            final JSONObject data = dataArray.getJSONObject(i);
            final String thumb = data.getString("imageUrl");
            final String desc = data.getString("descText");
            final String title = data.getString("channel");
            final int count = data.getInteger("count");
            final int id = data.getInteger("id");
            final MultipleItemEntity entity = MultipleItemEntity.builder()
                    .setItemField(MultipleFields.ITEM_TYPE, ItemType.CHANNEL_CARD_VIEW)
                    .setItemField(MultipleFields.ID, id)
                    .setItemField(MultipleFields.IMAGE_URL, thumb)
                    .setItemField(MultipleFields.TITLE, title)
                    .setItemField(MultipleFields.TEXT, desc)
                    .setItemField(MultipleFields.COUNT, count)
                    .build();
            dataList.add(entity);
        }
        return dataList;
    }
}
