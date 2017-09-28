package cn.bravedawn.latte.ec.main.cart;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.joanzapata.iconify.widget.IconTextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
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

    @BindView(R2.id.icon_shop_cart_select_all)
    IconTextView mIconTextView = null;

    @OnClick(R2.id.icon_shop_cart_select_all)
    void onClickSelectAll(){
        final int tag = (int) mIconTextView.getTag();
        if (tag == 0){
            mIconTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.app_main));
            mIconTextView.setTag(1);
            mAdapter.setIsSelectAll(true);
            // 更新RecyclerView的显示状态
            mAdapter.notifyDataSetChanged();
        } else{
            mIconTextView.setTextColor(Color.GRAY);
            mIconTextView.setTag(0);
            mAdapter.setIsSelectAll(false);
            mAdapter.notifyDataSetChanged();
        }
    }

    private ShopCartAdapter mAdapter = null;

    @Override
    public Object setLayout() {
        return R.layout.delegate_shop_cart;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
        mIconTextView.setTag(0);
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
