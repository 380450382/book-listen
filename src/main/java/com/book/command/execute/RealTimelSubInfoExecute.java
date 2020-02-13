package com.book.command.execute;

import java.util.Map;
import java.util.function.Function;

public final class RealTimelSubInfoExecute<R> implements Function<String,R> {
    @Override
    public R apply(String arg) {
        Map<String, RealTimelOnExecute.SubState> stopMap = RealTimelOnExecute.getInstance().getStopMap();
        System.out.println("订阅信息:");
        stopMap.keySet().forEach(key -> {
            RealTimelOnExecute.SubState subState = stopMap.get(key);
            System.out.printf("书集url: %s \t状态: %s \t线程状态: %s\r\n",key,!subState.stop,subState.current.isAlive());
        });
        return null;
    }
}
