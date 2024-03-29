package com.nhnacademy.springmvc.controller;

import com.nhnacademy.springmvc.domain.Student;
import com.nhnacademy.springmvc.domain.StudentModifyRequest;
import com.nhnacademy.springmvc.domain.StudentRegisterRequest;
import com.nhnacademy.springmvc.exception.StudentNotFoundException;
import com.nhnacademy.springmvc.exception.ValidationFailedException;
import com.nhnacademy.springmvc.repository.StudentRepository;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
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
    public Student registerStudent(@Validated @RequestBody StudentRegisterRequest registerRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationFailedException(bindingResult);
        }
        return studentRepository.save(new Student(registerRequest.getName(), registerRequest.getEmail(),
                registerRequest.getScore(), registerRequest.getComment()));
    }

    @GetMapping("/students/{studentId}")
    public Student getStudent(@PathVariable long studentId) {
        if (!studentRepository.existsById(studentId)) {
            throw new StudentNotFoundException();}
        Student student = studentRepository.findById(studentId).get();
        return student;}
    @PutMapping("/students/{studentId}")
    @ResponseStatus(HttpStatus.CREATED)
    public Student updateStudent(@PathVariable long studentId, @Validated @RequestBody StudentModifyRequest modifyRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationFailedException(bindingResult);}
        if (!studentRepository.existsById(studentId)) {
            throw new StudentNotFoundException();}
        return studentRepository.save(new Student(studentId, modifyRequest.getName(), modifyRequest.getEmail(), modifyRequest.getScore(), modifyRequest.getComment()));}}