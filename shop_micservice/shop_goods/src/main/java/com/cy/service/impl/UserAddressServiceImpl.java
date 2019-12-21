package com.cy.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.cy.dao.AddressDao;
import com.cy.entity.Address;
import com.cy.entity.User;
import com.cy.service.IUserAddressService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class UserAddressServiceImpl implements IUserAddressService {

    @Autowired
    private AddressDao addressDao;

    /**
     * 根据用户id查询出所有用户地址
     * @param user
     * @return
     */
    @Override
    public List<Address> queryAddressByUid(User user) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("uid", user.getById());
        return addressDao.selectList(queryWrapper);
    }

    /**
     * 添加用户地址
     * 如果添加默认用户地址,则将原地址状态更改
     *
     * @param address
     * @param user
     */
    @Override
    public void insertAddress(Address address, User user) {
        Integer tf = address.getIsdefault();
        UpdateWrapper<Address> updateWrapper = new UpdateWrapper<>();
        QueryWrapper<Address> addressQueryWrapper = new QueryWrapper<>();
        address.setUid(user.getId());

        if (tf == 1){
            updateWrapper.set("isdefault",0);
            updateWrapper.eq("uid",user.getById());
            updateWrapper.eq("isdefault",1);
            addressDao.update(address, updateWrapper);
            addressDao.insert(address);
        }else {
            addressDao.insert(address);
        }


    }
}
