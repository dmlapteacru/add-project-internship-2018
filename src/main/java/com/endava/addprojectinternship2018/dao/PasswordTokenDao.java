package com.endava.addprojectinternship2018.dao;

import com.endava.addprojectinternship2018.model.PasswordToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordTokenDao extends JpaRepository<PasswordToken, Integer> {
    Optional<PasswordToken> findByUsername(String username);
    void deleteByUsername(String username);

    @Query(value = "DELETE FROM password_token WHERE date < (now()- INTERVAL '24' HOUR )", nativeQuery = true)
    void deleteOverdueTokens();
}
