package cn.bravedawn.latte.ec.main.index.search;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import java.util.ArrayList;

import cn.bravedawn.latte.ui.recycler.DataConverter;
import cn.bravedawn.latte.ui.recycler.MultipleFields;
import cn.bravedawn.latte.ui.recycler.MultipleItemEntity;
import cn.bravedawn.latte.util.log.LatteLogger;
import cn.bravedawn.latte.util.storage.LattePreference;

/**
 * Created by 冯晓 on 2017/10/2.
 */

public class SearchDateConverter extends DataConverter {

    public static final String TAG_SEARCH_HISTORY = "search_history";
    private final ArrayList<MultipleItemEntity> dataList = new ArrayList<>();

    @Override
    public ArrayList<MultipleItemEntity> convert() {

        final String jsonStr = LattePreference.getCustomAppProfile(TAG_SEARCH_HISTORY);
        if (!jsonStr.equals("")){
            LatteLogger.d(JSON.toJSONString(jsonStr));
            final JSONArray array = JSONArray.parseArray(jsonStr);
            final int size = array.size();
            for (int i = 0; i < size; i++){
                final String historyItemText = array.getString(i);
                final MultipleItemEntity entity = MultipleItemEntity.builder()
                        .setItemType(SearchItemType.ITEM_SEARCH)
                        .setItemField(MultipleFields.TEXT, historyItemText)
                        .build();
                dataList.add(entity);
            }
        }

        return dataList;
    }
}
