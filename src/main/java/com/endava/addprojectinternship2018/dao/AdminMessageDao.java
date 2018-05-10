package com.endava.addprojectinternship2018.dao;

import com.endava.addprojectinternship2018.model.AdminMessage;
import com.endava.addprojectinternship2018.model.enums.AdminMessagesStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminMessageDao extends JpaRepository<AdminMessage, Integer>{

    List<AdminMessage> findAllByOrderByDateDesc();
    void deleteById(int id);
    List<AdminMessage> getAllByStatusOrderByDateDesc(AdminMessagesStatus adminMessagesStatus);
    default List<AdminMessage> getAllByStatus_Unread(){
        return getAllByStatusOrderByDateDesc(AdminMessagesStatus.UNREAD);
    }
    default List<AdminMessage> getAllByStatus_Read(){
        return getAllByStatusOrderByDateDesc(AdminMessagesStatus.READ);
    }
}
