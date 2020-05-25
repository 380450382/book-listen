package com.book.command.execute;

import com.book.command.common.Common;
import com.book.command.enums.CacheOperateEnum;
import com.book.command.enums.ResultEnum;
import com.book.command.execute.base.AbstractExecute;
import com.book.command.util.CacheUtil;
import com.book.command.util.MessageUtil;
import org.apache.commons.lang3.StringUtils;

public class SetMailToInfoExecute extends AbstractExecute<String> {

    @Override
    public Integer execute(String mailTos) {
        if(CacheOperateEnum.CLEAN == Common.threadLocal.get()){
            CacheUtil.clearTo();
        }
        if(StringUtils.equalsIgnoreCase(mailTos,Common.CLEAN)){
            CacheUtil.clearTo();
        } else {
            String[] tos = mailTos.split(",");
            for (String to : tos) {
                CacheUtil.putTo(to);
            }
        }
        CacheUtil.storeCache();
        return ResultEnum.SUCCESS.code();
    }
    @Override
    public void checkParam(String mailTos) {
        if (StringUtils.isBlank(mailTos)) {
            throw new IllegalArgumentException("请输入mail，多个以\",\"号隔开");
        }
        if(StringUtils.equalsIgnoreCase(mailTos,Common.CLEAN)){
            return;
        }
        String[] mails = mailTos.split(",");
        StringBuilder messageBuilder = new StringBuilder();
        for (String mail : mails) {
            if(!mail.matches(Common.MAIL_REGEXT)){
                messageBuilder.append(mail);
                messageBuilder.append(",");
            }
        }
        if(StringUtils.isNotBlank(messageBuilder.toString())){
            throw new IllegalArgumentException(MessageUtil.message("{}格式不正确，请输入正确的mail",
                    messageBuilder.toString()));
        }
    }
}
