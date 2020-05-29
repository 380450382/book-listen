package com.book.command.handle.daily;

import com.book.command.common.Common;
import com.book.command.handle.Handle;
import com.book.command.util.PrintUtil;
import sun.misc.Unsafe;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.LockSupport;

public abstract class AbstractDailyHandle<R> implements Handle<R>, java.io.Serializable{
    private static final long serialVersionUID = -696326965154220925L;
    private Queue<Thread> waitQueue = new LinkedBlockingQueue<>();
    private final int NEW = 0;
    private final int EXECUTION = 1;
    private final int SUCCESS = 2;
    private final int FAIL = 3;
    private volatile int state = 0;

    private static final Unsafe unsafe;
    private static final long stateOffset;
    static {
        try {
            Field f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            unsafe = (Unsafe) f.get(null);
            stateOffset = unsafe.objectFieldOffset
                    (AbstractDailyHandle.class.getDeclaredField("state"));
        } catch (Exception ex) {
            throw new Error(ex);
        }
    }
    private R result;
    private Throwable throwable;
    @Override
    public R get() throws Throwable {
        start:
        if(state <= EXECUTION){
            Thread currentThread = Thread.currentThread();
            if(waitQueue.offer(currentThread)) {
                LockSupport.park();
            } else {
                PrintUtil.print("AbstractDailyHandle:等待队列添加失败");
            }
            break start;
        }
        if(state == SUCCESS) {
            return result;
        }
        throw throwable;
    }
    protected final boolean start(){
        return unsafe.compareAndSwapInt(this,stateOffset,NEW,EXECUTION);
    }
    protected final boolean success(){
        if(unsafe.compareAndSwapInt(this,stateOffset,EXECUTION,SUCCESS)){

            return true;
        }
        return false;
    }
    protected final boolean fail(Throwable t){
        throwable = t;
        return unsafe.compareAndSwapInt(this,stateOffset,EXECUTION,FAIL);
    }
    protected void signalAll(){
        Thread thread;
        while ((thread = waitQueue.poll()) != null){
            LockSupport.unpark(thread);
        }
    }

    @Override
    public void handle(String url, boolean sendMail) {
        Common.threadPool.execute(() -> {
            start();
            try {
                result = execute(url);
                success();
            } catch (Exception e) {
                fail(e);
            }
            signalAll();
        });
    }
    public abstract R execute(String url) throws IOException;
    public abstract void sendMail(String url) throws IOException;
}
