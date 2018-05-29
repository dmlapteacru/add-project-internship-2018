package com.endava.addprojectinternship2018.service;

import com.endava.addprojectinternship2018.dao.NotificationDao;
import com.endava.addprojectinternship2018.model.Notification;
import com.endava.addprojectinternship2018.model.enums.NotificationStatus;
import com.endava.addprojectinternship2018.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationDao notificationDao;

    @Autowired
    private UserUtil userUtil;

    public void save(Notification notification){
        notificationDao.save(notification);
    }

    public List<Notification> getAllByStatusUnread(String userTo){
     return notificationDao.findAllByStatusAndUserToOrderByDateAsc(NotificationStatus.UNREAD, userTo);
    }

    public List<Notification> getByRecipientAndPages(int page){
        Pageable top = new PageRequest(page,6);
        List<Notification> notifications = notificationDao.findAllByUserToOrderByDateDesc(userUtil.getCurrentUser().getUsername(),
                                                                                        top);
        return notifications;
    }

    public void changeNotificationStatusOnRead(int id){
        Notification notification = notificationDao.getOne(id);
        notification.setStatus(NotificationStatus.READ);
        notificationDao.save(notification);
    }
}
