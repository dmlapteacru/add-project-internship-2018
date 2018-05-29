package com.endava.addprojectinternship2018.controller;

import com.endava.addprojectinternship2018.model.Company;
import com.endava.addprojectinternship2018.model.Notification;
import com.endava.addprojectinternship2018.model.dto.CompanyDtoLight;
import com.endava.addprojectinternship2018.service.CompanyService;
import com.endava.addprojectinternship2018.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "companyRest")
public class CompanyRestController {

    private final CompanyService companyService;
    private final NotificationService notificationService;

    @Autowired
    public CompanyRestController(CompanyService companyService, NotificationService notificationService) {
        this.companyService = companyService;
        this.notificationService = notificationService;
    }

    @GetMapping(value = "/getNameById")
    public String getCompanyNameById(@RequestParam(name = "companyId") int companyId) {
        return companyService.getCompanyById(companyId)
                .map(Company::getName).orElse("");
    }

    @GetMapping(value = "getAllIdAndName")
    public List<CompanyDtoLight> getAllCompany() {
        return companyService.getAllCompanies().stream()
                .map(company -> new CompanyDtoLight(company.getId(), company.getName()))
                .collect(Collectors.toCollection(LinkedList::new));
    }

    @GetMapping(value = "notifications/view")
    public List<Notification> showNotificationByPages(@RequestParam(value = "page") int page){
        return notificationService.getByRecipientAndPages(page);
    }
}
