package com.endava.addprojectinternship2018.dao;

import com.endava.addprojectinternship2018.model.Company;
import com.endava.addprojectinternship2018.model.User;
import com.endava.addprojectinternship2018.model.dto.UserWithProfileDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserDao extends JpaRepository<User, Integer> {

    Optional<User> findUserByUsername(String username);

    @Query("select new com.endava.addprojectinternship2018.model.dto.UserWithProfileDto(a.role," +
            "case when co.id is not null then co.name when cu.id is not null then cu.firstName else null end," +
            "a.username, " +
            "case when co.id is not null then co.email when cu.id is not null then cu.email else null end, a.userStatus)" +
            "from User a " +
            "left join Company co on a.id = co.user " +
            "left join Customer cu on a.id = cu.user")
    List<UserWithProfileDto> findAllUsersWithProfile();
}
