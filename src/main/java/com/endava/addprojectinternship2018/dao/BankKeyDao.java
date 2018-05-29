package com.endava.addprojectinternship2018.dao;

import com.endava.addprojectinternship2018.model.BankKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankKeyDao extends JpaRepository<BankKey, Integer> {

}
