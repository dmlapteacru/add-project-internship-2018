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

import javax.validation.Valid;

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
        model.addAttribute("invoice", new InvoiceDto());
        Company company = userUtil.getCurrentCompany();
        model.addAttribute("listOfContracts", contractService
                .getContractsByCompanyName(company.getName()));
        model.addAttribute("productList", productService.getAllProducts());
        return "invoice/newInvoice";
}

    @PostMapping(value = "createNew")
    public String createNewInvoice(@ModelAttribute @Valid InvoiceDto invoiceDto, BindingResult bindingResult
                                    , Model model){
        System.out.println(invoiceDto);

        if (bindingResult.hasErrors()){
            model.addAttribute("invoice", invoiceDto);
            Company company = userUtil.getCurrentCompany();
            model.addAttribute("listOfContracts", contractService
                    .getContractsByCompanyName(company.getName()));
            model.addAttribute("productList", productService.getAllProducts());
            return "invoice/newInvoice";
        }
        invoiceService.saveDto(invoiceDto);
        return "redirect:/company/invoices";
    }

    @GetMapping(value = "deleteInvoice")
    public String deleteInvoiceById(@RequestParam("id") int invoiceId){
        invoiceService.deleteInvoice(invoiceId);
        return "redirect:/company/invoices";
    }

}
