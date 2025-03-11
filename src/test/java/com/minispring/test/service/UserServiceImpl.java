package com.minispring.test.service;

import com.minispring.annotation.Component;
import com.minispring.annotation.Value;

/**
 * Implementation of the UserService interface.
 */
@Component
public class UserServiceImpl implements UserService {

    @Value("${default.user:Admin}")
    private String defaultUser;

    private String currentUser;

    @Override
    public String getCurrentUser() {
        return currentUser != null ? currentUser : defaultUser;
    }

    @Override
    public void setCurrentUser(String username) {
        this.currentUser = username;
    }
}