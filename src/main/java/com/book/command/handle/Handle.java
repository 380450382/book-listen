package com.book.command.handle;

import java.io.IOException;

public interface Handle {
    boolean support(String url);
    void handle(String url) throws IOException;
}
