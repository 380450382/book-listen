package com.book.command.execute;

import com.book.command.common.Common;
import com.book.command.enums.CacheOperateEnum;
import com.book.command.enums.ResultEnum;
import com.book.command.execute.base.AbstractExecute;
import org.apache.commons.lang3.StringUtils;

public class CacheStatusExecute extends AbstractExecute<String> {
    @Override
    public Integer execute(String status) {
        Common.threadLocal.set(CacheOperateEnum.statusOf(status));
        return ResultEnum.SUCCESS.code();
    }
    @Override
    public void checkParam(String status) {
        if (StringUtils.isBlank(status)) {
            throw new IllegalArgumentException("请输入缓存操作状态");
        }
    }
}
