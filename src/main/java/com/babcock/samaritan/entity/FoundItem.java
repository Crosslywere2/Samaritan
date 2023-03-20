package com.babcock.samaritan.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FoundItem extends Item {
    @ManyToOne
    @JoinColumn(name = "found_by_id")
    private User foundBy;
    private Date dateFound;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn
    private Student claimedBy;
    private Date dateClaimed;
}
