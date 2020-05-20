package com.book.command.util;

import com.alibaba.fastjson.JSON;
import com.book.command.execute.RealTimelOnExecute;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.awt.geom.IllegalPathStateException;
import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public final class CacheUtil {
    private static Map<String,Map> cacheList = new HashMap<>();
    private static Map<String,String> bookCache = new ConcurrentHashMap<>();
    private static Properties properties = PropertiesUtil.load("/cache/cache.properties");
    static {
        cacheList.put("book",bookCache);
    }
    public static String putBook(String key,String value){
        return bookCache.put(key,value);
    }
    public static String getBook(String key){
        return bookCache.get(key);
    }
    public static void init(){
        StringBuilder cache = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(getCacheFile()));) {
            String line;
            while ((line = reader.readLine()) != null){
                cache.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(StringUtils.isNotBlank(cache)){
            cacheList = JSON.parseObject(cache.toString(),Map.class);
            bookCache = Collections.synchronizedMap(cacheList.get("book"));
            if(bookCache != null){
                Map<String, RealTimelOnExecute.SubState> stopMap = RealTimelOnExecute.getInstance().getStopMap();
                bookCache.forEach((k,v) -> stopMap.put(k,new RealTimelOnExecute.SubState(v)));
            }
        }
    }
    public static File getCacheFile(){
        try {
            String path = properties.getProperty("cache.path",System.getProperty("user.dir"));
            String fileName = properties.getProperty("cache.filename","cache.db");
            File root = new File(path);
            if(!root.exists()){
                root.mkdirs();
            }
            File file = new File(root,fileName);
            if(!file.exists()){
                file.createNewFile();
            }
            return file;
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new IllegalPathStateException("获取资源失败");
    }
    public static void storeCache(){
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(getCacheFile()),"UTF-8"));) {
            writer.write(JSON.toJSONString(cacheList));
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
