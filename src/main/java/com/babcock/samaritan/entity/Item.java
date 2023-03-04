package com.babcock.samaritan.entity;

import com.babcock.samaritan.model.Color;
import com.babcock.samaritan.model.Type;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Item {

    @Id
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
    @NotBlank(message = "Color required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Color color;
    @Column(nullable = false)
    private String colorStr;
    @NotBlank(message = "Type required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Type type;
    @NotBlank
    @Column(nullable = false)
    private String typeStr;
}
