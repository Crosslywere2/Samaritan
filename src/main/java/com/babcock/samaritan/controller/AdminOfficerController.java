package com.babcock.samaritan.controller;

import com.babcock.samaritan.dto.AdminOfficerDTO;
import com.babcock.samaritan.service.OfficerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminOfficerController {
    @Autowired
    private final OfficerService officerService;

    @GetMapping("/info")
    public AdminOfficerDTO fetchAdminOfficerInfo() {
        return officerService.getAdminOfficerInfo();
    }

    @DeleteMapping("/delete-{id}")
    public AdminOfficerDTO deleteOfficer(@PathVariable("id") String officerId) {
        return officerService.deleteOfficer(officerId);
    }
}
