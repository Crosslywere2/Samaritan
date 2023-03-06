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
            if (!studentRepo.existsByEmailIgnoreCase(student.getEmail())) {
                String encoded = passwordEncoder.encode(student.getPassword());
                student.setPassword(encoded);
                student.setRole(Role.STUDENT);
                User user = studentRepo.save(student);
                String token = jwtUtil.generateToken(user.getId());
                log.info("Registered Student with ID {}", student.getId());
                return new Token(token);
            }
            log.info("Student with email {} already exists", student.getEmail());
            throw new DataAlreadyExistException("Email is already taken");
        }
        log.info("Student with ID {} already exists", student.getId());
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
            return new StudentDTO(student.get(), studentItemRepo.findByOwnedBy_Id(studentId));
        }
        else {
            log.info("Unable to retrieve student with ID {}'s data", studentId);
            throw new UserNotFoundException("Student does not exist");
        }
    }

    @Override
    public StudentDTO updateStudentInfo(Student student) throws UserNotFoundException {
        String studentId = SecurityContextHolder.getContext().getAuthentication().getName();
        if (studentRepo.findById(studentId).isPresent()) {
            Student s = studentRepo.findById(studentId).get();
            if (Objects.nonNull(student.getFirstName()) && !"".equalsIgnoreCase(student.getFirstName())) {
                log.info("Student with ID {} changed first name", studentId);
                s.setFirstName(student.getFirstName());
            }
            if (Objects.nonNull(student.getLastName()) && !"".equalsIgnoreCase(student.getLastName())) {
                log.info("Student with ID {} changed last name", studentId);
                s.setLastName(student.getLastName());
            }
            if (Objects.nonNull(student.getOtherNames()) && !"".equalsIgnoreCase(student.getOtherNames())) {
                log.info("Student with ID {} changed other name", studentId);
                s.setOtherNames(student.getOtherNames());
            }
            if (Objects.nonNull(student.getEmail()) && !"".equalsIgnoreCase(student.getEmail())) {
                log.info("Student with ID {} changed email", studentId);
                s.setEmail(student.getEmail());
            }
            if (Objects.nonNull(student.getPassword()) && !"".equalsIgnoreCase(student.getPassword())) {
                log.info("Student with ID {} changed password", studentId);
                String encodedPassword = passwordEncoder.encode(student.getPassword());
                s.setPassword(encodedPassword);
            }
            return new StudentDTO(studentRepo.save(s), studentItemRepo.findByOwnedBy_Id(studentId));
        }
        throw new UserNotFoundException("Student does not exist");
    }

    @Override
    public StudentDTO registerItem(StudentItem item) throws UserNotFoundException, RequiredArgNotFoundException {
        String studentId = SecurityContextHolder.getContext().getAuthentication().getName();
        Student student = studentRepo.findById(studentId).orElseThrow(() -> new UserNotFoundException("Student does not exist"));
        if (Objects.isNull(item.getId())) {
            item.setDateRegistered(new Date());
            if (item.getColor() != Color.OTHER) {
                item.setColorName(item.getColor().name().toLowerCase());
            } else if (Objects.nonNull(item.getColorName()) && !"".equalsIgnoreCase(item.getColorName())) {
                item.setColorName(item.getColorName().toLowerCase());
            } else {
                throw new RequiredArgNotFoundException("Color name not present when selecting other as color");
            }
            if (item.getType() != Type.OTHER) {
                item.setTypeName(item.getType().name().toLowerCase());
            } else if (Objects.nonNull(item.getTypeName()) && !"".equalsIgnoreCase(item.getTypeName())) {
                item.setTypeName(item.getTypeName().toLowerCase());
            } else {
                throw new RequiredArgNotFoundException("Type name not present when selecting other as type");
            }
            item.setOwnedBy(student);
            studentItemRepo.save(item);
            return new StudentDTO(student, studentItemRepo.findByOwnedBy_Id(studentId));
        }
        log.info("Student with ID {} attempted to register an item with an ID", studentId);
        throw new UserNotFoundException("Student does not exist");
    }

    @Override
    public Map<String, Object> logoutStudent(String userToken) throws UserNotFoundException {
        String studentId = SecurityContextHolder.getContext().getAuthentication().getName();
        if (jwtUtil.invalidateToken(new Token(userToken))) {
            log.info("Logged out Student with ID {}", studentId);
            SecurityContextHolder.clearContext();
            return Collections.singletonMap("success", true);
        } else {
            throw new UserNotFoundException("Could not sign out student");
        }
    }

    @Override
    public StudentDTO updateItem(Long itemId, StudentItem modifiedItem) throws ItemNotFoundException, UserNotFoundException, InvalidItemOwnerException, RequiredArgNotFoundException {
        String studentId = SecurityContextHolder.getContext().getAuthentication().getName();
        StudentItem item = studentItemRepo.findById(itemId).orElseThrow(() -> new ItemNotFoundException("Item does not exist"));
        Student student = studentRepo.findById(studentId).orElseThrow(() -> new UserNotFoundException("Student does not exist"));
        if (studentItemRepo.findByOwnedBy_Id(studentId).contains(item)) {
            if (Objects.nonNull(modifiedItem.getLost())) {
                item.setLost(modifiedItem.getLost());
                if (item.getLost()) {
                    log.info("Student with ID {} lost item with ID {}", studentId, itemId);
                    item.setDateLost(new Date());
                } else {
                    log.info("Student with ID {} found item with ID {}", studentId, itemId);
                    item.setDateLost(null);
                }
            }
            if (Objects.nonNull(modifiedItem.getDescription())) {
                log.info("Student with ID {} updated item with ID {} description", studentId, itemId);
                item.setDescription(modifiedItem.getDescription());
            }
            if (Objects.nonNull(modifiedItem.getColor())) {
                log.info("Student with ID {} changed color of item with ID {}", studentId, itemId);
                item.setColor(modifiedItem.getColor());
                if (item.getColor() != Color.OTHER) {
                    item.setColorName(item.getColor().name().toLowerCase());
                } else if (Objects.nonNull(modifiedItem.getColorName()) && !"".equalsIgnoreCase(modifiedItem.getColorName())) {
                    item.setColorName(modifiedItem.getColorName().toLowerCase());
                } else {
                    log.info("Student with ID {} attempted to change color to 'OTHER' for item without providing color name", studentId);
                    throw new RequiredArgNotFoundException("Color name not specified when color is other");
                }
            }
            if (Objects.nonNull(modifiedItem.getDateIn()) && item.getDateIn() == null) {
                log.info("Student with ID {} set date in of item with ID {}", studentId, itemId);
                item.setDateIn(modifiedItem.getDateIn());
            }
            if (Objects.nonNull(modifiedItem.getDateOut()) && Objects.nonNull(item.getDateIn()) && item.getDateOut() == null) {
                log.info("Student with ID {} set date out of item with ID {}", studentId, itemId);
                item.setDateOut(modifiedItem.getDateOut());
            }
            studentItemRepo.save(item);
            return new StudentDTO(student, studentItemRepo.findByOwnedBy_Id(studentId));
        }
        log.error("Student with ID {} tried to modify item not owned", studentId);
        throw new InvalidItemOwnerException("Item does not belong to this student");
    }

    @Override
    public StudentDTO deleteItem(Long itemId) throws ItemNotFoundException, UserNotFoundException, InvalidItemOwnerException {
        String studentId = SecurityContextHolder.getContext().getAuthentication().getName();
        StudentItem item = studentItemRepo.findById(itemId).orElseThrow(() -> new ItemNotFoundException("Item does not exist"));
        Student student = studentRepo.findById(studentId).orElseThrow(() -> new UserNotFoundException("Student does not exist"));
        if (studentItemRepo.findByOwnedBy_Id(studentId).contains(item)) {
            log.info("Student with ID {} deleted item", studentId);
            studentItemRepo.deleteById(itemId);
            return new StudentDTO(student, studentItemRepo.findByOwnedBy_Id(studentId));
        }
        log.info("Student with ID {} tried to delete item not owned", studentId);
        throw new InvalidItemOwnerException("Item does not belong to this student");
    }
}
