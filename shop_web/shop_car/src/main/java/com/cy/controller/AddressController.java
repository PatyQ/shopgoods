package com.cy.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.cy.aop.IsLogin;
import com.cy.aop.UserHolder;
import com.cy.entity.Address;
import com.cy.entity.ResultData;
import com.cy.entity.User;
import com.cy.service.IUserAddressService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "address")
public class AddressController {

    @Reference
    private IUserAddressService userAddressService;

    @IsLogin(mustLogin = true)
    @ResponseBody
    @RequestMapping("insert")
    public ResultData<String> insertAddress(Address address){
        User user = UserHolder.getUser();
        user.setId(user.getById());
        userAddressService.insertAddress(address,user);
        return new ResultData<String>().setCode(ResultData.ResultCodeList.OK).setMsg("添加成功！");
    }

}
