package com.rhw.learning.manager;

import android.app.ProgressDialog;
import android.content.Context;

import com.rhw.learning.R;

/**
 * Date:2017/12/4 on 13:20
 * @author Simon
 */
public class DialogManager {
    private static DialogManager mInstnce = null;
    private ProgressDialog mDialog;

    public static DialogManager getInstnce() {

        if (mInstnce == null) {

            synchronized (DialogManager.class) {

                if (mInstnce == null) {

                    mInstnce = new DialogManager();
                }
            }
        }
        return mInstnce;
    }

    public void showProgressDialog(Context context) {

        if (mDialog == null) {
            mDialog = new ProgressDialog(context);
            mDialog.setMessage(context.getResources().getString(R.string.please_wait));
            mDialog.setCanceledOnTouchOutside(false);
        }
        mDialog.show();
    }

    public void dismissProgressDialog() {

        if (mDialog != null) {
            mDialog.dismiss();
        }
        mDialog = null;
    }
}
