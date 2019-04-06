package com.rhw.learning.video.monitor;

import com.rhw.learning.video.monitor.emevent.EMEvent;

import java.util.ArrayList;

public class VideoValue {


    public String resourceID;
    public String adid;
    public String resource;
    public String thumb;
    public ArrayList<Monitor> startMonitor;
    public ArrayList<Monitor> middleMonitor;
    public ArrayList<Monitor> endMonitor;
    public String clickUrl;
    public ArrayList<Monitor> clickMonitor;
    public EMEvent event;
}
