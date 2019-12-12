package com.cy.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cy.entity.User;

public interface UserDao extends BaseMapper<User> {

    Integer selectDemo();
}
