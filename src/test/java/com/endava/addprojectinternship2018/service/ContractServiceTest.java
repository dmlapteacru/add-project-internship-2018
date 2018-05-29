package com.endava.addprojectinternship2018.service;

import com.endava.addprojectinternship2018.dao.*;
import com.endava.addprojectinternship2018.model.Contract;
import com.endava.addprojectinternship2018.model.User;
import com.endava.addprojectinternship2018.model.enums.ContractStatus;
import com.endava.addprojectinternship2018.model.enums.Role;
import com.endava.addprojectinternship2018.util.UserUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.stubbing.answers.AnswerReturnValuesAdapter;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ContractServiceTest {

    @MockBean
    private UserUtil userUtil;
    @MockBean
    private ContractDao contractDao;
    @Mock
    private InvoiceDao invoiceDao;
    @Mock
    private CompanyDao companyDao;
    @Mock
    private CustomerDao customerDao;
    @Mock
    private ProductDao productDao;
    @MockBean
    private WebSocketDistributeService webSocketDistributeService;

    @InjectMocks
    private ContractService contractServiceTest;

    @Test
    public void testSignContract_whenCustomerSignUnsignedContract() {

        contractServiceTest.setUserUtil(userUtil);

        Contract contractUnderTest = new Contract();
        contractUnderTest.setStatus(ContractStatus.UNSIGNED);
        User userUnderTest = new User();
        userUnderTest.setRole(Role.CUSTOMER);

        when(contractDao.findById(anyInt())).thenReturn(Optional.of(contractUnderTest));
//        when(contractDao.findById(anyInt())).thenAnswer((Answer<Optional<Contract>>)
//                invocationOnMock -> Optional.of(contractUnderTest));
        when(userUtil.getCurrentUser()).thenReturn(userUnderTest);
        //when(userUtil.getCurrentUser()).thenAnswer((Answer<User>) invocationOnMock -> userUnderTest);

        assertTrue(contractServiceTest.signContract(anyInt()).equals("Signed only by Customer"));

    }

    @Test
    public void testGetAllByCompanyId() {
        when(contractDao.findAllByCompanyIdOrderByIssueDate(anyInt())).thenReturn(Collections.emptyList());
        contractServiceTest.getAllByCompanyId(anyInt());
        verify(contractDao).findAllByCompanyIdOrderByIssueDate(anyInt());
    }

    @Test
    public void testGetAllByProductId() {
        when(contractDao.findAllByProductIdOrderByIssueDate(anyInt())).thenReturn(Collections.emptyList());
        contractServiceTest.getAllByProductId(anyInt());
        verify(contractDao).findAllByProductIdOrderByIssueDate(anyInt());
    }

    @Test
    public void testGetAllByCustomerId() {
        when(contractDao.findAllByCustomerIdOrderByIssueDate(anyInt())).thenReturn(Collections.emptyList());
        contractServiceTest.getAllByCustomerId(anyInt());
        verify(contractDao).findAllByCustomerIdOrderByIssueDate(anyInt());
    }

    @Test
    public void testCountByCompanyAndStatus() {

//        when(contractDao.countByCompanyIdAndStatus(anyInt(), mock(ContractStatus.class))).thenReturn(anyLong());
//        contractServiceTest.countByCompanyAndStatus(anyInt(), mock(ContractStatus.class));
//        verify(contractDao).countByCompanyIdAndStatus(anyInt(), mock(ContractStatus.class));
    }

    @Test
    public void testCountByCustomerAndStatus() {
    }

    @Test
    public void testDeleteContract() {
    }
}