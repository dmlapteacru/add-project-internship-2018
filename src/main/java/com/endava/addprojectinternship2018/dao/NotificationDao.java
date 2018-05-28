package com.endava.addprojectinternship2018.dao;

import com.endava.addprojectinternship2018.model.Notification;
import com.endava.addprojectinternship2018.model.enums.NotificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationDao extends JpaRepository<Notification, Integer>{
    List<Notification> findAllByStatusAndUserToOrderByDateAsc(NotificationStatus status, String userTo);
    List<Notification> findAllByUserToOrderByDateDesc(String username);
}
