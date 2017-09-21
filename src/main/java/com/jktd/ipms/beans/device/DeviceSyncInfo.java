package com.jktd.ipms.beans.device;

public class DeviceSyncInfo {

    /**
     * 表格数据长度
     */
    private final int SYNC_DATA_CHART_LENGTH = 500;
    /**
     * 上包数据索引,计算丢包率使用
     */
    private short lastPackageIndex;
    private short lastSyncIndex;

    /**
     * 丢失数据包
     */
    private long lossLength=0;
    private long lossLengthSync=0;
    /**
     * 同步效果当前值
     */
    private double syncEffect;

    /**
     * 上次的最小二乘
     */
    private double lastCoefficient;

    /**
     * 上次的主从机时间戳
     */
    private long lastMasterTimestamp;
    private long lastSlaverTimestamp;
    private long lastMasterSlaverDiff;
    /**
     * 之前数据的平均值
     */
    private double lastAverageData;
    /**
     * 之前标准差
     */
    private double lastStdevData;
    /**
     * 从开始到现在的同步数据包数，为-1则说明上午启动，第一包不作处理
     */
    private double syncIndex = -1;

    private float[] syncEffectBuffer=new float[SYNC_DATA_CHART_LENGTH];

    public int getSYNC_DATA_CHART_LENGTH() {
        return SYNC_DATA_CHART_LENGTH;
    }

    public short getLastPackageIndex() {
        return lastPackageIndex;
    }

    public void setLastPackageIndex(short lastPackageIndex) {
        this.lastPackageIndex = lastPackageIndex;
    }

    public short getLastSyncIndex() {
        return lastSyncIndex;
    }

    public void setLastSyncIndex(short lastSyncIndex) {
        this.lastSyncIndex = lastSyncIndex;
    }

    public long getLossLength() {
        return lossLength;
    }

    public void setLossLength(long lossLength) {
        this.lossLength = lossLength;
    }

    public long getLossLengthSync() {
        return lossLengthSync;
    }

    public void setLossLengthSync(long lossLengthSync) {
        this.lossLengthSync = lossLengthSync;
    }

    public double getSyncEffect() {
        return syncEffect;
    }

    public void setSyncEffect(double syncEffect) {
        this.syncEffect = syncEffect;
    }

    public double getLastCoefficient() {
        return lastCoefficient;
    }

    public void setLastCoefficient(double lastCoefficient) {
        this.lastCoefficient = lastCoefficient;
    }

    public long getLastMasterTimestamp() {
        return lastMasterTimestamp;
    }

    public void setLastMasterTimestamp(long lastMasterTimestamp) {
        this.lastMasterTimestamp = lastMasterTimestamp;
    }

    public long getLastSlaverTimestamp() {
        return lastSlaverTimestamp;
    }

    public void setLastSlaverTimestamp(long lastSlaverTimestamp) {
        this.lastSlaverTimestamp = lastSlaverTimestamp;
    }

    public long getLastMasterSlaverDiff() {
        return lastMasterSlaverDiff;
    }

    public void setLastMasterSlaverDiff(long lastMasterSlaverDiff) {
        this.lastMasterSlaverDiff = lastMasterSlaverDiff;
    }

    public double getLastAverageData() {
        return lastAverageData;
    }

    public void setLastAverageData(double lastAverageData) {
        this.lastAverageData = lastAverageData;
    }

    public double getLastStdevData() {
        return lastStdevData;
    }

    public void setLastStdevData(double lastStdevData) {
        this.lastStdevData = lastStdevData;
    }

    public double getSyncIndex() {
        return syncIndex;
    }

    public void setSyncIndex(double syncIndex) {
        this.syncIndex = syncIndex;
    }

    public float[] getSyncEffectBuffer() {
        return syncEffectBuffer;
    }

    public void setSyncEffectBuffer(float[] syncEffectBuffer) {
        this.syncEffectBuffer = syncEffectBuffer;
    }
}
