package com.nhnacademy.springmvc.controller;

import com.nhnacademy.springmvc.domain.StudentModifyRequest;
import com.nhnacademy.springmvc.exception.StudentNotFoundException;
import com.nhnacademy.springmvc.exception.ValidationFailedException;
import com.nhnacademy.springmvc.repository.StudentRepository;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@Valid
@RequestMapping("/student")
@Slf4j
public class StudentController {
    private final StudentRepository studentRepository;

    public StudentController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @GetMapping("/{studentId}")
    public String viewStudent(@PathVariable long studentId, Model model) {
        if (!studentRepository.exists(studentId)) {
            throw new StudentNotFoundException();
        }
        model.addAttribute("student", studentRepository.getStudent(studentId));
        return "studentView";
    }

    @GetMapping("/{studentId}/modify")
    public String studentModifyForm(@PathVariable long studentId, Model model) {
        if (!studentRepository.exists(studentId)) {
            throw new StudentNotFoundException();
        }
        model.addAttribute("student", studentRepository.getStudent(studentId));
        return "studentModify";
    }

    @GetMapping(value = "/{studentId}", params = "hideScore")
    public String viewStudentHideScore(@PathVariable long studentId, @RequestParam(name = "hideScore", defaultValue = "no") String hideScore, ModelMap modelMap) {
        modelMap.addAttribute("student", studentRepository.getStudent(studentId));
        if ("yes".equals(hideScore)) {
            modelMap.addAttribute("hideScore",true);
        }
        return "studentView";
    }

    @PostMapping("/{studentId}/modify")
    public ModelAndView modifyUser(@PathVariable long studentId, @Valid @ModelAttribute StudentModifyRequest modifyRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationFailedException(bindingResult);
        }
        studentRepository.modify(studentId, modifyRequest.getName(), modifyRequest.getEmail(), modifyRequest.getScore(), modifyRequest.getComment());
        ModelAndView mav = new ModelAndView("studentView");
        mav.addObject("student", studentRepository.getStudent(studentId));
        return mav;
    }
    @ExceptionHandler(StudentNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleStudentNotFoundExceptionException(Exception ex, Model model) {
        log.error("", ex);

        model.addAttribute("exception", ex);
        return "error";
    }
    @ExceptionHandler(ValidationFailedException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleValidationFailedException(Exception ex, Model model) {
        log.error("", ex);

        model.addAttribute("exception", ex);
        return "error";
    }
}
