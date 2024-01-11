package com.nhnacademy.springmvc.domain;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@AllArgsConstructor
public class StudentModifyRequest {
    @NotBlank
    @Length(min = 1)
    String name;
    @Email
    String email;
    @Min(value = 0)
    @Max(value = 100)
    int score;
    @NotBlank
    @Length(min = 1, max = 200)
    String comment;
    public StudentModifyRequest(){}
}
