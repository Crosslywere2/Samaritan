package com.babcock.samaritan.service.implementation;

import com.babcock.samaritan.dto.StudentDTO;
import com.babcock.samaritan.entity.Student;
import com.babcock.samaritan.entity.StudentItem;
import com.babcock.samaritan.entity.Token;
import com.babcock.samaritan.entity.User;
import com.babcock.samaritan.error.*;
import com.babcock.samaritan.model.Color;
import com.babcock.samaritan.model.LoginCredentials;
import com.babcock.samaritan.model.Role;
import com.babcock.samaritan.model.Type;
import com.babcock.samaritan.repository.StudentItemRepo;
import com.babcock.samaritan.repository.StudentRepo;
import com.babcock.samaritan.security.JWTUtil;
import com.babcock.samaritan.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
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
    public Token registerStudent(Student student) throws DataAlreadyExistException {
        if (!studentRepo.existsById(student.getId())) {
            String encoded = passwordEncoder.encode(student.getPassword());
            student.setPassword(encoded);
            student.setRole(Role.STUDENT);
            User user = studentRepo.save(student);
            String token = jwtUtil.generateToken(user.getId());
            log.info("Registered Student with ID {}", student.getId());
            return new Token(token);
        }
        throw new DataAlreadyExistException("Student with student id " + student.getId() + " already exists");
    }

    @Override
    public Token loginStudent(LoginCredentials credentials) throws InvalidLoginCredentialsException {
        Optional<Student> student = studentRepo.findById(credentials.getUserId());
        if (student.isPresent()) {
            if (passwordEncoder.matches(credentials.getPassword(), student.get().getPassword())) {
                log.info("Logged in Student with ID {}", credentials.getUserId());
                String token = jwtUtil.generateToken(credentials.getUserId());
                return new Token(token);
            }
            log.info("Login attempt failed for student with ID {}", credentials.getUserId());
            throw new InvalidLoginCredentialsException("Invalid user id or password");
        }
        log.info("Login attempt failed for student with ID {}", credentials.getUserId());
        throw new InvalidLoginCredentialsException("Invalid user id or password");
    }

    @Override
    public StudentDTO getStudentDetails() throws UserNotFoundException {
        String studentId = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Student> student = studentRepo.findById(studentId);
        if (student.isPresent()) {
            log.info("Retrieved student with ID {}'s data", studentId);
            return new StudentDTO(student.get());
        }
        else {
            log.info("Unable to retrieve student with ID {}'s data", studentId);
            throw new UserNotFoundException("Student with student id " + studentId + " not found");
        }
    }

    @Override
    public StudentDTO updateStudentInfo(Student student) throws UserNotFoundException {
        String studentId = SecurityContextHolder.getContext().getAuthentication().getName();
        if (studentRepo.findById(studentId).isPresent()) {
            Student s = studentRepo.findById(studentId).get();
            if (!"".equalsIgnoreCase(student.getFirstName())) {
                log.info("Student with ID {} changed first name", studentId);
                s.setFirstName(student.getFirstName());
            }
            if (!"".equalsIgnoreCase(student.getLastName())) {
                log.info("Student with ID {} changed last name", studentId);
                s.setLastName(student.getLastName());
            }
            if (!"".equalsIgnoreCase(student.getOtherNames())) {
                log.info("Student with ID {} changed other name", studentId);
                s.setOtherNames(student.getOtherNames());
            }
            if (!"".equalsIgnoreCase(student.getEmail())) {
                log.info("Student with ID {} changed email", studentId);
                s.setEmail(student.getEmail());
            }
            if (!"".equalsIgnoreCase(student.getPassword())) {
                log.info("Student with ID {} changed password", studentId);
                String encodedPassword = passwordEncoder.encode(student.getPassword());
                s.setPassword(encodedPassword);
            }
            return new StudentDTO(studentRepo.save(s));
        }
        throw new UserNotFoundException("Student with student id " + studentId + " not found");
    }

    @Override
    public StudentDTO registerItem(StudentItem item) throws UserNotFoundException {
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
        log.info("Student with ID {} not found", studentId);
        throw new UserNotFoundException("Student id could not be determined");
    }

    @Override
    public Map<String, Object> logoutStudent(String userToken) {
        String studentId = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("Logged out Student with ID {}", studentId);
        SecurityContextHolder.clearContext();
        return Collections.singletonMap("success", jwtUtil.invalidateToken(new Token(userToken)));
    }

    @Override
    public StudentDTO updateItem(Long itemId, StudentItem studentItem) throws ItemNotFoundException, UserNotFoundException, InvalidItemOwnerException, RequiredArgNotFoundException {
        String studentId = SecurityContextHolder.getContext().getAuthentication().getName();
        StudentItem item = studentItemRepo.findById(itemId).orElseThrow(() -> new ItemNotFoundException("Item does not exist"));
        Student student = studentRepo.findById(studentId).orElseThrow(() -> new UserNotFoundException("Student id could not be determined"));
        int i;
        if ((i = student.getItems().indexOf(item)) >= 0) {
            if (Objects.nonNull(studentItem.getLost())) {
                item.setLost(studentItem.getLost());
                if (studentItem.getLost()) {
                    log.info("Student with ID {} lost item with ID {}", studentId, itemId);
                    item.setDateLost(new Date());
                }
                else {
                    log.info("Student with ID {} found item with ID {}", studentId, itemId);
                    item.setDateLost(null);
                }
            }
            if (Objects.nonNull(studentItem.getDescription())) {
                log.info("Student with ID {} updated item with ID {} description", studentId, itemId);
                item.setDescription(studentItem.getDescription());
            }
            if (Objects.nonNull(studentItem.getColor())) {
                item.setColor(studentItem.getColor());
                if (studentItem.getColor() != Color.OTHER) {
                    log.info("Student with ID {} changed color of item with ID {}", studentId, itemId);
                    item.setColorStr(studentItem.getColor().name().toLowerCase());
                }
                else if (!"".equalsIgnoreCase(studentItem.getColorStr())) {
                    log.info("Student with ID {} changed color name of item with ID {}", studentId, itemId);
                    item.setColorStr(studentItem.getColorStr().toLowerCase());
                } else {
                    log.info("Student with ID {} attempted to change color of item with ID {} to other without providing color name", studentId, itemId);
                    throw new RequiredArgNotFoundException("Color name not provided");
                }
            }
            if (Objects.nonNull(studentItem.getDateIn())) {
                if (!Objects.nonNull(item.getDateIn())) {
                    log.info("Student with ID {} updated item with ID {} in-date", studentId, itemId);
                    item.setDateIn(studentItem.getDateIn());
                } else {
                    log.info("Student with ID {} failed to update item with ID {} in-date", studentId, itemId);
                }
            }
            if (Objects.nonNull(studentItem.getDateOut())) {
                 if (Objects.nonNull(item.getDateIn()) && !Objects.nonNull(item.getDateOut())) {
                     log.info("Student with ID {} updated item with ID {} out-date", studentId, itemId);
                     item.setDateOut(studentItem.getDateOut());
                 } else {
                     log.info("Student with ID {} failed to updated item with ID {} out-date", studentId, itemId);
                 }
            }
            student.getItems().remove(i);
            student.getItems().add(studentItemRepo.save(item));
            return new StudentDTO(student);
        }
        log.error("Student with ID {} tried to modify item not owned", studentId);
        throw new InvalidItemOwnerException("Item does not belong to this student");
    }

    @Override
    public StudentDTO deleteItem(Long itemId) throws ItemNotFoundException, UserNotFoundException, InvalidItemOwnerException {
        String studentId = SecurityContextHolder.getContext().getAuthentication().getName();
        StudentItem item = studentItemRepo.findById(itemId).orElseThrow(() -> new ItemNotFoundException("Item does not exist"));
        Student student = studentRepo.findById(studentId).orElseThrow(() -> new UserNotFoundException("Student id could not be determined"));
        int i;
        if ((i = student.getItems().indexOf(item)) >= 0) {
            log.info("Student with ID {} deleted item", studentId);
            studentItemRepo.deleteById(itemId);
            student.getItems().remove(i);
            return new StudentDTO(student);
        }
        log.info("Student with ID {} tried to delete item not owned", studentId);
        throw new InvalidItemOwnerException("Item does not belong to this student");
    }
}
