package com.nhnacademy.springmvc.controller;

import com.nhnacademy.springmvc.domain.Student;
import com.nhnacademy.springmvc.domain.StudentRegisterRequest;
import com.nhnacademy.springmvc.exception.StudentNotFoundException;
import com.nhnacademy.springmvc.exception.ValidationFailedException;
import com.nhnacademy.springmvc.repository.StudentRepository;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class StudentRestController {
    private final StudentRepository studentRepository;

    public StudentRestController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @PostMapping("/students")
    @ResponseStatus(HttpStatus.CREATED)
    public Student registerStudent(@Valid @RequestBody StudentRegisterRequest registerRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationFailedException(bindingResult);
        }
        return studentRepository.register(registerRequest.getName(), registerRequest.getEmail(),
                registerRequest.getScore(), registerRequest.getComment());
    }

    @GetMapping("/students/{studentId}")
    public Student getStudent(@PathVariable long studentId) {
        Student student = studentRepository.getStudent(studentId);
        if (student == null) {
            throw new StudentNotFoundException();
        }
        return student;
    }

    @PutMapping("/students/{studentId}")
    public Student updateStudent(@PathVariable long studentId, @Valid @RequestBody Student updatedStudent, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationFailedException(bindingResult);
        }

        if (!studentRepository.exists(studentId)) {
            throw new StudentNotFoundException();
        }
        return studentRepository.modify(studentId, updatedStudent.getName(), updatedStudent.getEmail(),
                updatedStudent.getScore(), updatedStudent.getComment());
    }
}

