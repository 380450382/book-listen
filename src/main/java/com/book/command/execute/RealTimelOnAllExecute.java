package com.book.command.execute;

import com.book.command.enums.OptionEnum;

import java.util.Map;
import java.util.function.Function;

public final class RealTimelOnAllExecute<R> implements Function<String, R> {
    @Override
    public R apply(String arg) {
        Map<String, RealTimelOnExecute.SubState> stopMap = RealTimelOnExecute.getInstance().getStopMap();
        stopMap.keySet().forEach(key -> {
            RealTimelOnExecute.SubState subState = stopMap.get(key);
            if(subState.stop != false && !subState.current.isAlive()) {
                subState.stop = false;
                OptionEnum.REAL_TIME_SUB_ON.exec(key);
            }
        });
        return null;
    }
}
