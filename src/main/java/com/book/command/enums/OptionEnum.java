package com.book.command.enums;

import com.book.command.execute.*;
import com.book.command.execute.base.BaseExecute;
import com.book.command.util.MessageUtil;
import com.book.command.util.PrintUtil;

public enum OptionEnum {
    SUB_URL("s","subUrl",true,"订阅书集的url",false,new SubUrlExecute()),
    HELP("h","help",false,"帮助",false,null),
    REAL_TIME_SUB_ON("ron","realTimeSubOn",true,"开启实时订阅书集的url",false, RealTimelOnExecute.getInstance()),
    REAL_TIME_SUB_OFF("roff","realTimeSubOff",true,"关闭实时订阅书集的url",false, new RealTimelOffExecute()),
    REAL_TIME_SUB_OFF_ALL("roffa","realTimeSubOffAll",false,"关闭所有实时订阅书集的url",false, new RealTimelOffAllExecute()),
    REAL_TIME_SUB_ON_ALL("rona","realTimeSubOnAll",false,"开启所有实时订阅书集的url",false, new RealTimelOnAllExecute()),
    REAL_TIME_SUB_INFO("si","realTimeSubInfo",false,"查看实时订阅信息",false, new RealTimelSubInfoExecute()),
    SET_MAIL_TO_INFO("smti","setMailToInfo",true,"设置接收人邮箱(xx@mm.com,xx@mm.com,clean清空)",false, new SetMailToInfoExecute()),
    CACHE_STATUS("cs","cacheStatus",true,"接下来cache的操作(默认clean;目前mail、keyword已实现)",false, new CacheStatusExecute()),
    DAILY_RECOMMEND("dr","dailyRecommend",true,"开启/关闭每日推荐(ON/OFF)",false, new DailyRecommendExecute()),
    SET_KEYWORD("sk","setKeyword",true,"设置每日推荐关键字(多个以,隔开)",false, new SetKeywordExecute()),
    ;

    private String command;
    private String fullCommand;
    private boolean hasArg;
    private String desc;
    private boolean required;
    private BaseExecute execute;

    public void exec(String val,boolean printResult) {
        Object result = execute.apply(val);
        if(printResult) {
            PrintUtil.print(MessageUtil.message("返回结果: {}", result),true);
        }
    }

    public void exec(String val) {
        exec(val,true);
    }

    public void exec() {
        exec(true);
    }

    public void exec(boolean printResult) {
        try {
            Object result = execute.call();
            if(printResult) {
                PrintUtil.print(MessageUtil.message("返回结果: {}", result),true);
            }
        } catch (Exception e) {
            PrintUtil.print(MessageUtil.message("call异常,{}",e.getMessage()));
        }
    }

    public String command() {
        return command;
    }

    public String fullCommand() {
        return fullCommand;
    }

    public boolean hasArg() {
        return hasArg;
    }

    public String desc() {
        return desc;
    }

    public boolean required() {
        return required;
    }

    OptionEnum(String command, String fullCommand, boolean hasArg, String desc, boolean required,BaseExecute execute) {
        this.command = command;
        this.fullCommand = fullCommand;
        this.hasArg = hasArg;
        this.desc = desc;
        this.required = required;
        this.execute = execute;
    }
}
