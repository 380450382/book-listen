package com.book.command.common;

import com.book.command.enums.CacheOperateEnum;

public class Common {
    public static final String URL_REGEXT = "(ht|f)tp(s?)\\:\\/\\/[0-9a-zA-Z]([-.\\w]*[0-9a-zA-Z])*(:(0-9)*)*(\\/?)([a-zA-Z0-9\\-\\.\\?\\,\\'\\/\\\\\\+&amp;%\\$#_]*)?";
    public static final String MAIL_REGEXT = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";
    public static final String CLEAN = "clean";
//    public final static String ENCODING_CODE = System.getProperty("file.encoding");
    public static final ThreadLocal<CacheOperateEnum> threadLocal = new ThreadLocal<>();
    static {
        threadLocal.set(CacheOperateEnum.CLEAN);
    }
}
