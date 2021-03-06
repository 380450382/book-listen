package com.book.command.execute;

import com.book.command.common.Common;
import com.book.command.enums.ResultEnum;
import com.book.command.execute.base.AbstractExecute;
import com.book.command.util.CacheUtil;
import com.book.command.util.MessageUtil;
import com.book.command.util.PrintUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
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
                PrintUtil.print(MessageUtil.message("书集url: {} \t状态: {} \t线程状态: {} \t书名: {} \t当前最新章节：{}",key,!subState.stop,
                        subState.current!=null?subState.current.isAlive():false,subState.bookName ,subState.lastArticle));
            });
        } else {
            PrintUtil.print("还未订阅书籍");
        }
        PrintUtil.print("----------------------------------");
        PrintUtil.print("接收邮箱:");
        Set<String> tos = CacheUtil.getTos();
        if(CollectionUtils.isNotEmpty(tos)){
            for (String to : tos) {
                PrintUtil.print(MessageUtil.message("邮箱: {}",to));
            }
        } else {
            PrintUtil.print("还未设置邮箱");
        }
        PrintUtil.print("----------------------------------");
        PrintUtil.print("目前缓存操作状态:");
        PrintUtil.print(MessageUtil.message("缓存操作状态: {}",Common.threadLocal.get()));
        PrintUtil.print("----------------------------------");
        PrintUtil.print("每日推荐:");
        Thread dailyThread = DailyRecommendExecute.getDailyThread();
        boolean isAlive = dailyThread != null ? dailyThread.isAlive() : false;
        PrintUtil.print(MessageUtil.message("每日定时是否存活:{} \t关键词: {}"
                , isAlive, StringUtils.join(CacheUtil.getKeywords(),",")));
        return ResultEnum.SUCCESS.code();
    }
}
