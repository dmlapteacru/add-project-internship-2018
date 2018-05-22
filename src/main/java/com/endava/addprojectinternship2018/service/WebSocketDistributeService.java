package com.endava.addprojectinternship2018.service;

import com.endava.addprojectinternship2018.model.Notification;
import com.endava.addprojectinternship2018.model.enums.NotificationCase;
import com.endava.addprojectinternship2018.model.enums.NotificationStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WebSocketDistributeService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private ContractService contractService;

    public void sendNewContractNotification(String userTo){
        notificationService.save(new Notification(NotificationCase.NEW_CONTRACT, "New contract was offered to you.", userTo, contractService.getLastContract().getId()));
        messagingTemplate.convertAndSendToUser(userTo, "/queue/messages", "NOTIFICATION");
    }

    public void sendSignContractNotification(String userTo, int contractId){
        notificationService.save(new Notification(NotificationCase.CONTRACT_SIGNED, "Contract was signed.", userTo, contractId));
        messagingTemplate.convertAndSendToUser(userTo, "/queue/messages", "NOTIFICATION");
    }

    public void sendNewInvoiceNotification(String userTo, int invoiceId){
        notificationService.save(new Notification(NotificationCase.NEW_INVOICE, "New invoice was sent to you..", userTo, invoiceId));
        messagingTemplate.convertAndSendToUser(userTo, "/queue/messages", "NOTIFICATION");
    }
    public void sendNewInvoicePaidNotification(String userTo, int invoiceId){
        notificationService.save(new Notification(NotificationCase.INVOICE_PAID, "Inovice was paid.", userTo, invoiceId));
        messagingTemplate.convertAndSendToUser(userTo, "/queue/messages", "NOTIFICATION");
    }
    public void sendNewAdminMessageNotification(String email, int messageId){
        notificationService.save(new Notification(NotificationCase.NEW_MESSAGE, "New message from - " + email, "admin", messageId));
        messagingTemplate.convertAndSendToUser("admin", "/queue/messages", "NOTIFICATION");
    }
}
