package com.babcock.samaritan.service.implementation;

import com.babcock.samaritan.entity.Officer;
import com.babcock.samaritan.entity.Student;
import com.babcock.samaritan.repository.OfficerRepo;
import com.babcock.samaritan.repository.StudentRepo;
import com.babcock.samaritan.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private StudentRepo studentRepo;
    @Autowired
    private OfficerRepo officerRepo;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
//        Optional<User> userRes = userRepo.findById(userId);
//        if (userRes.isPresent()) {
//            User user = userRes.get();
//            return new org.springframework.security.core.userdetails.User(
//                    userId, user.getPassword(), Collections.singleton(new SimpleGrantedAuthority(user.getRole().name()))
//            );
//        }
        Optional<Student> studentRes;
        Optional<Officer> officerRes;
        if ((studentRes = studentRepo.findById(userId)).isPresent()) {
             Student student = studentRes.get();
             return new org.springframework.security.core.userdetails.User(
                     userId, student.getPassword(), Collections.singleton(new SimpleGrantedAuthority(student.getRole().name()))
             );
        } else if ((officerRes = officerRepo.findById(userId)).isPresent()) {
            Officer officer = officerRes.get();
            return new org.springframework.security.core.userdetails.User(
                    userId, officer.getPassword(), Collections.singleton(new SimpleGrantedAuthority(officer.getRole().name()))
            );
        }
        throw new UsernameNotFoundException("User with user id " + userId + " not found.");
    }
}
