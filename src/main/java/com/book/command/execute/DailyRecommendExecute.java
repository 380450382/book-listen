package com.book.command.execute;

import com.book.command.common.Common;
import com.book.command.enums.DailyRecommendEnum;
import com.book.command.enums.ResultEnum;
import com.book.command.execute.base.AbstractExecute;
import com.book.command.handle.Handle;
import com.book.command.handle.daily.AbstractDailyHandle;
import com.book.command.handle.daily.CSDNHandle;
import com.book.command.handle.daily.JianshuHandle;
import com.book.command.handle.daily.SegmentfaultHandle;
import com.book.command.util.CacheUtil;
import com.book.command.util.MessageUtil;
import com.book.command.util.PrintUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DailyRecommendExecute extends AbstractExecute<String> {

    private static Thread dailyThread = null;
    private final int NUM = 3;
    private volatile boolean stop = true;
    private List<AbstractDailyHandle> handles = new ArrayList<>();

    public static Thread getDailyThread() {
        return dailyThread;
    }

    {
        handles.add(new JianshuHandle());
        handles.add(new SegmentfaultHandle());
        handles.add(new CSDNHandle());
    }

    @Override
    public void checkParam(String open) {
        if (StringUtils.isBlank(open)){
            throw new IllegalArgumentException("请输入参数");
        }
        if(StringUtils.equalsIgnoreCase(Common.ON,open) || StringUtils.equalsIgnoreCase(Common.OFF,open)){
            return ;
        }
        throw new IllegalArgumentException("请输入ON/OFF");
    }

    @Override
    public Integer execute(String open) {
        if(StringUtils.equalsIgnoreCase(open,Common.ON)){
            if(!stop) {
                return ResultEnum.SUCCESS.code();
            }
            stop = false;
            Common.threadPool.execute(() -> {
                dailyThread = Thread.currentThread();
                while(!stop) {
                    if(Thread.interrupted()){
                        stop = true;
                        dailyThread.interrupt();
                    }
                    List<String> dailys = new ArrayList<>();
                    List<String> result = new ArrayList<>();
                    for (DailyRecommendEnum value : DailyRecommendEnum.values()) {
                        for (Handle handle : handles) {
                            String url = value.url();
                            url = url.replace("{keyword}", StringUtils.join(CacheUtil.getKeywords(), " "));
                            if (handle.support(url)) {
                                try {
                                    handle.handle(url, false);
                                    dailys.addAll((List<String>) handle.get());
                                } catch (Throwable e) {
                                    PrintUtil.print(MessageUtil.message("AbstractDailyHandle--->handle:{},{}",
                                            handle.getClass().getSimpleName(), e), true);
                                }
                                break;
                            }
                        }
                    }
                    for (int i = 0; i < NUM; i++) {
                        int size = dailys.size();
                        int random = (int) (Math.random() * size);
                        String daily = dailys.get(random);
                        result.add(daily);
                        dailys.remove(random);
                    }
                    try {
                        sendMail(result);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Common.wait(this, 1, TimeUnit.DAYS, null);
                }
            });
        } else {
            if (dailyThread != null) {
                stop = true;
                dailyThread.interrupt();
            }
        }
        return ResultEnum.SUCCESS.code();
    }

    private void sendMail(List<String> urls) throws IOException {
        for (String url : urls) {
            for (AbstractDailyHandle handle : handles) {
                if(handle.support(url)){
                    handle.sendMail(url);
                    break;
                }
            }
        }
    }
}
