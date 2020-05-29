package com.book.command.util;

import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public final class HttpUtil {
    private volatile static OkHttpClient client;

    private static OkHttpClient getClient() {
        if (client == null) {
            synchronized (HttpUtil.class) {
                if (client == null) {
                    client = new OkHttpClient();
                }
            }
        }
        return client;
    }

    public static Response get(String url,  Map<String,String> param,boolean async){
        return connect(url,"get",param,async);
    }

    public static Response post(String url,  Map<String,String> param,boolean async) {
        return connect(url,"post",param,async);
    }

    public static Response connect(String url, String method, Map<String,String> param,boolean async) {
        FormBody.Builder builder = new FormBody.Builder();
        param.entrySet().forEach(entry -> {
            builder.add(entry.getKey(),entry.getValue());
        });
        Request request = new Request.Builder().url("https://www.jianshu.com/search/do")
                .method(method,builder.build())
                .build();
        Call call = getClient().newCall(request);
        try {
            if(async){
                CompletableFuture<Response> completableFuture = new CompletableFuture<>();
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        PrintUtil.print(MessageUtil.message("{},请求失败:{}",url,e));
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        completableFuture.complete(response);
                    }
                });
                return completableFuture.get();
            }
            return call.execute();
        } catch (InterruptedException | ExecutionException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
