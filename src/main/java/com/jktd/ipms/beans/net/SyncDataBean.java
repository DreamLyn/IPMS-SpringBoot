package com.jktd.ipms.beans.net;

public class SyncDataBean {
    /**
     * 帧序号
     */
    private short packageIndex;
    /**
     * 基站ID
     */
    private long deviceId;
    /**
     * 同步序号
     */
    private short syncIndex;
    /**
     * 上层基站时间戳
     */
    private long upBaseTimestamp;
    /**
     * 本机时间戳
     */
    private long slaverTimestamp;
    /**
     * 主从时间戳差值
     */
    private long masterSlaverDiff;
    /**
     * 最小二乘系数
     */
    private double olsCoefficient;
    /**
     * 首径信号强度
     */
    private float firstPathRssi;
    /**
     * 接收信号强度
     */
    private float rssi;

    public short getPackageIndex() {
        return packageIndex;
    }

    public void setPackageIndex(short packageIndex) {
        this.packageIndex = packageIndex;
    }

    public long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(long deviceId) {
        this.deviceId = deviceId;
    }

    public short getSyncIndex() {
        return syncIndex;
    }

    public void setSyncIndex(short syncIndex) {
        this.syncIndex = syncIndex;
    }

    public long getUpBaseTimestamp() {
        return upBaseTimestamp;
    }

    public void setUpBaseTimestamp(long upBaseTimestamp) {
        this.upBaseTimestamp = upBaseTimestamp;
    }

    public long getSlaverTimestamp() {
        return slaverTimestamp;
    }

    public void setSlaverTimestamp(long slaverTimestamp) {
        this.slaverTimestamp = slaverTimestamp;
    }

    public long getMasterSlaverDiff() {
        return masterSlaverDiff;
    }

    public void setMasterSlaverDiff(long masterSlaverDiff) {
        this.masterSlaverDiff = masterSlaverDiff;
    }

    public double getOlsCoefficient() {
        return olsCoefficient;
    }

    public void setOlsCoefficient(double olsCoefficient) {
        this.olsCoefficient = olsCoefficient;
    }

    public float getFirstPathRssi() {
        return firstPathRssi;
    }

    public void setFirstPathRssi(float firstPathRssi) {
        this.firstPathRssi = firstPathRssi;
    }

    public float getRssi() {
        return rssi;
    }

    public void setRssi(float rssi) {
        this.rssi = rssi;
    }


}
