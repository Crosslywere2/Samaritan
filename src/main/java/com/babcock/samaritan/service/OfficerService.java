package com.babcock.samaritan.service;

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

import java.util.Map;

public interface OfficerService {
    AdminOfficerDTO registerOfficer(Officer officer) throws DataAlreadyExistException, RequiredArgNotFoundException;

    Token loginOfficer(LoginCredentials credentials) throws InvalidLoginCredentialsException;

    OfficerDTO getOfficerDetails() throws UserNotFoundException;

    OfficerDTO updateOfficerInfo(Officer officer);

    Map<String, Object> registerFoundItem(FoundItem foundItem);

    Map<String, Object> logoutOfficer(String userToken);

    AdminOfficerDTO getAdminOfficerInfo();
}
