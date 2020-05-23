package com.book.command.execute;

import com.book.command.enums.ResultEnum;
import com.book.command.execute.base.AbstractExecute;

import java.util.Map;

public final class RealTimelOffAllExecute extends AbstractExecute<String> {

    @Override
    public Integer execute(String param) {
        Map<String, RealTimelOnExecute.SubState> stopMap = RealTimelOnExecute.getInstance().getStopMap();
        stopMap.keySet().forEach(key -> {
            Thread current = stopMap.get(key).current;
            if(current != null){
                current.interrupt();
            }
        });
        return ResultEnum.SUCCESS.code();
    }
}
