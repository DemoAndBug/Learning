package com.rhw.learning.okhttp.request;

import java.util.Map;

import okhttp3.FormBody;
import okhttp3.Request;

/**
 * Author:renhongwei
 * Date:2017/11/24 on 12:12
 * function:接收请求参数，为我们生成request对象
 */
public class CommontRequest {


    /**
     * @param url
     * @param params
     * @return 返回一个Post类型的request对象
     */
    public static  Request  createPostRequest(String url,RequestParams params){
        FormBody.Builder builder = new FormBody.Builder();
        if(params !=  null){
            for (Map.Entry<String,String> entry :  params.urlParams.entrySet()){
                //将请求参数遍历加入到构件中
                builder.add(entry.getKey(),entry.getValue());
            }
        }
        FormBody formBody = builder.build();
        return new  Request.Builder().url(url).post(formBody).build();
    }

    /**
     * @param url
     * @param params
     * @return 返回一个Get类型的request对象
     */
    public static  Request  createGetRequest(String url,RequestParams params){
        StringBuilder urlBuilder  = new StringBuilder(url).append("?");
        if(params !=  null){
            for (Map.Entry<String,String> entry :  params.urlParams.entrySet()){
                //将请求参数遍历加入到构件中
                urlBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
        }

        return new  Request.Builder().url(urlBuilder.substring(0,urlBuilder.length()-1)).get().build();
    }
}
