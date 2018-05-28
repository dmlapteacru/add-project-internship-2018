package com.endava.addprojectinternship2018.service;

import com.endava.addprojectinternship2018.dao.NotificationDao;
import com.endava.addprojectinternship2018.model.Notification;
import com.endava.addprojectinternship2018.model.enums.NotificationStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationDao notificationDao;

    public void save(Notification notification){
        notificationDao.save(notification);
    }

    public List<Notification> getAllByStatusUnread(String userTo){
     return notificationDao.findAllByStatusAndUserToOrderByDateAsc(NotificationStatus.UNREAD, userTo);
    }

    public List<Notification> getAllByRecipient(String username){
        List<Notification> notifications = notificationDao.findAllByUserToOrderByDateDesc(username);
        return notifications;
    }

    public void changeNotificationStatusOnRead(int id){
        Notification notification = notificationDao.getOne(id);
        notification.setStatus(NotificationStatus.READ);
        notificationDao.save(notification);
    }
}
