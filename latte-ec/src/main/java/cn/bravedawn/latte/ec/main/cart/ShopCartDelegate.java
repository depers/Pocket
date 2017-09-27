package cn.bravedawn.latte.ec.main.cart;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import cn.bravedawn.latte.delegates.bottom.BottomItemDelegate;
import cn.bravedawn.latte.ec.R;
import cn.bravedawn.latte.ec.R2;
import cn.bravedawn.latte.net.RestClient;
import cn.bravedawn.latte.net.callback.ISuccess;
import cn.bravedawn.latte.ui.recycler.MultipleItemEntity;

/**
 * Created by 冯晓 on 2017/9/27.
 */

public class ShopCartDelegate extends BottomItemDelegate implements ISuccess {

    @BindView(R2.id.rv_shop_cart)
    RecyclerView mRecyclerView = null;

    private ShopCartAdapter mAdapter = null;

    @Override
    public Object setLayout() {
        return R.layout.delegate_shop_cart;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {

    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        RestClient.builder()
                .url("shop_cart_data")
                .loader(getContext())
                .success(this)
                .build()
                .get();

    }

    @Override
    public void onSuccess(String response) {
        final ArrayList<MultipleItemEntity> data =
                new ShopCartDataConverter()
                        .setJsonData(response)
                        .convert();
        mAdapter = new ShopCartAdapter(data);
        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mAdapter);
    }
}
