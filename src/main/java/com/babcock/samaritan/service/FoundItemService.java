package com.babcock.samaritan.service;

import com.babcock.samaritan.dto.FoundItemDTO;

import java.util.List;

public interface FoundItemService {
    List<FoundItemDTO> fetchAllFoundItems();
}
