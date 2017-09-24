package cn.bravedawn.latte.ui.recycler;

import java.util.LinkedHashMap;

/**
 * Created by 冯晓 on 2017/9/24.
 */

public class MultipleItemEntityBuilder {

    private static final LinkedHashMap<Object, Object> FIELDS = new LinkedHashMap<>();

    public MultipleItemEntityBuilder(){
        //先清除之前的数据
        FIELDS.clear();
    }

    public final MultipleItemEntityBuilder setItemType(int itemType){
        FIELDS.put(MultipleFields.ITEM_TYPE, itemType);
        return this;
    }

    public final MultipleItemEntityBuilder setItemField(Object key, Object value){
        FIELDS.put(key, value);
        return this;
    }

    public final MultipleItemEntityBuilder setItemFields(LinkedHashMap<Object, Object> fields){
        FIELDS.putAll(fields);
        return this;
    }

    public final MultipleItemEntity build(){
        return new MultipleItemEntity(FIELDS);
    }
}
