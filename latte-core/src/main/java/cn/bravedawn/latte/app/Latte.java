package cn.bravedawn.latte.app;

import android.content.Context;

import java.util.HashMap;
import java.util.WeakHashMap;

/**
 * Created by 冯晓 on 2017/9/13.
 */

public final class Latte {

    public static Configurtor init(Context context){
        getConfigurations().put(ConfigType.APPLICATION_CONTENT.name(), context.getApplicationContext());
        return Configurtor.getInstance();
    }

    public static HashMap<String, Object> getConfigurations(){
        return Configurtor.getInstance().getLatteConfigs();
    }

    public static Context getApplication(){
        return (Context) getConfigurations().get(ConfigType.APPLICATION_CONTENT.name());
    }

}
