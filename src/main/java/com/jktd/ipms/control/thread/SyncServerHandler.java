package com.jktd.ipms.control.thread;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jktd.ipms.beans.device.DeviceInfo;
import com.jktd.ipms.beans.device.DeviceSyncInfo;
import com.jktd.ipms.beans.device.interfaces.DeviceSyncInfoInterface;
import com.jktd.ipms.beans.net.SyncDataBean;
import com.jktd.ipms.beans.websocket.command.ServerCommandSycEffect;
import com.jktd.ipms.beans.websocket.command.ServerCommandSyncBaseInfo;
import com.jktd.ipms.control.SystemInfo;
import com.jktd.ipms.control.websocket.DeviceInfoSocketServer;
import com.jktd.ipms.tools.CommonFun;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.TreeMap;

@Component
@Sharable
public class SyncServerHandler extends SimpleChannelInboundHandler<DatagramPacket> implements InitializingBean {

    private final long Max_TS = 1099511627776L;
    public static final int COMMAND_SYNC_BASE=2;
    public static final int COMMAND_SYNC_EFFECT=3;
    private TreeMap<Long, DeviceInfo> deviceInfos;
    private ServerCommandSyncBaseInfo serverCommandSyncBaseInfo=new ServerCommandSyncBaseInfo();
    private DeviceSyncInfoInterface deviceSyncInfoInterface=new DeviceSyncInfoInterface();
    private ServerCommandSycEffect serverCommandSyncEffect=new ServerCommandSycEffect();
    @Autowired
    private SystemInfo systemInfo;
    @Autowired
    private DeviceInfoSocketServer deviceInfoSocketServer;

    public SyncServerHandler() {

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // TODO Auto-generated method stub
        this.deviceInfos = systemInfo.getDeviceInfos();

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                //发送同步基本状态。
//                while(true){
//
//                    serverCommandSyncBaseInfo.setCommand(COMMAND_SYNC_BASE);
//                    deviceSyncInfoInterface.setLossRate((float) (Math.random()*0.2));
//                    deviceSyncInfoInterface.setSyncLossRate((float) (Math.random()));
//                    deviceSyncInfoInterface.setSyncStdev((float) (Math.random()*40));
//                    serverCommandSyncBaseInfo.setDeviceSyncInfoInterface(deviceSyncInfoInterface);
//                    ObjectMapper objectMapper=new ObjectMapper();
//                    String commandString= null;
//                    try {
//                        commandString = objectMapper.writeValueAsString(serverCommandSyncBaseInfo);
//                        deviceInfoSocketServer.sendInfo(COMMAND_SYNC_BASE,1234567812345678l,commandString);
//                    } catch (JsonProcessingException e) {
//                        e.printStackTrace();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//
//                    serverCommandSyncEffect.setCommand(COMMAND_SYNC_EFFECT);
//                    serverCommandSyncEffect.setSyncEffect((float) Math.random()*200);
//                    try {
//                        commandString=objectMapper.writeValueAsString(serverCommandSyncEffect);
//                        deviceInfoSocketServer.sendInfo(COMMAND_SYNC_EFFECT,1234567812345678l,commandString);
//                    } catch (JsonProcessingException e) {
//                        e.printStackTrace();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//
//
//                    try {
//                        Thread.sleep(100);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }).start();
    }

    /**
     * 读取内容
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) throws Exception {
        ByteBuf byteBuf = (ByteBuf) packet.copy().content();
        SyncDataBean syncDataBean;
        /**
         * 解析同步数据
         */
        syncDataBean = CommonFun.getSyncBean(byteBuf);
        final long deviceId = syncDataBean.getDeviceId();
        //System.out.println(Long.toHexString(deviceId));
        /**
         * 如果基站信息中包含此基站
         */
        if (deviceInfos.containsKey(deviceId)) {
            //获取基站信息
            DeviceInfo deviceInfo = deviceInfos
                    .get(deviceId);
            DeviceSyncInfo deviceSyncInfo = deviceInfo.getDeviceSyncInfo();

            if (deviceSyncInfo.getSyncIndex() == -1) {
                //第一次,则只记录一些数据，其他不作处理。
                /**
                 * 记录上次的包序号
                 */
                deviceSyncInfo.setLastPackageIndex(syncDataBean
                        .getPackageIndex());
                deviceSyncInfo.setLastSyncIndex(syncDataBean
                        .getSyncIndex());
                /**
                 * 记录上次的同步系数
                 */
                deviceSyncInfo.setLastCoefficient(syncDataBean
                        .getOlsCoefficient());
                /**
                 * 记录上次的主机时间戳
                 */
                deviceSyncInfo.setLastMasterTimestamp(syncDataBean
                        .getUpBaseTimestamp());
                /**
                 * 记录上次的从机时间戳
                 */
                deviceSyncInfo.setLastSlaverTimestamp(syncDataBean
                        .getSlaverTimestamp());
                /**
                 * 记录上次的主从差值,经过换算,此值也可先不使用
                 */
                deviceSyncInfo.setSyncIndex(1);
            } else {
                /**
                 * 计算网络丢包率
                 */
                int temp = 0;
                int lastIndex = deviceSyncInfo.getLastPackageIndex();
                int thisIndex = syncDataBean.getPackageIndex();
                temp = thisIndex > lastIndex ? (thisIndex - lastIndex - 1) : (thisIndex + 255 - lastIndex);
                deviceSyncInfo.setLossLength(deviceSyncInfo
                        .getLossLength() + temp);//设置丢失数据包

                /**
                 * 计算同步丢包率,,,此丢包率包含Wifi丢包率,原则上减去Wifi丢包率为实际同步丢包率
                 */
                lastIndex = deviceSyncInfo.getLastSyncIndex();
                thisIndex = syncDataBean.getSyncIndex();
                temp = (thisIndex > lastIndex) ? (thisIndex - lastIndex - 1) : (thisIndex + 99 - lastIndex);
                deviceSyncInfo.setLossLengthSync(deviceSyncInfo
                        .getLossLengthSync() + temp);


                /**
                 * 开始计算同步效果数据
                 */
                long lastMasterTimeStamp = deviceSyncInfo.getLastMasterTimestamp();
                long lastSlaverTimeStamp = deviceSyncInfo.getLastSlaverTimestamp();
                double lastCoefficient = deviceSyncInfo.getLastCoefficient();
                long masterTimeStamp = syncDataBean.getUpBaseTimestamp();
                long slaverTimeStamp = syncDataBean.getSlaverTimestamp();
                if (slaverTimeStamp < lastSlaverTimeStamp) {
                    slaverTimeStamp += Max_TS;
                }
                if (masterTimeStamp < lastMasterTimeStamp) {
                    masterTimeStamp += Max_TS;
                }
                // 最小二乘同步算法,计算出同步效果，并将效果值放到数组中
                double syncEffect = (slaverTimeStamp - lastSlaverTimeStamp)
                        * (lastCoefficient)
                        + lastMasterTimeStamp
                        - masterTimeStamp;
                deviceSyncInfo.setSyncEffect(syncEffect);
                int length = deviceSyncInfo.getSYNC_DATA_CHART_LENGTH();
                System.arraycopy(deviceSyncInfo.getSyncEffectBuffer(), 1, deviceSyncInfo.getSyncEffectBuffer(), 0, length - 1);
                deviceSyncInfo.getSyncEffectBuffer()[length - 1] = (float) syncEffect;
                //计算均值，用于计算标准差
                double syncAverage = (syncEffect + deviceSyncInfo
                        .getLastAverageData()
                        * (deviceSyncInfo.getSyncIndex()))
                        / (deviceSyncInfo.getSyncIndex() + 1);
                deviceSyncInfo.setLastAverageData(syncAverage);

                /**
                 * 然后计算标准差,在此其实是方差值,显示的时候进行换算
                 */
                double syncStdev = ((syncEffect - syncAverage)
                        * (syncEffect - syncAverage) + deviceSyncInfo
                        .getLastStdevData()
                        * (deviceSyncInfo.getSyncIndex()))
                        / (deviceSyncInfo.getSyncIndex() + 1);
                deviceSyncInfo.setLastStdevData(syncStdev);
                /**
                 * 记录上次的包序号
                 */
                deviceSyncInfo.setLastPackageIndex(syncDataBean
                        .getPackageIndex());
                deviceSyncInfo.setLastSyncIndex(syncDataBean
                        .getSyncIndex());
                /**
                 * 记录上次的同步系数
                 */
                deviceSyncInfo.setLastCoefficient(syncDataBean
                        .getOlsCoefficient());
                /**
                 * 记录上次的主机时间戳
                 */
                deviceSyncInfo.setLastMasterTimestamp(syncDataBean
                        .getUpBaseTimestamp());
                /**
                 * 记录上次的从机时间戳
                 */
                deviceSyncInfo.setLastSlaverTimestamp(syncDataBean
                        .getSlaverTimestamp());
                deviceSyncInfo.setSyncIndex(deviceSyncInfo.getSyncIndex() + 1);
                //发送同步基本状态。
                serverCommandSyncBaseInfo.setCommand(COMMAND_SYNC_BASE);
                deviceSyncInfoInterface.setLossRate((float) (deviceSyncInfo.getLossLength()
                        / (deviceSyncInfo.getSyncIndex() + deviceSyncInfo.getLossLength())));
                deviceSyncInfoInterface.setSyncLossRate((float) (deviceSyncInfo.getLossLengthSync()
                        / (deviceSyncInfo.getSyncIndex() + deviceSyncInfo.getLossLengthSync())));
                deviceSyncInfoInterface.setSyncStdev((float) Math.sqrt(syncStdev));
                serverCommandSyncBaseInfo.setDeviceSyncInfoInterface(deviceSyncInfoInterface);
                ObjectMapper objectMapper=new ObjectMapper();
                String commandString=objectMapper.writeValueAsString(serverCommandSyncBaseInfo);
                deviceInfoSocketServer.sendInfo(COMMAND_SYNC_BASE,deviceId,commandString);
                //发送同步效果数据。
                serverCommandSyncEffect.setCommand(COMMAND_SYNC_EFFECT);
                serverCommandSyncEffect.setSyncEffect((float) syncEffect);
                commandString=objectMapper.writeValueAsString(serverCommandSyncEffect);
                deviceInfoSocketServer.sendInfo(COMMAND_SYNC_EFFECT,deviceId,commandString);
            }
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
