package com.book.command.util;

import com.alibaba.fastjson.JSON;
import com.book.command.execute.RealTimelOnExecute;
import com.book.command.model.Book;
import org.apache.commons.lang3.StringUtils;

import java.awt.geom.IllegalPathStateException;
import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public final class CacheUtil {
    private static Map<String, Object> cacheList = new HashMap<>();
    private static Map<String, Book> bookCache = new ConcurrentHashMap<>();
    private static Set<String> toCache = new HashSet<>();
    private static Properties properties = PropertiesUtil.load("/cache/cache.properties");
    static {
        cacheList.put("book",bookCache);
        cacheList.put("tos",toCache);
    }
    public static Book putBook(String key,Book book){
        return bookCache.put(key,book);
    }
    public static Book getBook(String key){
        return bookCache.getOrDefault(key,new Book());
    }
    public static boolean putTo(String to){
        return toCache.add(to);
    }
    public static Set<String> getTo(){
        return toCache;
    }
    public static void clearTo(){
        toCache.clear();
    }
    public static void init(){
        StringBuilder cache = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(getCacheFile()),"UTF-8"));) {
            String line;
            while ((line = reader.readLine()) != null){
                cache.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(StringUtils.isNotBlank(cache)){
            PrintUtil.print("加载本地缓存");
            cacheList = JSON.parseObject(cache.toString(),Map.class);
            Map<String, Book> bookMap = (Map<String, Book>) cacheList.get("book");
            if(Objects.nonNull(bookMap)){
                PrintUtil.print("加载book");
                bookCache = Collections.synchronizedMap(bookMap);
                bookCache.keySet().forEach(key -> {
                    bookCache.put(key,JSON.parseObject(JSON.toJSONString(bookCache.get(key)),Book.class));
                });
                if(bookCache != null){
                    Map<String, RealTimelOnExecute.SubState> stopMap = RealTimelOnExecute.getInstance().getStopMap();
                    bookCache.forEach((k,book) -> stopMap.put(k,new RealTimelOnExecute.SubState(book)));
                }
            }
            if(Objects.nonNull(cacheList.get("tos"))){
                PrintUtil.print("加载tos");
                toCache.addAll(JSON.parseArray(JSON.toJSONString(cacheList.get("tos")), String.class));
                cacheList.put("tos",toCache);
            }
        }
        PrintUtil.print("初始化完成");
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
