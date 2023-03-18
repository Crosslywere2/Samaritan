package com.babcock.samaritan.dto;

import com.babcock.samaritan.entity.Officer;
import com.babcock.samaritan.model.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OfficerDTO {
    private String officerId;
    private String firstName;
    private String lastName;
    private String otherNames;
    private String email;
    private Role role;

    public OfficerDTO(Officer officer) {
        officerId = officer.getId();
        firstName = officer.getFirstName();
        lastName = officer.getLastName();
        otherNames = officer.getOtherNames();
        email = officer.getEmail();
        role = officer.getRole();
    }
}
