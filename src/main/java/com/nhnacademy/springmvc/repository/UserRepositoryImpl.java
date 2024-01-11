package com.nhnacademy.springmvc.repository;

import com.nhnacademy.springmvc.domain.User;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class UserRepositoryImpl implements UserRepository {
    private final Map<String, User> userMap = new HashMap<>();

    @Override
    public boolean exists(String id) {
        return userMap.containsKey(id);
    }

    @Override
    public boolean matches(String id, String password) {
        return Optional.ofNullable(getUser(id))
                       .map(user -> user.getPassword().equals(password))
                       .orElse(false);
    }
    @Override
    public void addUser(String id, String password) {
        User user = new User(id, password);
        userMap.put(id, user);
    }
    @Override
    public User getUser(String id) {
        return exists(id) ? userMap.get(id) : null;
    }
}
