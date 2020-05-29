package com.book.command.handle;

import java.io.IOException;

public interface Handle<R> {
    boolean support(String url);
    void handle(String url,boolean sendMail) throws IOException;
    default R get() throws Throwable {
        return null;
    }
}
