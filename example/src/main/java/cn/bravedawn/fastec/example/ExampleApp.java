package cn.bravedawn.fastec.example;

import android.app.Application;

import com.joanzapata.iconify.fonts.FontAwesomeModule;

import cn.bravedawn.latte.app.Latte;
import cn.bravedawn.latte.ec.icon.FontEcModule;

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
                .withApiHost("http://127.0.0.1:8080")
                .configure();
    }
}
