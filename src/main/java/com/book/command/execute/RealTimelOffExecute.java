package com.book.command.execute;

import java.util.Map;
import java.util.function.Function;

public final class RealTimelOffExecute<R> implements Function<String,R> {
    @Override
    public R apply(String url) {
        ((Map<String, RealTimelOnExecute.SubState>)RealTimelOnExecute.getInstance().getStopMap()).get(url).current.interrupt();
        return null;
    }
}
