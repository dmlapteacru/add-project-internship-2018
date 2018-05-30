package com.endava.addprojectinternship2018.dao;

import com.endava.addprojectinternship2018.model.Contract;
import com.endava.addprojectinternship2018.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ContractDaoTest {

    @MockBean
    private UserService userService;

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ContractDao contractDao;

    @Test
    public void testFindById() {

    }

    @Test
    public void testSave() {
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