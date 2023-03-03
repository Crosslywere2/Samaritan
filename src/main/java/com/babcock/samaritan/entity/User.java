package com.babcock.samaritan.entity;

import com.babcock.samaritan.model.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User {
    @Id
    private String id;
    @NotBlank(message = "Password required")
    @Column(nullable = false)
    private String password;
    @NotBlank(message = "First name required")
    @Column(nullable = false)
    private String firstName;
    @NotBlank(message = "Last name required")
    @Column(nullable = false)
    private String lastName;
    private String otherNames;
    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private Role role;
    @NotBlank(message = "Email required")
    @Column(nullable = false, unique = true)
    private String email;
}
