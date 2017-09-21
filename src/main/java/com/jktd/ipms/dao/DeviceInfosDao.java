package com.jktd.ipms.dao;

import com.jktd.ipms.entity.DeviceInfos;
import com.jktd.ipms.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface DeviceInfosDao extends JpaRepository<DeviceInfos,Long> {
    /**
     * 根据设备ID获取设备信息
     * @param deviceId
     * @return
     */
    DeviceInfos findByDeviceId(long deviceId);
}
