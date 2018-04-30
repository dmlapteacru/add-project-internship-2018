package com.endava.addprojectinternship2018.controller;

import com.endava.addprojectinternship2018.model.Contract;
import com.endava.addprojectinternship2018.model.Enums.ContractStatus;
import com.endava.addprojectinternship2018.model.Enums.Role;
import com.endava.addprojectinternship2018.model.dto.ContractDto;
import com.endava.addprojectinternship2018.service.CompanyService;
import com.endava.addprojectinternship2018.service.ContractService;
import com.endava.addprojectinternship2018.service.ProductService;
import com.endava.addprojectinternship2018.service.user.UserService;
import com.endava.addprojectinternship2018.util.UserUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Controller
@RequestMapping(value = "contract")
public class ContractController {

    @Autowired
    private ContractService contractService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private UserUtil userUtil;

    @Autowired
    private ProductService productService;

    @GetMapping(value = "createContract")
    public String printContract(@RequestParam(name = "companyId") int companyId,
                                Model model) {
        Role role = userUtil.getCurrentUser().getRole();
        ContractDto contractDto = new ContractDto();
        if (role == Role.CUSTOMER) {
            contractDto.setSelectedCustomer(userUtil.getCurrentCustomer());
            if (companyId != 0) {
                contractDto.setSelectedCompany(companyService.getCompanyById(companyId).get());
            }
            contractDto.setStatus(ContractStatus.SIGNED_BY_CUSTOMER);
        } else if (role == Role.COMPANY) {
            contractDto.setSelectedCompany(userUtil.getCurrentCompany());
            contractDto.setStatus(ContractStatus.SIGNED_BY_COMPANY);
        }
        contractDto.setIssueDate(LocalDateTime.now());
        LocalDateTime endOfCurrentYear = LocalDateTime.of(LocalDate.now().getYear(), 12, 31, 12, 00);
        contractDto.setExpireDate(endOfCurrentYear);
        model.addAttribute("contractDto", contractDto);
        model.addAttribute("update", false);
        return "contract/contractPage";
    }

    @PostMapping(value = "updateContract")
    public String createNewContract(@ModelAttribute ContractDto contractDto,
                                    BindingResult result, Model model) {
        model.addAttribute("contractDto", contractDto);
        contractService.saveContract(contractDto);
        return "redirect:/customer/contracts";
    }

}
