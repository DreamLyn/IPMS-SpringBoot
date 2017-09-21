package com.jktd.ipms.beans.websocket.command;

import com.jktd.ipms.beans.device.interfaces.DeviceSyncInfoInterface;

public class ServerCommandSyncBaseInfo {
    private int command;
    private DeviceSyncInfoInterface deviceSyncInfoInterface;

    public DeviceSyncInfoInterface getDeviceSyncInfoInterface() {
        return deviceSyncInfoInterface;
    }

    public void setDeviceSyncInfoInterface(DeviceSyncInfoInterface deviceSyncInfoInterface) {
        this.deviceSyncInfoInterface = deviceSyncInfoInterface;
    }

    public int getCommand() {

        return command;
    }

    public void setCommand(int command) {
        this.command = command;
    }
}
