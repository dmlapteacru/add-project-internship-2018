package com.endava.addprojectinternship2018.controller;


import com.endava.addprojectinternship2018.model.Notification;
import com.endava.addprojectinternship2018.model.dto.InvoiceDescriptionPaymentDto;
import com.endava.addprojectinternship2018.model.dto.NotificationDto;
import com.endava.addprojectinternship2018.model.enums.NotificationCase;
import com.endava.addprojectinternship2018.model.enums.NotificationStatus;
import com.endava.addprojectinternship2018.service.AdminMessageService;
import com.endava.addprojectinternship2018.service.NotificationService;
import com.endava.addprojectinternship2018.service.UserService;
import com.endava.addprojectinternship2018.testWS.User;
import com.endava.addprojectinternship2018.testWS.UserResponse;
import org.aspectj.weaver.ast.Not;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
public class WebSocketController {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private AdminMessageService adminMessageService;

    @MessageMapping("/admin")
    public void getNotificationMessage(NotificationDto notificationDto) {

        Notification notification = new Notification();
        if (notificationDto.getNotificationCase().equals("NEW_MESSAGE")){
            notification.setNotificationCase(NotificationCase.NEW_MESSAGE);
            notification.setIdSearch(adminMessageService.getAdminMessageByEmail(notificationDto.getContent().split(": ")[1]).getId());
        }
        notification.setContent(notificationDto.getContent());
        notification.setUserTo(notificationDto.getUserTo());
        notificationService.save(notification);
        messagingTemplate.convertAndSendToUser(notificationDto.getUserTo(),"/queue/messages", notificationDto);
    }

}