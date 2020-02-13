package com.book.command.util;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MailUtil {
    private static Properties properties = new Properties();
    static {
        try {
            InputStream resourceAsStream = MailUtil.class.getResourceAsStream("/mail/mail.properties");
            properties.load(resourceAsStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void send(String title,String content) {
        try {
            String from = properties.getProperty("mail.username");
            String to = properties.getProperty("mail.to");
            String token = properties.getProperty("mail.token");
            Session session = Session.getDefaultInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(from,token);
                }
            });
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipient(Message.RecipientType.TO,new InternetAddress(to));
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
