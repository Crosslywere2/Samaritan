package com.babcock.samaritan.service.implementation;

import com.babcock.samaritan.entity.FoundItem;
import com.babcock.samaritan.repository.FoundItemRepo;
import com.babcock.samaritan.service.FoundItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class FoundItemServiceImpl implements FoundItemService {

    @Autowired
    private FoundItemRepo foundItemRepo;

    @Override
    public List<FoundItem> fetchAllFoundItems() {
        return foundItemRepo.findAll();
    }
}
