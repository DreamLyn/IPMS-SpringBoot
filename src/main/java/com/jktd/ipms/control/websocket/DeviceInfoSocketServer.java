package com.jktd.ipms.control.websocket;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.jktd.ipms.beans.device.DeviceInfo;
import com.jktd.ipms.beans.websocket.command.ClientCommand;
import com.jktd.ipms.control.SystemInfo;
import com.jktd.ipms.control.thread.SyncServerHandler;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.TreeMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 实现websocket服务器端。
 */
@ServerEndpoint(value = "/websocket/sync")
@Component
public class DeviceInfoSocketServer extends SpringBootBeanAutowiringSupport implements InitializingBean {
    //concurrent包的线程安全Set，用来存放每个客户端对应的WebSocket对象。
    private static CopyOnWriteArraySet<DeviceInfoSocketServer> syncSocketServers = new CopyOnWriteArraySet<>();
    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;
    private long deviceId;
    private boolean sendStateInfo;
    private boolean sendBaseSyncinfo;
    private boolean sendSyncEffect;

    @Autowired
    private SystemInfo systemInfo;
    private TreeMap<Long, DeviceInfo> deviceInfos;

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        syncSocketServers.add(this);     //加入set中
        System.out.println("客户端连接，当前客户端个数："+syncSocketServers.size());
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        syncSocketServers.remove(this);  //从set中删除
        System.out.println("客户端断开");
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ClientCommand clientCommand = objectMapper.readValue(message, ClientCommand.class);
            switch (clientCommand.getCommand()) {
                case 0://发送设备id
                    this.deviceId = Long.parseLong(clientCommand.getData());
                    break;
                case 1://改变发送状态信息
                    this.sendStateInfo = Boolean.parseBoolean(clientCommand.getData());
                    break;
                case 2://改变发送设备同步基本信息
                    this.sendBaseSyncinfo = Boolean.parseBoolean(clientCommand.getData());
                    break;
                case 3://请求推送设备同步效果
                    this.sendSyncEffect = Boolean.parseBoolean(clientCommand.getData());
                    break;
                default:
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发生错误时调用
     */
    @OnError
    public void onError(Session session, Throwable error) {
        //error.printStackTrace();
    }

    /**
     * 发送消息
     *
     * @param message
     * @throws IOException
     */
    public void sendMessage(String message) {
        try {
            this.session.getBasicRemote().sendText(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 群发自定义消息
     */
    public void sendInfo(int command, long deviceId, String message) throws IOException {
        for (DeviceInfoSocketServer item : syncSocketServers) {
            //System.out.println(deviceId+"-----"+item.getDeviceId());
            //System.out.println(command+"Base:"+item.isSendBaseSyncinfo()+"---effect:"+item.isSendSyncEffect());
            if (item.getDeviceId() == deviceId) {
                switch (command) {
                    case SyncServerHandler.COMMAND_SYNC_BASE:
                        if (item.isSendBaseSyncinfo()) {
                            //System.out.println("发送同步");
                            item.sendMessage(message);
                        }
                        break;
                    case SyncServerHandler.COMMAND_SYNC_EFFECT:
                        if (item.isSendSyncEffect()) {
                            //System.out.println("发送效果");
                            item.sendMessage(message);
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.deviceInfos = systemInfo.getDeviceInfos();
    }

    public long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(long deviceId) {
        this.deviceId = deviceId;
    }

    public boolean isSendStateInfo() {
        return sendStateInfo;
    }

    public void setSendStateInfo(boolean sendStateInfo) {
        this.sendStateInfo = sendStateInfo;
    }

    public boolean isSendBaseSyncinfo() {
        return sendBaseSyncinfo;
    }

    public void setSendBaseSyncinfo(boolean sendBaseSyncinfo) {
        this.sendBaseSyncinfo = sendBaseSyncinfo;
    }

    public boolean isSendSyncEffect() {
        return sendSyncEffect;
    }

    public void setSendSyncEffect(boolean sendSyncEffect) {
        this.sendSyncEffect = sendSyncEffect;
    }
}
