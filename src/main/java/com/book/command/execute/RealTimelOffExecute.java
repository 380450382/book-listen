package com.book.command.execute;

import com.book.command.common.Common;
import com.book.command.enums.ResultEnum;
import com.book.command.execute.base.AbstractExecute;
import com.book.command.util.PrintUtil;
import org.apache.commons.lang3.StringUtils;

public final class RealTimelOffExecute extends AbstractExecute<String> {
    @Override
    public Integer execute(String url) {
        Thread current = RealTimelOnExecute.getInstance().getStopMap().get(url).current;
        if(current != null){
            current.interrupt();
        }
        return ResultEnum.SUCCESS.code();
    }

    @Override
    public void checkParam(String url) {
        if(StringUtils.isBlank(url) || !url.matches(Common.URL_REGEXT)){
            PrintUtil.print("请输入正确的url");
        }
    }
}
