package com.jktd.ipms.beans.device;

public class DeviceInfo {
    /**
     * 设备网络信息
     */
    private DeviceNetInfo deviceNetInfo=new DeviceNetInfo();
    /**
     * 设备在线信息
     */
    private DeviceOnlineInfo deviceOnlineInfo=new DeviceOnlineInfo();
    /**
     * 设备硬件状态信息
     */
    private DeviceStateInfo deviceStateInfo=new DeviceStateInfo();
    /**
     * 设备同步信息
     */
    private DeviceSyncInfo deviceSyncInfo=new DeviceSyncInfo();

    public DeviceNetInfo getDeviceNetInfo() {
        return deviceNetInfo;
    }

    public void setDeviceNetInfo(DeviceNetInfo deviceNetInfo) {
        this.deviceNetInfo = deviceNetInfo;
    }

    public DeviceOnlineInfo getDeviceOnlineInfo() {
        return deviceOnlineInfo;
    }

    public void setDeviceOnlineInfo(DeviceOnlineInfo deviceOnlineInfo) {
        this.deviceOnlineInfo = deviceOnlineInfo;
    }

    public DeviceStateInfo getDeviceStateInfo() {
        return deviceStateInfo;
    }

    public void setDeviceStateInfo(DeviceStateInfo deviceStateInfo) {
        this.deviceStateInfo = deviceStateInfo;
    }

    public DeviceSyncInfo getDeviceSyncInfo() {
        return deviceSyncInfo;
    }

    public void setDeviceSyncInfo(DeviceSyncInfo deviceSyncInfo) {
        this.deviceSyncInfo = deviceSyncInfo;
    }
}
