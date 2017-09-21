package com.jktd.ipms.beans.device;

public class DeviceStateInfo {
    private short deviceWifiRSSI;
    private float deviceCpuOccuprate;
    private float deviceCpuTemp;
    private float deviceDecaTemp;

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
