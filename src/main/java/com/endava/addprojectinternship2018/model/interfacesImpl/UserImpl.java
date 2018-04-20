package com.endava.addprojectinternship2018.model.interfacesImpl;

import com.endava.addprojectinternship2018.model.enums.Role;
import com.endava.addprojectinternship2018.model.interfaces.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "USER")
@Data
@NoArgsConstructor
public class UserImpl implements User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "role")
    private Role role;

    public UserImpl(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }
}
