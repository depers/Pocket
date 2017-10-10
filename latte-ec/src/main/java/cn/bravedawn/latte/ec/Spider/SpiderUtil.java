package cn.bravedawn.latte.ec.Spider;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import cn.bravedawn.latte.ec.R;
import cn.bravedawn.latte.ec.bean.Record;
import cn.bravedawn.latte.util.log.LatteLogger;


/**
 * Created by 冯晓 on 2017/5/9.
 */

public class SpiderUtil {

    private static String[] colorRes = {
        "#00BCD4", "#4CAF50", "#03A9F4", "#FF9800", "#CDDC39", "#FFC107", "#E91E63"};

    public static Record getRecordFromSpider(String url) throws IOException {

        Connection.Response response = Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36")
                .timeout(300)
                .execute();

        int statusCode = response.statusCode();
        LatteLogger.d("statusCode", statusCode);
        if(statusCode == 200){
            Document document = response.parse();
            Record record = new Record();
            record.setTitle(document.title());
            record.setUrl(document.location());
            record.setColorAvatar(colorRes[new Random().nextInt(colorRes.length)]);
            return record;
        }

        return new Record();
    }
}
