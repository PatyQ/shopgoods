package com.cy.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cy.dao.UserDao;
import com.cy.entity.User;
import com.cy.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
     private UserDao userDao;

    @Override
    public int userRegist(User user) {

        //================至此可以接受到数据但无法添加到数据库=================//
        Integer i = userDao.selectDemo();
        System.out.println(i);
        //================至此可以接受到数据但无法添加到数据库=================//

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",user.getUsername());
        Integer rec = userDao.selectCount(queryWrapper);
        if (rec==0){
            return userDao.insert(user);
        }
        return -1;
    }

    @Override
    public User selUserByName(String username) {

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",username);
        User user = userDao.selectOne(queryWrapper);
        return user;
    }
}
