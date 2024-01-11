package com.nhnacademy.springmvc.repository;

import com.nhnacademy.springmvc.domain.User;

public interface UserRepository {
    boolean exists(String id);
    boolean matches(String id, String password);
    void addUser(String id, String password);
    User getUser(String id);
}
