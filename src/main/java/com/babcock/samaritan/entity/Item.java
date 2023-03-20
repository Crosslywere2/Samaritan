package com.babcock.samaritan.entity;

import com.babcock.samaritan.model.Color;
import com.babcock.samaritan.model.Type;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Objects;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Item {

    @Id
    @GeneratedValue
    private Long id;
    @NotBlank(message = "Brand required")
    @Length(max = 255)
    @Column(nullable = false)
    private String brand;
    @NotBlank(message = "Model required")
    @Length(max = 255)
    @Column(nullable = false)
    private String model;
    @Length(max = 300)
    @Column(length = 300)
    private String description;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Color color;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Type type;
    @Column(nullable = false)
    private String typeName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Item item = (Item) o;
        return getId() != null && Objects.equals(getId(), item.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
