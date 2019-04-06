package com.rhw.learning.module.home;

import com.rhw.learning.module.BaseModel;

import java.util.ArrayList;

/**
 * Date:2017/11/26
 * @author Simon
 */
public class RecommandModel extends BaseModel {


    public ArrayList<RecommandBodyValue> list;
    public RecommandHeadValue head;

    @Override
    public String toString() {
        return "RecommandModel{" +
                "list=" + list +
                ", head=" + head +
                '}';
    }
}
