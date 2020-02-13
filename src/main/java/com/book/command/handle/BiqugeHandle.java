package com.book.command.handle;

import com.book.command.util.MailUtil;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class BiqugeHandle implements Handle{
    private Map<String,String> bookCache = new HashMap<>();
    @Override
    public boolean support(String url) {
        return StringUtils.isNotBlank(url) && url.contains("www.biquge.lu");
    }

    @Override
    public void handle(String url) throws IOException {
        String bookLast = bookCache.get(url);
        URL URL = new URL(url);
        String host = URL.getHost();
        String protocol = URL.getProtocol();
        Document document = Jsoup.connect(url).get();
        String bookName = document.getElementsByClass("info").get(0).getElementsByTag("h2").get(0).html();
        Elements lastElement = document.getElementsByClass("last").get(1).getElementsByTag("a");
        String last = lastElement.html();
        String lastUrl = lastElement.get(0).attr("href");
        if(StringUtils.isBlank(bookLast) || !bookLast.equals(last)){
            System.out.println("已更新，发送中...");
            bookCache.put(url,last);
            Document lastDocument = Jsoup.connect(protocol + "://" + host + lastUrl).get();
            Element contentElement = lastDocument.getElementsByClass("content").get(0);
            String title = contentElement.getElementsByTag("h1").html();
            String content = contentElement.getElementById("content").html();
            MailUtil.send(title + "_" + bookName,content);
            System.out.println("已发送");
        }
//        else {
//            System.out.println("还未更新");
//        }
    }
}
