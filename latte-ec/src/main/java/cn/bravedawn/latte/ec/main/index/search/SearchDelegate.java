package cn.bravedawn.latte.ec.main.index.search;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.choices.divider.Divider;
import com.choices.divider.DividerItemDecoration;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bravedawn.latte.delegates.LatteDelegate;
import cn.bravedawn.latte.ec.R;
import cn.bravedawn.latte.ec.R2;
import cn.bravedawn.latte.ec.detail.RecordDetailDelegate;
import cn.bravedawn.latte.ec.main.index.IndexDataAdapter;
import cn.bravedawn.latte.ec.main.index.IndexDataConverter;
import cn.bravedawn.latte.net.RestClient;
import cn.bravedawn.latte.net.callback.IFailure;
import cn.bravedawn.latte.net.callback.ISuccess;
import cn.bravedawn.latte.ui.loader.LoaderStyle;
import cn.bravedawn.latte.ui.recycler.MultipleFields;
import cn.bravedawn.latte.ui.recycler.MultipleItemEntity;
import cn.bravedawn.latte.util.log.LatteLogger;
import cn.bravedawn.latte.util.storage.LattePreference;
import me.yokeyword.fragmentation.ISupportFragment;

/**
 * Created by 冯晓 on 2017/10/2.
 */

public class SearchDelegate extends LatteDelegate {

    @BindView(R2.id.rv_search)
    RecyclerView mRecyclerView = null;

    @BindView(R2.id.et_search_view)
    AppCompatEditText mEditText = null;

    @BindView(R2.id.tb_main_page)
    Toolbar mToolbar = null;

    @OnClick(R2.id.tv_top_search)
    void onClickSearch() {
        final String searchItemText = mEditText.getText().toString();
        RestClient.builder()
                .url("record/search/"+ LattePreference.getCustomAppProfile("userId"))
                .params("keyword", searchItemText)
                .loader(getContext(), LoaderStyle.LineScaleIndicator)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        LatteLogger.d("search", response);
                        final JSONObject data = JSON.parseObject(response);
                        final Integer size = data.getInteger("total");
                        if (size == 0){
                            Toast.makeText(getContext(), data.getString("msg"), Toast.LENGTH_LONG).show();
                        }else{
                            final ArrayList<MultipleItemEntity> dataList = new IndexDataConverter()
                                    .setJsonData(response).convert();
                            final IndexDataAdapter mAdapter = new IndexDataAdapter(dataList);
                            final LinearLayoutManager manager = new LinearLayoutManager(getContext());
                            mRecyclerView.setLayoutManager(manager);
                            mRecyclerView.setAdapter(mAdapter);
                            mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
                                @Override
                                public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                                    final MultipleItemEntity entity = mAdapter.getData().get(position);
                                    final String url = entity.getField(MultipleFields.URL);
                                    final boolean isStar = entity.getField(MultipleFields.BOOL);
                                    final Integer id = entity.getField(MultipleFields.ID);
                                    LatteLogger.d(entity.getField(MultipleFields.TITLE));
                                    final RecordDetailDelegate delegate = RecordDetailDelegate.create(url, isStar, id);
                                    SearchDelegate.this.getSupportDelegate().start(delegate, ISupportFragment.SINGLETASK);
                                }
                            });
                        }

                        saveItem(searchItemText);
                        mEditText.setText("");
                    }
                })
                .build()
                .get();

    }

    @OnClick(R2.id.icon_top_search_back)
    void onClickBack() {
        getSupportDelegate().pop();
    }

    private void saveItem(String itemText) {
        if (!StringUtils.isEmpty(itemText) && !StringUtils.isSpace(itemText)) {
            Set<String> history;
            final String historyStr =
                    LattePreference.getCustomAppProfile(SearchDateConverter.TAG_SEARCH_HISTORY);
            if (StringUtils.isEmpty(historyStr)) {
                history = new HashSet<>();
            } else {
                history = JSON.parseObject(historyStr, HashSet.class);
            }
            history.add(itemText);
            final String json = JSON.toJSONString(history);

            LattePreference.addCustomAppProfile(SearchDateConverter.TAG_SEARCH_HISTORY, json);
        }
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_search;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        mToolbar.setContentInsetsAbsolute(0, 0);
        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(manager);

        final List<MultipleItemEntity> data = new SearchDateConverter().convert();
        final SearchAdapter adapter = new SearchAdapter(data);
        mRecyclerView.setAdapter(adapter);

        final DividerItemDecoration itemDecoration = new DividerItemDecoration();
        itemDecoration.setDividerLookup(new DividerItemDecoration.DividerLookup() {
            @Override
            public Divider getVerticalDivider(int position) {
                return null;
            }

            @Override
            public Divider getHorizontalDivider(int position) {
                return new Divider.Builder()
                        .size(2)
                        .margin(20, 20)
                        .color(Color.GRAY)
                        .build();
            }
        });
        mRecyclerView.addItemDecoration(itemDecoration);
        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                final MultipleItemEntity entity= (MultipleItemEntity) adapter.getData().get(position);
                final String keyword = entity.getField(MultipleFields.TEXT);
                mEditText.setText(keyword);
                onClickSearch();
            }
        });

    }
}
