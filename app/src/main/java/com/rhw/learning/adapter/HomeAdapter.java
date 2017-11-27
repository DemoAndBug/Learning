package com.rhw.learning.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:renhongwei
 * Date:2017/11/26 on 16:21
 */
public class HomeAdapter extends BaseAdapter {

    private static Context mContext ;
    private List datas = new ArrayList();

    public HomeAdapter (Context context, List  list){
        this.mContext = context;
        this.datas = list;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
