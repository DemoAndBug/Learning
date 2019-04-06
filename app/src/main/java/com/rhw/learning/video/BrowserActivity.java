package com.rhw.learning.video;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;

import com.rhw.learning.utils.LogUtils;
import com.rhw.learning.utils.Utils;
import com.rhw.learning.video.browser.Base64Drawables;
import com.rhw.learning.video.browser.BrowserLayout;
import com.rhw.learning.video.browser.BrowserWebView;
import com.rhw.learning.video.browser.BrowserWebViewClient;

/**
 * Date:2017/11/30 on 14:21
 * function:WebView页面
 * @author Simon
 */
public class BrowserActivity extends AppCompatActivity {
    private static final String LOG_TAG = BrowserActivity.class.getSimpleName();

    public static final String KEY_URL = "url";
    private BrowserWebView mAdBrowserWebview;
    private BrowserLayout mLayout;

    private boolean mIsBackFromMarket = false;

    private View mProgress;
    private Button mBackButton;

    private String mUrl;

    private BrowserWebViewClient.Listener mWebClientListener;

    private Base64Drawables mBase64Drawables = new Base64Drawables();

    @Override
    public final void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        if (isValidExtras()) {

            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);

            mLayout = new BrowserLayout(this.getApplicationContext());
            setContentView(mLayout);

            mProgress = mLayout.getProgressBar();
            mBackButton = mLayout.getBackButton();

            mAdBrowserWebview = mLayout.getWebView();
            initWebView(mAdBrowserWebview);

            if (bundle != null) {
                mAdBrowserWebview.restoreState(bundle);
            } else {
                mAdBrowserWebview.loadUrl(mUrl);
            }
            initButtonListeners(mAdBrowserWebview);
        } else {
            finish();
        }
    }

    private boolean isValidExtras() {
        mUrl = getIntent().getStringExtra(KEY_URL);
        return !TextUtils.isEmpty(mUrl);
    }

    @Override
    protected final void onPause() {
        super.onPause();
        LogUtils.i(LOG_TAG, "onPause");
        if (mAdBrowserWebview != null) {
            mAdBrowserWebview.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        LogUtils.i(LOG_TAG, " onDestroy");
        if (mAdBrowserWebview != null) {
            mAdBrowserWebview.clearCache(true);
        }
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtils.i(LOG_TAG, "onResume");
        if (mIsBackFromMarket) {
            //finish();
        }
        mIsBackFromMarket = true;
        mLayout.getProgressBar().setVisibility(View.INVISIBLE);
    }

    private void initWebView(BrowserWebView webView) {
        mWebClientListener = initAdBrowserClientListener();
        BrowserWebViewClient client = new BrowserWebViewClient(mWebClientListener);
        webView.setWebViewClient(client);
        webView.getSettings().setBuiltInZoomControls(false);
    }

    private BrowserWebViewClient.Listener initAdBrowserClientListener() {
        return new BrowserWebViewClient.Listener() {

            @Override
            public void onReceiveError() {
                finish();
            }

            @Override
            public void onPageStarted() {
                mProgress.setVisibility(View.VISIBLE);
            }

            @Override
            @SuppressLint("NewApi")
            public void onPageFinished(boolean canGoBack) {
                mProgress.setVisibility(View.INVISIBLE);
                if (canGoBack) {
                    setImage(mBackButton, mBase64Drawables.getBackActive());
                } else {
                    setImage(mBackButton, mBase64Drawables.getBackInactive());
                }
            }

            @Override
            public void onLeaveApp() {

            }
        };
    }

    @SuppressLint("NewApi")
    private void setImage(Button button, String imageString) {
        if (Build.VERSION.SDK_INT < 16) {
            button.setBackgroundDrawable(Utils.decodeImage(imageString));
        } else {
            button.setBackground(Utils.decodeImage(imageString));
        }
    }

    private void initButtonListeners(final WebView webView) {

        mLayout.getBackButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (webView.canGoBack()) {
                    mLayout.getProgressBar().setVisibility(View.VISIBLE);
                    webView.goBack();
                }
            }
        });

        mLayout.getCloseButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mLayout.getRefreshButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLayout.getProgressBar().setVisibility(View.VISIBLE);
                webView.reload();
            }
        });

        mLayout.getNativeButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uriString = webView.getUrl();
                if (uriString != null) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uriString));

                    boolean isActivityResolved = getPackageManager()
                            .resolveActivity(browserIntent, PackageManager.MATCH_DEFAULT_ONLY) != null
                            ? true : false;
                    if (isActivityResolved) {
                        startActivity(browserIntent);
                    }
                }
            }
        });
    }

    @Override
    public final boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mAdBrowserWebview.canGoBack()) {
                mAdBrowserWebview.goBack();
                return true;
            }
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        mIsBackFromMarket = false;
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        mAdBrowserWebview.saveState(outState);
        super.onSaveInstanceState(outState);
    }
}
