package com.endava.addprojectinternship2018.controller;

import com.endava.addprojectinternship2018.model.Contract;
import com.endava.addprojectinternship2018.model.Invoice;
import com.endava.addprojectinternship2018.model.dto.InvoiceEditDto;
import com.endava.addprojectinternship2018.model.dto.InvoiceSaveNewDto;
import com.endava.addprojectinternship2018.service.ContractService;
import com.endava.addprojectinternship2018.service.InvoiceService;
import com.endava.addprojectinternship2018.validation.ErrorMessage;
import com.endava.addprojectinternship2018.validation.ValidationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "restinvoice")
public class InvoiceRestController {

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private ContractService contractService;

    @PostMapping(value = "/editInvoice")
    public ResponseEntity editNewInvoiceFrom(@RequestBody @Valid InvoiceEditDto dto, BindingResult result) {
        //TODO check if this invoice is of that company
        //TODO get invoice by id ,change data that comes from modal dialog



        if (result.hasErrors()) {
            new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            Invoice invoice = invoiceService.getInvoiceById(dto.getId());
            invoice.setDueDate(dto.getDueDate());
            invoice.setSum(dto.getSum());
            invoiceService.save(invoice);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping(value = "/createInvoice")
    public ResponseEntity createNewInvoiceFrom(@RequestBody @Valid InvoiceSaveNewDto dto, BindingResult result) {
        //TODO check if this invoice is of that company
        //TODO get invoice by id ,change data that comes from modal dialog
        //TODO persist this entity

        boolean success = true;

        Contract contract = contractService.getById(dto.getContractId());
        if (contract == null) {
            success = false;
        }

        List<Invoice> list = invoiceService.getInvoiceInPeriodService(dto.getContractId(), dto.getIssueDate());

        for (Invoice inv : list
                ) {
            System.out.println(inv);
        }

        Invoice invoice = new Invoice();

        if (result.hasErrors()) {
            success = false;
        }

        if (success) {
            invoice.setIssueDate(dto.getIssueDate());
            invoice.setDueDate(dto.getDueDate());
            invoice.setStatus(dto.getStatus());
            invoice.setContract(contract);
            invoice.setSum(dto.getSum());

            return new ResponseEntity(HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping(value = "/newInvoice")
    public @ResponseBody
    ValidationResponse saveNewInvoice(@Valid @RequestBody InvoiceSaveNewDto dto,
                                      BindingResult bindingResult) {
        ValidationResponse response = new ValidationResponse();
        response.setStatus("SUCCESS");
        final List<ErrorMessage> errorMessageList = new ArrayList<>();
        List<Invoice> list = invoiceService.getInvoiceInPeriodService(dto.getContractId(), dto.getIssueDate());
        Invoice invoice = new Invoice();

        Contract currentContract = contractService.getById(dto.getContractId());
        if (currentContract == null) {
            response.setStatus("FAIL");
            errorMessageList.add(new ErrorMessage("issue_inv_date", "Contract with id " + dto.getContractId() + " not found"));
        }

        if (!list.isEmpty()) {
            response.setStatus("FAIL");
            errorMessageList.add(new ErrorMessage("issue_inv_date", "Contract for active period already exists !"));
        }

        if (dto.getDueDate() == null) {
            response.setStatus("FAIL");
            errorMessageList.add(new ErrorMessage("expire_inv_date", "Cannot be null"));
        }

        if (response.getStatus().equals("SUCCESS")) {
            invoice.setIssueDate(dto.getIssueDate());
            invoice.setDueDate(dto.getDueDate());
            invoice.setStatus(dto.getStatus());
            invoice.setSum(dto.getSum());
            invoice.setContract(currentContract);
            invoiceService.save(invoice);
        }

        response.setErrorMessageList(errorMessageList);

        return response;
    }

    @PostMapping(value = "/sendToCustomer")
    public String sendInvoiceToCustomer(@RequestParam int invoiceId) {
        System.out.println("invoice_d:   " + invoiceId);
        System.out.println("In sentToCustomer kkk");
        invoiceService.changeInvoiceStatusToSent(invoiceId);
        return "Ok";
    }

    @PostMapping(value = "/sendBulkToCustomer")
    public String sendBulkInvoiceToCustomer(@RequestBody Map<String, Object> data) {

        List<String> list = (List<String>) data.get("invoiceIDS");
        list.forEach(s -> System.out.println(s));
        List<Integer> invoiceBulkIds = list.stream().map(Integer::parseInt).collect(Collectors.toList());
        System.out.println("Size of the list:  " + invoiceBulkIds.size());
        invoiceBulkIds.forEach(integer -> System.out.println(integer));
        for (int id : invoiceBulkIds
                ) {
            invoiceService.changeInvoiceStatusToSent(id);
        }
        return "Ok";
    }

    @PostMapping(value = "/deleteInvoice")
    public String deleteInvoice(@RequestParam int invoiceId) {
        System.out.println("Invoice_ID to be deleted  " + invoiceId);
        invoiceService.deleteInvoice(invoiceId);
        return "Ok";
    }

}