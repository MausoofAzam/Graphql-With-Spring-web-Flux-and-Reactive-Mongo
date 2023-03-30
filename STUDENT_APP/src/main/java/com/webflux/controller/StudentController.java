package com.webflux.controller;

import com.webflux.dto.StudentInput;
import com.webflux.entity.Student;
import com.webflux.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Controller
public class StudentController {
    @Autowired
    private StudentService studentService;

    @MutationMapping("addStudent")
    public Mono<Student> addStudent(@Argument StudentInput studentInput) {
        log.info("Mutation method i.e Mono<Student> addStudent(StudentInput studentInput) is Called");
        return studentService.addStudent(studentInput);
    }
    //This method is for Creating List of Students along with its resources
    @MutationMapping("addStudents")
    public Flux<Student> addStudents(@Argument List<StudentInput> studentInputs) {
        log.info("Mutation Method i.e Flux<Student> addStudents(List<StudentInput> studentInputs)  is called");
        return studentService.addStudents(studentInputs);
    }
}
