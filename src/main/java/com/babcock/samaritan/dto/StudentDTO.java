package com.babcock.samaritan.dto;

import com.babcock.samaritan.entity.Student;
import com.babcock.samaritan.entity.StudentItem;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
public class StudentDTO {
    private String studentId;
    private String firstName;
    private String otherNames;
    private String lastName;
    private String email;
    private List<StudentItemDTO> items;

    public StudentDTO(Student student, List<StudentItem> items) {
        studentId = student.getId();
        firstName = student.getFirstName();
        lastName = student.getLastName();
        otherNames = student.getOtherNames();
        email = student.getEmail();
        this.items = new ArrayList<>(items.size());
        items.forEach((item) -> this.items.add(new StudentItemDTO(item)));
    }
}
