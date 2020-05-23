package com.book.command.execute;

import com.book.command.enums.OptionEnum;
import com.book.command.enums.ResultEnum;
import com.book.command.execute.base.AbstractExecute;

import java.util.Map;

public final class RealTimelOnAllExecute extends AbstractExecute<String> {

    @Override
    public Integer execute(String param) {
        Map<String, RealTimelOnExecute.SubState> stopMap = RealTimelOnExecute.getInstance().getStopMap();
        stopMap.keySet().forEach(key -> {
            RealTimelOnExecute.SubState subState = stopMap.get(key);
            if(subState.stop != false && (subState.current == null || !subState.current.isAlive())) {
                subState.stop = false;
                OptionEnum.REAL_TIME_SUB_ON.exec(key,false);
            }
        });
        return ResultEnum.SUCCESS.code();
    }
}
