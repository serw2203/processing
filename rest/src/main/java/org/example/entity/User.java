package org.example.entity;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Table(name = "A_USER", schema = "", catalog = "")
@Data
public class User {
    @Id
    private Integer id;
    private String name;
    private String email;
    private Integer age;
}
