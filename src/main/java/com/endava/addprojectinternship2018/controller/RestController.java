package com.endava.addprojectinternship2018.controller;

import com.endava.addprojectinternship2018.dao.InvoiceDao;
import com.endava.addprojectinternship2018.model.Company;
import com.endava.addprojectinternship2018.model.Contract;
import com.endava.addprojectinternship2018.model.Customer;
import com.endava.addprojectinternship2018.model.Invoice;
import com.endava.addprojectinternship2018.service.CompanyService;
import com.endava.addprojectinternship2018.service.ContractService;
import com.endava.addprojectinternship2018.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@org.springframework.web.bind.annotation.RestController
public class RestController {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ContractService contractService;

    @Autowired
    private InvoiceDao invoiceDao;

    // REST Companies

    @GetMapping("/rest/company/getAll")
    public List<Company> getAllCompanies(){
        return companyService.getAllCompanies();
    }

    @GetMapping("/rest/company/getByName/{name}")
    public Company getCompanyByUsername(@PathVariable String name){
        return companyService.getCompanyByName(name);
    }

    @GetMapping("/rest/company/getById/{id}")
    public Company getCompanyById(@PathVariable Integer id){
        return companyService.getCompanyById(id);
    }

    @GetMapping("/rest/company/getByEmail/{email}")
    public Company getCompanyByEmail(@PathVariable String email){
        return companyService.getCompanyByEmail(email);
    }


    //  -----     REST Customer
    @GetMapping("rest/customer/ById/{id}")
    public Customer getCustomerById(@PathVariable int id){
        return customerService.getCustomerById(id);
    }


    // -----      REST Contracts
    @GetMapping("rest/contracts/byCustomerName/{customername}")
    public List<Contract> getContractsByCustomer(String customername){
        return contractService.getContractsByCompanyName(customername);
    }

    // -----      REST Invoices

    @GetMapping("rest/invoice/getAll")
    public List<Invoice> getAllInvoices(){
        return invoiceDao.findAll();
    }





}
