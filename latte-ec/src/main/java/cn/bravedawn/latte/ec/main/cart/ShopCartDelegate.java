package cn.bravedawn.latte.ec.main.cart;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ViewStubCompat;
import android.view.View;
import android.widget.Toast;

import com.joanzapata.iconify.widget.IconTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bravedawn.latte.delegates.bottom.BottomItemDelegate;
import cn.bravedawn.latte.ec.R;
import cn.bravedawn.latte.ec.R2;
import cn.bravedawn.latte.net.RestClient;
import cn.bravedawn.latte.net.callback.ISuccess;
import cn.bravedawn.latte.ui.recycler.MultipleItemEntity;
import cn.bravedawn.latte.util.log.LatteLogger;

/**
 * Created by 冯晓 on 2017/9/27.
 */

public class ShopCartDelegate extends BottomItemDelegate implements ISuccess, ICartItemListener{


    private ShopCartAdapter mAdapter = null;

    @BindView(R2.id.rv_shop_cart)
    RecyclerView mRecyclerView = null;

    @BindView(R2.id.icon_shop_cart_select_all)
    IconTextView mIconTextView = null;

    @BindView(R2.id.stun_no_item)
    ViewStubCompat mStubCompat = null;

    @BindView(R2.id.tv_shop_cart_total_price)
    AppCompatTextView mTvTotalPrice = null;

    @OnClick(R2.id.icon_shop_cart_select_all)
    void onClickSelectAll(){
        final int tag = (int) mIconTextView.getTag();
        if (tag == 0){
            mIconTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.app_main));
            mIconTextView.setTag(1);
            mAdapter.setIsSelectAll(true);
            // 更新RecyclerView的显示状态，这会加载所有的数据
            //mAdapter.notifyDataSetChanged();
            // 给一个范围加载
            mAdapter.notifyItemRangeChanged(0, mAdapter.getItemCount());
        } else{
            mIconTextView.setTextColor(Color.GRAY);
            mIconTextView.setTag(0);
            mAdapter.setIsSelectAll(false);
            //mAdapter.notifyDataSetChanged();
            mAdapter.notifyItemRangeChanged(0, mAdapter.getItemCount());
        }
    }

    @OnClick(R2.id.tv_top_shop_cart_del)
    void onClickRemoveSelectedItem(){
        final List<MultipleItemEntity> data = mAdapter.getData();
        // 要删除的数据
        final List<MultipleItemEntity> deleteEntities = new ArrayList<>();
        int i = 0;
        for (MultipleItemEntity entity : data){
            final boolean isSelected = entity.getField(ShopCartMultipleFields.IS_SELECTED);
            entity.setField(ShopCartMultipleFields.POSITION, i);
            if (isSelected){
                deleteEntities.add(entity);
            }
            i++;
        }
        for (MultipleItemEntity entity : deleteEntities){
            final int removePosition = entity.getField(ShopCartMultipleFields.POSITION);
            if (removePosition <= mAdapter.getItemCount()) {
                mAdapter.remove(removePosition);
                //更新数据
                mAdapter.notifyItemRangeChanged(removePosition, mAdapter.getItemCount());
            }
        }
        checkItemCount();
    }

    @OnClick(R2.id.tv_top_shop_cart_clear)
    void onClickClear(){
        mAdapter.getData().clear();
        mAdapter.notifyDataSetChanged();
        checkItemCount();
    }

    @OnClick(R2.id.tv_shop_cart_pay)
    void onClickPay(){

    }

    //创建订单，注意这和支付没有关系
    private void createOrder(){
        final String orderUrl = "";
        final WeakHashMap<String, Object> orderParams = new WeakHashMap<>();
        orderParams.put("userId", 1);
        orderParams.put("amount", 0.01);
        orderParams.put("comment", "测试支付");
        orderParams.put("type", 1);
        orderParams.put("orderType", 0);
        orderParams.put("isanonymous", true);
        orderParams.put("followeduser", 0);
        RestClient.builder()
                .url(orderUrl)
                .loader(getContext())
                .params(orderParams)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        // 进行具体的支付

                    }
                })
                .build()
                .post();
    }

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
        mAdapter.setCartItemListener(this);
        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mAdapter);
        mTvTotalPrice.setText(String.valueOf(mAdapter.getTotalPrice()));
        checkItemCount();
    }

    private void checkItemCount(){
        final int count = mAdapter.getItemCount();
        if (count == 0){
            final View stubView = mStubCompat.inflate();
            final AppCompatTextView tvToBuy = (AppCompatTextView) stubView.findViewById(R.id.tv_stub_to_buy);
            tvToBuy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), "你该购物啦！", Toast.LENGTH_LONG).show();
                }
            });
            mRecyclerView.setVisibility(View.GONE);
        } else{
            mRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onItemClick(double itemTotalPrice) {
        final double totalPrice = mAdapter.getTotalPrice();
        mTvTotalPrice.setText(String.valueOf(totalPrice));
    }
}
