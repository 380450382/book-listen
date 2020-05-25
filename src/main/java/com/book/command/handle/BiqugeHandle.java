package com.book.command.handle;

import com.book.command.execute.SetMailToInfoExecute;
import com.book.command.model.Book;
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
import java.net.URL;
import java.util.Objects;

public class BiqugeHandle implements Handle{
    @Override
    public boolean support(String url) {
        return StringUtils.isNotBlank(url) && url.contains("www.biquge.lu");
    }

    @Override
    public void handle(String url,boolean sendMail) throws IOException {
        Book book = CacheUtil.getBook(url);
        URL URL = new URL(url);
        String host = URL.getHost();
        String protocol = URL.getProtocol();
        Document document = Jsoup.connect(url).get();
        String bookName = document.getElementsByClass("info").get(0).getElementsByTag("h2").get(0).html();
        Elements lastElement = document.getElementsByClass("last").get(1).getElementsByTag("a");
        String last = lastElement.html();
        String lastUrl = lastElement.get(0).attr("href");
        if(Objects.isNull(book) || StringUtils.isBlank(book.getLastArticle()) || !book.getLastArticle().equals(last)){
            PrintUtil.print(MessageUtil.message("<{}>已更新", bookName));
            if(Objects.isNull(book)){
                book = new Book();
            }
            book.setLastArticle(last);
            book.setBookName(bookName);
            Document lastDocument = Jsoup.connect(protocol + "://" + host + lastUrl).get();
            Element contentElement = lastDocument.getElementsByClass("content").get(0);
            String title = contentElement.getElementsByTag("h1").html();
            String content = contentElement.getElementById("content").html();
            if(sendMail) {
                PrintUtil.print(MessageUtil.message("<{}>发送中...", bookName));
                MailUtil.send(title + "_" + bookName, content, CacheUtil.getTo());
                PrintUtil.print(MessageUtil.message("<{}>已发送", bookName));
            }
            CacheUtil.putBook(url,book);
            PrintUtil.print("本地缓存已更新");
        }
//        else {
//            PrintUtil.print("还未更新");
//        }
    }
}
