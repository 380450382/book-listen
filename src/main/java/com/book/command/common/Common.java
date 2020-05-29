package com.book.command.common;

import com.book.command.enums.CacheOperateEnum;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Common {
    public static final String URL_REGEXT = "(ht|f)tp(s?)\\:\\/\\/[0-9a-zA-Z]([-.\\w]*[0-9a-zA-Z])*(:(0-9)*)*(\\/?)([a-zA-Z0-9\\-\\.\\?\\,\\'\\/\\\\\\+&amp;%\\$#_]*)?";
    public static final String MAIL_REGEXT = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";
    public static final String CLEAN = "clean";
    public static final String ON = "on";
    public static final String OFF = "off";
//    public final static String ENCODING_CODE = System.getProperty("file.encoding");
    public static final ThreadLocal<CacheOperateEnum> threadLocal = new ThreadLocal<>();
    public static final ExecutorService threadPool = new ThreadPoolExecutor(0, Integer.MAX_VALUE,
            3L, TimeUnit.SECONDS,
            new SynchronousQueue<>());
    static {
        threadLocal.set(CacheOperateEnum.CLEAN);
    }
    public static final void wait(Object lock, long time, TimeUnit timeUnit, Runnable error){
        synchronized (lock){
            try {
                lock.wait(timeUnit.toMillis(time));
            } catch (InterruptedException e) {
                if (error != null) {
                    error.run();
                }
            }
        }
    }
}
