package com.book.command.execute;

import com.book.command.common.Common;
import com.book.command.enums.ResultEnum;
import com.book.command.execute.base.AbstractExecute;
import com.book.command.handle.Biquge2Handle;
import com.book.command.handle.BiqugeHandle;
import com.book.command.handle.Handle;
import com.book.command.util.MessageUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SubUrlExecute extends AbstractExecute<String> {
    private List<Handle> handles = new ArrayList<>();
    {
        handles.add(new BiqugeHandle());
        handles.add(new Biquge2Handle());
    }

    @Override
    public Integer execute(String url) {
        for (Handle handle : handles) {
            if(handle.support(url)){
                try {
                    handle.handle(url,true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return ResultEnum.SUCCESS.code();
            }
        }
        System.out.println("没找到相应的处理器");
        return ResultEnum.SUCCESS.code();
    }

    @Override
    public void checkParam(String url) {
        if(StringUtils.isBlank(url) || !url.matches(Common.URL_REGEXT)){
            new IllegalArgumentException(MessageUtil.message("{},格式不正确,请输入正确的url",url));
        }
    }
}
