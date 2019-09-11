package com.krzysiek.rest.rest_app.entity;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@SequenceGenerator(name = "seq_student", initialValue = 100, allocationSize = 1)
@ApiModel(description = "Details about student")
@JsonFilter("StudentsFilter")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_student")
    private Long id;

    @NotBlank(message = "First name can not by empty")
    @Size(max = 15, message = "First name is too long  (max. 15 characters)")
    @ApiModelProperty(notes = "First name should have between 0-15 characters and can not be empty")
    private String firstName;

    @NotBlank(message = "Second name can not by empty")
    @Size(max = 20, message = "Second name is too long  (max. 20 characters)")
    @ApiModelProperty(notes = "Second name should have between 0-20 characters and can not be empty")
    private String secondName;

    @Past(message = "Birth date should by in the past")
    @ApiModelProperty(notes = "Birth date should by in the past")
    private Date birthday;

    @Email(message = "Incorrect e-mail format")
    @Size(max = 50, message = "E-mail is too long  (max. 50 characters)")
    @ApiModelProperty(notes = "E-mail should have maximally 50 characters")
    private String email;

    private Integer yearOfStudy;


}
