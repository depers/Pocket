package cn.bravedawn.latte.ec.main.channel;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import cn.bravedawn.latte.delegates.bottom.BottomItemDelegate;
import cn.bravedawn.latte.ec.R;

/**
 * Created by 冯晓 on 2017/10/10.
 */

public class ChannelDelegate extends BottomItemDelegate {


    @Override
    public Object setLayout() {
        return R.layout.deleage_channel;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.channel_menu, menu);
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item == null){
            return false;
        }
        int i = item.getItemId();
        if (i == R.id.channel_action_refresh) {
            // TODO: 2017/10/10 这里做点击事件 
        }
        
        return true;
    }
}
