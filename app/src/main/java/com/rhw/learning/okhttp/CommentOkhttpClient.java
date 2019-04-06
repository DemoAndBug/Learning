package com.rhw.learning.okhttp;

import com.rhw.learning.okhttp.https.HttpsUtils;
import com.rhw.learning.okhttp.listener.DisposeDataHandle;
import com.rhw.learning.okhttp.response.CommonFileCallback;
import com.rhw.learning.okhttp.response.CommonJsonCallback;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.Call;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Date:2017/11/24 on 11:47
 * function: 请求参数设置，请求发送，https支持
 * @author Simon
 */
public class CommentOkhttpClient {
    /**
     * 超时参数
     */
    private static final  int TIME_OUT = 30;
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

        /**
         *  为所有请求添加请求头，看个人需求
         *  User-Agent 标明发送本次请求的客户端
         */
        okHttpBuilder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request()
                        .newBuilder()
                        .addHeader("User-Agent", "Imooc-Mobile")
                        .build();
                return chain.proceed(request);
            }
        });
        /**
         * trust all the https point
         */
        okHttpBuilder.sslSocketFactory(HttpsUtils.initSSLSocketFactory(), HttpsUtils.initTrustManager());
        mOkHttpClient = okHttpBuilder.build();
    }

    /**
     * 指定cilent信任指定证书
     * @param certificates
     */
    public static void setCertificates(InputStream... certificates) {
        mOkHttpClient.newBuilder().sslSocketFactory(HttpsUtils.getSslSocketFactory(certificates, null, null)).build();
    }

    /**
     * 通过构造好的Request,Callback去发送请求
     *
     * @param request
     * @param handle
     */
    public static Call get(Request request, DisposeDataHandle handle) {
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new CommonJsonCallback(handle));
        return call;
    }

    public static Call post(Request request, DisposeDataHandle handle) {
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new CommonJsonCallback(handle));
        return call;
    }

    public static Call downloadFile(Request request, DisposeDataHandle handle) {
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new CommonFileCallback(handle));
        return call;
    }
}
