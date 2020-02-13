package com.book.command.execute;

import com.book.command.common.Common;
import com.book.command.handle.BiqugeHandle;
import com.book.command.handle.Handle;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class SubUrlExecute<R> implements Function<String,R> {
    private List<Handle> handles = new ArrayList<>();
    {
        handles.add(new BiqugeHandle());
    }
    @Override
    public R apply(String url) {
        if(StringUtils.isBlank(url) || !url.matches(Common.URL_REGEXT)){
            System.out.println("请输入正确的url");
            return null;
        }
        for (Handle handle : handles) {
            if(handle.support(url)){
                try {
                    handle.handle(url);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }
        System.out.println("没找到相应的处理器");
        return null;
    }
}
