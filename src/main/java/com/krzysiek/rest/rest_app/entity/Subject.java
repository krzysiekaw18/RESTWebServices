package com.krzysiek.rest.rest_app.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@SequenceGenerator(name = "seq_subject", initialValue = 100, allocationSize = 1)
@ApiModel(description = "Details about subject")
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_subject")
    @JsonIgnore
    private Long id;

    @NotBlank(message = "Name of subcject can not be empty")
    @Size(max = 30, message = "Name is too long (max. 30 characters)")
    @ApiModelProperty(notes = "Name of subject should have between 0-30 characters and can not be empty")
    private String name;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    @ManyToMany(mappedBy = "subjects")
    Set<Student> students;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Teacher teacher;


}
