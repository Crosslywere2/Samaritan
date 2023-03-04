package com.babcock.samaritan.service.implementation;

import com.babcock.samaritan.dto.StudentDTO;
import com.babcock.samaritan.entity.Student;
import com.babcock.samaritan.entity.StudentItem;
import com.babcock.samaritan.entity.Token;
import com.babcock.samaritan.entity.User;
import com.babcock.samaritan.model.Color;
import com.babcock.samaritan.model.LoginCredentials;
import com.babcock.samaritan.model.Role;
import com.babcock.samaritan.model.Type;
import com.babcock.samaritan.repository.StudentItemRepo;
import com.babcock.samaritan.repository.StudentRepo;
import com.babcock.samaritan.security.JWTUtil;
import com.babcock.samaritan.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StudentServiceImpl implements StudentService {
    @Autowired
    private StudentRepo studentRepo;
    @Autowired
    private StudentItemRepo studentItemRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JWTUtil jwtUtil;

    @Override
    public Token registerStudent(Student student) {
        if (studentRepo.findById(student.getId()).isEmpty()) {
            String encoded = passwordEncoder.encode(student.getPassword());
            student.setPassword(encoded);
            student.setRole(Role.STUDENT);
            User user = studentRepo.save(student);
            String token = jwtUtil.generateToken(user.getId());
            return new Token(token);
        }
        // TODO Change exception to custom exception
        throw new RuntimeException("Student with student id " + student.getId() + " already exists");
    }

    @Override
    public Token loginStudent(LoginCredentials credentials) {
        Optional<Student> student = studentRepo.findById(credentials.getUserId());
        if (student.isPresent()) {
            if (passwordEncoder.matches(credentials.getPassword(), student.get().getPassword())) {
                String token = jwtUtil.generateToken(credentials.getUserId());
                return new Token(token);
            }
            // TODO Change exception to custom exception
            throw new RuntimeException("Invalid user id or password");
        }
        // TODO Change exception to custom exception
        throw new RuntimeException("Invalid user id or password");
    }

    @Override
    public StudentDTO getStudentDetails() {
        String studentId = SecurityContextHolder.getContext().getAuthentication().getName();
        return new StudentDTO(studentRepo.findById(studentId).orElseThrow(
            // TODO Change exception to custom exception
            () -> new RuntimeException("Student with student id " + studentId + " not found")
        ));
    }

    @Override
    public StudentDTO updateStudentInfo(Student student) {
        String studentId = SecurityContextHolder.getContext().getAuthentication().getName();
        if (studentRepo.findById(studentId).isPresent()) {
            Student s = studentRepo.findById(studentId).get();
            if (!"".equalsIgnoreCase(student.getFirstName()))
                s.setFirstName(student.getFirstName());

            if (!"".equalsIgnoreCase(student.getLastName()))
                s.setLastName(student.getLastName());

            if (!"".equalsIgnoreCase(student.getOtherNames()))
                s.setOtherNames(student.getOtherNames());

            if (!"".equalsIgnoreCase(student.getEmail()))
                s.setEmail(student.getEmail());

            if (!"".equalsIgnoreCase(student.getPassword())) {
                String encodedPassword = passwordEncoder.encode(student.getPassword());
                s.setPassword(encodedPassword);
            }
            studentRepo.save(s);
        }
        // TODO Change exception to custom exception
        throw new RuntimeException("Student with student id " + studentId + " not found");
    }

    @Override
    public StudentDTO registerItem(StudentItem item) {
        String studentId = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Student> studentRes;
        if ((studentRes = studentRepo.findById(studentId)).isPresent()) {
            Student s = studentRes.get();
            item.setDateRegistered(new Date());
            if (item.getColor() != Color.OTHER)
                item.setColorStr(item.getColor().name().toLowerCase());
            else
                item.setColorStr(item.getColorStr().toLowerCase());

            if (item.getType() != Type.OTHER)
                item.setTypeStr(item.getType().name().toLowerCase());
            else
                item.setTypeStr(item.getTypeStr().toLowerCase());

            StudentItem i = studentItemRepo.save(item);
            s.getItems().add(i);
            return new StudentDTO(studentRepo.save(s));
        }
        // TODO Change exception to custom exception
        throw new RuntimeException("Student id could not be determined");
    }

    @Override
    public Map<String, Object> logoutStudent(String userToken) {
        SecurityContextHolder.clearContext();
        return Collections.singletonMap("success", jwtUtil.invalidateToken(new Token(userToken)));
    }

    @Override
    public StudentDTO updateItem(Long itemId, StudentItem studentItem) {
        String studentId = SecurityContextHolder.getContext().getAuthentication().getName();
        // TODO Change exception to custom exception
        StudentItem item = studentItemRepo.findById(itemId).orElseThrow(() -> new RuntimeException("Item does not exist"));
        Student student = studentRepo.findById(studentId).orElseThrow(() -> new RuntimeException("Student id could not be determined"));
        if (student.getItems().contains(item)) {
            int i = student.getItems().indexOf(item);
            if (Objects.nonNull(studentItem.getLost())) {
                item.setLost(studentItem.getLost());
                if (studentItem.getLost())
                    item.setDateLost(new Date());
                else
                    item.setDateLost(null);
            }

            if (!"".equalsIgnoreCase(studentItem.getDescription()))
                item.setDescription(studentItem.getDescription());

            if (Objects.nonNull(studentItem.getColor())) {
                item.setColor(studentItem.getColor());
                if (studentItem.getColor() != Color.OTHER)
                    item.setColorStr(studentItem.getColor().name().toLowerCase());
                else
                    item.setColorStr(studentItem.getColorStr().toLowerCase());
            }

            if (Objects.nonNull(studentItem.getDateIn()) && !Objects.nonNull(item.getDateIn()))
                item.setDateIn(studentItem.getDateIn());

            if (Objects.nonNull(studentItem.getDateOut()) && Objects.nonNull(item.getDateIn()) && !Objects.nonNull(item.getDateOut()))
                item.setDateOut(studentItem.getDateOut());

            student.getItems().remove(i);
            student.getItems().add(i, studentItemRepo.save(item));
            return new StudentDTO(student);
        } else {
            // TODO Change exception to custom exception
            throw new RuntimeException("Item does not belong to this student");
        }
    }

    @Override
    public StudentDTO deleteItem(Long itemId) {
        String studentId = SecurityContextHolder.getContext().getAuthentication().getName();
        // TODO Change exception to custom exception
        StudentItem item = studentItemRepo.findById(itemId).orElseThrow(() -> new RuntimeException("Item does not exist"));
        Student student = studentRepo.findById(studentId).orElseThrow(() -> new RuntimeException("Student id could not be determined"));
        int i;
        if ((i = student.getItems().indexOf(item)) >= 0) {
            studentItemRepo.deleteById(itemId);
            student.getItems().remove(i);
            return new StudentDTO(student);
        }
        // TODO Change exception to custom exception
        throw new RuntimeException("Item does not belong to this student");
    }
}
