package com.babcock.samaritan.entity;

import com.babcock.samaritan.model.Role;
import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Officer extends User {
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "registered_by_id")
    private Officer registeredBy;

    private boolean admin = false;

    @Override
    public Role getRole() {
        return admin ? Role.ADMIN_OFFICER : Role.OFFICER;
    }
}
