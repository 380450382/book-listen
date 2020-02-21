package com.book.command.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class PropertiesUtil {
    private PropertiesUtil(){
    }
    public static Properties load(String path) {
        Properties properties = new Properties();
        try {
            InputStream resourceAsStream = PropertiesUtil.class.getResourceAsStream(path);
            properties.load(resourceAsStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }
}
