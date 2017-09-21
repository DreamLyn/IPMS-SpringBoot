package com.jktd.ipms.beans.device;

public class DeviceOnlineInfo {
    /**
     * 设备上次心跳时间
     */
    private long timestamp;
    /**
     * 设备是否在线
     */
    private boolean online;

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }
}
