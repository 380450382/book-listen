package com.book.command.enums;

public enum  ResultEnum {
    SUCCESS("成功",200),
    FAIL("失败",500),
    ;
    private String desc;
    private int code;

    public String desc() {
        return desc;
    }

    public int code() {
        return code;
    }

    ResultEnum(String desc, int code) {
        this.desc = desc;
        this.code = code;
    }
}
