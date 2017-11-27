package com.rhw.learning.okhttp.listener;

/**
 * Author:renhongwei
 * Date:2017/11/26 on 16:49
 */
public interface DisposeDownloadListener  extends DisposeDataListener {
    /**
     * @function 监听下载进度
     */
    public void onProgress(int progrss);
}
