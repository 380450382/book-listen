package com.book.command.enums;

import org.apache.commons.lang3.StringUtils;

public enum  CacheOperateEnum {
    CLEAN("CLEAN"),
    ADD("ADD"),
    ;
    private String status;

    public String status() {
        return status;
    }

    CacheOperateEnum(java.lang.String status) {
        this.status = status;
    }
    public static CacheOperateEnum statusOf(String status){
        for (CacheOperateEnum value : values()) {
            if(StringUtils.equalsIgnoreCase(value.status,status)){
                return value;
            }
        }
        throw new IllegalArgumentException("没找到相应的缓存操作状态");
    }
}
