package com.book.command.handle.daily;

import com.book.command.enums.DailyRecommendEnum;
import com.book.command.util.CacheUtil;
import com.book.command.util.MailUtil;
import com.book.command.util.MessageUtil;
import com.book.command.util.PrintUtil;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SegmentfaultHandle extends AbstractDailyHandle<List> {
    @Override
    public boolean support(String url) {
        try {
            URL segmentfaultUrl = new URL(DailyRecommendEnum.SEGMENTFAULT.url().replace("{keyword}",""));
            URL URL = new URL(url);
            String csdnHost = segmentfaultUrl.getHost().substring(segmentfaultUrl.getHost().indexOf("."));
            String URLHost = URL.getHost().substring(URL.getHost().indexOf("."));
            return StringUtils.equalsIgnoreCase(csdnHost,URLHost);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<String> execute(String url) throws IOException {
        URL URL = new URL(url);
        String host = URL.getHost();
        String protocol = URL.getProtocol();
        List<String> result = new ArrayList<>();
        Document document = Jsoup.connect(url).get();
        Elements widgetBlog = document.getElementsByClass("widget-blog");
        widgetBlog.forEach(element -> {
            Element title = element.getElementsByTag("a").get(0);
            String href = title.attr("href");
            result.add(protocol + "://" + host + href);
        });
        return result;
    }

    @Override
    public void sendMail(String url) throws IOException {
        Document document = Jsoup.connect(url).get();
        String title = document.getElementById("sf-article_title").getElementsByTag("a").get(0).html();
        String content = document.getElementsByTag("article").get(0).html();
        MailUtil.send(title,content, CacheUtil.getTos());
        PrintUtil.print(MessageUtil.message("<{}_{}>已发送", title,DailyRecommendEnum.SEGMENTFAULT.desc()));
    }
}
