package cn.bravedawn.fastec.example.generators;

import cn.bravedawn.latte.annotations.EntryGenerator;
import cn.bravedawn.latte.wechat.templates.WXEntryTemplate;

/**
 * Created by 冯晓 on 2017/9/23.
 */

@EntryGenerator(
        packageName = "cn.bravedawn.fastec.example",
        entryTemplate = WXEntryTemplate.class
)
public interface WeChatEntry {
}
