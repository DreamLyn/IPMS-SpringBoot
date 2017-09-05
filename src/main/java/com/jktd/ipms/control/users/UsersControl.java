package com.jktd.ipms.control.users;

import com.jktd.ipms.dao.UsersDao;
import com.jktd.ipms.entity.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("interface")
public class UsersControl {

    @Autowired
    private UsersDao usersDao;

    @RequestMapping("save")
    public String save(){
        Users users=new Users();
        users.setUsername("张三");
        return "ok";
    }

    @RequestMapping("find")
    public List<Users> find(){
        return (List<Users>) usersDao.findAll();
    }

    @RequestMapping("findByName")
    public Users findByName(){
        return usersDao.findByUsername("lianrui");
    }
}
