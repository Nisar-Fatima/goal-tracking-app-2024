package com.techtide.goaltracking.entity;
import jakarta.persistence.*;
import lombok.Data;
@Data
@Entity
@Table(schema = "goal", name="signup")
public class SignUpEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_name")
    private String username;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

}
