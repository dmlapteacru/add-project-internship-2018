package com.endava.addprojectinternship2018.controller;

import com.endava.addprojectinternship2018.model.Contract;
import com.endava.addprojectinternship2018.model.Invoice;
import com.endava.addprojectinternship2018.model.dto.InvoiceEditDto;
import com.endava.addprojectinternship2018.model.dto.InvoiceSaveNewDto;
import com.endava.addprojectinternship2018.service.ContractService;
import com.endava.addprojectinternship2018.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "restinvoice")
public class InvoiceRestController {

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private ContractService contractService;

    @PostMapping(value = "/editInvoice")
    public ResponseEntity editNewInvoiceFrom(@RequestBody @Valid InvoiceEditDto dto, BindingResult result){
    //TODO check if this invoice is of that company
        //TODO get invoice by id ,change data that comes from modal dialog
        //TODO persist this entity


        if (result.hasErrors()){
            new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }else{
            Invoice invoice = invoiceService.getInvoiceById(dto.getId());
            invoice.setDueDate(dto.getDueDate());
            invoice.setSum(dto.getSum());
            invoiceService.save(invoice);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping(value = "/createInvoice")
    public ResponseEntity createNewInvoiceFrom(@RequestBody @Valid InvoiceSaveNewDto dto, BindingResult result){
        //TODO check if this invoice is of that company
        //TODO get invoice by id ,change data that comes from modal dialog
        //TODO persist this entity


        System.out.println(dto);

        if (result.hasErrors()){
            new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }else{

            Invoice invoice = new Invoice();

            invoice.setIssueDate(dto.getIssueDate());
            invoice.setDueDate(dto.getDueDate());
            invoice.setStatus(dto.getStatus());
            invoice.setContract(contractService.getById(dto.getContractId()));

            System.out.println(contractService.getById(dto.getContractId()));
            invoice.setSum(dto.getSum());
            invoiceService.save(invoice);

        }
        return new ResponseEntity(HttpStatus.OK);
    }

}
