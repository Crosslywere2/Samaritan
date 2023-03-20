package com.babcock.samaritan.service.implementation;

import com.babcock.samaritan.dto.FoundItemDTO;
import com.babcock.samaritan.entity.FoundItem;
import com.babcock.samaritan.repository.FoundItemRepo;
import com.babcock.samaritan.service.FoundItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class FoundItemServiceImpl implements FoundItemService {

    @Autowired
    private FoundItemRepo foundItemRepo;

    @Override
    public List<FoundItemDTO> fetchAllFoundItems() {
        List<FoundItem> foundItems = foundItemRepo.findByClaimedByNull();
        List<FoundItemDTO> foundItemDTOs = new ArrayList<>(foundItems.size());
        foundItems.forEach(f -> foundItemDTOs.add(new FoundItemDTO(f)));
        return foundItemDTOs;
    }
}
