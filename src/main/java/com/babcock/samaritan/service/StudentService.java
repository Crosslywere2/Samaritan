package com.babcock.samaritan.service;

import com.babcock.samaritan.dto.StudentDTO;
import com.babcock.samaritan.entity.Student;
import com.babcock.samaritan.entity.StudentItem;
import com.babcock.samaritan.entity.Token;
import com.babcock.samaritan.error.*;
import com.babcock.samaritan.model.LoginCredentials;

import java.util.Map;

public interface StudentService {
    Token registerStudent(Student student) throws DataAlreadyExistException;

    Token loginStudent(LoginCredentials credentials) throws InvalidLoginCredentialsException;

    StudentDTO getStudentDetails() throws UserNotFoundException;

    StudentDTO updateStudentInfo(Student student) throws UserNotFoundException;

    StudentDTO registerItem(StudentItem item) throws UserNotFoundException, RequiredArgNotFoundException;

    Map<String, Object> logoutStudent(String userToken) throws UserNotFoundException;

    StudentDTO updateItem(Long itemId, StudentItem item) throws ItemNotFoundException, UserNotFoundException, InvalidItemOwnerException, RequiredArgNotFoundException;

    StudentDTO deleteItem(Long itemId) throws ItemNotFoundException, UserNotFoundException, InvalidItemOwnerException;
}
