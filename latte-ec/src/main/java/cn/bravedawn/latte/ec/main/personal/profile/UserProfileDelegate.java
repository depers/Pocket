package cn.bravedawn.latte.ec.main.personal.profile;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bravedawn.latte.delegates.LatteDelegate;
import cn.bravedawn.latte.ec.R;
import cn.bravedawn.latte.ec.R2;
import cn.bravedawn.latte.ec.database.DatabaseManager;
import cn.bravedawn.latte.ec.database.UserProfile;
import cn.bravedawn.latte.ec.main.personal.list.ListAdapter;
import cn.bravedawn.latte.ec.main.personal.list.ListBean;
import cn.bravedawn.latte.ec.main.personal.list.ListItemType;

/**
 * Created by 冯晓 on 2017/9/29.
 */

public class UserProfileDelegate extends LatteDelegate {

    @BindView(R2.id.rv_user_profile)
    RecyclerView mRecyclerView = null;

    @OnClick(R2.id.icon_user_profile_back)
    void onClickBack(){
        getSupportDelegate().pop();
    }

    @BindView(R2.id.user_profile_toolbar)
    Toolbar mToolbar = null;

    private String USER_NAME;
    private String USER_AVATAR;
    private String USER_GENDER;

    @Override
    public Object setLayout() {
        return R.layout.delegate_user_profile;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        mToolbar.setContentInsetsAbsolute(0, 0);
        initPersonInfo();
        final ListBean image = new ListBean.Builder()
                .setItemType(ListItemType.ITEM_AVATAR)
                .setId(1)
                .setImageUrl(USER_AVATAR)
                .build();
        final ListBean name = new ListBean.Builder()
                .setResImage(getResources().getIdentifier("name", "drawable", "cn.bravedawn.fastec.example"))
                .setItemType(ListItemType.ITEM_NORMAL)
                .setId(2)
                .setText("用户名")
                .setValue(USER_NAME)
                .build();

        final ListBean gender = new ListBean.Builder()
                .setResImage(getResources().getIdentifier("gender", "drawable", "cn.bravedawn.fastec.example"))
                .setItemType(ListItemType.ITEM_NORMAL)
                .setId(3)
                .setText("性别")
                .setValue(USER_GENDER)
                .build();

        final List<ListBean> data = new ArrayList<>();
        data.add(image);
        data.add(name);
        data.add(gender);

        //设置RecyclerView
        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(manager);
        final ListAdapter adapter = new ListAdapter(data);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addOnItemTouchListener(new UserProfileClickListener(this));
    }

    private void initPersonInfo(){
        UserProfile userProfile = DatabaseManager.getInstance().getDao().loadByRowId(1);
        USER_GENDER = userProfile.getGender();
        USER_AVATAR = userProfile.getAvatar();
        USER_NAME = userProfile.getName();
    }

}
