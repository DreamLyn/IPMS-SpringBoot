package com.jktd.ipms.control.sysinfo;

import com.jktd.ipms.beans.device.DeviceInfo;
import com.jktd.ipms.beans.device.DeviceStateInfo;
import com.jktd.ipms.beans.device.DeviceSyncInfo;
import com.jktd.ipms.beans.device.interfaces.DeviceStateInfoInterface;
import com.jktd.ipms.control.SystemInfo;
import com.jktd.ipms.entity.DeviceInfos;
import com.jktd.ipms.service.sysinfo.DeviceInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

@RestController
@RequestMapping("sysinfo")
public class TagInfoControl {

    @Autowired
    private SystemInfo systemInfo;

}
