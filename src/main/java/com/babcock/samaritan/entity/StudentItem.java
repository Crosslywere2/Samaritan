package com.babcock.samaritan.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class StudentItem extends Item {
    @Column(unique = true)
    private String serial;
    private Date dateRegistered;
    private Boolean lost = false;
    private Date dateLost;
    private Date dateIn;
    private Date dateOut;
}
