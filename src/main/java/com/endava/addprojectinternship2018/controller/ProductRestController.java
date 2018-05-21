package com.endava.addprojectinternship2018.controller;

import com.endava.addprojectinternship2018.model.Product;
import com.endava.addprojectinternship2018.service.ProductService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "productRest")
public class ProductRestController {

    private final ProductService productService;
    private static final Logger LOGGER = Logger.getLogger(ProductRestController.class);

    @Autowired
    public ProductRestController(ProductService productService) {
        this.productService = productService;
    }

    @RequestMapping(value = "/pdfExport", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> getPriceList(@RequestParam(name = "companyId") int companyId) {

        ByteArrayInputStream bis = productService.getPriceList(true, new ArrayList<>());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=ServicePriceList.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
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
