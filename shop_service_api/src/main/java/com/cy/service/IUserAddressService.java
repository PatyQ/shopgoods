package com.cy.service;

import com.cy.entity.Address;
import com.cy.entity.User;

import java.util.List;

public interface IUserAddressService {

    List<Address> queryAddressByUid(User user);

    void insertAddress(Address address, User user);
}
