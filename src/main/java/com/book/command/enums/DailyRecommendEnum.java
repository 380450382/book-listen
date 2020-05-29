package com.book.command.enums;

public enum DailyRecommendEnum {
    JUEJIN("https://juejin.im/search?query={keyword}&type=all","掘金"),
    JIANSHU("https://www.jianshu.com/search?q={keyword}&page=1&type=note","简书"),
    SEGMENTFAULT("https://segmentfault.com/search?q={keyword}","思否"),
    CSDN("https://so.csdn.net/so/search/s.do?q={keyword}&t=&u=","CSDN"),
    ;
    private String url;
    private String desc;

    public String url() {
        return url;
    }

    public String desc() {
        return desc;
    }

    DailyRecommendEnum(String url, String desc) {
        this.url = url;
        this.desc = desc;
    }
}
