package com.endava.addprojectinternship2018.dao;

import com.endava.addprojectinternship2018.model.User;
import com.endava.addprojectinternship2018.model.dto.UserBankAccountDto;
import com.endava.addprojectinternship2018.model.dto.UserWithProfileDto;
import com.endava.addprojectinternship2018.model.dto.UserEmailDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserDao extends JpaRepository<User, Integer> {

    Optional<User> findUserByUsername(String username);

    @Query("select new com.endava.addprojectinternship2018.model.dto.UserWithProfileDto(a.role," +
            "case when co.id is not null then co.name when cu.id is not null then concat(concat(cu.firstName, ' ') , cu.lastName) else null end," +
            "a.username, " +
            "case when co.id is not null then co.email when cu.id is not null then cu.email else null end, a.userStatus)" +
            "from User a " +
            "left join Company co on a.id = co.user " +
            "left join Customer cu on a.id = cu.user order by a.id desc")
    List<UserWithProfileDto> findAllUsersWithProfile();

    @Query("select new com.endava.addprojectinternship2018.model.dto.UserEmailDto(" +
            "case when co.id is not null then co.email when cu.id is not null then cu.email else null end) " +
            "from User a " +
            "left join Company co on a.id = co.user " +
            "left join Customer cu on a.id=cu.user where a.username=:username")
    public UserEmailDto findUsersEmailByUsername(@Param("username") String username);

    @Query("select new com.endava.addprojectinternship2018.model.dto.UserBankAccountDto(" +
            "case when co.id is not null then co.countNumber when cu.id is not null then cu.countNumber else null end," +
            " case when co.id is not null then co.accessKey when cu.id is not null then cu.accessKey else null end) " +
            "from User a " +
            "left join Company co on a.id=co.user " +
            "left join Customer cu on a.id=cu.user " +
            "where a.username=:username")
    public UserBankAccountDto findUserBankAccountByUsername(@Param("username") String username);
}