package cn.bravedawn.latte.ec.main.personal;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bravedawn.latte.delegates.bottom.BottomItemDelegate;
import cn.bravedawn.latte.ec.R;
import cn.bravedawn.latte.ec.R2;
import cn.bravedawn.latte.ec.main.personal.address.AddressDelegate;
import cn.bravedawn.latte.ec.main.personal.list.ListAdapter;
import cn.bravedawn.latte.ec.main.personal.list.ListBean;
import cn.bravedawn.latte.ec.main.personal.list.ListItemType;
import cn.bravedawn.latte.ec.main.personal.order.OrderListDelegate;
import cn.bravedawn.latte.ec.main.personal.profile.UserProfileDelegate;
import cn.bravedawn.latte.ec.main.personal.settings.PersonalClickListener;
import cn.bravedawn.latte.ec.main.personal.settings.SettingsDelegate;

/**
 * Created by 冯晓 on 2017/9/29.
 */

public class PersonalDelegate extends BottomItemDelegate{

    public static final String ORDER_TYPE = "ORDER_TYPE";
    private Bundle mArgs = null;


    @BindView(R2.id.rv_personal_setting)
    RecyclerView mRvSettings = null;

    @OnClick(R2.id.tv_all_order)
    void onClickAllOrder(){
        mArgs.putString(ORDER_TYPE, "all");
        startOrderListByType();
    }

    @OnClick(R2.id.ll_pay)
    void onClickPayOrder(){
        mArgs.putString(ORDER_TYPE, "all");
        startOrderListByType();
    }

    @OnClick(R2.id.ll_receive)
    void onClickReceiveOrder(){
        mArgs.putString(ORDER_TYPE, "all");
        startOrderListByType();
    }

    @OnClick(R2.id.ll_evaluate)
    void onClickEvaluateOrder(){
        mArgs.putString(ORDER_TYPE, "all");
        startOrderListByType();
    }

    @OnClick(R2.id.ll_after_market)
    void onClickAfterOrder(){
        mArgs.putString(ORDER_TYPE, "all");
        startOrderListByType();
    }

    @OnClick(R2.id.img_user_avatar)
    void onClickAvatar(){
        getParentDelegate().getSupportDelegate().start(new UserProfileDelegate());
    }


    @Override
    public Object setLayout() {
        return R.layout.delegate_personal;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        final ListBean address = new ListBean.Builder()
                .setItemType(ListItemType.ITEM_NORMAL)
                .setId(1)
                .setDelegate(new AddressDelegate())
                .setText("收货地址")
                .build();
        final ListBean system = new ListBean.Builder()
                .setItemType(ListItemType.ITEM_NORMAL)
                .setId(2)
                .setText("系统设置")
                .setDelegate(new SettingsDelegate())
                .build();
        final List<ListBean> data = new ArrayList<>();
        data.add(address);
        data.add(system);

        // 设置RecyclerView
        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRvSettings.setLayoutManager(manager);
        final ListAdapter adapter = new ListAdapter(data);
        mRvSettings.setAdapter(adapter);
        mRvSettings.addOnItemTouchListener(new PersonalClickListener(this));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mArgs = new Bundle();
    }

    private void startOrderListByType(){
        final OrderListDelegate delegate = new OrderListDelegate();
        delegate.setArguments(mArgs);
        getParentDelegate().getSupportDelegate().start(delegate);
    }

}
