package com.jktd.ipms.control.thread;


import com.jktd.ipms.beans.device.DeviceInfo;
import com.jktd.ipms.beans.device.DeviceNetInfo;
import com.jktd.ipms.beans.device.DeviceOnlineInfo;
import com.jktd.ipms.beans.device.DeviceStateInfo;
import com.jktd.ipms.control.SystemInfo;
import com.jktd.ipms.tools.CommonFun;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.ChannelHandler.Sharable;

import java.util.TreeMap;

@Component
@Sharable
public class BeatServerHandler extends SimpleChannelInboundHandler<DatagramPacket> implements InitializingBean {
    @Autowired
    private SystemInfo systemInfo;
    private TreeMap<Long,DeviceInfo> deviceInfos;


    public BeatServerHandler() {

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // TODO Auto-generated method stub
        deviceInfos=systemInfo.getDeviceInfos();
    }

    /**
     * 设置网络
     * @param deviceNetInfo
     * @param datagramPacket
     */
    private void setNetInfo(DeviceNetInfo deviceNetInfo,DatagramPacket datagramPacket){
        String socketString = datagramPacket.sender().getAddress().toString();
        String ipString = socketString.substring(1, socketString.length());
        deviceNetInfo.setIp(ipString);
        deviceNetInfo.setPort(datagramPacket.sender().getPort());
    }
    /**
     * 读取内容
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) throws Exception {
        ByteBuf byteBuf = (ByteBuf) packet.copy().content();
        long deviceId=byteBuf.getLongLE(8);
        //获取设备状态信息,Wifi信号强度，CPU占用率，CPU温度，UWB温度
        short deviceWifiRSSI = byteBuf.getShortLE(19);
        float deviceCpuOccuprate =
                Float.intBitsToFloat(byteBuf.getIntLE(29));
        float deviceCpuTemp = Float.intBitsToFloat(byteBuf.getIntLE(33));
        float deviceDecaTemp = Float.intBitsToFloat(byteBuf.getIntLE(37));
        if(deviceInfos.containsKey(deviceId)){
            //如果包含此设备
            DeviceInfo deviceInfo=deviceInfos.get(deviceId);
            DeviceOnlineInfo deviceOnlineInfo=deviceInfo.getDeviceOnlineInfo();
            DeviceStateInfo deviceStateInfo=deviceInfo.getDeviceStateInfo();
            //更新在线信息
            deviceOnlineInfo.setTimestamp(System.currentTimeMillis());
            //更新状态信息
            deviceStateInfo.setDeviceWifiRSSI(deviceWifiRSSI);
            deviceStateInfo.setDeviceCpuOccuprate(deviceCpuOccuprate);
            deviceStateInfo.setDeviceCpuTemp(deviceCpuTemp);
            deviceStateInfo.setDeviceDecaTemp(deviceDecaTemp);
        }else{
            //如果不包含此设备
            DeviceInfo deviceInfo=new DeviceInfo();
            DeviceNetInfo deviceNetInfo=deviceInfo.getDeviceNetInfo();
            DeviceOnlineInfo deviceOnlineInfo=deviceInfo.getDeviceOnlineInfo();
            DeviceStateInfo deviceStateInfo=deviceInfo.getDeviceStateInfo();
            ///添加网络信息
            setNetInfo(deviceNetInfo,packet);
            //添加在线信息
            deviceOnlineInfo.setTimestamp(System.currentTimeMillis());
            //添加状态信息
            deviceStateInfo.setDeviceWifiRSSI(deviceWifiRSSI);
            deviceStateInfo.setDeviceCpuOccuprate(deviceCpuOccuprate);
            deviceStateInfo.setDeviceCpuTemp(deviceCpuTemp);
            deviceStateInfo.setDeviceDecaTemp(deviceDecaTemp);
            //添加此设备信息
            deviceInfos.put(deviceId,deviceInfo);
        }
        byteBuf.release();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }

}
