package com.book.command.execute.base;

import com.book.command.enums.ResultEnum;
import com.book.command.util.MessageUtil;
import org.apache.commons.lang3.time.DateUtils;

import java.util.Date;
import java.util.Objects;

public abstract class AbstractExecute<P> implements BaseExecute<P,Integer> {
    @Override
    public Integer apply(P p) {
        long start = System.currentTimeMillis();
        try {
            if(Objects.nonNull(p)){
                System.out.println(MessageUtil.message("入参: {}, 开始执行", p));
            }
            checkParam(p);
            return execute(p);
        } catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
        } catch (Exception e){
            System.out.println("系统异常");
        } finally {
            close();
            System.out.println(MessageUtil.message("{},耗时: {}ms",this.getClass().getSimpleName(),System.currentTimeMillis() - start));
        }
        return ResultEnum.FAIL.code();
    }

    @Override
    public Integer call() {
        return apply(null);
    }
}
