package com.jktd.ipms.service.sysinfo;

import com.jktd.ipms.dao.DeviceInfosDao;
import com.jktd.ipms.entity.DeviceInfos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;


@Service
public class DeviceInfoService {
    @Autowired
    private DeviceInfosDao deviceInfosDao;

    /**
     * 根据当前页数获取相应内容
     * @param pageNow
     * @param pageSize
     * @return
     */
    public Page<DeviceInfos> getDeviceInfosWithPage(int pageNow, int pageSize){
        PageRequest request = this.buildPageRequest(pageNow,pageSize);
        Page<DeviceInfos> deviceInfos= this.deviceInfosDao.findAll(request);
        return deviceInfos;
    }


    /**
     * 构建request
     * @param pageNow
     * @param pagzSize
     * @return
     */
    private PageRequest buildPageRequest(int pageNow, int pagzSize) {
        return new PageRequest(pageNow , pagzSize, null);
    }
}
