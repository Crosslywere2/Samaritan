package com.babcock.samaritan.controller;

import com.babcock.samaritan.dto.FoundItemDTO;
import com.babcock.samaritan.service.FoundItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/found")
public class FoundItemController {

    @Autowired
    private FoundItemService foundItemService;

    @GetMapping
    public List<FoundItemDTO> fetchAllFoundItems() {
        return foundItemService.fetchAllFoundItems();
    }
}
