package com.babcock.samaritan.entity;

import com.babcock.samaritan.model.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public abstract class User {
    @Id
    private String id;
    @NotBlank(message = "Password required")
    @Length(min = 8, max = 127)
    @Column(nullable = false)
    private String password;
    @NotBlank(message = "First name required")
    @Column(nullable = false)
    private String firstName;
    @NotBlank(message = "Last name required")
    @Column(nullable = false)
    private String lastName;
    private String otherNames;
    @Email
    @NotBlank(message = "Email required")
    @Column(nullable = false, unique = true)
    private String email;
    public abstract Role getRole();
}
