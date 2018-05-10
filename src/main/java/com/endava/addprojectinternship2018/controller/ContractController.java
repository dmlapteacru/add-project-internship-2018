package com.endava.addprojectinternship2018.controller;

import com.endava.addprojectinternship2018.dao.CompanyDao;
import com.endava.addprojectinternship2018.dao.CustomerDao;
import com.endava.addprojectinternship2018.dao.ProductDao;
import com.endava.addprojectinternship2018.model.Company;
import com.endava.addprojectinternship2018.model.User;
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

import javax.validation.Valid;
import java.time.LocalDate;

@Controller
@RequestMapping(value = "contract")
public class ContractController {

    @Autowired
    private ContractService contractService;

    @Autowired
    private UserUtil userUtil;

    @GetMapping(value = "createContract")
    public String getContractPageForCreate(@RequestParam(name = "customerId") int customerId,
                                           @RequestParam(name = "companyId") int companyId,
                                           @RequestParam(name = "productId") int productId,
                                           Model model) {
        ContractDto contractDto = contractService.createNewContractDto(customerId, companyId, productId);
        model.addAttribute("contractDto", contractDto);
        model.addAttribute("update", false);
        return "contract/contractPage";
    }

    @GetMapping(value = "updateContract")
    public String getContractPageForUpdate(@RequestParam(name = "contractId") int contractId,
                                           Model model) {
        ContractDto contractDto = contractService.createUpdateContractDto(contractId);
        model.addAttribute("contractDto", contractDto);
        model.addAttribute("update", true);
        return "contract/contractPage";
    }

    @GetMapping(value = "deleteContractOld")
    public String deleteContract(@RequestParam(name = "contractId") int contractId,
                                 Model model) {
        Role currentUserRole = userUtil.getCurrentUser().getRole();
        String controllerName = currentUserRole == Role.CUSTOMER ? "customer" : "company";
        String deleteResult = contractService.deleteContract(contractId);
        if (deleteResult.equals("OK")) {
            return "redirect:/" + controllerName + "/contracts?successDelete";
        } else {
            model.addAttribute("errorMessage", deleteResult);
            return "redirect:/" + controllerName + "/contracts?error";
        }
    }

    @PostMapping(value = "saveContract")
    public String saveContract(@ModelAttribute(name = "contractDto") @Valid ContractDto contractDto,
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
            model.addAttribute("contractDto", contractDto);
            return "contract/contractPage";
        }

        if (contractDto.getIssueDate().isAfter(contractDto.getExpireDate())) {
            model.addAttribute("contractDto", contractDto);
            result.rejectValue("expireDate", "Issue date can not be more then Expire date!");
            return "contract/contractPage";
        }

        contractService.saveContract(contractDto);

        return resultString;
    }

}
