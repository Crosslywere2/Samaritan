package com.babcock.samaritan.dto;

import com.babcock.samaritan.entity.FoundItem;
import com.babcock.samaritan.model.Color;
import lombok.Getter;

import java.util.Date;

@Getter
public class FoundItemDTO {
    private final long id;
    private final String brand;
    private final String model;
    private final String description;
    private final Color color;
    private final String typeName;
    private final Date foundDate;

    public FoundItemDTO(FoundItem foundItem) {
        id = foundItem.getId();
        brand = foundItem.getBrand();
        model = foundItem.getModel();
        description = foundItem.getDescription();
        color = foundItem.getColor();
        typeName = foundItem.getTypeName();
        foundDate = foundItem.getDateFound();
    }
}
