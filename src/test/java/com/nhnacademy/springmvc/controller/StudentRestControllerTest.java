package com.nhnacademy.springmvc.controller;


import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.springmvc.domain.Student;
import com.nhnacademy.springmvc.domain.StudentModifyRequest;
import com.nhnacademy.springmvc.domain.StudentRegisterRequest;
import com.nhnacademy.springmvc.exception.StudentNotFoundException;
import com.nhnacademy.springmvc.exception.ValidationFailedException;
import com.nhnacademy.springmvc.repository.StudentRepository;
import java.util.Arrays;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.NestedTestConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.util.NestedServletException;

class StudentRestControllerTest {
    private StudentRepository studentRepository;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        studentRepository = mock(StudentRepository.class);
        mockMvc = MockMvcBuilders.standaloneSetup(new StudentRestController(studentRepository)).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void registerStudent() throws Exception {
        StudentRegisterRequest studentRegisterRequest = new StudentRegisterRequest("nae", "email@gmail.com", 100, "comment");
        String json = objectMapper.writeValueAsString(studentRegisterRequest);
        when(studentRepository.register(anyString(),anyString(),anyInt(),anyString())).thenReturn(new Student("test", "email", 100, "comment"));

        mockMvc.perform(post("/students")
                        .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", Matchers.equalTo("test")))
                .andExpect(jsonPath("$.email", Matchers.equalTo("email")))
                .andExpect(jsonPath("$.score", Matchers.equalTo(100)))
                .andExpect(jsonPath("$.comment", Matchers.equalTo("comment")));
    }

    @Test
    void getStudent() throws Exception {
        when(studentRepository.getStudent(1)).thenReturn(new Student("test", "email", 100, "comment"));
        when(studentRepository.exists(anyLong())).thenReturn(true);
        mockMvc.perform(get("/students/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", Matchers.equalTo("test")))
                .andExpect(jsonPath("$.email", Matchers.equalTo("email")))
                .andExpect(jsonPath("$.score", Matchers.equalTo(100)))
                .andExpect(jsonPath("$.comment", Matchers.equalTo("comment")));
    }
    @Test
    void updateStudent() throws Exception {
        StudentModifyRequest studentModifyRequest = new StudentModifyRequest("nae", "email@gmail.com", 100, "comment");
        when(studentRepository.modify(anyLong(),anyString(),anyString(),anyInt(),anyString())).thenReturn(new Student("test", "email", 100, "comment"));
        when(studentRepository.exists(anyLong())).thenReturn(true);
        String json = objectMapper.writeValueAsString(studentModifyRequest);

        mockMvc.perform(put("/students/1")
                        .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", Matchers.equalTo("test")))
                .andExpect(jsonPath("$.email", Matchers.equalTo("email")))
                .andExpect(jsonPath("$.score", Matchers.equalTo(100)))
                .andExpect(jsonPath("$.comment", Matchers.equalTo("comment")));
    }
    @Test
    void registerStudentValidationFailedException() throws Exception {
        StudentRegisterRequest studentRegisterRequest = new StudentRegisterRequest("nae", "email", 901, "comment");
        String json = objectMapper.writeValueAsString(studentRegisterRequest);
        when(studentRepository.register(anyString(),anyString(),anyInt(),anyString())).thenReturn(new Student("test", "email", 901, "comment"));
        Throwable th = catchThrowable(() ->
                mockMvc.perform(post("/students").contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isBadRequest()).andReturn());
        assertThat(th)
                .isInstanceOf(NestedServletException.class)
                .hasCauseInstanceOf(ValidationFailedException.class);
    }
    @Test
    void updateStudentValidationFailedException() throws Exception {
        StudentModifyRequest studentModifyRequest = new StudentModifyRequest("nae", "email", 901, "comment");
        String json = objectMapper.writeValueAsString(studentModifyRequest);
        when(studentRepository.modify(anyLong(),anyString(),anyString(),anyInt(),anyString())).thenReturn(new Student("test", "email", 901, "comment"));
        when(studentRepository.exists(anyLong())).thenReturn(false);
        Throwable th = catchThrowable(() ->
                mockMvc.perform(put("/students/1").contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isBadRequest()).andReturn());
        assertThat(th)
                .isInstanceOf(NestedServletException.class)
                .hasCauseInstanceOf(StudentNotFoundException.class);
    }
    @Test
    void getStudentNotFound(){
        Throwable th = catchThrowable(() ->
        mockMvc.perform(get("/students/1")  .accept(MediaType.APPLICATION_JSON)).andDo(print()));
        assertThat(th).isInstanceOf(NestedServletException.class)
                .hasCauseInstanceOf(StudentNotFoundException.class);
    }
}