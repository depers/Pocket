package cn.bravedawn.latte.ec.detail;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;

/**
 * Created by 冯晓 on 2017/10/2.
 */

public class TabPagerAdapter extends FragmentStatePagerAdapter{

    private final ArrayList<String> TAB_TITELS = new ArrayList<>();
    private final ArrayList<ArrayList<String>> PICTURE = new ArrayList<>();


    public TabPagerAdapter(FragmentManager fm, JSONObject data) {
        super(fm);
        // 获取tabs信息，注意，这里的tabs是一条信息
        final JSONArray tabs = data.getJSONArray("tabs");
        final int size = tabs.size();
        for (int i = 0; i <size; i++){
            final JSONObject eachTab = tabs.getJSONObject(i);
            final String name = eachTab.getString("name");
            final JSONArray array = eachTab.getJSONArray("pictures");
            final ArrayList<String> pictures = new ArrayList<>();
            final int picSize = array.size();
            for (int j = 0; j < picSize; j++){
                pictures.add(array.getString(j));
            }
            PICTURE.add(pictures);
            TAB_TITELS.add(name);
        }

    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0){
            return ImageDelegate.create(PICTURE.get(0));
        } else if (position == 1){
            return ImageDelegate.create(PICTURE.get(1));
        }
        return null;
    }

    @Override
    public int getCount() {
        return TAB_TITELS.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TAB_TITELS.get(position);
    }
}
