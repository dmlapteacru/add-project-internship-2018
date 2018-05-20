package com.endava.addprojectinternship2018.controller;

import com.endava.addprojectinternship2018.dao.InvoiceTransactionDao;
import com.endava.addprojectinternship2018.model.Company;
import com.endava.addprojectinternship2018.model.enums.InvoiceStatus;
import com.endava.addprojectinternship2018.model.Invoice;
import com.endava.addprojectinternship2018.model.dto.InvoiceDto;
import com.endava.addprojectinternship2018.service.ContractService;
import com.endava.addprojectinternship2018.service.InvoiceService;
import com.endava.addprojectinternship2018.service.ProductService;
import com.endava.addprojectinternship2018.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping(value = "invoice")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private UserUtil userUtil;

    @Autowired
    private ContractService contractService;

    @Autowired
    private ProductService productService;

    @GetMapping(value = "createNew")
    public String showNewInvoice(Model model){
        InvoiceDto invoiceDto = new InvoiceDto(LocalDate.now(), InvoiceStatus.ISSUED);
        model.addAttribute("invoice", invoiceDto);
        Company company = userUtil.getCurrentCompany();
        model.addAttribute("listOfContracts", contractService
                .getAllByCompanyName(company.getName()));
        return "invoice/newInvoice";
}

    @PostMapping(value = "createNew")
    public String createNewInvoice( @ModelAttribute("invoice") @Valid InvoiceDto invoiceDto, BindingResult bindingResult,
                                   Model model ){
        System.out.println(invoiceDto);

        boolean success = true;

        if (bindingResult.hasErrors()){
            model.addAttribute("invoice", invoiceDto);
            Company company = userUtil.getCurrentCompany();
            model.addAttribute("listOfContracts", contractService
                    .getAllByCompanyName(company.getName()));
            System.out.println(bindingResult.getAllErrors());
            return "invoice/newInvoice";
        }


        if (!success){
            return "invoice/newInvoice";
        }

        invoiceService.saveInvoiceDto(invoiceDto);
        return "redirect:/company/invoices";


    }

    @GetMapping(value = "deleteInvoice")
    public String deleteInvoiceById(@RequestParam("id") int invoiceId){
        invoiceService.deleteInvoice(invoiceId);
        return "redirect:/company/invoices";
    }

    @Deprecated
    @GetMapping(value = "updateInvoice")
    public String updateInvoiceById(Model model, @RequestParam("id") int invoiceId){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        InvoiceDto invoiceDto = invoiceService.convertInvoiceToInvoiceDto(invoiceService.getInvoiceById(invoiceId));
        model.addAttribute("formatter",formatter);
        model.addAttribute("invoice", invoiceDto);
        model.addAttribute("invoiceList", invoiceService.getAllInvoices());
        Company company = userUtil.getCurrentCompany();
        List<Invoice> invoiceList = invoiceService.getInvoicesByCompany(company.getName());
        return "invoice/updateInvoice";

        //todo verify id invoice if mine
    }

    @GetMapping(value = "changeStatus")
    public String changeInvoiceStatus(Model model, @RequestParam(value = "id") int id){
       invoiceService.changeInvoiceStatus(id);
       Invoice invoice = invoiceService.getInvoiceById(id);
       model.addAttribute("invoice", invoice);
       model.addAttribute("invoiceList", invoiceService.getAllInvoices());
       model.addAttribute("id", id);
        return "invoice/updateInvoice";
    }

    @GetMapping(value = "filterByActiveStatus")
    public String showInvoicesByStatus(Model model,@RequestParam(required = false) InvoiceStatus status){
        Company company = userUtil.getCurrentCompany();
        
        //TODO Limit shown invoices ,show just companies invoices
        if(status==null){
            model.addAttribute("invoices", invoiceService
                    .getAllInvoices());
        }else {
            model.addAttribute("invoices", invoiceService
                    .getInvoicesByStatus(status));
        }
        return "invoice/invoicesByCompany";
    }

    @GetMapping("/test")
    public List getTest(){

        return dao.getInvoiceDtoExt(1,1,InvoiceStatus.ACTIVE);
    }
    @Autowired
    InvoiceTransactionDao dao=new InvoiceTransactionDao();
}
