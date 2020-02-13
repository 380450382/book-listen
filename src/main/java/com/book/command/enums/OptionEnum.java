package com.book.command.enums;

import com.book.command.execute.*;

import java.util.function.Function;

public enum OptionEnum {
    SUB_URL("s","subUrl",true,"订阅书集的url",false,new SubUrlExecute()),
    HELP("h","help",false,"帮助",false,null),
    REAL_TIME_SUB_ON("ron","realTimeSubOn",true,"开启实时订阅书集的url",false, RealTimelOnExecute.getInstance()),
    REAL_TIME_SUB_OFF("roff","realTimeSubOff",true,"关闭实时订阅书集的url",false, new RealTimelOffExecute()),
    REAL_TIME_SUB_OFF_ALL("roffa","realTimeSubOffAll",false,"关闭所有实时订阅书集的url",false, new RealTimelOffAllExecute()),
    REAL_TIME_SUB_ON_ALL("rona","realTimeSubOnAll",false,"开启所有实时订阅书集的url",false, new RealTimelOnAllExecute()),
    REAL_TIME_SUB_INFO("si","realTimeSubInfo",false,"查看实时订阅信息",false, new RealTimelSubInfoExecute()),
    ;

    private String command;
    private String fullCommand;
    private boolean hasArg;
    private String desc;
    private boolean required;
    private Function function;

    public void exec(String val) {
        function.apply(val);
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

    OptionEnum(String command, String fullCommand, boolean hasArg, String desc, boolean required,Function function) {
        this.command = command;
        this.fullCommand = fullCommand;
        this.hasArg = hasArg;
        this.desc = desc;
        this.required = required;
        this.function = function;
    }
}
