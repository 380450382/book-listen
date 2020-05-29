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

public class CSDNHandle extends AbstractDailyHandle<List> {
    @Override
    public boolean support(String url) {
        try {
            URL csdnUrl = new URL(DailyRecommendEnum.CSDN.url().replace("{keyword}",""));
            URL URL = new URL(url);
            String csdnHost = csdnUrl.getHost().substring(csdnUrl.getHost().indexOf("."));
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
        Element list = document.getElementsByClass("search-list-con").get(0);
        Elements dl = list.getElementsByTag("dl");
        dl.forEach(element -> {
            Element title = element.getElementsByTag("a").get(0);
            String href = title.attr("href");
            result.add(href);
        });
        return result;
    }

    @Override
    public void sendMail(String url) throws IOException {
        Document document = Jsoup.connect(url).get();
        String title = document.getElementsByClass("title-article").get(0).html();
        String content = document.getElementsByTag("article").get(0).html();
        MailUtil.send(title,content, CacheUtil.getTos());
        PrintUtil.print(MessageUtil.message("<{}_{}>已发送", title, DailyRecommendEnum.CSDN.desc()));
    }
}
