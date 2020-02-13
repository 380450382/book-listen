package com.book.command.execute;

import com.book.command.common.Common;
import com.book.command.enums.OptionEnum;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.concurrent.*;
import java.util.function.Function;

public final class RealTimelOnExecute<R> implements Function<String,R> {
    class SubState{
        boolean stop;
        Thread current;

        public SubState(boolean stop, Thread current) {
            this.stop = stop;
            this.current = current;
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
    public R apply(String url) {
        if(StringUtils.isBlank(url) || !url.matches(Common.URL_REGEXT)){
            System.out.println("请输入正确的url");
            return null;
        }
        threadPool.execute(() -> {
            Thread th = Thread.currentThread();
            SubState subState = stopMap.computeIfAbsent(url, s -> new SubState(false, th));
            subState.current = th;
            subState.stop = false;
            while(!subState.stop) {
                if(Thread.interrupted()){
                    stopMap.get(url).stop = true;
                    th.interrupt();
                }
                OptionEnum.SUB_URL.exec(url);
                synchronized (this){
                    try {
                        this.wait(5L*60*1000);
                    } catch (InterruptedException e) {
                        stopMap.get(url).stop = true;
                    }
                }
            }
        });
        return null;
    }
    public Map<String, SubState> getStopMap() {
        return stopMap;
    }
}
