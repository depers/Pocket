package cn.bravedawn.latte.ui.recycler;

import java.util.ArrayList;

/**
 * Created by 冯晓 on 2017/9/24.
 */

public abstract class DataConverter {

    protected final ArrayList<MultipleItemEntity> ENTITIES = new ArrayList<>();
    private String mJsonData = null;


    public abstract ArrayList<MultipleItemEntity> convert();

    public DataConverter setJsonData(String json){
        this.mJsonData = json;
        return this;
    }

    protected String getJsonData(){
        if (mJsonData == null || mJsonData.isEmpty()) {
            throw new NullPointerException("Data is NULL");
        }
        return mJsonData;
    }

    public void clearData(){
        ENTITIES.clear();
    }
}
