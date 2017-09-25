package cn.bravedawn.latte.ec.main.sort.list;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;

import cn.bravedawn.latte.ui.recycler.DataConverter;
import cn.bravedawn.latte.ui.recycler.ItemType;
import cn.bravedawn.latte.ui.recycler.MultipleFields;
import cn.bravedawn.latte.ui.recycler.MultipleItemEntity;

/**
 * Created by 冯晓 on 2017/9/25.
 */

public class VerticalListDataConverter extends DataConverter {


    @Override
    public ArrayList<MultipleItemEntity> convert() {
        final ArrayList<MultipleItemEntity> dataList = new ArrayList<>();

        final JSONArray dataArray =  JSON.parseObject(getJsonData()).getJSONObject("data").getJSONArray("list");
        final int size = dataArray.size();
        for (int i = 0; i < size; i++){
            final JSONObject data = dataArray.getJSONObject(i);
            final int id = data.getInteger("id");
            final String name = data.getString("name");

            final MultipleItemEntity entity = MultipleItemEntity.builder()
                    .setItemField(MultipleFields.ITEM_TYPE, ItemType.VERTICAL_MENU_LIST)
                    .setItemField(MultipleFields.ID, id)
                    .setItemField(MultipleFields.NAME, name)
                    .setItemField(MultipleFields.TAG, false)
                    .build();
            dataList.add(entity);
            dataList.get(0).setField(MultipleFields.TAG, true);
        }
        return dataList;
    }
}
