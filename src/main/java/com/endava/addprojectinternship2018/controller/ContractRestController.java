package com.endava.addprojectinternship2018.controller;

import com.endava.addprojectinternship2018.model.Contract;
import com.endava.addprojectinternship2018.model.dto.ContractDto;
import com.endava.addprojectinternship2018.service.CompanyService;
import com.endava.addprojectinternship2018.service.ContractService;
import com.endava.addprojectinternship2018.service.CustomerService;
import com.endava.addprojectinternship2018.service.ProductService;
import com.endava.addprojectinternship2018.util.UserUtil;
import com.endava.addprojectinternship2018.validation.ErrorMessage;
import com.endava.addprojectinternship2018.validation.ValidationResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping(value = "contractRest")
public class ContractRestController {

    private final UserUtil userUtil;
    private final ContractService contractService;

    private static final Logger LOGGER = Logger.getLogger(ContractRestController.class);

    @Autowired
    public ContractRestController(ContractService contractService, UserUtil userUtil) {
        this.contractService = contractService;
        this.userUtil = userUtil;
    }

    @PostMapping(value = "/newContract")
    public @ResponseBody
    ValidationResponse saveNewContract(@Valid @RequestBody ContractDto contractDto,
                                       BindingResult bindingResult) {

        String currentUsername = userUtil.getCurrentUser().getUsername();
        LOGGER.info(String.format("%s: creating new contract...", currentUsername));

        ValidationResponse response = new ValidationResponse();
        response.setStatus("SUCCESS");
        final List<ErrorMessage> errorMessageList = new ArrayList<>();

        if (contractDto.getIssueDate() == null) {
            response.setStatus("FAIL");
            errorMessageList.add(new ErrorMessage("new_issueDate", "cannot be null"));
            response.setErrorMessageList(errorMessageList);
            return response;
        }

        if (contractDto.getExpireDate() == null) {
            response.setStatus("FAIL");
            errorMessageList.add(new ErrorMessage("new_expireDate", "cannot be null"));
            response.setErrorMessageList(errorMessageList);
            return response;
        }

        if (contractDto.getExpireDate().isBefore(contractDto.getIssueDate())) {
            response.setStatus("FAIL");
            errorMessageList.add(new ErrorMessage("new_expireDate", "cannot be more than issue date"));
        }

        if (contractDto.getProductId() == 0) {
            response.setStatus("FAIL");
            errorMessageList.add(new ErrorMessage("new_productSelect", "product must be selected"));
        }

        if (contractDto.getCustomerId() == 0) {
            response.setStatus("FAIL");
            errorMessageList.add(new ErrorMessage("new_customerSelect", "customer must be selected"));
        }

        if (contractDto.getSum() == 0) {
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
                contractDto.getCustomerId(),
                contractDto.getCompanyId(),
                contractDto.getProductId());
        if (optionalContract.isPresent()) {
            if (optionalContract.get().getExpireDate().isAfter(LocalDate.now())) {
                response.setStatus("FAIL");
                errorMessageList.add(new ErrorMessage("new_contract", "Duplicate contract exists!"));
            }
        }

        response.setErrorMessageList(errorMessageList);

        if (response.getStatus().equals("SUCCESS")) {
            contractService.saveContract(contractDto);
        }

        if (!errorMessageList.isEmpty()) {
            for (ErrorMessage errorMessage : errorMessageList) {
                LOGGER.warn(String.format("%s: contract validation error (field:%s message:%s)", currentUsername, errorMessage.getFieldName(), errorMessage.getMessage()));
            }
        }

        return response;
    }

    @RequestMapping(value = "/deleteContract", method = POST)
    public ResponseEntity<?> deleteContract(@RequestParam(name = "contractId") int contractId) {
        LOGGER.info(String.format("%s: attempt to delete contract: %s", userUtil.getCurrentUser().getUsername(), contractId));
        Set<String> deleteResult = contractService.deleteContract(contractId);
        if (deleteResult.contains("OK")) {
            LOGGER.info("Contract was deleted");
            return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
        } else {
            StringBuilder sb = new StringBuilder();
            for (String s : deleteResult) {
                sb.append(s + "\n");
            }
            LOGGER.info("contract can not be deleted: " + sb.toString());
            return new ResponseEntity<>(sb.toString(), HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping(value = "/signContract")
    public String signContract(@RequestParam(name = "contractId") int contractId) {
        return contractService.signContract(contractId);
    }

}
