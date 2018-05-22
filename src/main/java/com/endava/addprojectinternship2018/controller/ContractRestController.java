package com.endava.addprojectinternship2018.controller;

import com.endava.addprojectinternship2018.model.Contract;
import com.endava.addprojectinternship2018.model.dto.ContractDto;
import com.endava.addprojectinternship2018.model.dto.ContractDtoTest;
import com.endava.addprojectinternship2018.model.enums.ContractStatus;
import com.endava.addprojectinternship2018.service.CompanyService;
import com.endava.addprojectinternship2018.service.ContractService;
import com.endava.addprojectinternship2018.service.CustomerService;
import com.endava.addprojectinternship2018.service.ProductService;
import com.endava.addprojectinternship2018.util.UserUtil;
import com.endava.addprojectinternship2018.validation.ErrorMessage;
import com.endava.addprojectinternship2018.validation.ValidationResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping(value = "contractRest")
public class ContractRestController {

    @Autowired
    private UserUtil userUtil;

    private final ContractService contractService;
    private final CustomerService customerService;
    private final CompanyService companyService;
    private final ProductService productService;

    private static final Logger LOGGER = Logger.getLogger(ContractRestController.class);

    @Autowired
    public ContractRestController(ContractService contractService, ProductService productService,
                                  CompanyService companyService, CustomerService customerService) {
        this.contractService = contractService;
        this.productService = productService;
        this.companyService = companyService;
        this.customerService = customerService;
    }

    @PostMapping(value = "/newContract")
    public @ResponseBody
    ValidationResponse saveNewContract(@Valid @RequestBody ContractDtoTest contractDtoTest,
                                       BindingResult bindingResult) {

        String currentUsername = userUtil.getCurrentUser().getUsername();
        LOGGER.info(String.format("%s: creating new contract...", currentUsername));

        ValidationResponse response = new ValidationResponse();
        response.setStatus("SUCCESS");
        final List<ErrorMessage> errorMessageList = new ArrayList<>();

        if (contractDtoTest.getIssueDate() == null) {
            response.setStatus("FAIL");
            errorMessageList.add(new ErrorMessage("new_issueDate", "cannot be null"));
            response.setErrorMessageList(errorMessageList);
            return response;
        }

        if (contractDtoTest.getExpireDate() == null) {
            response.setStatus("FAIL");
            errorMessageList.add(new ErrorMessage("new_expireDate", "cannot be null"));
            response.setErrorMessageList(errorMessageList);
            return response;
        }

        if (contractDtoTest.getExpireDate().isBefore(contractDtoTest.getIssueDate())) {
            response.setStatus("FAIL");
            errorMessageList.add(new ErrorMessage("new_expireDate", "cannot be more than issue date"));
        }

        if (contractDtoTest.getProductId() == 0) {
            response.setStatus("FAIL");
            errorMessageList.add(new ErrorMessage("new_productSelect", "product must be selected"));
        }

        if (contractDtoTest.getCustomerId() == 0) {
            response.setStatus("FAIL");
            errorMessageList.add(new ErrorMessage("new_customerSelect", "customer must be selected"));
        }

        if (contractDtoTest.getSum() == 0) {
            response.setStatus("FAIL");
            errorMessageList.add(new ErrorMessage("new_sum", "cannot be null"));
        }

        if (bindingResult.hasErrors()) {
            response.setStatus("FAIL");
            bindingResult.getFieldErrors().stream()
                    .map(fieldError -> new ErrorMessage(fieldError.getField(), fieldError.getDefaultMessage()))
                    .forEach(errorMessageList::add);
        }

        Optional<Contract> optionalContract = contractService.getByCustomerIdCompanyIdProductId(
                contractDtoTest.getCustomerId(),
                contractDtoTest.getCompanyId(),
                contractDtoTest.getProductId());
        if (optionalContract.isPresent()) {
            if (optionalContract.get().getExpireDate().isAfter(LocalDate.now())) {
                response.setStatus("FAIL");
                errorMessageList.add(new ErrorMessage("new_contract", "Duplicate contract exists!"));
            }
        }

        response.setErrorMessageList(errorMessageList);

        if (response.getStatus().equals("SUCCESS")) {
            ContractDto contractDto = new ContractDto();
            contractDto.setSelectedProduct(productService.getById(contractDtoTest.getProductId()));
            contractDto.setSelectedCustomer(customerService.getCustomerById(contractDtoTest.getCustomerId()).get());
            contractDto.setSelectedCompany(companyService.getCompanyById(contractDtoTest.getCompanyId()).get());
            contractDto.setSum(contractDtoTest.getSum());
            contractDto.setIssueDate(contractDtoTest.getIssueDate());
            contractDto.setExpireDate(contractDtoTest.getExpireDate());
            contractDto.setStatus(ContractStatus.valueOf(contractDtoTest.getStatus()));
            contractService.saveContract(contractDto);
            LOGGER.info(String.format("%s: creating new contract... SUCCESS", currentUsername));
        }

        if (!errorMessageList.isEmpty()) {
            for (ErrorMessage errorMessage : errorMessageList) {
                LOGGER.warn(String.format("%s: contract validation error (field:%s message:%s)", currentUsername, errorMessage.getFieldName(), errorMessage.getMessage()));
            }
        }

        return response;
    }

    @RequestMapping(value = "/deleteContract", method = POST)
    public String deleteContract(@RequestParam(name = "contractId") int contractId) {
        LOGGER.info(String.format("%s: attempt to delete contract: %s", userUtil.getCurrentUser().getUsername(), contractId));
        return contractService.deleteContract(contractId);
    }

    @PostMapping(value = "/signContract")
    public String signContract(@RequestParam(name = "contractId") int contractId) {
        return contractService.signContract(contractId);
    }

}
