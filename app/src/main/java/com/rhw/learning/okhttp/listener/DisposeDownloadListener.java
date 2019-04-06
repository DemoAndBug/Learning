package com.rhw.learning.okhttp.listener;

/**
 * Date:2017/11/26 on 16:49
 * @author Simon
 */
public interface DisposeDownloadListener  extends DisposeDataListener {
    /**
     * @function 监听下载进度
     */
    public void onProgress(int progrss);
}
