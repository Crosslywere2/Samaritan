package com.babcock.samaritan.controller;

import com.babcock.samaritan.dto.AdminOfficerDTO;
import com.babcock.samaritan.service.OfficerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminOfficerController {
    @Autowired
    private OfficerService officerService;

    public AdminOfficerDTO fetchAdminOfficerInfo() {
        return officerService.getAdminOfficerInfo();
    }
}
