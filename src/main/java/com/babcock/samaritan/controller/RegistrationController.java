package com.babcock.samaritan.controller;

import com.babcock.samaritan.entity.Student;
import com.babcock.samaritan.entity.Token;
import com.babcock.samaritan.error.DataAlreadyExistException;
import com.babcock.samaritan.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/register")
public class RegistrationController {
    @Autowired
    private StudentService studentService;

    @PostMapping("/student")
    public Token registerStudent(@Valid @RequestBody Student student) throws DataAlreadyExistException {
        return studentService.registerStudent(student);
    }

//    @PostMapping("/officer")
//    public OfficerDTO registerOfficer(@Valid @RequestBody Officer officer) {
//        return officerService.registerOfficer(officer);
//    }
}
