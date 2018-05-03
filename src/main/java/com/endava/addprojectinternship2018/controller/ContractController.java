package com.endava.addprojectinternship2018.controller;

import com.endava.addprojectinternship2018.model.Company;
import com.endava.addprojectinternship2018.model.enums.ContractStatus;
import com.endava.addprojectinternship2018.model.enums.Role;
import com.endava.addprojectinternship2018.model.dto.ContractDto;
import com.endava.addprojectinternship2018.service.CompanyService;
import com.endava.addprojectinternship2018.service.ContractService;
import com.endava.addprojectinternship2018.service.ProductService;
import com.endava.addprojectinternship2018.util.UserUtil;
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
    public String getContractPageForCreate(@RequestParam(name = "companyId") int companyId,
                                           Model model) {
        Role currentUserRole = userUtil.getCurrentUser().getRole();
        ContractDto contractDto = new ContractDto();
        if (currentUserRole == Role.CUSTOMER) {
            contractDto.setSelectedCustomer(userUtil.getCurrentCustomer());
            if (companyId != 0) {
                contractDto.setSelectedCompany(companyService.getCompanyById(companyId).get());
                contractDto.setProducts(productService.getAllByCompanyId(companyId));
            } else {
                contractDto.setCompanies(companyService.getAllCompanies());
                contractDto.setProducts(productService.getAllProducts());
            }
            contractDto.setStatus(ContractStatus.SIGNED_BY_CUSTOMER);
        } else if (currentUserRole == Role.COMPANY) {
            Company currentCompany = userUtil.getCurrentCompany();
            contractDto.setSelectedCompany(currentCompany);
            contractDto.setStatus(ContractStatus.SIGNED_BY_COMPANY);
            contractDto.setProducts(productService.getAllByCompanyId(currentCompany.getId()));
        }
        contractDto.setIssueDate(LocalDateTime.now());
        LocalDateTime endOfCurrentYear = LocalDateTime.of(LocalDate.now().getYear(), 12, 31, 12, 00);
        contractDto.setExpireDate(endOfCurrentYear);
        model.addAttribute("contractDto", contractDto);
        model.addAttribute("update", false);
        return "contract/contractPage";
    }

    @GetMapping(value = "updateContract")
    public String getContractPageForUpdate(@RequestParam(name = "contractId") int contractId,
                                           Model model) {
        ContractDto contractDto = contractService.convertContractToContractDto(contractService.getContractById(contractId));
        contractDto.setProducts(productService.getAllProducts());
        model.addAttribute("contractDto", contractDto);
        model.addAttribute("update", true);
        return "contract/contractPage";
    }

    @GetMapping(value = "deleteContract")
    public String deleteContract(@RequestParam(name = "contractId") int contractId,
                                 Model model) {
        Role currentUserRole = userUtil.getCurrentUser().getRole();
        String controllerName = currentUserRole == Role.CUSTOMER ? "customer" : "company";
        String deleteResult = contractService.deleteContract(contractId);
        if (deleteResult.equals("OK")) {
            return "redirect:/"+controllerName+"/contracts?successDelete";
        } else {
            model.addAttribute("errorMessage", deleteResult);
            return "redirect:/"+controllerName+"/contracts?error";
        }
    }

    @PostMapping(value = "saveContract")
    public String saveContract(@ModelAttribute(name = "contractDto") ContractDto contractDto,
                               BindingResult result, Model model) {

        String resultString;
        if (contractDto.getContractId() == 0) {
            model.addAttribute("update", false);
            resultString = "redirect:/contract/createContract?companyId=" + contractDto.getSelectedCompany().getId() + "&success";
        } else {
            model.addAttribute("update", true);
            resultString = "redirect:/contract/updateContract?contractId=" + contractDto.getContractId() + "&success";
        }
        if (result.hasErrors()) {
            return "contract/contractPage";
        }
        if (contractDto.getIssueDate().isAfter(contractDto.getExpireDate())) {
            result.rejectValue("expireDate", "Issue date can not be more then Expire date!");
            return "contract/contractPage";
        }
        if (contractDto.getSum() < 0) {
            result.rejectValue("sum", "The sum can not be negative!");
            return "contract/contractPage";
        }

        contractService.saveContract(contractDto);

        return resultString;
    }

}
