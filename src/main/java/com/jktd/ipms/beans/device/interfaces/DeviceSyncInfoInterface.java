package com.jktd.ipms.beans.device.interfaces;

/**
 * 此类主要是与websocket通信所用
 */
public class DeviceSyncInfoInterface {
    /**
     * 网络丢包率
     */
    private float lossRate;
    /**
     * 同步丢包率
     */
    private float syncLossRate;
    /**
     * 同步标准差
     */
    private float syncStdev;

    public float getLossRate() {
        return lossRate;
    }

    public void setLossRate(float lossRate) {
        this.lossRate = lossRate;
    }

    public float getSyncLossRate() {
        return syncLossRate;
    }

    public void setSyncLossRate(float syncLossRate) {
        this.syncLossRate = syncLossRate;
    }

    public float getSyncStdev() {
        return syncStdev;
    }

    public void setSyncStdev(float syncStdev) {
        this.syncStdev = syncStdev;
    }
}
