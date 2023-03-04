package com.babcock.samaritan.service;

import com.babcock.samaritan.dto.StudentDTO;
import com.babcock.samaritan.entity.Student;
import com.babcock.samaritan.entity.StudentItem;
import com.babcock.samaritan.entity.Token;
import com.babcock.samaritan.model.LoginCredentials;

import java.util.Map;

public interface StudentService {
    Token registerStudent(Student student);

    Token loginStudent(LoginCredentials credentials);

    StudentDTO getStudentDetails();

    StudentDTO updateStudentInfo(Student student);

    StudentDTO registerItem(StudentItem item);

    Map<String, Object> logoutStudent(String userToken);

    StudentDTO updateItem(Long itemId, StudentItem item);
}
