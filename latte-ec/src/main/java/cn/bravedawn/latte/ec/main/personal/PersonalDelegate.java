package cn.bravedawn.latte.ec.main.personal;

import android.os.Bundle;
import android.support.annotation.IntegerRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bravedawn.latte.app.AccountManager;
import cn.bravedawn.latte.delegates.bottom.BottomItemDelegate;
import cn.bravedawn.latte.ec.R;
import cn.bravedawn.latte.ec.R2;
import cn.bravedawn.latte.ec.database.DatabaseManager;
import cn.bravedawn.latte.ec.database.UserProfile;
import cn.bravedawn.latte.ec.main.personal.list.ListAdapter;
import cn.bravedawn.latte.ec.main.personal.list.ListBean;
import cn.bravedawn.latte.ec.main.personal.list.ListItemType;
import cn.bravedawn.latte.ec.main.personal.profile.UserProfileDelegate;
import cn.bravedawn.latte.ec.main.personal.settings.SettingsDelegate;
import cn.bravedawn.latte.ec.sign.SignInDelegate;
import cn.bravedawn.latte.util.log.LatteLogger;
import cn.bravedawn.latte.util.storage.LattePreference;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by 冯晓 on 2017/9/29.
 */

public class PersonalDelegate extends BottomItemDelegate{

    @BindView(R2.id.rv_personal_setting)
    RecyclerView mRvSettings = null;

    @BindView(R2.id.personal_avatar)
    CircleImageView mCircleImageView = null;

    @BindView(R2.id.personal_name)
    AppCompatTextView mCompatTextView = null;


    @OnClick(R2.id.personal_avatar)
    void onClickAvatar(){
        getParentDelegate().getSupportDelegate().start(new UserProfileDelegate());
    }

    @OnClick(R2.id.modify_personal_info)
    void onClickInfo(){
        getParentDelegate().getSupportDelegate().start(new UserProfileDelegate());
    }

    @OnClick(R2.id.drop_out)
    void onClickDropUp(){
        String userId = LattePreference.getCustomAppProfile("userId");
        AccountManager.setSignState(false);
        UserProfile userProfile = new UserProfile();
        userProfile.setUserId(Integer.parseInt(userId));
        DatabaseManager.getInstance().getDao().delete(userProfile);
        getParentDelegate().getSupportDelegate().startWithPop(new SignInDelegate());
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_personal;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        initPersonInfo();

        final ListBean address = new ListBean.Builder()
                .setItemType(ListItemType.ITEM_SWITCH)
                .setResImage(getResources().getIdentifier("night", "drawable", "cn.bravedawn.fastec.example"))
                .setId(1)
                .setText("夜间模式")
                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked){
                            Toast.makeText(getContext(), "切换夜间模式", Toast.LENGTH_LONG).show();
                        } else{
                            Toast.makeText(getContext(), "关闭夜间模式", Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .build();
        final ListBean system = new ListBean.Builder()
                .setItemType(ListItemType.ITEM_NORMAL)
                .setResImage(getResources().getIdentifier("setting", "drawable", "cn.bravedawn.fastec.example"))
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


    private void initPersonInfo(){
        String userId = LattePreference.getCustomAppProfile("userId");
        UserProfile userProfile = DatabaseManager.getInstance().getDao().loadByRowId(Integer.parseInt(userId));
        Glide.with(getContext())
                .load(userProfile.getAvatar())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .dontAnimate()
                .into(mCircleImageView);
        mCompatTextView.setText(userProfile.getName());
    }


}
