package com.jktd.ipms.control.sysinfo;

import com.jktd.ipms.beans.device.DeviceInfo;
import com.jktd.ipms.beans.device.DeviceStateInfo;
import com.jktd.ipms.beans.device.DeviceSyncInfo;
import com.jktd.ipms.beans.device.interfaces.DeviceStateInfoInterface;
import com.jktd.ipms.control.SystemInfo;
import com.jktd.ipms.dao.UsersDao;
import com.jktd.ipms.entity.DeviceInfos;
import com.jktd.ipms.entity.Users;
import com.jktd.ipms.service.sysinfo.DeviceInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("sysinfo")
public class DeviceInfoControl {

    @Autowired
    private DeviceInfoService deviceInfoService;

    @Autowired
    private SystemInfo systemInfo;
    /**
     * 根据当前页数获取相应内容
     * @param pageNow
     * @param pageSize
     * @return
     */
    @RequestMapping("getdeviceinfoswithpage")
    public HashMap<String,Object> getDeviceInfosWithPage(int pageNow, int pageSize){
        //用户返回信息
        HashMap<String,Object> hashMap=new HashMap<>();
        //从数据库获取信息
        Page<DeviceInfos> pages= deviceInfoService.getDeviceInfosWithPage(pageNow,pageSize);
        //基站信息列表
        ArrayList<DeviceStateInfoInterface> arrayList=new ArrayList<>();
        ///////
        TreeMap<Long,DeviceInfo> deviceInfos=systemInfo.getDeviceInfos();
        for (DeviceInfos page : pages) {
            Long deviceId=page.getDeviceId();
            DeviceStateInfoInterface deviceStateInfoInterface=new DeviceStateInfoInterface();
            deviceStateInfoInterface.setDeviceId(deviceId);
            deviceStateInfoInterface.setOnline(page.isOnline());
            deviceStateInfoInterface.setUse(page.isUse());
            if(deviceInfos.containsKey(deviceId)){
                DeviceInfo deviceInfo=deviceInfos.get(deviceId);
                DeviceStateInfo deviceStateInfo=deviceInfo.getDeviceStateInfo();
                DeviceSyncInfo deviceSyncInfo=deviceInfo.getDeviceSyncInfo();
                deviceStateInfoInterface.setDeviceCpuOccuprate(deviceStateInfo.getDeviceCpuOccuprate());
                deviceStateInfoInterface.setDeviceCpuTemp(deviceStateInfo.getDeviceCpuTemp());
                deviceStateInfoInterface.setDeviceDecaTemp(deviceStateInfo.getDeviceDecaTemp());
                deviceStateInfoInterface.setDeviceWifiRSSI(deviceStateInfo.getDeviceWifiRSSI());
                deviceStateInfoInterface.setDeviceLossRate((float) (deviceSyncInfo.getLossLength()
                        / (deviceSyncInfo.getSyncIndex() + deviceSyncInfo.getLossLength())));
                deviceStateInfoInterface.setDeviceSyncLossRate((float) (deviceSyncInfo.getLossLengthSync()
                        / (deviceSyncInfo.getSyncIndex() + deviceSyncInfo.getLossLengthSync())));
                deviceStateInfoInterface.setDeviceSyncStdev((float) Math.sqrt(deviceSyncInfo.getLastStdevData()));
            }
            arrayList.add(deviceStateInfoInterface);
        }
        hashMap.put("content",arrayList);
        hashMap.put("totalElements",pages.getTotalElements());

        return hashMap;
    }
}
