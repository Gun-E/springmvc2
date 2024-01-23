package com.nhnacademy.springmvc.repository;

import com.nhnacademy.springmvc.domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {

}
