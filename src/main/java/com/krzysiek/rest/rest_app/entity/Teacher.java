package com.krzysiek.rest.rest_app.entity;


import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@SequenceGenerator(name = "seq_teacher", initialValue = 100, allocationSize = 1)
@ApiModel(description = "Details about teacher")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@JsonFilter("TeacherFilter")
public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_teacher")
    private Long id;

    @NotBlank(message = "First name can not be empty")
    @Size(max = 15, message = "First name is too long (max. 15 characters)")
    @ApiModelProperty(notes = "First name should have between 0-15 characters and can not by empty")
    private String firstName;

    @NotBlank(message = "Second name can not be empty")
    @Size(max = 20, message = "Second name is too long (max. 20 characters)")
    @ApiModelProperty(notes = "Second name should have between 0-20 characters and can not by empty")
    private String secondName;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "teacher")
    private Set<Subject> subjects;
}
