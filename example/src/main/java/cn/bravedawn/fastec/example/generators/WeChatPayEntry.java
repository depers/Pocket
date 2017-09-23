package cn.bravedawn.fastec.example.generators;

import cn.bravedawn.latte.annotations.PayEntryGenerator;
import cn.bravedawn.latte.wechat.templates.WXPayEntryTemplate;

/**
 * Created by 冯晓 on 2017/9/23.
 */

@PayEntryGenerator(
        packageName = "cn.bravedawn.fastec.example",
        payEntryTemplate = WXPayEntryTemplate.class
)
public interface WeChatPayEntry {
}
