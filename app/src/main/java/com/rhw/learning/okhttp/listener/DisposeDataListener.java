package com.rhw.learning.okhttp.listener;

/**
 * Author:renhongwei
 * Date:2017/11/24 on 13:19
 * function:业务处理的地方，包括java层异常和业务层异常
 */
public interface DisposeDataListener {

    /**
     * 请求成功回调事件处理
     */
    public void onSuccess(Object responseObj);

    /**
     * 请求失败回调事件处理
     */
    public void onFailure(Object reasonObj);
}
