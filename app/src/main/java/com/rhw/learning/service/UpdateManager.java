package com.rhw.learning.service;

import com.rhw.learning.service.update.UpdateDownloadListener;
import com.rhw.learning.service.update.UpdateDownloadRequest;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Date:2017/12/9 on 14:25
 * Function:下载调度管理器
 * @author Simon
 */
public class UpdateManager {
    private static UpdateManager manager;
    private ThreadPoolExecutor threadPool;
    private UpdateDownloadRequest downloadRequest;

    private UpdateManager() {
        threadPool = (ThreadPoolExecutor) Executors.newCachedThreadPool();
    }

    static {
        manager = new UpdateManager();
    }

    public static UpdateManager getInstance() {
        return manager;
    }

    public void startDownload(String downloadUrl, String localFilePath,
                              UpdateDownloadListener downloadListener) {
        if (downloadRequest != null && downloadRequest.isDownloading()) {
            return;
        }
        checkLocalFilePath(localFilePath);

        downloadRequest = new UpdateDownloadRequest(downloadUrl, localFilePath,
                downloadListener);
        Future<?> request = threadPool.submit(downloadRequest);
        new WeakReference<Future<?>>(request);
    }

    private void checkLocalFilePath(String localFilePath) {
        File path = new File(localFilePath.substring(0,
                localFilePath.lastIndexOf("/") + 1));
        File file = new File(localFilePath);
        if (!path.exists()) {
            path.mkdirs();
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
