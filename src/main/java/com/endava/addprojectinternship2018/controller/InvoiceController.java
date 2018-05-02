package com.endava.addprojectinternship2018.controller;

import com.endava.addprojectinternship2018.model.Company;
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
    public String showNewInvoice(Model model) {
        model.addAttribute("invoice", new Invoice());
        Company company = userUtil.getCurrentCompany();
        model.addAttribute("listOfContracts", contractService
                .getContractsByCompanyName(company.getName()));
        model.addAttribute("productList", productService.getAllProducts());
        return "invoice/newInvoice";
    }

    @PostMapping(value = "createNew")
    public String createNewInvoice(@ModelAttribute InvoiceDto invoiceDto, BindingResult result) {
        invoiceService.saveDto(invoiceDto);
        return "redirect:/company/invoices";
    }

    @GetMapping(value = "deleteInvoice")
    public String deleteUserByUsername(@RequestParam("invoiceId") int invoiceId) {
        Invoice invoice = null;
        invoiceService.deleteInvoice(invoiceId);
        return "redirect:/company/contracts";
    }

}
