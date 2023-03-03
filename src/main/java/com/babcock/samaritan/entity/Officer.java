package com.babcock.samaritan.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Officer extends User {
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "registered_by_id", nullable = false)
    private Officer registeredBy;
}
