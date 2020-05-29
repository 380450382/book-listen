package com.book.command.util;

public final class MessageUtil {
    public static String message(String temp, Object... params){
        String message = temp;
        for (Object param : params) {
            message = message.replaceFirst("\\{}",param.toString());
        }
        return message;
    }
}
