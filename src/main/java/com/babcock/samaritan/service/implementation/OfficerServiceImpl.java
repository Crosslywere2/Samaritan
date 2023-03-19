package com.babcock.samaritan.service.implementation;

import com.babcock.samaritan.dto.AdminOfficerDTO;
import com.babcock.samaritan.dto.OfficerDTO;
import com.babcock.samaritan.entity.FoundItem;
import com.babcock.samaritan.entity.Officer;
import com.babcock.samaritan.entity.Token;
import com.babcock.samaritan.error.DataAlreadyExistException;
import com.babcock.samaritan.error.InvalidLoginCredentialsException;
import com.babcock.samaritan.error.RequiredArgNotFoundException;
import com.babcock.samaritan.error.UserNotFoundException;
import com.babcock.samaritan.model.LoginCredentials;
import com.babcock.samaritan.repository.FoundItemRepo;
import com.babcock.samaritan.repository.OfficerRepo;
import com.babcock.samaritan.repository.StudentItemRepo;
import com.babcock.samaritan.security.JWTUtil;
import com.babcock.samaritan.service.OfficerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class OfficerServiceImpl implements OfficerService {
    @Autowired
    private OfficerRepo officerRepo;
    @Autowired
    private FoundItemRepo foundItemRepo;
    @Autowired
    private StudentItemRepo studentItemRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JWTUtil jwtUtil;

    @Override
    public AdminOfficerDTO registerOfficer(Officer officer) throws DataAlreadyExistException, RequiredArgNotFoundException {
        String registeredById = SecurityContextHolder.getContext().getAuthentication().getName();
        Officer registeredBy = officerRepo.findById(registeredById).orElseThrow();
        if (!officerRepo.existsById(officer.getId())) {
            if (!officerRepo.existsByEmailIgnoreCase(officer.getEmail())) {
                if (Objects.nonNull(officer.getRole())) {
                    String encodedPassword = passwordEncoder.encode(officer.getPassword());
                    officer.setPassword(encodedPassword);
                    officer.setRegisteredBy(registeredBy);
                    officerRepo.save(officer);
                    return new AdminOfficerDTO(registeredBy, officerRepo.findByRegisteredBy_IdIgnoreCase(registeredById));
                }
                throw new RequiredArgNotFoundException("Officer role not specified");
            }
            throw new DataAlreadyExistException("Email is already taken");
        }
        throw new DataAlreadyExistException("Officer with student id " + officer.getId() + " already exists");
    }

    @Override
    public Token loginOfficer(LoginCredentials credentials) throws InvalidLoginCredentialsException {
        Optional<Officer> officer = officerRepo.findById(credentials.getUserId());
        if (officer.isPresent()) {
            if (passwordEncoder.matches(credentials.getPassword(), officer.get().getPassword())) {
                String token = jwtUtil.generateToken(credentials.getUserId());
                return new Token(token, credentials.getUserId());
            }
            throw new InvalidLoginCredentialsException("Invalid user id or password");
        }
        throw new InvalidLoginCredentialsException("Invalid user id or password");
    }

    @Override
    public OfficerDTO getOfficerDetails() throws UserNotFoundException {
        String officerId = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Officer> officer = officerRepo.findById(officerId);
        if (officer.isPresent()) {
            return new OfficerDTO(officer.get());
        } else {
            throw new UserNotFoundException("Officer does not exist");
        }
    }

    @Override
    public OfficerDTO updateOfficerInfo(Officer officer) {
        String officerId = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Officer> officerRes = officerRepo.findById(officerId);
        if (officerRes.isPresent()) {
            Officer o = officerRes.get();
            if (Objects.nonNull(officer.getPassword()) && !"".equalsIgnoreCase(officer.getPassword())) {
                if (officer.getPassword().length() >= 8 && officer.getPassword().length() <= 127) {
                    String encodedPassword = passwordEncoder.encode(officer.getPassword());
                    o.setPassword(encodedPassword);
                }
                if (Objects.nonNull(officer.getFirstName()) && !"".equalsIgnoreCase(officer.getFirstName())) {
                    o.setFirstName(officer.getFirstName());
                }
                if (Objects.nonNull(officer.getLastName()) && !"".equalsIgnoreCase(officer.getLastName())) {
                    o.setLastName(officer.getLastName());
                }
                if (Objects.nonNull(officer.getOtherNames()) && !"".equalsIgnoreCase(officer.getOtherNames())) {
                    o.setOtherNames(officer.getOtherNames());
                }
            }
            return new OfficerDTO(officerRepo.save(o));
        }
        throw new RuntimeException("");
    }

    @Override
    public Map<String, Object> registerFoundItem(FoundItem foundItem) {
        foundItem.setDateFound(new Date());
        return Collections.singletonMap("success", true);
    }

    @Override
    public Map<String, Object> logoutOfficer(String userToken) {
        String officerId = SecurityContextHolder.getContext().getAuthentication().getName();
        return Collections.singletonMap("success", jwtUtil.invalidateToken(new Token(userToken, officerId)));
    }

    @Override
    public AdminOfficerDTO getAdminOfficerInfo() {
        String officerId = SecurityContextHolder.getContext().getAuthentication().getName();
        return new AdminOfficerDTO(
                officerRepo.findById(officerId).orElseThrow(() -> new UsernameNotFoundException("Officer not found")),
                officerRepo.findByRegisteredBy_IdIgnoreCase(officerId)
        );
    }
}
