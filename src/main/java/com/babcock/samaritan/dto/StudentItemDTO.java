package com.babcock.samaritan.dto;

import com.babcock.samaritan.entity.StudentItem;
import com.babcock.samaritan.model.Color;
import com.babcock.samaritan.model.Type;
import lombok.Data;

import java.util.Date;

@Data
public class StudentItemDTO {
    private Long id;
    private String brand;
    private String model;
    private String description;
    private Color color;
    private String colorName;
    private Type type;
    private String typeName;
    private String serial;
    private Date dateRegistered;
    private Boolean lost;
    private Date dateLost;
    private Date dateIn;
    private Date dateOut;

    public StudentItemDTO(StudentItem studentItem) {
        id = studentItem.getId();
        brand = studentItem.getBrand();
        model = studentItem.getModel();
        description = studentItem.getDescription();
        serial = studentItem.getSerial();
        color = studentItem.getColor();
        colorName = studentItem.getColorName();
        type = studentItem.getType();
        typeName = studentItem.getTypeName();
        dateRegistered = studentItem.getDateRegistered();
        lost = studentItem.getLost();
        dateLost = studentItem.getDateLost();
        dateIn = studentItem.getDateIn();
        dateOut = studentItem.getDateOut();
    }
}
