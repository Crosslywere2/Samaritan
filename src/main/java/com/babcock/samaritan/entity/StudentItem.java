package com.babcock.samaritan.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StudentItem extends Item {
    @Column(unique = true)
    private String serial;
    @Column(nullable = false)
    private Date dateRegistered;
    @Column(nullable = false)
    private Boolean lost = false;
    private Date dateLost;
    private Date dateIn;
    private Date dateOut;
}
