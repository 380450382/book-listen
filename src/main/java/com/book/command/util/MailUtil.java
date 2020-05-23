package com.book.command.util;

import com.book.command.execute.SetMailToInfoExecute;
import org.apache.commons.collections4.CollectionUtils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;

public class MailUtil {
    private static Properties properties = PropertiesUtil.load("/mail/mail.properties");
    public static void send(String title, String content, Set<String> tos) {
        try {
            if(CollectionUtils.isEmpty(tos)){
                System.out.println("还未设置邮箱");
                return;
            }
            String from = properties.getProperty("mail.username");
            String token = properties.getProperty("mail.token");
            Session session = Session.getDefaultInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(from,token);
                }
            });
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            InternetAddress[] internetAddresses = new InternetAddress[tos.size()];
            for (int i = 0; i < tos.size(); i++) {
                internetAddresses[i] = new InternetAddress(CollectionUtils.get(tos,i));
            }
            message.setRecipients(Message.RecipientType.TO,internetAddresses);
            message.setRecipient(Message.RecipientType.CC,new InternetAddress(from));
            message.setSubject(title);
            message.setContent(content,"text/html;charset=UTF-8");
            Transport transport = session.getTransport();
            transport.connect(from,token);
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
