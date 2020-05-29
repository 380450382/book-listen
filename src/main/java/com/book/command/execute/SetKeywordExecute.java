package com.book.command.execute;

import com.book.command.enums.ResultEnum;
import com.book.command.execute.base.AbstractExecute;
import com.book.command.util.CacheUtil;
import org.apache.commons.lang3.StringUtils;

public class SetKeywordExecute extends AbstractExecute<String> {
    @Override
    public Integer execute(String keyword) {
        CacheUtil.putKeywords(keyword.split(","));
        CacheUtil.storeCache();
        return ResultEnum.SUCCESS.code();
    }
    @Override
    public void checkParam(String keyword) {
        if (StringUtils.isBlank(keyword)) {
            throw new IllegalArgumentException("请输入每日推荐关键字");
        }
    }
}
