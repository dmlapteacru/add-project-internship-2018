package com.endava.addprojectinternship2018.dao;

import com.endava.addprojectinternship2018.model.Company;
import com.endava.addprojectinternship2018.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyDao extends JpaRepository<Company, Integer> {

    List<Company> findAllByOrderByName();

    Optional<Company> findById(int id);

    Optional<Company> findByName(String name);

    Optional<Company> findByEmail(String email);

    Optional<Company> findByUserId(int id);

    @Query("select co from Company co join Contract con on co.id=con.company join Invoice inv on con.id=inv.contract where inv.id=:id")
    Company findByInvoiceId(@Param("id") int id);

    Optional<Company> findByCountNumber(long count);

}
