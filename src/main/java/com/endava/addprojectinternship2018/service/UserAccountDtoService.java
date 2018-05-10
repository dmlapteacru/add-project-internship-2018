package com.endava.addprojectinternship2018.service;

import com.endava.addprojectinternship2018.dao.UserAccountDtoDao;
import com.endava.addprojectinternship2018.model.dto.UserAccountDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserAccountDtoService {

    @Autowired
    private UserAccountDtoDao userAccountDtoDao;
    public void save(UserAccountDto userAccountDto){
        userAccountDtoDao.save(userAccountDto);
    }
}
