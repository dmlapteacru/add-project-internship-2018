package com.endava.addprojectinternship2018.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "USERS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @Column(unique = true)
    private String username;

    @NotNull
    @Column
    private String password;

    @NotNull
    @Column
    @Enumerated(EnumType.STRING)
    private Role role;

    @NotNull
    @Column
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(String username, String password, UserStatus userStatus) {
        this.username = username;
        this.password = password;
        this.userStatus = userStatus;
    }
}
