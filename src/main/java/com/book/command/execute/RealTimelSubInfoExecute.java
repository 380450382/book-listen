package com.book.command.execute;

import com.book.command.common.Common;
import com.book.command.enums.ResultEnum;
import com.book.command.execute.base.AbstractExecute;
import com.book.command.util.CacheUtil;
import com.book.command.util.PrintUtil;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

public final class RealTimelSubInfoExecute extends AbstractExecute<String> {

    @Override
    public Integer execute(String url) {
        Map<String, RealTimelOnExecute.SubState> stopMap = RealTimelOnExecute.getInstance().getStopMap();
        PrintUtil.print("订阅信息:");
        Set<String> urls = stopMap.keySet();
        if(CollectionUtils.isNotEmpty(urls)){
            urls.forEach(key -> {
                RealTimelOnExecute.SubState subState = stopMap.get(key);
                System.out.printf("书集url: %s \t状态: %s \t线程状态: %s \t书名: %s \t当前最新章节：%s\r\n",key,!subState.stop,
                        subState.current!=null?subState.current.isAlive():false,subState.bookName ,subState.lastArticle);
            });
        } else {
            PrintUtil.print("还未订阅书籍");
        }
        PrintUtil.print("----------------------------------");
        PrintUtil.print("接收邮箱:");
        Set<String> tos = CacheUtil.getTo();
        if(CollectionUtils.isNotEmpty(tos)){
            for (String to : tos) {
                System.out.printf("邮箱: %s\r\n",to);
            }
        } else {
            PrintUtil.print("还未设置邮箱");
        }
        PrintUtil.print("----------------------------------");
        PrintUtil.print("目录缓存操作状态:");
        System.out.printf("缓存操作状态: %s\r\n", Common.threadLocal.get());
        return ResultEnum.SUCCESS.code();
    }
}
