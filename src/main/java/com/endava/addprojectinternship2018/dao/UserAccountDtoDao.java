package com.endava.addprojectinternship2018.dao;

import com.endava.addprojectinternship2018.model.dto.UserAccountDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAccountDtoDao extends JpaRepository<UserAccountDto, Integer>{
}
