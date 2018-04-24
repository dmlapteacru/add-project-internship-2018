package com.endava.addprojectinternship2018.service;

import com.endava.addprojectinternship2018.dao.CompanyDao;
import com.endava.addprojectinternship2018.model.Company;
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

    public List<Company> getAllCompanies(){
        return companyDao.findAll();
    }

    public Company getCompanyById(int id) {
        return companyDao.findById(id);
    }

    public Company getCompanyByEmail(String email){
        return companyDao.findByEmail(email);
    }

    public Company getCompanyByName(String name){
        return companyDao.findByName(name);
    }


}
