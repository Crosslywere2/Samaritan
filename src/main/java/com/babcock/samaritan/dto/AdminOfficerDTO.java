package com.babcock.samaritan.dto;

import com.babcock.samaritan.entity.Officer;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class AdminOfficerDTO extends OfficerDTO {
    private List<OfficerDTO> registeredOfficers;

    public AdminOfficerDTO(Officer officer, List<Officer> registeredOfficers) {
        super(officer);
        this.registeredOfficers = new ArrayList<>(registeredOfficers.size());
        registeredOfficers.forEach((registeredOfficer) -> this.registeredOfficers.add(new OfficerDTO(registeredOfficer)));
    }
}
