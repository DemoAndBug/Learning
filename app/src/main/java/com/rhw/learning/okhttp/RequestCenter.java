package com.rhw.learning.okhttp;

import com.rhw.learning.constant.HttpConstants;
import com.rhw.learning.module.home.BaseRecommandModel;
import com.rhw.learning.module.update.UpdateModel;
import com.rhw.learning.module.user.User;
import com.rhw.learning.okhttp.listener.DisposeDataHandle;
import com.rhw.learning.okhttp.listener.DisposeDataListener;
import com.rhw.learning.okhttp.request.CommonRequest;
import com.rhw.learning.okhttp.request.RequestParams;

/**
 * Date:2017/11/26 on 16:26
 *
 * @author Simon
 */
public class RequestCenter {


    /**
     * 根据参数发送所有post请求
     */
    public static void postRequest(String url, RequestParams params, DisposeDataListener listener, Class<?> clazz) {
        CommentOkhttpClient.get(CommonRequest.
                createGetRequest(url, params), new DisposeDataHandle(listener, clazz));
    }

    public static void requestRecommandData(DisposeDataListener listener) {
        RequestCenter.postRequest(HttpConstants.HOME_RECOMMAND, null, listener, BaseRecommandModel.class);
    }

    /**
     * 用户登陆请求
     *
     * @param userName
     * @param passwd
     */
    public static void login(String userName, String passwd, DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        params.put("mb", userName);
        params.put("pwd", passwd);
        RequestCenter.postRequest(HttpConstants.LOGIN, params, listener, User.class);
    }

    /**
     * 应用版本号请求
     */
    public static void checkVersion(DisposeDataListener listener) {
        RequestCenter.postRequest(HttpConstants.CHECK_UPDATE, null, listener, UpdateModel.class);
    }
}
