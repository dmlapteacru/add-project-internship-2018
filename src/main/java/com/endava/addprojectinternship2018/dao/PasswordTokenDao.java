package com.endava.addprojectinternship2018.dao;

import com.endava.addprojectinternship2018.model.PasswordToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordTokenDao extends JpaRepository<PasswordToken, Integer> {
    Optional<PasswordToken> findByUsername(String username);
    void deleteByUsername(String username);
}
