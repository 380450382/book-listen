package com.book.command.execute.base;

import com.book.command.enums.ResultEnum;
import com.book.command.util.MessageUtil;
import com.book.command.util.PrintUtil;

import java.util.Objects;

public abstract class AbstractExecute<P> implements BaseExecute<P,Integer> {
    @Override
    public Integer apply(P p) {
        long start = System.currentTimeMillis();
        try {
            if(Objects.nonNull(p)){
                PrintUtil.print(MessageUtil.message("入参: {}, 开始执行", p));
            }
            checkParam(p);
            return execute(p);
        } catch (IllegalArgumentException e){
            PrintUtil.print(e.getMessage());
        } catch (Exception e){
            PrintUtil.print("系统异常");
        } finally {
            close();
            PrintUtil.print(MessageUtil.message("{},耗时: {}ms",this.getClass().getSimpleName(),System.currentTimeMillis() - start));
        }
        return ResultEnum.FAIL.code();
    }

    @Override
    public Integer call() {
        return apply(null);
    }
}
