package com.jktd.ipms.dao;

import com.jktd.ipms.entity.Users;
import org.springframework.data.repository.CrudRepository;

public interface UsersDao extends CrudRepository<Users,Long>{
    Users findByUsername(String username);
}
