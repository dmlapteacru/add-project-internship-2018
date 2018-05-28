package com.endava.addprojectinternship2018.service;


import com.endava.addprojectinternship2018.dao.InvoiceDao;
import com.endava.addprojectinternship2018.model.Contract;
import com.endava.addprojectinternship2018.model.Customer;
import com.endava.addprojectinternship2018.model.Invoice;
import com.endava.addprojectinternship2018.model.User;
import com.endava.addprojectinternship2018.model.enums.InvoiceStatus;
import org.junit.Before;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.Mockito.times;

//@RunWith(SpringRunner.class)
public class InvoiceServiceTest {

    private InvoiceService invoiceService;
    private InvoiceDao invoiceDao;
    private WebSocketDistributeService webSocketDistributeService;
    @Before
    public void init(){
        invoiceDao = Mockito.mock(InvoiceDao.class);
        webSocketDistributeService = Mockito.mock(WebSocketDistributeService.class);
        invoiceService=new InvoiceService(invoiceDao,null,webSocketDistributeService,null);
    }


    @Test
    public void testChangeInvoiceStatusToSent() throws Exception {
        Invoice invoiceFromDb=new Invoice();
        invoiceFromDb.setIssueDate(LocalDate.now().minusDays(15));
        invoiceFromDb.setStatus(InvoiceStatus.ISSUED);
        Contract contract=new Contract();
        Customer customer=new Customer();
        User user =new User();
        user.setUsername("login132");
        customer.setUser(user);
        contract.setCustomer(customer);
        invoiceFromDb.setContract(contract);
        Mockito.when(invoiceDao.findById(10)).thenReturn(Optional.of(invoiceFromDb));
        Invoice expectedParam=new Invoice();
        expectedParam.setIssueDate(LocalDate.now().minusDays(15));
        expectedParam.setStatus(InvoiceStatus.SENT);
        expectedParam.setContract(contract);
        //invoice.getContract().getCustomer().getUser().getUsername(),
       // expectedParam.setContract(contract);
        invoiceService.changeInvoiceStatusToSent(10);
        Mockito.verify(invoiceDao,times(1)).save(expectedParam);
        Mockito.verify(webSocketDistributeService,times(1)).sendNewInvoiceNotification("login132",10, "");
    }
}
