package cn.bravedawn.latte.app;

import com.joanzapata.iconify.IconFontDescriptor;
import com.joanzapata.iconify.Iconify;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.WeakHashMap;

/**
 * Created by 冯晓 on 2017/9/13.
 */

public class Configurtor {

    // 初始化配置
    private static final HashMap<String, Object> LATTE_CONFIGS = new HashMap<>();
    // 字体图标配置
    private static final ArrayList<IconFontDescriptor> ICONS = new ArrayList<>();

    final HashMap<String, Object> getLatteConfigs(){
        return LATTE_CONFIGS;
    }

    private Configurtor(){
        LATTE_CONFIGS.put(ConfigType.CONFIG_READY.name(), false);
    }

    private static class Holder{
        private static final Configurtor INSTACE = new Configurtor();
    }

    public static Configurtor getInstance(){
        return Holder.INSTACE;
    }

    public final void configure(){
        initIcons();
        LATTE_CONFIGS.put(ConfigType.CONFIG_READY.name(), true);
    }

    public final Configurtor withApiHost(String host){
        LATTE_CONFIGS.put(ConfigType.API_HOST.name(), host);
        return this;
    }

    public final Configurtor withIcon(IconFontDescriptor descriptor){
        ICONS.add(descriptor);
        return this;
    }

    private void initIcons(){
        if (ICONS.size() > 0){
            final Iconify.IconifyInitializer initializer = Iconify.with(ICONS.get(0));
            for (int i = 1; i < ICONS.size(); i++){
                initializer.with(ICONS.get(i));
            }
        }
    }

    private void checkConfiguration(){
        final boolean isReady = (boolean) LATTE_CONFIGS.get(ConfigType.APPLICATION_CONTENT.name());
        if (!isReady){
            throw new RuntimeException("Configuration is not ready, call configure");
        }
    }

    @SuppressWarnings("unchecked")
    final <T> T getconfiguration(Enum<ConfigType> key){
        checkConfiguration();
        return (T) LATTE_CONFIGS.get(key.name());
    }



}
