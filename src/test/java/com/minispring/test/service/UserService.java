package com.minispring.test.service;

/**
 * Service interface for user-related operations.
 */
public interface UserService {

    /**
     * Get the current user.
     *
     * @return the current user
     */
    String getCurrentUser();

    /**
     * Set the current user.
     *
     * @param username the username
     */
    void setCurrentUser(String username);
}