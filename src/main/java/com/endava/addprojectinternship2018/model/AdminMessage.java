package com.endava.addprojectinternship2018.model;

import com.endava.addprojectinternship2018.model.enums.AdminMessagesStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "ADMIN_MESSAGES")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    @Column
    private String message;

    @Column
    private String user_email;

    @Column
    @Enumerated(value = EnumType.STRING)
    private AdminMessagesStatus status = AdminMessagesStatus.UNREAD;

    public AdminMessage(String message, String user_email) {
        this.message = message;
        this.user_email = user_email;
    }
}
