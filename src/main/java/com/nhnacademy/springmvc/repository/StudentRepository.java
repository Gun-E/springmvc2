package com.nhnacademy.springmvc.repository;

import com.nhnacademy.springmvc.domain.Student;

public interface StudentRepository {
    boolean exists(long id);

    Student register(String name, String email, int score, String comment);

    Student getStudent(long id);

    Student modify(long id, String name, String email, int score, String comment);
}
