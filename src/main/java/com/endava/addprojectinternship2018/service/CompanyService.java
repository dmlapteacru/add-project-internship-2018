package com.endava.addprojectinternship2018.service;

import com.endava.addprojectinternship2018.dao.CompanyDao;
import com.endava.addprojectinternship2018.model.Company;
import com.endava.addprojectinternship2018.model.dto.CompanyDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CompanyService {

    @Autowired
    private CompanyDao companyDao;

    @Autowired
    private UserService userService;

    public List<Company> getAllCompanies() {
        return companyDao.findAll();
    }

    public Optional<Company> getCompanyById(int id) {
        return companyDao.findById(id);
    }

    public Optional<Company> getCompanyByEmail(String email) {
        return companyDao.findByEmail(email);
    }

    public Optional<Company> getCompanyByName(String name) {
        return companyDao.findByName(name);
    }

    public Optional<Company> getCompanyByUserId(int id) {
        return companyDao.findByUserId(id);
    }

    public Company saveCompany(CompanyDto companyDto) {
        return companyDao.save(convertCompanyDtoToCompany(companyDto));
    }

    public void save(Company company){
        companyDao.save(company);
    }

    public CompanyDto convertCompanyToCompanyDto(Company company) {
        CompanyDto companyDto = new CompanyDto();
        companyDto.setName(company.getName());
        companyDto.setCompanyId(company.getId());
        companyDto.setEmail(company.getEmail());
        companyDto.setCountNumber(company.getCountNumber());
        companyDto.setAccessKey(company.getAccessKey());
        companyDto.setUserDto(userService.convertUserToUserDto(company.getUser()));
        return companyDto;
    }

    public Company convertCompanyDtoToCompany(CompanyDto companyDto) {
        Company company = companyDao.findById(companyDto.getCompanyId())
                .orElseGet(Company::new);
        company.setName(companyDto.getName());
        company.setEmail(companyDto.getEmail());
        company.setAccessKey(companyDto.getAccessKey());
        company.setCountNumber(companyDto.getCountNumber());
        company.setUser(userService.convertUserDtoToUser(companyDto.getUserDto()));
        return company;
    }

    public Company getCompanyByInvoiceId(int id){
      return companyDao.findByInvoiceId(id);
    }
}
