package com.endava.addprojectinternship2018.dao;

import com.endava.addprojectinternship2018.model.Contract;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ContractDaoTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ContractDao contractDao;

    @Test
    public void testFindById() {
        //Contract contract = entityManager;

    }

    @Test
    public void testFindByCustomerIdAndCompanyIdAndProductId() {
    }

    @Test
    public void testFindAllByCompanyIdOrderByIssueDate() {
    }

    @Test
    public void testFindAllByCustomerIdOrderByIssueDate() {
    }

    @Test
    public void testFindAllByCompanyIdAndSumBetweenAndIssueDateBetweenOrderByIssueDate() {
    }

    @Test
    public void testFindAllByCustomerIdAndSumBetweenAndIssueDateBetweenOrderByIssueDate() {
    }

    @Test
    public void testFindAllByCustomerIdAndStatusAndSumBetweenAndIssueDateBetweenOrderByIssueDate() {
    }

    @Test
    public void testFindAllByCompanyIdAndStatusAndSumBetweenAndIssueDateBetweenOrderByIssueDate() {
    }
}