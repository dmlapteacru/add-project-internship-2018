package com.endava.addprojectinternship2018.controller;

import com.endava.addprojectinternship2018.model.Customer;
import com.endava.addprojectinternship2018.model.Role;
import com.endava.addprojectinternship2018.model.User;
import com.endava.addprojectinternship2018.model.dto.ContractDto;
import com.endava.addprojectinternship2018.service.CompanyService;
import com.endava.addprojectinternship2018.service.ContractService;
import com.endava.addprojectinternship2018.service.CustomerService;
import com.endava.addprojectinternship2018.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "contract")
public class ContractController {

    @Autowired
    private UserUtil userUtil;

    @Autowired
    private ContractService contractService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private CustomerService customerService;

    @GetMapping(value = "createNew")
    public String showNewContract(Model model) {
        if (userUtil.getCurrentUser().getRole() == Role.CUSTOMER) {
            model.addAttribute("owner", userUtil.getCurrentCustomer());
            model.addAttribute("listOfAllCompanies", companyService.getAllCompanies());
        } else {
            model.addAttribute("owner", userUtil.getCurrentCompany());
            model.addAttribute("listOfAllCustomers", customerService.getAllCustomers());
        }
        return "contract/newContractPage";
    }

    @PostMapping
    public String createNewContract(@ModelAttribute ContractDto contractDto,
                                    BindingResult result, Model model) {
        contractService.createNewContract(contractDto);
        return "";
    }
}
