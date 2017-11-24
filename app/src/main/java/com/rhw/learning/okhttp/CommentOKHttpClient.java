package com.rhw.learning.okhttp;

import com.rhw.learning.okhttp.https.HttpsUtils;
import com.rhw.learning.okhttp.response.CommonJsonCallback;

import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Author:renhongwei
 * Date:2017/11/24 on 11:47
 * function: 请求参数设置，请求发送，https支持
 */
public class CommentOKHttpClient {
    private static final  int TIME_OUT = 30;//超时参数
    private static OkHttpClient mOkHttpClient;

    //为我们的client配置参数
    static {
        //创建client对象
        OkHttpClient.Builder  okHttpBuilder = new OkHttpClient.Builder();
        //为构建者设置超时时间
        okHttpBuilder.connectTimeout(TIME_OUT, TimeUnit.SECONDS);
        okHttpBuilder.readTimeout(TIME_OUT,TimeUnit.SECONDS);
        okHttpBuilder.writeTimeout(TIME_OUT,TimeUnit.SECONDS);

        okHttpBuilder.followRedirects(true);
        //https支持
        okHttpBuilder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
        okHttpBuilder.sslSocketFactory(HttpsUtils.getSslSocketFactory());

        mOkHttpClient = okHttpBuilder.build();
    }

    /**
     * 发送具体的http/https请求
     * @param request
     * @param commCallback
     * @return Call
     */
    public static Call sentRequest(Request request, CommonJsonCallback commCallback){
        Call  call = mOkHttpClient.newCall(request);
        call.enqueue(commCallback);
        return call;
    }
}
