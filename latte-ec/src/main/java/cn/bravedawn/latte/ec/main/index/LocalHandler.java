package cn.bravedawn.latte.ec.main.index;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;

import org.greenrobot.greendao.query.Query;
import org.json.JSONArray;

import java.util.List;

import cn.bravedawn.latte.delegates.LatteDelegate;
import cn.bravedawn.latte.ec.database.DatabaseManager;
import cn.bravedawn.latte.ec.database.RecordProfile;
import cn.bravedawn.latte.ec.detail.RecordDetailDelegate;
import cn.bravedawn.latte.ec.main.EcBottomDelegate;
import cn.bravedawn.latte.ui.recycler.DataConverter;
import cn.bravedawn.latte.ui.recycler.MultipleFields;
import cn.bravedawn.latte.ui.recycler.MultipleItemEntity;
import cn.bravedawn.latte.util.log.LatteLogger;
import me.yokeyword.fragmentation.ISupportFragment;

/**
 * Created by 冯晓 on 2017/10/21.
 */

public class LocalHandler {

    private final RecyclerView RECYCLERVIEW;
    private final DataConverter CONVERTER;
    private final LatteDelegate DELEGATE;
    private IndexDataAdapter mAdapter = null;

    public LocalHandler(RecyclerView recyclerView, DataConverter converter, LatteDelegate delegate) {
        this.RECYCLERVIEW = recyclerView;
        this.CONVERTER = converter;
        this.DELEGATE = delegate;
    }

    public static LocalHandler create(RecyclerView recyclerView, DataConverter converter, LatteDelegate delegate){
        return new LocalHandler(recyclerView, converter, delegate);
    }


    public void firstLocalLoad(){
        Query<RecordProfile> recordProfileQuery = DatabaseManager.getInstance().getRecordDao()
                .queryBuilder().build();
        List<RecordProfile> recordProfiles = recordProfileQuery.list();
        JSONObject data = new JSONObject();
        data.put("data", recordProfiles);
        LatteLogger.d("recordProfiles", data.toJSONString());

        mAdapter = new IndexDataAdapter(CONVERTER.setJsonData(data.toJSONString()).convert());
        RECYCLERVIEW.setAdapter(mAdapter);
        RECYCLERVIEW.removeOnItemTouchListener(IndexItemClickListener
                .create(DELEGATE));
        RECYCLERVIEW.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                final MultipleItemEntity entity = (MultipleItemEntity) adapter.getData().get(position);
                final String url = entity.getField(MultipleFields.URL);
                final boolean isStar = entity.getField(MultipleFields.BOOL);
                final Integer id = entity.getField(MultipleFields.ID);
                LatteLogger.d(entity.getField(MultipleFields.TITLE));
                final RecordDetailDelegate delegate = RecordDetailDelegate.create(url, isStar, id);
                DELEGATE.getSupportDelegate().start(delegate, ISupportFragment.SINGLETASK);
            }
        });
    }


}
