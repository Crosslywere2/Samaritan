package com.babcock.samaritan.dto;

import com.babcock.samaritan.entity.Student;
import com.babcock.samaritan.entity.StudentItem;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class StudentDTO {
    private String studentId;
    private String firstName;
    private String otherNames;
    private String lastName;
    private String email;
    private List<StudentItem> items;

    public StudentDTO(Student student) {
        studentId = student.getId();
        firstName = student.getFirstName();
        lastName = student.getLastName();
        otherNames = student.getOtherNames();
        email = student.getEmail();
        items = student.getItems();
    }
}
