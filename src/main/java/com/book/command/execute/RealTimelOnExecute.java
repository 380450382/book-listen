package com.book.command.execute;

import com.book.command.common.Common;
import com.book.command.enums.OptionEnum;
import com.book.command.enums.ResultEnum;
import com.book.command.execute.base.AbstractExecute;
import com.book.command.model.Book;
import com.book.command.util.CacheUtil;
import com.book.command.util.PrintUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.*;

public final class RealTimelOnExecute extends AbstractExecute<String> {
    public static class SubState{
        boolean stop;
        Thread current;
        String lastArticle;
        String bookName;

        public SubState(boolean stop, Thread current) {
            this.stop = stop;
            this.current = current;
        }

        public SubState(boolean stop, Thread current,String lastArticle) {
            this(stop,current);
            this.lastArticle = lastArticle;
        }

        public SubState(boolean stop, Thread current,String lastArticle,String bookName) {
            this(stop,current,lastArticle);
            this.bookName = bookName;
        }

        public SubState(boolean stop, Thread current,Book book) {
            this(stop,current,book.getLastArticle(),book.getBookName());
        }

        public SubState(String lastArticle) {
            this(true,null);
            this.lastArticle = lastArticle;
        }

        public SubState(Book book) {
            this(book.getLastArticle());
            this.bookName = book.getBookName();
        }
    }
    private Map<String,SubState> stopMap = new ConcurrentHashMap<>();
    private ExecutorService threadPool = new ThreadPoolExecutor(0, Integer.MAX_VALUE,
            1L, TimeUnit.SECONDS,
            new SynchronousQueue<Runnable>());
    private static RealTimelOnExecute realTimelOnExecute;
    private RealTimelOnExecute() {
    }
    public static RealTimelOnExecute getInstance(){
        if(realTimelOnExecute == null){
            synchronized (RealTimelOnExecute.class){
                if(realTimelOnExecute == null) {
                    realTimelOnExecute = new RealTimelOnExecute();
                }
            }
        }
        return realTimelOnExecute;
    }

    @Override
    public Integer execute(String url) {
        threadPool.execute(() -> {
            Thread th = Thread.currentThread();
            SubState subState = stopMap.computeIfAbsent(url, s -> new SubState(false, th, CacheUtil.getBook(url)));
            subState.current = th;
            subState.stop = false;
            while(!subState.stop) {
                if(Thread.interrupted()){
                    stopMap.get(url).stop = true;
                    th.interrupt();
                }
                OptionEnum.SUB_URL.exec(url,false);
                Book book = CacheUtil.getBook(url);
                if(Objects.nonNull(book) &&
                        (StringUtils.isBlank(subState.lastArticle) || !subState.lastArticle.equals(book.getLastArticle()))){
                    if(StringUtils.isBlank(subState.lastArticle)){
                        subState.bookName = book.getBookName();
                    }
                    subState.lastArticle = book.getLastArticle();
                    CacheUtil.storeCache();
                }
                synchronized (this){
                    try {
                        this.wait(5L*60*1000);
                    } catch (InterruptedException e) {
                        stopMap.get(url).stop = true;
                    }
                }
            }
        });
        return ResultEnum.SUCCESS.code();
    }

    @Override
    public void checkParam(String url) {
        if(StringUtils.isBlank(url) || !url.matches(Common.URL_REGEXT)){
            PrintUtil.print("请输入正确的url");
        }
    }

    public Map<String, SubState> getStopMap() {
        return stopMap;
    }
}
