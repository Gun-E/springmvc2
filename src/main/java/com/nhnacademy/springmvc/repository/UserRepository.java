package com.nhnacademy.springmvc.repository;

import com.nhnacademy.springmvc.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByIdAndPassword(String id, String password);
}
