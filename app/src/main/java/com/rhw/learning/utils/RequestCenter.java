package com.rhw.learning.utils;

import com.rhw.learning.constant.HttpConstants;
import com.rhw.learning.module.recommand.RecommandModel;
import com.rhw.learning.okhttp.CommentOKHttpClient;
import com.rhw.learning.okhttp.listener.DisposeDataHandle;
import com.rhw.learning.okhttp.listener.DisposeDataListener;
import com.rhw.learning.okhttp.request.CommonRequest;
import com.rhw.learning.okhttp.request.RequestParams;

/**
 * Author:renhongwei
 * Date:2017/11/26 on 16:26
 */
public class RequestCenter {


    //根据参数发送所有post请求
    public static void postRequest(String url, RequestParams params, DisposeDataListener listener, Class<?> clazz) {
        CommentOKHttpClient.get(CommonRequest.
                createGetRequest(url, params), new DisposeDataHandle(listener, clazz));
    }

    public static void requestRecommandData(DisposeDataListener listener) {
        RequestCenter.postRequest(HttpConstants.HOME_RECOMMAND, null, listener, RecommandModel.class);
    }
}
