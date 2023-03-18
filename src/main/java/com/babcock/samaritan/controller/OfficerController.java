package com.babcock.samaritan.controller;

import com.babcock.samaritan.dto.OfficerDTO;
import com.babcock.samaritan.entity.FoundItem;
import com.babcock.samaritan.entity.Officer;
import com.babcock.samaritan.entity.Token;
import com.babcock.samaritan.error.InvalidLoginCredentialsException;
import com.babcock.samaritan.error.UserNotFoundException;
import com.babcock.samaritan.model.LoginCredentials;
import com.babcock.samaritan.service.OfficerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/officer")
public class OfficerController {
    @Autowired
    private OfficerService officerService;

    @GetMapping("/login")
    public Token loginOfficer(@RequestBody LoginCredentials credentials) throws InvalidLoginCredentialsException {
        return officerService.loginOfficer(credentials);
    }

    @GetMapping("/info")
    public OfficerDTO getOfficerDetails() throws UserNotFoundException {
        return officerService.getOfficerDetails();
    }

    @PutMapping("/info")
    public OfficerDTO updateOfficerInfo(@RequestBody Officer officer) {
        return officerService.updateOfficerInfo(officer);
    }

    @PostMapping("/found")
    public Map<String, Object> registerFoundItem(@Valid @RequestBody FoundItem foundItem) {
        return officerService.registerFoundItem(foundItem);
    }

    @PostMapping("/logout")
    public Map<String, Object> logoutOfficer(@RequestHeader("Authorization") String userToken) {
        return officerService.logoutOfficer(userToken);
    }
}
