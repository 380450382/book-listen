package com.book.command.execute.base;

import java.util.concurrent.Callable;
import java.util.function.Function;

public interface BaseExecute<S,R> extends Function<S,R>, Callable<R> {
    R execute(S param);
    default void checkParam(S param){
    }
    default void close(){
    }
}
