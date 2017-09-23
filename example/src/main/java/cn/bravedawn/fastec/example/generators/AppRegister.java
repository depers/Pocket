package cn.bravedawn.fastec.example.generators;

import cn.bravedawn.latte.annotations.AppRegisterGenerator;
import cn.bravedawn.latte.wechat.templates.AppRegisterTemplate;

/**
 * Created by 冯晓 on 2017/9/23.
 */
@AppRegisterGenerator(
        packageName = "cn.bravedawn.fastec.example",
        registerTemplate = AppRegisterTemplate.class
)
public interface AppRegister {
}
