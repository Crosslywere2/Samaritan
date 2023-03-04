package com.babcock.samaritan.service.implementation;

import com.babcock.samaritan.entity.User;
import com.babcock.samaritan.repository.UserRepo;
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
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        Optional<User> userRes = userRepo.findById(userId);
        if (userRes.isPresent()) {
            User user = userRes.get();
            return new org.springframework.security.core.userdetails.User(
                    userId, user.getPassword(), Collections.singleton(new SimpleGrantedAuthority(user.getRole().name()))
            );
        }
        throw new UsernameNotFoundException("User with user id " + userId + " not found.");
    }
}
