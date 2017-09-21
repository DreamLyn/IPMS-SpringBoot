package com.jktd.ipms.beans.device.interfaces;

public class DeviceStateInfoInterface {
    private long deviceId;
    private short deviceWifiRSSI;
    private float deviceCpuOccuprate;
    private float deviceCpuTemp;
    private float deviceDecaTemp;
    private float deviceLossRate;
    private float deviceSyncLossRate;
    private float deviceSyncStdev;

    private boolean use;
    private boolean online;


    public float getDeviceLossRate() {
        return deviceLossRate;
    }

    public void setDeviceLossRate(float deviceLossRate) {
        this.deviceLossRate = deviceLossRate;
    }

    public float getDeviceSyncLossRate() {
        return deviceSyncLossRate;
    }

    public void setDeviceSyncLossRate(float deviceSyncLossRate) {
        this.deviceSyncLossRate = deviceSyncLossRate;
    }

    public float getDeviceSyncStdev() {
        return deviceSyncStdev;
    }

    public void setDeviceSyncStdev(float deviceSyncStdev) {
        this.deviceSyncStdev = deviceSyncStdev;
    }

    public long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(long deviceId) {
        this.deviceId = deviceId;
    }

    public boolean isUse() {
        return use;
    }

    public void setUse(boolean use) {
        this.use = use;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public short getDeviceWifiRSSI() {
        return deviceWifiRSSI;
    }

    public void setDeviceWifiRSSI(short deviceWifiRSSI) {
        this.deviceWifiRSSI = deviceWifiRSSI;
    }

    public float getDeviceCpuOccuprate() {
        return deviceCpuOccuprate;
    }

    public void setDeviceCpuOccuprate(float deviceCpuOccuprate) {
        this.deviceCpuOccuprate = deviceCpuOccuprate;
    }

    public float getDeviceCpuTemp() {
        return deviceCpuTemp;
    }

    public void setDeviceCpuTemp(float deviceCpuTemp) {
        this.deviceCpuTemp = deviceCpuTemp;
    }

    public float getDeviceDecaTemp() {
        return deviceDecaTemp;
    }

    public void setDeviceDecaTemp(float deviceDecaTemp) {
        this.deviceDecaTemp = deviceDecaTemp;
    }
}