package com.babcock.samaritan.controller;

import com.babcock.samaritan.dto.StudentDTO;
import com.babcock.samaritan.entity.Student;
import com.babcock.samaritan.entity.StudentItem;
import com.babcock.samaritan.entity.Token;
import com.babcock.samaritan.model.LoginCredentials;
import com.babcock.samaritan.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/student")
public class StudentController {
    @Autowired
    private StudentService studentService;

    @GetMapping("/login")
    public Token loginStudent(@RequestBody LoginCredentials credentials) {
        return studentService.loginStudent(credentials);
    }

    @GetMapping("/info")
    public StudentDTO getStudentDetails() {
        return studentService.getStudentDetails();
    }

    @PutMapping("/info")
    public StudentDTO updateStudentInfo(@RequestBody Student student) {
        return studentService.updateStudentInfo(student);
    }

    @PostMapping("/items")
    public StudentDTO registerItem(@Valid @RequestBody StudentItem item) {
        return studentService.registerItem(item);
    }

    @PutMapping("/item/{id}")
    public StudentDTO updateItem(@PathVariable("id") Long itemId, @RequestBody StudentItem item) {
        return studentService.updateItem(itemId, item);
    }

    @DeleteMapping("/item/{id}")
    public StudentDTO deleteItem(@PathVariable("id") Long itemId) {
        return null; // studentService.deleteItem(itemId);
    }

    @PostMapping("/logout")
    public Map<String, Object> logoutStudent(@RequestHeader("token") String userToken) {
        return studentService.logoutStudent(userToken);
    }
}
