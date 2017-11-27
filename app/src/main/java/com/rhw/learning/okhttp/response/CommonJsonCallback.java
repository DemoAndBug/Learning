package com.rhw.learning.okhttp.response;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.gson.Gson;
import com.rhw.learning.okhttp.exception.OkHttpException;
import com.rhw.learning.okhttp.listener.DisposeDataHandle;
import com.rhw.learning.okhttp.listener.DisposeDataListener;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Author:renhongwei
 * Date:2017/11/24 on 13:25
 * function:专门处理Json的回调响应
 */
public class CommonJsonCallback implements Callback {
    private static final String TAG = "CommonJsonCallback";

    //与服务器返回的字段的一个对应关系
    protected final String RESULT_CODE = "ecode"; // 有返回则对于http请求来说是成功的，但还有可能是业务逻辑上的错误
    protected final int RESULT_CODE_VALUE = 0;
    protected final String ERROR_MSG = "emsg";
    protected final String EMPTY_MSG = "";
    protected final String COOKIE_STORE = "Set-Cookie";

    //自定义异常类型
    protected final int NETWORK_ERROR = -1; // the network relative error
    protected final int JSON_ERROR = -2; // the JSON relative error
    protected final int OTHER_ERROR = -3; // the unknow error

    /**
     * 将其它线程的数据转发到UI线程
     */
    private Handler mDeliveryHandler; //将消息传输到主线程进行处理
    private DisposeDataListener mListener;
    private Class<?> mClass;

    public CommonJsonCallback(DisposeDataHandle handle) {
        this.mListener = handle.mListener;
        this.mClass = handle.mClass;
        this.mDeliveryHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void onFailure(Call call, final IOException e) {

        Log.i(TAG, "onFailure  111");
        /**
         * 此时还在非UI线程，因此要转发到主线程进行处理
         */
        mDeliveryHandler.post(new Runnable() {
            @Override
            public void run() {
                mListener.onFailure(new OkHttpException(NETWORK_ERROR, e));
            }
        });
    }

    //响应处理函数
    @Override
    public void onResponse( Call call, final Response response) throws IOException {
        Log.i(TAG, "onResponse  333" + response.toString()+ response.body().string());
        final String result = response.body().string();
        mDeliveryHandler.post(new Runnable() {
            @Override
            public void run() {
                handlerResponse(result);
            }
        });

    }

    /**
     * 处理服务器中返回的数据
     */
    private void handlerResponse(Object responseObj) {

        if (responseObj == null && responseObj.toString().trim().equals("")) {
            Log.i(TAG, "onResponse  333");
            mListener.onFailure(new OkHttpException(NETWORK_ERROR, EMPTY_MSG));
            return;
        }
        try {
            /**
             * 协议确定后看这里如何修改
             */
            if (mClass == null) {
                mListener.onSuccess(responseObj);
            } else {
                Gson gson = new Gson();
                //Gson gson2 = new GsonBuilder().setExclusionStrategies(new SpecificClassExclusionStrategy(null, mClass.getClass())).create();
                Object obj = gson.fromJson(responseObj.toString(), mClass.getClass());
                if (obj != null) {
                    mListener.onSuccess(obj);
                } else {
                    mListener.onFailure(new OkHttpException(JSON_ERROR, EMPTY_MSG));
                }
            }
        } catch (Exception e) {
            Log.i(TAG, "onResponse  444");
            mListener.onFailure(new OkHttpException(OTHER_ERROR, e.getMessage()));
            e.printStackTrace();
        }

    }
}
