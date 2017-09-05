package com.jktd.ipms.service;

import com.jktd.ipms.dao.UsersDao;
import com.jktd.ipms.entity.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsersService {
    @Autowired
    private UsersDao usersDao;

    /**
     * 获取用户信息
     * @param username
     * @return
     */
    public Users findByUsername(String username){
        return usersDao.findByUsername(username);
    }

}
