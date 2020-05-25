package com.book.command.handle;

import com.book.command.common.Common;
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

public class Biquge2Handle implements Handle{
    @Override
    public boolean support(String url) {
        return StringUtils.isNotBlank(url) && url.contains("www.biquge.info");
    }

    @Override
    public void handle(String url,boolean sendMail) throws IOException {
        Book book = CacheUtil.getBook(url);
        Document document = Jsoup.connect(url).get();
        Element info = document.getElementById("info");
        String bookName = info.getElementsByTag("h1").get(0).html();
        Elements lastElement = info.getElementsByTag("a");
        String last = lastElement.get(0).html();
        String lastUrl = lastElement.get(0).attr("href");
        if(Objects.isNull(book) || StringUtils.isBlank(book.getLastArticle()) || !book.getLastArticle().equals(last)){
            PrintUtil.print(MessageUtil.message("<{}>已更新", bookName));
            if(Objects.isNull(book)){
                book = new Book();
            }
            book.setLastArticle(last);
            book.setBookName(bookName);
            Document lastDocument = Jsoup.connect(url + lastUrl).get();
            String title = lastDocument.getElementsByClass("bookname").get(0).getElementsByTag("h1").get(0).html();
            String content = lastDocument.getElementById("content").html();
            if(sendMail) {
                PrintUtil.print(MessageUtil.message("<{}>发送中...", bookName));
                MailUtil.send(title + "_" + bookName, content, CacheUtil.getTo());
                PrintUtil.print(MessageUtil.message("<{}>已发送", bookName));
            }
            CacheUtil.putBook(url,book);
            PrintUtil.print("本地缓存已更新");
        }
    }
}
