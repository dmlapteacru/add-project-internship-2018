package com.endava.addprojectinternship2018.service;

import com.endava.addprojectinternship2018.dao.AdminMessageDao;
import com.endava.addprojectinternship2018.model.AdminMessage;
import com.endava.addprojectinternship2018.model.enums.AdminMessagesStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class AdminMessageService {

    @Autowired
    private AdminMessageDao adminMessageDao;

    public void save(AdminMessage adminMessage){
        Date date = new Date();
        adminMessage.setDate(date);
        adminMessageDao.save(adminMessage);
    }
    public void deleteById(int id){
        adminMessageDao.deleteById(id);
    }
    public List<AdminMessage> getAllMessages(){
        return adminMessageDao.findAllByOrderByDateDesc();
    }
    public List<AdminMessage> getAllMessagesByStatusUnread(){
        return adminMessageDao.getAllByStatus_Unread();
    }
    public List<AdminMessage> getAllMessagesByStatusRead(){
        return adminMessageDao.getAllByStatus_Read();
    }
    public void changeMessageStatus(int id){
        AdminMessage adminMessage = adminMessageDao.getOne(id);
        if (adminMessage.getStatus()== AdminMessagesStatus.UNREAD){
            adminMessage.setStatus(AdminMessagesStatus.READ);
        } else adminMessage.setStatus(AdminMessagesStatus.UNREAD);
        adminMessageDao.save(adminMessage);
    }
}
