package cn.bravedawn.fastec.example;

import android.app.Application;
import android.support.annotation.Nullable;

import com.facebook.stetho.Stetho;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

import cn.bravedawn.fastec.example.event.ShareEvent;
import cn.bravedawn.latte.app.Latte;
import cn.bravedawn.fastec.example.event.TestEvent;
import cn.bravedawn.latte.ec.database.DatabaseManager;
import cn.bravedawn.latte.ec.icon.FontEcModule;
import cn.bravedawn.latte.net.Interceptors.DebugInterceptor;
import cn.bravedawn.latte.net.rx.AddCookiesInterceptor;
import cn.bravedawn.latte.util.callback.CallBackManager;
import cn.bravedawn.latte.util.callback.CallBackType;
import cn.bravedawn.latte.util.callback.IGlobalCallback;
import cn.bravedawn.latte.util.clipboard.ClipboardUtil;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by 冯晓 on 2017/9/13.
 */

public class ExampleApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Latte.init(this)
                .withIcon(new FontAwesomeModule())
                .withIcon(new FontEcModule())
                // load延迟
                .withLoaderDelayed(1000)
                .withApiHost("http://202.201.52.153:8081/")
                // 数据源
                /*.withInterceptor(new DebugInterceptor("sign_up", R.raw.user_profile))
                //.withInterceptor(new DebugInterceptor("user_record", R.raw.test))
                .withInterceptor(new DebugInterceptor("sort_list", R.raw.sort_list_data))
                .withInterceptor(new DebugInterceptor("sort_content_list", R.raw.sort_content_data_1))
                .withInterceptor(new DebugInterceptor("shop_cart_data", R.raw.shop_cart_data))
                .withInterceptor(new DebugInterceptor("shop_cart_count", R.raw.shop_cart_data))
                .withInterceptor(new DebugInterceptor("order_list", R.raw.shop_cart_data))
                .withInterceptor(new DebugInterceptor("address", R.raw.address))
                .withInterceptor(new DebugInterceptor("about", R.raw.about))
                //.withInterceptor(new DebugInterceptor("refresh", R.raw.index_2_data))
                .withInterceptor(new DebugInterceptor("goods_detail", R.raw.goods_detail_data_1))*/
                // 微信
                .withWeChatAppId("wx7d79fb81a4d8bc9f")
                .withWeChatAppSecret("692710c7042b098ca24b57b81653c03a")
                // js事配置
                .withJavascriptInterface("latte")
                // web事件
                .withWebEvent("test", new TestEvent())
                .withWebEvent("share", new ShareEvent())
                // 添加cookie同步拦截器
                .withInterceptor(new AddCookiesInterceptor())
                // 浏览器加载的host
                .withWebHost("https://www.baidu/com/") //记得加一斜杆
                .configure();

        initStetho();
        DatabaseManager.getInstance().init(this);
        // 开启极光推送
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);

        // 通过接口开关极光推送
        CallBackManager.getInstance()
                .addCallBack(CallBackType.TAG_OPEN_PUSH, new IGlobalCallback() {
                    @Override
                    public void executeCallBack(@Nullable Object args) {
                        if (JPushInterface.isPushStopped(Latte.getApplicationContext())){
                            // 开启极光推送
                            JPushInterface.setDebugMode(true);
                            JPushInterface.init(Latte.getApplicationContext());
                        }
                    }
                })
                .addCallBack(CallBackType.TAG_STOP_PUSH, new IGlobalCallback() {
                    @Override
                    public void executeCallBack(@Nullable Object args) {
                        if (!JPushInterface.isPushStopped(Latte.getApplicationContext())){
                            JPushInterface.stopPush(Latte.getApplicationContext());
                        }
                    }
                });
    }

    private void initStetho(){
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                .build()
        );
    }

}
