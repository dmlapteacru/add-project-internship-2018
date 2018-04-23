package com.endava.addprojectinternship2018.dao;

import com.endava.addprojectinternship2018.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDao extends JpaRepository<User, Integer> {
    Optional<User> findUserByUsername(String username);
}
