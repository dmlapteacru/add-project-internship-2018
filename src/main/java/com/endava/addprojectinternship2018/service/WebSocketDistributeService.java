package com.endava.addprojectinternship2018.service;

import com.endava.addprojectinternship2018.controller.ContractRestController;
import com.endava.addprojectinternship2018.model.Notification;
import com.endava.addprojectinternship2018.model.enums.NotificationCase;
import com.endava.addprojectinternship2018.model.enums.NotificationStatus;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class WebSocketDistributeService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private ContractService contractService;

    @Autowired
    private UserService userService;

    private static final Logger LOGGER = Logger.getLogger(WebSocketDistributeService.class);

    public void sendNewContractNotification(String userTo, String userFrom){
        notificationService.save(new Notification(NotificationCase.NEW_CONTRACT, "New contract was offered to you", userTo, contractService.getLastContract().getId(), userFrom));
        messagingTemplate.convertAndSendToUser(userService.getUserByUsername(userTo).get().getSocketToken(), "/queue/messages", "NOTIFICATION");
    }

    public void sendSignContractNotification(String userTo, int contractId, String userFrom){
        notificationService.save(new Notification(NotificationCase.CONTRACT_SIGNED, "Contract was signed", userTo, contractId, userFrom));
        messagingTemplate.convertAndSendToUser(userService.getUserByUsername(userTo).get().getSocketToken(), "/queue/messages", "NOTIFICATION");
        LOGGER.info(userService.getUserByUsername(userTo).get().getSocketToken());
    }

    public void sendNewInvoiceNotification(String userTo, int invoiceId, String userFrom){
        notificationService.save(new Notification(NotificationCase.NEW_INVOICE, "New invoice was sent to you", userTo, invoiceId, userFrom));
        messagingTemplate.convertAndSendToUser(userService.getUserByUsername(userTo).get().getSocketToken(), "/queue/messages", "NOTIFICATION");
    }
    public void sendNewInvoicePaidNotification(String userTo, int invoiceId, String userFrom){
        notificationService.save(new Notification(NotificationCase.INVOICE_PAID, "Inovice was paid", userTo, invoiceId, userFrom));
        messagingTemplate.convertAndSendToUser(userService.getUserByUsername(userTo).get().getSocketToken(), "/queue/messages", "NOTIFICATION");
    }
    public void sendNewAdminMessageNotification(String email, int messageId, String userFrom){
        notificationService.save(new Notification(NotificationCase.NEW_MESSAGE, "New message from - " + email, "admin", messageId, userFrom));
        messagingTemplate.convertAndSendToUser(userService.getUserByUsername("admin").get().getSocketToken(), "/queue/messages", "NOTIFICATION");
    }
    public String generateSocketToken(){
        String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder uniqueID = new StringBuilder();
        Random random = new Random();
        while (uniqueID.length() < 18) {
            int index = (int) (random.nextFloat() * CHARS.length());
            uniqueID.append(CHARS.charAt(index));
        }
        String token = uniqueID.toString();
        return token;
    }
}
