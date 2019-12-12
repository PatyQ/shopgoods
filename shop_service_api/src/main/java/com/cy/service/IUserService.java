package com.cy.service;

import com.cy.entity.User;

public interface IUserService {

    int userRegist(User user);

    User selUserByName(String username);
}
