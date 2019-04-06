package com.rhw.learning.okhttp.exception;

/**
 * Date:2017/11/24 on 13:38
 * @author Simon
 */
public class OkHttpException extends Exception {
    private static final long serialVersionUID = 1L;

    /**
     * the server return code
     */
    private int ecode;

    /**
     * the server return error message
     */
    private Object emsg;
    public OkHttpException(int ecode, Object emsg) {
        this.ecode = ecode;
        this.emsg = emsg;
    }

    public int getEcode() {
        return ecode;
    }

    public Object getEmsg() {
        return emsg;
    }

    @Override
    public String toString() {
        return "OkHttpException{" +
                "ecode=" + ecode +
                ", emsg=" + emsg +
                '}';
    }
}
