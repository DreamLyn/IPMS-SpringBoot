package com.jktd.ipms.control;

import com.jktd.ipms.beans.device.DeviceInfo;
import com.jktd.ipms.beans.device.DeviceOnlineInfo;
import com.jktd.ipms.beans.tag.TagInfo;
import javassist.bytecode.Descriptor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.TreeMap;

@Component
public class SystemInfo extends Thread implements InitializingBean{

    private TreeMap<Long,TagInfo> tagInfos;
    private TreeMap<Long,DeviceInfo> deviceInfos;
    private final long offlineTime=60000;


    public SystemInfo(){
        tagInfos=new TreeMap<>();
        deviceInfos=new TreeMap<>();
    }

    public TreeMap<Long, TagInfo> getTagInfos() {
        return tagInfos;
    }

    public void setTagInfos(TreeMap<Long, TagInfo> tagInfos) {
        this.tagInfos = tagInfos;
    }

    public TreeMap<Long, DeviceInfo> getDeviceInfos() {
        return deviceInfos;
    }

    public void setDeviceInfos(TreeMap<Long, DeviceInfo> deviceInfos) {
        this.deviceInfos = deviceInfos;
    }

    public long getOfflineTime() {
        return offlineTime;
    }

    //每分钟执行一次
    public void run() {
        while (true){
            //每分钟检测一次，基站在线情况。
            Iterator iterator=deviceInfos.entrySet().iterator();
            while (iterator.hasNext()){
                Entry<Long,DeviceInfo> entry= (Entry<Long, DeviceInfo>) iterator.next();
                DeviceInfo deviceInfo=entry.getValue();
                DeviceOnlineInfo deviceOnlineInfo=deviceInfo.getDeviceOnlineInfo();
                if(System.currentTimeMillis()-deviceOnlineInfo.getTimestamp()<=offlineTime){
                    deviceOnlineInfo.setOnline(true);
                }else{
                    deviceOnlineInfo.setOnline(false);
                }
            }
            try {
                Thread.sleep(offlineTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public void afterPropertiesSet() throws Exception {
        this.start();
    }
}
