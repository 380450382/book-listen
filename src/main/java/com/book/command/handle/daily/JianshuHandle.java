package com.book.command.handle.daily;

import com.book.command.enums.DailyRecommendEnum;
import com.book.command.util.CacheUtil;
import com.book.command.util.MailUtil;
import com.book.command.util.MessageUtil;
import com.book.command.util.PrintUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class JianshuHandle extends AbstractDailyHandle<List> {
    @Override
    public boolean support(String url) {
        return false;
    }

    @Override
    public List<String> execute(String url) throws IOException {
        URL URL = new URL(url);
        String host = URL.getHost();
        String protocol = URL.getProtocol();
        List<String> result = new ArrayList<>();
        Document document = Jsoup.connect(url).get();
        Element noteList = document.getElementsByClass("note-list").get(0);
        Elements li = noteList.getElementsByTag("li");
        li.forEach(element -> {
            Element title = element.getElementsByClass("title").get(0);
            String href = title.attr("href");
            result.add(protocol + "://" + host + href);
        });
        return result;
    }

    @Override
    public void sendMail(String url) throws IOException {
        Document document = Jsoup.connect(url).get();
        String title = document.getElementsByClass("_1RuRku").get(0).html();
        String content = document.getElementsByTag("article").get(0).html();
        MailUtil.send(title,content, CacheUtil.getTos());
        PrintUtil.print(MessageUtil.message("<{}_{}>已发送",title, DailyRecommendEnum.JIANSHU.desc()));
    }
}
