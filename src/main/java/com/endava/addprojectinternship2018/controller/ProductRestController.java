package com.endava.addprojectinternship2018.controller;

import com.endava.addprojectinternship2018.model.Product;
import com.endava.addprojectinternship2018.model.dto.ProductDto;
import com.endava.addprojectinternship2018.service.ProductService;
import com.endava.addprojectinternship2018.util.UserUtil;
import com.endava.addprojectinternship2018.validation.ErrorMessage;
import com.endava.addprojectinternship2018.validation.ValidationResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping(value = "productRest")
public class ProductRestController {

    private UserUtil userUtil;

    private final ProductService productService;
    private static final Logger LOGGER = Logger.getLogger(ProductRestController.class);
    private static final String PATTERN = "(.*<\\s*script\\s*>.*)|(.*alert\\s*\\(\\s*\\).*)";

    @Autowired
    public ProductRestController(ProductService productService) {
        this.productService = productService;
    }

    @Autowired
    public void setUserUtil(UserUtil userUtil) {
        this.userUtil = userUtil;
    }

    @RequestMapping(value = "/pdfExport", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> getPriceList(@RequestParam(name = "products") int[] products) {

        ByteArrayInputStream bis = productService.getPriceList(products);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=ServicePriceList.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }

    @RequestMapping(value = "/updateProduct", method = POST)
    public @ResponseBody
    ValidationResponse updateService(@RequestBody ProductDto productDto) {

        ValidationResponse response = new ValidationResponse();
        response.setStatus("SUCCESS");
        final List<ErrorMessage> errorMessageList = new ArrayList<>();

        if (productDto.getDescription().matches(PATTERN)) {
            response.setStatus("FAIL");
            errorMessageList.add(new ErrorMessage("upd_service_desc", "contains illegal characters"));
            LOGGER.error(String.format("Product:%s description validating error", productDto.getProductId()));
        }

        response.setErrorMessageList(errorMessageList);

        if (response.getStatus().equals("SUCCESS")) {
            productService.save(productDto);
            LOGGER.info(String.format("Product:%s was updated", productDto.getProductId()));
        }

        return response;
    }

    @RequestMapping(value = "/newProduct", method = POST)
    public @ResponseBody
    ValidationResponse saveNewService(@RequestBody @Valid ProductDto productDto,
                                      BindingResult bindingResult) {

        ValidationResponse response = new ValidationResponse();
        response.setStatus("SUCCESS");
        final List<ErrorMessage> errorMessageList = new ArrayList<>();

        LOGGER.info(String.format("%s attempts to create new service '%s'", userUtil.getCurrentUser().getUsername(), productDto.getName()));

        if (productDto.getCategoryId() == 0) {
            response.setStatus("FAIL");
            errorMessageList.add(new ErrorMessage("select_category", "must not be empty"));
        }

        if (productDto.getName() == null || productDto.getName().isEmpty()) {
            response.setStatus("FAIL");
            errorMessageList.add(new ErrorMessage("new_service_name", "must not be empty"));
        }

        if (productDto.getName().matches(PATTERN)) {
            response.setStatus("FAIL");
            errorMessageList.add(new ErrorMessage("new_service_name", "contains illegal characters"));
        }

        if (productDto.getDescription().matches(PATTERN)) {
            response.setStatus("FAIL");
            errorMessageList.add(new ErrorMessage("new_service_desc", "contains illegal characters"));
        }

        if (productDto.getPrice() <= 0) {
            response.setStatus("FAIL");
            errorMessageList.add(new ErrorMessage("new_service_price", "must be more than 0"));
        }

        Optional<Product> optionalProduct = productService.getByNameAndCategoryIdAndCompanyId(
                productDto.getName(),
                productDto.getCategoryId(),
                productDto.getCompanyId());
        if (optionalProduct.isPresent()) {
            response.setStatus("FAIL");
            errorMessageList.add(new ErrorMessage("new_service_name", "service name exists"));
        }

        if (bindingResult.hasErrors()) {
            response.setStatus("FAIL");
            bindingResult.getFieldErrors().stream()
                    .map(fieldError -> new ErrorMessage(fieldError.getField(), fieldError.getDefaultMessage()))
                    .forEach(errorMessageList::add);
        }

        response.setErrorMessageList(errorMessageList);

        if (response.getStatus().equals("SUCCESS")) {
            productService.save(productDto);
            LOGGER.info(String.format("product '%s' was created", productDto.getName()));
        }
        if (!errorMessageList.isEmpty()) {
            for (ErrorMessage errorMessage : errorMessageList) {
                LOGGER.warn(String.format("product validation error (field:%s message:%s)", errorMessage.getFieldName(), errorMessage.getMessage()));
            }
        }

        return response;
    }

    @RequestMapping(value = "/deleteProduct", method = POST)
    public ResponseEntity<?> deleteProduct(@RequestParam(name = "productId") int productId) {
        LOGGER.info(String.format("%s attempts to delete service: %s", userUtil.getCurrentUser().getUsername(), productId));
        Set<String> deleteResult = productService.deleteProduct(productId);
        if (deleteResult.contains("OK")) {
            LOGGER.info("Service was deleted");
            return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
        } else {
            StringBuilder sb = new StringBuilder();
            for (String s : deleteResult) {
                sb.append(s + "\n");
            }
            LOGGER.info("Can not delete: " + sb.toString());
            return new ResponseEntity<>(sb.toString(), HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping(value = "/getAllByCompanyId")
    public Map<Integer, String> getAllByCompanyId(@RequestParam(name = "companyId") int companyId) {

        return productService.getAllByCompanyId(companyId)
                .stream()
                .collect(Collectors
                        .toMap(Product::getId, Product::getName, (a, b) -> b));

    }

    @GetMapping(value = "/getPriceById")
    public double getPriceById(@RequestParam(name = "productId") int productId) {
        return productService.getById(productId).getPrice();
    }


}
