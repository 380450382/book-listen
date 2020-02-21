package com.book.command.execute;

import java.util.Map;
import java.util.function.Function;

public final class RealTimelOffAllExecute<R> implements Function<String,R> {
    @Override
    public R apply(String arg) {
        Map<String, RealTimelOnExecute.SubState> stopMap = RealTimelOnExecute.getInstance().getStopMap();
        stopMap.keySet().forEach(key -> {
            Thread current = stopMap.get(key).current;
            if(current != null){
                current.interrupt();
            }
        });
        return null;
    }
}
